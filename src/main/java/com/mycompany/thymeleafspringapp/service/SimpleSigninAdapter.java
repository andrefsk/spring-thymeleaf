/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/**
 *
 * @author andrey
 */
@Component
public class SimpleSigninAdapter implements SignInAdapter {

    @Autowired
    UserService userService;

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        String email = connection.fetchUserProfile().getEmail();
        UserDetails userDetails = userService.loadUserByUsername(email);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, null));
        return null;
    }
}
