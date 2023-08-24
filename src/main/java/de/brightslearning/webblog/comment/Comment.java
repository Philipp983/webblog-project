package de.brightslearning.webblog.comment;

import de.brightslearning.webblog.blogentry.BlogEntry;
import de.brightslearning.webblog.user.BlogUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "blog_User_id")
    private BlogUser blogUser;

    @Column(name = "last_edited")
    private LocalDateTime lastEdited;

    @Column(name ="isDeleted")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "blogEntry_id")
    private BlogEntry blogEntry;

    public Comment() {
        this.deleted = false;
        this.date = LocalDateTime.now();
    }

    public Comment(Integer id, String content, LocalDateTime date) {
        this.id = id;
        this.content = content;
        this.date = LocalDateTime.now();
        this.deleted = false;
    }

    public Comment(String content, LocalDateTime date) {
        this.content = content;
        this.date = LocalDateTime.now();
        this.deleted = false;
    }

    public void setDeleted(Boolean b) {
        this.deleted = b;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Boolean isDeleted() {
        return this.deleted;
    }

}
