package gitlet;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdRandom;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

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
    public static final File DEL_DIR = join(STAGING_DIR, "del");
    /** head pointer */
    public static File head = join(GITLET_DIR, "head");
    /** master pointer */
    public static File master = join(BRANCH_DIR, "master");

    /* TODO: fill in the rest of this class. */

    /** Set up initial repository
     * check whether it is already a git repository
     * create every directory needed */
    public static void setupRepository() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();
        BRANCH_DIR.mkdir();
        STAGING_DIR.mkdir();
        ADD_DIR.mkdir();
        DEL_DIR.mkdir();
    }

    /** save commit in directory
     *  and set head, master pointer to it
     *  clean staging area if there is file.
     *  @param commit commit to be saved */
    public static void saveCommit(Commit commit) {
        String UID = sha1(serialize(commit));
        File commitPath = join(COMMIT_DIR, UID);
        writeObject(commitPath, commit);
        writeContents(head, UID);
        writeContents(master, UID);
        cleanStagingArea();
    }

    /** clean staging area if there is any file. */
    public static void cleanStagingArea() {
       cleanDirectory(ADD_DIR);
       cleanDirectory(DEL_DIR);
    }

    /** empty specific directory.
     * @param directoryPath path of directory */
    private static void cleanDirectory(File directoryPath) {
        if (plainFilenamesIn(directoryPath) != null) {
            for (String filename : plainFilenamesIn(directoryPath)) {
                File delFile = join(directoryPath, filename);
                if (directoryPath == CWD) {
                    restrictedDelete(delFile);
                } else {
                    delFile.delete();
                }
            }
        }
    }

    /** return head commit. */
    public static Commit currentCommit() {
        return readObject(join(COMMIT_DIR, readContentsAsString(head)), Commit.class);
    }

    /** find commit by UID in directory. */
    public static Commit commitByUID(String UID) {
        if (join(COMMIT_DIR, UID).exists()) {
            return readObject(join(COMMIT_DIR, UID), Commit.class);
        } else {
            return null;
        }
    }

    /** get path of blob by UID. */
    private static File blobsByUID(String UID) {
        return join(BLOB_DIR, UID);
    }

    /** add one file to staging area
     * if file is same as current commit, do not stage and remove staged file
     * else rewrite staged file */
    public static void add(String filename) {
        File CWDFile = join(CWD, filename);
        File addFile = join(ADD_DIR, filename);
        if (!CWDFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String UIDofCWDFile = sha1(readContents(CWDFile));
        Commit latestCommit = currentCommit();
        if (latestCommit.containFile(filename)) {
            if (Objects.equals(latestCommit.getUID(filename), UIDofCWDFile)) {
                if (addFile.exists()) {
                    restrictedDelete(addFile);
                }
            } else {
                writeContents(addFile, readContents(CWDFile));
            }
        } else {
            writeContents(addFile, readContents(CWDFile));
        }
    }

    /** remove the file if it is currently staged for addition.
     * If the file is tracked in the current commit,
     * stage it for removal and remove the file from the working directory
     * If the file is neither staged nor tracked by the head commit,
     * print the error message.*/
    public static void rm(String filename) {
        if (plainFilenamesIn(ADD_DIR) == null) {
            if (!currentCommit().containFile(filename)) {
                System.out.println("No reason to remove the file.");
                System.exit(0);
            }
        } else {
            if (plainFilenamesIn(ADD_DIR).contains(filename)) {
                File rmFile = join(ADD_DIR, filename);
                rmFile.delete();
            } else {
                if (!currentCommit().containFile(filename)) {
                    System.out.println("No reason to remove the file.");
                    System.exit(0);
                }
            }
        }
        if (currentCommit().containFile(filename)) {
            File CWDFile = join(CWD, filename);
            File rmFile = join(DEL_DIR, filename);
            writeContents(rmFile, readContents(CWDFile));
            restrictedDelete(CWDFile);
        }
    }

    /** Starting at the current head commit,
     * display information about each commit backwards
     * along the commit tree until the initial commit,
     * following the first parent commit links */
    public static void log() {
        logHelper(currentCommit());
    }
    private static void logHelper(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + sha1(serialize(commit)));
        if (commit.parent2 != null) {
            System.out.println("Merge: " + commit.parent1.substring(0,7) + commit.parent2.substring(0,7));
        }
        System.out.println("Date: " + String.format(Locale.ENGLISH, "%ta %tb %td %tT %tY %tz", commit.time, commit.time, commit.time, commit.time, commit.time, commit.time));
        System.out.println(commit.message);
        System.out.println();
        if (commit.parent1 != null) {
            logHelper(commitByUID(commit.parent1));
        }
    }

    /** recover specific file to head commit version.
     * if it does not exist in head commit
     * print error message. */
    public static void checkout(String filename) {
        if (!currentCommit().containFile(filename)) {
            System.out.println("File does not exist in that commit.");
        } else {
            writeContents(join(CWD, filename), readContents(blobsByUID(currentCommit().getUID(filename))));
        }
    }
    /** recover file to specific commit version.
     * if commit don't exist, print error message
     * if file don't exist in that commit, print error message.*/
    public static void checkout(String UID, String filename) {
        if (commitByUID(UID) == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!commitByUID(UID).containFile(filename)) {
            System.out.println("File does not exist in that commit.");
        } else {
            writeContents(join(CWD, filename), readContents(blobsByUID(commitByUID(UID).getUID(filename))));
        }
    }
    /** put file at the head commit of the given branch to CWD.
     * if branch no exist, print error message
     * if branch is current branch, print error message
     * if CWD file untracked, print error message */
    public static void checkoutB(String branchName) {
        File branch = join(BRANCH_DIR, branchName);
        if (branch.exists()) {
            if (readContentsAsString(head).equals(readContentsAsString(branch))) {
                System.out.println("No need to checkout the current branch.");
                System.exit(0);
            } else {
                if (plainFilenamesIn(CWD) != null) {
                    for (String filename : plainFilenamesIn(CWD)) {
                        if (!currentCommit().containFile(filename)) {
                            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                            System.exit(0);
                        }
                    }
                }
                cleanDirectory(CWD);
                Commit branchCommit = commitByUID(readContentsAsString(branch));
                for (String filename : branchCommit.keySet()) {
                    File addFile = join(CWD, filename);
                    writeContents(addFile, readContents(blobsByUID(branchCommit.getUID(filename))));
                }
                cleanStagingArea();
                writeContents(head, readContentsAsString(branch));
            }
        } else {
            System.out.println("No such branch exists.");
        }
    }

}
