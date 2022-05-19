package web.english.application.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.exam.Exam;
import web.english.application.entity.exam.Question;

import java.util.List;

@Service
@Slf4j
public class QuestionDAO {

    @Autowired
    private RestTemplate restTemplate;


    public Question save(Question question){
        return restTemplate.postForObject("http://54.169.60.141:8000/question",question,Question.class);
    }

    public List<Question> getAll(){
        ResponseEntity<List<Question>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/question",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Question>>() {
                        });
        List<Question> questions = responseEntity.getBody();
        return questions;
    }
}
