package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.svn.SVNUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    @Autowired
    SVNUtils svnUtils;

    public Location getLocation(String path, Long revision, Long revisionTo, Long revisionMax) {
        if(path == null)
            path = "/";

        if(path.equals(""))
            path = "/";

        if(revision == null)
            revision = svnUtils.getRevisionData(null).getId();

        if(revisionTo == null)
            revisionTo = 1L;

        if(revisionMax == null)
            revisionMax = 100L;

        return new Location(path, revision, revisionTo, revisionMax);
    }

    public List<Revision> getHistory(String path, Long revision, Long revisionTo, Long revisionMax) {
        if(path == null)
            path = "/";

        if(revisionTo == null)
            revisionTo = 1L;

        if(revisionMax == null)
            revisionMax = 100L;

        return svnUtils.getHistory(path, revision, revisionTo, revisionMax);
    }
}
