package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.LinkedList;

import static gitlet.Utils.*;


// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The Main directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** Shared UID of initial Commit */
    public static String UID = "8302022aba41d8b3d27a6d73c33a284d948cd2a8";
    /** Create a stage for add and for remove folder */
    public static final File stageAdd = join(GITLET_DIR, "stageAdd");
    public static final File stageDel = join(GITLET_DIR, "stageDelete");
    /** Create a tracked file folder */
    public static final File commitFolder = join(GITLET_DIR, "commitFolder");
    public static final File trackFolder = join(GITLET_DIR, "trackFolder");
    public static final File recording = join(GITLET_DIR, "recording");

    // Create a Initial Commit and create a master branch
    public static void init() {
        File IC = join(commitFolder, UID);
        if (IC.exists()) {
            System.out.println("A Gitlet version-control system already " +
                "exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdirs();
        stageAdd.mkdirs();
        stageDel.mkdirs();
        commitFolder.mkdirs();
        trackFolder.mkdirs();
        RecordList record = new RecordList();
        writeRecord(record);

        String initialTime = "Mon Jan 1 00:00:00 1970 -0800";
        Commit initialCommit = new Commit("initial commit", null, initialTime);
        writeCommit(initialCommit);
    }

    public static void add(String fileName) {
        File sourceFile = join(CWD, fileName);
        if (!sourceFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        Commit headCommit = getHeadCommit();
        TreeMap<String, String> trackFiles = headCommit.getTrack();
        if (trackFiles == null) {
            addToStage(fileName);
            return;
        }
        if (trackFiles.containsKey(fileName)) {
            join(stageDel, fileName).delete();
            byte[] contentCopy = Utils.readContents(sourceFile);
            if (Utils.sha1(contentCopy).equals(trackFiles.get(fileName))) {
                join(stageAdd, fileName).delete();
                return;
            }
            addToStage(fileName);
        } else {
            addToStage(fileName);
        }
    }

    public static void Commit(String commitMsg) {
        if (commitMsg.length() == 0) {
            System.out.println("Please enter a commit message.");
            return;
        }
        TreeMap<String, String> trackFiles = committing();
        Commit currentCommit = getHeadCommit();
        Commit newCommit = new Commit(commitMsg, currentCommit.getMyID(), trackFiles);
        writeCommit(newCommit);
    }

    public static void rm(String fileName) {
        List<String> stageAddFolder = plainFilenamesIn(stageAdd);
        TreeMap<String, String> trackFile = getCurrentTrack();
        if (trackFile.containsKey(fileName)) {
            join(stageAdd, fileName).delete();
            join(CWD, fileName).delete();
            byte[] fileContent = readContents(join(trackFolder, trackFile.get(fileName)));
            writeContents(join(stageDel, fileName), fileContent);
        } else if(stageAddFolder.contains(fileName)) {
            join(stageAdd, fileName).delete();
        } else {
            System.out.println("No reason to remove the file.");
        }
    }
    public static void log() {
        if (!join(commitFolder, UID).exists()) {
            return;
        }
        System.out.println(getHeadCommit().getLog());
        for (Commit loop = getHeadCommit(); loop.getParent() != null;) {
            loop = getCommit(loop.getParent());
            System.out.println(loop.getLog());
        }
    }

    public static void global_Log() {
        List<String> commitList = Utils.plainFilenamesIn(commitFolder);
        String global_log = new String();
        for (String commitName : commitList) {
            System.out.println(getCommit(commitName).getLog());
        }
    }

    public static void find(String msg) {
        List<String> commitList = Utils.plainFilenamesIn(commitFolder);
        List<String> sameMsgCommitID = new ArrayList<>();
        for (String commitName : commitList) {
            Commit commit = getCommit(commitName);
            if (msg.equals(commit.getMsg())) {
                sameMsgCommitID.add(commit.getMyID());
                System.out.println(commit.getMyID());
            }
        }
        if (sameMsgCommitID.isEmpty()) {
            System.out.println("Found no commit with that message. ");
        }
    }

    public static void status() {
        //Initialize list
        List<String> branchList = new ArrayList<>();
        List<String> addList = new ArrayList<>();
        addList.add("=== Staged Files ===" + "\n");
        List<String> delList = new ArrayList<>();
        delList.add("=== Removed Files ===" + "\n");
        List<String> MBNS = new ArrayList<>();
        MBNS.add("=== Modifications Not Staged For Commit ===" + "\n");
        List<String> untracked = new ArrayList<>();
        untracked.add("=== Untracked Files ===" + "\n");
        //Fill in branchList
        RecordList record = Utils.readObject(recording, RecordList.class);
        String headBranch = record.getCurrentBranchName();
        String branchTitle = "=== Branches ===" + "\n" + "*" + headBranch + "\n";
        branchList.add(branchTitle);
        for (Map.Entry<String, String> entry : record.getRecordFile().entrySet()) {
            if (!entry.getKey().equals(headBranch)) {
                branchList.add(entry.getKey() + "\n");
            }
        }
        //Fill in stageAddList
        List<String> stageAddFolder = plainFilenamesIn(stageAdd);
        for (String inStageAdd : stageAddFolder) {
            addList.add(inStageAdd + "\n");
            File inStageAddFile = join(stageAdd, inStageAdd);
            MBNS = statusMBNS(MBNS, sha1(readContents(inStageAddFile)), inStageAdd);
        }
        //Fill in stageDel list
        List<String> stageDelFolder = plainFilenamesIn(stageDel);
        for (String inStageDel : stageDelFolder) {
            delList.add(inStageDel + "\n");
        }
        Map<String, String> trackFiles = getCurrentTrack();
        for (Map.Entry<String, String> entry : trackFiles.entrySet()) {
            if (stageAddFolder.contains(entry.getKey()) | stageDelFolder.contains(entry.getKey())) {
                continue;
            }
            MBNS = statusMBNS(MBNS, entry.getValue(), entry.getKey());
        }
        //Fill in untrackList
        List<String> allFilesInCWD = Utils.plainFilenamesIn(CWD);
        for (String s : allFilesInCWD) {
            File file = join(CWD, s);
            if (trackFiles.containsKey(s) | stageAddFolder.contains(s)) {
                continue;
            }
            if (file.isDirectory()) {
                continue;
            }
            untracked.add(s + "\n");
        }
        String statusTxt = "";
        statusTxt = combineString(statusTxt, branchList) + "\n";
        statusTxt = combineString(statusTxt, addList) + "\n";
        statusTxt = combineString(statusTxt, delList) + "\n";
        statusTxt = combineString(statusTxt, MBNS) + "\n";
        statusTxt = combineString(statusTxt, untracked);
        System.out.println(statusTxt);
        //writeContents(join(CWD, "testStatus"), statusTxt);
    }

    public static void checkoutBranch(String branchName) {
        RecordList record = Utils.readObject(recording, RecordList.class);
        TreeMap<String, String> recordFile = record.getRecordFile();
        if (branchName.equals(record.getCurrentBranchName())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        if (!record.getRecordFile().containsKey(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        checkoutCommit(recordFile.get(branchName));
        record.switchBranch(branchName);
        writeRecord(record);
    }

    public static void checkoutFile(String fileName) {
        TreeMap<String, String> currentTrack = getCurrentTrack();
        if (currentTrack.containsKey(fileName)) {
            File inCWD = join(CWD, fileName);
            File putFile = join(trackFolder, currentTrack.get(fileName));
            writeContents(inCWD, readContents(putFile));
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }

    public static void checkoutCommitFile(String commitID, String fileName) {
        TreeMap<String, String> track = getTrack(commitID);
        if (track.containsKey(fileName)) {
            File inCWD = join(CWD, fileName);
            File putFile = join(trackFolder, track.get(fileName));
            writeContents(inCWD, readContents(putFile));
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }
    public static void branch(String branchName) {
        RecordList record = readObject(recording, RecordList.class);
        record.createNewBranch(branchName);
        writeRecord(record);
    }
    public static void rmBranch(String branchName) {
        RecordList record = readObject(recording, RecordList.class);
        record.removeBranch(branchName);
        writeRecord(record);
    }
    public static void reset(String commitID) {
        checkoutCommit(commitID);
        RecordList record = readObject(recording, RecordList.class);
        record.updateHeadCommit(commitID);
        writeRecord(record);
    }

    public static void merge(String givenBranch) {
        RecordList record = readObject(recording, RecordList.class);
        String givenBranchHead = record.getHeadCommit(givenBranch);
        String currentBranchHead = record.getHeadCommit();
        if (givenBranch.equals(record.getCurrentBranchName())) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        String splitPoint = getSplitPoint(currentBranchHead, givenBranchHead);
        if (splitPoint.equals(currentBranchHead)) {
            checkoutBranch(givenBranch);
            return;
        }
        if (splitPoint.equals(givenBranch)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (!stageAreaIsClear()) {
            System.out.println("You have uncommitted changes.");
            return;
        }

        TreeMap<String, String> currentHeadFiles = getCurrentTrack();
        TreeMap<String, String> givenHeadFiles = getTrack(givenBranchHead);
        TreeMap<String, String> splitPointFiles = getTrack(splitPoint);
        List<String> CWDFiles = plainFilenamesIn(CWD);
        for (Map.Entry<String, String> entry : givenHeadFiles.entrySet()) {
            String k = entry.getKey();
            if (!currentHeadFiles.containsKey(k) & CWDFiles.contains(k)) {
                System.out.println("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
                return;
            }
        }

        for (Map.Entry<String, String> entry : currentHeadFiles.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            // Case1: givenBranch has key
            if (givenHeadFiles.containsKey(k)) {
                if (!v.equals(givenHeadFiles.get(k))) {
                    // variant2: current key and given key different, splitpoint has the key
                    if (splitPointFiles.containsKey(k)) {
                        // variant3: splitpoint key same as current key
                        if (v.equals(splitPointFiles.get(k))) {
                            mergeAdd(k, join(trackFolder, givenHeadFiles.get(k)));
                        } else if (givenHeadFiles.get(k).equals(splitPointFiles.get(k))) {
                            // variant4: splitpoint key same as given key;
                        } else {
                            // variant5: splitpoint key different from both current and given key
                            conflictAdd(entry, givenHeadFiles.get(k));
                        }
                    } else {
                        //variant6: splitpoint does not exist
                        conflictAdd(entry, givenHeadFiles.get(k));
                    }
                }
            } else {
                // Case2: given branch does not have key
                // variant1: splitpoint has key
                if (splitPointFiles.containsKey(k)) {
                    //variant2: splitpoint value is same as current value
                    if (v.equals(splitPointFiles.get(k))) {
                        mergeDel(k, v);
                    } else {
                        // variant3: splitpoint value is different from current value
                        conflictAdd(entry, null);
                    }
                }
            }
        }

        for (Map.Entry<String, String> entry : givenHeadFiles.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (currentHeadFiles.containsKey(k)) {
                continue;
            }
            if (splitPointFiles.containsKey(k)) {
                if (!splitPointFiles.get(k).equals(v)) {
                    conflictAdd(entry, null);
                }
            } else {
                mergeAdd(k, join(trackFolder, v));
            }
        }

        rmBranch((givenBranch));
        TreeMap<String, String> trackFiles = committing();
        String commitMSG = "Merge: " + currentBranchHead.substring(0, 6) + " " +
            givenBranchHead.substring(0, 6) + " \n";
        Commit newCommit = new Commit(commitMSG, currentBranchHead +
            givenBranchHead, trackFiles);
        newCommit.mergeLog("Merged "+ givenBranch + " into " + record.getCurrentBranchName() + ".");
        writeCommit(newCommit);
    }



    ///////////////////////
    ///////////////////////
    ///////////////////////
    //Private helper method
    private static void writeCommit(Commit commit) {
        File writeCommit = join(commitFolder, commit.getMyID());
        Utils.writeObject(writeCommit, commit);

        RecordList record = Utils.readObject(recording, RecordList.class);
        record.updateHeadCommit(commit.getMyID());
        writeRecord(record);
    }
    private static void writeRecord(RecordList record) {
        Utils.writeObject(recording, record);
    }
    private static Commit getHeadCommit() {
        RecordList record = Utils.readObject(recording, RecordList.class);
        File headCommitFile = join(commitFolder, record.getHeadCommit());
        return Utils.readObject(headCommitFile, Commit.class);
    }
    private static Commit getCommit(String commitID) {
        File commitFile = join(commitFolder, commitID);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return Utils.readObject(commitFile, Commit.class);
    }
    private static void addToStage(String fileName) {
        File sourceFile = join(CWD, fileName);
        byte[] copyOfContent = Utils.readContents(sourceFile);
        File destinationFile = join(stageAdd, fileName);
        Utils.writeContents(destinationFile, copyOfContent);
    }
    private static TreeMap<String, String> getCurrentTrack() {
        Commit headCommit = getHeadCommit();
        return headCommit.getTrack();
    }
    private static TreeMap<String,String> getTrack(String commitID) {
        Commit branchHeadCommit = getCommit(commitID);
        return branchHeadCommit.getTrack();
    }

    private static void deleteFolder(File file) {
        List<String> fileList = plainFilenamesIn(file);
        for (String s : fileList) {
            File eachFile = join(file, s);
            eachFile.delete();
        }
    }
    private static void checkoutCommit(String commitID) {
        TreeMap<String, String> currentTrack = getCurrentTrack();
        TreeMap<String, String> commitTrack = getTrack(commitID);
        for (Map.Entry<String, String> entry : commitTrack.entrySet()) {
            File trackFileInCWD = join(CWD, entry.getKey());
            if (trackFileInCWD.exists() & !currentTrack.containsKey(entry.getKey())) {
                System.out.println("There is an untracked file in the way; delete it, " +
                    "or add and commit it first.");
                System.exit(0);
            }
        }
        for (Map.Entry<String, String> entry : commitTrack.entrySet()) {
                File cwdFile = join(CWD, entry.getKey());
                File trackFile = join(trackFolder, entry.getValue());
                Utils.writeContents(cwdFile, readContents(trackFile));
        }
        for (Map.Entry<String, String> entry : currentTrack.entrySet()) {
            if (!commitTrack.containsKey(entry.getKey())) {
                join(CWD, entry.getKey()).delete();
            }
        }
        deleteFolder(stageAdd);
        deleteFolder(stageDel);
    }
    private static LinkedList<String> getBranchFamily(String headCommitID) {
        LinkedList<String> branchFamily = new LinkedList<>();
        branchFamily.add(headCommitID);
        for (Commit head = getCommit(headCommitID); head.getParent() != null;) {
            head = readObject(join(commitFolder, head.getParent()), Commit.class);
            branchFamily.addFirst(head.getMyID());
        }
        return branchFamily;
    }

    private static String getSplitPoint(String head1, String head2) {
        LinkedList<String> head1Family = getBranchFamily(head1);
        LinkedList<String> head2Family = getBranchFamily(head2);
        String splitPoint = head2Family.get(0);
        for (int i = 1; i < head1Family.size(); i++) {
            if ((i + 1) > head2Family.size()) {
                return splitPoint;
            }
            if (!head1Family.get(i).equals(head2Family.get(i))) {
                return splitPoint;
            }
            splitPoint = head1Family.get(i);
        }
        return splitPoint;
    }
    private static boolean stageAreaIsClear() {
        List<String> stageAddFolder = plainFilenamesIn(stageAdd);
        List<String> stageDelFolder = plainFilenamesIn(stageDel);
        return stageAddFolder.isEmpty() & stageDelFolder.isEmpty();
    }
    private static String conflictContent(String file1, String file2) {
        File mergeFile1 = join(trackFolder, file1);
        String file1Content = readContentsAsString(mergeFile1);
        String file2Content;
        if (file2 == null) {
            file2Content = "";
        } else {
            File mergeFile2 = join(trackFolder, file2);
            file2Content = readContentsAsString(mergeFile2);
        }
        String mergeContent = "<<<<<<< HEAD" + "\n" +file1Content + "\n" + "=======" + "\n";
        return mergeContent + file2Content + "\n" + ">>>>>>>" + "\n";
    }
    private static void mergeAdd(String fileName, File source) {
        File stageAddDir = join(stageAdd, fileName);
        File CWDDir = join(CWD, fileName);
        writeContents(stageAddDir, readContents(source));
        writeContents(CWDDir, readContents(source));
    }
    private static void mergeDel(String fileName, String sourceFile) {
        File destination = join(stageDel, fileName);
        File source = join(trackFolder, sourceFile);
        writeContents(destination, readContents(source));
    }
    private static void conflictAdd(Map.Entry<String, String> entry, String v2) {
        File conflict = join(CWD, entry.getKey());
        writeContents(conflict, conflictContent(entry.getValue(), v2));
        writeContents(join(stageAdd, entry.getKey()), readContents(conflict));
    }

    private static TreeMap<String, String> committing() {
        List<String> addFolderList = Utils.plainFilenamesIn(stageAdd);
        List<String> delFolderList = Utils.plainFilenamesIn(stageDel);
        if (addFolderList.isEmpty() & delFolderList.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Commit currentCommit = getHeadCommit();
        TreeMap<String, String> trackFiles = currentCommit.getTrack();
        if (!addFolderList.isEmpty()) {
            for (String s : addFolderList) {
                File addFile = join(stageAdd, s);
                byte[] addFileContent = Utils.readContents(addFile);
                String addFileSha1 = Utils.sha1(addFileContent);
                addFile.delete();

                trackFiles.put(s, addFileSha1);
                File putInTrack = join(trackFolder, addFileSha1);
                Utils.writeContents(putInTrack, addFileContent);
            }
        }

        if (!delFolderList.isEmpty()) {
            for (String s : delFolderList) {
                trackFiles.remove(s);

                File delFile = join(stageDel, s);
                delFile.delete();
            }
        }
        return trackFiles;
    }
    private static List<String> statusMBNS(List<String> MBNS, String content, String fileName) {
        File inCWD = join(CWD, fileName);
        if (inCWD.exists()) {
            byte[] inCWDContent = readContents(inCWD);
            if (!content.equals(sha1(inCWDContent))) {
                MBNS.add(fileName + " (modified)" + "\n");
            }
        } else {
            MBNS.add(fileName + " (deleted)" + "\n");
        }
        return MBNS;
    }
    private static String combineString(String s, List<String> stringList) {
        for (String S : stringList) {
            s = s + S;
        }
        return s;
    }
    public static void initializeCheck() {
        File IC = join(commitFolder, UID);
        if (!IC.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
