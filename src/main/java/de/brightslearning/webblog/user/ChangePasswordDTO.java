package de.brightslearning.webblog.user;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Confirm password cannot be empty")
    private String newPassword1;

    @Size(min = 5, message = "your password must have at least 5 characters")
    @NotBlank(message = "Confirm password cannot be empty")
    private String newPassword2;

    public ChangePasswordDTO(String username, String currentPassword, String newPassword1, String newPassword2) {
        this.username = username;
        this.currentPassword = currentPassword;
        this.newPassword1 = newPassword1;
        this.newPassword2 = newPassword2;
    }
}
