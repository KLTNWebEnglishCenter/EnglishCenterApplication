package web.english.application.entity.exam;

import lombok.*;
import web.english.application.entity.user.Student;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UsersExamScores {

    private UsersExamScoresKey usersExamScoresKey;
    private Student student;
    private Exam exam;

    private int scores;


}
