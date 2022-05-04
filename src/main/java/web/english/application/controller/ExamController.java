package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import web.english.application.utils.JwtHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
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
    public String getExamPage(Model model, HttpServletRequest httpServletRequest){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        if(token != ""){
            user = userDAO.getUserFromToken(token);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

        List<Exam> exams = examDAO.getAll();

        model.addAttribute("exams",exams);

        return "admin/exam/quanlykiemtra";
    }

    @GetMapping("/exam/add")
    public String getAddExamPage(Model model,HttpServletRequest httpServletRequest){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        if(token != ""){
            user = userDAO.getUserFromToken(token);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

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
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        reader.close();
        String params = sb.toString();
        String[] _params = params.split("&");

        log.info(params);
        String[] _n;
        List<String> strings = new ArrayList<>();
        for (String param : _params) {
            log.info(param);
            _n = param.split("=");
            strings.add(_n[0]);
            strings.add(_n[1]);
        }

        int id = Integer.parseInt(strings.get(1));
        String name = strings.get(5);
        String description = strings.get(7);

        int i = 0;
        while(i < 8){
            strings.remove(0);
            i++;
        }

        int j = 0;
        List<Integer> integers = new ArrayList<>();
        int size = strings.size();
        while(j < size){
            try {
                int x = Integer.parseInt(strings.get(j));
                integers.add(x);
            }catch (Exception e){

            }
            j++;

        }

        Exam exam = new Exam(name,description,"Ready",new Teacher());

        Exam temp =  examDAO.save(id,exam);
        log.info(temp.toString());
        integers.forEach(integer -> {
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
    public String examDetail(Model model, HttpServletRequest httpServletRequest, @PathVariable int id){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        if(token != ""){
            user = userDAO.getUserFromToken(token);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);


        Exam exam = examDAO.getExamById(id);

        model.addAttribute("exam",exam);

        List<Question> questions = examDAO.getListQuestionByExam(exam.getId());

        model.addAttribute("questions",questions);

        return "admin/exam/chitietkiemtra";

    }
}
