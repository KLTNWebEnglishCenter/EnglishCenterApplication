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
import web.english.application.entity.Notification;
import web.english.application.entity.ScheduleInfoHolder;
import web.english.application.entity.user.Teacher;
import web.english.application.utils.UsersType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TeacherDAO {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * @author VQKHANH
     * @return list all teacher
     */
    public List<Teacher> findAllTeacher(){
        ResponseEntity<List<Teacher>> responseEntity =
                restTemplate.exchange("http://localhost:8000/teachers/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Teacher>>() {
                        });
        List<Teacher> teachers = responseEntity.getBody();

        return teachers;
    }

    /**
     * @author VQKHANH
     * @param teacher
     * @return teacher data after saved to db
     */
    public Teacher saveTeacher(Teacher teacher){
        Teacher teacher1=restTemplate.postForObject("http://localhost:8000/teacher/save",teacher,Teacher.class);
        return  teacher1;
    }

    /**
     * @author VQKHANH
     * @param id
     * @return
     */
    public Teacher findTeacherById(int id){
        Teacher teacher=restTemplate.getForObject("http://localhost:8000/teacher/"+id,Teacher.class);
        return  teacher;
    }

    /**
     * find the teacher by id or username first, if not found, then find by full_name
     * @author VQKHANH
     * @param idOrUsername
     * @param fullName
     * @return
     */
    public List<Teacher> searchUser(String idOrUsername, String fullName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("idOrUsername", idOrUsername);
        map.add("fullName", fullName);
        map.add("dtype", UsersType.TEACHER);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<Teacher>> responseEntity =  restTemplate.exchange("http://localhost:8000/user/search",HttpMethod.POST, request,new ParameterizedTypeReference<List<Teacher>>() {
            });
        List<Teacher> teachers = responseEntity.getBody();
        return teachers;
    }

    /**
     * @author VQKHANH
     * @param teacherid
     * @return
     */
    public List<Classroom> getAllClassroomOfTeacher(int teacherid){
        ResponseEntity<List<Classroom>> responseEntity=restTemplate.exchange("http://localhost:8000/teacher/classrooms/" + teacherid, HttpMethod.GET, null, new ParameterizedTypeReference<List<Classroom>>() {
        });
        return responseEntity.getBody();
    }
}
