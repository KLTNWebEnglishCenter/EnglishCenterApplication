package web.english.application.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.*;
import web.english.application.entity.Post;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.UsersCourseRequest;
import web.english.application.entity.exam.Exam;
import web.english.application.entity.exam.Question;
import web.english.application.entity.exam.UsersExamScores;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.service.MyEmailService;
import web.english.application.utils.JwtHelper;
import web.english.application.utils.StatusHelper;
import web.english.application.utils.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    public MyEmailService myEmailService;

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

        List<Course> courses = courseDAO.getListCourseLimit();

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
    public String getContact(HttpServletRequest httpServletRequest, Model model, @ModelAttribute("msg") String msg, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        model.addAttribute("msg",msg);
        return "contact";
    }

    @PostMapping("/contact/sendmail")
    public String sendMail(@RequestParam("name") String name,@RequestParam("email") String email,@RequestParam("phone") String phone,@RequestParam("message") String message,RedirectAttributes redirectAttributes){
        try {
            String n = "Tên:"+ name + "<br>" + "Email:" + email + "<br>" + "Nội dung:" + message;
            myEmailService.sendOtpMessage("nguyenlamit0609@gmail.com", "Khách hàng phản ảnh", n);
            redirectAttributes.addFlashAttribute("msg","Đã gửi thành công");
            return "redirect:/contact";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("msg","Không thể gửi");
            return "redirect:/contact";
        }
    }

    @GetMapping("/exam")
    public String getExamTest(HttpServletRequest httpServletRequest, Model model){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        Users user =null;
        if(!jwt.equals("")){
            String token=jwtHelper.createToken(jwt);
            user = userDAO.getUserFromToken(token);
        }

        model.addAttribute("users",user);


        List<Exam> exams = examDAO.getAll();
        Random random = new Random();
        int ran = random.nextInt(exams.size())+1;
        Exam exam = new Exam();
        List<Question> questions = new ArrayList<>();
        try {
            exam = examDAO.getExamById(ran);
            questions.addAll(examDAO.getListQuestionByExam(ran));
        }catch (Exception e){

        }
        model.addAttribute("exam",exam);
        model.addAttribute("questions",questions);

        return "kiemtra";
    }

    @PostMapping("/exam/result")
    public String testTheExam(HttpServletRequest httpServletRequest, Model model) throws IOException {
        List<String> theUserChoose = new ArrayList<>();

        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        String id = "";

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();

            String[] paramValues = httpServletRequest.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                if (paramName.equals("examId")){
                    id = paramValues[i];
                }else {
                    String paramValue = paramValues[i];
                    theUserChoose.add(paramValue);
                }
            }
        }

        log.info(id);

        Exam exam = examDAO.getExamById(Integer.parseInt(id));
        List<Question> questions = examDAO.getListQuestionByExam(Integer.parseInt(id));

        List<String> theResult = new ArrayList<>();
        questions.forEach(question -> {
            theResult.add(question.getCorrectAnswer());
        });

        int point = utils.checkPoint(theUserChoose,theResult);

        diem = point;
        return "redirect:/home";
    }

    @GetMapping("/user/exam")
    public String getExamPage(HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Exam> exams = examDAO.getAll();

        model.addAttribute("exams",exams);

        return "student/studentExam";
    }

    @GetMapping("/user/exam/{id}")
    public String getExam(HttpServletRequest httpServletRequest, Model model, @PathVariable int id, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Exam exam = examDAO.getExamById(id);

        model.addAttribute("exam",exam);

        List<Question> questions = examDAO.getListQuestionByExam(exam.getId());

        model.addAttribute("questions",questions);

        return "student/studentExamQuetion";
    }

    @PostMapping("/user/exam/result")
    public String markTheExam(HttpServletRequest req, Model model, Authentication authentication) throws IOException {

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<String> theUserChoose = new ArrayList<>();

        Enumeration<String> parameterNames = req.getParameterNames();

        String[] examId = req.getParameterValues("examId");
        int eId = Integer.parseInt(examId[0]);

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            String[] paramValues = req.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                if (!paramName.equals("examId")){
                    theUserChoose.add(paramValue);
                }
            }
        }

        Exam exam = examDAO.getExamById(eId);
        List<Question> questions = examDAO.getListQuestionByExam(eId);

        List<String> theResult = new ArrayList<>();
        questions.forEach(question -> {
            theResult.add(question.getCorrectAnswer());
        });

        questions.forEach(e->{
            log.info(e.toString());
        });

        theUserChoose.forEach(a->{
            log.info(a);
        });

        int point = utils.checkPoint(theUserChoose,theResult);

        model.addAttribute("point",point);
        model.addAttribute("sl",theResult.size());
        model.addAttribute("exam",exam);
        model.addAttribute("questions",questions);

        int score = studentDAO.getScoreOfStudentByExam(userDetails.getUsers().getId(),eId);
        if (score < point){
            UsersExamScores scores = studentDAO.saveScore(userDetails.getUsers().getId(),eId,point);
        }
        return "student/studentExamResult";
    }

    @GetMapping("/posts")
    public String getPostPage(HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Post> posts = postDAO.getAllPostWithStatusHasAccept();
        model.addAttribute("posts",posts);

        return "student/studentPost";
    }

    @GetMapping("/myPost")
    public String getMyPostPage(HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Post> posts = postDAO.getMyPost(userDetails.getUsers().getId());
        model.addAttribute("posts",posts);

        return "student/myStudentPost";
    }

    @GetMapping("/newPost")
    public String getSavePostPage(HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Post post = new Post();
        model.addAttribute("post",post);
        return "student/addnewpost";
    }

    @PostMapping("/new/post/save")
    public String savePostPage(@ModelAttribute Post post, @RequestParam("username") String username){
        Users users = new Users(username);
        post.setUsers(users);
        post.setStatus(StatusHelper.STATUS_NO_ACCEPT);
        postDAO.savePost(post);
        return "redirect:/myPost";
    }

    @GetMapping("/course/{id}")
    public String getMyCourse(@PathVariable int id,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<UsersCourseRequest> courseRequests = studentDAO.getCourseRequestOfStudent(id);

        model.addAttribute("courseRequests",courseRequests);

        return "student/myStudentCourse";
    }

    @GetMapping("/myPost/delete/{id}")
    public String deletePost(@PathVariable int id){
        Post post = postDAO.deletePost(id);
        return "redirect:/myPost";
    }

    @GetMapping("/score")
    public String getScoresPage(HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<UsersExamScores> usersExamScores = studentDAO.getScoreOfStudent(userDetails.getUsers().getId());
        model.addAttribute("scores",usersExamScores);
        return "student/studentScores";
    }

}
