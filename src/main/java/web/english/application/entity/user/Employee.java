package web.english.application.entity.user;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Employee extends Users implements Serializable {


    /**
     *
     * @param username
     * @param password
     * @param fullName
     * @param email
     */
    public Employee( String username,  String password,String fullName, String email) {
        super(username, password, fullName, email);
    }


}
