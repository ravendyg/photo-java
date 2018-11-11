package dbService.dao;

import dbService.DataServices.ImageDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class ImageDAO {
    private Session session;

    public ImageDAO(Session session) {
        this.session = session;
    }

    public List<ImageDataSet> get() throws HibernateException {

        Criteria criteria = session.createCriteria(ImageDataSet.class);
        List<ImageDataSet> list = criteria.list();
        return list;
    }

    public long add(ImageDataSet image) throws HibernateException {
        return (long) session.save(image);
    }
}
