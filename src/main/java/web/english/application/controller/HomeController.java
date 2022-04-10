package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@Slf4j
public class HomeController {
    @Autowired
    private UserDAO userDAO;

    @GetMapping("/home")
    public String getIndex(HttpServletRequest httpServletRequest, Model model){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
//        log.info(token);
        String token_valid = "Bearer "+token;
        log.info(token_valid);
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }
        log.info(user.toString());
        if(user == null){
            return "redirect:/login";
        }

        return "admin/index";
    }
}
