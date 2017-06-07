# WASSA Android Candidate Project

## Why this test ?

At Wassa we think that there's no better way to judge technical skills of a candidate than by reading the code he or she can produce.

So here we go !

## What are we waiting for ?

So it's quite simple, imagine that you have to start this new project from scratch.
The two first *user stories* that you have to develop are :

#####*01 :  "As a user, I can browse the complete list of Places."*
#####*02 :  "As a user, I can see the details of one Place."*

Story Bonus !
#####*03 :  "As a user, I can mark a place as favorite, and browse my favorites in a separate screen."*

A *Place* is an object from the data model, describing a physical place in the world, with several attributes (label, country, image, etc ...).
See API Documentation for details.


## How to do it ?

- Design app screens as you want, they just have to validate the *user stories*.
- Follow the UX guidelines you want.
- Choose the architecture model you need, imagine that more stories have to come.
- Feel free to use any external library you need.
- Add tests if you estimate them required.
- You can add comments in code if you need to. Add TODOs or explanations on specifis part of your code.
- Fork this repository and send us your work through a pull request ([Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)).
- Be creative, have fun !


## Annexes

### Local WebServer

- The project contains a module "webserver".
- This module provides to the app a local server.
- The server is automatically started and stopped by `LaunchActivity.java`. You don't have to modify this part.
- The EndPoint of this server is : `http://localhost:8080`


####The documentation of the webservices :

* [API Documentation](/webserver/API_DOCUMENTATION.md)


####Licenses :

* The server is build on library [NanoHTTPD](https://github.com/NanoHttpd/nanohttpd).


