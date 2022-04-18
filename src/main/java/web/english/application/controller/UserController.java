package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;

@Controller
@RequestMapping("/admin")
@Slf4j
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/info/{id}")
    public String getProfilePage(@PathVariable("id") int id, Model model){
        Users users = userDAO.getUser(id);
        model.addAttribute("users",users);
        return "admin/thongtincanhan";
    }
}
