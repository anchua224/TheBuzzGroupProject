# CSE 216
This is Team Fourteam's repository. It is intended for use during phase 1.

## Details
- Application Name: The Buzz
- Bitbucket Repository: https://bitbucket.org/junchenbao/cse216_fl22_group14/src/master/README.md?mode=edit&at=master
- Trello Invitation Link: https://trello.com/invite/b/MEsnAxxj/0745274807676a41afc93408ecbc6a7e/team-fourteam-phase-1

## Contributors
1. Junchen Bao (jub424@lehigh.edu)
2. Ala Chua (anc224@lehigh.edu)
3. Na Chen (nac324@lehigh.edu)
4. Gerardo Hernandez Macoto (geh223@lehigh.edu)
5. Alex Guataipu (alg223@lehigh.edu)

## Functionality 
Phase 1 Release:
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
	- Deploy to localhost

## Code Documentation
- [Backend](backend/javadocs)
- [Admin](admin-cli/javadocs) 

# Project Design and Planning Artifacts
## System Architecture Diagram
![image](markdown_images/SystemDiagram.png)
## Routes
![image](markdown_images/BackendRoutes_v2.png)
## Entity Diagram
![image](markdown_images/EntityDiagram.png)
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

- Admin
	- As an admin, I want to create and drop a table, so that I can manage posts
	- As an admin, I want a command-line interface, so that I can interact with and manage ideas
	- As an admin, I want to create other routes, so that I can send data to the front-end
	- As an admin, i want to view all profiles, so that i can manage the app

## User State Machine
### Mobile
![image](markdown_images/StateMachineDiagram_v2.png)
### Web
![image](markdown_images/Web_StateMachine.png)
## User Interfaces
### Mobile
![image](markdown_images/Mobile_UI.png)
### Web
![image](markdown_images/Web_UI.png)
	

