package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.TeacherDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.user.Teacher;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * get the teacher management page
     * @author VQKHANH
     * @param model
     * @return
     */
    @GetMapping("/teacher")
    public String getTeacher(Model model, HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Teacher> teachers=teacherDAO.findAllTeacher();
        model.addAttribute("teachers",teachers);
        return "admin/teacher/teacher";
    }

    /**
     * get add teacher page
     * @author VQKHANH
     * @param model
     * @return
     */
    @GetMapping("/addteacher")
    public String getAddTeacherPage(Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Teacher teacher=new Teacher();
        teacher.setGender("Nam");
        model.addAttribute("teacher",teacher);
        return  "admin/teacher/addteacher";
    }

    /**
     * save new teacher
     * @author VQKHANH
     * @param teacher
     * @param model
     * @return
     */
    @PostMapping("/teacher/add")
    public String saveTeacher(@ModelAttribute Teacher teacher,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        if(!utils.checkFullNameFormat(teacher.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/teacher/addteacher";
        }
        if(!utils.checkEmailFormat(teacher.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/teacher/addteacher";
        }
        if(!utils.checkPhoneNumberFormat(teacher.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return  "admin/teacher/addteacher";
        }

        if(!utils.checkUsernameFormat(teacher.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/teacher/addteacher";
        }

        if(!utils.checkDob(teacher.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/teacher/addteacher";
        }

        teacher.setEnable(true);
        String msg=teacherDAO.saveTeacher(teacher);
        if(msg.equals(""))
            return "redirect:/admin/teacher";
        else{
            model.addAttribute("msg",msg);
            return  "admin/teacher/addteacher";
        }
    }

    /**
     * get teacher info editing page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editteacher/{id}")
    public String getEditTeacherPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Teacher teacher=teacherDAO.findTeacherById(id);
        model.addAttribute("teacher",teacher);
        return "admin/teacher/editteacher";
    }

    /**
     * save teacher info after editing
     * @author VQKHANH
     * @param teacher
     * @param model
     * @return
     */
    @PostMapping("/teacher/edit")
    public String editTeacher(@ModelAttribute Teacher teacher,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        if(!utils.checkFullNameFormat(teacher.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/teacher/editteacher";
        }
        if(!utils.checkEmailFormat(teacher.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/teacher/editteacher";
        }
        if(!utils.checkPhoneNumberFormat(teacher.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return  "admin/teacher/editteacher";
        }

        if(!utils.checkUsernameFormat(teacher.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/teacher/editteacher";
        }

        if(!utils.checkDob(teacher.getDob())){
            model.addAttribute("errorDob", Utils.yearRequire);
            return  "admin/teacher/editteacher";
        }


//        teacher.setEnable(true);
        String msg=teacherDAO.updateTeacher(teacher);
        if(msg.equals(""))
            return "redirect:/admin/teacher";
        else{
            model.addAttribute("msg",msg);
            return  "admin/teacher/editteacher";
        }
    }

    /**
     * get teacher info page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/teacherinfo/{id}")
    public String getTeacherInfoPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Teacher teacher=teacherDAO.findTeacherById(id);
        model.addAttribute("teacher",teacher);
        return "admin/teacher/teacherinfo";
    }

    /**
     * search teacher by id or username or full_name
     * @author VQKHANH
     * @param idOrUsername
     * @param fullName
     * @param model
     * @return
     */
    @PostMapping("/teacher/search")
    public String searchTeacher(@RequestParam String idOrUsername, @RequestParam String fullName,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Teacher> teachers=teacherDAO.searchUser(idOrUsername,fullName);

        model.addAttribute("teachers",teachers);
        return "admin/teacher/teacher";
    }

    @GetMapping("/teacher/classrooms/{teacherid}")
    public String getListClassroomOfTeacher(@PathVariable int teacherid,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Classroom> classrooms=teacherDAO.getAllClassroomOfTeacher(teacherid);
        log.info(classrooms.toString());
        model.addAttribute("classrooms",classrooms);
        return "admin/lophoc";
    }
}
