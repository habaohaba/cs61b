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
5. Map<String, String> blobs : Map for blobs in the commit, key is filename, value is UID 

#### method
1. Commit() : initialize commit
2. Commit(String text) : create commit with message
3. containFile(String filename) : check whether commit contain specific file
4. getUID(String filename) : get the UID of specific file in this commit
5. keySet() : return keySet of commit blobs


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

#### method
1. setupRepository : setup repository
2. saveCommit : save one new commit in directory and set head, master pointer to it 
3. cleanStagingArea() : clean staging area if there is any file\
4. cleanDirectory(File directoryPath) : empty specific directory
5. currentCommit() : return head commit
6. commitByUID(String UID) : find commit by UID in directory
7. blobsByUID(String UID) : get path of blob by UID
8. add(String filename) : add one file to staging area
9. rm(String filename) : remove the file if it is currently staged for addition
10. log() : display information about each commit backwards
11. globalLog() : 
12. checkout(String filename) : recover specific file to head commit version
13. checkout(String UID, String filename) : recover file to specific commit version
14. checkoutB(String branchName) : put file at the head commit of the given branch to CWD

## Algorithms

## Persistence
storage commit, blob, branch pointer, staging file
