//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Commands implements Serializable {
    private File gitletDirectory;
    private File stagingDirectory;
    private File commitsDirectory;
    private File filesDirectory;
    private File branchDirectory;
    private File addDirectory;
    private File removeDirectory;
    private File currentBranchDirectory;
    private AddArea addArea;
    private RemoveArea removeArea;
    private HashMap<String, String> branchHeads;
    private String currentBranch;

    public Commands() {
    }

    public void init() throws IOException {
        if (!hasGitletRepository()) {
            gitletDirectory = new File(".gitlet");
            gitletDirectory.mkdirs();
            stagingDirectory = Utils.join(gitletDirectory, "stage");
            stagingDirectory.mkdir();
            commitsDirectory = Utils.join(gitletDirectory, "commits");
            commitsDirectory.mkdir();
            filesDirectory = Utils.join(gitletDirectory, "files");
            filesDirectory.mkdir();
            addDirectory = Utils.join(gitletDirectory, "add");
            removeDirectory = Utils.join(gitletDirectory, "remove");
            branchDirectory = Utils.join(gitletDirectory, "branch");
            currentBranchDirectory = Utils.join(gitletDirectory, "current");

            addArea = new AddArea();
            removeArea = new RemoveArea();
            Utils.writeObject(addDirectory, addArea);
            Utils.writeObject(removeDirectory, removeArea);

            currentBranch = "master";
            Utils.writeContents(currentBranchDirectory, currentBranch);
            branchHeads = new HashMap();
            Commit initial = new Commit();
            Utils.writeObject(Utils.join(commitsDirectory, initial.getID()), initial);
            branchHeads.put("master", initial.getID());
            Utils.writeObject(branchDirectory, branchHeads);
        } else {
            System.out.println(" A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
    }

    public void add(String name) throws IOException {
        File file = new File(name);
        AddArea addArea = Utils.readObject(Utils.join(".gitlet", "add"), AddArea.class);
        RemoveArea removeArea = Utils.readObject(Utils.join(".gitlet", "remove"), RemoveArea.class);
        if (!file.exists()) {
            System.out.println("File does not exist");
            System.exit(0);
        } else {
            String fileContents = Utils.readContentsAsString(file);
            String fileID = Utils.sha1(fileContents);
            File stageFile = Utils.join(".gitlet", "stage", name);
            Commit headCommit = getHeadCommit();
            HashMap<String, String> currentFiles = headCommit.getFiles();

            // The head commit contains the same contents
            if (currentFiles != null && currentFiles.get(name) != null && (currentFiles.get(name)).equals(fileID)) {
                clearStage();
                System.exit(0);
            }

            // If the stage file already exists
            if (stageFile.exists()) {
                Utils.writeContents(stageFile, fileContents);
                addArea.add(name, fileID);
            } else { // If it doesn't exist
                Utils.writeContents(stageFile, fileContents);
                addArea.add(name, fileID);
            }

            Utils.writeObject(Utils.join(".gitlet", "add"), addArea);
            Utils.writeObject(Utils.join(".gitlet", "remove"), removeArea);
        }
    }

    public void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else {
            Commit headCommit = getHeadCommit();
            HashMap<String, String> headIDs = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
            String currentBranch = Utils.readContentsAsString(Utils.join(".gitlet", "current"));
            HashMap<String, String> headTrackedFiles = headCommit.getFiles();
            AddArea aa = Utils.readObject(Utils.join(".gitlet", "add"), AddArea.class);
            RemoveArea ra = Utils.readObject(Utils.join(".gitlet", "remove"), RemoveArea.class);
            HashMap<String, String> addFiles = aa.getAddedFiles();
            ArrayList<String> removeFiles = ra.getRemoveFiles();
            if (headTrackedFiles == null) {
                headTrackedFiles = new HashMap();
            }

            if (addFiles.size() == 0 && removeFiles.size() == 0) {
                System.out.println("No changes added to the commit");
                System.exit(0);
            } else {

                // Save added files
                for (String s: addFiles.keySet()) {
                    headTrackedFiles.put(s, addFiles.get(s));
                    File path = Utils.join(".gitlet", "files", addFiles.get(s));
                    File stageVersion = Utils.join(".gitlet", "stage", s);

                    Utils.writeContents(path, Utils.readContentsAsString(stageVersion));
                    stageVersion.delete();
                }

                // Remove Deleted Files
                for (String name: removeFiles) {
                    headTrackedFiles.remove(name);
                }

                Commit newHead = new Commit(message, headTrackedFiles, headCommit.getID());
                File allCommitsFile = Utils.join(Utils.join(".gitlet", "commits"), newHead.getID());
                Utils.writeObject(allCommitsFile, newHead);
                headIDs.put(currentBranch, newHead.getID());
                Utils.writeObject(Utils.join(".gitlet", "branch"), headIDs);
                Utils.writeObject(Utils.join(".gitlet", "add"), new AddArea());
                Utils.writeObject(Utils.join(".gitlet", "remove"), new RemoveArea());
            }
        }
    }

    public void remove(String name) {
        File file = new File(name);
        Commit headCommit = getHeadCommit();
        AddArea aa = Utils.readObject(Utils.join(".gitlet", "add"), AddArea.class);
        RemoveArea ra = Utils.readObject(Utils.join(".gitlet", "remove"), RemoveArea.class);
        HashMap<String, String> addFiles = aa.getAddedFiles();
        if ((addFiles == null || addFiles.size() == 0) && (headCommit.getFiles() == null || headCommit.getFiles().size() == 0)) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        }
        if (!addFiles.containsKey(name) && !headCommit.getFiles().containsKey(name)) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        } else {
            if (addFiles != null && addFiles.size() != 0 && addFiles.containsKey(name)) {
                aa.remove(name);
                File stagedFile = Utils.join(Utils.join(".gitlet", "stage"), name);
                stagedFile.delete();
            }
            if (headCommit.getFiles() != null && headCommit.getFiles().size() != 0 && headCommit.getFiles().containsKey(name)) {
                ra.add(name);
                file.delete();
            }


            Utils.writeObject(Utils.join(".gitlet", "add"), aa);
            Utils.writeObject(Utils.join(".gitlet", "remove"), ra);
        }
    }

    public void log() {
        Commit headCommit = getHeadCommit();
        Commit pointer = headCommit;
        while (pointer.getParentID() != null) {
            System.out.println("===");
            System.out.println(pointer.toString());
            pointer = Utils.readObject(Utils.join(".gitlet", "commits", pointer.getParentID()), Commit.class);
        }
        System.out.println("===");
        System.out.println(pointer.toString());

    }

    public void globalLog() {
        File[] commitList = Utils.join(".gitlet", "commits").listFiles();
        assert commitList != null;
        for (File f: commitList) {
            System.out.println("===");
            Commit c = getCommit(f.getName());
            System.out.println(c.toString());
        }

    }

    public void find(String msg) {
        int count = 0;
        File[] commitList = Utils.join(".gitlet", "commits").listFiles();
        assert commitList != null;
        for (File f: commitList) {
            Commit c = getCommit(f.getName());
            if (c.getMessage().equals(msg)) {
                count += 1;
                System.out.println(c.getID());
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message");
            System.exit(0);
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        HashMap<String, String> branchHeads = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
        ArrayList<String> branchKeys = new ArrayList<String>(branchHeads.keySet());
        String currentBranch = Utils.readContentsAsString(Utils.join(".gitlet", "current"));
        Collections.sort(branchKeys);
        for (String s: branchKeys) {
            if (s.equals(currentBranch)) {
                System.out.println("*" + s);
            } else {
                System.out.println(s);
            }
        }

        AddArea aa = Utils.readObject(Utils.join(".gitlet", "add"), AddArea.class);
        RemoveArea ra = Utils.readObject(Utils.join(".gitlet", "remove"), RemoveArea.class);
        HashMap<String, String> addFiles = aa.getAddedFiles();
        ArrayList<String> removeFiles = ra.getRemoveFiles();
        System.out.println("\n=== Staged Files ===");
        if (addFiles != null && addFiles.size() != 0) {
            List<String> addedKeys = new ArrayList(addFiles.keySet());
            Collections.sort(addedKeys);
            for (String s: addedKeys) {
                System.out.println(s);
            }
        }

        System.out.println("\n=== Removed Files ===");
        if (removeFiles != null && removeFiles.size() != 0) {
            Collections.sort(removeFiles);
            for (String s: removeFiles) {
                System.out.println(s);
            }
        }
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        System.out.println("\n=== Untracked Files ===");
    }

    public void checkoutFile(String name) {
        Commit headCommit = getHeadCommit();
        if (headCommit.getFiles().containsKey(name)) {
            String fID = headCommit.getFiles().get(name);
            File f = Utils.join(Utils.join(".gitlet", "files"), fID);
            if (f.exists()) {
                Utils.writeContents(new File(name), Utils.readContentsAsString(f));
            }

        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    public void checkoutIDFile(String commitID, String name) {
        Commit theOne = getCommitID(commitID);
        if (theOne != null) {
            if (theOne.getFiles().containsKey(name)) {
                String fID = theOne.getFiles().get(name);
                File f = Utils.join(Utils.join(".gitlet", "files"), fID);
                if (f.exists()) {
                    Utils.writeContents(new File(name), Utils.readContentsAsString(f));
                }

            } else {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }
        } else {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
    }

    public void checkoutBranch(String name) {
        HashMap<String, String> branchHeads = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
        ArrayList<String> branches = new ArrayList<String>(branchHeads.keySet());
        String currentBranch = Utils.readContentsAsString(Utils.join(".gitlet", "current"));
        if (!branches.contains(name)) {
            System.out.println("No such branch exists");
            System.exit(0);
        } else if (currentBranch.equals(name)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        } else {
            if (hasUntracked()) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            List<String> CWDfileNames = Utils.plainFilenamesIn(new File(System.getProperty("user.dir")));
            if (CWDfileNames != null && CWDfileNames.size() != 0) {
                // FIXME: ASSUME NO UNTRACKED FILES
                String branchHeadCommitID = branchHeads.get(name);
                Commit bCommit = getCommit(branchHeadCommitID);
                HashMap<String, String> bCommitFiles = bCommit.getFiles();
                Commit currentHeadCommit = getHeadCommit();
                HashMap<String, String> cHeadCommitFiles = currentHeadCommit.getFiles();
                if (bCommitFiles != null && bCommitFiles.size() != 0) {
                    for (String fName: bCommitFiles.keySet()) {
                        // Overwriting the contents or creating a new file in CWD
                        File currentFile = new File(fName);
                        File savedFile = Utils.join(".gitlet", "files", bCommitFiles.get(fName));
                        Utils.writeContents(currentFile, Utils.readContentsAsString(savedFile));
                    }
                }
                // Getting rid of files in CWD tracked by current head commit but not tracked by checked out branch head commit
                if (CWDfileNames != null && CWDfileNames.size() != 0) {
                    for (String cwdName: CWDfileNames) {
                        if (tracked(cwdName) && cHeadCommitFiles.containsKey(cwdName) && (bCommitFiles == null || !bCommitFiles.containsKey(cwdName))) {
                            File currentFile = new File(cwdName);
                            currentFile.delete();
                        }
                    }
                }
            }
            // Clear the Stage & Removal Area
            clearStage();
            // Move the current branch
            Utils.writeContents(Utils.join(".gitlet", "current"), name);

        }
    }

    public void branch(String name) {
        HashMap<String, String> branches = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
        if (branches.containsKey(name)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            branches.put(name, getHeadCommit().getID());
            Utils.writeObject(Utils.join(".gitlet", "branch"), branches);
        }
    }

    public void removeBranch(String name) {
        HashMap<String, String> branches = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
        if (!branches.containsKey(name)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (Utils.readContentsAsString(Utils.join(".gitlet", "current")).equals(name)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        } else {
            branches.remove(name);
            Utils.writeObject(Utils.join(".gitlet", "branch"), branches);
        }
    }

    public void reset(String ID) {
        if (getCommitID(ID) != null) {
            List<String> CWDfileNames = Utils.plainFilenamesIn(new File(System.getProperty("user.dir")));
            if (CWDfileNames != null && CWDfileNames.size() != 0) {
                for (String cwdFileName : CWDfileNames) {
                    if (!tracked(cwdFileName)) {
                        System.out.println("There is an untracked file in the way; delete it or add it first.");
                        System.exit(0);
                    }
                }
            }
            Commit workingCommit = getCommitID(ID);
            HashMap<String, String> workingCommitFiles = workingCommit.getFiles();
            // Checkout all files from commit
            for (String wcfName: workingCommitFiles.keySet()) {
                checkoutIDFile(ID, wcfName);
            }
            // Remove files in CWD tracked by CURRENT head not present in GIVEN commit
            for (String cwdName: CWDfileNames) {
                if (tracked(cwdName) && tracked(cwdName) && !workingCommitFiles.containsKey(cwdName)) {
                    File currentFile = new File(cwdName);
                    currentFile.delete();
                }
            }
            // Move current branch head's pointer to this ID
            File branchPath = Utils.join(".gitlet", "branch");
            HashMap<String, String> branchHeads = getBranchHeads();
            branchHeads.put(getCurrentBranchName(), ID);
            Utils.writeObject(branchPath, branchHeads);


            // Clear the staging area
            clearStage();
        } else {
            System.out.println("No commit with that id exists");
            System.exit(0);
        }
    }

    public void merge(String branchName) throws IOException {
        // Uncommitted changes present--staged additions/removal
        if ((getAddArea().getAddedFiles() == null || getAddArea().getAddedFiles().size() == 0) ||
            getRemoveArea().getRemoveFiles() == null || getRemoveArea().getRemoveFiles().size() == 0) {
            System.out.println("You have uncommited changes.");
            System.exit(0);
        }
        // Branch name doesn't exist
        if (!getBranchHeads().containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        // Branch merging with itself
        if (getCurrentBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself");
            System.exit(0);
        }
        // Untracked file present
        if (hasUntracked()) {
            System.out.println("There is an untracked file in the way; delete it or add it first.");
        }

        // Failure cases have been accounted for
        /**
         *     1. Files modified in GIVEN but not in CURRENT since split is checked out from GIVEN & changes, then ADD changes.
         *     2. Files modified in CURRENT but not in GIVEN since split: do nothing.
         *     3. Files modified samely in GIVEN/CURRENT since split: do nothing.
         *     4. Files present in CURRENT but not GIVEN since split: do nothing.
         *     5. Files not present in CURRENT but in GIVEN since split: checkout & add.
         *     6. Files in CURRENT & unmodified & absent in GIVEN since split: remove.
         *     7. Files not in CURRENT & unmodified in GIVEN since split: do nothing (don't add).
         *     8. Files modified in CURRENT and GIVEN since split in different ways OR <br/>
         *        File modified in either CURRENT or GIVEN but deleted in the other OR <br/>
         *        File not present in CURRENT nor GIVEN at split & have different contents post-split <br/>
         *        THEN: replace contents of file with the message indicated in SPEC & add. Afterward, <br/>
         *        attempt to merge() again.
         */
        Commit currentHead = getHeadCommit();
        HashMap<String, String> currentFiles = currentHead.getFiles();
        Commit givenHead = getCommit(getBranchHeads().get(branchName));
        HashMap<String, String> givenFiles = givenHead.getFiles();
        Commit splitPoint = getSplitPoint(getCurrentBranchName(), branchName);
        HashMap<String, String> splitFiles = splitPoint.getFiles();
        for (String splitFile: splitFiles.keySet()) {
            // Case 1: File inGiven() && inCurrent() && inSplit() && currentFile == splitFile && givenFile != splitFile --> checkout givenFile & stage for addition
            if (containsFile(currentHead, splitFile) && containsFile(givenHead, splitFile) &&
                Utils.sha1(splitFile).equals(currentFiles.get(splitFile)) &&
                    Utils.sha1(splitFile).equals(givenFiles.get(splitFile))) {
                checkoutIDFile(givenHead.getID(), splitFile);
                // FIXME: correctly added??
                add(splitFile);

            }
            // Case 2: File inGiven() && inCurrent() && inSplit() && currentFile != splitFile && givenFile == splitFile --> do nothing
            // Case 3: File inGiven() && inCurrent() && inSplit() && currentFile == splitFile && givenFile == splitFile --> do nothing
                // Case 3.5: File !inGiven() && !inCurrent() && inSplit() --> do nothing
            // Case 6: File atSplit() && inGiven() && givenFile == splitFile && !inCurrent() --> do nothing
            // Case 7: File atSplit() && inCurrent() && currentFile == splitFile && !inGiven() --> Remove & Untrack
            // Case 8a: File inGiven() && inCurrent() && inSplit() && currentFile != splitFile && givenFile != splitFile && givenFile != currentFile
            // Case 8b: File atSplit() && inGiven() && splitFile != givenFile && !inCurrent()
            // Case 8c: File atSplit() && inCurrent() && splitFile != currentFile && !inGiven()
            // Case 8d: File !atSplit() && inCurrent() && inGiven() && currentFile != givenFile
            /** Add the following--> treat deleted File as empty File
             * <<<<<<< HEAD
             * contents of file in current branch
             * =======
             * contents of file in given branch
             * >>>>>>>
             */
            // For all case 8's --> stage file to be added
        }
        // Case 4: File !atSplit() && !inGiven() && inCurrent() --> do nothing
        // Case 5: File !atSplit() && inGiven() && !inCurrent() --> Checkout & Stage givenFile
        for (String givenFile: givenFiles.keySet()) {
            if (containsFile(splitPoint, givenFile) && !containsFile(currentHead, givenFile)) {
                checkoutIDFile(givenHead.getID(), givenFile);
                // FIXME: correctly added??
                add(givenFile);
            }
        }

        // Commit the merge
            // Print: "Encountered a merge conflict" if case 8 is invoked
    }

    public boolean hasGitletRepository() {
        File testDirectory = new File(".gitlet");
        return testDirectory.exists();
    }

    public Commit getCommit(String ID) {
        File f = Utils.join(".gitlet", "commits", ID);
        return Utils.readObject(f, Commit.class);
    }

    public Commit getHeadCommit() {
        String headBranch = getCurrentBranchName();
        HashMap<String, String> branchHeads = getBranchHeads();
        String headCommitID = branchHeads.get(headBranch);
        return getCommit(headCommitID);
    }

    public boolean staged(String fileName) {
        List<String> stageFileNames = Utils.plainFilenamesIn(Utils.join(".gitlet", "stage"));
        for (String s: stageFileNames) {
            if (s.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public boolean tracked(String fileName) {
        Commit headCommit = getHeadCommit();
        HashMap<String, String> headFiles = headCommit.getFiles();
        if (headFiles != null && headFiles.keySet().contains(fileName)) {
            return true;
        }
        return false;
    }

    public boolean hasUntracked() {
        List<String> CWDfileNames = Utils.plainFilenamesIn(new File("."));
        for (String cwdName: CWDfileNames) {
            if (!tracked(cwdName)) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentBranchName() {
        return Utils.readContentsAsString(Utils.join(".gitlet", "current"));
    }


    public HashMap<String, String> getBranchHeads() {
        return Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
    }

    public List<String> getCommitsList() {
        return Utils.plainFilenamesIn(Utils.join(".gitlet", "commits"));
    }

    public void clearStage() {
        Utils.writeObject(Utils.join(".gitlet", "add"), new AddArea());
        Utils.writeObject(Utils.join(".gitlet", "remove"), new RemoveArea());
        List<String> stageFileNames = Utils.plainFilenamesIn(Utils.join(".gitlet", "stage"));
        if (stageFileNames != null && stageFileNames.size() != 0) {
            for (String sName: stageFileNames) {
                File stageFile = Utils.join(".gitlet", "stage", sName);
                stageFile.delete();
            }
        }
    }

    public AddArea getAddArea() {
        return Utils.readObject(Utils.join(".gitlet", "add"), AddArea.class);
    }

    public RemoveArea getRemoveArea() {
        return Utils.readObject(Utils.join(".gitlet", "remove"), RemoveArea.class);
    }


    public Commit getCommitID(String ID) {
        List<String> commitIDs = getCommitsList();
        String fullID = "";
        int total = 0;
        for (String cID: commitIDs) {
            if (cID.substring(0, ID.length()).equals(ID)) {
                fullID = cID;
                total += 1;
            }
        }
        if (total == 1) {
            return getCommit(fullID);
        }
        return null;
    }

    // FIXME
    public Commit getSplitPoint(String currBranch, String givenBranch) {
        // Filler for the time being
        return getCommit(getHeadCommit().getParentID());
    }

    public boolean containsFile(Commit c, String fileName) {
        return c.getFiles().containsKey(fileName);
    }
}
