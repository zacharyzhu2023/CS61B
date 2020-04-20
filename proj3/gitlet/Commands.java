package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.util.*;

public class Commands implements Serializable {

    // Instance/Static variables
    private File gitletDirectory; // Contains all the subdirectories
    private File stagingDirectory; // Stores files staged for addition
    private File commitsDirectory; // Stores all commitID --> Commits
    private File filesDirectory; // Stores the fileID --> contents
    private File branchDirectory; // Stores branch names--> headCommit
    private File addDirectory; // Stores AddArea object
    private File removeDirectory; // Stores RemoveArea object
    private File currentBranchDirectory; // Tracks the current branch
    private AddArea addArea;
    private RemoveArea removeArea;
    private ArrayList<String> branchNames; // Name of branches
    private File allBranchNames;
    private String currentBranch;

    // Init method
    public void init() {
        if (!hasGitletRepository()) {
            // Making directories
            gitletDirectory = new File(".gitlet");
            gitletDirectory.mkdir();
            stagingDirectory = Utils.join(gitletDirectory, "stage");
            stagingDirectory.mkdir();
            commitsDirectory = Utils.join(gitletDirectory, "commits");
            commitsDirectory.mkdir();
            filesDirectory = Utils.join(gitletDirectory, "files");
            filesDirectory.mkdir();
            addDirectory = Utils.join(gitletDirectory, "add");
            addDirectory.mkdir();
            removeDirectory = Utils.join(gitletDirectory, "remove");
            removeDirectory.mkdir();
            currentBranchDirectory = Utils.join(gitletDirectory, "current");
            currentBranchDirectory.mkdir();
            allBranchNames = Utils.join(gitletDirectory, "branch names");
            allBranchNames.mkdir();
            

            // Make Adding and Removing Area
            addArea = new AddArea();
            removeArea = new RemoveArea();
            Utils.writeObject(addDirectory, addArea);
            Utils.writeObject(removeDirectory, removeArea);

            // Making Master Branch + Initial Commit

            currentBranch = "master";
            branchNames = new ArrayList<String>();
            branchNames.add("master");
            Utils.writeObject(allBranchNames, branchNames); // Saving ArrayList of Branch Names
            Utils.writeObject(currentBranchDirectory, currentBranch); // Create a current branch
            Commit initial = new Commit();
            Utils.writeObject(commitsDirectory, initial); // Add Commit to ALL commits
            Utils.join(commitsDirectory, currentBranch).mkdir();
            Utils.writeObject(Utils.join(commitsDirectory, currentBranch), initial); // Add Commit to current branch commits


        } else {
            System.out.println(" A Gitlet version-control system already exists in the current directory.");
            throw new GitletException();
        }
    }

    // Add method
    public void add(String name) throws IOException {
        File file = new File(name);
        AddArea addArea = Utils.readObject(addDirectory, AddArea.class);
        RemoveArea removeArea = Utils.readObject(removeDirectory, RemoveArea.class);
        if (!file.exists()) {
            System.out.println("File does not exist");
            throw new GitletException();
        } else {
            String fileContents = Utils.readContentsAsString(file);
            String fileID = Utils.sha1(fileContents);
            File stageFile = Utils.join(stagingDirectory, fileID);

            Commit headCommit = getHeadCommit();
            HashMap<String, String> currentFiles = headCommit.getFiles();
            // Current commit contains file
            if (currentFiles.containsKey(name)) {
                // The file is staged for addition
                if (stageFile.exists()) {
                    // Identical contents
                    if (currentFiles.get(name).equals(fileID)) {
                        addArea.remove(name);
                        Utils.restrictedDelete(stageFile);
                    } else { // Nonidentical contents
                        addArea.remove(name);
                        Utils.restrictedDelete(stageFile);
                        File newStageFile = Utils.join(stagingDirectory, fileID); //stagingDirectory/ID
                        Utils.writeContents(newStageFile, fileContents); // staging/ID --> contents
                        addArea.add(name, fileID);
                    }
                } else { // The file was not previously staged
                    Utils.writeContents(stageFile, fileContents);
                    addArea.add(name, fileID);
                }
            }
            Utils.writeObject(addDirectory, addArea);
            Utils.writeObject(removeDirectory, removeArea);
        }
    }

    // FIXME: Where do the files actually get saved?
    public void commit(String message) throws IOException {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            throw new GitletException();
        }

