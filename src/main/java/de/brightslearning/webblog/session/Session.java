package de.brightslearning.webblog.session;

import de.brightslearning.webblog.user.BlogUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

@Entity
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private BlogUser blogUser;

    private Instant expiresAt;

    public Session() {
    }

    public Session(BlogUser blogUser, Instant expiresAt) {
        this.blogUser = blogUser;
        this.expiresAt = expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
