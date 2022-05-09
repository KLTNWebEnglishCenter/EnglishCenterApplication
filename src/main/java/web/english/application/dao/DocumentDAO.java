package web.english.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import web.english.application.entity.Document;
import web.english.application.entity.Notification;

import java.util.List;

@Service
@Slf4j
public class DocumentDAO {
    @Autowired
    private RestTemplate restTemplate;
    /**
     * @author VQKHANH
     * @param token
     * @return
     */
    public List<Document> findAllDocument(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Document>> responseEntity =
                restTemplate.exchange("http://localhost:8000/documents/",
                        HttpMethod.GET, request, new ParameterizedTypeReference<List<Document>>() {
                        });
        List<Document> documents = responseEntity.getBody();

        return documents;
    }

    public Document uploadFile(String token, MultipartFile file,String name,String description){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", token);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", file.getResource());
        map.add("name", name);
        map.add("description", description);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
                map, headers);

        ResponseEntity<Document> result = restTemplate.exchange(
                "http://localhost:8000/document/uploadFile", HttpMethod.POST, requestEntity,
                Document.class);
        Document document=result.getBody();
        return document;
    }

    public String deleteFile(int documentId){
        try {
            restTemplate.delete("http://localhost:8000/document/deleteFile/"+documentId);
            return "Delete success!";
        }catch (Exception exception){
            exception.printStackTrace();
            return "Delete fail!";
        }
    }

    public List<Document> searchDocument(String token,String id, String name){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("id", id);
        map.add("name", name);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<List<Document>> responseEntity =  restTemplate.exchange("http://localhost:8000/document/search",HttpMethod.POST, request,new ParameterizedTypeReference<List<Document>>() {
        });
        List<Document> documents = responseEntity.getBody();
        return  documents;
    }
}
