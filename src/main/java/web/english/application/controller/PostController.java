package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.PostDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.Post;
import web.english.application.entity.user.Users;
import web.english.application.utils.StatusHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class PostController {

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/posts")
    public String getPostPage(Model model, HttpServletRequest httpServletRequest,@ModelAttribute(name = "msg") String msg){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
        String token_valid = "Bearer "+token;
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
        List<Post> posts = postDAO.getMyPost(user.getId());


        model.addAttribute("posts",posts);
        model.addAttribute("msg",msg);
        return "admin/post/baidang";
    }

    @GetMapping("/addPost")
    public String getAddPostPage(Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
//        log.info(token);
        String token_valid = "Bearer "+token;
//        log.info(token_valid);
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        Post post = new Post();
        model.addAttribute("users",user);
        model.addAttribute("post",post);
        return "admin/post/addbaidang";
    }

    @PostMapping("/post/save")
    public String savePost(@ModelAttribute Post post, @RequestParam("username") String username, RedirectAttributes redirectAttributes){
        Users users = new Users(username);
        post.setUsers(users);
        post.setStatus(StatusHelper.STATUS_ACCEPT);
        postDAO.savePost(post);
        redirectAttributes.addFlashAttribute("msg","Thêm/Cập nhật bài đăng thành công");
        return "redirect:/admin/posts";
    }

    @GetMapping("/post/update/{id}")
    public String updatePost(@PathVariable int id,Model model,HttpServletRequest httpServletRequest){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
//        log.info(token);
        String token_valid = "Bearer "+token;
//        log.info(token_valid);
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        Post post = postDAO.getPostById(id);

        model.addAttribute("users",user);
        model.addAttribute("post",post);
        return "admin/post/editbaidang";
    }

    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable int id,RedirectAttributes redirectAttributes){
        Post post = postDAO.deletePost(id);
        redirectAttributes.addFlashAttribute("msg","Xóa thành công");
        return "redirect:/admin/posts";
    }

    @GetMapping("/post/accept")
    public String getAcceptPage(HttpServletRequest httpServletRequest,Model model,@ModelAttribute(name = "msg") String msg){
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
//        log.info(token);
        String token_valid = "Bearer "+token;
//        log.info(token_valid);
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }

        List<Post> posts = postDAO.getAllPostWithStatusNoAccept();

        model.addAttribute("users",user);
        model.addAttribute("posts",posts);
        model.addAttribute("msg",msg);
        return "admin/post/duyetbaidang";
    }

    @GetMapping("/post/accept/yes/{id}")
    public String acceptPost(@PathVariable int id,RedirectAttributes redirectAttributes){
        Post post = postDAO.getPostById(id);
        post.setStatus(StatusHelper.STATUS_ACCEPT);
        postDAO.savePost(post);
        redirectAttributes.addFlashAttribute("msg","Duyệt thành công");
        return "redirect:/admin/post/accept";
    }

    @GetMapping("/post/accept/never/{id}")
    public String neverPost(@PathVariable int id,RedirectAttributes redirectAttributes){
        Post post = postDAO.getPostById(id);
        post.setStatus(StatusHelper.STATUS_DENI);
        postDAO.savePost(post);
        redirectAttributes.addFlashAttribute("msg","Duyệt thành công");
        return "redirect:/admin/post/accept";
    }

    @PostMapping("/post/search")
    public String searchPost(@RequestParam String idOrName,HttpServletRequest httpServletRequest,Model model){
        List<Post> posts = postDAO.getPostByIdOrName(idOrName);
        String token = "";
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
            }
        }
//        log.info(token);
        String token_valid = "Bearer "+token;
//        log.info(token_valid);
        if(token != ""){
            user = userDAO.getUserFromToken(token_valid);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);
        model.addAttribute("posts",posts);
        return "admin/post/baidang";
    }
}
