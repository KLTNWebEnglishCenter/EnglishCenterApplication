package web.english.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import web.english.application.entity.user.Teacher;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    private int id;

    private String name;

    private String description;

    private String link;

    private Teacher teacher;

    public Document(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public Document(String name, String description, String link, Teacher teacher) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.teacher = teacher;
    }
}
