package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.svn.SVNUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class HistoryService {
    @Autowired
    SVNUtils svnUtils;

    private final String SORT_REVISION = "revision";
    private final String SORT_DATE = "date";
    private final String SORT_AUTHOR_ID = "authorId";
    private final String SORT_AUTHOR_NAME = "authorName";
    private final String SORT_MESSAGE = "message";

    private final String ORDER_DESC = "desc";

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

    public List<Revision> getHistory(String path, Long revision, Long revisionTo, Long revisionMax, String sort, String order) {
        if(path == null)
            path = "/";

        if(revisionTo == null)
            revisionTo = 1L;

        if(revisionMax == null)
            revisionMax = 100L;

        List<Revision> history = svnUtils.getHistory(path, revision, revisionTo, revisionMax);

        if(history != null && sort != null) {
            if (sort.equals(SORT_REVISION)) {
                history.sort(ORDER_DESC.equals(order) ?
                        Comparator.comparing(Revision::getId).reversed() :
                        Comparator.comparing(Revision::getId));
            }

            if (sort.equals(SORT_DATE)) {
                history.sort(ORDER_DESC.equals(order) ?
                        Comparator.comparing(Revision::getDate).reversed() :
                        Comparator.comparing(Revision::getDate));
            }

            if (sort.equals(SORT_AUTHOR_ID)) {
                history.sort(ORDER_DESC.equals(order) ?
                        Comparator.comparing(Revision::getAuthorId).reversed() :
                        Comparator.comparing(Revision::getAuthorId));
            }

            if (sort.equals(SORT_AUTHOR_NAME)) {
                history.sort(ORDER_DESC.equals(order) ?
                        Comparator.comparing(Revision::getAuthorName).reversed() :
                        Comparator.comparing(Revision::getAuthorName));
            }

            if (sort.equals(SORT_MESSAGE)) {
                history.sort(ORDER_DESC.equals(order) ?
                        Comparator.comparing(Revision::getMessage).reversed() :
                        Comparator.comparing(Revision::getMessage));
            }
        }

        return history;
    }
}
