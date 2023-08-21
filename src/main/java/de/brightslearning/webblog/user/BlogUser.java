package de.brightslearning.webblog.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bloguser")
@Getter
@Setter
public class BlogUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "admin")
    private boolean admin;

    public BlogUser() {
    }

    public BlogUser(Integer id, String username, String password, boolean admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public BlogUser(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }
}
