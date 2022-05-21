package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.NotificationDAO;
import web.english.application.dao.TeacherDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.Notification;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;
import web.english.application.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class NotificationController {


    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private UserDAO userDAO;

    private Utils utils=new Utils();

    private JwtHelper jwtHelper=new JwtHelper();

    /**
     * Find notification list were created by specify teacher (need access token)
     * @author VQKHANH
     * @param httpServletRequest
     * @param model
     * @return
     */
    @GetMapping("/notification")
    public String getNotification(HttpServletRequest httpServletRequest, Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        List<Notification> notifications=notificationDAO.findAllNotification(token);
        model.addAttribute("notifications",notifications);
        return "admin/notification/notification";
    }

    /**
     * Delete notification by id
     * @author VQKHANH
     * @param id
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/notification/delete/{id}")
    public String deleteById(@PathVariable int id, RedirectAttributes redirectAttributes){
        String message=notificationDAO.deleteNotificationById(id);

        if(message.equalsIgnoreCase("Delete success!"))
            redirectAttributes.addFlashAttribute("msg","Xóa thông báo thành công!");
        else
            redirectAttributes.addFlashAttribute("msg","Xóa thông báo không thành công!");
        return "redirect:/admin/notification";
    }

    /**
     * @author VQKHANH
     * @param httpServletRequest
     * @param model
     * @return
     */
    @GetMapping("/addnotification")
    public String getAddNotificationPage(HttpServletRequest httpServletRequest,Model model, Authentication authentication){
        Notification notification=new Notification();
        model.addAttribute("notification",notification);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        //get classroom list of specify teacher
        List<Classroom> classrooms=teacherDAO.getAllClassroomOfTeacher(userDetails.getUsers().getId());
        model.addAttribute("classrooms",classrooms);

        return "admin/notification/addnotification";
    }

    /**
     * Save Notification with selected classroom list
     * @author VQKHANH
     * @param httpServletRequest
     * @param notification
     * @param selectedClassroom
     * @return
     */
    @PostMapping("/notification/add")
    public String saveNotification(HttpServletRequest httpServletRequest, @ModelAttribute Notification notification, int[] selectedClassroom, Model model,Authentication authentication,RedirectAttributes redirectAttributes){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        if(!utils.checkMaxLength(notification.getTitle())){
            model.addAttribute("errorTitle",Utils.maxLengthRequire);
            return "admin/notification/addnotification";
        }
        if(!utils.checkMaxLength(notification.getContent())){
            model.addAttribute("errorContent",Utils.maxLengthRequire);
            return "admin/notification/addnotification";
        }

        //Cần xử lý thông báo khi người dùng không chọn lớp nào cả
        if(selectedClassroom==null||selectedClassroom.length==0){
            model.addAttribute("msg","Vui lòng chọn lớp học để đăng thông báo!");
            return "admin/notification/addnotification";
        }

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        String msg=notificationDAO.saveNotificationWithListClassroom(notification,selectedClassroom,token);
        if(msg.contains("success"))
            redirectAttributes.addFlashAttribute("msg","Thêm thông báo thành công!");
        else if(msg.contains("fail"))
            redirectAttributes.addFlashAttribute("msg","Thêm thông báo không thành công!");
        return "redirect:/admin/notification";
    }

    /**
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/notificationinfo/{id}")
    public String getNotificationInfoPage(HttpServletRequest httpServletRequest,@PathVariable("id") int id,Model model, Authentication authentication){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Notification notification=notificationDAO.findNotificationById(id,token);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        model.addAttribute("notification",notification);
        return "admin/notification/notificationinfo";
    }

    /**
     * @author VQKHANH
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editnotification/{id}")
    public String getEditNotificationPage(HttpServletRequest httpServletRequest,@PathVariable("id") int id, Model model, Authentication authentication){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Notification notification=notificationDAO.findNotificationById(id,token);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        model.addAttribute("notification",notification);
        return "admin/notification/editnotification";
    }

    @PostMapping("/notification/edit")
    public String editNotification(HttpServletRequest httpServletRequest,@ModelAttribute Notification notification,@RequestParam int classroomId,Model model,Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        if(!utils.checkMaxLength(notification.getTitle())){
            model.addAttribute("errorTitle","Tiêu đề "+Utils.maxLengthRequire);
            return "admin/notification/editnotification";
        }
        if(!utils.checkMaxLength(notification.getContent())){
            model.addAttribute("errorContent","Nội dung "+Utils.maxLengthRequire);
            return "admin/notification/editnotification";
        }

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        notification.setClassroom(new Classroom(classroomId));

        notificationDAO.saveNotification(notification,token);
        return "redirect:/admin/notification";
    }

    @PostMapping("/notification/search")
    public String searchTeacher(HttpServletRequest httpServletRequest,@RequestParam String id, @RequestParam(value = "classroomIdOrClassname") String classroomIdOrClassname,Model model, Authentication authentication){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        List<Notification> notifications=notificationDAO.searchNotification(id,classroomIdOrClassname,token);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        model.addAttribute("notifications",notifications);
        return "admin/notification/notification";
    }
}
