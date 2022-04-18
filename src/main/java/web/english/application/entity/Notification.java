package web.english.application.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import web.english.application.entity.user.Teacher;

import java.time.LocalDate;
import java.util.List;

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
