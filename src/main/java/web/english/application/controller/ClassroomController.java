package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.*;
import web.english.application.entity.Classroom;
import web.english.application.entity.course.Course;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Teacher;
import web.english.application.entity.user.Users;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class ClassroomController {

    @Autowired
    private ClassroomDAO classroomDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private StudentDAO studentDAO;

    @GetMapping("/classrooms")
    public String getClassrooms(Model model, HttpServletRequest httpServletRequest){
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
        List<Classroom> classrooms = classroomDAO.findAll();
        model.addAttribute("classrooms",classrooms);
        return "admin/classroom/lophoc";
    }

    @GetMapping("/addClassroom")
    public String getAddClassroomPage(Model model,HttpServletRequest httpServletRequest){
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
        Classroom classroom = new Classroom();
        List<Course> courses = courseDAO.findAllCourse();
        List<Teacher> teachers = teacherDAO.findAllTeacher();
        model.addAttribute("classroom",classroom);
        model.addAttribute("courses", courses);
        model.addAttribute("teachers",teachers);
        return "admin/classroom/addlophoc";
    }

    @GetMapping("/updateClassroom/{id}")
    public String getUpdateClassroomPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest){
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
        Classroom classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/classroom/editlophoc";
    }

    @GetMapping("/classroom/info/{id}")
    public String getInformationPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest){
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
        Classroom classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/classroom/xemthongtinlophoc";
    }

    @PostMapping("/classroom/add")
    public String addClassroom(@ModelAttribute("classroom") Classroom classroom,@RequestParam("course_id") String course_id,@RequestParam("teacher_id") String teacher_id, Model mode){
        int courseId = Integer.parseInt(course_id);
        int teacherId = Integer.parseInt(teacher_id);

        classroom.setStatus("Openning");
        classroomDAO.saveClassroom(classroom,teacherId,courseId);
        return "redirect:/admin/classrooms";
    }

    @PostMapping("/classroom/update")
    public String updateClassroom(@ModelAttribute("classroom") Classroom classroom){
        Classroom classroom1 = classroomDAO.getClassroom(classroom.getId());
        classroom.setModifiedDate(LocalDate.now());
        classroomDAO.saveClassroom(classroom,classroom1.getTeacher().getId(),classroom1.getCourse().getId());
        return "redirect:/admin/classrooms";
    }

    @GetMapping("/classroom/delete/{id}")
    public String deleteClassroom(@PathVariable("id") int id){
        Classroom classroom = classroomDAO.deleteClassroom(id);
        return "redirect:/admin/classrooms";
    }

    @PostMapping("/classroom/search")
    public String searchCourse(@RequestParam("idOrName") String idOrName,HttpServletRequest httpServletRequest,Model model){
        List<Classroom> classrooms = classroomDAO.findByIdOrClassName(idOrName);
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
        model.addAttribute("classrooms",classrooms);
        return "admin/classroom/lophoc";
    }

    @GetMapping("/classroom/students/{id}")
    public String getListStudentPage(HttpServletRequest httpServletRequest, Model model, @PathVariable int id){
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
        List<Student> students= classroomDAO.getStudentFromClassroom(id);
        model.addAttribute("classroomId",id);
        model.addAttribute("students",students);
        return "admin/classroom/danhsachhocvien";
    }
}
