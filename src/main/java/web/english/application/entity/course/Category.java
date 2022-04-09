package web.english.application.entity.course;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private int id;
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
