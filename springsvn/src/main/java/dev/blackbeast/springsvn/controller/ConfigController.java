package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.dto.ConfigDto;
import dev.blackbeast.springsvn.service.AuthorService;
import dev.blackbeast.springsvn.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class ConfigController {

    @Autowired
    ConfigService configService;

    @Autowired
    AuthorService authorService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String init() {
        if(configService.getSvnRepositoryAddress() == null || configService.getSvnRepositoryAddress().equals(""))
            return "redirect:/config";
        else
            return "redirect:/content";
    }

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String config(Model model) {
        ConfigDto config = configService.getConfiguration();

        model.addAttribute("config", config);
        return "config";
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public String saveConfig(@Valid ConfigDto config, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "config";
        }else
            configService.saveConfiguration(config);

        return "redirect:/content";
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public String refresh() {
        configService.refreshData();
        authorService.refresh();
        return "redirect:/content";
    }
}
