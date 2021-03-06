package web.english.application.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.UsersCourseRequest;
import web.english.application.entity.course.UsersCourseRequestKey;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;

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

    private JwtHelper jwtHelper=new JwtHelper();

    @GetMapping("/course/home")
    public String getCourseHome(Model model, HttpServletRequest httpServletRequest){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        Users user =null;
        if(!jwt.equals("")){
            String token=jwtHelper.createToken(jwt);
            user = userDAO.getUserFromToken(token);
        }

        model.addAttribute("users",user);

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

        model.addAttribute("courses",courses1);
        model.addAttribute("flag", false);
        model.addAttribute("flag1", false);
        model.addAttribute("flag2", false);
        return "course-web";
    }

    @GetMapping("/course/signup/{id}")
    public String signupCourse(@PathVariable int id, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        Users user =null;
        if(!jwt.equals("")){
            String token=jwtHelper.createToken(jwt);
            user = userDAO.getUserFromToken(token);
        }

        if(user == null){
            redirectAttributes.addFlashAttribute("msg","Vui l??ng ????ng nh???p tr?????c khi ????ng k?? kh??a h???c!");
            return "redirect:/course/home";
        }
        model.addAttribute("users",user);

        Course course = courseDAO.getCourse(id);
        Student student = new Student(user.getId(),user.getUsername(),user.getPassword(),user.getFullName(),user.getDob(),user.getGender(),user.getEmail(),user.getPhoneNumber(),user.isEnable());
        UsersCourseRequestKey key = new UsersCourseRequestKey(user.getId(),id);
        UsersCourseRequest request = new UsersCourseRequest(key,student,course);
        UsersCourseRequest signupCourse = courseDAO.signupCourse(request);

        if(signupCourse != null)
            redirectAttributes.addFlashAttribute("msg","????ng k?? th??nh c??ng!");
        else
            redirectAttributes.addFlashAttribute("msg","????ng k?? kh??ng th??nh c??ng!");

        return "redirect:/course/home";
    }

    @GetMapping("/course/signup/toeic/{rs}")
    public String changeCourse(Model model,HttpServletRequest httpServletRequest,@PathVariable boolean rs){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        Users user =null;
        if(!jwt.equals("")){
            String token=jwtHelper.createToken(jwt);
            user = userDAO.getUserFromToken(token);
        }

        model.addAttribute("users",user);

        List<Course> courses = new ArrayList<>();
        if (rs == true){
            rs = false;
            courses.addAll( courseDAO.findAllCourse());
        }else {
            rs = true;
            courses.addAll(courseDAO.findByCategory(1));
        }


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

        model.addAttribute("courses",courses1);
        model.addAttribute("flag", rs);
        model.addAttribute("flag1", false);
        model.addAttribute("flag2", false);
        return "course-web";
    }

    @GetMapping("/course/signup/communicate/{rs}")
    public String changeCourseTwo(Model model,HttpServletRequest httpServletRequest,@PathVariable boolean rs){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        Users user =null;
        if(!jwt.equals("")){
            String token=jwtHelper.createToken(jwt);
            user = userDAO.getUserFromToken(token);
        }

        model.addAttribute("users",user);

        List<Course> courses = new ArrayList<>();
        if (rs == true){
            rs = false;
            courses.addAll( courseDAO.findAllCourse());
        }else {
            rs = true;
            courses.addAll(courseDAO.findByCategory(2));
        }

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

        model.addAttribute("courses",courses1);
        model.addAttribute("flag", false);
        model.addAttribute("flag1", rs);
        model.addAttribute("flag2", false);
        return "course-web";
    }

    @GetMapping("/course/signup/school/{rs}")
    public String changeCourseThree(Model model,HttpServletRequest httpServletRequest,@PathVariable boolean rs){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        Users user =null;
        if(!jwt.equals("")){
            String token=jwtHelper.createToken(jwt);
            user = userDAO.getUserFromToken(token);
        }

        model.addAttribute("users",user);

        List<Course> courses = new ArrayList<>();
        if (rs == true){
            rs = false;
            courses.addAll( courseDAO.findAllCourse());
        }else {
            rs = true;
            courses.addAll(courseDAO.findByCategory(3));
        }

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

        model.addAttribute("courses",courses1);
        model.addAttribute("flag", false);
        model.addAttribute("flag1", false);
        model.addAttribute("flag2", rs);
        return "course-web";
    }


    @PostMapping("/course/search")
    public String searchCourse(@RequestParam("idOrName") String idOrName, HttpServletRequest httpServletRequest, Model model){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        Users user =null;
        if(!jwt.equals("")){
            String token=jwtHelper.createToken(jwt);
            user = userDAO.getUserFromToken(token);
        }

        model.addAttribute("users",user);

        List<Course> courses = courseDAO.findByIdOrCourseName(idOrName);
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

        model.addAttribute("courses",courses1);
        model.addAttribute("flag", false);
        model.addAttribute("flag1", false);
        model.addAttribute("flag2", false);
        return "course-web";
    }
}
