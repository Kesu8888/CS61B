package gitlet;
import java.io.File;
import java.util.*;
import static gitlet.Utils.*;

public class Repository1 extends Repository{
    private static HashMap<String, File> remoteInfo = new HashMap<>(0);

    public static void addRemote(String remoteName, String remoteLocation) {
        if (remoteInfo.containsKey(remoteName)) {
            System.out.println("A remote with that name already exists.");
            return;
        }
        File remoteDirLocation = new File(locationConversion(remoteLocation));
        remoteInfo.put(remoteName, remoteDirLocation);
    }
    public static void rmRemote(String remoteName) {
        if (!remoteInfo.containsKey(remoteName)) {
            System.out.println("A remote with that name does not exist.");
            return;
        }
        remoteInfo.remove(remoteName);
    }
    public static void push(String remoteName, String remoteBranchName) {
        if (!remoteInfo.containsKey(remoteName)) {
            System.out.println("A remote with that name does not exist.");
        }
        File remoteDir = remoteInfo.get(remoteName);
        if (!remoteDir.exists()) {
            System.out.println("Remote directory not found.");
            return;
        }
        File remoteRecordFile = join(remoteDir, "recording");
        RecordList remoteRecord = readObject(remoteRecordFile, RecordList.class);
        Commit localHead = getHeadCommit();
        String localHeadID = localHead.getMyID();
        Graph localHeadFamily = getBF(localHeadID, new Graph(), commitFolder);
        if (!remoteRecord.branchExist(remoteBranchName)) {
            syncBranch(localHeadFamily, trackFolder, remoteDir);
            remoteRecord.createNewBranch(remoteBranchName, localHeadID);
            writeObject(remoteRecordFile, remoteRecord);
        } else {
            if (!localHeadFamily.contains(remoteRecord.getHeadCommit(remoteBranchName))) {
                System.out.println("Please pull down remote changes before pushing.");
                return;
            }
            syncBranch(localHeadFamily, trackFolder, remoteDir);
            remoteRecord.updateHeadCommit(remoteBranchName, localHeadID);
            writeObject(remoteRecordFile, remoteRecord);
        }
    }
    public static void fetch(String remoteName, String remoteBranchName) {
        if (!remoteInfo.containsKey(remoteName)) {
            System.out.println("That remote does not have that branch.");
            return;
        }
        // Get the record File
        File remoteDir = remoteInfo.get(remoteName)
        RecordList remoteRecord = readObject(join(remoteDir, "recording"), RecordList.class);
        // Loop all commits in the remoteBranch.
        Graph remoteFamily = getBF(remoteRecord.getHeadCommit(remoteBranchName), new Graph(), remoteDir);
        syncBranch(remoteFamily, remoteDir, CWD);
        //get the local recordFile and put the new Branch in the local record File;
        RecordList record = readObject(recording, RecordList.class);
        record.createNewBranch(remoteName + "/" + remoteBranchName, remoteFamily.getVertex());
        writeRecord(record);
    }
    public static void pull(String remoteName, String remoteBranchName) {
        fetch(remoteName, remoteBranchName);
        merge(remoteName + "/" + remoteBranchName);
    }

    ///////////////////////////////////////////
    ///////////////////////////////////////////
    ///////////////////////////////////////////
    // Helper method
    private static Graph getBF(String commitID, Graph g, File commitFolder) {
        if (g.contains(commitID)) {
            return g;
        }
        Commit commit = getC(commitID, commitFolder);
        g.addPoint(commitID, commit.getParent(), commit.getParent2());
        if (commit.getParent() != null) {
            g = getBF(commit.getParent(), g, commitFolder);
        }
        if (commit.getParent2() != null) {
            g = getBF(commit.getParent2(), g, commitFolder);
        }
        return g;
    }
    private static String locationConversion(String remoteLocation) {
        char[] locationChars= remoteLocation.toCharArray();
        for (int i = 0; i < locationChars.length; i++) {
            if (locationChars[i] == '/') {
                locationChars[i] = File.separatorChar;
            }
        }
        return locationChars.toString();
    }
    private static Commit getC(String commitID, File remoteDir) {
        File commitFile = join(remoteDir, "commitFolder", commitID);
        return Utils.readObject(commitFile, Commit.class);
    }
    private static void syncBranch(Graph sourceBranchFamily, File sourceDir, File targetDir) {
        List<String> targetTracks = plainFilenamesIn(join(targetDir, "trackFolder"));
        List<String> targetCommits = plainFilenamesIn(join(targetDir, "commitFolder"));
        for (String commitName : sourceBranchFamily) {
            Commit commit = readObject(join(getCommitFolder(sourceDir), commitName), Commit.class);
            if (targetCommits.contains(commitName)) {
                continue;
            }
            TreeMap<String, String> commitTrack = commit.getTrack();
            //Loop each file tracked in the remote commit.
            for (Map.Entry<String, String> entry : commitTrack.entrySet()) {
                if (targetTracks.contains(entry.getValue())) {
                    continue;
                }
                byte[] trackContent = readContents(join(getTrackFolder(sourceDir), entry.getValue()));
                writeContents(join(getTrackFolder(targetDir), entry.getValue()), trackContent);
            }
            writeObject(join(getCommitFolder(targetDir), commitName), commit);
        }
    }
    private static File getCommitFolder(File Dir) {
        return join(Dir, "commitFolder");
    }
    private static File getTrackFolder(File Dir) {
        return join(Dir, "trackFolder");
    }
}