        Commit headCommit = getHeadCommit();
        String currentBranch = Utils.readContentsAsString(currentBranchDirectory);
        HashMap<String, String> headTrackedFiles = headCommit.getFiles();
        AddArea aa = Utils.readObject(addDirectory, AddArea.class);
        RemoveArea ra = Utils.readObject(removeDirectory, RemoveArea.class);
        HashMap<String, String> addFiles = aa.getAddedFiles();
        ArrayList<String> removeFiles = ra.getRemoveFiles();

        if (addFiles.size() == 0 && removeFiles.size() == 0) {
            System.out.println("No changes added to the commit");
            throw new GitletException();
        }

        // Saving the staged for addition files
        for (String s: addFiles.keySet()) {
            headTrackedFiles.put(s, stagedFiles.get(s));
            File f = Utils.join(filesDirectory, stagedFiles.get(s)); // filesDirectory/stagedFileID
            File stageVersion = Utils.join(stagingDirectory, stagedFiles.get(s)); // StagingDirectory/fileID
            Utils.writeContents(f, Utils.readContentsAsString(stageVersion)); // Put staged version in file directory
        }
        // Getting rid of the staged for removal files
        for (String s: removeFiles) {
            headTrackedFiles.remove(s);
        }

        // Tracking new Commit
        Commit newHead = new Commit(message, headTrackedFiles, headCommit.getID()); // Create new Commit
        File allCommitsFile = Utils.join(commitsDirectory, newHead.getID());
        Utils.writeObject(allCommitsFile, newHead); // Adding to all commits
        File branchHeadFile = Utils.join(branchDirectory, currentBranch);
        Utils.writeObject(branchHeadFile, newHead); // Adding as current Branch Head


