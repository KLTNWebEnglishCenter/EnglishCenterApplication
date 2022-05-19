package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.course.Category;
import web.english.application.entity.course.Level;

import java.util.List;

@Service
@Slf4j
public class CategoryDAO {

    @Autowired
    private RestTemplate restTemplate;

    public List<Category> findAllCategory(){
        ResponseEntity<List<Category>> responseEntity =
                restTemplate.exchange("http://54.169.60.141:8000/category/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Category>>() {
                        });
        List<Category> categories = responseEntity.getBody();

        return categories;
    }

//    public Level getCourse(int id){
//        Level level = restTemplate.getForObject(("http://54.169.60.141:8000/course/"+id),Course.class);
//        return level;
//    }

    public Category saveCategory(Category category){
        Category category1 = restTemplate.postForObject("http://54.169.60.141:8000/level",category,Category.class);
        return category;
    }
}
