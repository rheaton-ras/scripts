if(firstSync){
   issue.projectKey   = "CATW" 
   // Set type name from source issue, if not found set a default
   issue.typeName     = nodeHelper.getIssueType(replica.type?.name, issue.projectKey)?.name ?: "Task"
}
issue.summary      = replica.summary
issue.description  = replica.description
issue.attachments  = attachmentHelper.mergeAttachments(issue, replica)

replica.labels.each{value -> 
  if(value[0..-2] == "effort-"){
      def points

      switch (value[7..-1])
      {
          case "1":
              storyPoints = 1
              break
          case "2":
              storyPoints = 2
              break
          case "3":
              storyPoints = 3
              break
          case "4":
          case "5":
              storyPoints = 5
              break
          case "6":
          case "7":
          case "8":
              storyPoints = 8
              break
          case "9":
          case "10":
          case "11":
          case "12":
          case "13":
              storyPoints = 13
              break
      }
  } else if(value == "bug"){
      issue.typeName = "Bug"
  } else {
      issue.labels.push(value)
  }
}

/*
User Synchronization (Assignee/Reporter)
Set a Reporter/Assignee from the source side, if the user can't be found set a default user
You can use this approach for custom fields of type User
*/
def defaultUser = nodeHelper.getUserByEmail("rheaton@coresupply.com")
issue.reporter = nodeHelper.getUserByEmail(replica.reporter?.email) ?: defaultUser
issue.assignee = nodeHelper.getUserByEmail(replica.assignee?.email) ?: defaultUser

/*
Comment Synchronization
Sync comments with the original author if the user exists in the local instance
Remove original Comments sync line if you are using this approach
original
//issue.comments     = commentHelper.mergeComments(issue, replica)
*/
issue.comments = commentHelper.mergeComments(issue, replica){ it.executor = nodeHelper.getUserByEmail(it.author?.email) }

/*
Status Synchronization
Sync status according to the mapping [remote issue status: local issue status]
If statuses are the same on both sides don't include them in the mapping
*/
// "remote status name": "local status name"
def statusMap = ["open" : "To Do", "closed" : "Done"]
def remoteStatusName = replica.status.name
issue.setStatus(statusMap[remoteStatusName] ?: remoteStatusName)

/*
Custom Fields
This line will sync Text, Option(s), Number, Date, Organization, and Labels CFs
For other types of CF check documentation
issue.customFields."CF Name".value = replica.customFields."CF Name".value
*/
