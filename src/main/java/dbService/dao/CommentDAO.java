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
        List<CommentsDataSet> list = criteria
                .add(Restrictions.eq("image", image))
                .list();
        return list;
    }

    public void insert(CommentsDataSet commentsDataSet) throws HibernateException {
        session.save(commentsDataSet);
    }

    public CommentsDataSet delete(String cid, UsersDataSet user) throws HibernateException {
        Criteria criteria = session.createCriteria(CommentsDataSet.class);
        CommentsDataSet comment = ((CommentsDataSet) criteria
                .add(Restrictions.eq("cid", cid))
                .add(Restrictions.eq("user", user))
                .uniqueResult());
        session.delete(comment);
        return comment;
    }
}