        // Clearing staged for addition/removal
        Utils.writeObject(addDirectory, new AddArea());
        Utils.writeObject(removeDirectory, new RemoveArea());
    }

    public void remove(String name) {
        File file = new File(name);
        Commit headCommit = getHeadCommit();
        HashMap<String, String> headTrackedFiles = headCommit.getFiles();
        AddArea aa = Utils.readObject(addDirectory, AddArea.class);
        RemoveArea ra = Utils.readObject(removeDirectory, RemoveArea.class);
        HashMap<String, String> addFiles = aa.getAddedFiles();
        ArrayList<String> removeFiles = ra.getRemoveFiles();
        if (!addFiles.containsKey(name) && !headCommit.getFiles().containsKey(name)) {
            System.out.println("No reason to remove the file");
            throw new GitletException();
        }
        if (addFiles.containsKey(name)) {
            addFiles.remove(name);
            File stagedFile = Utils.join(stagingDirectory, name);
            Utils.restrictedDelete(stagedFile);
        }
        if (headCommit.getFiles().containsKey(name)) {
            removeFiles.add(name);
            Utils.restrictedDelete(file);
        }
        Utils.writeObject(addDirectory, addArea);
        Utils.writeObject(removeDirectory, removeArea);

    }
    // FIXME: Adjust for merge commits
    public void log() {
        Commit headCommit = getHeadCommit();
        Commit pointer = headCommit;
        while (pointer != null) {
            System.out.println("===");
            System.out.println(pointer.toString());
            String parentID = pointer.getParentID();
            File parentFile = Utils.join(commitsDirectory, parentID);
            pointer = Utils.readObject(parentFile, Commit.class);
        }
    }

    // FIXME: Adjust for merge commits
    public void globalLog() {
        File[] logFiles = commitsDirectory.listFiles();
        for (File f: logFiles) {
            Commit c = Utils.readObject(f, Commit.class);
            System.out.println("===");
            System.out.println(c.toString());
        }
    }

    public void find(String msg) {
        int count = 0;
        File[] logFiles = commitsDirectory.listFiles();
        for (File f: logFiles) {
            Commit c = Utils.readObject(f, Commit.class);
            if (c.getMessage().equals(msg)) {
                System.out.println(c.getID());
                count += 1;
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message");
            throw new GitletException();
        }
    }

    // FIXME: Adjust for the revolucion
    public void status() {

        System.out.println("=== Branches ===");
        ArrayList<String> branchKeys = Utils.readObject(allBranchNames, ArrayList.class);
        Collections.sort(branchKeys);
        for (String s: branchKeys) {
            if (s.equals(currentBranch)) {
                System.out.println("*" + s);
            } else {
                System.out.println(s);
            }
        }
        AddArea aa = Utils.readObject(addDirectory, AddArea.class);
        RemoveArea ra = Utils.readObject(removeDirectory, RemoveArea.class);
        HashMap<String, String> addFiles = aa.getAddedFiles();
        ArrayList<String> removeFiles = ra.getRemoveFiles();
        System.out.println("\n=== Staged Files ===");
        List<String> addedKeys = new ArrayList<String>(addFiles.keySet());
        Collections.sort(addedKeys);
        for (String s: addedKeys) {
            System.out.println(s);
        }
        List<String> removedKeys = List.copyOf(removeFiles);
        Collections.sort(removedKeys);
        System.out.println("\n=== Removed Files ===");
        for (String s: removedKeys) {
            System.out.println(s);
        }
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("\n=== Untracked Files ===");
        System.out.println();
    }

    public void checkoutFile(String name) {
        Commit headCommit = getHeadCommit();
        if (headCommit.getFiles().containsKey(name)) {
            String fID = headCommit.getFiles().get(name);
            File f = Utils.join(filesDirectory, fID);
            if (f.exists()) {
                Utils.writeContents(new File(name), Utils.readContentsAsString(f));
            }
        } else {
            System.out.println("File does not exist in that commit.");
            throw new GitletException();
        }
    }

    public void checkoutIDFile(String commitID, String name) {
        Commit theOne = null;
        File[] logFiles = commitsDirectory.listFiles();
        for (File f: logFiles) {
            Commit c = Utils.readObject(f, Commit.class);
            if (c.getID().equals(commitID)) {
                theOne = c;
            }
        }
        if (theOne != null) {
            if (theOne.getFiles().containsKey(name)) {
                String fID = theOne.getFiles().get(name);
                File f = Utils.join(filesDirectory, fID);
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

    public void checkoutBranch(String name) throws IOException {
        ArrayList<String> branches = Utils.readObject(allBranchNames, ArrayList.class);
        if (!branches.contains(name)) {
            System.out.println("No such branch exists");
            throw new GitletException();
        } else if (currentBranch.equals(name)) {
            System.out.println("No need to checkout the current branch.");
            throw new GitletException();
        } else {
            // FIXME: CHECK TO SEE IF THERE IS AN UNTRACKED FILE

            // Assuming there's no untracked file
            Commit branchHead = Utils.readObject(branchDirectory, )
            HashMap<String, String> newFiles = branchHead.getFiles();
            for (String f: newFiles.keySet()) {
                // FIXME: Obtain the proper file
                File file = new File(f);
                if (file.exists()) {
                    Utils.writeContents(file, newFiles.get(f));
                } else {
                    file.createNewFile();
                }
            }
            currentBranch = name;
            stage.getRemovedFiles().clear();
            stage.getAddedFiles().clear();
            Utils.writeObject(stagingDirectory, stage);
        }
    }

    public boolean checkTracked(String name) {
        Commit commitHead = getHeadCommit();
        Set<String> allFileNames = commitHead.getFiles().keySet();

    }
    public void branch(String name) {
        if (branches.containsKey(name)) {
            System.out.println("A branch with that name already exists.");
            throw new GitletException();
        } else {
            branches.put(name, branches.get(currentBranch));
            Utils.join(commitsDirectory, name).mkdir();
        }
    }

    public void removeBranch(String name) {
        if (!branches.containsKey(name)) {
            System.out.println("A branch name with that name does not exist.");
            throw new GitletException();
        } else if (currentBranch.equals(name)) {
            System.out.println("Cannot remove the current branch");
            throw new GitletException();
        } else {
            branches.remove(name);
            Utils.restrictedDelete(Utils.join(commitsDirectory, name));
        }
    }

    public void reset(String ID) {

    }

    public void merge(String branchName) {

    }

    public boolean hasGitletRepository() {
        File testDirectory = new File(".gitlet");
        if (testDirectory.exists()) {
            return true;
        }
        return false;
    }

    public Commit getCommit(String ID) {
        File f = Utils.join(commitsDirectory, ID);
        return Utils.readObject(f, Commit.class);
    }

    public Commit getHeadCommit() {
        String headBranch = Utils.readObject(currentBranchDirectory, String.class);
        HashMap<String, String> branchHeads = Utils.readObject(branchDirectory, HashMap.class);
        String headCommitID = branchHeads.get(headBranch);
        return getCommit(headCommitID);
    }


}

