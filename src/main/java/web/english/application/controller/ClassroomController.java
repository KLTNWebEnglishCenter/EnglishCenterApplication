package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.english.application.dao.ClassroomDAO;
import web.english.application.dao.CourseDAO;
import web.english.application.dao.TeacherDAO;
import web.english.application.entity.Classroom;
import web.english.application.entity.course.Course;
import web.english.application.entity.user.Teacher;

import java.time.LocalDate;
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

    @GetMapping("/classrooms")
    public String getClassrooms(Model model){
        List<Classroom> classrooms = classroomDAO.findAll();
        model.addAttribute("classrooms",classrooms);
        return "admin/lophoc";
    }

    @GetMapping("/addClassroom")
    public String getAddClassroomPage(Model model){
        Classroom classroom = new Classroom();
        List<Course> courses = courseDAO.findAllCourse();
        List<Teacher> teachers = teacherDAO.findAllTeacher();
        model.addAttribute("classroom",classroom);
        model.addAttribute("courses", courses);
        model.addAttribute("teachers",teachers);
        return "admin/addlophoc";
    }

    @GetMapping("/updateClassroom/{id}")
    public String getUpdateClassroomPage(@PathVariable("id") int id,Model model){
        Classroom classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/editlophoc";
    }

    @GetMapping("/classroom/info/{id}")
    public String getInfomationPage(@PathVariable("id") int id,Model model){
        Classroom classroom = classroomDAO.getClassroom(id);
        model.addAttribute("classroom",classroom);
        return "admin/xemthongtinlophoc";
    }

    @PostMapping("/classroom/add")
    public String addClassroom(@ModelAttribute("classroom") Classroom classroom,@RequestParam("course_id") String course_id,@RequestParam("teacher_id") String teacher_id, Model mode){
        int courseId = Integer.parseInt(course_id);
        int teacherId = Integer.parseInt(teacher_id);

        classroom.setStatus("Openning");
        classroomDAO.saveClassroom(classroom,teacherId,courseId);
        return "redirect:/admin/classrooms";
    }

    @PostMapping("/classroom/update")
    public String updateClassroom(@ModelAttribute("classroom") Classroom classroom){
        Classroom classroom1 = classroomDAO.getClassroom(classroom.getId());
        classroom.setModifiedDate(LocalDate.now());
        classroomDAO.saveClassroom(classroom,classroom1.getTeacher().getId(),classroom1.getCourse().getId());
        return "redirect:/admin/classrooms";
    }

    @GetMapping("/classroom/delete/{id}")
    public String deleteClassroom(@PathVariable("id") int id){
        Classroom classroom = classroomDAO.deleteClassroom(id);
        return "redirect:/admin/classrooms";
    }

}
