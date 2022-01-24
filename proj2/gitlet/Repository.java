package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author LinZhuo
 */
public class Repository {
    /**
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
    private static File head = join(GITLET_DIR, "head");
    /** master pointer */
    private static File master = join(BRANCH_DIR, "master");
    /** current branch name */
    private static File currentBranch = join(GITLET_DIR, "currentBranch");


    /** Set up initial repository
     * check whether it is already a git repository
     * create every directory needed */
    public static void setupRepository() {
        if (GITLET_DIR.exists()) {
            String s = "A Gitlet version-control system already exists in the current directory.";
            System.out.println(s);
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();
        BRANCH_DIR.mkdir();
        STAGING_DIR.mkdir();
        ADD_DIR.mkdir();
        DEL_DIR.mkdir();
        writeContents(currentBranch, "master");
    }

    /** check whether .gitlet exist. */
    public static void initialized() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    /** save commit in directory
     *  and set head, master pointer to it
     *  clean staging area if there is file.
     *  @param commit commit to be saved */
    public static void saveCommit(Commit commit) {
        String uid = sha1(serialize(commit));
        File commitPath = join(COMMIT_DIR, uid);
        writeObject(commitPath, commit);
        writeContents(head, uid);
        writeContents(join(BRANCH_DIR, readContentsAsString(currentBranch)), uid);
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
        for (String filename : plainFilenamesIn(directoryPath)) {
            File delFile = join(directoryPath, filename);
            if (directoryPath == CWD) {
                restrictedDelete(delFile);
            } else {
                delFile.delete();
            }
        }
    }

    /** return head commit. */
    public static Commit currentCommit() {
        return readObject(join(COMMIT_DIR, readContentsAsString(head)), Commit.class);
    }

    /** find commit by UID in directory. */
    public static Commit commitByUID(String uid) {
        if (uid.length() == 40) {
            if (join(COMMIT_DIR, uid).exists()) {
                return readObject(join(COMMIT_DIR, uid), Commit.class);
            } else {
                return null;
            }
        } else if (uid.length() >= 6 && uid.length() < 40) {
            for (String commitUID : plainFilenamesIn(COMMIT_DIR)) {
                if (uid.equals(commitUID.substring(0, uid.length()))) {
                    return readObject(join(COMMIT_DIR, commitUID), Commit.class);
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /** get path of blob by UID. */
    private static File blobsByUID(String uid) {
        return join(BLOB_DIR, uid);
    }

    /** add one file to staging area
     * if file is same as current commit, do not stage and remove staged file
     * else rewrite staged file
     * if is staged for removal, undo remove */
    public static void add(String filename) {
        File cwdFile = join(CWD, filename);
        File addFile = join(ADD_DIR, filename);
        File delFile = join(DEL_DIR, filename);
        if (!cwdFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String uidOfcwdFile = sha1(readContents(cwdFile));
        Commit latestCommit = currentCommit();
        if (Objects.equals(latestCommit.getUID(filename), uidOfcwdFile)) {
            if (addFile.exists()) {
                addFile.delete();
            } else if (delFile.exists()) {
                delFile.delete();
            }
        } else {
            writeContents(addFile, readContents(cwdFile));
            if (delFile.exists()) {
                delFile.delete();
            }
        }
    }

    /** remove the file if it is currently staged for addition.
     * If the file is tracked in the current commit,
     * stage it for removal and remove the file from the working directory
     * If the file is neither staged nor tracked by the head commit,
     * print the error message.*/
    public static void rm(String filename) {
        if (directoryEmpty(ADD_DIR)) {
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
            File cwdFile = join(CWD, filename);
            File rmFile = join(DEL_DIR, filename);
            if (cwdFile.exists()) {
                writeContents(rmFile, readContents(cwdFile));
                restrictedDelete(cwdFile);
            } else {
                try {
                    rmFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            String parent1 = commit.getParent1().substring(0, 7);
            String parent2 = commit.getParent2().substring(0, 7);
            System.out.println("Merge: " + parent1 + " " + parent2);
        }
        Date t = commit.getTime();
        String date = String.format(Locale.ENGLISH, "%ta %tb %td %tT %tY %tz", t, t, t, t, t, t);
        System.out.println("Date: " + date);
        System.out.println(commit.getMessage());
        System.out.println();
        if (commit.getParent1() != null) {
            logHelper(commitByUID(commit.getParent1()));
        }
    }

    /** display information about all commit. */
    public static void globalLog() {
        for (String commitUID : plainFilenamesIn(COMMIT_DIR)) {
            Commit commit = commitByUID(commitUID);
            System.out.println("===");
            System.out.println("commit " + sha1(serialize(commit)));
            if (commit.getParent2() != null) {
                String parent1 = commit.getParent1().substring(0, 7);
                String parent2 = commit.getParent2().substring(0, 7);
                System.out.println("Merge: " + parent1 + " " + parent2);
            }
            Date t = commit.getTime();
            String d = String.format(Locale.ENGLISH, "%ta %tb %td %tT %tY %tz", t, t, t, t, t, t);
            System.out.println("Date: " + d);
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    /** recover specific file to head commit version.
     * if it does not exist in head commit
     * print error message. */
    public static void checkout(String filename) {
        if (!currentCommit().containFile(filename)) {
            System.out.println("File does not exist in that commit.");
        } else {
            writeContents(join(CWD, filename), readContents(blob(currentCommit(), filename)));
        }
    }
    /** return blob file path.
     * @param c commit
     * @param f filename
     * */
    private static File blob(Commit c, String f) {
        return blobsByUID(c.getUID(f));
    }
    /** return blob file path.
     * @param uid uid of given commit
     * @param f filename
     * */
    private static File blob(String uid, String f) {
        return blobsByUID(commitByUID(uid).getUID(f));
    }
    /** recover file to specific commit version.
     * if commit don't exist, print error message
     * if file don't exist in that commit, print error message.*/
    public static void checkout(String uid, String filename) {
        if (commitByUID(uid) == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!commitByUID(uid).containFile(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            writeContents(join(CWD, filename), readContents(blob(uid, filename)));
        }
    }
    /** put file at the head commit of the given branch to CWD.
     * if branch no exist, print error message
     * if branch is current branch, print error message
     * if CWD file untracked, print error message */
    public static void checkoutB(String branchName) {
        File branch = join(BRANCH_DIR, branchName);
        String t = "There is an untracked file in the way; delete it, or add and commit it first.";
        if (branch.exists()) {
            if (Objects.equals(branchName, readContentsAsString(currentBranch))) {
                System.out.println("No need to checkout the current branch.");
                System.exit(0);
            } else {
                for (String filename : plainFilenamesIn(CWD)) {
                    if (!currentCommit().containFile(filename)) {
                        System.out.println(t);
                        System.exit(0);
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
                writeContents(currentBranch, branchName);
            }
        } else {
            System.out.println("No such branch exists.");
        }
    }

    /** Prints out the ids of all commits that have the given commit message. */
    public static void find(String message) {
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

    /** Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal. */
    public static void status() {
        branchesStatus();
        stagedFileStatus();
        removedFileStatus();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }
    private static void branchesStatus() {
        System.out.println("=== Branches ===");
        List<String> output = new ArrayList<>();
        for (String branchName : plainFilenamesIn(BRANCH_DIR)) {
            if (Objects.equals(currentCommit().getBranch(), branchName)) {
                System.out.println("*" + branchName);
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
        if (!directoryEmpty(ADD_DIR)) {
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
        if (!directoryEmpty(DEL_DIR)) {
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
                break;
            }
        }
        if (!exist) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else {
            if (Objects.equals(name, readContentsAsString(currentBranch))) {
                System.out.println("Cannot remove the current branch.");
                System.exit(0);
            } else {
                File delBranch = join(BRANCH_DIR, name);
                delBranch.delete();
            }
        }
    }

    /** Checks out all the files tracked by the given commit.
     * moves the current branch’s head to that commit node. */
    public static void reset(String uid) {
        String t = "There is an untracked file in the way; delete it, or add and commit it first.";
        if (commitByUID(uid) == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!currentCommit().containFile(fileName) && commitByUID(uid).containFile(fileName)) {
                System.out.println(t);
                System.exit(0);
            }
            if (commitByUID(uid).containFile(fileName)) {
                checkout(uid, fileName);
            } else {
                restrictedDelete(fileName);
            }
        }
        String fullUID;
        if (uid.length() == 40) {
            fullUID = uid;
        } else {
            fullUID = sha1(serialize(commitByUID(uid)));
        }
        writeContents(head, fullUID);
        writeContents(join(BRANCH_DIR, readContentsAsString(currentBranch)), fullUID);
        cleanStagingArea();
    }

    /** merge current commit and given branch head, resulting a new commit.
     * there are 8 different file situation to solve */
    public static void merge(String name) {
        String t = "There is an untracked file in the way; delete it, or add and commit it first.";
        if (!directoryEmpty(ADD_DIR) || !directoryEmpty(DEL_DIR)) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!plainFilenamesIn(BRANCH_DIR).contains(name)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (Objects.equals(name, readContentsAsString(currentBranch))) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Commit splitNode = findSplitNode(name);
        if (sha1(serialize(splitNode)).equals(readContentsAsString(join(BRANCH_DIR, name)))) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (sha1(serialize(splitNode)).equals(readContentsAsString(head))) {
            checkoutB(name);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        Commit branchHead = commitByUID(readContentsAsString(join(BRANCH_DIR, name)));
        Set<String> c = currentCommit().keySet();
        Set<String> s = splitNode.keySet();
        Set<String> b = branchHead.keySet();
        Set<String> allFile = mergeSet(c, s, b);
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!currentCommit().containFile(fileName) && branchHead.containFile(fileName)) {
                System.out.println(t);
                System.exit(0);
            }
        }
        for (String fileName : allFile) {
            if (splitNode.containFile(fileName) && branchHead.containFile(fileName) && currentCommit().containFile(fileName)) {
                if (!Objects.equals(branchHead.getUID(fileName), splitNode.getUID(fileName)) && Objects.equals(splitNode.getUID(fileName), currentCommit().getUID(fileName))) {
                    /* case 1 : modified in branch not modified in current. */
                    checkout(sha1(serialize(branchHead)), fileName);
                    add(fileName);
                } else if (!Objects.equals(currentCommit().getUID(fileName), splitNode.getUID(fileName)) && Objects.equals(splitNode.getUID(fileName), branchHead.getUID(fileName))) {
                    /* case 2 : modified in current not modified in branch. */
                    checkout(fileName);
                } else if (Objects.equals(currentCommit().getUID(fileName), branchHead.getUID(fileName))) {
                    /* case 3.1 : both modified with same content. */
                    checkout(fileName);
                } else if (!Objects.equals(currentCommit().getUID(fileName), branchHead.getUID(fileName))) {
                    /* case 8 : both changed differently */
                    conflict(fileName, currentCommit(), branchHead);
                }
            } else if (!splitNode.containFile(fileName)) {
                if (currentCommit().containFile(fileName) && !branchHead.containFile(fileName)) {
                    /* case 4 : present only in current. */
                    checkout(fileName);
                } else if (!currentCommit().containFile(fileName) && branchHead.containFile(fileName)) {
                    /* case 5 : present only in branch. */
                    checkout(sha1(serialize(branchHead)), fileName);
                    add(fileName);
                } else if (!Objects.equals(currentCommit().getUID(fileName), branchHead.getUID(fileName))) {
                    /* case 8 : absent in split, different in current and branch. */
                    conflict(fileName, currentCommit(), branchHead);
                }
            } else if (!branchHead.containFile(fileName) && currentCommit().containFile(fileName)) {
                if (Objects.equals(currentCommit().getUID(fileName), splitNode.getUID(fileName))) {
                    /* case 6 : absent in branch, same in current and split */
                    rm(fileName);
                } else {
                    /* case 8 : current changed and branch deleted. */
                    conflict(fileName, currentCommit(), null);
                }
            } else if (!currentCommit().containFile(fileName) && branchHead.containFile(fileName)) {
                if (!Objects.equals(branchHead.getUID(fileName), splitNode.getUID(fileName))) {
                    /* case 8 : branch changed and current deleted. */
                    conflict(fileName, null, branchHead);
                }
            }
        }
        Commit mergeCommit = new Commit("Merged " + name + " into " + readContentsAsString(currentBranch) + ".", sha1(serialize(branchHead)));
        saveCommit(mergeCommit);
    }
    /** return SplitNode between current node and given branch head node. */
    private static Commit findSplitNode(String name) {
        Set<String> ancestor = currentAncestor(currentCommit());
        String branchUid = readContentsAsString(join(BRANCH_DIR, name));
        while (branchUid != null) {
            if (ancestor.contains(branchUid)) {
                return commitByUID(branchUid);
            }
            branchUid = commitByUID(branchUid).getParent1();
        }
        return null;
    }
    /** put all the ancestor of current commit in a list. */
    private static Set<String> currentAncestor(Commit current) {
        Set<String> ancestor = new HashSet<>();
        currentAncestorHelper(sha1(serialize(current)), ancestor);
        return ancestor;
    }
    /** recursively get all the ancestor. */
    private static void currentAncestorHelper(String uid, Set<String> ancestor) {
        if (uid != null) {
            Commit current = commitByUID(uid);
            ancestor.add(uid);
            if (current.getParent2() != null) {
                currentAncestorHelper(current.getParent2(), ancestor);
            }
            currentAncestorHelper(current.getParent1(), ancestor);
        }
    }
    /** merge splitNode branchNode currentNode file into a set. */
    private static Set<String> mergeSet(Set<String> a, Set<String> b, Set<String> c) {
        Set<String> out = new HashSet<>(a);
        out.addAll(b);
        out.addAll(c);
        return out;
    }
    /** replace conflict file with required content. */
    private static void conflict(String fileName, Commit  current, Commit branch) {
        if (current == null) {
            writeContents(join(CWD, fileName), "<<<<<<< HEAD\n", "=======\n", readContents(blobsByUID(branch.getUID(fileName))), ">>>>>>>\n");
        } else if (branch == null) {
            writeContents(join(CWD, fileName), "<<<<<<< HEAD\n", readContents(blobsByUID(current.getUID(fileName))), "=======\n", ">>>>>>>\n");
        } else {
            writeContents(join(CWD, fileName), "<<<<<<< HEAD\n", readContents(blobsByUID(current.getUID(fileName))), "=======\n", readContents(blobsByUID(branch.getUID(fileName))), ">>>>>>>\n");
        }
        add(fileName);
        System.out.println("Encountered a merge conflict.");
    }

    /** get private variable. */
    public static File getHead() {
        return head;
    }
    public static File getCurrentBranch() {
        return currentBranch;
    }
}
