package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Authentication;
import web.english.application.entity.user.Users;
import web.english.application.utils.RoleType;
import web.english.application.utils.UsersType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    /**
     * get logging page
     * @author VQKHANH
     * @return
     */
    @GetMapping("/login")
    public String getLogin(){

        return "dangnhap";
    }

    /**
     * get logging page
     * @author VQKHANH
     * @return
     */
    @GetMapping("/login/{error}")
    public String getLogin(HttpServletRequest httpServletRequest, Model model, @PathVariable String error){
        log.info(error);
        if(error.equals("fail"))model.addAttribute("msg","Tên đăng nhập hoặc mật khẩu không chính xác!");
        if(error.equals("expired"))model.addAttribute("msg","Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!");
        if(error.equals("disable"))model.addAttribute("msg","Tài khoản của bạn đã bị vô hiệu!");

        return "dangnhap";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("users") Users users, @RequestParam("passwordMatch") String passwordMatch, Model model, RedirectAttributes redirectAttributes){
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
//                    Users users1 = userDAO.save(users);
                    redirectAttributes.addFlashAttribute("users",users);
                    return "redirect:/register/generateOtp";
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
            log.info(users1.toString());
            return "dangky";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "/access-denied";
    }

}
