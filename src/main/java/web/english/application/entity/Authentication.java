package web.english.application.entity;

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
