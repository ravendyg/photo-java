package dbService.dao;

import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class CommentDAO {
    private Session session;

    public CommentDAO (Session session) { this.session = session; }

    public List<CommentsDataSet> getByImage(ImageDataSet image) throws HibernateException {
        Criteria criteria = session.createCriteria(CommentsDataSet.class);
        List list = criteria
                .add(Restrictions.eq("image", image.getId()))
                .list();
        return ((List<CommentsDataSet>) list);
    }

    public CommentsDataSet insert(
            UsersDataSet user,
            String text,
            Long image
    ) throws HibernateException {
        CommentsDataSet comment = new CommentsDataSet(text, user, image);
        session.save(comment);
        return comment;
    }
}
