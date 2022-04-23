package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import web.english.application.entity.Classroom;
import web.english.application.entity.Post;
import web.english.application.entity.user.Student;

import java.util.List;

@Service
@Slf4j
public class PostDAO {

    @Autowired
    private RestTemplate restTemplate;

    public List<Post> getAllPost(){
        ResponseEntity<List<Post>> responseEntity =
                restTemplate.exchange("http://localhost:8000/posts",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
                        });
        List<Post> posts = responseEntity.getBody();
        return posts;
    }

    public Post getPostById(int id){
        Post post = restTemplate.getForObject("http://localhost:8000/post/"+id,Post.class);
        return post;
    }

    public Post savePost(Post post){
        Post post1 = restTemplate.postForObject("http://localhost:8000/post/save",post,Post.class);
        return post1;
    }

    public Post deletePost(int id){
        Post post = restTemplate.getForObject("http://localhost:8000/post/delete/"+id,Post.class);
        return post;
    }

    public List<Post> getAllPostWithStatusNoAccept(){
        ResponseEntity<List<Post>> responseEntity =
                restTemplate.exchange("http://localhost:8000/post/status/no/accept",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
                        });
        List<Post> posts = responseEntity.getBody();
        return posts;
    }

    public List<Post> getMyPost(int myId){
        ResponseEntity<List<Post>> responseEntity =
                restTemplate.exchange("http://localhost:8000/post/my/"+myId,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
                        });
        List<Post> posts = responseEntity.getBody();
        return posts;
    }


    public List<Post> getPostByIdOrName(String idOrName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("idOrTitle", idOrName);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<Post>> responseEntity =
                restTemplate.exchange("http://localhost:8000/post/searchByIdOrTitle",
                        HttpMethod.POST, request, new ParameterizedTypeReference<List<Post>>() {
                        });
        List<Post> postList = responseEntity.getBody();
        return postList;
    }
}
