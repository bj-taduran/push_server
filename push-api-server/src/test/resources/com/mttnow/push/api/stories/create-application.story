Meta:
@story RDBFU-45

Narrative:
In order to configure my channels for push messaging
As an admin user
I want to be able to create an application

Scenario: user creates an application without any complications
Given The user is on app creation page
And The user inputs app name and app mode
And The user did not select any channel type
When The user clicks on save button
Then The app creation is successful


Scenario: user did not supply required fields
Given The user was not able to supply the required fields.
When The user clicks on save button
Then The user is presented with an error message "Please fill in required fields"

Scenario: user created an app with an existing app name
Given The user has an existing app
And The app is still active
When The user creates a similar app
Then The user is presented an error message "Application already exists. Please check your application details."

Scenario: user tries to create an app with an existing INACTIVE app name
Given The user has an existing app
And The app is no longer active
When The user creates a similar app
Then The app creation is successful
