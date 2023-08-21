package de.brightslearning.webblog;

import de.brightslearning.webblog.comment.CommentRepository;
import de.brightslearning.webblog.user.BlogUser;
import de.brightslearning.webblog.user.BlogUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
    private final CommentRepository commentRepository;

    private final BlogUserRepository blogUserRepository;

    @PostConstruct
    public void generateDummyData() {
        BlogUser admin = new BlogUser(1,"admin", "admin", true);

        blogUserRepository.save(admin);
    }
    @Autowired
    public HomeController(CommentRepository commentRepository, BlogUserRepository blogUserRepository) {
        this.commentRepository = commentRepository;
        this.blogUserRepository = blogUserRepository;
    }




    @GetMapping("/")
    public String home(@ModelAttribute("sessionUser") BlogUser sessionUser, Model model) {
        model.addAttribute("messages", commentRepository.findAllByOrderByDateDesc());
        return "home";
    }

}
