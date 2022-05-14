package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.english.application.dao.ScheduleDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.ScheduleInfoHolder;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class ScheduleController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ScheduleDAO scheduleDAO;


    private JwtHelper jwtHelper=new JwtHelper();

    @GetMapping("/schedule")
    public String getScheduleOfTeacher(HttpServletRequest httpServletRequest, Model model, Authentication authentication){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

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

        List<ScheduleInfoHolder> scheduleInMondays=scheduleDAO.getScheduleOfTeaher(monday,token);
        List<ScheduleInfoHolder> scheduleInTuesdays=scheduleDAO.getScheduleOfTeaher(tuesday,token);
        List<ScheduleInfoHolder> scheduleInWednesdays=scheduleDAO.getScheduleOfTeaher(wednesday,token);
        List<ScheduleInfoHolder> scheduleInThursdays=scheduleDAO.getScheduleOfTeaher(thursday,token);
        List<ScheduleInfoHolder> scheduleInFridays=scheduleDAO.getScheduleOfTeaher(friday,token);
        List<ScheduleInfoHolder> scheduleInSaturdays=scheduleDAO.getScheduleOfTeaher(saturday,token);
        List<ScheduleInfoHolder> scheduleInSundays=scheduleDAO.getScheduleOfTeaher(sunday,token);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

//        log.info("scheduleInMonday"+scheduleInMondays.size());
//        log.info("scheduleInTuesday"+scheduleInTuesdays.size());
//        log.info("scheduleInWednesday"+scheduleInWednesdays.size());
//        log.info("scheduleInThursday"+scheduleInThursdays.size());
//        log.info("scheduleInFriday"+scheduleInFridays.size());
//        log.info("scheduleInSaturday"+scheduleInSaturdays.size());
//        log.info("scheduleInSunday"+scheduleInSundays.size());

        return "admin/index";
    }

    @PostMapping("/schedule/next")
    public String getScheduleOfTeacherInNextWeek(HttpServletRequest httpServletRequest, Model model,@RequestParam @DateTimeFormat(pattern = "M/d/yyyy") LocalDate nextMonday, Authentication authentication){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

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

        List<ScheduleInfoHolder> scheduleInMondays=scheduleDAO.getScheduleOfTeaher(nextMonday,token);
        List<ScheduleInfoHolder> scheduleInTuesdays=scheduleDAO.getScheduleOfTeaher(tuesday,token);
        List<ScheduleInfoHolder> scheduleInWednesdays=scheduleDAO.getScheduleOfTeaher(wednesday,token);
        List<ScheduleInfoHolder> scheduleInThursdays=scheduleDAO.getScheduleOfTeaher(thursday,token);
        List<ScheduleInfoHolder> scheduleInFridays=scheduleDAO.getScheduleOfTeaher(friday,token);
        List<ScheduleInfoHolder> scheduleInSaturdays=scheduleDAO.getScheduleOfTeaher(saturday,token);
        List<ScheduleInfoHolder> scheduleInSundays=scheduleDAO.getScheduleOfTeaher(sunday,token);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        return "admin/index";
    }

    @PostMapping("/schedule/previous")
    public String getScheduleOfTeacherInPreviousWeek(HttpServletRequest httpServletRequest, Model model,@RequestParam @DateTimeFormat(pattern = "M/d/yyyy") LocalDate lastMonday, Authentication authentication){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

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

        List<ScheduleInfoHolder> scheduleInMondays=scheduleDAO.getScheduleOfTeaher(lastMonday,token);
        List<ScheduleInfoHolder> scheduleInTuesdays=scheduleDAO.getScheduleOfTeaher(tuesday,token);
        List<ScheduleInfoHolder> scheduleInWednesdays=scheduleDAO.getScheduleOfTeaher(wednesday,token);
        List<ScheduleInfoHolder> scheduleInThursdays=scheduleDAO.getScheduleOfTeaher(thursday,token);
        List<ScheduleInfoHolder> scheduleInFridays=scheduleDAO.getScheduleOfTeaher(friday,token);
        List<ScheduleInfoHolder> scheduleInSaturdays=scheduleDAO.getScheduleOfTeaher(saturday,token);
        List<ScheduleInfoHolder> scheduleInSundays=scheduleDAO.getScheduleOfTeaher(sunday,token);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        return "admin/index";
    }
}

