/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author andrey
 */
@Controller
public class WelcomeController {

    @RequestMapping(value = {"/home", "", "/"}, method = RequestMethod.GET)
    public String getHome(@RequestParam(value = "name", defaultValue = "user") String name, Model model) {
        model.addAttribute("name", name);
        return "welcome";
    }
}
