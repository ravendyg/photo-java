package dbService.dao;

import dbService.DataServices.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UserDAO {
    private Session session;

    public UserDAO(Session session) {
        this.session = session;
    }

    public UsersDataSet getById(long id) throws HibernateException {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

    public UsersDataSet getByName(String name) throws HibernateException {
        return getByKey("name", name);
    }

    public UsersDataSet getByUid(String uid) throws HibernateException {
        return getByKey("uid", uid);
    }

    public void insertUser(
            String uid,
            String login,
            String password
    ) throws HibernateException {
        UsersDataSet user = new UsersDataSet(uid, login, password);
        session.save(user);
    }

    private UsersDataSet getByKey(String key, String value) {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria
                .add(Restrictions.eq(key, value))
                .uniqueResult());
    }
}
