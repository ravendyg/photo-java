package dbService;

import dbService.DataServices.ImageDataSet;
import dbService.DataServices.SessionsDataSet;
import dbService.DataServices.UsersDataSet;
import dbService.dao.ImageDAO;
import dbService.dao.SessionDAO;
import dbService.dao.UserDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getMysqlConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public void printConnectInfo() {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UsersDataSet getUser(long id) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UserDAO dao = new UserDAO(session);
            UsersDataSet dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UsersDataSet getUser(String name) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UserDAO dao = new UserDAO(session);
            UsersDataSet dataSet = dao.get(name);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UsersDataSet createUser(String name, String password) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDAO dao = new UserDAO(session);
            UsersDataSet usersDataSet = dao.insetUser(name, password);
            transaction.commit();
            session.close();
            return usersDataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public SessionsDataSet getSession(String cookie) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            SessionDAO dao = new SessionDAO(session);
            SessionsDataSet sessionsDataSet = dao.getSession(cookie);
            session.close();

            return sessionsDataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UsersDataSet getUserBySession(String cookie) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            SessionDAO dao = new SessionDAO(session);
            UsersDataSet usersDataSet = dao.getUser(cookie);

            return usersDataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void createSession(UsersDataSet user, String cookie) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            SessionDAO dao = new SessionDAO(session);
            dao.createSession(user, cookie);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void deleteSession(SessionsDataSet sessionsDataSet) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(sessionsDataSet);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void deleteUser(UsersDataSet user) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<ImageDataSet> getUserPhotos(UsersDataSet user) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO dao = new ImageDAO((session));
            List<ImageDataSet> images = dao.get(user);
            transaction.commit();
            session.close();
            return images;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addPhoto(UsersDataSet user, String src) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO dao = new ImageDAO(session);
            // title and description will be updated later
            ImageDataSet image = new ImageDataSet(user, src, "", "");
            long id = dao.add(image);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    private Configuration getMysqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(SessionsDataSet.class);
        configuration.addAnnotatedClass(ImageDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);

        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}
