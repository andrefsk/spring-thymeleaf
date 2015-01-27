/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.service;

import com.mycompany.thymeleafspringapp.dao.DealsDAO;
import com.mycompany.thymeleafspringapp.model.Deals;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author andrey
 */
@Service
public class DealsService {

    @Autowired
    DealsDAO dealsDAO;
    
    public List<Deals> getDeals(long userId){
        return dealsDAO.getDeals(userId);
    }
    
    public Deals getDealById(long dealId){
        return dealsDAO.getDeal(dealId);
    }
    
    public byte[] getScreenshotById(long imgId){
        return dealsDAO.getScreenshot(imgId);
    }
}
