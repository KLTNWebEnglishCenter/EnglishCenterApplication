package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.UserDAO;
import web.english.application.entity.Users;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
@Slf4j
public class LoginController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/register")
    public String getRegister(){
        return "dangky";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "dangnhap";
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        Users user = new Users(username,password,email);
        Users users = userDAO.save(user);
        log.info("Ko hiendbhvjsbbbbbbb");
        return "dangnhap";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,@RequestParam("password") String password){
        String access_token = userDAO.login(username,password);
        return "dangky";
    }
}
