package web.english.application.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import web.english.application.entity.course.Course;
import web.english.application.entity.user.Teacher;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Classroom implements Serializable {

    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String status;
    private String classname;
    private int maxMember;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate modifiedDate;

    private Teacher teacher;
    private Course course;

    public Classroom(LocalDate startDate, LocalDate endDate, String status, String classname, int maxMember, LocalDate createDate, LocalDate modifiedDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.classname = classname;
        this.maxMember = maxMember;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }

    public Classroom(LocalDate startDate, LocalDate endDate, String status, String classname, int maxMember, LocalDate createDate, LocalDate modifiedDate, Teacher teacher, Course course) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.classname = classname;
        this.maxMember = maxMember;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.teacher = teacher;
        this.course = course;
    }

    public Classroom(int id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", classname='" + classname + '\'' +
                ", maxMember=" + maxMember +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                ", teacher=" + teacher +
                ", course=" + course +
                '}';
    }
}
