package dbService;

import DTO.CommentDTO;
import DTO.ImageDTO;
import DTO.RatingDTO;
import Data.PhotoRequest;
import Helpers.Utils;
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
    private final Utils utils;

    public DBService(Utils utils) {
        this.utils = utils;
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

    public void createUser(String login, String password) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDAO dao = new UserDAO(session);
            String uid = utils.getUid();
            dao.insertUser(uid, login, password);
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
            for (ImageDataSet image : images) {
                List<CommentsDataSet> commentsDataSets = commentDAO.getByImage(image);
                HashMap<String, String> ratings = ratingDAO.getAverageRating(user, image);
                imageDTOs.add(new ImageDTO(image, commentsDataSets, ratings));
            }
            transaction.commit();
            session.close();
            return imageDTOs;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    private ImageDTO getPhotoMetadata(
            Session session,
            UsersDataSet user,
            ImageDataSet image
    ) throws DBException {
        try {
            CommentDAO commentDAO = new CommentDAO(session);
            RatingDAO ratingDAO = new RatingDAO(session);
            List<CommentsDataSet> commentsDataSets = commentDAO.getByImage(image);
            HashMap<String, String> ratings = ratingDAO.getAverageRating(user, image);
            ImageDTO dto = new ImageDTO(image, commentsDataSets, ratings);
            return dto;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public CommentsDataSet createComment(
            UsersDataSet user,
            String iid,
            String text
    ) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO imageDAO = new ImageDAO(session);
            CommentDAO commentDAO = new CommentDAO(session);
            String cid = utils.getUid();
            ImageDataSet image = imageDAO.get(iid);
            CommentsDataSet comment = new CommentsDataSet(cid, text, user, image);
            commentDAO.insert(comment);
            transaction.commit();
            session.close();
            return comment;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<CommentsDataSet> getComments(String iid) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO imageDAO = new ImageDAO(session);
            ImageDataSet image = imageDAO.get(iid);
            CommentDAO commentDAO = new CommentDAO(session);
            List<CommentsDataSet> comments = commentDAO.getByImage(image);
            transaction.commit();
            session.close();
            return comments;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public CommentsDataSet deleteComment(String cid, UsersDataSet user) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            CommentDAO commentDAO = new CommentDAO(session);
            CommentsDataSet comment = commentDAO.delete(cid, user);
            transaction.commit();
            session.close();
            return comment;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public ImageDTO addPhoto(UsersDataSet user, PhotoRequest photoRequest) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO dao = new ImageDAO(session);
            ImageDataSet image = new ImageDataSet(photoRequest);
            dao.add(image);
            ImageDTO result = getPhotoMetadata(session, user, image);
            transaction.commit();
            session.close();
            return result;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public ImageDataSet deletePhoto(String iid, UsersDataSet user) throws  DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ImageDAO imageDAO = new ImageDAO(session);
            ImageDataSet image = imageDAO.delete(iid, user);
            transaction.commit();
            session.close();
            return image;
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
            HashMap<String, String> ratings = ratingDAO.getAverageRating(user, image);
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
