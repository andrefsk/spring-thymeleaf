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

    @Override
    public List<Deals> getDeals(long userId) {
        Criteria crit = sessionFactory.openSession().createCriteria(Deals.class);
        return crit.list();
    }

    @Override
    public Deals getDeal(long dealId) {
        Criteria crit = sessionFactory.openSession().createCriteria(Deals.class);
        crit.add(Restrictions.eq("deal_id", dealId));
        crit.setMaxResults(1);
        return (Deals) crit.list().get(0);
    }

    @Override
    public void closeDeal(long dealId, BigDecimal closePrice, Date closeDate) {

        Deals deal = getDeal(dealId);
        closeDeal(deal, closePrice, closeDate);
    }

    @Override
    public void closeDeal(Deals deal, BigDecimal closePrice, Date closeDate) {
        Transaction tx = null;
        Session session = null;
        deal.setDateClose(closeDate);
        deal.setPriceClose(closePrice.doubleValue());
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            session.persist(deal);
            tx.commit();

        } catch (RuntimeException e) {
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
    public Deals createDeal(Instruments instr,Users user, DealDirection direction, BigDecimal openPrice, Date openDate,String descr, Set<Tags> tags, Set<Screenshots> screenshots) {
        return  new Deals(0, instr, user, direction==DealDirection.LONG?true:false, openPrice.doubleValue(), null, openDate, null,descr, screenshots, tags);
    }

}
