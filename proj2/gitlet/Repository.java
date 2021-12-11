package gitlet;

import edu.princeton.cs.algs4.StdRandom;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author LinZhuo
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commits directory */
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    /** The blobs directory */
    public static final File BLOB_DIR = join(GITLET_DIR, "blobs");
    /** The branch pointer directory */
    public static final File BRANCH_DIR = join(GITLET_DIR, "branches");
    /** staging file directory */
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /** adding staging file directory */
    public static final File ADD_DIR = join(STAGING_DIR, "add");
    /** deleting staging file directory */
    public static final File del_DIR = join(STAGING_DIR, "del");
    /** head pointer */
    public static File head = join(GITLET_DIR, "head");
    /** master pointer */
    public static File master = join(BRANCH_DIR, "master");

    /* TODO: fill in the rest of this class. */
    /** set up initial repository */
    public static void setupRepository() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();
        BRANCH_DIR.mkdir();
    }
    /** save commit in directory
     *  and set head, master pointer
     *  @param commit new commit to be saved */
    public static void saveCommit(Commit commit) {
        String UID = sha1(serialize(commit));
        File commitPath = join(COMMIT_DIR, UID);
        writeObject(commitPath, commit);
        writeContents(head, UID);
        writeContents(master, UID);
    }
}
