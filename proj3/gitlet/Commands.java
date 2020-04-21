//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
            throw new GitletException();
        }
    }

    public void add(String name) throws IOException {
        File file = new File(name);
        AddArea addArea = Utils.readObject(Utils.join(".gitlet", "add"), AddArea.class);
        RemoveArea removeArea = Utils.readObject(Utils.join(".gitlet", "remove"), RemoveArea.class);
        if (!file.exists()) {
            System.out.println("File does not exist");
            throw new GitletException();
        } else {
            String fileContents = Utils.readContentsAsString(file);
            String fileID = Utils.sha1(fileContents);
            File stageFile = Utils.join(".gitlet", "stage", name);
            Commit headCommit = getHeadCommit();
            HashMap<String, String> currentFiles = headCommit.getFiles();

            // The head commit contains the same contents
            if (currentFiles != null && currentFiles.get(name) != null && (currentFiles.get(name)).equals(fileID)) {
                addArea.remove(name);
                removeArea.remove(name);
                stageFile.delete();
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
            throw new GitletException();
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
                throw new GitletException();
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
        ArrayList<String> removeFiles = ra.getRemoveFiles();
        if (!addFiles.containsKey(name) && !headCommit.getFiles().containsKey(name)) {
            System.out.println("No reason to remove the file");
            throw new GitletException();
        } else {

            if (addFiles.containsKey(name)) {
                addFiles.remove(name);
                File stagedFile = Utils.join(Utils.join(".gitlet", "stage"), name);
                stagedFile.delete();
            }
            if (headCommit.getFiles().containsKey(name)) {
                removeFiles.add(name);
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
            throw new GitletException();
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
            List<String> removedKeys = List.copyOf(removeFiles);
            Collections.sort(removedKeys);
            for (String s: removedKeys) {
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
            throw new GitletException();
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
                throw new GitletException();
            }
        } else {
            System.out.println("No commit with that id exists.");
            throw new GitletException();
        }
    }

    public void checkoutBranch(String name) {
        HashMap<String, String> branchHeads = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
        ArrayList<String> branches = new ArrayList<String>(branchHeads.keySet());
        String currentBranch = Utils.readContentsAsString(Utils.join(".gitlet", "current"));
        if (!branches.contains(name)) {
            System.out.println("No such branch exists");
            throw new GitletException();
        } else if (currentBranch.equals(name)) {
            System.out.println("No need to checkout the current branch.");
            throw new GitletException();
        } else {
            List<String> CWDfileNames = Utils.plainFilenamesIn(new File(System.getProperty("user.dir")));
            if (CWDfileNames != null && CWDfileNames.size() != 0) {
                for (String cwdFileName: CWDfileNames) {
                    if (!tracked(cwdFileName)) {
                        System.out.println("There is an untracked file in the way; delete it or add it first.");
                        throw new GitletException();
                    }
                }
                // FIXME: ASSUME NO UNTRACKED FILES
                String branchHeadCommitID = branchHeads.get(name);
                Commit bCommit = getCommit(branchHeadCommitID);
                HashMap<String, String> bCommitFiles = bCommit.getFiles();
                Commit currentHeadCommit = getHeadCommit();
                HashMap<String, String> cHeadCommitFiles = currentHeadCommit.getFiles();
                for (String fName: bCommitFiles.keySet()) {
                    // Overwriting the contents or creating a new file in CWD
                    File currentFile = new File(fName);
                    File savedFile = Utils.join(".gitlet", "files", bCommitFiles.get(fName));
                    Utils.writeContents(currentFile, Utils.readContentsAsString(savedFile));
                }
                // Getting rid of files in CWD tracked by current head commit but not tracked by checked out branch head commit
                for (String cwdName: CWDfileNames) {
                    if (tracked(cwdName) && cHeadCommitFiles.containsKey(cwdName) && !bCommitFiles.containsKey(cwdName)) {
                        File currentFile = new File(cwdName);
                        currentFile.delete();
                    }
                }

                // Clear the Stage & Removal Area
                // FIXME: Do I need to clear removed files?
                clearStage();
                // Move the current branch
                Utils.writeContents(Utils.join(".gitlet", "current"), name);
            }


        }
    }

    public void branch(String name) {
        HashMap<String, String> branches = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
        if (branches.containsKey(name)) {
            System.out.println("A branch with that name already exists.");
            throw new GitletException();
        } else {
            branches.put(name, getHeadCommit().getID());
            Utils.writeObject(Utils.join(".gitlet", "branch"), branches);
        }
    }

    public void removeBranch(String name) {
        HashMap<String, String> branches = Utils.readObject(Utils.join(".gitlet", "branch"), HashMap.class);
        if (!branches.containsKey(name)) {
            System.out.println("A branch name with that name does not exist.");
            throw new GitletException();
        } else if (Utils.join(".gitlet", "current").equals(name)) {
            System.out.println("Cannot remove the current branch");
            throw new GitletException();
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
                        throw new GitletException();
                    }
                }
            }
            Commit workingCommit = getCommitID(ID);
            HashMap<String, String> workingCommitFiles = workingCommit.getFiles();
            // Checkout all files from commit
            for (String wcfName: workingCommitFiles.keySet()) {

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
            throw new GitletException();
        }
    }

    public void merge(String branchName) {
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
        if (headFiles.keySet().contains(fileName)) {
            return true;
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
    
}
