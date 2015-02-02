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
import java.util.logging.Level;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
        try {
            Users user
                    = getUserDetails();
            Deals d = ds.getDealById(user.getUserId(),dealId);
            model.addAttribute("deal", d);
            return "viewDeal";
        } catch (IllegalAccessException ex) {
            log.error("Acces denied.", ex);
            return "redirect:/403";
        }
    }

    private Users getUserDetails() {
        Users userDetails
                = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }

    @RequestMapping(value = "/deals/{dealid}/{imgid}", method = RequestMethod.GET)
    public void getImage(@PathVariable(value = "imgid") long imgId, @PathVariable(value = "dealid") long dealId, HttpServletResponse response) throws IOException {
        Users user
                = getUserDetails();
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(ds.getScreenshotById(user.getUserId(),imgId));
        response.getOutputStream().close();
    }
}
