package dbService;

import DTO.ImageDTO;
import DTO.RatingDTO;
import Data.PhotoRequest;
import dbService.DataServices.*;
import dbService.dao.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "validate";

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

    public UsersDataSet getUserid(long id) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UserDAO dao = new UserDAO(session);
            UsersDataSet dataSet = dao.getById(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UsersDataSet getUserByName(String name) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UserDAO dao = new UserDAO(session);
            UsersDataSet dataSet = dao.getByName(name);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UsersDataSet getUserByUid(String uid) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UserDAO dao = new UserDAO(session);
            UsersDataSet dataSet = dao.getByUid(uid);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void createUser(UsersDataSet usersDataSet) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDAO dao = new UserDAO(session);
            dao.insertUser(usersDataSet);
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

    public List<ImageDTO> getPhotos(UsersDataSet user) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO imageDAO = new ImageDAO(session);
            CommentDAO commentDAO = new CommentDAO(session);
            RatingDAO ratingDAO = new RatingDAO(session);
            List<ImageDataSet> images = imageDAO.get();
            List<ImageDTO> imageDTOs = new ArrayList<>();
            // think about a way to optimize this
            for (ImageDataSet image : images) {
                List<CommentsDataSet> commentsDataSets = commentDAO.getByImage(image);
                HashMap<String, Number> ratings = ratingDAO.getAverageRating(user, image);
                List<RatingDataSet> ratingDataSets = ratingDAO.get(image);
                imageDTOs.add(new ImageDTO(image, commentsDataSets, ratings));
            }
            transaction.commit();
            session.close();
            return imageDTOs;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void createComment(
            UsersDataSet user,
            String cid,
            String text,
            Long image
    ) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            CommentDAO commentDAO = new CommentDAO(session);
            CommentsDataSet commentsDataSet = new CommentsDataSet(cid, text, user, image);
            commentDAO.insert(commentsDataSet);
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addPhoto(PhotoRequest photoRequest) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO dao = new ImageDAO(session);
            ImageDataSet image = new ImageDataSet(photoRequest);
            long id = dao.add(image);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public boolean deletePhoto(String iid, UsersDataSet user) throws  DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO imageDAO = new ImageDAO(session);
            return imageDAO.delete(iid, user);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public ViewDataSet addView(UsersDataSet user, String iid) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ViewDAO viewDAO = new ViewDAO(session);
            ImageDAO imageDAO = new ImageDAO(session);
            ImageDataSet image = imageDAO.get(iid);
            ViewDataSet view = new ViewDataSet(user, image);
            viewDAO.add(view);
            transaction.commit();
            session.close();
            return view;
        } catch (HibernateException e) {
            int code = ((SQLException) e.getCause()).getErrorCode();
            if (code == 1062) {
                return null;
            } else {
                throw new DBException(e);
            }
        }
    }

    public RatingDTO upsertRating(UsersDataSet user, String iid, int rating) throws DBException{
        RatingDTO ratingDTO = null;

        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO imageDAO = new ImageDAO(session);
            RatingDAO ratingDAO = new RatingDAO(session);
            ImageDataSet image = imageDAO.get(iid);
            ratingDAO.upsertRating(user, image, rating);
            HashMap<String, Number> ratings = ratingDAO.getAverageRating(user, image);
            ratingDTO = new RatingDTO(
                    user,
                    image,
                    ratings
            );
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return ratingDTO;
    }

    private Configuration getMysqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(ImageDataSet.class);
        configuration.addAnnotatedClass(CommentsDataSet.class);
        configuration.addAnnotatedClass(ViewDataSet.class);
        configuration.addAnnotatedClass(RatingDataSet.class);

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
