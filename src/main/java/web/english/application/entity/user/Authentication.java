package web.english.application.entity.user;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Authentication {

    private int id;
    private String role;
    private boolean enable;
}
