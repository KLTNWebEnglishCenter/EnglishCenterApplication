package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.*;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.ScheduleInfoHolder;
import web.english.application.entity.course.Course;
import web.english.application.entity.schedule.ClassroomSchedule;
import web.english.application.entity.schedule.ClassroomScheduleKey;
import web.english.application.entity.schedule.Schedule;
import web.english.application.entity.user.Student;
import web.english.application.entity.user.Teacher;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class ClassroomController {

    @Autowired
    private ClassroomDAO classroomDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private ScheduleDAO scheduleDAO;



    private JwtHelper jwtHelper=new JwtHelper();

    @GetMapping("/classrooms")
    public String getClassrooms(Model model, HttpServletRequest httpServletRequest, @ModelAttribute(name = "msg") String msg, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Classroom> classrooms = classroomDAO.findAll();
        model.addAttribute("classrooms",classrooms);
        model.addAttribute("msg",msg);
        return "admin/classroom/lophoc";
    }

    @GetMapping("/addClassroom")
    public String getAddClassroomPage(Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Classroom classroom = new Classroom();
        List<Course> courses = courseDAO.findAllCourse();
        List<Teacher> teachers = teacherDAO.findAllTeacher();
        model.addAttribute("classroom",classroom);
        model.addAttribute("courses", courses);
        model.addAttribute("teachers",teachers);
        return "admin/classroom/addlophoc";
    }

    @GetMapping("/updateClassroom/{id}")
    public String getUpdateClassroomPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Classroom classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/classroom/editlophoc";
    }

    @GetMapping("/classroom/info/{id}")
    public String getInformationPage(@PathVariable("id") int id,Model model,HttpServletRequest httpServletRequest, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        Classroom classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/classroom/xemthongtinlophoc";
    }

    @PostMapping("/classroom/add")
    public String addClassroom(RedirectAttributes redirectAttributes,@ModelAttribute("classroom") Classroom classroom,@RequestParam("course_id") String course_id,@RequestParam("teacher_id") String teacher_id, Model mode){
        int courseId = Integer.parseInt(course_id);
        int teacherId = Integer.parseInt(teacher_id);

        classroom.setStatus("Openning");
        classroomDAO.saveClassroom(classroom,teacherId,courseId);
        redirectAttributes.addFlashAttribute("msg","Thêm thành công");
        return "redirect:/admin/classrooms";
    }

    @PostMapping("/classroom/update")
    public String updateClassroom(@ModelAttribute("classroom") Classroom classroom,RedirectAttributes redirectAttributes){
        Classroom classroom1 = classroomDAO.getClassroom(classroom.getId());
        classroom.setModifiedDate(LocalDate.now());
        classroom.setStatus("Openning");
        classroomDAO.saveClassroom(classroom,classroom1.getTeacher().getId(),classroom1.getCourse().getId());
        redirectAttributes.addFlashAttribute("msg","Cập nhật thành công");
        return "redirect:/admin/classrooms";
    }

    @GetMapping("/classroom/delete/{id}")
    public String deleteClassroom(@PathVariable("id") int id, RedirectAttributes redirectAttributes){
        String rs = classroomDAO.deleteClassroom(id);
        if (rs.equals("DeleteAccess")){
            redirectAttributes.addFlashAttribute("msg","Xóa thành công");
        }else {
            redirectAttributes.addFlashAttribute("msg","Xóa thất bại.Lớp học vẫn còn lịch");
        }
        return "redirect:/admin/classrooms";
    }

    @PostMapping("/classroom/search")
    public String searchCourse(@RequestParam("idOrName") String idOrName,HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Classroom> classrooms = classroomDAO.findByIdOrClassName(idOrName);
        model.addAttribute("classrooms",classrooms);
        return "admin/classroom/lophoc";
    }

    @GetMapping("/classroom/students/{id}")
    public String getListStudentPage(HttpServletRequest httpServletRequest, Model model, @PathVariable int id, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Student> students= classroomDAO.getStudentFromClassroom(id);
        model.addAttribute("classroomId",id);
        model.addAttribute("students",students);
        return "admin/classroom/danhsachhocvien";
    }

