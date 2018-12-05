package dbService.dao;

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

    public HashMap<String, String> getAverageRating(UsersDataSet user, ImageDataSet image) {
        Query query = session.createQuery(
                (new StringBuilder())
                        .append("SELECT AVG(value) FROM RatingDataSet")
                        .append(" WHERE image = ")
                        .append(image.getId())
                        .toString()
        );
        HashMap<String, String> result = new HashMap<>();
        Double average = (Double) query.uniqueResult();
        if (average == null) {
            average = new Double(0);
        }
        String averageStr = average.toString();
        result.put("average", averageStr);

        Criteria criteria = session.createCriteria(RatingDataSet.class);
        criteria.add(Restrictions.eq("user", user.getId()));
        criteria.add(Restrictions.eq("image", image.getId()));
        RatingDataSet userRating = (RatingDataSet) criteria.uniqueResult();
        if (userRating != null) {
            result.put("user", new Integer(userRating.getValue()).toString());
        } else {
            result.put("user", "0");
        }

        return result;
    }
}
