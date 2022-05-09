package web.english.application.entity;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduleInfoHolder {

    int scheduleId;
    String classname;
    String dayOfWeek;
    String lesson;
    String teacherName;
    String type;
    String location;
    String meetingInfo;

    public ScheduleInfoHolder(String classname, String dayOfWeek, String lesson, String teacherName, String type, String location, String meetingInfo) {
        this.classname = classname;
        this.dayOfWeek = dayOfWeek;
        this.lesson = lesson;
        this.teacherName = teacherName;
        this.type = type;
        this.location = location;
        this.meetingInfo = meetingInfo;
    }
}

