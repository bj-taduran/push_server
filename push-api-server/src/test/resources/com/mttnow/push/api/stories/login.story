Meta:
@story RDBFU-44

Narrative:
In order to configure my channels for push messaging
As an admin user
I want to be able to create an application

!--Scenario: User is logging in using a valid username and password.
!--Given User has a valid account
!--And User has one or more applications
!--When The user tries to log in w/ username and password
!--Then The user is presented the my apps page with the list of applications

Scenario: User is logging in using an INVALID username and password.
Given User has no valid account
When The user tries to log in w/ username and password
Then The user is presented with HTTP response 401
