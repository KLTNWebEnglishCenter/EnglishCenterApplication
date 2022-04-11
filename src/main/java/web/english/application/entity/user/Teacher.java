package web.english.application.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Teacher extends Users implements Serializable {

    private String certificate;

    /**
     *
     * @param username
     * @param password
     * @param fullName
     * @param email
     * @param certificate
     */
    public Teacher( String username,  String password,  String fullName, String email, String certificate) {
        super(username, password, fullName, email);
        this.certificate = certificate;
    }

    /**
     *
     * @param username
     * @param password
     * @param fullName
     * @param email
     */
    public Teacher( String username, String password,  String fullName,  String email) {
        super(username, password, fullName, email);
    }

    /**
     *
     * @param certificate
     */
    public Teacher(String certificate) {
        this.certificate = certificate;
    }
}
