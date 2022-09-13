if(firstSync){
   issue.repository   = "rheaton-ras/customer-portal"
}
issue.summary      = replica.summary
issue.description  = replica.description
issue.comments     = commentHelper.mergeComments(issue, replica)

issue.labels       = replica.labels
if(replica?.type){
   issue.labels.push(replica.type.toLowerCase())
}
if(replica?.storyPoints){
   issue.labels.push("effort-$replica.storyPoints")
}
if(replica?.key){
   issue.labels.push("jira-key-$replica.key")
}
if(replica?.priority){
   issue.labels.push("priority-$replica.priority")
}
if(replica?.project){
   issue.labels.push(replica.project)
}
if(replica?.parentId){
   issue.labels.push("parent-$replica.parentId")
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
