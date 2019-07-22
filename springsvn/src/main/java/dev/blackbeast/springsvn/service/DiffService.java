package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.svn.SVNUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class DiffService {
    @Autowired
    SVNUtils svnUtils;

    private final String SORT_NAME = "name";
    private final String ORDER_DESC = "desc";

    public Location getLocation(String path, Long revision, Long revisionTo) {
        if(path == null)
            path = "/";

        if(path.equals(""))
            path = "/";

        if(revision == null)
            revision = svnUtils.getRevisionData(null).getId();

        if(revisionTo == null)
            revisionTo = revision - 1L;

        return new Location(path, revision, revisionTo);
    }

    public String getDiff(String path, Long revision, Long revisionTo) {
        return svnUtils.diff(path, revision, revisionTo);
    }

    public List<String> getFileList(String diff, String sort, String order) {
        String[] lines = diff.split(System.getProperty("line.separator"));

        List<String> files = new ArrayList<>();

        for(String line : lines)
            if(line.startsWith("Index: "))
                files.add(line.substring(7, line.indexOf('=')));

        if(sort != null) {
            if (sort.equals(SORT_NAME)) {
                files.sort(ORDER_DESC.equals(order) ?
                        Comparator.reverseOrder() :
                        Comparator.naturalOrder());
            }
        }

        return files;
    }

    public List<String> getLines(String diff) {
        String[] lines = diff.split("\n");

        if(lines != null) {
            List<String> linesList = new ArrayList<>();
            for(String line : lines)
                if(!line.trim().isEmpty() && !line.startsWith("==="))
                    linesList.add(line.replace("+++", "").replace("---", "").trim());

            return linesList;
        } else
            return null;
    }
}
