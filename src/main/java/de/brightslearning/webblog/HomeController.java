package de.brightslearning.webblog;

import de.brightslearning.webblog.comment.CommentRepository;
import de.brightslearning.webblog.user.BlogUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
    private final CommentRepository commentRepository;

    @Autowired
    public HomeController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping("/")
    public String home(@ModelAttribute("sessionUser") BlogUser sessionUser, Model model) {
        model.addAttribute("messages", commentRepository.findAllByOrderByDateDesc());
        return "home";
    }

}
