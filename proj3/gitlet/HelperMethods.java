package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/** Class that contains helper methods used by Main.
 *  @author Zachary Zhu
 */
public class HelperMethods {
    /**
     * Determine whether a gitlet repository exists.
     * @return true or false value.
     */
    public boolean hasGitletRepository() {
        File testDirectory = new File(".gitlet");
        return testDirectory.exists();
    }

    /**
     * Method returns commit given ID.
     * @param id ID of commit
     * @return Commit of ID
     */
    public Commit getCommit(String id) {
        File f = Utils.join(".gitlet", "commits", id);
        return Utils.readObject(f, Commit.class);
    }

    /**
     * Method returns current head's commit.
     * @return the current head Commit.
     */
    public Commit getHeadCommit() {
        String headBranch = getCurrentBranchName();
        HashMap<String, String> branchHeads = getBranchHeads();
        String headCommitID = branchHeads.get(headBranch);
        return getCommit(headCommitID);
    }

    /**
     * Determine whether or not a file is staged for addition.
     * @param fileName name of file
     * @return true or false value
     */
    public boolean staged(String fileName) {
        List<String> stageFileNames = Utils.plainFilenamesIn(
                Utils.join(".gitlet", "stage"));
        for (String s: stageFileNames) {
            if (s.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether or not a file is tracked in headCommit.
     * @param fileName name of file
     * @return true or false value
     */
    public boolean tracked(String fileName) {
        Commit headCommit = getHeadCommit();
        HashMap<String, String> headFiles = headCommit.getFiles();
        if (headFiles != null
                && headFiles.keySet().contains(fileName)) {
            return true;
        }
        return false;
    }

    /**
     * Method that generates arraylist of all untracked files.
     * @return Names of all untracked files.
     */
    public ArrayList<String> untrackedFiles() {
        List<String> cwdFileNames = getCWDFiles();
        ArrayList<String> untrackedFiles = new ArrayList<String>();
        for (String name: cwdFileNames) {
            if (!tracked(name) && !staged(name)) {
                untrackedFiles.add(name);
            }
        }
        return untrackedFiles;
    }

    /**
     * Method that generates all files modified & not staged in a capacity.
     * @return list of file names
     */
    public ArrayList<String> modifiedNotStaged() {
        List<String> cwdfileNames = getCWDFiles();
        ArrayList<String> modNotStaged = new ArrayList<String>();
        if (cwdfileNames != null) {
            for (String name: cwdfileNames) {
                // Tracked in headCommit, changed in CWD, not staged
                File stageFile = Utils.join(".gitlet", "stage", name);
                String cwdID = Utils.sha1(
                        Utils.readContentsAsString(new File(name)));
                String headID = "";
                if (getHeadCommit().getFiles() != null) {
                    headID = getHeadCommit().getFiles().get(name);
                }
                if (tracked(name) && !stageFile.exists()
                        && !cwdID.equals(headID)) {
                    modNotStaged.add(name + " (modified)");
                }
            }
        }
        List<String> stageFileNames = Utils.plainFilenamesIn(
                Utils.join(".gitlet", "stage"));
        if (stageFileNames != null) {
            for (String name: stageFileNames) {
                // In stage, deleted from CWD
                File cwdFile = Utils.join(".", name);
                if (!cwdFile.exists()) {
                    modNotStaged.add(name + " (deleted)");
                }
            }
        }
        // Not staged for removal, Tracked by headCommit, deleted from CWD
        if (getHeadCommit() != null) {
            List<String> removedFiles = new ArrayList<String>();
            if (getRemoveArea() != null) {
                removedFiles = getRemoveArea().getRemoveFiles();
            }

            ArrayList<String> trackedFiles = new ArrayList<>();
            if (getHeadCommit().getFiles() != null) {
                trackedFiles = new ArrayList<String>(
                        getHeadCommit().getFiles().keySet());
            }
            for (String name: trackedFiles) {
                if (!removedFiles.contains(name) && (cwdfileNames == null
                        || !cwdfileNames.contains(name))) {
                    modNotStaged.add(name + " (deleted)");
                }
            }
        }
        return modNotStaged;
    }

    /**
     * Method that gets current branch.
     * @return current branch's name
     */
    public String getCurrentBranchName() {
        return Utils.readContentsAsString(
                Utils.join(".gitlet", "current"));
    }

    /**
     * Method that gets HashMap of branch --> headCommitID.
     * @return HashMap of branch --> headCommitID
     */
    public HashMap<String, String> getBranchHeads() {
        return Utils.readObject(Utils.join(
                ".gitlet", "branch"),
                HashMap.class);
    }

    /**
     * Method that generates a list of all commits.
     * @return List of commit IDs.
     */
    public List<String> getCommitsList() {
        return Utils.plainFilenamesIn(
                Utils.join(".gitlet", "commits"));
    }

    /**
     * Method that clears stage of removal/addition.
     */
    public void clearStage() {
        Utils.writeObject(Utils.join(".gitlet", "add"),
                new AddArea());
        Utils.writeObject(Utils.join(".gitlet", "remove"),
                new RemoveArea());
        List<String> stageFileNames = Utils.plainFilenamesIn(
                Utils.join(".gitlet", "stage"));
        if (stageFileNames != null && stageFileNames.size() != 0) {
            for (String sName: stageFileNames) {
                File stageFile = Utils.join(".gitlet", "stage", sName);
                stageFile.delete();
            }
        }
    }

    /**
     * Accesstor method for AddArea.
     * @return AddArea object.
     */
    public AddArea getAddArea() {
        return Utils.readObject(Utils.join(".gitlet", "add"),
                AddArea.class);
    }

    /**
     * Method generates RemoveArea.
     * @return RemoveArea Object
     */
    public RemoveArea getRemoveArea() {
        return Utils.readObject(Utils.join(".gitlet", "remove"),
                RemoveArea.class);
    }


    /**
     * The commit of a given ID (allows for abridged ID).
     * @param id String ID of object
     * @return return commit's ID
     */
    public Commit getCommitID(String id) {
        List<String> commitIDs = getCommitsList();
        String fullID = "";
        int total = 0;
        for (String cID: commitIDs) {
            if (cID.substring(0, id.length()).equals(id)) {
                fullID = cID;
                total += 1;
            }
        }
        if (total == 1) {
            return getCommit(fullID);
        }
        return null;
    }


    /**
     * Method finds the splitPoint for a given branch name.
     * @param givenBranch name of branch
     * @return the Commit of the branch.
     */
    public Commit getSplitPoint(String givenBranch) {
        HashMap<String, Integer>
                ancestorValues = new HashMap<String, Integer>();
        Commit gBranchCommit = getCommit(
                getBranchHeads().get(givenBranch));
        ArrayList<String> allAncestors = new ArrayList<>();
        findAllAncestors(gBranchCommit, allAncestors);
        traverseAllPaths(getHeadCommit(),
                ancestorValues, 0, allAncestors);
        int lowVal = Integer.MAX_VALUE;
        Commit c = null;
        for (String s: ancestorValues.keySet()) {
            if (ancestorValues.get(s) <= lowVal) {
                c = getCommit(s);
                lowVal = ancestorValues.get(s);
            }
        }
        return c;
    }

    /**
     * Method that finds all ancestors & distance to head commit.
     * @param c given commit
     * @param allPaths valid paths
     * @param totalSoFar total distance
     * @param ancestors all ancestors of branch
     */
    public void traverseAllPaths(Commit c,
                                 HashMap<String, Integer> allPaths,
                                 int totalSoFar, ArrayList<String> ancestors) {
        if (c != null) {
            if (ancestors.contains(c.getID())) {
                allPaths.put(c.getID(), totalSoFar);
            } else {
                if (c.getParentID() != null) {
                    traverseAllPaths(getCommit(c.getParentID()), allPaths,
                            totalSoFar + 1, ancestors);
                }
                if (c.getParent2ID() != null) {
                    traverseAllPaths(getCommit(c.getParent2ID()), allPaths,
                            totalSoFar + 1, ancestors);
                }
            }
        }
    }

    /**
     * Method that finds all the ancestors of a commit.
     * @param c A given commit
     * @param ancestors all its ancestors at the end of method
     */
    public void findAllAncestors(Commit c, ArrayList<String> ancestors) {
        if (c != null) {
            ancestors.add(c.getID());
            if (c.getParentID() != null) {
                findAllAncestors(getCommit(c.getParentID()), ancestors);
            }
            if (c.getParent2ID() != null) {
                findAllAncestors(getCommit(c.getParentID()), ancestors);
            }
        }
    }

    /**
     * Accessor methods for files in CWD.
     * @return files in CWD.
     */
    public List<String> getCWDFiles() {
        return Utils.plainFilenamesIn(
                new File(System.getProperty("user.dir")));
    }

    /**
     * Method writes out merge conflict.
     * @param currID current branch's ID
     * @param givenID conflicting branch's ID
     * @return String representation of merged file.
     */
    public String writeMergeConflict(String currID, String givenID) {
        String cContents = "";
        String gContents = "";
        if (currID != null) {
            File currFile = Utils.join(".gitlet", "files", currID);
            cContents = Utils.readContentsAsString(currFile);
        }
        if (givenID != null) {
            File givenFile = Utils.join(".gitlet", "files", givenID);
            gContents = Utils.readContentsAsString(givenFile);
        }
        String finalContents = "<<<<<<< HEAD\n" + cContents
                + "=======\n" + gContents
                + ">>>>>>>\n";
        return finalContents;
    }

    /**
     * Method that checks if a given commit contains a filename.
     * @param c commit
     * @param fileName name of file
     * @return whether or not a commit contains the file.
     */
    public boolean containsFile(Commit c, String fileName) {
        if (c.getFiles() == null) {
            return false;
        }
        return c.getFiles().containsKey(fileName);
    }

    /**
     * Method that creates a merge version of commit.
     * @param message message of a commit
     * @param secondParentID the second parent's ID
     */
    public void mergeCommit(String message, String secondParentID) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else {
            Commit headCommit = getHeadCommit();
            HashMap<String, String> headIDs = getBranchHeads();
            String currentBranch = Utils.readContentsAsString(
                    Utils.join(".gitlet", "current"));
            HashMap<String, String> headTrackedFiles = headCommit.getFiles();
            AddArea aa = getAddArea();
            RemoveArea ra = getRemoveArea();
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
                    File path = Utils.join(".gitlet",
                            "files", addFiles.get(s));
                    File stageVersion = Utils.join(
                            ".gitlet", "stage", s);

                    Utils.writeContents(path,
                            Utils.readContentsAsString(stageVersion));
                    stageVersion.delete();
                }

                // Remove Deleted Files
                for (String name: removeFiles) {
                    headTrackedFiles.remove(name);
                }
                Commit newHead = new Commit(message,
                        headTrackedFiles, headCommit.getID(),
                        secondParentID);
                File allCommitsFile = Utils.join(
                        ".gitlet", "commits", newHead.getID());
                Utils.writeObject(allCommitsFile, newHead);
                headIDs.put(currentBranch, newHead.getID());
                Utils.writeObject(Utils.join(".gitlet", "branch"),
                        headIDs);
                Utils.writeObject(Utils.join(".gitlet", "add"),
                        new AddArea());
                Utils.writeObject(Utils.join(".gitlet", "remove"),
                        new RemoveArea());
            }
        }
    }
}
