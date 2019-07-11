package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.ContentEntry;
import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.svn.SVNUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ContentService {
    @Autowired
    SVNUtils svnUtils;

    private final String SORT_NAME = "name";
    private final String SORT_REVISION = "revision";
    private final String SORT_DATE = "date";
    private final String SORT_AUTHOR_ID = "authorId";
    private final String SORT_AUTHOR_NAME = "authorName";
    private final String SORT_MESSAGE = "message";

    private final String ORDER_ASC = "asc";

    private static final String LOCATION_ROOT_PATH = "/";

    public Location getLocation(String path, Long revision) {
        if(path == null)
            path = "/";

        if(path.equals(""))
            path = "/";

        return new Location(path, revision);
    }

    public Revision getRevisionData(Long revision) {
        return svnUtils.getRevisionData(revision);
    }

    public List<ContentEntry> getContent(String path, Long revision, String sort, String order) {
        List<ContentEntry> contentEntries = svnUtils.getContent(path, revision);

        if(sort != null){
            if(sort.equals(SORT_NAME)) {
                contentEntries.sort(ORDER_ASC.equals(order) ?
                        Comparator.comparing(ContentEntry::getName).reversed() :
                        Comparator.comparing(ContentEntry::getName));
            }

            if(sort.equals(SORT_REVISION)) {
                contentEntries.sort(ORDER_ASC.equals(order) ?
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getId()).reversed() :
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getId()));
            }

            if(sort.equals(SORT_DATE)) {
                contentEntries.sort(ORDER_ASC.equals(order) ?
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getDate()).reversed() :
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getDate()));
            }

            if(sort.equals(SORT_AUTHOR_ID)) {
                contentEntries.sort(ORDER_ASC.equals(order) ?
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getAuthorId()).reversed() :
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getAuthorId()));
            }

            if(sort.equals(SORT_AUTHOR_NAME)) {
                contentEntries.sort(ORDER_ASC.equals(order) ?
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getAuthorName()).reversed() :
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getAuthorName()));
            }

            if(sort.equals(SORT_MESSAGE)) {
                contentEntries.sort(ORDER_ASC.equals(order) ?
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getMessage()).reversed() :
                        Comparator.comparing((ContentEntry contentEntry) -> contentEntry.getLastRevision().getMessage()));
            }
        }

        return contentEntries;
    }
}
