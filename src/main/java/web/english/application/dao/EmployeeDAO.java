package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.user.Employee;
import web.english.application.entity.user.Student;

import java.util.List;
@Service
@Slf4j
public class EmployeeDAO {
    @Autowired
    private RestTemplate restTemplate;

    public List<Employee> findAllEmployee(){
        ResponseEntity<List<Employee>> responseEntity =
                restTemplate.exchange("http://localhost:8000/employees/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Employee>>() {
                        });
        List<Employee> employees = responseEntity.getBody();

        return employees;
    }

    public Employee saveEmployee(Employee employee){
        Employee employee1=restTemplate.postForObject("http://localhost:8000/employee/save",employee,Employee.class);
        return  employee1;
    }

    public Employee findEmployeeById(int id){
        Employee employee=restTemplate.getForObject("http://localhost:8000/employee/"+id,Employee.class);
        return  employee;
    }
}
