package web.english.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.ClassroomDAO;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.StudentDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.course.Course;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Users;
import web.english.application.utils.UserRequestStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class ApproveStudentsController {

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private ClassroomDAO classroomDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private UserDAO userDAO;

    /**
     * @author VQKHANH
     * @param model
     * @return
     */
    @GetMapping("/approvestudent")
    public String getApproveStudentsPage( Model model,HttpServletRequest httpServletRequest){
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
        List<Course> courses=courseDAO.findAllCourse();
        model.addAttribute("courses",courses);
        return "admin/approvestudent";
    }

    /**
     * @author VQKHANH
     * @param idOrCourseName
     * @param model
     * @return
     */
    @PostMapping("/approvestudent/course/search")
    public String searchCourseById(@RequestParam String idOrCourseName,Model model,HttpServletRequest httpServletRequest){
        log.info(idOrCourseName);
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
        List<Course> courses=courseDAO.findByIdOrCourseName(idOrCourseName);
        model.addAttribute("courses",courses);
        return "admin/approvestudent";
    }

    /**
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/approvestudent/search")
    public String searchClassroomAndStudentWithSpecifyCourse(@RequestParam int id, Model model,HttpServletRequest httpServletRequest){
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
        List<Course> courses=courseDAO.findAllCourse();
        List<Classroom> classrooms=classroomDAO.findClassroomByCourseId(id);
        List<Student> students=studentDAO.findStudentRequestToJoinByCourseId(id);

        model.addAttribute("courses",courses);
        model.addAttribute("classrooms",classrooms);
        model.addAttribute("students",students);
        return "admin/approvestudent";
    }

    /**
     * @author VQKHANH
     * @param classroomId
     * @param students
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/approvestudent/addstudenttoclassroom")
    public String addStudentToClassroom(@RequestParam int classroomId,@RequestParam String students, RedirectAttributes redirectAttributes) throws JsonProcessingException {
//        log.info(classroomId+"");
//        log.info(students);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Student> studentList = objectMapper.readValue(students, new TypeReference<List<Student>>(){});

        String msg=classroomDAO.addStudentToClassroom(classroomId,studentList);
        redirectAttributes.addFlashAttribute("msg",msg);

        Classroom classroom=classroomDAO.getClassroom(classroomId);
        int courseId=classroom.getCourse().getId();

        for (Student student :
                studentList) {
            studentDAO.updateStudentRequestCourseStatus(student.getId(),courseId, UserRequestStatus.APPROVED);
        }

//        log.info(studentList.size()+"");
        return "redirect:/admin/approvestudent";
    }
}
