package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.StudentDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Teacher;
import web.english.application.utils.Utils;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class StudentController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private StudentDAO studentDAO;

    private Utils utils=new Utils();

    @GetMapping("/student")
    public String getStudent(Model model){
        List<Student> students=studentDAO.findAllStudent();
        model.addAttribute("students",students);
        return "admin/student/student";
    }

    @GetMapping("/addstudent")
    public String getAddStudentPage(Model model){
        Student student=new Student();
        student.setGender("Nam");
        model.addAttribute("student",student);
        return  "admin/student/addstudent";
    }
    
    @PostMapping("/student/add")
    public String saveStudent(@ModelAttribute Student student, Model model){

        if(!utils.checkFullNameFormat(student.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/student/addstudent";
        }
        if(!utils.checkEmailFormat(student.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/student/addstudent";
        }
        if(!utils.checkPhoneNumberFormat(student.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return  "admin/student/addstudent";
        }

        if(!utils.checkUsernameFormat(student.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/student/addstudent";
        }

        if(!utils.checkDob(student.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/student/addstudent";
        }


        student.setEnable(true);
        log.info(student.toString());
        studentDAO.saveStudent(student);
        return "redirect:/admin/student";
    }

    @GetMapping("/editstudent/{id}")
    public String getEditStudentPage(@PathVariable("id") int id, Model model){
//        log.info(id+"");
        Student student=studentDAO.findStudentById(id);
        if(student.getGender()==null)student.setGender("Nam");
//        log.info(teacher.toString());
        model.addAttribute("student",student);
        return "admin/student/editstudent";
    }

    @PostMapping("/student/edit")
    public String editStudent(@ModelAttribute Student student,Model model){

        if(!utils.checkFullNameFormat(student.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/student/editstudent";
        }
        if(!utils.checkEmailFormat(student.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/student/editstudent";
        }
        if(!utils.checkPhoneNumberFormat(student.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return  "admin/student/editstudent";
        }

        if(!utils.checkUsernameFormat(student.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/student/editstudent";
        }

        if(!utils.checkDob(student.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/student/editstudent";
        }


        student.setEnable(true);
        log.info(student.toString());

        studentDAO.saveStudent(student);
        return "redirect:/admin/student";
    }

    @GetMapping("/studentinfo/{id}")
    public String getStudentInfoPage(@PathVariable("id") int id,Model model){
//        log.info(id+"");
        Student student=studentDAO.findStudentById(id);
//        log.info(teacher.toString());
        model.addAttribute("student",student);
        return "admin/student/studentinfo";
    }
}
