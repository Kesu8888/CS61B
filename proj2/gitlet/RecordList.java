package gitlet;
import java.io.Serializable;
import java.util.TreeMap;

public class RecordList implements Serializable {
    private TreeMap<String, String> recordFile;
    private String currentBranch;

    public RecordList() {
        currentBranch = "master";
        recordFile = new TreeMap<>();
        recordFile.put(currentBranch, null);
    }

    public void createNewBranch(String branchName) {
        if (branchExist(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        recordFile.put(branchName, recordFile.get(currentBranch));
    }

    public void removeBranch(String branchName) {
        if (!branchExist(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        recordFile.remove(branchName);
    }

    public String getCurrentBranchName() {
        return currentBranch;
    }

    public void updateHeadCommit(String commitID) {
        recordFile.put(currentBranch, commitID);
    }

    public boolean branchExist(String branchName) {
        return recordFile.containsKey(branchName);
    }

    public String getHeadCommit(String branchName) {
        if (!branchExist(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        return recordFile.get(branchName);
    }

    public String getHeadCommit() {
        return recordFile.get(currentBranch);
    }

    public TreeMap<String, String> getRecordFile() {
        return recordFile;
    }

    public void switchBranch(String branchName) {
        currentBranch = branchName;
    }
}

