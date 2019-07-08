package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.ContentEntry;
import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.svn.SVNUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {
    @Autowired
    SVNUtils svnUtils;

    private static final String LOCATION_ROOT_PATH = "/";

    public Location getLocation(String path, Long revision) {
        if(path == null)
            path = "/";

        return new Location(path, revision);
    }

    public Revision getRevisionData(Long revision) {
        return svnUtils.getRevisionData(revision);
    }

    public List<ContentEntry> getContent(String path, Long revision) {
        return svnUtils.getContent(path, revision);
    }

    public List<Revision> getHistory(String path) {
        List<Revision> revisionList = new ArrayList<>();

        /*for(int i = 0; i < 30; i++) {
            revisionList.add(getRevisionData(500L-i));
        }*/

        return revisionList;
    }
}
