/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.thymeleafspringapp.dao;

import com.mycompany.thymeleafspringapp.model.Users;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author andrey
 */
public interface UsersDAO {
    
    
    public Users getUserByName(String username);
    public Users getUserById(long id);
    public List<Users> getUsersList();
    public boolean  setUserPassword();
    public boolean  setUserEmail();
    public boolean deleteUser(Users user);
    public Users createUser(String username,String password,String email);
    
}
