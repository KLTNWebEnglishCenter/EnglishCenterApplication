package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.TeacherDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.user.Teacher;
import web.english.application.entity.user.Users;
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
    public String getTeacher(Model model, HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
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
    public String getAddTeacherPage(Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
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
    public String saveTeacher(@ModelAttribute Teacher teacher,Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

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
//        log.info(teacher.toString());
        teacherDAO.saveTeacher(teacher);
        return "redirect:/admin/teacher";
    }

    /**
     * get teacher info editing page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editteacher/{id}")
    public String getEditTeacherPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
        Teacher teacher=teacherDAO.findTeacherById(id);
//        log.info(teacher.toString());
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
    public String editTeacher(@ModelAttribute Teacher teacher,Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
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


        teacher.setEnable(true);
//        log.info(teacher.toString());
        teacherDAO.saveTeacher(teacher);
        return "redirect:/admin/teacher";
    }

    /**
     * get teacher info page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/teacherinfo/{id}")
    public String getTeacherInfoPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
        Teacher teacher=teacherDAO.findTeacherById(id);
//        log.info(teacher.toString());
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
    public String searchTeacher(@RequestParam String idOrUsername, @RequestParam String fullName,Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
        List<Teacher> teachers=teacherDAO.searchUser(idOrUsername,fullName);

        model.addAttribute("teachers",teachers);
        return "admin/teacher/teacher";
    }

    @GetMapping("/teacher/classrooms/{teacherid}")
    public String getListClassroomOfTeacher(@PathVariable int teacherid,Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
        List<Classroom> classrooms=teacherDAO.getAllClassroomOfTeacher(teacherid);
        log.info(classrooms.toString());
        model.addAttribute("classrooms",classrooms);
        return "admin/lophoc";
    }
}
