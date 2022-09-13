if(firstSync){
issue.repository   = "rheaton-ras/customer-portal"
}
issue.summary      = replica.summary
issue.description  = replica.description
issue.comments     = commentHelper.mergeComments(issue, replica)

issue.labels       = replica.labels
if(replica?.typeName){
   issue.labels.push(replica.typeName.toLowerCase())
}
if(replica?.storyPoints){
   issue.labels.push("effort-$replica.storyPoints")
}

/*
Status Synchronization
Sync status according to the mapping [remote issue status: local issue status]
If statuses are the same on both sides don"t include them in the mapping
*/
// "remote status name": "local status name"
def statusMap = ["To Do" : "open" , "Done" : "closed"]
def remoteStatusName = replica.status.name
issue.setStatus(statusMap[remoteStatusName] ?: remoteStatusName)

issue.assignee     = nodeHelper.getUserByUsername(replica.assignee?.username)
issue.reporter     = nodeHelper.getUserByUsername(replica.reporter?.username)
