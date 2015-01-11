/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.thymeleafspringapp.service.UserService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author andrey
 */
public class UserServiceTest {

    UserService service;

    public UserServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        service = new UserService();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getUserList() {
//        List<Users> users=service.gerUsersList();
//        for(Users user : users){
//            System.err.println(user.getPassword());
//        }
    }
    
    
    
        @Test
    public void encodepass() {
            BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
            System.out.println(encoder.encode("pendos90"));
            System.out.println(encoder.matches("pendos90", "$2a$10$l3kuWXnu98zcLdCNC4FqJuXY6ivaoQ/EGxsVMh16r0CU575T00A5."));
            System.out.println(encoder.encode("qq"));
            System.out.println(encoder.encode("qqq"));
//            
//            INSERT INTO trdr.users(
//            username, password, email, enabled, user_id)
//    VALUES ('user', '$2a$10$l3kuWXnu98zcLdCNC4FqJuXY6ivaoQ/EGxsVMh16r0CU575T00A5.', 'qq@qq.ru', true, 1);
//
//INSERT INTO trdr.users(
//            username, password, email, enabled, user_id)
//    VALUES ('user1', '$2a$10$2AeZo0QkpRFPGp12sw/ls.qU7cIXRfoYmd5KTsxNsmH7Cqs/eTs2i', 'qq1@qq1.ru', true, 2);
//INSERT INTO trdr.users(
//            username, password, email, enabled, user_id)
//    VALUES ('user2', '$2a$10$YU1ME2YI.hl3Uo/KE9C.wuYv2xYm57ROHg2jNWK0v/CPpZ/JuIxYe', 'qq2@qq2.ru', true, 3);
//            
    }

}
