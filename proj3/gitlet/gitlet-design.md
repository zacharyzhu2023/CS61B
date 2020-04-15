# Gitlet Design Document

**Name**: Zachary Zhu

# Classes and Data Structures

## Init

This class contains information/command for creating new Gitlet system in CWD.

**Fields**
1. boolean containsDirectory: refers to whether a directory is already present
2. Time time: refers to time directory was created
3. Commit initialCommit: an instance of commit for creation of directory

## Add

This class allows for addition of file to staging area.

**Fields**
1. File file: file to be added to the staging area


## Commit
This class contains information/command for saving files in staging area to track files.

**Fields**
1. Stage stage: The current stage that contains all the files to be committed.
2. File[] filesArray: Contains the files present in directory--existing & modified, excluding removed.
    * May want to modify into an ArrayList or different data structure
    * Perhaps can be represented with just the ID's?
3. Date date: variable that contains date/time a commit is made.
4. String commitMessage: refers to message user provides when committing.
5. String idString: SHA-1 ID that's unique to each commit. This will contain files, date, message, & parent.
6. LinkedList<Commit> bCommitList: LinkedList of commits in current branch (not sure if here's where to put it)
    * Will be placed in a HashMap of commitLists with <Key, Value> --> <branchName, commitList>
7. Commit head: Reference to commitList for the head of the current commit for the given branch.
    * How could this be integrated into the HashMap structure?
8. LinkedList<Commit> allCommitList: LinkedList of all commits ever made for all eternity.
9. Branch currentBranch: Pull the current working branch from the branch class.

## Remove
This class unstages file for addition.

**Fields**
1. Stage stage: Current stage that either does/doesn't contain the file.
2. Commit currentCommit: Working version of commit (borrowed from Commit.java) to check if file is tracked or not.

## Log
This class prints out all commits starting from head backward.

**Fields**
1. Commit currentCommit: Current commit (with reference to head) to provide all commits in commitList.
2. ArrayList<commitMessage>: Contains each commit's ID, date, and message.

## GlobalLog
This class prints out all commits in the existing branch.

**Fields**
1. Commit currentCommit: Current commit (with pointer at head) that still contains all previous commits.

## Find
This class prints IDs of all commits with a given commit message.

**Fields**
1. String findMessage: Message that is matched against the commit messages.
2. ArrayList<String> matchingID: ArrayList of strings that eventually match the message.
3. Log log: Log that is searched through for commit ID and message.

## Status
This class shows existing branches and current branch.

**Fields**
1. Stage stage: Current stage that contains reference to staged files.
    * Should be able to reference working directory in conjunction
    * Stage contains info about which files have been modified
    * Modifications Not Staged for Commit & Untracked Files: OPTIONAL
2. Branch branch: Contains reference to current branch and all existing branches


## Checkout
This class should be able to overwrite contents in a specific file or all files in a branch

**Fields**
1. File checkoutFile: File to be checked out
2. String commitID: ID providing commit to be referenced to checkout a specific file.
3. Branch checkoutBranch: Entire branch to be checked out.
    * Basically just performing File version but on a larger scale
4. boolean filePresent: checks to see if file with name already present.
5. Stage stage: Stage to be cleared if branch version is invoked.

## Branch
This class allows for creating of a new branch: a commit tree.

**Fields**
1. Branch currentBranch: contains a reference to the current working branch.
2. Commit currentCommit: contains a reference to current commit in working branch.
3. String branchName: The new name for the branch, should it be added.
4. ArrayList<Branch> allBranches: contains an ArrayList with reference to all existing branches

**Questions to consider**
   * Will each branch contain a single commit?
   * Should each commit have access to a branch?
   * What are the time/space considerations to consider with reference to other classes?

## RemoveBranch
This class allows for the removal of a branch with a given name

**Fields**
1. String branchName: branch name to delete (if it exists)
    * Pulls allBranches from Branch to destroy the pointer
    * Note: It should not destroy actual commits
    * Another note: cannot remove a branch that is the currentBranch

## Reset
This class checks out all files from a commit & removes tracked files not at commit.

**Fields**
1. String commitID: ID to check out the files from a given commit
2. Commit resetCommit: Use ID to reference commit using FIND function/class
3. Stage stage: Stage to be cleared post-reset

**Considerations**
* Essentially functions as checking out a branch while moving branch head

## Merge
This class allows for the merging of files of a given branch into current branch

**Fields**
1. String currentBranchName: Name of current branch name.
2. String toBeMergedName: Name of branch to be merged with current branch.
3. Branch currentBranch: Branch referring to current branch.
4. Branch toBeMerged: Branch that will get merged with the above.
    * Note: Both currentBranch and toBeMerged can be pulled from Branch.java.
5. Stage stage: Stage that may contain files that are added/removed/staged.
    * Note: not sure if this is necessary: come back to merge later.
    
## Stage
This class contains the files to be added in the Gitlet directory through a commit.

**Fields**
1. ArrayList<File> filesAdded: An ArrayList of files to be committed.

## File/Blob--File will likely extent or implement Blob
This class contains the necessary information about each file/object.

**Fields**
1. HashMap<String, String> fileInfo: Maps file name to the ID (SHA-1)
2. byte[] fileContents: contains all the text of the file.
    * This would be unique to File

**Other Considerations**
* This may not be necessary and could potentially be integrated w/ other files.
* To be unique, a file/blob must have the same name + contents
    * Hash function can be used to determine same contents

## Main
Has the ability to run all the commands.

**Fields**
1. HashMap<String, Command> commandRunner: Maps name of command to the corresponding class.

## Other Possible Class Ideas
1. Command: Generic command abstractClass that all the others can build off of
    * What are the common characteristics?

# Algorithms
1. Init: hasGitletDirectory() to check if a directory has already been initialized. <br />
   createGitletDirectory() which will track existing files & commit with date & message.
2. Add: fileExists() to check to make sure that the file exists in current directory. <br />
   Then, check to make sure that the file isn't present on stage in Stage class. If both <br/>
   conditions are satisfied, check to make sure that file's contents are different using File </br>
   class's hashContents() function. If it isn't unique, REMOVE file from staging area. If it is </br>
   unique, then addFile() will add the file to the stage.
3. Commit: Check to see that stage contains > 0 files, whether for adding or removal. Then, <br/>
   hasMessage() will check to make sure that a valid commit message is entered. If so, carry <br/>
   out commitFiles() which will call save current staged version of files in stage added to filesArray, remove <br/>
   those as neccessary. Track date/time/message with commit, hashContents() to obtain unique identifier <br/>
   for version of the commit, and add Commit object to commitList, moving head to point to most recent commit. <br/>
   * Note: May also want to save branch info--look into further
4. Remove: Unstage if in stage. removeFile() will check commit's head to see if file is present. <br/>
   If it is, place in stage and remove from working directory.
5. Log: printLog() will go through the commit of the current branch, working backward to print out <br/>
   all the Commit object's ID's, date, and message, separated appropriately. Merge commits will have 2 ID's.
6. GlobalLog: printGlobalLong()  does the same thing as Log but for all commits ever made.
7. Find: findCommitMessage() combs through global log and searches through commit messages to see if <br/>
   a matching message exists. Print all all that matches criteria.
8. Status: status() will call branch's findCurrentBranch() method, stage's stagedForAdditionFiles() and <br/>
   stagedForRemovalFiles() to display the current working status.
9. Checkout: checkoutFile()--check if file is present in commit head's filesArray. If it is, pull contents & override. <br/>
   checkoutIDFile()--check if file is present at given commit ID (as referenced by globalCommits), and if it is present, <br/>
   overwrite existing file's contents with this one. checkoutBranch() will overwrite files in CWD with branch's commit head's <br/>
   version of the files. If file isn't present in CWD, add them (track them with some List structure). Make checked out branch <br/>
   the current branch in Branch, and any files originally present that aren't in the checked out branch are deleted. <br/>
   Must check to make sure file name & branch name exists, inherited respectively from those classes: FILE & BRANCH.
10. Branch--createBranch() will call hashFunction() to provide an identifier for commit. allBranches() should provide an ArrayList <br/>
    of allBranches present. Will contain a reference to commits made while this branch is currentBranch, without being affected by <br/>
    the other branches.
    * What kind of structure is necessary to implement this?--How does it relate to commits?
11. RemoveBranch--removeBranch() will check to make sure branch is present with allBranches() from Branch class. Then, if it is <br/>
    present, simply remove it from the ArrayList allBranches. Removes pointer--not file
12. Reset
13. Merge
14. Stage
15. File/Blob


# Persistence

