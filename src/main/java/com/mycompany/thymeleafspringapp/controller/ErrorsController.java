/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author andrey
 */
@Controller
public class ErrorsController {

    @RequestMapping("/403")
    public String accessDenied() {
        return "errors/403";
    }

}
