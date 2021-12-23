package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author LinZhuo
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    public String message;
    /** first parent commit */
    public String parent1;
    /** second parent commit */
    public String parent2;
    /** time of the commit */
    public Date time;
    /** Map for blobs in the commit, key is filename, value is UID */
    private Map<String, String> blobs = new HashMap<>();

    /* TODO: fill in the rest of this class. */

    /** initialize commit with nothing */
    public Commit() {
        message = "initial commit";
        parent1 = null;
        parent2 = null;
        time = new Date(0);
    }

    /** create commit with message.
     * Print error message if no file been staged
     * otherwise save staging file in commit.*/
    public Commit(String text) {
        message = text;
        parent1 = readContentsAsString(Repository.head);
        parent2 = null;
        time = new Date();
        blobs = Repository.currentCommit().blobs;
        if (plainFilenamesIn(Repository.ADD_DIR) == null && plainFilenamesIn(Repository.DEL_DIR) == null) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if (plainFilenamesIn(Repository.ADD_DIR) != null) {
            for (String key : plainFilenamesIn(Repository.ADD_DIR)) {
                File addFile = join(Repository.ADD_DIR, key);
                String UID = sha1(readContents(addFile));
                blobs.put(key, UID);
                File addBlob = join(Repository.BLOB_DIR, UID);
                writeContents(addBlob, readContents(addFile));
            }
        }
        if (plainFilenamesIn(Repository.DEL_DIR) != null) {
            for (String key : plainFilenamesIn(Repository.DEL_DIR)) {
                blobs.remove(key);
            }
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

}
