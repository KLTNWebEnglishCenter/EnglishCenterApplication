package web.english.application.entity;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.user.Teacher;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private int id;

    private String title;

    private String content;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate createDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate modifiedDate;

    private Teacher teacher;

    private Classroom classroom;

}
