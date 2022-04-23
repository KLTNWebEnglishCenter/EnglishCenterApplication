package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;
import web.english.application.utils.RoleType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/info/{id}")
    public String getProfilePage(@PathVariable("id") int id, Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if(cookie.getName().equals("access_token")){
                    token = cookie.getValue();
                }
            }
            String token_valid = "Bearer "+token;
        }else{
            return "redirect:/login";
        }
        Users users = userDAO.getUser(id);
        String author = userDAO.getAuthorFromToken(token);
        if(author.equals(RoleType.STUDENT)){
            model.addAttribute("author",false);
        }else{
            model.addAttribute("author",true);
        }

        model.addAttribute("users",users);
        return "admin/thongtincanhan";
    }

    @GetMapping("/info/update/{id}")
    public String getUpdatePage(@PathVariable("id") int id, Model model){
        Users users = userDAO.getUser(id);
        model.addAttribute("users",users);
        return "admin/editthongtincanhan";
    }

    @PostMapping("/info/update")
    public String updateUser(@ModelAttribute Users users,Model model){
        Users users1 = userDAO.update(users);
        return "redirect:/user/info/"+users.getId();
    }
}