//    ============================================================================================
    @GetMapping("/classroom/schedule/{id}")
    public String getScheduleOfClassroom(HttpServletRequest httpServletRequest, Model model,@PathVariable int id, Authentication authentication){

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

        List<ScheduleInfoHolder> scheduleInMondays   =classroomDAO.getClassroomSchedule(monday,id);
        List<ScheduleInfoHolder> scheduleInTuesdays  =classroomDAO.getClassroomSchedule(tuesday,id);
        List<ScheduleInfoHolder> scheduleInWednesdays=classroomDAO.getClassroomSchedule(wednesday,id);
        List<ScheduleInfoHolder> scheduleInThursdays =classroomDAO.getClassroomSchedule(thursday,id);
        List<ScheduleInfoHolder> scheduleInFridays   =classroomDAO.getClassroomSchedule(friday,id);
        List<ScheduleInfoHolder> scheduleInSaturdays =classroomDAO.getClassroomSchedule(saturday,id);
        List<ScheduleInfoHolder> scheduleInSundays   =classroomDAO.getClassroomSchedule(sunday,id);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        model.addAttribute("id",id);

        return "admin/classroom/xemlichhoc";
    }

    @PostMapping("/classroom/schedule/next/{id}")
    public String getScheduleOfClassroomInNextWeek(@PathVariable int id,HttpServletRequest httpServletRequest, Model model,@RequestParam @DateTimeFormat(pattern = "M/d/yyyy") LocalDate nextMonday, Authentication authentication){

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

        List<ScheduleInfoHolder> scheduleInMondays   =classroomDAO.getClassroomSchedule(nextMonday,id);
        List<ScheduleInfoHolder> scheduleInTuesdays  =classroomDAO.getClassroomSchedule(tuesday,id);
        List<ScheduleInfoHolder> scheduleInWednesdays=classroomDAO.getClassroomSchedule(wednesday,id);
        List<ScheduleInfoHolder> scheduleInThursdays =classroomDAO.getClassroomSchedule(thursday,id);
        List<ScheduleInfoHolder> scheduleInFridays   =classroomDAO.getClassroomSchedule(friday,id);
        List<ScheduleInfoHolder> scheduleInSaturdays =classroomDAO.getClassroomSchedule(saturday,id);
        List<ScheduleInfoHolder> scheduleInSundays   =classroomDAO.getClassroomSchedule(sunday,id);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        return "admin/classroom/xemlichhoc";
    }

    @PostMapping("/classroom/schedule/previous/{id}")
    public String getScheduleOfClassroomInPreviousWeek(@PathVariable int id,HttpServletRequest httpServletRequest, Model model,@RequestParam @DateTimeFormat(pattern = "M/d/yyyy") LocalDate lastMonday, Authentication authentication){

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

        List<ScheduleInfoHolder> scheduleInMondays   =classroomDAO.getClassroomSchedule(lastMonday,id);
        List<ScheduleInfoHolder> scheduleInTuesdays  =classroomDAO.getClassroomSchedule(tuesday,id);
        List<ScheduleInfoHolder> scheduleInWednesdays=classroomDAO.getClassroomSchedule(wednesday,id);
        List<ScheduleInfoHolder> scheduleInThursdays =classroomDAO.getClassroomSchedule(thursday,id);
        List<ScheduleInfoHolder> scheduleInFridays   =classroomDAO.getClassroomSchedule(friday,id);
        List<ScheduleInfoHolder> scheduleInSaturdays =classroomDAO.getClassroomSchedule(saturday,id);
        List<ScheduleInfoHolder> scheduleInSundays   =classroomDAO.getClassroomSchedule(sunday,id);

        model.addAttribute("scheduleInMondays",scheduleInMondays);
        model.addAttribute("scheduleInTuesdays",scheduleInTuesdays);
        model.addAttribute("scheduleInWednesdays",scheduleInWednesdays);
        model.addAttribute("scheduleInThursdays",scheduleInThursdays);
        model.addAttribute("scheduleInFridays",scheduleInFridays);
        model.addAttribute("scheduleInSaturdays",scheduleInSaturdays);
        model.addAttribute("scheduleInSundays",scheduleInSundays);

        return "admin/classroom/xemlichhoc";
    }

    @GetMapping("/classroom/schedule/update/{id}")
    public String updateSchedule(@PathVariable int id,HttpServletRequest httpServletRequest, Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        List<Schedule> schedules = scheduleDAO.getAll();
        model.addAttribute("schedules",schedules);
        Classroom  classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/classroom/capnhatlichhoc";
    }

    @PostMapping("/classroom/schedule/update")
    public String saveClassroomSchedule(HttpServletRequest httpServletRequest){
        String classroomId = httpServletRequest.getParameter("classroomId");
        String type = httpServletRequest.getParameter("type");
        String meetingInfo = httpServletRequest.getParameter("meetingInfo");
        String location = httpServletRequest.getParameter("location");

        List<Integer> scheduleList = new ArrayList<>();
        String[] paramValues = httpServletRequest.getParameterValues("scheduleList");
        for (int i = 0; i < paramValues.length; i++) {
            String paramValue = paramValues[i];
            scheduleList.add(Integer.parseInt(paramValue));
        }

        int id = Integer.parseInt(classroomId);
        if (type.equals("on")){
            scheduleList.forEach(integer -> {
                ClassroomScheduleKey key = new ClassroomScheduleKey(id,integer);
                ClassroomSchedule classroomSchedule = new ClassroomSchedule();
                classroomSchedule.setClassroomScheduleKey(key);
                classroomSchedule.setClassroom(classroomDAO.getClassroom(id));
                classroomSchedule.setSchedule(scheduleDAO.getSchedule(integer));
                classroomSchedule.setType("Trực tuyến");
                classroomSchedule.setMeetingInfo(meetingInfo);

                scheduleDAO.save(classroomSchedule);
            });
        }else if (type.equals("off")){
            scheduleList.forEach(integer -> {
                ClassroomScheduleKey key = new ClassroomScheduleKey(id,integer);
                ClassroomSchedule classroomSchedule = new ClassroomSchedule();
                classroomSchedule.setClassroomScheduleKey(key);
                classroomSchedule.setClassroom(classroomDAO.getClassroom(id));
                classroomSchedule.setSchedule(scheduleDAO.getSchedule(integer));
                classroomSchedule.setType("Trực tiếp");
                classroomSchedule.setLocation(location);

                scheduleDAO.save(classroomSchedule);
            });
        }
        return "redirect:/admin/classroom/schedule/"+id;
    }

    @PostMapping("/classroom/schedule/delete")
    public String deleteClassroomSchedule(HttpServletRequest httpServletRequest){
        String[] c = httpServletRequest.getParameterValues("classroomId");
        String[] s = httpServletRequest.getParameterValues("scheduleId");
        int id = Integer.parseInt(c[0]);
        int scheduleId = Integer.parseInt(s[0]);
        String delete = classroomDAO.deleteClassroomSchedule(id,scheduleId);
        log.info(delete);
        return "redirect:/admin/classroom/schedule/"+id;
    }

    @PostMapping("/classroom/schedule/updateOne")
    public String updateClassroomSchedule(HttpServletRequest httpServletRequest,Model model, Authentication authentication){

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("users",userDetails.getUsers());

        String[] c = httpServletRequest.getParameterValues("classroomId");
        String[] s = httpServletRequest.getParameterValues("scheduleId");
        int id = Integer.parseInt(c[0]);
        int scheduleId = Integer.parseInt(s[0]);

        model.addAttribute("scheduleId",scheduleId);
        Classroom  classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/classroom/capnhatmotlichhoc";
    }

    @PostMapping("/classroom/schedule/update/one")
    public String updateChangeSchedule(HttpServletRequest httpServletRequest){
        String classroomId = httpServletRequest.getParameter("classroomId");
        String schedule = httpServletRequest.getParameter("scheduleId");
        String type = httpServletRequest.getParameter("type");
        String meetingInfo = httpServletRequest.getParameter("meetingInfo");
        String location = httpServletRequest.getParameter("location");

        int id = Integer.parseInt(classroomId);
        int scheduleId = Integer.parseInt(schedule);
        if (type.equals("on")){
            ClassroomScheduleKey key = new ClassroomScheduleKey(id,scheduleId);
            ClassroomSchedule classroomSchedule = new ClassroomSchedule();
            classroomSchedule.setClassroomScheduleKey(key);
            classroomSchedule.setType("Trực tuyến");
            classroomSchedule.setMeetingInfo(meetingInfo);

            scheduleDAO.save(classroomSchedule);

        }else if (type.equals("off")){
            ClassroomScheduleKey key = new ClassroomScheduleKey(id,scheduleId);
            ClassroomSchedule classroomSchedule = new ClassroomSchedule();
            classroomSchedule.setClassroomScheduleKey(key);
            classroomSchedule.setType("Trực tiếp");
            classroomSchedule.setLocation(location);

            scheduleDAO.save(classroomSchedule);
        }
        return "redirect:/admin/classroom/schedule/"+id;
    }
}
