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
                              @RequestParam(value = "thresholdFrom", required = false) String thresholdFrom,
                              @RequestParam(value = "thresholdTo", required = false) String thresholdTo,
                              @RequestParam(value = "revisionMax", required = false) Long revisionMax,
                              @RequestParam(value = "searchText", required = false) String searchText,
                              @RequestParam(value = "sort", required = false) String sort,
                              @RequestParam (value = "order", required = false) String order,
                              Model model) {

        Location location = historyService.getLocation(path, thresholdFrom, thresholdTo, revisionMax, searchText);
        List<Revision> revisionList = historyService.getHistory(
                location.getPath(),
                location.getThresholdFrom(),
                location.getThresholdTo(),
                location.getRevisionMax(),
                location.getSearchText(),
                sort,
                order
        );

        if(revisionList == null)
            return "redirect:/config?message=svn_exception";

        model.addAttribute("location", location);
        model.addAttribute("revisionList", revisionList);
        model.addAttribute("sort", sort != null ? sort : "none");
        model.addAttribute("order", order != null ? order : "none");
        return "history";
    }
}
