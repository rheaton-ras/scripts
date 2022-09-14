if(firstSync){
   issue.projectKey   = "CATW" 
   issue.typeName     = nodeHelper.getIssueType(replica.type?.name, issue.projectKey)?.name ?: "Task"
}
issue.summary      = replica.summary
issue.description  = replica.description
issue.attachments  = attachmentHelper.mergeAttachments(issue, replica)

if(replica?.key){
   def labelValue = "github-key-".plus(replica.key)
   issue.labels += nodeHelper.getLabel(labelValue)
}

replica.labels
.collect{ it.label = it.label.replace(" ", "_"); it }
.each{value -> 
   def stringValue = value.label[0..-2]
   if(stringValue == "effort-"){
      switch (value.label[7..-1])
      {
          case "1":
              issue.storyPoints = 1
              break
          case "2":
              issue.storyPoints = 2
              break
          case "3":
              issue.storyPoints = 3
              break
          case "4":
          case "5":
              issue.storyPoints = 5
              break
          case "6":
          case "7":
          case "8":
              issue.storyPoints = 8
              break
          case "9":
          case "10":
          case "11":
          case "12":
          case "13":
              issue.storyPoints = 13
              break
      }
   } else if(value.label == "bug"){
      issue.typeName = "Bug"
   } else {
      issue.labels += nodeHelper.getLabel(value.label)
   }
}

def userMap = ["rheaton-ras": "rheaton@coresupply.com", "gbarkersurge": "garybarker@coresupply.com", "RASCFranke": "cfranke@coresupply.com"]
def assignee = replica.assignee.username
def reporter = replica.reporter.username
def defaultUser = nodeHelper.getUserByEmail("rheaton@coresupply.com")
issue.reporter = nodeHelper.getUserByEmail(userMap[reporter]) ?: defaultUser
issue.assignee = nodeHelper.getUserByEmail(userMap[assignee]) ?: defaultUser

issue.comments = commentHelper.mergeComments(issue, replica){ it.executor = nodeHelper.getUserByEmail(it.author?.email) }

// "remote status name": "local status name"
def statusMap = ["open" : "To Do", "closed" : "Done"]
def remoteStatusName = replica.status.name
issue.setStatus(statusMap[remoteStatusName] ?: remoteStatusName)

/*
issue.customFields."CF Name".value = replica.customFields."CF Name".value
*/
