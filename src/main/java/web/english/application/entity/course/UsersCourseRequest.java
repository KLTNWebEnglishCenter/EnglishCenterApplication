package web.english.application.entity.course;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.english.application.entity.user.Student;
import web.english.application.utils.UserRequestStatus;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UsersCourseRequest {

    private UsersCourseRequestKey userRequestCourseKey;
    private Student student;
    private Course course;
    private String status;

    public UsersCourseRequest(UsersCourseRequestKey userRequestCourseKey, Student student, Course course) {
        this.userRequestCourseKey = userRequestCourseKey;
        this.student = student;
        this.course = course;
        this.status= UserRequestStatus.REQUESTING;
    }

    @Override
    public String toString() {
        return "UsersCourseRequest{" +
                "userRequestCourseKey=" + userRequestCourseKey +
                ", student=" + student +
                ", course=" + course +
                ", status='" + status + '\'' +
                '}';
    }
}
