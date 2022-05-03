package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.ScheduleInfoHolder;
import web.english.application.entity.schedule.ClassroomSchedule;
import web.english.application.entity.schedule.Schedule;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ScheduleDAO {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @author VQKHANH
     * @param selectedDate
     * @param token
     * @return
     */
    public List<ScheduleInfoHolder> getScheduleOfTeaher(LocalDate selectedDate, String token ){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", token);

//        log.info(selectedDate);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("selectedDate", selectedDate.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<ScheduleInfoHolder>> responseEntity=restTemplate.exchange("http://localhost:8000/schedule/teacher",
                HttpMethod.POST, request, new ParameterizedTypeReference<List<ScheduleInfoHolder>>() {
                });
        List<ScheduleInfoHolder> scheduleInfoHolders=responseEntity.getBody();
        return  scheduleInfoHolders;
    }

    /**
     * @author VQKHANH
     * @param selectedDate
     * @param token
     * @return
     */
    public List<ScheduleInfoHolder> getScheduleOfStudent(LocalDate selectedDate, String token ){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", token);

//        log.info(selectedDate);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("selectedDate", selectedDate.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<ScheduleInfoHolder>> responseEntity=restTemplate.exchange("http://localhost:8000/schedule/student",
                HttpMethod.POST, request, new ParameterizedTypeReference<List<ScheduleInfoHolder>>() {
                });
        List<ScheduleInfoHolder> scheduleInfoHolders=responseEntity.getBody();
        return  scheduleInfoHolders;
    }

    public ClassroomSchedule save(ClassroomSchedule classroomSchedule){
        ClassroomSchedule schedule = restTemplate.postForObject("http://localhost:8000/classroom/schedule/save",classroomSchedule,ClassroomSchedule.class);
        return schedule;
    }

    public List<Schedule> getAll(){
        ResponseEntity<List<Schedule>> responseEntity =
                restTemplate.exchange("http://localhost:8000/schedules", HttpMethod.GET, null, new ParameterizedTypeReference<List<Schedule>>() {
                });
        List<Schedule> schedules = responseEntity.getBody();
        return schedules;
    }

    public Schedule getSchedule(int id){
        Schedule schedule = restTemplate.getForObject("http://localhost:8000/schedule/"+id,Schedule.class);
        return schedule;
    }
}
