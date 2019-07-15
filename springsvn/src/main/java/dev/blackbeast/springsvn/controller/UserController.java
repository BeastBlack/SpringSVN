package dev.blackbeast.springsvn.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import dev.blackbeast.springsvn.domain.User;
import dev.blackbeast.springsvn.service.ConfigService;
import dev.blackbeast.springsvn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        Boolean adminEnabled;

        if(configService.isAppAnonAccess())
            adminEnabled = Boolean.TRUE;
        else{
            User loggedUser = userService.getLoggedUser();

            if(loggedUser == null)
                adminEnabled = Boolean.FALSE;
            else
                adminEnabled = loggedUser.getIsAdmin();
        }

        model.addAttribute("user", new User());
        model.addAttribute("adminEnabled", adminEnabled);

        return "user";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveUser(Model model, @Valid User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "user";
        }else{
            if(!userService.exists(user.getUsername())) {
                userService.save(user);
                model.addAttribute("showMessage", "REGISTER_OK");
                model.addAttribute("user", new User());
                model.addAttribute("adminEnabled", configService.isAppAnonAccess());
            } else {
                model.addAttribute("showMessage", "REGISTER_EXISTS");
                model.addAttribute("user", new User());
                model.addAttribute("adminEnabled", configService.isAppAnonAccess());
            }

            return "user";
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String showUsers(Model model) {
        List<User> users = userService.getAll();

        model.addAttribute("users", users);
        return "users";
    }
}
