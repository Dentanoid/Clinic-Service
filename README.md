# ![Title-Picture](https://i.ibb.co/CVrvbBk/Clincic-Service-Title.png)
Welcome to the Clinic service! This service handles requests for:

* Registering clinics
* Deleting clinics
* Adding dentist to clinic
* Removing dentist from clinic
* Retrieving a specific clinic
* Retrieving all clinics
* Retrieving clinics within radius
* Retrieving N closest clinics

![Clinic-Service-Connection](https://i.ibb.co/1RMrTbR/Clinic-Connection.png)

 
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

## Code documentation

### Scalability
A comprehensive folder structure that accounts for generalizations and abstractions is necessary for scalability and maintainability. The relations of the folders were designed with the motive to facilitate extensions of the code in the future and to accommodate room for unpredictable changes whereas the self-contained environment adheres to the single responsibility principle.


Below, the microservice will be presented with respect to its three main folders:

![3 main folders](https://i.ibb.co/yRPrzzQ/folder1.png)

First, a table that illustrates and discusses in-depth how changes are accommodated in `TopicManagement` folder is presented. This table also touches on imperative concepts that are similar in its peer folder `DataManagement`. Afterwards, a multitude of trees with nodes brings more light on the existing similarities to highlight a general pattern of sub-folders that is strictly followed as a result of obtaining maintainable code. Lastly, `BackendMapAPI’s` involvement in the microservice and and how its behaviour deviates from its two peer folders is briefly discussed.

### Clarification Table

| DEFINITION | DESCRIPTION | CODE USAGE | FOLDER USAGE | POTENTIAL FUTURE EXTENSIONS |
| ------ | ------ | ------ | ------ | ------ |
|   Topic Artifacts     |    A general type in which responses and requests in the code are handled    |    **Clinic:**<br>Register Clinic<br>Delete Clinic<br>Add Dentist<br>Delete Dentist <br><br> **Map:** Radius range (return clinics within range) <br> Fixed number (return N closest clinics)   |   ![folder pict1](https://i.ibb.co/g9RK18m/folder2.png) <br> ![folder pic2](https://i.ibb.co/ZKmZrbK/folder3.png)     |    **Appointments folder:** A relevant domain that could be added in the future: <br><br> Display appointment information inside infowindow when clicking a clinic marker on the map    |
|    Artifact Subtypes    |    A specific type of an artifact    |    **Dental:** <br> A specific type of _Clinic_ artifact: Clinics with dentists as employees <br><br> **Nearby:** <br> A specific type of _Map_ artifact: Queries returning data-points nearest to a position   |   ![folder pic3](https://i.ibb.co/fY5YvTm/folder4.png)     |    **Clinic subtype:** <br> Adding more sub-types of Clinic artifact implies creating a folder containing the related classes. At the moment we have the folder `Dental`. An example of an extension that supports health clinics would imply creating a folder called `Health` <br><br> **Map subtype:** <br> Other operations that are directly related to returning nearby clinics to the user’s positions are: <br> - A* algorithm <br> - Breadth First Search <br> - Depth First Search <br><br> In the folder structure,we would have to create a new sub-folder `Multiplicity` (returns multiple graph paths) in `MapManagement`, since it's a map operation but distinguished from the functionality of solely returning clinics sorted by one dimension (range from reference position) in its peer folder `Nearby`


### Extending the code
Adding new features would imply that the developer strictly follows the laid out folder structure to keep things organized.

### Tree of extensions

* Black nodes --> Already existing folders
* Red nodes --> Example extensions of folders
* Documents --> Example extensions of scripts

![Extension tree](https://i.ibb.co/mJ0gBLQ/Extension-Tree.png)

### Class diagram extensions
This diagram provides further details on what was adressed in the children nodes of `TopicManagement` in the tree above:

* Green --> Already existing classes
* Yellow --> Demonstrations of further extensions of abstract classes that weren't mentioned in the tree above
* Red --> The red nodes in the tree above

![Class extensions](https://i.ibb.co/n126s1v/Class-Extension.png)


### Code flow

Purpose: Provide high-level overview of the code flow - Not all details (classes / folders) are included

**NOTE:** Only the most significant classes and methods to the codeflow is included in the diagrams

The colors in the 2 diagrams below represent the following operational levels:
* Red = Microservice
* Orange = Segment of service
* Yellow = Artifact
* Green = Artifact subtypes → Where the requested operation occurs and generates a response


#### Code flow: Folders
Keywords of the mqtt topic defines the codeflow trajectory which has its end in the green area
![Folder Code flow tree](https://i.ibb.co/6n09TGn/Code-Flow-Folder-Tree.png)


#### Code flow: Classes
The picture above expressed in `.java` classes rather than folders looks like this:
![Class Code flow tree](https://i.ibb.co/gRQTddL/Code-Flow-Class-Tree.png)


### BackendMapAPI folder


* A self-contained nodejs runtime environment
![nodejs-logo](https://i.ibb.co/NVw6RZQ/Clinic-Service-Nodejs.png)

This folder's structure and behaviour is vastly different from its two peer folders `TopicManagement` and `Datamanagement`, but plays a crucial role in the system.


TODO - Write cohesive text:
* Checks content of `clinic.json`
* `clinic.json` is intended to forward payload data `dentist_name` and `position` to a nodejs runtime environment
* Try to fetch data (ratings, photoURL, address)
* Return via clinic.json

![BackendMapAPI - Communication](https://i.ibb.co/25hf2f7/Backend-Map-API-Communication.png)


BackendMapAPI found clinic and assigned fetched data:
![validated-clinic-pic](https://i.ibb.co/88TPzJm/Ratings-Clinic.png)


![validated-clinic-2](https://i.ibb.co/Px5xYY1/Ratings-Clinic2.png)

BackendMapAPI did not find clinic and could not assign additional data:
![fictitious-clinic-pic](https://i.ibb.co/KqWdq3V/No-Ratings-Clinic.png)


#### Security
TODO: Write section here
* This adds an additional layer of security to the system --> Clinic must be an established well-known coorporation to be registered as an official establishment in the Google API --> These clinics gets additional UI when selecting them on the map (ratings, photo and address) which give them a competitive advantage because they are trust worthy


#### Registering a clinic to the system
TODO: Make this section cohesive

1 out of 2 cases happen:

1. Clinic was found and data fetched


2. Clinic was not found and only employees are displayed on the infowindow

A status code is used (between `TopicManagement` and `BackendMapAPI`)
200 = Existing clinic found
404 = Clinic not found → Fictitious clinic
This status code is used in the conditions deciding the lifecycle of a thread that is listening for a response from the BackendMapAPI nodejs environment
Lastly, whether or not the BackendMapAPI return 200 or 404, the operation was successful in registering a clinic. Hence, the status code returned in the payload to the Patient API is 200.



TODO: Write this into a cohesive text:

Due to naming inconsistencies between Google Maps and Google Maps API
Not identical strings → clinic names,
The following procedure must be followed:
Find the identical string of clinic name that the API checks for:
Use ‘SEARCH’ mode in Patient Client and check the box ‘establishment’ and search for the desired clinic (already existing corporation that wants to use this system)
Find the global coordinates of the clinic as an identifier:
Use Google Maps and search for the clinic
Right click on the marker and copy the lat,lng values
Delete the space character

Recommendation: Select clinics with more than 20 ratings/reviews to ensure that it’s an official establishment registered in Google API’s database
If you did any mistake in these steps, the code will not find the existing clinic and return a ‘fictitious’ one without ratings and pictures


#### Inconsistencies

TODO: Make this section clear and cohesive

Clinic naming inconsistencies between Backend Nodejs API and the public Google Maps API
Places API vs Places API (New) → Free and unlimited usage but has its deficiencies in places available (100m places vs its newer but expensive option with 200m global places)

**Find the identical string that the system checks for:**
Go to ‘SEARCH’ mode and check the ‘establishment’ box
Search for the clinic you want
The autocomplete search-bar generates a string consisting of the establishment’s name and address. Copy paste the name and input it in dentist client
Global coordinates → Google Maps → Right click → Past ein dentist client

**What to do if a clinic owner's clinic isn't found:**
In some cases - The string may be tricky → Contact the customer support (one of the developers) that can find the clinic for you → In the code → bash-api.sh, query.js, printOutputForDevelopers() → Prints the exact strings that the API is validating to fetch data from (in Google API DB)


Developer printing: A way of navigating in the registration of existing clinics

![Developer Printing](https://i.ibb.co/3NHy1S1/Developer-Printing.png)


## Global constraints on clinics displayed

TODO:
* Haversine formula
* Priority queue
* Max heap


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
