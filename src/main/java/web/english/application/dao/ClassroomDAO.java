package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.Classroom;

import java.util.List;

@Service
@Slf4j
public class ClassroomDAO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    public List<Classroom> findAll(){
        ResponseEntity<List<Classroom>> responseEntity =
                restTemplate.exchange("http://localhost:8000/classrooms/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Classroom>>() {
                        });
        List<Classroom> classrooms = responseEntity.getBody();
        return classrooms;
    }

    public Classroom saveClassroom(Classroom classroom, int teacher_id,int course_id){
        Classroom classroom1=restTemplate.postForObject("http://localhost:8000/classroom/save",classroom,Classroom.class);
        Classroom classroom2 = restTemplate.getForObject("http://localhost:8000/classroom/addTeacherAndCourseToClassroom/"+classroom1.getId()+"/"+teacher_id+"/"+course_id,Classroom.class);
        return  classroom2;
    }

    public Classroom getClassroom(int id){
        Classroom classroom = restTemplate.getForObject("http://localhost:8000/classroom/"+id,Classroom.class);
        return classroom;
    }

    public Classroom deleteClassroom(int id){
        Classroom classroom = restTemplate.getForObject("http://localhost:8000/classroom/delete/"+id,Classroom.class);
        return classroom;
    }
}
