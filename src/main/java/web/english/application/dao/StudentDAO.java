package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Teacher;
import web.english.application.utils.UsersType;

import java.util.List;

@Service
@Slf4j
public class StudentDAO {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * @author VQKHANH
     * @return
     */
    public List<Student> findAllStudent(){
        ResponseEntity<List<Student>> responseEntity =
                restTemplate.exchange("http://localhost:8000/students/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                        });
        List<Student> students = responseEntity.getBody();

        return students;
    }

    /**
     * @author VQKHANH
     * @param student
     * @return student data after saved to db
     */
    public Student saveStudent(Student student){
        Student student1=restTemplate.postForObject("http://localhost:8000/student/save",student,Student.class);
        return  student1;
    }

    /**
     * @author VQKHANH
     * @param id
     * @return
     */
    public Student findStudentById(int id){
        Student student=restTemplate.getForObject("http://localhost:8000/student/"+id,Student.class);
        return  student;
    }

    /**
     * find the student by id or username first, if not found, then find by full_name
     * @author VQKHANH
     * @param idOrUsername
     * @param fullName
     * @return
     */
    public List<Student> searchUser(String idOrUsername, String fullName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("idOrUsername", idOrUsername);
        map.add("fullName", fullName);
        map.add("dtype", UsersType.STUDENT);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<Student>> responseEntity =  restTemplate.exchange("http://localhost:8000/user/search",HttpMethod.POST, request,new ParameterizedTypeReference<List<Student>>() {
        });
        List<Student> students = responseEntity.getBody();
        return students;
    }
}