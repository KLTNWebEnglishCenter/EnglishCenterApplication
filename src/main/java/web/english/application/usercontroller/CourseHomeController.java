package web.english.application.usercontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.UsersCourseRequest;
import web.english.application.entity.course.UsersCourseRequestKey;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Users;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

        model.addAttribute("users",user);
        model.addAttribute("courses",courses);
        return "course-web";
    }

    @GetMapping("/course/signup/{id}")
    public String signupCourse(@PathVariable int id, Model model, HttpServletRequest httpServletRequest){
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
        courseDAO.signupCourse(request);
        return "redirect:/course/home";
    }

}
