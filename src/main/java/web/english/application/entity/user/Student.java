package web.english.application.entity.user;

import lombok.*;
import web.english.application.entity.Classroom;
import web.english.application.entity.course.UsersCourseRequest;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Student extends Users implements Serializable {

    private List<UsersCourseRequest> userRequestCourses;
    private List<Classroom> classrooms;

    public Student(@NonNull String username, @NonNull String password, @NonNull String fullName, @NonNull String email) {
        super(username, password, fullName, email);
    }
}
