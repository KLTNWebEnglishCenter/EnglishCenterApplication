package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.english.application.dao.UserDAO;
import web.english.application.entity.Users;

import javax.servlet.http.Cookie;
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
    public String register(@ModelAttribute("user") Users user, Model theModel, HttpServletResponse response){
        Users users = userDAO.save(user);
        return "dangnhap";

    }
}
