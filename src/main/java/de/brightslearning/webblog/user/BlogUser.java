package de.brightslearning.webblog.user;

import de.brightslearning.webblog.blogentry.BlogEntry;
import de.brightslearning.webblog.comment.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "blogUser")
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

    @OneToMany(mappedBy = "blogUser")
    private List<BlogEntry> blogEntries;

    @OneToMany(mappedBy = "blogUser")
    private List<Comment> comments;

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

    public BlogUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
}
