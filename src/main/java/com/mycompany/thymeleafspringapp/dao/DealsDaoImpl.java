/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.dao;

import com.mycompany.thymeleafspringapp.model.DealDirection;
import com.mycompany.thymeleafspringapp.model.Deals;
import com.mycompany.thymeleafspringapp.model.Instruments;
import com.mycompany.thymeleafspringapp.model.Screenshots;
import com.mycompany.thymeleafspringapp.model.Tags;
import com.mycompany.thymeleafspringapp.model.Users;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 *
 * @author andrey
 */
@Repository
@Profile({"test", "production"})
public class DealsDaoImpl implements DealsDAO {

    Logger log = LoggerFactory.getLogger("DealsDaoImpl");

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private UsersDAO usersDAO;

    @Override
    public List<Deals> getDeals(long userId) {
        Criteria crit = sessionFactory.openSession().createCriteria(Deals.class);
        crit.add(Restrictions.eq("users", usersDAO.getUserById(userId)));
        return crit.list();
    }

    @Override
    public Deals getDeal(long userId, long dealId) throws IllegalAccessException {
        Criteria crit = sessionFactory.openSession().createCriteria(Deals.class);
        crit.add(Restrictions.and(Restrictions.eq("dealId", dealId), Restrictions.eq("users", usersDAO.getUserById(userId))));
        crit.setMaxResults(1);
        List<Deals> deals=crit.list();
        if (deals.isEmpty()) {
            throw new IllegalAccessException("Unable to find the deal with given deal_id");

        }
        Deals d = deals.get(0);
        return d;
    }

    @Override
    public void closeDeal(long userId, long dealId, BigDecimal closePrice, Date closeDate) throws IllegalAccessException {

        Deals deal = getDeal(userId, dealId);
        closeDeal(deal, closePrice, closeDate);
    }

    @Override
    public void closeDeal(Deals deal, BigDecimal closePrice, Date closeDate) {
        deal.setDateClose(closeDate);
        deal.setPriceClose(closePrice.doubleValue());
        persistDeal(deal);
    }

    protected void persistDeal(Deals deal) throws HibernateException {
        Transaction tx = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            session.merge(deal);
            tx.commit();

        } catch (HibernateException e) {
            try {
                tx.rollback();
            } catch (RuntimeException rbe) {
                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Deals createDeal(Instruments instr, Users user, DealDirection direction, BigDecimal openPrice, Date openDate, String descr, Set<Tags> tags, Set<Screenshots> screenshots) {
        Deals d = new Deals(0, instr, user, direction == DealDirection.LONG ? true : false, openPrice.doubleValue(), null, openDate, null, descr, screenshots, tags);
        persistDeal(d);
        return d;
    }

    @Override
    public byte[] getScreenshot(long userId, long imgId) {
        Criteria crit = sessionFactory.openSession().createCriteria(Screenshots.class);
        crit.add(Restrictions.eq("screenshotId", imgId));
        crit.setMaxResults(1);
        Screenshots s = (Screenshots) crit.list().get(0);
        return s.getDeals().getUsers().getUserId() == userId ? s.getFile() : null;
    }

}
