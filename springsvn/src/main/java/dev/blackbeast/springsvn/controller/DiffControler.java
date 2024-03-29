package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.dto.UserDto;
import dev.blackbeast.springsvn.service.DiffService;
import dev.blackbeast.springsvn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DiffControler {

    @Autowired
    DiffService diffService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/diff", method = RequestMethod.GET)
    public String showHistory(@RequestParam(value = "path", required = false) String path,
                              @RequestParam(value = "revision", required = false) Long revision,
                              @RequestParam(value = "revisionTo", required = false) Long revisionTo,
                              @RequestParam(value = "details", required = false) Boolean details,
                              @RequestParam(value = "sort", required = false) String sort,
                              @RequestParam (value = "order", required = false) String order,
                              Model model) {

        Location location = diffService.getLocation(path, revision, revisionTo);
        String diff = diffService.getDiff(
                location.getPath(),
                location.getRevision(),
                location.getRevisionTo()
        );

        if(diff == null)
            return "redirect:/config?message=svn_exception";

        List<String> fileList = diffService.getFileList(diff, sort, order);

        model.addAttribute("location", location);
        model.addAttribute("fileList", fileList);

        if(details != null)
            if(details)
                model.addAttribute("lines", diffService.getLines(diff));

        model.addAttribute("sort", sort != null ? sort : "none");
        model.addAttribute("order", order != null ? order : "none");

        model.addAttribute("loggedUser", new UserDto(userService.getLoggedUser()));

        return "diff";
    }
}
