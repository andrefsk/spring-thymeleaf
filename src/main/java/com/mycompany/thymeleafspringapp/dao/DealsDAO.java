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

/**
 *
 * @author andrey
 */
public interface DealsDAO {
    
    public List<Deals> getDeals(long userId);
    public Deals getDeal(long dealId);
    public void closeDeal(long dealId, BigDecimal closePrice, Date closeDate);
    public void closeDeal(Deals deal, BigDecimal closePrice, Date closeDate);
    public Deals createDeal(Instruments instr,Users user,DealDirection direction,BigDecimal openPrice, Date openDate,String descr, Set<Tags> tags,Set<Screenshots>screenshots);
}
