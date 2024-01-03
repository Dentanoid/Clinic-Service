# Clinic service
Welcome to the Clinic service! This service handles request for:

* Registering clinics
* Deleting clinics
* Adding dentist to clinic
* Removing dentist from clinic
* Retrieving a specific clinic
* Retrieving all clinics
* Retrieving clinics within radius
* Retrieving N cloest clinics

 
## Getting started

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

### Google API Key
Due to the usage of Google Maps API, a key is needed to run the service:

1. Open [Google Cloud Console](https://console.cloud.google.com/projectcreate?utm_source=Docs_NewProject&utm_content=Docs_places-backend&_gl=1*1f1gepp*_ga*MzMzOTMzNDk3LjE3MDIwNTU2ODQ.*_ga_NRWSTWS78N*MTcwNDMyMTU1Mi4zNC4xLjE3MDQzMjE1NjIuMC4wLjA.) website and create a project

2. Click on ‘Navigation menu’ on the top left corner
![Picture 1](https://i.ibb.co/GH7x5vw/apikey1.png)

3. Click “APIs & Services” and “Credentials”
![Picture 2](https://i.ibb.co/gS5hrtX/apikey2.png)

4. Click “Create credentials” → API Key”

5. Click “Show key” and "Copy"

6. Search for "Places API" and enable it
![Picture 3](https://i.ibb.co/4FdX0S1/apikey3.png)

7. Done! You now have a valid API key that will be used in the section below


### Run Clinic service
In order to build and run the Clinic service you need to type these commands in your terminal:

1. Navigate to the root project directory

```cd Clinic-Service/src/main/java/com/group20/dentanoid /BackendMapAPI```

2. Install node modules

```npm install```

4. Configure environment variables

Create a `.env` file

```
GOOGLE_MAPS_API_KEY={key here}
``````


3. Navigate to `Clinic-Service` folder and compile the project into a binary (including deps)


```mvn clean compile assembly:single```

4. Run the compiled JAR file

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
