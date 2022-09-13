if(firstSync){
   issue.repository   = "rheaton-ras/customer-portal"
}
issue.summary      = replica.summary
issue.description  = replica.description
issue.comments     = commentHelper.mergeComments(issue, replica)

issue.labels       = replica.labels
if(replica?.type){
   issue.labels += nodeHelper.getLabel(replica.type.toLowerCase())
}
if(replica?.storyPoints){
   def labelValue = "effort-".plus()
   issue.labels += nodeHelper.getLabel(labelValue)
}
if(replica?.key){
   def labelValue = "jira-key-".plus(replica.key)
   issue.labels += nodeHelper.getLabel(labelValue)
}
if(replica?.priority){
   def labelValue = "priority-".plus(replica.priority)
   issue.labels += nodeHelper.getLabel(labelValue)
}
if(replica?.project){
   issue.labels += nodeHelper.getLabel(replica.project)
}
if(replica?.parentId){
   def labelValue = "parent-".plus(replica.parentId)
   issue.labels += nodeHelper.getLabel(labelValue)
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
