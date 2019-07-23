package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.domain.Author;
import dev.blackbeast.springsvn.dto.UserDto;
import dev.blackbeast.springsvn.service.AuthorService;
import dev.blackbeast.springsvn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthorController {
    private final static String MESSAGE_USER_EXISTS = "user_exists";
    private final static String MESSAGE_USER_EXISTS_TEXT = "Autor o podanym identyfikatorze już istnieje";

    private final static String MESSAGE_OK = "ok";
    private final static String MESSAGE_OK_TEXT = "Dane autora zostały zapisane";

    @Autowired
    AuthorService authorService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public String showAuthors(Model model,
                              @RequestParam(value = "message", required = false) String message) {
        List<Author> authors = authorService.getAuthors();

        model.addAttribute("authors", authors);

        if(message != null)
            model.addAttribute("message", message);

        model.addAttribute("loggedUser", new UserDto(userService.getLoggedUser()));

        return "authors";
    }

    @RequestMapping(value = "/authors/delete/{id}", method = RequestMethod.GET)
    public String showAuthors(@PathVariable("id") Long id) {
        authorService.delete(id);
        authorService.refresh();

        return "redirect:/authors";
    }

    @RequestMapping(value = "/authors/edit/{id}", method = RequestMethod.GET)
    public String editAuthor(Model model, @PathVariable("id") Long id) {
        Author author = authorService.get(id);

        model.addAttribute("author", author);
        model.addAttribute("loggedUser", new UserDto(userService.getLoggedUser()));
        return "author";
    }

    @RequestMapping(value = "/authors/add", method = RequestMethod.GET)
    public String addAuthor(Model model) {
        Author author = new Author();

        model.addAttribute("author", author);
        model.addAttribute("loggedUser", new UserDto(userService.getLoggedUser()));
        return "author";
    }

    @RequestMapping(value = "/authors", method = RequestMethod.POST)
    public String saveAuthor(Model model, @Valid Author author, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "author";
        }else
             if(author.getId() == null) { //nowy użytkownik
                 if (authorService.exists(author.getAuthorId()))
                     return "redirect:/authors?message=" + MESSAGE_USER_EXISTS;
                 else {
                     authorService.save(author);
                     return "redirect:/authors?message=" + MESSAGE_OK;
                 }
             } else {
                 authorService.save(author);
                 return "redirect:/authors?message=" + MESSAGE_OK;
             }


    }
}
