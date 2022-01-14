# Gitlet Design Document

**Lin Zhuo**

## Classes and Data Structures

### Commit 
everything inside commit and some methods commit need
#### variable 

1. String message : commit message
2. String parent1 : default parent commit  
3. String parent2 : new parent commit if there is a new branch
4. Date time : timestamp for commit
5. Map<String, String> blobs : Map for blobs in the commit, key is filename, value is UID of that blob 

#### method
1. Commit() : initialize commit
2. Commit(String text) : create commit with message
3. Commit(String text, String branchHeadUID) : create merge commit 
4. containFile(String filename) : check whether commit contain specific file
5. getUID(String filename) : get the UID of specific file in this commit
6. keySet() : return keySet of commit blobs
7. get(variable)() : get variable



### Repository 

#### variable 

1. File CWD : current working directory 
2. File GITLET_DIR : .gitlet directory
3. File COMMIT_DIR : commits directory
4. File BLOB_DIR : blobs directory
5. File BRANCH_DIR : branches pointer directory
6. File STAGING_DIR : staging file directory
7. File ADD_DIR : directory for staging file to add 
8. File DEL_DIR : directory for staging file to del
9. File head : file storage head pointer
10. File master : file storage master pointer
11. File currentBranch : current branch name

#### method
1. setupRepository : setup repository
2. initialized() : check whether .gitlet exist.
3. saveCommit : save one new commit in directory and set head, master pointer to it 
4. cleanStagingArea() : clean staging area if there is any file
5. cleanDirectory(File directoryPath) : empty specific directory
6. currentCommit() : return head commit
7. commitByUID(String UID) : find commit by UID in directory
8. blobsByUID(String UID) : get path of blob by UID
9. add(String filename) : add one file to staging area
10. rm(String filename) : remove the file if it is currently staged for addition
11. log() : display information about each commit backwards
12. globalLog() : display information of all commits.
13. checkout(String filename) : recover specific file to head commit version
14. checkout(String UID, String filename) : recover file to specific commit version
15. checkoutB(String branchName) : put file at the head commit of the given branch to CWD
16. find(String message) : Prints out the ids of all commits that have the given commit message.
17. status() : Displays what branches currently exist, and marks the current branch with a *.
Also displays what files have been staged for addition or removal.
18. branch(String name) : Creates a new branch with the given name, and points it at the current head commit.
19. rmBranch(String name) : Deletes the branch with the given name.
20. 

## Algorithms

## Persistence
1. COMMIT_DIR : save commit in file with name as its sha1 UID
2. BLOB_DIR : save blobs in file with name as its sha1 UID
3. BRANCH_DIR : save branch head commit UID with name as branch name
4. ADD_DIR : save added file with name as its filename 
5. DEL_DIR : save removed file with name as its filename
6. head : UID of the head commit
7. master : UID of master branch head commit
8. currentBranch : save name of current branch
