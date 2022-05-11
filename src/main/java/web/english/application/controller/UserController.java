package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    private boolean a = false;

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
        if (a == true){
            model.addAttribute("msg","Cập nhật thành công");
        }else {
            model.addAttribute("msg","");
        }
        return "admin/thongtincanhan";
    }

    @GetMapping("/info/update/{id}")
    public String getUpdatePage(@PathVariable("id") int id, Model model){
        Users users = userDAO.getUser(id);
        model.addAttribute("users",users);
        return "admin/editthongtincanhan";
    }

    @PostMapping("/info/update")
    public String updateUser(@ModelAttribute Users users,Model model,@RequestPart(value = "profile") MultipartFile file){
        if (file.getSize() > 0){
            String url = userDAO.uploadFileProfile(file);
            users.setImg(url);
        }
        Users users1 = userDAO.update(users);
        a = true;
        return "redirect:/user/info/"+users.getId();
    }

    @GetMapping("/password/{id}")
    public String updatePasswordUser(@PathVariable("id") int id,Model model){
        Users users = userDAO.getUser(id);
        model.addAttribute("users",users);
        model.addAttribute("oldp","");
        model.addAttribute("newp","");
        return "admin/doimatkhau";
    }

    @PostMapping("/password/update")
    public String updatePassUser(@RequestParam String userId,@RequestParam String oldPass, @RequestParam String newPass,@RequestParam String newPassCheck,Model model){
        Users users = userDAO.getUser(Integer.parseInt(userId));
        model.addAttribute("users",users);
        model.addAttribute("oldp",oldPass);
        model.addAttribute("newp",newPass);
        String rs1 = userDAO.updatePassword(userId,oldPass,newPass);
        String rs = userDAO.checkPasswordMatch(newPassCheck,newPass);
        if (rs != ""){
            model.addAttribute("errorMs",rs);
            return "admin/doimatkhau";
        }else if (rs1.equals("false")){
            model.addAttribute("errorMs","Nhập mật khẩu sai");
            return "admin/doimatkhau";
        }else {
            a = true;
            return "redirect:/user/info/"+users.getId();
        }
    }
}
