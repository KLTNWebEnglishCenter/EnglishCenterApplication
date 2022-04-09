package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Teacher;

import java.util.List;

@Service
@Slf4j
public class StudentDAO {
    @Autowired
    private RestTemplate restTemplate;

    public List<Student> findAllStudent(){
        ResponseEntity<List<Student>> responseEntity =
                restTemplate.exchange("http://localhost:8000/students/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                        });
        List<Student> students = responseEntity.getBody();

        return students;
    }

    public Student saveStudent(Student student){
        Student student1=restTemplate.postForObject("http://localhost:8000/student/save",student,Student.class);
        return  student1;
    }

    public Student findStudentById(int id){
        Student student=restTemplate.getForObject("http://localhost:8000/student/"+id,Student.class);
        return  student;
    }
}
