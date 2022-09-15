if(firstSync){
   issue.repository   = "rheaton-ras/customer-portal"
}
issue.summary      = replica.summary
issue.description  = replica.description
issue.labels       = replica.labels

if(replica?.key){
   def labelValue = "jira-key-".plus(replica.key)
   issue.labels += nodeHelper.getLabel(labelValue)
}

def userMap = [
   "rheaton@coresupply.com": "rheaton-ras",
   "garybarker@coresupply.com": "gbarkersurge",
   "cfranke@coresupply.com": "RASCFranke",
   "dmclaughlin@coresupply.com": "dmclaughlinCoresupply",
   "dleary@coresupply.com": "dlearycoresupply"
]
def defaultUser = nodeHelper.getUserByUsername("rheaton-ras")
def reporter = replica.reporter?.email ?: defaultUser.email
issue.reporter = nodeHelper.getUserByUsername(userMap[reporter]) ?: defaultUser
def assignee = replica.assignee?.email
if(assignee){
   issue.assignee = nodeHelper.getUserByUsername(userMap[assignee])
}
issue.comments     = commentHelper.mergeComments(issue, replica){ it.executor = nodeHelper.getUserByUsername(userMap[it.author?.email]) ?: defaultUser.username }

def statusMap = [
   "To Do" : "open" , 
   "Done" : "closed"
]
def remoteStatusName = replica.status.name
issue.setStatus(statusMap[remoteStatusName] ?: remoteStatusName)
