package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.TeacherDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Teacher;
import web.english.application.utils.Utils;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class TeacherController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    private Utils utils=new Utils();

    @GetMapping("/teacher")
    public String getGiangVien(Model model){
        List<Teacher> teachers=teacherDAO.findAllTeacher();
        model.addAttribute("teachers",teachers);
        return "admin/giangvien";
    }

    @GetMapping("/addteacher")
    public String getAddTeacherPage(Model model){
        Teacher teacher=new Teacher();
        teacher.setGender("Nam");
        model.addAttribute("teacher",teacher);
        return  "admin/addgiangvien";
    }
    @PostMapping("/teacher/add")
    public String saveTeacher(@ModelAttribute Teacher teacher,Model model){

        if(!utils.checkFullNameFormat(teacher.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/addgiangvien";
        }
        if(!utils.checkEmailFormat(teacher.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/addgiangvien";
        }
        if(!utils.checkPhoneNumberFormat(teacher.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return  "admin/addgiangvien";
        }

        if(!utils.checkUsernameFormat(teacher.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/addgiangvien";
        }

        if(!utils.checkDob(teacher.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/addgiangvien";
        }


        teacher.setEnable(true);
        log.info(teacher.toString());
        teacherDAO.saveTeacher(teacher);
        return "redirect:/admin/teacher";
    }

    @GetMapping("/editteacher/{id}")
    public String getEditTeacherPage(@PathVariable("id") int id,Model model){
//        log.info(id+"");
        Teacher teacher=teacherDAO.findTeacherById(id);
//        log.info(teacher.toString());
        model.addAttribute("teacher",teacher);
        return "admin/editgiangvien";
    }

    @PostMapping("/teacher/edit")
    public String editTeacher(@ModelAttribute Teacher teacher,Model model){

        if(!utils.checkFullNameFormat(teacher.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/addgiangvien";
        }
        if(!utils.checkEmailFormat(teacher.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/addgiangvien";
        }
        if(!utils.checkPhoneNumberFormat(teacher.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return  "admin/addgiangvien";
        }

        if(!utils.checkUsernameFormat(teacher.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/addgiangvien";
        }

        if(!utils.checkDob(teacher.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/addgiangvien";
        }


        teacher.setEnable(true);
        log.info(teacher.toString());
        teacherDAO.saveTeacher(teacher);
        return "redirect:/admin/teacher";
    }
//    @GetMapping("/deleteteacher/{id}")
//    public String getDeleteTeacher(@PathVariable("id") int id){
//        log.info(id+"");
//        return "admin/editgiangvien";
//    }
}
