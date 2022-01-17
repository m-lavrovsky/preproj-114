package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT" +
                                    ", name VARCHAR(40) NOT NULL, lastname VARCHAR(40), age TINYINT NOT NULL)")
                    .addEntity(User.class).executeUpdate();
        }  catch (Exception e) {
            System.out.println("DB connection or SQL command createUsersTable failed:");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS `users`").executeUpdate();
        }
        catch (Exception e) {
            System.out.println("DB connection or SQL command dropUsersTable failed:");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            /*Transaction tx = */session.beginTransaction();
            session.save(new User(name,lastName,age));

            /*User user = new User(name, lastName, age);
            String hql = "insert into User (name, lastName, age) " +
                    "select user.name, user.lastName, user.age from User user";
            session.createQuery(hql).executeUpdate();
            tx.commit();*/

            System.out.printf("User с именем – %s %s добавлен в базу данных\n",name,lastName);
        }
        catch (Exception e) {
            System.out.println("DB connection or SQL command saveUser failed:");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();

            Query query = session.createQuery("DELETE User WHERE id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        }  catch (Exception ex) {
            System.out.println("DB connection or SQL command removeUserById failed:");
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            result = session.createQuery("FROM User").list();

        }  catch (Exception e) {
            System.out.println("DB connection or SQL command getAllUsers failed:");
            System.out.println(e.getMessage());

        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            //session.createSQLQuery("TRUNCATE `users`").executeUpdate();

            session.createQuery("delete User").executeUpdate();
        }  catch (Exception ex) {
            System.out.println("DB connection or SQL command cleanUsersTable failed:");
            System.out.println(ex.getMessage());
        }
    }
}
