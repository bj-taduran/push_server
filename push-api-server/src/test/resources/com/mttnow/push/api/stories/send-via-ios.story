Meta:
@story RDBFU-71

Narrative:
In order to allow iOS devices to receive push messages
As a user
I want to be able to send a message using iOS channel

Scenario: user sends to iOS channels only
Given user is on push message composition page
And iOS channel configuration is set up
And user enters valid message
And adds tags
And selects iOS channel
And selects Right Now as delivery time
When user clicks on Send button
Then user is presented with a page notification "Message created and sent successfuly"

Scenario: user sends to multiple channels including iOS 
!--Given the user is on push message compose page
!--And iOS channel configuration is set up
!--And user enters valid message
!--And user adds tags
!--When user selects iOS channel
!--And user selects other channels
!--And user selects Right Now as delivery time
!--And user clicks on Send button
!--Then user is presented with a page notification "Message created and sent successfuly. View Message History (link)"
!--And user is able to send messages through all channels
!--And user is retained on compose message page
!--And compose message fields are cleared