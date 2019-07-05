package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.ContentEntry;
import dev.blackbeast.springsvn.domain.Location;
import dev.blackbeast.springsvn.domain.Revision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentService {
    private static final String LOCATION_ROOT_PATH = "/";

    public Location getLocation(String path, Long revision) {
        if(path == null)
            path = LOCATION_ROOT_PATH;

        if(revision == null)
            revision = 1000L;

        return new Location(path, revision);
    }

    public Revision getRevisionData(Long id) {
        Revision revision = new Revision();
        revision.setId(id);
        revision.setAuthorId("123456");
        revision.setAuthorName("Adam Nowak");
        revision.setDate(new Date());
        revision.setMessage("Test commit");
        return revision;
    }

    public List<ContentEntry> getContent(String path) {
        List<ContentEntry> contentEntries = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            ContentEntry contentEntry = new ContentEntry();
            contentEntry.setName("katalog-w-svn");
            contentEntry.setLastRevision(getRevisionData(500L-i));
            contentEntries.add(contentEntry);
        }

        return contentEntries;
    }
}
