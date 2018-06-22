package dbService.dao;

import dbService.DataServices.ImageDataSet;
import dbService.DataServices.RatingDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class RatingDAO {
    private Session session;

    public RatingDAO(Session session) { this.session  = session; }

    public List<RatingDataSet> get(ImageDataSet image) throws HibernateException {
        Criteria criteria = session.createCriteria(RatingDataSet.class);
        List list = criteria
                .add(Restrictions.eq("image", image.getId()))
                .list();
        return ((List<RatingDataSet>) list);
    }
}
