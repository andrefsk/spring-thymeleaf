/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.dao;

import com.mycompany.thymeleafspringapp.model.Users;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author andrey
 */
@Repository
public class UsersDAOImpl implements UsersDAO {

    Logger log = LoggerFactory.getLogger("UsersDAOImpl");

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Users getUserByName(String username) {
        Criteria crit = sessionFactory.openSession().createCriteria(Users.class);
        crit.add(Restrictions.or(Restrictions.eq("email", username), Restrictions.eq("username", username)));
        crit.setMaxResults(1);
        return (Users) crit.list().get(0);

    }

    @Override
    public Users getUserById(long id) {
        Criteria crit = sessionFactory.openSession().createCriteria(Users.class);
        crit.add(Restrictions.eq("user_id", id));
        crit.setMaxResults(1);
        return (Users) crit.list().get(0);
    }

    @Override
    public List<Users> getUsersList() {
        Session session = sessionFactory.openSession();
        List<Users> users = session.createCriteria(Users.class).list();
        return users;
    }

    @Override
    public boolean setUserPassword() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setUserEmail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteUser(Users user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Users createUser(String username, String password, String email) {
        Transaction tx = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            Users user = new Users(0, username, password, email, Boolean.TRUE);
            session.persist(user);
            tx.commit();
            return user;

        } catch (RuntimeException e) {
            try {
                tx.rollback();
            } catch (RuntimeException rbe) {
                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
        }finally{
    		if(session!=null){
    			session.close();
    		}
    	}

    }

}
