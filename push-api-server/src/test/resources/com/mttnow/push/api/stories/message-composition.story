Meta:
@story RDBFU-64

Narrative:
In order to send my message to push channels
As a user
I want to be able to compose a message

Scenario: User tries to compose message on application with complete details
Given user has at least one channel configured for the app
When user tries to compose a message
Then user is presented with the compose message screen with the configured channels available for selection

Scenario: User tries to compose message on application with no configured channels
Given user has no channels configured for an app
When user tries to compose a message
Then compose message link/button is disabled
