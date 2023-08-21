package de.brightslearning.webblog;

import de.brightslearning.webblog.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
    private final CommentRepository messageRepository;

    @Autowired
    public HomeController(CommentRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("messages", messageRepository.findAllByOrderByDateDesc());
        return "home";
    }

}
