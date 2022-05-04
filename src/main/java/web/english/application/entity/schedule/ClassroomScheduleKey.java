package web.english.application.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomScheduleKey implements Serializable {


    private int classroomId;
    private int scheduleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassroomScheduleKey that = (ClassroomScheduleKey) o;
        return classroomId == that.classroomId && scheduleId == that.scheduleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(classroomId, scheduleId);
    }
}
