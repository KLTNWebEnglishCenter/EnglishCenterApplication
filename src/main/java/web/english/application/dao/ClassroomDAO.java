package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.Classroom;
import web.english.application.entity.course.Course;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Teacher;
import web.english.application.utils.UsersType;

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

    public List<Classroom> findClassroomByCourseId(int courseId){
        ResponseEntity<List<Classroom>> responseEntity =
                restTemplate.exchange("http://localhost:8000/classroom/course/"+courseId,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Classroom>>() {
                        });
        List<Classroom> classrooms=responseEntity.getBody();
        return classrooms;
    }

    /**
     * @author VQKHANH
     * @param classroomId
     * @param students
     * @return
     */
    public String addStudentToClassroom(int classroomId, List<Student> students){
//        /classroom/addstudent/{classroomId}
        try {
        restTemplate.put("http://localhost:8000/classroom/addstudent/"+classroomId,students);
            return "Cập nhật thành công";
        }catch (Exception exception){
            log.info("addStudentToClassroom Error:"+ exception.getMessage());
            if(exception.getMessage().contains("could not execute statement; SQL [n/a]; constraint [null]"))
                return "Học sinh đã có sẵn trong lớp học";
            return "Cập nhật không thành công";
        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, List<Student>> map= new LinkedMultiValueMap<>();
//        map.add("students", students);
//
//        HttpEntity<MultiValueMap<String, List<Student>>> request = new HttpEntity<MultiValueMap<String, List<Student>>>(map, headers);
//
//        try {
//            ResponseEntity<Classroom> responseEntity =  restTemplate.exchange("http://localhost:8000/classroom/addstudent/"+classroomId,HttpMethod.PUT, request,new ParameterizedTypeReference<Classroom>() {
//            });
//            Classroom classroom=responseEntity.getBody();
//            log.info("addStudentToClassroomcomplete");
//            if(classroom==null) return "Cập nhật không thành công";
//            return "Cập nhật thành công";
//        }catch (Exception exception){
//            log.info("addStudentToClassroom Error:"+ exception.getMessage());
//            return "Cập nhật không thành công";
//        }
    }


    public List<Classroom> findByIdOrClassName(String idOrName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("idOrClassName", idOrName);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<Classroom>> responseEntity =
                restTemplate.exchange("http://localhost:8000/classroom/findbyidorclassname",
                        HttpMethod.POST, request, new ParameterizedTypeReference<List<Classroom>>() {
                        });
        List<Classroom> classrooms = responseEntity.getBody();
        return classrooms;
    }
}
