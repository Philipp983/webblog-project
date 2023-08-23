package de.brightslearning.webblog.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    @NotEmpty
    private String username;
    private String currentPassword;

    @Size(min = 5, message = "your password must have at least 5 characters")
    private String newPassword1;
    private String newPassword2;

    public ChangePasswordDTO(String username, String currentPassword) {
        this.username = username;
        this.currentPassword = currentPassword;
    }
}
