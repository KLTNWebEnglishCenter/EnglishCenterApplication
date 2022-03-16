package web.english.application.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.Feedback;
import web.english.application.entity.Users;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserDAO {

    @Autowired
    private RestTemplate restTemplate;

    public UserDAO(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Users save(Users users){
        Users users1 = restTemplate.postForObject("localhost:8000/register",users,Users.class);
        return users1;
    }

    public String login(String username,String password){
        Feedback feedback = new Feedback(username,password);
        String access_token = restTemplate.postForObject("localhost:8000/api/login",feedback,String.class);
        return access_token;
    }

}
