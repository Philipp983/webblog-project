package de.brightslearning.webblog.blogentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class BlogEntryController {

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    public BlogEntryController(BlogEntryRepository blogEntryRepository) {
        this.blogEntryRepository = blogEntryRepository;
    }

    @GetMapping(value = "/message")
    public String showEntries(Model model) {
        model.addAttribute("blogEntry", new BlogEntry()); // Add an empty blog entry to the model
        return "message";
    }

    @PostMapping(value ="/message")
    public String addEntry(@ModelAttribute("BlogEntry")BlogEntry entry) {
        entry.setDate(LocalDateTime.now());
        blogEntryRepository.save(entry);
        return "redirect:/";
    }
}
