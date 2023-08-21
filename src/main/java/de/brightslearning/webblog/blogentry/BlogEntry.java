package de.brightslearning.webblog.blogentry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blogentry")
@Getter
@Setter
public class BlogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private LocalDateTime date;

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
