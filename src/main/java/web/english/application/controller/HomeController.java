package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.RoleType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@Slf4j
public class HomeController {
    @Autowired
    private UserDAO userDAO;

    @GetMapping("/home")
    public String getIndex(Authentication authentication){
        String role = authentication.getAuthorities().iterator().next().toString();
        if (role.equals(RoleType.EMPLOYEE)){
            return "redirect:/admin/teacher";
        }
        if (role.equals(RoleType.ADMIN)){
            return "redirect:/admin/employee";
        }
        return "redirect:/admin/schedule";
    }

}
