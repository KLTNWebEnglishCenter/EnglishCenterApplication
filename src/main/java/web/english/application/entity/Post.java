package web.english.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import web.english.application.entity.user.Users;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {

    private int id;
    private String title;
    private String content;
    private Users users;

    public Post(@NonNull String title, @NonNull String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user=" + users +
                '}';
    }
}
