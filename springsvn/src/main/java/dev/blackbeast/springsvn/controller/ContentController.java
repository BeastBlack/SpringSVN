package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContentController {

    @Autowired
    ContentService contentService;

    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public String showContent(@RequestParam(value = "path", required = false) String path,
                            @RequestParam(value = "revision", required = false) Long revision,
                            Model model) {
        Location location = contentService.getLocation(path, revision);
        Revision lastRevision = contentService.getRevisionData(location.getRevision());

        model.addAttribute("location", location);
        model.addAttribute("lastRev", lastRevision);
        return "content";
    }
}
