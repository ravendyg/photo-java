package dbService.dao;

import dbService.DataServices.ViewDataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ViewDAO {
    private Session session;

    public ViewDAO(Session session) {
        this.session = session;
    }

    public void add(ViewDataSet view) throws HibernateException {
        session.save(view);
    }
}
