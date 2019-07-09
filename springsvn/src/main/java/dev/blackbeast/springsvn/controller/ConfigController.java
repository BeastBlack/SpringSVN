package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.service.AuthorService;
import dev.blackbeast.springsvn.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConfigController {

    @Autowired
    ConfigService configService;

    @Autowired
    AuthorService authorService;

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public String refresh() {
        configService.refreshData();
        authorService.refresh();
        return "redirect:/content";
    }
}
