# CSE 216
This is Team Fourteam's repository. It is intended for use during phase 2.

## Details
- Application Name: The Buzz
- Bitbucket Repository: https://bitbucket.org/junchenbao/cse216_fl22_group14/src/master/README.md?mode=edit&at=master
- Trello Invitation Link: https://trello.com/invite/b/ce1jUW7H/ATTI1c150689108daa233d3151c856bd9e1d945CE3CB/fourteamphase3

## Contributors
1. Junchen Bao (jub424@lehigh.edu)
2. Ala Chua (anc224@lehigh.edu)
3. Na Chen (nac324@lehigh.edu)
4. Gerardo Hernandez Macoto (geh223@lehigh.edu)
5. Alex Guataipu (alg223@lehigh.edu)

## Functionality 
## Phase 1 Release:
- A single anonymous user can:
	- Get ideas stored on database
	- Post ideas to database
	- Edit ideas, changes updated to database
	- Delete idea from database
	- Like ideas and remove likes from ideas
- The server admin can:
	- Create table for database
	- Manage posts using an ID
	- Manage likes on posts using an ID
## Phase 2 Release:
- A single anonymous user can:
	- Get ideas stored on database
	- Get the number of likes and dislikes
	- Get comments on ideas from database 
	- Get login information from database 
	- Get profile info from database
	- Post new ideas to database
	- Post token to the backend
	- Like and remove like from ideas
	- Delete likes and dislikes
	- Edit new comments under ideas
	- Edit profile info
- The server admin can:
	- Create all the tables based on the ERD created
	- Invalidate an idea
		- In the case it is inappropriate or redundant
	- Invalidate a user
		- In the case when they use the app inappropriately
	- Ensure invalidated idea does not display
	- Ensure an invalidated user is not allowed to log in
## Phase 3 Release
- A single anonymous user can:
	- Get ideas stored on database
	- Get the number of likes and dislikes
	- Get comments on ideas from database 
	- Get login information from database 
	- Get profile info from database
	- Get link of resources from Google Drive
	- Post new ideas to database
	- Post token to the backend (login)
	- Like and remove like from ideas
	- Dislike and remove dislike from ideas
	- Post comments under ideas
	- Post new resource to Google Drive
	- Edit comments under ideas
	- Edit profile info
- The server admin can:
	- Manage all the tables based on the ERD created
	- Invalidate an idea
		- In the case it is inappropriate or redundant
	- Invalidate a user
		- In the case when they use the app inappropriately
	- Ensure invalidated idea does not display
	- Ensure an invalidated user is not allowed to log in
## Building & Running Application
- Backend
	- $ mvn package
	- $ mvn heroku:deploy
- Mobile
	- $ flutter run
- Admin
	- $ mvn package
	- $ DATABASE_URL=postgres://epyqfjqcxwfqev:5592fc974fab7f2e1482fab5391b99a39f085e06aff092e5f9c064e00fc27c8d@ec2-3-216-167-65.compute-1.amazonaws.com:5432/da1nea8nc5r1r mvn:exec java
- Web
	- Deploy to localhost (sh ldeploy.sh)

<!--## Code Documentation
- [Backend](backend/javadocs)
- [Admin](admin-cli/javadocs) 

# Project Design and Planning Artifacts
## System Architecture Diagram
![image](markdown_images/SystemDiagram_v2.png)
## Routes
![image](markdown_images/Route_v3.png)
## Entity Diagram
![image](markdown_images/ERD_v3.png)-->

## User Stories
- Lehigh Current User
	- As a user, I want to create and save posts, so that I can share my ideas
	- As a user, I want to interact with other posts, so that I can like and dislike ideas
	- As a user, I want to see other posts, so that I can view other users' ideas
	- As a user, I want to post on mobile or web, so that I can access data on different platforms
	- As a user, I want to edit a post, so that I can change my idea
	- As a user, I want to login, so that I can save information and read all the posts
	- As a user, i want to save my profile, so that i can express myself
	- As a user, i want to comment on a idea, so that I can comment
	- As a user, I want to edit my comment, so that i can change my opinion
	- As a user, I want to logout, so that my info is safe remove "edit post"
	- As a user, I would like to upload a resource, so I can save the resource in the app
	- As a user, I would like to use the link I want, so that i can get the resource from google drive
- Admin
	- As an admin, I want to create and drop a table, so that I can manage posts
	- As an admin, I want a command-line interface, so that I can interact with and manage ideas
	- As an admin, I want to create other routes, so that I can send data to the front-end
	- As an admin, I want to view all profiles, so that i can manage the app
	- As an admin, I would like to see all the resources, so I can manage what is shown in the app


## Artifacts:
- Link: https://bitbucket.org/junchenbao/cse216_fl22_group14/src/master/markdown_images/Latest_version_artifacts
	
## Backlog Item & Tech Debt List (sprint 10):
- Mobile:
	- Ideas do not show likes
	- Ideas do not show names of users, only user ids (Tried implementing a hash map that saves user id and names, couldn’t test with ideas table down)
	- Ideas cannot be liked
	- Unit tests not implemented
	- User cannot view other profiles (Tried to implement, couldn’t test with technical difficulties)
	- Users cannot view comments

- Backend:
	- Deploy not working

- Admin:
	- not specific methods to do each update of items in database specifically
	- no fast way to drop and create all tables with one method
	- moving from table to table isn't very optimal

- Web 
	- The JS Documentation
	- Unit test for react
	


	
