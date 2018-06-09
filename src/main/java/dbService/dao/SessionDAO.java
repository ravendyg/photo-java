package dbService.dao;

import dbService.DataServices.SessionsDataSet;
import dbService.DataServices.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class SessionDAO {
    private Session session;

    public SessionDAO(Session session) { this.session = session; }

    public UsersDataSet getUser(String cookie) throws HibernateException {
        Criteria criteria = session.createCriteria(SessionsDataSet.class);
        // TODO: add expiration criteria
        SessionsDataSet sessionsDataSet = (SessionsDataSet) criteria
                .setFetchMode("users", FetchMode.JOIN)
                .add(Restrictions.eq("cookie", cookie))
                .uniqueResult()
                ;
        if (sessionsDataSet == null) {
            return null;
        } else {
            return sessionsDataSet.getUser();
        }
    }

    public SessionsDataSet getSession(String cookie) throws HibernateException {
        Criteria criteria = session.createCriteria(SessionsDataSet.class);
        return (SessionsDataSet) criteria
                .add(Restrictions.eq("cookie", cookie))
                .uniqueResult();
    }

    public long createSession(UsersDataSet user, String cookie) throws HibernateException {
        long id = (long) session.save(new SessionsDataSet(user, cookie));
        return  id;
    }

}
