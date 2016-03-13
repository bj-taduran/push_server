Meta:
@story RDBFU-65

Narrative:
In order to set recipients of my message
As a user composing a message
I want to to be able to search and select tags as recipients

Scenario: User filtered the tags list
Given user is in the Compose page
And user typed some characters
When user clicked the Search button
Then user is directed to Tags list page limited with selection containing the user typed string

Scenario: User wants to see all tags
Given user is in the Compose page
And user didn't type any character
When user clicked the Search button
Then user is directed to Tags list page with the complete list of available tags