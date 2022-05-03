package web.english.application.entity.schedule;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomSchedule {

    private ClassroomScheduleKey classroomScheduleKey;
    private Classroom classroom;
    private Schedule schedule;
    private String type;
    private String location;
    private String meetingInfo;

    @Override
    public String toString() {
        return "ClassroomSchedule{" +
                "classroomScheduleKey=" + classroomScheduleKey +
                ", classroom=" + classroom +
                ", schedule=" + schedule +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", meetingInfo='" + meetingInfo + '\'' +
                '}';
    }
}
