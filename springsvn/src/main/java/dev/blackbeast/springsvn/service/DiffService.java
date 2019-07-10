package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.svn.SVNUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiffService {
    @Autowired
    SVNUtils svnUtils;

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

    public List<String> getFileList(String diff) {
        String[] lines = diff.split(System.getProperty("line.separator"));

        List<String> files = new ArrayList<>();

        for(String line : lines)
            if(line.startsWith("Index: "))
                files.add(line.substring(7, line.indexOf('=')));

        return files;
    }
}
