package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.Notification;
import web.english.application.entity.course.UsersCourseRequest;
import web.english.application.entity.course.Course;
import web.english.application.entity.exam.UsersExamScores;
import web.english.application.entity.exam.UsersExamScoresKey;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Teacher;
import web.english.application.utils.UsersType;
import web.english.application.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StudentDAO {
    @Autowired
    private RestTemplate restTemplate;

    private Utils utils=new Utils();

    /**
     * @author VQKHANH
     * @return
     */
    public List<Student> findAllStudent(){
        ResponseEntity<List<Student>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/students/",
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
    public String saveStudent(Student student){
        String msg="";
        try {
        Student student1=restTemplate.postForObject("http://54.169.60.141:8000/student/save",student,Student.class);
        }catch (Exception exception){
            log.info(exception.getMessage());
            msg=utils.extractMessageFromException(exception.getMessage());
            log.info(msg);
        }

        return  msg;
    }

    /**
     * @author VQKHANH
     * @param student
     * @return student data after saved to db
     */
    public String updateStudent(Student student){
        String msg="";
        try {
            Student student1=restTemplate.postForObject("http://54.169.60.141:8000/student/update",student,Student.class);
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
    public Student findStudentById(int id){
        Student student=restTemplate.getForObject("http://54.169.60.141:8000/student/"+id,Student.class);
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

        ResponseEntity<List<Student>> responseEntity =  restTemplate.exchange("http://54.169.60.141:8000/user/search",HttpMethod.POST, request,new ParameterizedTypeReference<List<Student>>() {
        });
        List<Student> students = responseEntity.getBody();
        return students;
    }

    public List<Student> findStudentRequestToJoinByCourseId(int courseId){
        ResponseEntity<List<Student>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/student/course/"+courseId,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                        });
        List<Student> students=responseEntity.getBody();
        return  students;
    }

    /**
     * @author VQKHANH
     * @param studentId
     * @param courseId
     * @return
     */
    public String updateStudentRequestCourseStatus(int studentId, int courseId,String status){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("studentId", studentId+"");
            map.add("courseId", courseId+"");
            map.add("status", status);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            ResponseEntity<UsersCourseRequest> responseEntity =  restTemplate.exchange("http://54.169.60.141:8000/student/requestcourse/status/",HttpMethod.POST, request,new ParameterizedTypeReference<UsersCourseRequest>() {
            });
//           restTemplate.put("http://54.169.60.141:8000/student/requestcourse/status/"+studentId+"/"+courseId+"/"+status,null);
           return "C???p nh???t th??nh c??ng";
        }catch (Exception exception){
//            exception.printStackTrace();
            log.info("updateStudentRequestCourseStatus Error:"+ exception.getMessage());
            return "C???p nh???t kh??ng th??nh c??ng";
        }
    }

    /**
     * for manage classroom of student
     * @param token
     * @return
     */
    public List<Classroom> getAllClassroomOfStudent(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Classroom>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/student/classrooms/",
                        HttpMethod.GET, request, new ParameterizedTypeReference<List<Classroom>>() {
                        });
        List<Classroom> classrooms = responseEntity.getBody();
        return classrooms;
    }

    /**
     * for manage classroom of student
     * @param classroomId
     * @return
     */
    public Classroom getClassroomOfStudent(int classroomId){
        Classroom classroom=restTemplate.getForObject("http://54.169.60.141:8000/student/classroom/"+classroomId,Classroom.class);
        return  classroom;
    }

    /**
     * for manage classroom of student
     * @param token
     * @return
     */
    public List<Notification> getNotificationsOfStudent(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Notification>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/student/notifications/",
                        HttpMethod.GET, request, new ParameterizedTypeReference<List<Notification>>() {
                        });
        List<Notification> notifications = responseEntity.getBody();
        return  notifications;
    }

    public List<UsersCourseRequest> getCourseRequestOfStudent(int studentId){
        ResponseEntity<List<UsersCourseRequest>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/student/list/course/" + studentId,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UsersCourseRequest>>() {
                        });
        List<UsersCourseRequest> courseRequests = responseEntity.getBody();
        List<UsersCourseRequest> usersCourseRequests = new ArrayList<>();
        courseRequests.forEach(request -> {
            Course course = restTemplate.getForObject(("http://54.169.60.141:8000/course/find/" + request.getUserRequestCourseKey().getCourseId()), Course.class);
            UsersCourseRequest usersCourseRequest = new UsersCourseRequest();
            usersCourseRequest.setUserRequestCourseKey(request.getUserRequestCourseKey());
            usersCourseRequest.setCourse(course);
            usersCourseRequest.setStatus(request.getStatus());

            usersCourseRequests.add(usersCourseRequest);
        });
        return usersCourseRequests;
    }

    public List<UsersExamScores> getScoreOfStudent(int studentId){
        ResponseEntity<List<UsersExamScores>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/student/score/" + studentId,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UsersExamScores>>() {
                        });
        List<UsersExamScores> usersExamScores = responseEntity.getBody();
        return usersExamScores;
    }

    public UsersExamScores saveScore(int studentId,int examId,int score){
        UsersExamScoresKey key = new UsersExamScoresKey(studentId,examId);
        UsersExamScores usersExamScores = new UsersExamScores();
        usersExamScores.setUsersExamScoresKey(key);
        usersExamScores.setScores(score);

        UsersExamScores usersExamScores1 = restTemplate.postForObject("http://54.169.60.141:8000/student/score/save",usersExamScores,UsersExamScores.class);
        return usersExamScores1;
    }

    public Integer getScoreOfStudentByExam(int studentId,int examId){
        Integer score = restTemplate.getForObject("http://54.169.60.141:8000/student/score/get/"+studentId+"/"+examId,Integer.class);
        return score;
    }
}
