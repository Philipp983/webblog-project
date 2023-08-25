package de.brightslearning.webblog.blogentry;

import de.brightslearning.webblog.comment.Comment;
import de.brightslearning.webblog.user.BlogUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "blogEntry")
@Getter
@Setter
public class BlogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Size(max = 5000)
    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "last_edited")
    private LocalDateTime lastEdited;

    @ManyToOne
    @JoinColumn(name = "blogUser_id")
    private BlogUser blogUser;

    @OneToMany(mappedBy = "blogEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public BlogEntry() {
        this.date = LocalDateTime.now();
    }

    public BlogEntry(Integer id, String title, String content, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public BlogEntry(String title, String content, LocalDateTime date) {
        this.title = title;
        this.content = content;
        this.date = LocalDateTime.now();
    }
}
