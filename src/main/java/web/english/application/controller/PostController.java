package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.PostDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.Post;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.StatusHelper;
import web.english.application.utils.Utils;

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

    private Utils utils = new Utils();

    @GetMapping("/posts")
    public String getPostPage(Model model, HttpServletRequest httpServletRequest, @ModelAttribute(name = "msg") String msg, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Post> posts = postDAO.getMyPost(userDetails.getUsers().getId());

        model.addAttribute("posts",posts);
        model.addAttribute("msg",msg);
        return "admin/post/baidang";
    }

    @GetMapping("/addPost")
    public String getAddPostPage(Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Post post = new Post();
        model.addAttribute("post",post);
        return "admin/post/addbaidang";
    }

    @PostMapping("/post/save")
    public String savePost(@ModelAttribute Post post, @RequestParam("username") String username, RedirectAttributes redirectAttributes,Model model){
        model.addAttribute("post",post);
        if (!utils.checkMaxLength(post.getTitle())){
            model.addAttribute("errorName","Tên bài đăng không quá 255 kí tự");
            return "admin/post/addbaidang";
        }
        if (!utils.checkMaxLength(post.getContent())){
            model.addAttribute("errorContent","Nội dung bài đăng không quá 255 kí tự");
            return "admin/post/addbaidang";
        }

        Users users = new Users(username);
        post.setUsers(users);
        post.setStatus(StatusHelper.STATUS_ACCEPT);
        postDAO.savePost(post);
        redirectAttributes.addFlashAttribute("msg","Thêm/Cập nhật bài đăng thành công");
        return "redirect:/admin/posts";
    }

    @PostMapping("/post/update")
    public String update(Model model,@ModelAttribute Post post, @RequestParam("username") String username, RedirectAttributes redirectAttributes){
        model.addAttribute("post",post);
        if (!utils.checkMaxLength(post.getTitle())){
            model.addAttribute("errorName","Tên bài đăng không quá 255 kí tự");
            return "admin/post/update"+post.getId();
        }
        if (!utils.checkMaxLength(post.getContent())){
            model.addAttribute("errorContent","Nội dung bài đăng không quá 255 kí tự");
            return "admin/post/update"+post.getId();
        }

        Users users = new Users(username);
        post.setUsers(users);
        post.setStatus(StatusHelper.STATUS_ACCEPT);
        postDAO.savePost(post);
        redirectAttributes.addFlashAttribute("msg","Thêm/Cập nhật bài đăng thành công");
        return "redirect:/admin/posts";
    }

    @GetMapping("/post/update/{id}")
    public String updatePost(@PathVariable int id,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Post post = postDAO.getPostById(id);
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
    public String getAcceptPage(HttpServletRequest httpServletRequest,Model model,@ModelAttribute(name = "msg") String msg, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Post> posts = postDAO.getAllPostWithStatusNoAccept();
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
    public String searchPost(@RequestParam String idOrName,HttpServletRequest httpServletRequest,Model model, Authentication authentication){
        List<Post> posts = postDAO.getPostByIdOrName(idOrName);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        model.addAttribute("posts",posts);
        return "admin/post/baidang";
    }
}
