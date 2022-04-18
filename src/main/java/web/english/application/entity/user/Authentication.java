package web.english.application.entity.user;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Authentication {

    private int id;
    private boolean enable;
    private String role;

    /**
     *
     * @param role
     */
    public Authentication(String role) {
        this.role = role;
    }


}
