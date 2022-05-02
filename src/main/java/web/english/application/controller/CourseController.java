package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.CategoryDAO;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.LevelDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.course.Category;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.Level;
import web.english.application.entity.user.Users;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class CourseController {
    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private LevelDAO levelDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private UserDAO userDAO;



    @GetMapping("/courses")
    public String getCourse(Model model, HttpServletRequest httpServletRequest){
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
        List<Course> courses = courseDAO.findAllCourse();
        model.addAttribute("courses",courses);
        return "admin/course/khoahoc";
    }

    @GetMapping("/course/addCourse")
    public String getAddCoursePage(Model model,HttpServletRequest httpServletRequest){
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
        Course course = new Course();
        List<Category> categories = categoryDAO.findAllCategory();
        List<Level> levels = levelDAO.findAllLevel();
        model.addAttribute("course",course);
        model.addAttribute("levels",levels);
        model.addAttribute("categories",categories);
        return "admin/course/addkhoahoc";
    }

    @GetMapping("/course/updateCourse/{courseId}")
    public String getUpdateCoursePage(@PathVariable("courseId") int courseId,Model model,HttpServletRequest httpServletRequest){
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
        Course course = courseDAO.getCourse(courseId);
        List<Category> categories = categoryDAO.findAllCategory();
        List<Level> levels = levelDAO.findAllLevel();
        model.addAttribute("course",course);
        model.addAttribute("levels",levels);
        model.addAttribute("categories",categories);
        return "admin/course/editkhoahoc";
    }

    @GetMapping("/course/deleteCourse/{courseId}")
    public String deleteCourse(@PathVariable("courseId") int courseId){
        courseDAO.deleteCourse(courseId);
        return "redirect:/admin/courses";
    }

    @PostMapping("/course/add")
    public String addCourse(@ModelAttribute("course") Course course, @RequestParam("level_id") String level_id,@RequestParam("category_id") String category_id){
        int levelId = Integer.parseInt(level_id);
        int categoryId = Integer.parseInt(category_id);
        courseDAO.saveCourse(course,levelId,categoryId);
        return "redirect:/admin/courses";
    }

    @PostMapping("/course/update")
    public String updateCourse(@ModelAttribute("course") Course course, @RequestParam("level_id") String level_id,@RequestParam("category_id") String category_id){
        int levelId = Integer.parseInt(level_id);
        int categoryId = Integer.parseInt(category_id);
        courseDAO.saveCourse(course,levelId,categoryId);
        return "redirect:/admin/courses";
    }

    @PostMapping("/course/search")
    public String searchCourse(@RequestParam("idOrName") String idOrName,HttpServletRequest httpServletRequest,Model model){
       List<Course> courses = courseDAO.findByIdOrCourseName(idOrName);
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
        model.addAttribute("courses",courses);
        return "admin/course/khoahoc";
    }

    @GetMapping("/course/detail/{id}")
    public String getDetailPage(@PathVariable int id,Model model,HttpServletRequest httpServletRequest){
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
        Course course = courseDAO.findCourse(id);
        model.addAttribute("course",course);
        return "admin/course/chitietkhoahoc";
    }
}
