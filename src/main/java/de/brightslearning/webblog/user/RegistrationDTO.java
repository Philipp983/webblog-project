package de.brightslearning.webblog.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegistrationDTO {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Only letters, digits and underscores allowed")
    private final String username;

    @Size(min = 5, message = "your password must have at least 5 characters")
    private final String password1;
    private final String password2;

    public RegistrationDTO(String username, String password1, String password2) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }
}
