package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/** Class that contains commands for main.
 *  @author Zachary Zhu
 */

public class Commands implements Serializable {

    /** Accessor to helper methods. **/
    private HelperMethods help = new HelperMethods();

    /**
     * Accessor method for the help variable.
     * @return help variable.
     */
    public HelperMethods getHelperMethod() {
        return help;
    }
    /**
     * Method that creates a gitlet directory.
     * @throws IOException
     */
    public void init() throws IOException {
        if (!help.hasGitletRepository()) {
            File gitletDirectory = new File(".gitlet");
            gitletDirectory.mkdir();
            Utils.join(gitletDirectory, "stage").mkdir();
            Utils.join(gitletDirectory, "commits").mkdir();
            Utils.join(gitletDirectory, "files").mkdir();

            AddArea addArea = new AddArea();
            RemoveArea removeArea = new RemoveArea();
            Utils.writeObject(Utils.join(gitletDirectory,
                    "add"), addArea);
            Utils.writeObject(Utils.join(gitletDirectory, "remove"),
                    removeArea);

            Utils.writeContents(Utils.join(gitletDirectory, "current"),
                    "master");
            HashMap<String, String> branchHeads = new HashMap();
            Commit initial = new Commit();
            Utils.writeObject(Utils.join(".gitlet", "commits",
                    initial.getID()), initial);
            branchHeads.put("master", initial.getID());
            Utils.writeObject(Utils.join(gitletDirectory, "branch"),
                    branchHeads);
        } else {
            System.out.println(" A Gitlet version-control system already "
                    + "exists in the current directory.");
            System.exit(0);
        }
    }

    /**
     * Method that adds a file.
     * @param name Name of file
     * @throws IOException
     */
    public void add(String name) throws IOException {
        File file = new File(name);
        AddArea addArea = Utils.readObject(Utils.join(".gitlet", "add"),
                AddArea.class);
        RemoveArea removeArea = Utils.readObject(Utils.join(".gitlet",
                "remove"), RemoveArea.class);
        if (!file.exists()) {
            System.out.println("File does not exist");
            System.exit(0);
        } else {
            String fileContents = Utils.readContentsAsString(file);
            String fileID = Utils.sha1(fileContents);
            File stageFile = Utils.join(".gitlet", "stage", name);
            Commit headCommit = help.getHeadCommit();
            HashMap<String, String> currentFiles = headCommit.getFiles();
            if (currentFiles != null && currentFiles.get(name) != null
                    && (currentFiles.get(name)).equals(fileID)) {
                help.clearStage();
                System.exit(0);
            }
            if (stageFile.exists()) {
                Utils.writeContents(stageFile, fileContents);
                addArea.add(name, fileID);
            } else {
                Utils.writeContents(stageFile, fileContents);
                addArea.add(name, fileID);
            }
            Utils.writeObject(Utils.join(".gitlet", "add"), addArea);
            Utils.writeObject(Utils.join(".gitlet", "remove"), removeArea);
        }
    }

