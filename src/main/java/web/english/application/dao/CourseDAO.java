package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.course.Course;

import java.util.List;

@Service
@Slf4j
public class CourseDAO {
    @Autowired
    private RestTemplate restTemplate;

    public List<Course> findAllCourse(){
        ResponseEntity<List<Course>> responseEntity =
                restTemplate.exchange("http://localhost:8000/course/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Course>>() {
                        });
        List<Course> courses = responseEntity.getBody();

        return courses;
    }

    public Course getCourse(int id){
        Course course = restTemplate.getForObject(("http://localhost:8000/course/"+id),Course.class);
        return course;
    }
}
