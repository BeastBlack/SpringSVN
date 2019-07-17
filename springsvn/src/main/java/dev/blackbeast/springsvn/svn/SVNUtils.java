package dev.blackbeast.springsvn.svn;

import dev.blackbeast.springsvn.bugtracker.BugTracker;
import dev.blackbeast.springsvn.date.TimeAgo;
import dev.blackbeast.springsvn.domain.ContentEntry;
import dev.blackbeast.springsvn.domain.Revision;
import dev.blackbeast.springsvn.service.AuthorService;
import dev.blackbeast.springsvn.service.ConfigService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc2.ng.SvnDiffGenerator;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

@Data
@Component
public class SVNUtils {

    @Autowired
    ConfigService configService;

    @Autowired
    AuthorService authorService;

    @Autowired
    BugTracker bugTracker;

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
            revision.setTimeAgo(TimeAgo.timeAgo(entry.getDate()));
            revision.setAuthorId(entry.getAuthor());
            revision.setAuthorName(authorService.getAuthorName(entry.getAuthor()));
            revision.setMessage(configService.isAppBugTrackerIntegrationEnabled() ?
                    bugTracker.format(entry.getMessage()) :
                    entry.getMessage());

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
            return path.startsWith("/") ?
                    configService.getSvnRepositoryAddress() + path :
                    configService.getSvnRepositoryAddress() + "/" + path;
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
                        revision.setTimeAgo(TimeAgo.timeAgo(object.getDate()));
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
                ce.getLastRevision().setMessage(configService.isAppBugTrackerIntegrationEnabled() ?
                        bugTracker.format(messages.get(ce.getLastRevision().getId())) :
                        messages.get(ce.getLastRevision().getId()));

            operationFactory.dispose();

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

    public List<Revision> getHistory(String path, Long revision, Long revisionTo, Long revisionMax) {
        try {
            SvnOperationFactory operationFactory = new SvnOperationFactory();

            if(configService.isBasicAuthentication()) {
                ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(configService.getSvnLogin(), configService.getSvnPassword().toCharArray());
                operationFactory.setAuthenticationManager(authManager);
            }

            SvnLog logOperation = operationFactory.createLog();
            logOperation.setSingleTarget(
                    SvnTarget.fromURL(
                            SVNURL.parseURIEncoded(getAbsoluteSvnPath(path))));
            logOperation.setLimit(revisionMax);

            SVNRevision svnRev = revision != null ? SVNRevision.create(revision) : SVNRevision.HEAD;
            SVNRevision svnRevTo = SVNRevision.create(revisionTo);

            logOperation.setRevisionRanges(Collections.singleton(
                    SvnRevisionRange.create(
                            svnRev,
                            svnRevTo
                    )
            ) );

            Collection<SVNLogEntry> logEntries = logOperation.run( null );

            List<Revision> revisionList = new ArrayList<>();

            for(SVNLogEntry entry : logEntries) {
                Revision rev = new Revision();
                rev.setId(entry.getRevision());
                rev.setDate(entry.getDate());
                rev.setTimeAgo(TimeAgo.timeAgo(entry.getDate()));
                rev.setAuthorId(entry.getAuthor());
                rev.setAuthorName(authorService.getAuthorName(entry.getAuthor()));
                rev.setMessage(configService.isAppBugTrackerIntegrationEnabled() ?
                        bugTracker.format(entry.getMessage()) :
                        entry.getMessage());

                revisionList.add(rev);
            }

            operationFactory.dispose();

            return revisionList;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public String diff(String path, Long revision, Long revisionTo) {
        final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();

        if(configService.isBasicAuthentication()) {
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(configService.getSvnLogin(), configService.getSvnPassword().toCharArray());
            svnOperationFactory.setAuthenticationManager(authManager);
        }

        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final SvnDiffGenerator diffGenerator = new SvnDiffGenerator();
            diffGenerator.setBasePath(new File(""));

            final SvnDiff diff = svnOperationFactory.createDiff();
            diff.setSources(SvnTarget.fromURL(SVNURL.parseURIEncoded(getAbsoluteSvnPath(path)), SVNRevision.create(revision)), SvnTarget.fromURL(SVNURL.parseURIEncoded(getAbsoluteSvnPath(path)), SVNRevision.create(revisionTo)));
            diff.setDiffGenerator(diffGenerator);
            diff.setOutput(byteArrayOutputStream);
            diff.run();

            return byteArrayOutputStream.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            svnOperationFactory.dispose();
        }

        return null;
    }
}
