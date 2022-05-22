package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.CategoryDAO;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.LevelDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.course.Category;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.Level;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.Utils;

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

    private Utils utils = new Utils();

    @GetMapping("/courses")
    public String getCourse(Model model, HttpServletRequest httpServletRequest, @ModelAttribute(name = "msg") String msg, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Course> courses = courseDAO.findAllCourse();
        model.addAttribute("courses",courses);
        model.addAttribute("msg",msg);
        return "admin/course/khoahoc";
    }

    @GetMapping("/course/addCourse")
    public String getAddCoursePage(Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Course course = new Course();
        List<Category> categories = categoryDAO.findAllCategory();
        List<Level> levels = levelDAO.findAllLevel();
        model.addAttribute("course",course);
        model.addAttribute("levels",levels);
        model.addAttribute("categories",categories);
        return "admin/course/addkhoahoc";
    }

    @GetMapping("/course/updateCourse/{courseId}")
    public String getUpdateCoursePage(@PathVariable("courseId") int courseId,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

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
    public String addCourse(Authentication authentication,Model model,RedirectAttributes redirectAttributes, @ModelAttribute("course") Course course, @RequestParam("level_id") String level_id, @RequestParam("category_id") String category_id){
        int levelId = Integer.parseInt(level_id);
        int categoryId = Integer.parseInt(category_id);
        List<Category> categories = categoryDAO.findAllCategory();
        List<Level> levels = levelDAO.findAllLevel();
        model.addAttribute("course",course);
        model.addAttribute("levels",levels);
        model.addAttribute("categories",categories);
        model.addAttribute("levelId",levelId);
        model.addAttribute("categoryId",categoryId);
        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        if (!utils.checkMaxLength(course.getName())){
            model.addAttribute("errorName","Tên khóa học không quá 255 kí tự");
            return "admin/course/addkhoahoc";
        }
        if (!utils.checkMaxLength(course.getDescription())){
            model.addAttribute("errorContent","Nội dung khóa học không quá 255 kí tự");
            return "admin/course/addkhoahoc";
        }
        if (course.getPrice() < 0){
            model.addAttribute("errorPrice","Giá không âm");
            return "admin/course/addkhoahoc";
        }

        course.setEnable(true);
        courseDAO.saveCourse(course,levelId,categoryId);
        redirectAttributes.addFlashAttribute("msg","Thêm khóa học thành công");
        return "redirect:/admin/courses";
    }

    @PostMapping("/course/update")
    public String updateCourse(Authentication authentication,Model model,RedirectAttributes redirectAttributes,@ModelAttribute("course") Course course, @RequestParam("level_id") String level_id,@RequestParam("category_id") String category_id){
        int levelId = Integer.parseInt(level_id);
        int categoryId = Integer.parseInt(category_id);
        List<Category> categories = categoryDAO.findAllCategory();
        List<Level> levels = levelDAO.findAllLevel();
        model.addAttribute("course",course);
        model.addAttribute("levels",levels);
        model.addAttribute("categories",categories);
        model.addAttribute("levelId",levelId);
        model.addAttribute("categoryId",categoryId);
        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        if (!utils.checkMaxLength(course.getName())){
            model.addAttribute("errorName","Tên khóa học không quá 255 kí tự");
            return "admin/course/editkhoahoc";
        }
        if (!utils.checkMaxLength(course.getDescription())){
            model.addAttribute("errorContent","Nội dung khóa học không quá 255 kí tự");
            return "admin/course/editkhoahoc";
        }
        if (course.getPrice() < 0){
            model.addAttribute("errorPrice","Giá không âm");
            return "admin/course/editkhoahoc";
        }


        courseDAO.saveCourse(course,levelId,categoryId);
        redirectAttributes.addFlashAttribute("msg","Cập nhật khóa học thành công");
        return "redirect:/admin/courses";
    }

    @GetMapping("/course/disable/{id}")
    public String disableCourse(@PathVariable int id,RedirectAttributes redirectAttributes){
        Course course = courseDAO.findCourse(id);
        course.setEnable(false);
        courseDAO.disableCourse(course);
        redirectAttributes.addFlashAttribute("msg","Cập nhật thành công");
        return "redirect:/admin/courses";
    }

    @GetMapping("/course/enable/{id}")
    public String enableCourse(@PathVariable int id,RedirectAttributes redirectAttributes){
        Course course = courseDAO.findCourse(id);
        course.setEnable(true);
        courseDAO.disableCourse(course);
        redirectAttributes.addFlashAttribute("msg","Cập nhật thành công");
        return "redirect:/admin/courses";
    }

    @PostMapping("/course/search")
    public String searchCourse(@RequestParam("idOrName") String idOrName,HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Course> courses = courseDAO.findByIdOrCourseName(idOrName);
        model.addAttribute("courses",courses);
        return "admin/course/khoahoc";
    }

    @GetMapping("/course/detail/{id}")
    public String getDetailPage(@PathVariable int id,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Course course = courseDAO.findCourse(id);
        model.addAttribute("course",course);
        return "admin/course/chitietkhoahoc";
    }
}
