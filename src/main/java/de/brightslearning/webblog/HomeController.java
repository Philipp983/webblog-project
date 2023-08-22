package de.brightslearning.webblog;

import de.brightslearning.webblog.blogentry.BlogEntry;
import de.brightslearning.webblog.blogentry.BlogEntryRepository;
import de.brightslearning.webblog.comment.CommentRepository;
import de.brightslearning.webblog.user.BlogUser;
import de.brightslearning.webblog.user.BlogUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    private final CommentRepository commentRepository;

    private final BlogUserRepository blogUserRepository;

    private final BlogEntryRepository blogEntryRepository;

    @PostConstruct
    public void generateDummyData() {
        BlogUser admin = new BlogUser(1,"admin", "admin", true);
        BlogUser admin2 = new BlogUser(2,"admin2", "admin", true);

        blogUserRepository.save(admin);
        blogUserRepository.save(admin2);
    }

    @Autowired
    public HomeController(CommentRepository commentRepository, BlogUserRepository blogUserRepository, BlogEntryRepository blogEntryRepository) {
        this.commentRepository = commentRepository;
        this.blogUserRepository = blogUserRepository;
        this.blogEntryRepository = blogEntryRepository;
    }

    @GetMapping("/")
    public String home(@ModelAttribute("sessionUser") BlogUser sessionUser, Model model) {
//        model.addAttribute("messages", commentRepository.findAllByOrderByDateDesc());
        List<BlogEntry> blogEntries = blogEntryRepository.findAll();
        Collections.reverse(blogEntries);
        model.addAttribute("blogEntries", blogEntries);
        return "home";
    }

}
