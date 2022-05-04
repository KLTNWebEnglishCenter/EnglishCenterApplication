package web.english.application.entity.schedule;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;


import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    private int id;
    private String dayOfWeek;
    private String lesson;

    private List<ClassroomSchedule> classroomSchedules;

    public Schedule( String dayOfWeek, String lesson) {
        this.dayOfWeek = dayOfWeek;
        this.lesson = lesson;
    }
}
