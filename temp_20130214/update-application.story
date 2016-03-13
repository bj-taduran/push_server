Meta:
@story RDBFU-50

Narrative:
In order to update configuration of my channels for push messaging
As an admin user
I want to be able to update an application

Scenario: user updates an application without any complications
Given User has only one app
!--And The user is on the apps details page or the apps dashboard page
!--When The user clicks on edit button
!--Then The user is presented with the app details page in edit mode

!--Scenario: user did not supply required fields
!--Given User has only one app and the user is on the apps details page or the apps dashboard page
!--When The user edit the details of the app
!--And The User clicks save button
!--Then The details of the apps is updated successfully and the details page is presented again with the updated details
!--
!--Scenario: user created an app with an existing app name
!--Given User has only one app and the user is on the apps details page or the apps dashboard page
!--When The user edits the details of the app
!--And The user clicks cancel button
!--Then The previous details of the app is retained and user is presented with the details page again
!--
!--Scenario: user tries to create an app with an existing INACTIVE app name
!--Given User has only one app
!--And The user is on the apps details page or the apps dashboard page
!--When The user edits the details of the app and does not click any button after editing
!--And The user does not click any button after editing
!--Then The previous details of the app is retained and user is presented the details page again
!--
!--Scenario: user tries to create an app with an existing INACTIVE app name
!--Given User has more than one app and is on the app details page or the apps dashboard page
!--When The user edits one app details
!--And The user saves the edited app details
!--Then The other apps not edited should not be affected
!--And User is redirected to the app details page seeing the new details provided
