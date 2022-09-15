if(firstSync){
   issue.projectKey   = "CATW" 
   issue.typeName     = nodeHelper.getIssueType(replica.type?.name, issue.projectKey)?.name ?: "Story"
}
issue.summary      = replica.summary
issue.description  = replica.description
issue.labels       = replica.labels

if(replica?.key){
   def labelValue = "github-key-".plus(replica.key)
   issue.labels += nodeHelper.getLabel(labelValue)
}

def userMap = [
    "rheaton-ras": "rheaton@coresupply.com", 
    "gbarkersurge": "garybarker@coresupply.com", 
    "RASCFranke": "cfranke@coresupply.com",
    "dmclaughlinCoresupply": "dmclaughlin@coresupply.com",
    "dlearycoresupply": "dleary@coresupply.com"
]
def defaultUser = nodeHelper.getUserByEmail("rheaton@coresupply.com")
def reporter = replica.reporter?.username ?: defaultUser.username
issue.reporter = nodeHelper.getUserByEmail(userMap[reporter]) ?: defaultUser
def assignee = replica.assignee?.username
if(assignee){
   issue.assignee = nodeHelper.getUserByEmail(userMap[assignee])
}
issue.comments = commentHelper.mergeComments(issue, replica){ it.executor = nodeHelper.getUserByEmail(userMap[it.author?.username]) ?: defaultUser.email }

def statusMap = [
    "open" : "To Do", 
    "closed" : "Done"
]
def remoteStatusName = replica.status.name
issue.setStatus(statusMap[remoteStatusName] ?: remoteStatusName)
