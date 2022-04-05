package web.english.application.entity.course;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Level implements Serializable {

    private int id;
    private String name;

    private List<Course> courses;

    public Level( String name) {
        this.name = name;
    }


}
