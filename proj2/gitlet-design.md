# Gitlet Design Document

**Lin Zhuo**

## Classes and Data Structures

### commit 
everything inside commit and some methods commit need
#### variable 

1. String message : commit message
2. String parent1 : default parent commit  
3. String parent2 : new parent commit if there is a new branch
4. Date time : timestamp for commit

#### method
1. commit : create initial commit



### repository 

#### variable 

1. CWD : current working directory 
2. GITLET_DIR : .gitlet directory
3. COMMIT_DIR : commits directory
4. BLOB_DIR : blobs directory
5. BRANCH_DIR : branches pointer directory
6. STAGING_DIR : staging file directory
7. ADD_DIR : directory for staging file to add 
8. DEL_DIR : directory for staging file to del
9. head : file storage head pointer
10. master : file storage master pointer

#### method
1. setupRepository : setup repository
2. saveCommit : save one new commit in directory and set head, master pointer to it 

## Algorithms

## Persistence
storage commit, blob, branch pointer, staging file
