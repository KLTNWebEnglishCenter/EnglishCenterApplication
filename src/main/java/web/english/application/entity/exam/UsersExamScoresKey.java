package web.english.application.entity.exam;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsersExamScoresKey implements Serializable {



    private int studentId;
    private int examId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersExamScoresKey that = (UsersExamScoresKey) o;
        return studentId == that.studentId && examId == that.examId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, examId);
    }
}
