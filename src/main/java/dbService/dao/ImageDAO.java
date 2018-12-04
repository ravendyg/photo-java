package dbService.dao;

import dbService.DataServices.ImageDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ImageDAO {
    private Session session;

    public ImageDAO(Session session) {
        this.session = session;
    }

    public List<ImageDataSet> get() throws HibernateException {
        Criteria criteria = session.createCriteria(ImageDataSet.class);
        List<ImageDataSet> list = criteria.list();
        list.sort((im1, im2) -> im2.getUploaded().compareTo(im1.getUploaded()));
        return list;
    }

    public ImageDataSet get(String iid) throws HibernateException {
        Criteria criteria = session.createCriteria(ImageDataSet.class);
        return ((ImageDataSet) criteria
                .add(Restrictions.eq("iid", iid))
                .uniqueResult());
    }

    public long add(ImageDataSet image) throws HibernateException {
        return (long) session.save(image);
    }
}
