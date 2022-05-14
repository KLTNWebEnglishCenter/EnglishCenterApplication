package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.StudentDAO;
import web.english.application.dao.UserDAO;
import web.english.application.dao.UserRequestCourseStatusDAO;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.UsersCourseRequest;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class UserRequestCourseStatusController {
    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserRequestCourseStatusDAO userRequestCourseStatusDAO;

    private JwtHelper jwtHelper=new JwtHelper();

    @GetMapping("/requestcourse")
    public String getUpdateUserRequestCourseStatusPage(HttpServletRequest httpServletRequest, Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<UsersCourseRequest> usersCourseRequests=userRequestCourseStatusDAO.findAllUsersCourseRequest();
        model.addAttribute("usersCourseRequests",usersCourseRequests);

        return "/admin/updateStudentRequestCourseStatus/requestcourse";
    }

    @PostMapping("/requestcourse/status/update")
    public String updateUserRequestCourseStatus(@RequestParam int studentId,@RequestParam int courseId,@RequestParam String status, RedirectAttributes redirectAttributes){

        String msg=studentDAO.updateStudentRequestCourseStatus(studentId,courseId,status);
        redirectAttributes.addFlashAttribute("msg",msg);

        return "redirect:/admin/requestcourse";
    }

    @PostMapping("/requestcourse/search")
    public String search(HttpServletRequest httpServletRequest,Model model,@RequestParam String courseIdOrName,@RequestParam String studentIdOrFullName, Authentication authentication){
        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<UsersCourseRequest> usersCourseRequests=userRequestCourseStatusDAO.search(courseIdOrName, studentIdOrFullName);
        model.addAttribute("usersCourseRequests",usersCourseRequests);

        return "/admin/updateStudentRequestCourseStatus/requestcourse";
    }
}
