package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.UsersCourseRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CourseDAO {

    @Autowired
    private RestTemplate restTemplate;

    public List<Course> findAllCourse() {
        ResponseEntity<List<Course>> responseEntity =
                restTemplate.exchange("http://localhost:8000/course/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Course>>() {
                        });
        List<Course> courses = responseEntity.getBody();

        return courses;
    }

    public Course getCourse(int id) {
        Course course = restTemplate.getForObject(("http://localhost:8000/course/" + id), Course.class);
        return course;
    }

    public Course saveCourse(Course course, int levelId,int categoryId ){
        Course course1 = restTemplate.postForObject("http://localhost:8000/course",course,Course.class);
        Course course2 = restTemplate.getForObject("http://localhost:8000/course/addLevelAndCategoryToCourse/"+course1.getId()+"/"+levelId+"/"+categoryId,Course.class);
        return course2;
    }

    public Course deleteCourse(int id){
        Course course = restTemplate.getForObject("http://localhost:8000/course/delete/"+id,Course.class);
        return course;
    }

    public UsersCourseRequest signupCourse(UsersCourseRequest usersCourseRequest){
        UsersCourseRequest request = restTemplate.postForObject("http://localhost:8000/user/signup/course",usersCourseRequest,UsersCourseRequest.class);
        return request;
    }

    public List<UsersCourseRequest> getAllSignupCourse(){
        ResponseEntity<List<UsersCourseRequest>> responseEntity =
                restTemplate.exchange("http://localhost:8000/user/signup/course/list",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UsersCourseRequest>>() {
                        });
        List<UsersCourseRequest> courseRequests = responseEntity.getBody();

        return courseRequests;
    }

    public List<Course> getListCourseFromUserCourseRequest(int studentId,List<UsersCourseRequest> usersCourseRequests){
        List<Course> courses = new ArrayList<>();
        usersCourseRequests.forEach(usersCourseRequest -> {
            int stuId = usersCourseRequest.getUserRequestCourseKey().getStudentId();
            int couId = usersCourseRequest.getUserRequestCourseKey().getCourseId();
            if (stuId == studentId){
                Course course = getCourse(couId);
                courses.add(course);
            }
        });
        return courses;
    }
}
