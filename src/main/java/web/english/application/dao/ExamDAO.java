package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.Post;
import web.english.application.entity.exam.Exam;
import web.english.application.entity.exam.Question;

import java.util.List;

@Service
@Slf4j
public class ExamDAO {

    @Autowired
    private RestTemplate restTemplate;

    public Exam save(int teacherId,Exam exam){
        return restTemplate.postForObject("http://54.169.60.141:8000/exam/save/"+teacherId,exam,Exam.class);
    }

    public List<Exam> getAll(){
        ResponseEntity<List<Exam>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/exam",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Exam>>() {
                        });
        List<Exam> exams = responseEntity.getBody();
        return exams;
    }

    public Exam addQuestionToExam(int examId, int questionId){
        Exam exam = restTemplate.getForObject("http://54.169.60.141:8000/exam/"+examId+"/addQuestion/"+questionId,Exam.class);
        return exam;
    }


    public Exam getExamById(int id){
        Exam exam = restTemplate.getForObject("http://54.169.60.141:8000/exam/"+id,Exam.class);
        return exam;
    }

    public List<Question> getListQuestionByExam(int id){
        ResponseEntity<List<Question>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/exam/questions/"+id,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Question>>() {
                        });
        List<Question> questions = responseEntity.getBody();
        return questions;
    }

}
