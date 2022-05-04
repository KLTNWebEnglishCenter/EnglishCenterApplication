package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.Notification;

import java.util.List;

@Service
@Slf4j
public class NotificationDAO {

    @Autowired
    private RestTemplate restTemplate;


    /**
     * @author VQKHANH
     * @param token
     * @return
     */
    public List<Notification> findAllNotification(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Notification>> responseEntity =
                restTemplate.exchange("http://localhost:8000/notifications/",
                        HttpMethod.GET, request, new ParameterizedTypeReference<List<Notification>>() {
                        });
        List<Notification> notifications = responseEntity.getBody();

        return notifications;
    }

    /**
     * @author VQKHANH
     * @param id
     * @return "Delete success!" if no error occur
     */
    public String deleteNotificationById(int id){
        String message="";
        try {
            ResponseEntity<String> response  = restTemplate.exchange("http://localhost:8000/notification/"+id, HttpMethod.DELETE, null, String.class);
            message=response.getBody();
        } catch (HttpClientErrorException|HttpServerErrorException ex) {
             message = ex.getResponseBodyAsString();
        }
        return message;
    }

    /**
     * @author VQKHANH
     * @param id
     * @return
     */
    public Notification findNotificationById(int id,String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Notification> responseEntity=restTemplate.exchange("http://localhost:8000/notification/"+id,
                HttpMethod.GET, request, new ParameterizedTypeReference<Notification>() {
                });
        Notification notification=responseEntity.getBody();
        return  notification;
    }

    /**
     * Save notification for each classroomId
     * @author VQKHANH
     * @param notification
     * @param selectedClassroom
     * @param token
     * @return "Save notification success!" if no error occur
     */
    public String saveNotificationWithListClassroom(Notification notification,int[] selectedClassroom,String token){

        for (int i = 0; i < selectedClassroom.length; i++) {
            notification.setClassroom(new Classroom(selectedClassroom[i]));
            if(saveNotification(notification,token)==null)return  "Save notification fail!";;
        }

        return  "Save notification success!";
    }

    /**
     * Save one notification with one classroom data
     * @author VQKHANH
     * @param notification
     * @param token
     * @return
     */
    public Notification saveNotification(Notification notification,String token){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Notification> request = new HttpEntity<>(notification,headers);

        ResponseEntity<Notification> responseEntity =
                restTemplate.exchange("http://localhost:8000/notification/save",
                        HttpMethod.POST, request, new ParameterizedTypeReference<Notification>() {
                        });
        Notification notification1 = responseEntity.getBody();

//        Notification notification1=restTemplate.postForObject("http://localhost:8000/notification/save",notification,Notification.class);
        return  notification1;
    }

    public List<Notification> searchNotification(String id, String classroomIdOrClassname,String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", token);

        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        map.add("id", id);
        map.add("classroomIdOrClassname", classroomIdOrClassname);


        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

        ResponseEntity<List<Notification>> responseEntity =  restTemplate.exchange("http://localhost:8000/notification/search",HttpMethod.POST, request,new ParameterizedTypeReference<List<Notification>>() {
        });
        List<Notification> notifications = responseEntity.getBody();
        return notifications;
    }
}
