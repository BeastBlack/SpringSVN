package dev.blackbeast.springsvn.controller;

import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String showHistory(@RequestParam(value = "path", required = false) String path,
                              @RequestParam(value = "revision", required = false) Long revision,
                              @RequestParam(value = "revisionTo", required = false) Long revisionTo,
                              @RequestParam(value = "revisionMax", required = false) Long revisionMax,
                              @RequestParam(value = "sort", required = false) String sort,
                              @RequestParam (value = "order", required = false) String order,
                              Model model) {
        Location location = historyService.getLocation(path, revision, revisionTo, revisionMax);
        List<Revision> revisionList = historyService.getHistory(
                location.getPath(),
                location.getRevision(),
                location.getRevisionTo(),
                location.getRevisionMax(),
                sort,
                order
        );

        model.addAttribute("location", location);
        model.addAttribute("revisionList", revisionList);
        model.addAttribute("sort", sort != null ? sort : "none");
        model.addAttribute("order", order != null ? order : "none");
        return "history";
    }
}
