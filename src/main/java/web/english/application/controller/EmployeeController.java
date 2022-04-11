package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.EmployeeDAO;
import web.english.application.dao.StudentDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Employee;
import web.english.application.entity.user.Student;
import web.english.application.utils.Utils;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class EmployeeController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    private Utils utils=new Utils();

    /**
     * get the employee management page
     * @author VQKHANH
     * @param model
     * @return
     */
    @GetMapping("/employee")
    public String getEmployee(Model model){
        List<Employee> employees=employeeDAO.findAllEmployee();
        model.addAttribute("employees",employees);
        return "admin/employee/employee";
    }

    /**
     * get add employee page
     * @author VQKHANH
     * @param model
     * @return
     */
    @GetMapping("/addemployee")
    public String getAddEmployeePage(Model model){
        Employee employee=new Employee();
        employee.setGender("Nam");
        model.addAttribute("employee",employee);
        return  "admin/employee/addemployee";
    }

    /**
     * save new employee
     * @author VQKHANH
     * @param employee
     * @param model
     * @return
     */
    @PostMapping("/employee/add")
    public String saveEmployee(@ModelAttribute Employee employee, Model model){

        if(!utils.checkFullNameFormat(employee.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/employee/addemployee";
        }
        if(!utils.checkEmailFormat(employee.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/employee/addemployee";
        }
        if(!utils.checkPhoneNumberFormat(employee.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber",Utils.phoneNumberRequire);
            return  "admin/employee/addemployee";
        }

        if(!utils.checkUsernameFormat(employee.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/employee/addemployee";
        }

        if(!utils.checkDob(employee.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/employee/addemployee";
        }


        employee.setEnable(true);
        log.info(employee.toString());
        employeeDAO.saveEmployee(employee);
        return "redirect:/admin/employee";
    }

    /**
     * get employee info editing page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editemployee/{id}")
    public String getEditEmployeePage(@PathVariable("id") int id, Model model){
//        log.info(id+"");
        Employee employee=employeeDAO.findEmployeeById(id);
        if(employee.getGender()==null)employee.setGender("Nam");
//        log.info(teacher.toString());
        model.addAttribute("employee",employee);
        return "admin/employee/editemployee";
    }

    /**
     * save employee info after editing
     * @author VQKHANH
     * @param employee
     * @param model
     * @return
     */
    @PostMapping("/employee/edit")
    public String editEmployee(@ModelAttribute Employee employee,Model model){

        if(!utils.checkFullNameFormat(employee.getFullName())){
            model.addAttribute("errorFullName",Utils.fullNameRequire);
            return  "admin/employee/editemployee";
        }
        if(!utils.checkEmailFormat(employee.getEmail())){
            model.addAttribute("errorEmail",Utils.emailRequire);
            return  "admin/employee/editemployee";
        }
        if(!utils.checkPhoneNumberFormat(employee.getPhoneNumber())){
            model.addAttribute("errorPhoneNumber", Utils.phoneNumberRequire);
            return  "admin/employee/editemployee";
        }

        if(!utils.checkUsernameFormat(employee.getUsername())){
            model.addAttribute("errorUsername",Utils.usernameRequire);
            return  "admin/employee/editemployee";
        }

        if(!utils.checkDob(employee.getDob())){
            model.addAttribute("errorDob",Utils.yearRequire);
            return  "admin/employee/editemployee";
        }


        employee.setEnable(true);
        log.info(employee.toString());
        employeeDAO.saveEmployee(employee);
        return "redirect:/admin/employee";
    }

    /**
     * get employee info page
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/employeeinfo/{id}")
    public String getStudentInfoPage(@PathVariable("id") int id,Model model){
//        log.info(id+"");
        Employee employee=employeeDAO.findEmployeeById(id);
//        log.info(teacher.toString());
        model.addAttribute("employee",employee);
        return "admin/employee/employeeinfo";
    }

    /**
     * search employee by id or username or full_name
     * @author VQKHANH
     * @param idOrUsername
     * @param fullName
     * @param model
     * @return
     */
    @PostMapping("/employee/search")
    public String searchEmployee(@RequestParam String idOrUsername, @RequestParam String fullName,Model model){
//        log.info(idOrUsername);
//        log.info(fullName);
        List<Employee> employees=employeeDAO.searchUser(idOrUsername,fullName);

        model.addAttribute("employees",employees);
        return "admin/employee/employee";
    }
}
