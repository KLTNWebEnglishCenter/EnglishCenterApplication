package web.english.application.usercontroller;

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
@RequestMapping("/")
@Slf4j
public class UserHomeController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/home")
    public String getHome(HttpServletRequest httpServletRequest, Model model){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if(cookie.getName().equals("access_token")){
                    token = cookie.getValue();
                }
            }
            String token_valid = "Bearer "+token;
            if(token != ""){
                user = userDAO.getUserFromToken(token_valid);
            }
        }
        model.addAttribute("users",user);
        return "home";
    }
}