package web.english.application.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.ExamDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.course.Course;
import web.english.application.entity.exam.Exam;
import web.english.application.entity.exam.Question;
import web.english.application.entity.user.Users;
import web.english.application.utils.JwtHelper;
import web.english.application.utils.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class UserHomeController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private ExamDAO examDAO;

    private JwtHelper jwtHelper=new JwtHelper();

    private Utils utils = new Utils();

    private static int diem = -1;

    @GetMapping("/home")
    public String getHome(HttpServletRequest httpServletRequest, Model model){
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

        if(diem != -1){
            model.addAttribute("msg",diem+"");
            diem = -1;
        }


        return "home";
    }

    @GetMapping("/about")
    public String getAbout(HttpServletRequest httpServletRequest, Model model){
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

        model.addAttribute("users",user);
        return "about";
    }

    @GetMapping("/contact")
    public String getContact(HttpServletRequest httpServletRequest, Model model){
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

        model.addAttribute("users",user);
        return "contact";
    }

    @GetMapping("/exam")
    public String getExamTest(HttpServletRequest httpServletRequest, Model model){
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

        model.addAttribute("users",user);


        Exam exam = examDAO.getExamById(Utils.exam);

        model.addAttribute("exam",exam);

        List<Question> questions = examDAO.getListQuestionByExam(exam.getId());

        model.addAttribute("questions",questions);

        return "kiemtra";
    }

    @PostMapping("/exam/result")
    public String testTheExam(HttpServletRequest httpServletRequest, Model model) throws IOException {
        BufferedReader reader = httpServletRequest.getReader();
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        reader.close();
        String params = sb.toString();
        String[] _params = params.split("&");

        List<String> theUserChoose = new ArrayList<>();
        for (String param : _params) {
            theUserChoose.add(param.substring(8));
        }

        Exam exam = examDAO.getExamById(Utils.exam);
        List<Question> questions = examDAO.getListQuestionByExam(Utils.exam);

        List<String> theResult = new ArrayList<>();
        questions.forEach(question -> {
            theResult.add(question.getCorrectAnswer());
        });

        int point = utils.checkPoint(theUserChoose,theResult);

        diem = point;
        return "redirect:/home";
    }

}
