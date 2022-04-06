package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.CategoryDAO;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.LevelDAO;
import web.english.application.entity.course.Category;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.Level;

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

    @GetMapping("/courses")
    public String getCourse(Model model){
        List<Course> courses = courseDAO.findAllCourse();
        model.addAttribute("courses",courses);
        return "admin/khoahoc";
    }

    @GetMapping("/course/addCourse")
    public String getAddCoursePage(Model model){
        Course course = new Course();
        List<Category> categories = categoryDAO.findAllCategory();
        List<Level> levels = levelDAO.findAllLevel();
        model.addAttribute("course",course);
        model.addAttribute("levels",levels);
        model.addAttribute("categories",categories);
        return "admin/addkhoahoc";
    }

    @GetMapping("/course/updateCourse/{courseId}")
    public String getUpdateCoursePage(@PathVariable("courseId") int courseId,Model model){
        Course course = courseDAO.getCourse(courseId);
        List<Category> categories = categoryDAO.findAllCategory();
        List<Level> levels = levelDAO.findAllLevel();
        model.addAttribute("course",course);
        model.addAttribute("levels",levels);
        model.addAttribute("categories",categories);
        return "admin/editkhoahoc";
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

}
