package web.english.application.entity.exam;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {

    private int id;
    private String content;
    private String correctAnswer;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;

    public Question(@NonNull String content, String correctAnswer, String answerA, String answerB) {
        this.content = content;
        this.correctAnswer = correctAnswer;
        this.answerA = answerA;
        this.answerB = answerB;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", answerA='" + answerA + '\'' +
                ", answerB='" + answerB + '\'' +
                ", answerC='" + answerC + '\'' +
                ", answerD='" + answerD + '\'' +
                '}';
    }
}
