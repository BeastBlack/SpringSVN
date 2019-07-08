package dev.blackbeast.springsvn.svn;

import dev.blackbeast.springsvn.domain.ContentEntry;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.service.AuthorService;
import dev.blackbeast.springsvn.service.ConfigService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.ISvnObjectReceiver;
import org.tmatesoft.svn.core.wc2.SvnList;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import java.util.*;

@Data
@Component
public class SVNUtils {

    @Autowired
    ConfigService configService;

    @Autowired
    AuthorService authorService;

    public Revision getRevisionData(Long rev) {
        try {
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(configService.getSvnRepositoryAddress()));

            if(configService.isBasicAuthentication()) {
                ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(configService.getSvnLogin(), configService.getSvnPassword().toCharArray());
                repository.setAuthenticationManager(authManager);
            }

            if (rev == null)
                rev = repository.getLatestRevision();
            else if (rev > repository.getLatestRevision())
                rev = repository.getLatestRevision();

            Collection<SVNLogEntry> log = repository.log(new String[]{""}, null, rev, rev, false, false);
            SVNLogEntry entry = ((SVNLogEntry) log.toArray()[0]);

            Revision revision = new Revision();
            revision.setId(entry.getRevision());
            revision.setDate(entry.getDate());
            revision.setAuthorId(entry.getAuthor());
            revision.setAuthorName(authorService.getAuthorName(entry.getAuthor()));
            revision.setMessage(entry.getMessage());

            repository.closeSession();

            return revision;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private String getAbsoluteSvnPath(String path) {
        if(path == null || path.equals("/"))
            return configService.getSvnRepositoryAddress();
        else
            return configService.getSvnRepositoryAddress() + path;
    }

    public List<ContentEntry> getContent(String path, Long revision) {
        try {
            SvnOperationFactory operationFactory = new SvnOperationFactory();

            if(configService.isBasicAuthentication()) {
                ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(configService.getSvnLogin(), configService.getSvnPassword().toCharArray());
                operationFactory.setAuthenticationManager(authManager);
            }

            SvnList list = operationFactory.createList();
            list.setDepth(SVNDepth.IMMEDIATES);

            if (revision == null)
                list.setRevision(SVNRevision.HEAD);
            else if (revision > SVNRevision.HEAD.getNumber())
                list.setRevision(SVNRevision.HEAD);

            List<ContentEntry> contentEntries = new ArrayList<>();

            list.addTarget(SvnTarget.fromURL(SVNURL.parseURIEncoded(getAbsoluteSvnPath(path)), SVNRevision.HEAD));
            list.setReceiver(new ISvnObjectReceiver<SVNDirEntry>() {
                public void receive(SvnTarget target, SVNDirEntry object) throws SVNException {
                    if(!object.getName().isEmpty()) {
                        ContentEntry contentEntry = new ContentEntry();
                        String name = object.getRelativePath();
                        contentEntry.setName(name);
                        contentEntry.setIsFile(object.getKind() == SVNNodeKind.FILE);

                        Revision revision = new Revision();
                        revision.setId(object.getRevision());
                        revision.setDate(object.getDate());
                        revision.setAuthorId(object.getAuthor());
                        revision.setAuthorName(authorService.getAuthorName(object.getAuthor()));
                        revision.setMessage(object.getCommitMessage());
                        contentEntry.setLastRevision(revision);

                        contentEntries.add(contentEntry);
                    }
                }
            });

            list.run();

            List<Long> revs = new ArrayList<>();
            for(ContentEntry ce : contentEntries)
                revs.add(ce.getLastRevision().getId());

            Map<Long, String> messages = getRevisionsMessages(revs);

            for(ContentEntry ce : contentEntries)
                ce.getLastRevision().setMessage(messages.get(ce.getLastRevision().getId()));

            return contentEntries;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public Map<Long, String> getRevisionsMessages(List<Long> revs) {
        try {
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(configService.getSvnRepositoryAddress()));

            if(configService.isBasicAuthentication()) {
                ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(configService.getSvnLogin(), configService.getSvnPassword().toCharArray());
                repository.setAuthenticationManager(authManager);
            }

            Map<Long, String> messages = new HashMap<>();

            for(Long rev : revs) {
                if (rev == null)
                    rev = repository.getLatestRevision();
                else if (rev > repository.getLatestRevision())
                    rev = repository.getLatestRevision();

                Collection<SVNLogEntry> log = repository.log(new String[]{""}, null, rev, rev, false, false);
                SVNLogEntry entry = ((SVNLogEntry) log.toArray()[0]);
                messages.put(rev, entry.getMessage());
            }

            repository.closeSession();

            return messages;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /*
    public void test() {
        System.out.println("STARTING...");
        try {
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(configService.getSvnRepositoryAddress()));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(configService.getSvnLogin(), configService.getSvnPassword().toCharArray());
            repository.setAuthenticationManager(authManager);

            long lastRev = repository.getLatestRevision();

            SvnOperationFactory operationFactory = new SvnOperationFactory();
            operationFactory.setAuthenticationManager(repository.getAuthenticationManager());
            SvnLog logOperation = operationFactory.createLog();
            logOperation.setSingleTarget(
                    SvnTarget.fromURL(
                            SVNURL.parseURIEncoded(svnRepositoryAddress)));
            logOperation.setRevisionRanges(Collections.singleton(
                    SvnRevisionRange.create(
                            SVNRevision.create(lastRev - 100),
                            SVNRevision.HEAD
                    )
            ) );

            Collection<SVNLogEntry> logEntries = logOperation.run( null );
            System.out.println(">>>>>>>>>>>>>>>>>" + logEntries.size());

            for(SVNLogEntry entry : logEntries) {
                System.out.println(entry.getAuthor());
            }

            final SvnList list = operationFactory.createList();
            list.setDepth(SVNDepth.IMMEDIATES);
            list.setRevision(SVNRevision.HEAD);
            list.addTarget(SvnTarget.fromURL(SVNURL.parseURIEncoded(svnRepositoryAddress), SVNRevision.HEAD));
            list.setReceiver(new ISvnObjectReceiver<SVNDirEntry>() {
                public void receive(SvnTarget target, SVNDirEntry object) throws SVNException {
                    final String name = object.getRelativePath();
                    System.out.println(name);
                }
            });

            list.run();

            repository.closeSession();

            System.out.println("FINISH!...");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }*/
}
