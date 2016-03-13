Meta:
@story RDBFU-46

Narrative:
In order to see my existing applications
As a user
I want to be able to view the list of my applications with corresponding quick links

Scenario: An admin user tries to access the apps dashboard
Given user was able to login successfully
And user has admin rights
When there is more than one application
Then apps dashboard is displayed with the list of applications with quick links
And create application link

!--Scenario: A user tries to access the apps dashboard
!--Given user was able to login successfully
!--When there is more than one application
!--Then apps dashboard is displayed with the list of applications with quick links

Scenario: User with no channels tries to compose a message
Given user application doesn’t have any channels configured
When user is presented with quick links on the my apps dashboard
Then compose quick link should be disabled
And a warning message is displayed for the application stating that no channels are configured yet

Scenario: User channels configured tries to compose a message
Given user application have any channels configured
And user is presented with quick links on the my apps dashboard
When user tries to click one of the enabled quick links
Then user should be able to access that feature

Scenario: Another user with different set of applications tries to log in
Given another user was able to login successfully
When user tries to view his application
Then user has a different set of applications from admin user