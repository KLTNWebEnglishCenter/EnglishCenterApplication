package web.english.application.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.ScheduleDAO;
import web.english.application.dao.StudentDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.Notification;
import web.english.application.entity.ScheduleInfoHolder;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.user.Users;
import web.english.application.utils.JwtHelper;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * For students to manage their classes
 */
@Controller
@RequestMapping("/")
@Slf4j
public class ManageClassController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private ScheduleDAO scheduleDAO;

    private JwtHelper jwtHelper=new JwtHelper();

    @GetMapping("/schedule")
    public String getScheduleOfStudent(HttpServletRequest httpServletRequest, Model model){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        if(jwt == ""){
            return "redirect:/login";
        }
        Users user = userDAO.getUserFromToken(token);
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

        //get current date
        LocalDate today=LocalDate.now();

        //get current week start date => monday
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        //get other date of week sequentially
        LocalDate tuesday=monday.plusDays(1);
        LocalDate wednesday=tuesday.plusDays(1);
        LocalDate thursday=wednesday.plusDays(1);
        LocalDate friday=thursday.plusDays(1);
        LocalDate saturday=friday.plusDays(1);
        LocalDate sunday=saturday.plusDays(1);

        model.addAttribute("monday",monday);
        model.addAttribute("tuesday",tuesday);
        model.addAttribute("wednesday",wednesday);
        model.addAttribute("thursday",thursday);
        model.addAttribute("friday",friday);
        model.addAttribute("saturday",saturday);
        model.addAttribute("sunday",sunday);

        List<ScheduleInfoHolder> scheduleInMondays=scheduleDAO.getScheduleOfStudent(monday,token);
        List<ScheduleInfoHolder> scheduleInTuesdays=scheduleDAO.getScheduleOfStudent(tuesday,token);
        List<ScheduleInfoHolder> scheduleInWednesdays=scheduleDAO.getScheduleOfStudent(wednesday,token);
        List<ScheduleInfoHolder> scheduleInThursdays=scheduleDAO.getScheduleOfStudent(thursday,token);
        List<ScheduleInfoHolder> scheduleInFridays=scheduleDAO.getScheduleOfStudent(friday,token);
        List<ScheduleInfoHolder> scheduleInSaturdays=scheduleDAO.getScheduleOfStudent(saturday,token);
        List<ScheduleInfoHolder> scheduleInSundays=scheduleDAO.getScheduleOfStudent(sunday,token);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        return "/student/studentSchedule";
    }

    @PostMapping("/schedule/next")
    public  String getScheduleOfStudentInNextWeek(HttpServletRequest httpServletRequest, Model model,@RequestParam @DateTimeFormat(pattern = "M/d/yyyy") LocalDate nextMonday){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        if(jwt == ""){
            return "redirect:/login";
        }
        Users user = userDAO.getUserFromToken(token);
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

        //get other date of week sequentially
        LocalDate tuesday=nextMonday.plusDays(1);
        LocalDate wednesday=tuesday.plusDays(1);
        LocalDate thursday=wednesday.plusDays(1);
        LocalDate friday=thursday.plusDays(1);
        LocalDate saturday=friday.plusDays(1);
        LocalDate sunday=saturday.plusDays(1);

        model.addAttribute("monday",nextMonday);
        model.addAttribute("tuesday",tuesday);
        model.addAttribute("wednesday",wednesday);
        model.addAttribute("thursday",thursday);
        model.addAttribute("friday",friday);
        model.addAttribute("saturday",saturday);
        model.addAttribute("sunday",sunday);

        List<ScheduleInfoHolder> scheduleInMondays=scheduleDAO.getScheduleOfStudent(nextMonday,token);
        List<ScheduleInfoHolder> scheduleInTuesdays=scheduleDAO.getScheduleOfStudent(tuesday,token);
        List<ScheduleInfoHolder> scheduleInWednesdays=scheduleDAO.getScheduleOfStudent(wednesday,token);
        List<ScheduleInfoHolder> scheduleInThursdays=scheduleDAO.getScheduleOfStudent(thursday,token);
        List<ScheduleInfoHolder> scheduleInFridays=scheduleDAO.getScheduleOfStudent(friday,token);
        List<ScheduleInfoHolder> scheduleInSaturdays=scheduleDAO.getScheduleOfStudent(saturday,token);
        List<ScheduleInfoHolder> scheduleInSundays=scheduleDAO.getScheduleOfStudent(sunday,token);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        return "/student/studentSchedule";
    }

    @PostMapping("/schedule/previous")
    public String getScheduleOfStudentInPreviousWeek(HttpServletRequest httpServletRequest, Model model,@RequestParam @DateTimeFormat(pattern = "M/d/yyyy") LocalDate lastMonday){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        if(jwt == ""){
            return "redirect:/login";
        }
        Users user = userDAO.getUserFromToken(token);
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

        //get other date of week sequentially
        LocalDate tuesday=lastMonday.plusDays(1);
        LocalDate wednesday=tuesday.plusDays(1);
        LocalDate thursday=wednesday.plusDays(1);
        LocalDate friday=thursday.plusDays(1);
        LocalDate saturday=friday.plusDays(1);
        LocalDate sunday=saturday.plusDays(1);

        model.addAttribute("monday",lastMonday);
        model.addAttribute("tuesday",tuesday);
        model.addAttribute("wednesday",wednesday);
        model.addAttribute("thursday",thursday);
        model.addAttribute("friday",friday);
        model.addAttribute("saturday",saturday);
        model.addAttribute("sunday",sunday);

        List<ScheduleInfoHolder> scheduleInMondays=scheduleDAO.getScheduleOfStudent(lastMonday,token);
        List<ScheduleInfoHolder> scheduleInTuesdays=scheduleDAO.getScheduleOfStudent(tuesday,token);
        List<ScheduleInfoHolder> scheduleInWednesdays=scheduleDAO.getScheduleOfStudent(wednesday,token);
        List<ScheduleInfoHolder> scheduleInThursdays=scheduleDAO.getScheduleOfStudent(thursday,token);
        List<ScheduleInfoHolder> scheduleInFridays=scheduleDAO.getScheduleOfStudent(friday,token);
        List<ScheduleInfoHolder> scheduleInSaturdays=scheduleDAO.getScheduleOfStudent(saturday,token);
        List<ScheduleInfoHolder> scheduleInSundays=scheduleDAO.getScheduleOfStudent(sunday,token);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        return "/student/studentSchedule";
    }

    @GetMapping("/classroom")
    public String getManageClassroomPage(HttpServletRequest httpServletRequest,Model model){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        if(jwt == ""){
            return "redirect:/login";
        }
        Users user = userDAO.getUserFromToken(token);
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

        List<Classroom> classrooms=studentDAO.getAllClassroomOfStudent(token);
        List<Notification> notifications=studentDAO.getNotificationsOfStudent(token);
        Classroom classroom=null;
        if(classrooms.size()>0){
            classroom=studentDAO.getClassroomOfStudent(classrooms.get(0).getId());
        }

        model.addAttribute("classroom",classroom);
        model.addAttribute("classrooms",classrooms);
        model.addAttribute("notifications",notifications);

        return "/student/manageClassroom";
    }

    @GetMapping("/student/classroom/{id}")
    public  String getOneClassInManagePage(@PathVariable int id,HttpServletRequest httpServletRequest,Model model){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        if(jwt == ""){
            return "redirect:/login";
        }
        Users user = userDAO.getUserFromToken(token);
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

        List<Classroom> classrooms=studentDAO.getAllClassroomOfStudent(token);
        List<Notification> notifications=studentDAO.getNotificationsOfStudent(token);
        Classroom classroom=studentDAO.getClassroomOfStudent(id);

        model.addAttribute("classroom",classroom);
        model.addAttribute("classrooms",classrooms);
        model.addAttribute("notifications",notifications);

        return "/student/manageClassroom";
    }

}
