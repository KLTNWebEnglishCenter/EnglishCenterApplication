package web.english.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import web.english.application.entity.user.Users;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {

    private int id;
    private String title;
    private String content;
    private String status;
    private Users users;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate modifiedDate;

    public Post(@NonNull String title, @NonNull String content) {
        this.title = title;
        this.content = content;
    }

    public Post(String title, String content, String status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", users=" + users +
                '}';
    }
}
