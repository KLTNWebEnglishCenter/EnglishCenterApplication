package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.Notification;
import web.english.application.entity.course.UsersCourseRequest;

import java.util.List;

@Service
@Slf4j
public class UserRequestCourseStatusDAO {

    @Autowired
    private RestTemplate restTemplate;

    public List<UsersCourseRequest> findAllUsersCourseRequest(){

        ResponseEntity<List<UsersCourseRequest>> responseEntity =
                restTemplate.exchange("http://localhost:8000/requestcourses/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UsersCourseRequest>>() {
                        });
        List<UsersCourseRequest> usersCourseRequests = responseEntity.getBody();

        return usersCourseRequests;
    }

    public List<UsersCourseRequest> search(String courseIdOrName, String studentIdOrFullName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("courseIdOrName", courseIdOrName);
        map.add("studentIdOrFullName", studentIdOrFullName);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<UsersCourseRequest>> responseEntity =  restTemplate.exchange("http://localhost:8000/requestcourse/search/",HttpMethod.POST, request,new ParameterizedTypeReference<List<UsersCourseRequest>>() {
        });
        List<UsersCourseRequest> usersCourseRequests = responseEntity.getBody();

        return usersCourseRequests;
    }
}
