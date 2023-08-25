package de.brightslearning.webblog.blogentry;

import de.brightslearning.webblog.user.BlogUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String addEntry(@ModelAttribute("BlogEntry")BlogEntry entry, Model model) {

        BlogUser currentUser = (BlogUser) model.getAttribute("sessionUser");
        if(currentUser == null) {
            // The user is not logged in or the session expired. Handle this case appropriately.
            // Maybe redirect to the login page or show an error message.
            return "redirect:/login";
        }

        entry.setBlogUser(currentUser);
        entry.setDate(LocalDateTime.now());
        entry.setContent(entry.getContent().replace("\n", "<br>"));
        blogEntryRepository.save(entry);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showModifyEntryPage(@PathVariable Integer id, Model model) {
        BlogEntry entry = blogEntryRepository.findById(id).orElse(null);
        if (entry == null) {
            // Handle the case when the entry is not found
            return "redirect:/";
        }
        entry.setContent(entry.getContent().replace("<br>", ""));
        model.addAttribute("blogEntry", entry); // Add the existing blog entry to the model
        return "message"; // Name of your HTML file for modifying entries (e.g., modify.html)
    }

    @PostMapping("/edit/{id}")
    public String modifyEntry(@PathVariable Integer id, @ModelAttribute("blogEntry") BlogEntry editedEntry) {
        BlogEntry existingEntry = blogEntryRepository.findById(id).orElse(null);
        if (existingEntry == null) {
            // Handle the case when the entry is not found
            return "redirect:/";
        }
        existingEntry.setLastEdited(LocalDateTime.now());
        // Update the fields you want to change
        editedEntry.setId(existingEntry.getId());
        existingEntry.setTitle(editedEntry.getTitle());
        existingEntry.setContent(editedEntry.getContent());
        existingEntry.setContent(existingEntry.getContent().replace("<br>", ""));

        existingEntry.setContent(existingEntry.getContent().replace("\n", "<br>"));

        blogEntryRepository.save(existingEntry); // This will update the existing entry with the new values
        return "redirect:/"; // Redirect to the homepage or wherever you list the blog entries
    }

    @PostMapping("/delete/{id}")
    public String deleteEntry(@PathVariable Integer id) {
        // Optional validation to ensure only admin can delete
        BlogEntry existingEntry = blogEntryRepository.findById(id).orElse(null);
        if (existingEntry == null) {
            // Handle the case when the entry is not found
            return "redirect:/";
        }
        blogEntryRepository.deleteById(id);
        return "redirect:/"; // Redirect to the homepage or wherever you list the blog entries
    }

}
