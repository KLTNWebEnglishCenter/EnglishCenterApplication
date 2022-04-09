package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
@Slf4j
public class LoginController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/register")
    public String getRegister(Model model){
        Users users = new Users();
        model.addAttribute("users", users);
        return "dangky";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "dangnhap";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("users") Users users, @RequestParam("passwordMatch") String passwordMatch, Model model){
        String checkEmail = userDAO.checkRequired("Email",users.getEmail());
        String checkLengthUsername = userDAO.checkLength("Username",users.getUsername(),8,20);
        String checkLengthPassword = userDAO.checkLength("Password",users.getPassword(),6,20);
        String checkPasswordMatch = userDAO.checkPasswordMatch(users.getPassword(),passwordMatch);
        String checkRequiredUsername = userDAO.checkRequired("Username",users.getUsername());
        String checkRequiredPassword = userDAO.checkRequired("Password",users.getPassword());
        String checkRequiredEmail = userDAO.checkRequired("Email",users.getEmail());
        String checkRequiredPasswordMatch = userDAO.checkRequired("Password Match",passwordMatch);

        model.addAttribute("users",users);

        if(checkRequiredUsername.equals("") && checkRequiredEmail.equals("") && checkRequiredPassword.equals("") && checkRequiredPasswordMatch.equals("")){
            if(checkLengthUsername.equals("") && checkLengthPassword.equals("")){
                if(checkEmail.equals("") && checkPasswordMatch.equals("")){
                    Users users1 = userDAO.save(users);
                    return "dangnhap";
                }else{
                    model.addAttribute("errorEmail",checkEmail);
                    model.addAttribute("errorPasswordMatch",checkPasswordMatch);
                    return "dangky";
                }
            }else {
                model.addAttribute("errorUsername",checkLengthUsername);
                model.addAttribute("errorPassword",checkLengthPassword);
                return "dangky";
            }
        }else {
            model.addAttribute("errorUsername",checkRequiredUsername);
            model.addAttribute("errorEmail",checkRequiredEmail);
            model.addAttribute("errorPassword",checkRequiredPassword);
            model.addAttribute("errorPasswordMatch",checkRequiredPasswordMatch);
            Users users1 = userDAO.save(users);
            return "dangky";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,@RequestParam("password") String password, HttpServletResponse response){
        String access_token = userDAO.login(username,password);
        log.info(access_token);
        Cookie cookie=new Cookie("access_token",access_token);
        response.addCookie(cookie);
        return "redirect:/register";
    }
}
