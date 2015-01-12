package com.mycompany.thymeleafspringapp.service;

import com.mycompany.thymeleafspringapp.dao.UsersDAO;
import com.mycompany.thymeleafspringapp.model.Users;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author andrey
 */
@Service
@Profile({"test", "production"})
public class UserService implements UserDetailsService {

    @Autowired
    UsersDAO usersDAO;
    
    public void createUser(String userName,String password, String email){
        usersDAO.createUser(userName, password, email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersDAO.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Couldn't find the user " + username);
        }
        return new UserDetailsService(user);
    }

    private static final class UserDetailsService extends Users implements UserDetails {

        UserDetailsService(Users user) {
            super(user.getUserId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getEnabled());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("USER"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getPassword() {
            return super.getPassword();
        }

        @Override
        public String getUsername() {
            return super.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return super.getEnabled();
        }

        @Override
        public boolean isAccountNonLocked() {
            return super.getEnabled();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return super.getEnabled();
        }

        @Override
        public boolean isEnabled() {
            return super.getEnabled();
        }

    }

}
