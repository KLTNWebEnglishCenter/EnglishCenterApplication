package web.english.application.usercontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.UsersCourseRequest;
import web.english.application.entity.course.UsersCourseRequestKey;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Users;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class CourseHomeController {

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/course/home")
    public String getCourseHome(Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if(cookie.getName().equals("access_token")){
                    token = cookie.getValue();
                }
            }
            String token_valid = "Bearer "+token;
            if(token != ""){
                user = userDAO.getUserFromToken(token_valid);
            }
        }

        List<Course> courses = courseDAO.findAllCourse();
        List<UsersCourseRequest> courseRequests = courseDAO.getAllSignupCourse();
        List<Course> courseList = new ArrayList<>();
        try {
            courseList = courseDAO.getListCourseFromUserCourseRequest(user.getId(),courseRequests);
        }catch (Exception e){

        }
        List<Course> courses1 = new ArrayList<>();
        courses1.addAll(courses);
        if(courseList.size()>0){
            courseList.forEach(course -> {
                courses.forEach(course2 -> {
                    if (course.getId() == course2.getId()){
                        courses1.remove(course2);
                    }
                });
            });
        }

        model.addAttribute("users",user);
        model.addAttribute("courses",courses1);
        model.addAttribute("flag", false);
        model.addAttribute("flag1", false);
        model.addAttribute("flag2", false);
        return "course-web";
    }

    @GetMapping("/course/signup/{id}")
    public String signupCourse(@PathVariable int id, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes){
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
//        log.info(token);
        String token_valid = "Bearer "+token;
//        log.info(token_valid);
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        Course course = courseDAO.getCourse(id);
        Student student = new Student(user.getId(),user.getUsername(),user.getPassword(),user.getFullName(),user.getDob(),user.getGender(),user.getEmail(),user.getPhoneNumber(),user.isEnable());
        UsersCourseRequestKey key = new UsersCourseRequestKey(user.getId(),id);
        UsersCourseRequest request = new UsersCourseRequest(key,student,course);
        UsersCourseRequest signupCourse = courseDAO.signupCourse(request);

        if(signupCourse != null)
            redirectAttributes.addFlashAttribute("msg","Đăng ký thành công!");
        else
            redirectAttributes.addFlashAttribute("msg","Đăng ký không thành công!");

        return "redirect:/course/home";
    }

    @GetMapping("/course/signup/toeic/{rs}")
    public String changeCourse(Model model,HttpServletRequest httpServletRequest,@PathVariable boolean rs){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if(cookie.getName().equals("access_token")){
                    token = cookie.getValue();
                }
            }
            String token_valid = "Bearer "+token;
            if(token != ""){
                user = userDAO.getUserFromToken(token_valid);
            }
        }

        List<Course> courses = courseDAO.findAllCourse();
        List<UsersCourseRequest> courseRequests = courseDAO.getAllSignupCourse();
        List<Course> courseList = courseDAO.getListCourseFromUserCourseRequest(user.getId(),courseRequests);

        List<Course> courses1 = new ArrayList<>();
        courses1.addAll(courses);
        courseList.forEach(course -> {
            courses.forEach(course2 -> {
                if (course.getId() == course2.getId()){
                    courses1.remove(course2);
                }
            });
        });

        if (rs == true){
            rs = false;
        }else {
            rs = true;
        }

        model.addAttribute("users",user);
        model.addAttribute("courses",courses1);
        model.addAttribute("flag", rs);
        model.addAttribute("flag1", false);
        model.addAttribute("flag2", false);
        return "course-web";
    }

    @GetMapping("/course/signup/communicate/{rs}")
    public String changeCourseTwo(Model model,HttpServletRequest httpServletRequest,@PathVariable boolean rs){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if(cookie.getName().equals("access_token")){
                    token = cookie.getValue();
                }
            }
            String token_valid = "Bearer "+token;
            if(token != ""){
                user = userDAO.getUserFromToken(token_valid);
            }
        }

        List<Course> courses = courseDAO.findAllCourse();
        List<UsersCourseRequest> courseRequests = courseDAO.getAllSignupCourse();
        List<Course> courseList = courseDAO.getListCourseFromUserCourseRequest(user.getId(),courseRequests);

        List<Course> courses1 = new ArrayList<>();
        courses1.addAll(courses);
        courseList.forEach(course -> {
            courses.forEach(course2 -> {
                if (course.getId() == course2.getId()){
                    courses1.remove(course2);
                }
            });
        });

        if (rs == true){
            rs = false;
        }else {
            rs = true;
        }

        model.addAttribute("users",user);
        model.addAttribute("courses",courses1);
        model.addAttribute("flag", false);
        model.addAttribute("flag1", rs);
        model.addAttribute("flag2", false);
        return "course-web";
    }

    @GetMapping("/course/signup/school/{rs}")
    public String changeCourseThree(Model model,HttpServletRequest httpServletRequest,@PathVariable boolean rs){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if(cookie.getName().equals("access_token")){
                    token = cookie.getValue();
                }
            }
            String token_valid = "Bearer "+token;
            if(token != ""){
                user = userDAO.getUserFromToken(token_valid);
            }
        }

        List<Course> courses = courseDAO.findAllCourse();
        List<UsersCourseRequest> courseRequests = courseDAO.getAllSignupCourse();
        List<Course> courseList = courseDAO.getListCourseFromUserCourseRequest(user.getId(),courseRequests);

        List<Course> courses1 = new ArrayList<>();
        courses1.addAll(courses);
        courseList.forEach(course -> {
            courses.forEach(course2 -> {
                if (course.getId() == course2.getId()){
                    courses1.remove(course2);
                }
            });
        });

        if (rs == true){
            rs = false;
        }else {
            rs = true;
        }

        model.addAttribute("users",user);
        model.addAttribute("courses",courses1);
        model.addAttribute("flag", false);
        model.addAttribute("flag1", false);
        model.addAttribute("flag2", rs);
        return "course-web";
    }
}
