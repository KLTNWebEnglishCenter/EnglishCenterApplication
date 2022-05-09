package web.english.application.entity.user;

import lombok.*;
import web.english.application.entity.schedule.Classroom;
import web.english.application.entity.course.UsersCourseRequest;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student extends Users implements Serializable {

    private List<UsersCourseRequest> userRequestCourses;
    private List<Classroom> classrooms;

    /**
     *
     * @param username
     * @param password
     * @param fullName
     * @param email
     */
    public Student(String username,  String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    public Student(int id, String username, String password, String fullName, LocalDate dob, String gender, String email, String phoneNumber, boolean enable) {
        super(id, username, password, fullName, dob, gender, email, phoneNumber, enable);
    }
}
