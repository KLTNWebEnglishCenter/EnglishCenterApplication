package web.english.application.entity.user;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {

    private int id;
    private String username;
    private String password;
    private String fullName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String gender;
    private String email;
    private String phoneNumber;
    private boolean enable;
    private String role;

    /**
     *
     * @param username
     * @param password
     * @param email
     */
    public Users(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     *
     * @param username
     * @param password
     * @param fullName
     * @param email
     */
    public Users(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    public Users(String username) {
        this.username = username;
    }

    public Users(int id, String username, String password, String fullName, LocalDate dob, String gender, String email, String phoneNumber, boolean enable) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", enable=" + enable +
                '}';
    }
}
