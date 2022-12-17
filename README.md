# video-streaming-app
In this project, we'll build a complete application from the scratch using Spring Boot and Angular, this app is a Video Streaming Application like Youtube. 
we are not going to build the complete Clone of Youtube, but only a minimal set of functionality, which is common for Video Streaming applications like Youtube.

# Functional Requirements
We will be implementing the following features in this project:

-User can Upload new Videos<br/>
-User can Upload Thumbnails for the Videos<br/>
-User can View Videos<br/>
-User can Like/Dislike a Video<br/>
-User can Subscribe to another User, to receive updates about future videos<br/>
-User can Login/Logout using Single Sign On<br/>
-User can comment on Videos<br/>
-User can view the History of Videos he/she watched<br/>
-User can view the List of Videos he/she Liked<br/>

# Technologies Used
So to develop this project, we are going to use the following technologies:

Spring Boot
Auth0
MongoDB
Angular
AWS S3 – to store Videos and Thumbnails

# Discussing Application Architecture
As we have the Functional Requirements ready, now it’s time to think about the high-level architecture of the application we are going to build.

We are going to follow a 3 Tier Architecture, where we have a Client Application ie. the Frontend, we will develop this using Angular Framework, the Backend Application which will be running on a server, will be developed using Spring Boot, and finally the Database, we are going to use MongoDB.

![This is an image](https://i0.wp.com/programmingtechie.com/wp-content/uploads/2021/07/youtube_clone_Architecture.png?resize=1024%2C499&ssl=1)
