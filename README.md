# Clinic service
Welcome to the Clinic service! This service handles request for publishing, updating and deleing Clinics. Apart from name and location, clinics also contains references to Dentists. 
## Getting started

This service is written in Java. [Check this link for more information about Java.](https://www.java.com/en/)

To run this service you need to follow the steps described below:

### Clone the repo
First you need to Clone the repo in to a folder in your computer, if you dont know how to do it check this [guide](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository). 

### Installing Java using BREW (if you dont have Java)

If you do not have Java installed on your computer you can download both brew and Java with these commands:

#### Install brew
```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
``````

#### Install Java and maven with brew
```
brew install Java
brew install maven
``````


### Run Clinic service
In order to build and run the Clinic service you need to type these commands in to your terminal:


1. Navigate to the root project directory

```cd Clinic-Service```

2. Compile the project into a binary (including deps)

```mvn clean compile assembly:single```

3. Run the compiled JAR file

 ```java -jar target/Clinic-Service-1.0-SNAPSHOT-jar-with-dependencies.jar``` 


Congratulations! You are now running the Clinic service.
 

## Roadmap
This service will not get updated in the future, due to project being considered as closed when GU course DIT356 is finished.


## Authors and acknowledgment
This service is a part of DIT356 distributed systems course, and is created by Group 20. [Check here for more information about the entire project.](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-20/group-20-distributed-systems/-/wikis/home)

***WIP DUE TO SERVICE STILL BEING DEVELOPED***

In this service the following people have contributed:

- Mohamad Khalil
- Lucas Holter
- Cornelia Olofsson Larsson 
- James Klouda 
- Jonatan Boman 
- Joel Mattson



## Project status
The service may recieve updates until 9th January 2024, and none after.
