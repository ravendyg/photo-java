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

    public UsersDataSet get(long id) throws HibernateException {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

    public UsersDataSet get(String name) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria
                .add(Restrictions.eq("name", name))
                .uniqueResult());
    }

    public UsersDataSet get(String name, String password) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria
                .add(Restrictions.eq("name", name))
                .add(Restrictions.eq("password", password))
                .uniqueResult());
    }

    public UsersDataSet insertUser(String uid, String name, String passwordHash) throws HibernateException {
        UsersDataSet usersDataSet = new UsersDataSet(uid, name, passwordHash);
        session.save(usersDataSet);
        return usersDataSet;
    }
}
