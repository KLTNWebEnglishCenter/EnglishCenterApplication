package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;
import web.english.application.utils.RoleType;
import web.english.application.utils.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserDAO userDAO;

    private Utils utils=new Utils();
    private boolean a = false;

    @GetMapping("/info/{id}")
    public String getProfilePage(@PathVariable("id") int id, Model model,HttpServletRequest httpServletRequest,@ModelAttribute(name = "success") String success){
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
            model.addAttribute("type","STUDENT");
        }else if (author.equals(RoleType.TEACHER)){
            model.addAttribute("author",true);
            model.addAttribute("type","TEACHER");
        }else if (author.equals(RoleType.EMPLOYEE)){
            model.addAttribute("author",true);
            model.addAttribute("type","EMPLOYEE");
        }else if (author.equals(RoleType.ADMIN)){
            model.addAttribute("author",true);
            model.addAttribute("type","ADMIN");
        }


        model.addAttribute("users",users);
        if (a == true){
            model.addAttribute("msg","Cập nhật thành công");
        }else {
            model.addAttribute("msg","");
        }
        model.addAttribute("success",success);
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
        model.addAttribute("users",users);
        
        if (!utils.checkFullNameLength(users.getFullName())){
            model.addAttribute("errorFullName", Utils.fullNameLength);
            return "admin/editthongtincanhan";
        }
        if(!utils.checkFullNameFormat(users.getFullName())){
            model.addAttribute("errorFullName", Utils.fullNameRequire);
            return "admin/editthongtincanhan";
        }
        if(!utils.checkEmailFormat(users.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return "admin/editthongtincanhan";
        }
        if(!utils.checkPhoneNumberFormat(users.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return "admin/editthongtincanhan";
        }

        if(!utils.checkUsernameFormat(users.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return "admin/editthongtincanhan";
        }

        if(!utils.checkDob(users.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/employee/addemployee";
        }

        if (file.getSize() > 0){
            String url = userDAO.uploadFileProfile(file);
            users.setImg(url);
        }
        String users1 = userDAO.update(users);
        if (!users1.equals("")){
            model.addAttribute("errorTotal",users1);
            return  "admin/employee/addemployee";
        }
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
    public String updatePassUser(RedirectAttributes redirectAttributes, @RequestParam String userId, @RequestParam String oldPass, @RequestParam String newPass, @RequestParam String newPassCheck, Model model){
        Users users = userDAO.getUser(Integer.parseInt(userId));
        model.addAttribute("users",users);
        model.addAttribute("oldp",oldPass);
        model.addAttribute("newp",newPass);
        String rs1 = userDAO.updatePassword(userId,oldPass,newPass);
        String rs = userDAO.checkPasswordMatch(newPassCheck,newPass);
        if (newPass.length() < 6 || newPass.length() > 20){
            model.addAttribute("errorMs","Mật khẩu có tối thiểu 6 kí tự, tối đa 20 kí tự");
            return "admin/doimatkhau";
        }
        if (!newPass.matches("[A-Za-z0-9]{6,20}")){
            model.addAttribute("errorMs","Mật khẩu gồm các kí tự hoa, thường và số");
            return "admin/doimatkhau";
        }
        if (rs != ""){
            model.addAttribute("errorMs",rs);
            return "admin/doimatkhau";
        }else if (rs1.equals("false")){
            model.addAttribute("errorMs","Nhập mật khẩu sai");
            return "admin/doimatkhau";
        }else {
            a = true;
            users.setPassword(newPass);
            redirectAttributes.addFlashAttribute("users",users);
            return "redirect:/user/password/generateOtp";
        }
    }
}
