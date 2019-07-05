package dev.blackbeast.springsvn.svn;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.*;

import java.util.Collection;
import java.util.Collections;

public class SVNUtils {

    private final static String url = "https://svn.centrala.bzwbk:443/svn/svn8/trunk";

    public void test() {
        System.out.println("STARTING...");
        try {
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager("158113", "Panzer88".toCharArray());
            repository.setAuthenticationManager(authManager);

            long lastRev = repository.getLatestRevision();

            SvnOperationFactory operationFactory = new SvnOperationFactory();
            operationFactory.setAuthenticationManager(repository.getAuthenticationManager());
            SvnLog logOperation = operationFactory.createLog();
            logOperation.setSingleTarget(
                    SvnTarget.fromURL(
                            SVNURL.parseURIEncoded(url)));
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
            list.addTarget(SvnTarget.fromURL(SVNURL.parseURIEncoded(url), SVNRevision.HEAD));
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
    }
}
