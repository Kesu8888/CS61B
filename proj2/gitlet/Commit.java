package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date; // TODO: You'll likely use this in this class

 /*  @author FU KAIQI
 */

public class Commit implements Serializable {

    private String message;
    private String timestamp;
    private String parent;

    public Commit(String message, String parent) {
        this.parent = parent;
        this.message = message;
        if (this.parent == null) {
            this.timestamp = new Date().toString();
        }
    }

    public String getCommit() {
        return this.message;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getParent() {
        return this.parent;
    }
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /* TODO: fill in the rest of this class. */
}
