package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.domain.ContentEntry;
import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ContentController {

    @Autowired
    ContentService contentService;

    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public String showContent(@RequestParam(value = "path", required = false) String path,
                            @RequestParam(value = "revision", required = false) Long revision,
                            @RequestParam(value = "sort", required = false) String sort,
                            @RequestParam (value = "order", required = false) String order,
                            Model model) {
        Revision lastRevision = contentService.getRevisionData(revision);
        Location location = contentService.getLocation(path, lastRevision.getId());

        List<ContentEntry> contentEntries = contentService.getContent(path, revision, sort, order);

        model.addAttribute("location", location);
        model.addAttribute("lastRev", lastRevision);
        model.addAttribute("entryList", contentEntries);
        model.addAttribute("sort", sort != null ? sort : "none");
        model.addAttribute("order", order != null ? order : "none");
        return "content";
    }
}
