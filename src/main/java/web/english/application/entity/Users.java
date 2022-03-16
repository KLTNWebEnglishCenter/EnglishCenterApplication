package web.english.application.entity;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private int id;
    private String username;
    private String password;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String email;
    private String phoneNumber;
    private boolean enable;

    public Users(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
