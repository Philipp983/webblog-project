package de.brightslearning.webblog.session;

import lombok.Getter;

@Getter
public class LoginDTO {

    private final String username;
    private final String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
