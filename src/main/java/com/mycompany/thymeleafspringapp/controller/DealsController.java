/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.controller;

import com.mycompany.thymeleafspringapp.model.Deals;
import com.mycompany.thymeleafspringapp.model.Users;
import com.mycompany.thymeleafspringapp.service.DealsService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andrey
 */
@Controller
public class DealsController {
    
    Logger log = LoggerFactory.getLogger(DealsController.class);

    @Autowired
    DealsService ds;

    @RequestMapping(value = "/deals/{dealid}", method = RequestMethod.GET)
    public String getDeals(@PathVariable(value = "dealid") long dealId, Model model) {
        Users userDetails
                = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId=userDetails.getUserId();
        log.info("User_id= "+userId);
        Deals d = ds.getDealById(dealId);
        model.addAttribute("deal", d);
        return "viewDeal";
    }

    @RequestMapping(value = "/deals/{dealid}/{imgid}", method = RequestMethod.GET)
    public void getImage(@PathVariable(value = "imgid") long imgId, @PathVariable(value = "dealid") long dealId, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(ds.getScreenshotById(imgId));
        response.getOutputStream().close();
    }
}
