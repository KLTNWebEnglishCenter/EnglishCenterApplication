package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.user.Teacher;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TeacherDAO {
    @Autowired
    private RestTemplate restTemplate;

    public List<Teacher> findAllTeacher(){
        ResponseEntity<List<Teacher>> responseEntity =
                restTemplate.exchange("http://localhost:8000/teachers/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Teacher>>() {
                        });
        List<Teacher> teachers = responseEntity.getBody();

        return teachers;
    }

    public Teacher saveTeacher(Teacher teacher){
        Teacher teacher1=restTemplate.postForObject("http://localhost:8000/teacher/save",teacher,Teacher.class);
        return  teacher1;
    }

    public Teacher findTeacherById(int id){
        Teacher teacher=restTemplate.getForObject("http://localhost:8000/teacher/"+id,Teacher.class);
        return  teacher;
    }
}
