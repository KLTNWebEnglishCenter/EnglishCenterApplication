package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.user.Users;

@Service
@Slf4j
public class UserDAO {

    @Autowired
    private RestTemplate restTemplate;

    public UserDAO(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Users save(Users users){
        users.setFullName(users.getUsername());
//        log.info(users.toString());
        Users users1 = restTemplate.postForObject("http://localhost:8000/register",users,Users.class);
        return users;
    }

    public Users getUser(int id){
        Users users = restTemplate.getForObject("http://localhost:8000/user/"+id,Users.class);
        return users;
    }

    public String login(String username,String password){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( "http://localhost:8000/api/login", request , String.class );
//        log.info(response.getBody());
//        return response.getBody();
        return response.getHeaders().getFirst("access_token");
    }

    public Users getUserFromToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication",token);
        HttpEntity<String> request = new HttpEntity<>("body",headers);
        Users users = restTemplate.postForObject("http://localhost:8000/user/fromToken/",request,Users.class);
        return users;
    }

    public String checkEmail(String email){
        String regex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        if(email.matches(regex)){
            return "";
        }else{
            return "Email chưa chính xác!";
        }
    }

//    public String checkPassword(String password){
//        String psRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
//        if(password.matches(psRegex)){
//            return "";
//        }else{
//            return "Mật khẩu có tối thiểu 8 kí tự, tối đa 20 kí tự";
//        }
//    }

    public String checkPasswordMatch(String password,String passwordMatch){
        if(password.equals(passwordMatch))
            return "";
        else
            return "Mật khẩu nhập lại không đúng";
    }

    public String checkLength(String fieldName,String name, int min, int max){
        if(name.length() < min || name.length() > max){
            return fieldName+" tối thiểu " + min + " và tối đa "+ max + " kí tự";
        }else
            return "";
    }

    public String checkRequired(String fieldName,String name){
        if(name.equals("") || name == null){
            return fieldName + " chưa nhập dữ liệu";
        }else
            return "";
    }
}
