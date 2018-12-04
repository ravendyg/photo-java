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

    public boolean delete(String iid, UsersDataSet user) {
        Criteria criteria = session.createCriteria(ImageDataSet.class);
        ImageDataSet image = ((ImageDataSet) criteria
                .add(Restrictions.eq("iid", iid))
                .uniqueResult());
        if (image.getUploadedBy().getId() == user.getId()) {
            session.delete(image);
            return true;
        }
        return false;
    }
}
