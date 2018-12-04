package dbService.dao;

import DTO.RatingDTO;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.RatingDataSet;
import dbService.DataServices.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.List;

public class RatingDAO {
    private Session session;

    public RatingDAO(Session session) {
        this.session = session;
    }

    public List<RatingDataSet> get(ImageDataSet image) throws HibernateException {
        Criteria criteria = session
                .createCriteria(RatingDataSet.class)
                .add(Restrictions.eq("image", image.getId()));
        List<RatingDataSet> list = criteria.list();
        return list;
    }

    public void upsertRating(UsersDataSet user, ImageDataSet image, int rating) {
        RatingDataSet ratingDataSet = new RatingDataSet(user, image, rating);
        session.saveOrUpdate(ratingDataSet);
    }

    public HashMap<String, Number> getAverageRating(UsersDataSet user, ImageDataSet image) {
        Query query = session.createQuery(
                (new StringBuilder())
                        .append("SELECT COUNT(*), AVG(value) FROM RatingDataSet")
                        .append(" WHERE image = ")
                        .append(image.getId())
                        .toString()
        );
        Object[] res = (Object[]) query.uniqueResult();
        HashMap<String, Number> result = new HashMap<>();
        Long count = (Long) res[0];
        if (count == null) {
            count = 0L;
        }
        result.put("count", count);
        Double average = (Double) res[1];
        if (average == null) {
            average = 0.0;
        }
        result.put("average", average);

        Criteria criteria = session.createCriteria(RatingDataSet.class);
        criteria.add(Restrictions.eq("user", user.getId()));
        criteria.add(Restrictions.eq("image", image.getId()));
        RatingDataSet userRating = (RatingDataSet) criteria.uniqueResult();
        if (userRating != null) {
            result.put("user", (int) userRating.getValue());
        } else {
            result.put("user", 0);
        }

        return result;
    }
}
