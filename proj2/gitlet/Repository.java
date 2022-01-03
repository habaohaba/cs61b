package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
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
    /** current branch name */
    public static String currentBranch = "master";

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
        writeContents(join(BRANCH_DIR, currentBranch), UID);
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
        if (UID.length() == 40) {
            if (join(COMMIT_DIR, UID).exists()) {
                return readObject(join(COMMIT_DIR, UID), Commit.class);
            } else {
                return null;
            }
        } else if (UID.length() >= 6 && UID.length() < 40) {
            if (plainFilenamesIn(COMMIT_DIR) != null) {
                for (String commitUID : plainFilenamesIn(COMMIT_DIR)) {
                    if (UID.equals(commitUID.substring(0, UID.length()))) {
                        return readObject(join(COMMIT_DIR, commitUID), Commit.class);
                    }
                }
            }
            return null;
        } else return null;
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
        if (commit.getParent2() != null) {
            System.out.println("Merge: " + commit.getParent1().substring(0,7) + commit.getParent2().substring(0,7));
        }
        System.out.println("Date: " + String.format(Locale.ENGLISH, "%ta %tb %td %tT %tY %tz", commit.getTime(), commit.getTime(), commit.getTime(), commit.getTime(), commit.getTime(), commit.getTime()));
        System.out.println(commit.getMessage());
        System.out.println();
        if (commit.getParent1()!= null) {
            logHelper(commitByUID(commit.getParent1()));
        }
    }

    /** display information about all commit. */
    public static void globalLog() {
        if (plainFilenamesIn(COMMIT_DIR) != null) {
            for (String commitUID : plainFilenamesIn(COMMIT_DIR)) {
                Commit commit = commitByUID(commitUID);
                System.out.println("===");
                System.out.println("commit " + sha1(serialize(commit)));
                if (commit.getParent2() != null) {
                    System.out.println("Merge: " + commit.getParent1().substring(0, 7) + commit.getParent2().substring(0, 7));
                }
                System.out.println("Date: " + String.format(Locale.ENGLISH, "%ta %tb %td %tT %tY %tz", commit.getTime(), commit.getTime(), commit.getTime(), commit.getTime(), commit.getTime(), commit.getTime()));
                System.out.println(commit.getMessage());
                System.out.println();
            }
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
            if (Objects.equals(branchName, currentBranch)) {
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
                currentBranch = branchName;
            }
        } else {
            System.out.println("No such branch exists.");
        }
    }

    /** Prints out the ids of all commits that have the given commit message. */
    public static void find(String message) {
        if (plainFilenamesIn(COMMIT_DIR) != null) {
            boolean out = false;
            for (String commitUID : plainFilenamesIn(COMMIT_DIR)) {
                Commit commit = commitByUID(commitUID);
                if (Objects.equals(commit.getMessage(), message)) {
                    System.out.println(commitUID);
                    out = true;
                }
            }
            if (!out) {
                System.out.println("Found no commit with that message.");
            }
        }
    }

    /** Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal. */
    public static void status() {
        branchesStatus();
        stagedFileStatus();
        removedFileStatus();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("Untracked Files ===");
        System.out.println();
    }
    private static void branchesStatus() {
        System.out.println("=== Branches ===");
        List<String> output = new ArrayList<>();
        for (String branchName : plainFilenamesIn(BRANCH_DIR)) {
            if (Objects.equals(currentCommit().getBranch(), branchName)) {
                System.out.println("*"+branchName);
            } else {
                output.add(branchName);
            }
        }
        if (!output.isEmpty()) {
            output.sort(null);
            for (String text : output) {
                System.out.println(text);
            }
        }
        System.out.println();
    }
    private static void stagedFileStatus() {
        System.out.println("=== Staged Files ===");
        if (plainFilenamesIn(ADD_DIR) != null) {
            List<String> output = new ArrayList<>(plainFilenamesIn(ADD_DIR));
            output.sort(null);
            for (String text : output) {
                System.out.println(text);
            }
        }
        System.out.println();
    }
    private static void removedFileStatus() {
        System.out.println("=== Removed Files ===");
        if (plainFilenamesIn(DEL_DIR) != null) {
            List<String> output = new ArrayList<>(plainFilenamesIn(DEL_DIR));
            output.sort(null);
            for (String text : output) {
                System.out.println(text);
            }
        }
        System.out.println();
    }

    /** Creates a new branch with the given name, and points it at the current head commit. */
    public static void branch(String name) {
        for (String branchName : plainFilenamesIn(BRANCH_DIR)) {
            if (Objects.equals(branchName, name)) {
                System.out.println("A branch with that name already exists.");
                System.exit(0);
            }
        }
        File newBranch = join(BRANCH_DIR, name);
        writeContents(newBranch, readContentsAsString(head));
    }

    /** Deletes the branch with the given name. */
    public static void rmBranch(String name) {
        boolean exist = false;
        for (String branchName : plainFilenamesIn(BRANCH_DIR)) {
            if (Objects.equals(branchName, name)) {
                exist = true;
            }
        }
        if (!exist) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else {
            if (Objects.equals(name, currentBranch)) {
                System.out.println("Cannot remove the current branch.");
                System.exit(0);
            } else {
                File delBranch = join(BRANCH_DIR, name);
                restrictedDelete(delBranch);
            }
        }
    }

    /** Checks out all the files tracked by the given commit.
     * moves the current branch’s head to that commit node. */
    public static void reset(String UID) {
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!currentCommit().containFile(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            if (commitByUID(UID) == null) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            } else {
                if (commitByUID(UID).containFile(fileName)) {
                    checkout(UID, fileName);
                } else {
                    restrictedDelete(fileName);
                }
            }
        }
        String fullUID;
        if (UID.length() == 40) {
            fullUID = UID;
        } else {
            fullUID = sha1(serialize(commitByUID(UID)));
        }
        writeContents(head, fullUID);
        writeContents(join(BRANCH_DIR, currentBranch), fullUID);
        cleanStagingArea();
    }
}
