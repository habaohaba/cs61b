package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author LinZhuo
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** first parent commit */
    private String parent1;
    /** second parent commit */
    private String parent2;
    /** time of the commit */
    private Date time;
    /** branch of commit */
    private String branch;
    /** Map for blobs in the commit, key is filename, value is UID */
    private Map<String, String> blobs = new HashMap<>();

    /** initialize commit */
    public Commit() {
        message = "initial commit";
        parent1 = null;
        parent2 = null;
        time = new Date(0);
        branch = readContentsAsString(Repository.getCurrentBranch());
    }

    /** create commit with message.
     * Print error message if no file been staged
     * otherwise save staging file in commit.*/
    public Commit(String text) {
        message = text;
        parent1 = readContentsAsString(Repository.getHead());
        parent2 = null;
        time = new Date();
        blobs = Repository.currentCommit().blobs;
        branch = readContentsAsString(Repository.getCurrentBranch());
        if (directoryEmpty(Repository.ADD_DIR) && directoryEmpty(Repository.DEL_DIR)) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        for (String key : plainFilenamesIn(Repository.ADD_DIR)) {
            File addFile = join(Repository.ADD_DIR, key);
            String uid = sha1(readContents(addFile));
            blobs.put(key, uid);
            File addBlob = join(Repository.BLOB_DIR, uid);
            writeContents(addBlob, readContents(addFile));
        }
        for (String key : plainFilenamesIn(Repository.DEL_DIR)) {
            blobs.remove(key);
        }
    }

    /** create merge commit. */
    public Commit(String text, String branchHeadUID) {
        message = text;
        parent1 = readContentsAsString(Repository.getHead());
        parent2 = branchHeadUID;
        time = new Date();
        blobs = Repository.currentCommit().blobs;
        branch = readContentsAsString(Repository.getCurrentBranch());
        if (directoryEmpty(Repository.ADD_DIR) && directoryEmpty(Repository.DEL_DIR)) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        for (String key : plainFilenamesIn(Repository.ADD_DIR)) {
            File addFile = join(Repository.ADD_DIR, key);
            String uid = sha1(readContents(addFile));
            blobs.put(key, uid);
            File addBlob = join(Repository.BLOB_DIR, uid);
            writeContents(addBlob, readContents(addFile));
        }
        for (String key : plainFilenamesIn(Repository.DEL_DIR)) {
            blobs.remove(key);
        }
    }

    /** check whether commit contain specific file.
     * @param filename name of the file */
    public boolean containFile(String filename) {
        return blobs.containsKey(filename);
    }

    /** get the UID of specific file in this commit.
     * @param filename name of the file
     * @return UID of this file */
    public String getUID(String filename) {
        if (containFile(filename)) {
            return blobs.get(filename);
        } else {
            return null;
        }
    }


    /** return keySet of commit. */
    public Set<String> keySet() {
        return blobs.keySet();
    }

    public String getMessage() {
        return message;
    }
    public String getParent1() {
        return parent1;
    }
    public String getParent2() {
        return parent2;
    }
    public Date getTime() {
        return time;
    }
    public String getBranch() {
        return branch;
    }
}
