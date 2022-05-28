package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.course.Course;
import web.english.application.entity.course.Level;

import java.util.List;

@Service
@Slf4j
public class LevelDAO {

    @Autowired
    private RestTemplate restTemplate;

    public List<Level> findAllLevel(){
        ResponseEntity<List<Level>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/level/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Level>>() {
                        });
        List<Level> levels = responseEntity.getBody();

        return levels;
    }

//    public Level getCourse(int id){
//        Level level = restTemplate.getForObject(("http://54.169.60.141:8000/course/"+id),Course.class);
//        return level;
//    }

    public Level saveLevel(Level level){
        Level level1 = restTemplate.postForObject("http://54.169.60.141:8000/level",level,Level.class);
        return level1;
    }
//
//    public Course deleteCourse(int id){
//        Course course = restTemplate.getForObject("http://54.169.60.141:8000/course/delete"+id,Course.class);
//        return course;
//    }
}
