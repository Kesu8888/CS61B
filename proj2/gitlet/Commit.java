package gitlet;
import java.io.Serializable;
import java.util.TreeMap;
/*  @author FU KAIQI
 */

public class Commit implements Serializable {

    private String message;
    private String timestamp;
    private String parent;
    private String myID;
    private TreeMap<String, String> trackedFiles;
    private String log;
    private String mergeLog;

    public Commit(String message, String parent, TreeMap<String, String> trackedFiles) {
        this.parent = parent;
        this.message = message;
        this.timestamp = Date.getDateNow();
        this.myID = Utils.sha1(toString());
        this.trackedFiles = trackedFiles;
    }

    public Commit(String message, String parent, String timestamp) {
        this.parent = parent;
        this.message = message;
        this.timestamp = timestamp;
        this.myID = Utils.sha1(message + timestamp + parent);
        trackedFiles = new TreeMap<>();
        trackedFiles.put(this.myID, null);
        trackedFiles.remove(this.myID);
    }

    public void mergeLog(String mergeLog) {
        this.mergeLog = mergeLog;
    }

    public String getMyID() {
        return myID;
    }

    public String getLog() {
        String commitID = "===" + "\n" + "commit " + myID + " \n";
        String Date = "Date: " + timestamp + "\n";
        String msg = message + "\n";
        try {
            if (parent.length() > 40) {
                String mergeLog = "Merge: " + parent.substring(0, 7) + " " +
                    parent.substring(40, 47) + " \n";
                return commitID + mergeLog + Date + msg + "\n";
            }
        } catch (NullPointerException e) {
            return commitID + Date + msg + "\n";
        }
        return commitID + Date + msg + "\n";
    }

    public String getMsg() {
        return this.message;
    }

    public TreeMap<String, String> getTrack() {
        return this.trackedFiles;
    }

    public String toString() {
        return message + timestamp + parent;
    }

    public String getParent() {
        if (parent == null) {
            return null;
        }
        return this.parent.substring(0, 40);
    }

    public String getParent2() {
        if (parent == null) {
            return null;
        }
        if (parent.length() > 40) {
            return parent.substring(40, 80);
        }
        return null;
    }
}
