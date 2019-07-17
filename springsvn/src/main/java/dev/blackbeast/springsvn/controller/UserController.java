package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.domain.User;
import dev.blackbeast.springsvn.dto.UserProfileDto;
import dev.blackbeast.springsvn.service.ConfigService;
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
    public String showUsers(Model model,
                            @RequestParam(value = "message", required = false) String message) {
        List<User> users = userService.getAll();

        User loggedUser = userService.getLoggedUser();

        model.addAttribute("users", users);

        if(loggedUser != null)
            model.addAttribute("loggedUsername", loggedUser.getUsername());

        if(message != null)
            model.addAttribute("message", message);

        return "users";
    }

    @RequestMapping(value = "/users/activate/{id}", method = RequestMethod.GET)
    public String activateUser(@PathVariable("id") Long id) {
        userService.activateUser(id);

        return "redirect:/users?message=activate";
    }

    @RequestMapping(value = "/users/deactivate/{id}", method = RequestMethod.GET)
    public String deactivateUser(@PathVariable("id") Long id) {
        userService.deactivateUser(id);

        return "redirect:/users?message=deactivate";
    }

    @RequestMapping(value = "/users/grant-admin/{id}", method = RequestMethod.GET)
    public String grantAdminPermission(@PathVariable("id") Long id) {
        userService.grantAdminPermission(id);

        return "redirect:/users?message=grant-admin";
    }

    @RequestMapping(value = "/users/revoke-admin/{id}", method = RequestMethod.GET)
    public String revokeAdminPermission(@PathVariable("id") Long id) {
        userService.revokeAdminPermission(id);

        return "redirect:/users?message=revoke-admin";
    }

    @RequestMapping(value = "/users/remove/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return "redirect:/users?message=remove";
    }

    @RequestMapping(value = "/users/edit/{id}", method = RequestMethod.GET)
    public String editUser(Model model, @PathVariable("id") Long id) {
        UserProfileDto profile = new UserProfileDto(userService.get(id));
        model.addAttribute("user", profile);

        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveUser(@Valid UserProfileDto userProfileDto) {
        userService.updateUser(userProfileDto);

        if(userService.get(userProfileDto.getId()).getIsAdmin())
            return "redirect:/users?message=edit";
        else
            return "redirect:/content";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(Model model) {
        User loggedUser = userService.getLoggedUser();

        if(loggedUser == null)
            return "redirect:/content";

        UserProfileDto profile = new UserProfileDto(loggedUser);
        model.addAttribute("user", profile);
        model.addAttribute("loggedUserProfile", Boolean.TRUE);
        return "profile";
    }
}
