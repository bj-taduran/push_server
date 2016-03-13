Meta:
@story RDBFU-48

Narrative:
In order to check my channels configuration
As a user
I want to view my application details

Scenario: user goes to app details page to view an application with complete details
Given an app with complete details was already created for the user
When user access the app details page
Then user should see the complete details shown on the page

Scenario: user goes to app details page to view an application with details for some channels only (either APNS, GCM, email or SMS only)
Given an app with details for specific channels only was already created for the user
When user access the app details page
Then user should see only the details that was filled up on the page

Scenario: user goes to app details page with no configured channels
Given an app with no configured channels was already created for the user
When user access the app details page
Then warning notification "No channels are currently configured for this application. Please contact MTT support." should be presented to the user