package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.user.Employee;
import web.english.application.entity.user.Student;
import web.english.application.utils.UsersType;
import web.english.application.utils.Utils;

import java.util.List;
@Service
@Slf4j
public class EmployeeDAO {
    @Autowired
    private RestTemplate restTemplate;

    private Utils utils=new Utils();
    /**
     * @author VQKHANH
     * @return
     */
    public List<Employee> findAllEmployee(){
        ResponseEntity<List<Employee>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/employees/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Employee>>() {
                        });
        List<Employee> employees = responseEntity.getBody();

        return employees;
    }

    /**
     * @author VQKHANH
     * @param employee
     * @return employee data after saved to db
     */
    public String saveEmployee(Employee employee){
        String msg="";
        try {
            Employee employee1=restTemplate.postForObject("http://54.169.60.141:8000/employee/save",employee,Employee.class);
        }catch (Exception exception){
            log.info(exception.getMessage());
            msg=utils.extractMessageFromException(exception.getMessage());
            log.info(msg);
        }

        return  msg;
    }

    /**
     * @author VQKHANH
     * @param employee
     * @return employee data after saved to db
     */
    public String updateEmployee(Employee employee){
        String msg="";
        try {
        Employee employee1=restTemplate.postForObject("http://54.169.60.141:8000/employee/update",employee,Employee.class);
        }catch (Exception exception){
            log.info(exception.getMessage());
            msg=utils.extractMessageFromException(exception.getMessage());
            log.info(msg);
        }

        return  msg;
    }

    /**
     * @author VQKHANH
     * @param id
     * @return
     */
    public Employee findEmployeeById(int id){
        Employee employee=restTemplate.getForObject("http://54.169.60.141:8000/employee/"+id,Employee.class);
        return  employee;
    }

    /**
     * find the employee by id or username first, if not found, then find by full_name
     * @author VQKHANH
     * @param idOrUsername
     * @param fullName
     * @return
     */
    public List<Employee> searchUser(String idOrUsername, String fullName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("idOrUsername", idOrUsername);
        map.add("fullName", fullName);
        map.add("dtype", UsersType.EMPLOYEE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<Employee>> responseEntity =  restTemplate.exchange("http://54.169.60.141:8000/user/search",HttpMethod.POST, request,new ParameterizedTypeReference<List<Employee>>() {
        });
        List<Employee> employees = responseEntity.getBody();
        return employees;
    }
}
