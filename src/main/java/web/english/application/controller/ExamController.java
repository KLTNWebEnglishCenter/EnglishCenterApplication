package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.ExamDAO;
import web.english.application.dao.QuestionDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.exam.Exam;
import web.english.application.entity.exam.Question;
import web.english.application.entity.user.Teacher;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class ExamController {

    @Autowired
    private ExamDAO examDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private UserDAO userDAO;

    private JwtHelper jwtHelper=new JwtHelper();

    private static List<Question> questionsChoose = new ArrayList<>();

    @GetMapping("/exam")
    public String getExamPage(Model model, HttpServletRequest httpServletRequest, Authentication authentication){
        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Exam> exams = examDAO.getAll();

        model.addAttribute("exams",exams);

        return "admin/exam/quanlykiemtra";
    }

    @GetMapping("/exam/add")
    public String getAddExamPage(Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Exam exam = new Exam();

        model.addAttribute("exam",exam);

        List<Question> questions = questionDAO.getAll();

        model.addAttribute("questions",questions);

        Question question = new Question();

        model.addAttribute("question",question);

        model.addAttribute("listQuestionCheckValue",questionsChoose);

        return "admin/exam/addbaikiemtra";
    }

    @PostMapping("/exam/add")
    public String saveExam(HttpServletRequest request) throws IOException {
        int teacherId = 0;
        String name = "";
        String description = "";
        List<Integer> questionId = new ArrayList<>();

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];

                if (paramName.equals("teacherId")){
                    teacherId = Integer.parseInt(paramValue);
                }else if (paramName.equals("teacherName")){

                }
                else if (paramName.equals("name")){
                    name = paramValue;
                }else if (paramName.equals("description")){
                    description = paramValue;
                }else {
                    questionId.add(Integer.parseInt(paramValue));
                }
            }
        }
        log.info(teacherId+";"+name+";"+description+";"+questionId.toString());

        log.info("================================================");
        Exam exam = new Exam(name,description,"Ready",new Teacher());

        log.info(exam.toString());
        Exam temp =  examDAO.save(teacherId,exam);
        log.info(temp.toString());
        questionId.forEach(integer -> {
            if(integer != 0){
                examDAO.addQuestionToExam(temp.getId(),integer);
            }
        });

        questionsChoose.forEach(question -> {
            Question question1 = questionDAO.save(question);
            examDAO.addQuestionToExam(temp.getId(),question1.getId());
        });

        questionsChoose.clear();

        return "redirect:/admin/exam";
    }


    @PostMapping("/exam/question/add")
    public String saveQuestion(@ModelAttribute Question question) throws IOException {
        log.info(question.toString());
        questionsChoose.add(question);
        return "redirect:/admin/exam/add";
    }


    @GetMapping("/exam/detail/{id}")
    public String examDetail(Model model, HttpServletRequest httpServletRequest, @PathVariable int id, Authentication authentication){
        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Exam exam = examDAO.getExamById(id);

        model.addAttribute("exam",exam);

        List<Question> questions = examDAO.getListQuestionByExam(exam.getId());

        model.addAttribute("questions",questions);

        return "admin/exam/chitietkiemtra";

    }
}