    /**
     * Method that generates a commit from a message.
     * @param message String message
     */
    public void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else {
            Commit headCommit = help.getHeadCommit();
            HashMap<String, String> headIDs = Utils.readObject(
                    Utils.join(".gitlet", "branch"), HashMap.class);
            String currentBranch = Utils.readContentsAsString(Utils.join(
                    ".gitlet", "current"));
            HashMap<String, String> headTrackedFiles = headCommit.getFiles();
            AddArea aa = Utils.readObject(
                    Utils.join(".gitlet", "add"), AddArea.class);
            RemoveArea ra = Utils.readObject(
                    Utils.join(".gitlet", "remove"), RemoveArea.class);
            HashMap<String, String> addFiles = aa.getAddedFiles();
            ArrayList<String> removeFiles = ra.getRemoveFiles();
            if (headTrackedFiles == null) {
                headTrackedFiles = new HashMap();
            }
            if (addFiles.size() == 0 && removeFiles.size() == 0) {
                System.out.println("No changes added to the commit");
                System.exit(0);
            } else {
                for (String s: addFiles.keySet()) {
                    headTrackedFiles.put(s, addFiles.get(s));
                    File path = Utils.join(".gitlet", "files", addFiles.get(s));
                    File stageVersion = Utils.join(".gitlet", "stage", s);

                    Utils.writeContents(path,
                            Utils.readContentsAsString(stageVersion));
                    stageVersion.delete();
                }
                for (String name: removeFiles) {
                    headTrackedFiles.remove(name);
                }
                Commit newHead = new Commit(message,
                        headTrackedFiles, headCommit.getID());
                File allCommitsFile = Utils.join(
                        Utils.join(".gitlet", "commits"),
                        newHead.getID());
                Utils.writeObject(allCommitsFile, newHead);
                headIDs.put(currentBranch, newHead.getID());
                Utils.writeObject(Utils.join(".gitlet",
                        "branch"), headIDs);
                Utils.writeObject(Utils.join(".gitlet", "add"),
                        new AddArea());
                Utils.writeObject(Utils.join(".gitlet", "remove"),
                        new RemoveArea());
            }
        }
    }

    /**
     * Method removes a file.
     * @param name Name of file
     */
    public void remove(String name) {
        File file = new File(name);
        Commit headCommit = help.getHeadCommit();
        AddArea aa = help.getAddArea();
        RemoveArea ra = help.getRemoveArea();
        HashMap<String, String> addFiles = aa.getAddedFiles();
        if ((addFiles == null || addFiles.size() == 0)
                && (headCommit.getFiles() == null
                || headCommit.getFiles().size() == 0)) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        }
        if (!addFiles.containsKey(name)
                && !headCommit.getFiles().containsKey(name)) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        } else {
            if (addFiles != null && addFiles.size() != 0
                    && addFiles.containsKey(name)) {
                aa.remove(name);
                File stagedFile = Utils.join(
                        Utils.join(".gitlet", "stage"), name);
                stagedFile.delete();
            }
            if (headCommit.getFiles() != null
                    && headCommit.getFiles().size() != 0
                    && headCommit.getFiles().containsKey(name)) {
                ra.add(name);
                file.delete();
            }


            Utils.writeObject(Utils.join(".gitlet", "add"), aa);
            Utils.writeObject(Utils.join(".gitlet", "remove"), ra);
        }
    }

    /**
     * Method prints out log of commits for given head.
     */
    public void log() {
        Commit headCommit = help.getHeadCommit();
        Commit pointer = headCommit;
        while (pointer.getParentID() != null) {
            System.out.println("===");
            System.out.println(pointer.toString());
            pointer = Utils.readObject(Utils.join(".gitlet", "commits",
                    pointer.getParentID()), Commit.class);
        }
        System.out.println("===");
        System.out.println(pointer.toString());

    }

    /**
     * Method prints out all commits.
     */
    public void globalLog() {
        File[] commitList = Utils.join(".gitlet",
                "commits").listFiles();
        assert commitList != null;
        for (File f: commitList) {
            System.out.println("===");
            Commit c = help.getCommit(f.getName());
            System.out.println(c.toString());
        }

    }

    /**
     * Method looks for all commits with a given message.
     * @param msg String message
     */
    public void find(String msg) {
        int count = 0;
        File[] commitList = Utils.join(".gitlet",
                "commits").listFiles();
        assert commitList != null;
        for (File f: commitList) {
            Commit c = help.getCommit(f.getName());
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

    /**
     * Method prints out status.
     */
    public void status() {
        System.out.println("=== Branches ===");
        HashMap<String, String> branchHeads = help.getBranchHeads();
        ArrayList<String> branchKeys =
                new ArrayList<String>(branchHeads.keySet());
        String currentBranch = Utils.readContentsAsString(
                Utils.join(".gitlet", "current"));
        Collections.sort(branchKeys);
        for (String s: branchKeys) {
            if (s.equals(currentBranch)) {
                System.out.println("*" + s);
            } else {
                System.out.println(s);
            }
        }
        AddArea aa = help.getAddArea();
        RemoveArea ra = help.getRemoveArea();
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
        List<String> mnsFiles = help.modifiedNotStaged();
        Collections.sort(mnsFiles);
        for (String s: mnsFiles) {
            System.out.println(s);
        }
        System.out.println("\n=== Untracked Files ===");
        List<String> untrackedFiles = help.untrackedFiles();
        Collections.sort(untrackedFiles);
        for (String s: untrackedFiles) {
            System.out.println(s);
        }
    }

    /**
     * Method that checks out file given name.
     * @param name name of file
     */
    public void checkoutFile(String name) {
        Commit headCommit = help.getHeadCommit();
        if (headCommit.getFiles().containsKey(name)) {
            String fID = headCommit.getFiles().get(name);
            File f = Utils.join(Utils.join(".gitlet",
                    "files"), fID);
            if (f.exists()) {
                Utils.writeContents(new File(name),
                        Utils.readContentsAsString(f));
            }

        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    /**
     * Method that checks out file based on ID/name.
     * @param commitID ID of commit
     * @param name name of file
     */
    public void checkoutIDFile(String commitID, String name) {
        Commit theOne = help.getCommitID(commitID);
        if (theOne != null) {
            if (theOne.getFiles().containsKey(name)) {
                String fID = theOne.getFiles().get(name);
                File f = Utils.join(Utils.join(
                        ".gitlet", "files"), fID);
                if (f.exists()) {
                    Utils.writeContents(new File(name),
                            Utils.readContentsAsString(f));
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

    /**
     * Method that checks out branch.
     * @param name Branch name
     */
    public void checkoutBranch(String name) {
        HashMap<String, String> branchHeads = help.getBranchHeads();
        ArrayList<String> branches = new
                ArrayList<String>(branchHeads.keySet());
        String currentBranch = help.getCurrentBranchName();
        if (!branches.contains(name)) {
            System.out.println("No such branch exists");
            System.exit(0);
        } else if (currentBranch.equals(name)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        } else {
            List<String> cwdFileNames = help.getCWDFiles();
            String branchHeadCommitID = branchHeads.get(name);
            Commit bCommit = help.getCommit(branchHeadCommitID);
            HashMap<String, String> bCommitFiles = bCommit.getFiles();
            Commit currentHeadCommit = help.getHeadCommit();
            HashMap<String, String> cHeadCommitFiles =
                    currentHeadCommit.getFiles();
            if (cwdFileNames != null) {
                for (String cwdName: cwdFileNames) {
                    if (!help.tracked(cwdName)
                            && bCommitFiles.containsKey(cwdName)) {
                        System.out.println("There is an untracked file in "
                                + "the way; delete it, "
                                + "or add and commit it first.");
                        System.exit(0);
                    }
                }
                if (bCommitFiles != null) {
                    for (String fName: bCommitFiles.keySet()) {
                        File currentFile = new File(fName);
                        File savedFile = Utils.join(".gitlet", "files",
                                bCommitFiles.get(fName));
                        Utils.writeContents(currentFile,
                                Utils.readContentsAsString(savedFile));
                    }
                }
                for (String cwdName: cwdFileNames) {
                    if (help.tracked(cwdName)
                            && cHeadCommitFiles.containsKey(cwdName)
                            && (bCommitFiles == null
                            || !bCommitFiles.containsKey(cwdName))) {
                        File currentFile = new File(cwdName);
                        currentFile.delete();
                    }
                }
            }
            help.clearStage();
            Utils.writeContents(
                    Utils.join(".gitlet", "current"), name);

        }
    }

    /**
     * Method that creates a branch name.
     * @param name of the branch
     */
    public void branch(String name) {
        HashMap<String, String> branches = help.getBranchHeads();
        if (branches.containsKey(name)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            branches.put(name, help.getHeadCommit().getID());
            Utils.writeObject(Utils.join(".gitlet", "branch"), branches);
        }
    }

    /**
     * Function that removes reference to a branch name.
     * @param name of the branch
     */
    public void removeBranch(String name) {
        HashMap<String, String> branches = help.getBranchHeads();
        if (!branches.containsKey(name)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (Utils.readContentsAsString(
                Utils.join(".gitlet", "current")).equals(name)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        } else {
            branches.remove(name);
            Utils.writeObject(
                    Utils.join(".gitlet", "branch"), branches);
        }
    }

    /**
     * Reset files given an ID.
     * @param stringID of a branch
     */
    public void reset(String stringID) {
        if (help.getCommitID(stringID) != null) {
            List<String> cwdFileNames = help.getCWDFiles();
            Commit workingCommit = help.getCommitID(stringID);
            HashMap<String, String> workingCommitFiles =
                    workingCommit.getFiles();
            for (String wcfName: workingCommitFiles.keySet()) {
                checkoutIDFile(stringID, wcfName);
            }
            for (String cwdName: cwdFileNames) {
                if (help.tracked(cwdName) && help.tracked(cwdName)
                        && !workingCommitFiles.containsKey(cwdName)) {
                    File currentFile = new File(cwdName);
                    currentFile.delete();
                }
                if (!help.tracked(cwdName)
                        && workingCommitFiles.containsKey(cwdName)) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
            File branchPath = Utils.join(".gitlet", "branch");
            HashMap<String, String> branchHeads = help.getBranchHeads();
            branchHeads.put(help.getCurrentBranchName(), stringID);
            Utils.writeObject(branchPath, branchHeads);
            help.clearStage();
        } else {
            System.out.println("No commit with that id exists");
            System.exit(0);
        }
    }

    /**
     * Throws an error for merge if necessary.
     * @param branchName name of branch
     */
    public void mergeCheck(String branchName) {
        if ((help.getAddArea().getAddedFiles() != null
                && help.getAddArea().getAddedFiles().size() != 0)
                || help.getRemoveArea().getRemoveFiles() != null
                        && help.getRemoveArea().getRemoveFiles().size() != 0) {
            System.out.println("You have uncommited changes.");
            System.exit(0);
        }
        if (!help.getBranchHeads().containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (help.getCurrentBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself");
            System.exit(0);
        }

        Commit currentHead = help.getHeadCommit();
        Commit givenHead = help.getCommit(
                help.getBranchHeads().get(branchName));
        HashMap<String, String> givenFiles = givenHead.getFiles();
        Commit splitPoint = help.getSplitPoint(branchName);
        List<String> cwdFileNames = help.getCWDFiles();
        if (splitPoint.getID().equals(givenHead.getID())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
        }
        if (splitPoint.getID().equals(currentHead.getID())) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        for (String cwdName: cwdFileNames) {
            if (!help.tracked(cwdName) && givenFiles.containsKey(cwdName)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }
    /**
     * Merge two branches together.
     * @param branchName name of branch
     * @throws IOException
     */
    public void merge(String branchName) throws IOException {
        mergeCheck(branchName); boolean mergeConflict = false;
        Commit currentHead = help.getHeadCommit();
        HashMap<String, String> cFiles = currentHead.getFiles();
        Commit givenHead = help.getCommit(
                help.getBranchHeads().get(branchName));
        HashMap<String, String> gFiles = givenHead.getFiles();
        Commit splitPoint = help.getSplitPoint(branchName);
        HashMap<String, String> sFiles = splitPoint.getFiles();
        if (sFiles != null) {
            for (String sFile: sFiles.keySet()) {
                if (help.containsFile(currentHead, sFile)
                        && help.containsFile(givenHead, sFile)) {
                    if (sFiles.get(sFile).equals(cFiles.get(sFile))
                            && !gFiles.get(sFile).equals(sFiles.get(sFile))) {
                        checkoutIDFile(givenHead.getID(), sFile); add(sFile);
                    }
                    if (!sFiles.get(sFile).equals(cFiles.get(sFile))
                            && !gFiles.get(sFile).equals(sFiles.get(sFile))
                            && !gFiles.get(sFile).equals(cFiles.get(sFile))) {
                        String mergeContents = help.writeMergeConflict(cFiles.
                                        get(sFile), gFiles.get(sFile));
                        Utils.writeContents(
                                Utils.join(".", sFile), mergeContents);
                        mergeConflict = true; add(sFile);
                    }
                }
                if (help.containsFile(currentHead, sFile)
                        && sFiles.get(sFile).equals(cFiles.get(sFile))
                        && !help.containsFile(givenHead, sFile)) {
                    remove(sFile);
                }
                if (help.containsFile(givenHead, sFile)
                        && !sFiles.get(sFile).
                        equals(gFiles.get(sFile))
                        && !help.containsFile(currentHead, sFile)) {
                    String mergeContents = help.writeMergeConflict(null,
                            gFiles.get(sFile));
                    Utils.writeContents(Utils.join(".", sFile), mergeContents);
                    mergeConflict = true; add(sFile);
                }
                if (help.containsFile(currentHead, sFile)
                        && !sFiles.get(sFile).equals(cFiles.get(sFile))
                        && !help.containsFile(givenHead, sFile)) {
                    String mergeContents = help.writeMergeConflict(
                            cFiles.get(sFile), null);
                    Utils.writeContents(Utils.join(".", sFile), mergeContents);
                    mergeConflict = true; add(sFile);
                }
            }
        }
        mergeConflict = mergeGiven(branchName) || mergeConflict;
        printMerge(mergeConflict);
        String message = "Merged " + branchName + " into "
                + help.getCurrentBranchName() + ".";
        help.mergeCommit(message, givenHead.getID());
    }

    /**
     * Prints depending on whether encountered conflict.
     * @param conflict boolean
     */
    public void printMerge(boolean conflict) {
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /**
     * Split the merge in two.
     * @param branchName name of branch.
     * @return whether or not mergeConflict arises.
     * @throws IOException
     */
    public boolean mergeGiven(String branchName) throws IOException {
        boolean mergeConflict = false;
        List<String> cwdFileNames = help.getCWDFiles();
        Commit currentHead = help.getHeadCommit();
        HashMap<String, String> currentFiles = currentHead.getFiles();
        Commit givenHead = help.getCommit(
                help.getBranchHeads().get(branchName));
        HashMap<String, String> givenFiles = givenHead.getFiles();
        Commit splitPoint = help.getSplitPoint(branchName);
        HashMap<String, String> splitFiles = splitPoint.getFiles();
        if (givenFiles != null) {
            for (String givenFile: givenFiles.keySet()) {
                if ((splitFiles == null
                        || !help.containsFile(splitPoint, givenFile))
                        && !help.containsFile(currentHead, givenFile)) {
                    checkoutIDFile(givenHead.getID(), givenFile);
                    add(givenFile);
                }
                if ((splitFiles == null || !help.containsFile(splitPoint,
                        givenFile)) && help.containsFile(currentHead, givenFile)
                        && !givenFiles.get(givenFile).
                        equals(currentFiles.get(givenFile))) {
                    String mergeContents = help.writeMergeConflict(
                            currentFiles.get(givenFile),
                            givenFiles.get(givenFile));
                    File f = Utils.join(".", givenFile);
                    Utils.writeContents(f, mergeContents);
                    mergeConflict = true;
                    add(givenFile);
                }
            }
        }
        return mergeConflict;
    }

}
