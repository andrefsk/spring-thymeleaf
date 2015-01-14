/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thymeleafspringapp.config;

import com.mycompany.thymeleafspringapp.model.Users;
import com.mycompany.thymeleafspringapp.service.SimpleSigninAdapter;
import com.mycompany.thymeleafspringapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

/**
 *
 * @author andrey
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {
    @Autowired
    UserService usersService;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfc, Environment e) {
        cfc.addConnectionFactory(new FacebookConnectionFactory("324403211086437", "48d13789f9af24d5eb4ab4e3c742f6cf"));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator cfl) {
        InMemoryUsersConnectionRepository userRep= new InMemoryUsersConnectionRepository(cfl);
        userRep.setConnectionSignUp(new ConnectionSignUp() {

            @Override
            public String execute(Connection<?> cnctn) {
                UserProfile profile = cnctn.fetchUserProfile();
                usersService.createNewUser(profile., null, null)
                
            }
        });
        return userRep;
    }

    @Bean
    public ConnectController connectController(
            ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        ConnectController controller = new ConnectController(connectionFactoryLocator, connectionRepository);

        //controller.setApplicationUrl("http://localhost:8080/spring-thymeleaf/");
        return controller;
    }

    @Bean
    public ProviderSignInController providerSignInController(
            ConnectionFactoryLocator connectionFactoryLocator,
            UsersConnectionRepository usersConnectionRepository) {
        ProviderSignInController controller = new ProviderSignInController(
                connectionFactoryLocator,
                usersConnectionRepository(),
                new SimpleSigninAdapter());
        return controller;
    }

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

        registry.addConnectionFactory(new FacebookConnectionFactory("324403211086437", "48d13789f9af24d5eb4ab4e3c742f6cf"));

        return registry;
    }

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        return getUsersConnectionRepository(connectionFactoryLocator());
        
    }

}