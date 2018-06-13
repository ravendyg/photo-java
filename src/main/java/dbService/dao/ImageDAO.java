package dbService.dao;

import dbService.DataServices.ImageDataSet;
import dbService.DataServices.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ImageDAO {
    private Session session;

    public ImageDAO(Session session) { this.session = session; }

    public List<ImageDataSet> get(UsersDataSet uploadedBy) throws HibernateException {
        Criteria criteria = session.createCriteria(ImageDataSet.class);
        return ((List<ImageDataSet>) criteria
                .add(Restrictions.eq("uploaded_by", uploadedBy))
                .list());
    }

    public long add(ImageDataSet image) throws HibernateException {
        return (long) session.save(image);
    }
}
