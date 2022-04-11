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

    /**
     * get the student management page
     * @author VQKHANH
     * @param model
     * @return
     */
    @GetMapping("/student")
    public String getStudent(Model model){
        List<Student> students=studentDAO.findAllStudent();
        model.addAttribute("students",students);
        return "admin/student/student";
    }

    /**
     * get add student page
     * @author VQKHANH
     * @param model
     * @return
     */
    @GetMapping("/addstudent")
    public String getAddStudentPage(Model model){
        Student student=new Student();
        student.setGender("Nam");
        model.addAttribute("student",student);
        return  "admin/student/addstudent";
    }

    /**
     * save new student
     * @author VQKHANH
     * @param student
     * @param model
     * @return
     */
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

    /**
     * get student info editing page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editstudent/{id}")
    public String getEditStudentPage(@PathVariable("id") int id, Model model){
        Student student=studentDAO.findStudentById(id);
        if(student.getGender()==null)student.setGender("Nam");
//        log.info(teacher.toString());
        model.addAttribute("student",student);
        return "admin/student/editstudent";
    }

    /**
     * save student info after editing
     * @author VQKHANH
     * @param student
     * @param model
     * @return
     */
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

    /**
     * get student info page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/studentinfo/{id}")
    public String getStudentInfoPage(@PathVariable("id") int id,Model model){
        Student student=studentDAO.findStudentById(id);
//        log.info(teacher.toString());
        model.addAttribute("student",student);
        return "admin/student/studentinfo";
    }

    /**
     * search student by id or username or full_name
     * @author VQKHANH
     * @param idOrUsername
     * @param fullName
     * @param model
     * @return
     */
    @PostMapping("/student/search")
    public String searchStudent(@RequestParam String idOrUsername, @RequestParam String fullName,Model model){
        List<Student> students=studentDAO.searchUser(idOrUsername,fullName);

        model.addAttribute("students",students);
        return "admin/student/student";
    }
}
