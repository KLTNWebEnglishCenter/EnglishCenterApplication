package web.english.application.entity.exam;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import web.english.application.entity.user.Teacher;


import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Exam implements Serializable {


    private int id;
    private String name;
    private String description;
    private String status;
    private Teacher teacher;

    private List<Question> questions;
    private List<UsersExamScores> usersExamScores;

    public boolean addQuestion(Question question){
        return questions.add(question);
    }

    public Exam(String name, String description, String status, Teacher teacher) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.teacher = teacher;
    }

    public Exam(String name, String description, String status, Teacher teacher, List<Question> questions) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.teacher = teacher;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", teacher=" + teacher +
                ", questions=" + questions +
                ", usersExamScores=" + usersExamScores +
                '}';
    }
}
