package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
}
