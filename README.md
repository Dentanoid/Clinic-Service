# Clinic Service 🏥
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

6. Search for "Places API" and enable it 🌍
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

## Code documentation 📚

### Scalability 📈
A comprehensive folder structure that accounts for generalizations and abstractions is necessary for scalability and maintainability. The relations of the folders were designed with the motive to facilitate extensions of the code in the future and to accommodate room for unpredictable changes whereas the self-contained environment adheres to the single responsibility principle.


Below, the microservice will be presented with respect to its three main folders:

![3 main folders](https://i.ibb.co/yRPrzzQ/folder1.png)

First, a table that illustrates and discusses in-depth how changes are accommodated in `TopicManagement` folder is presented. This table also touches on imperative concepts that are similar in its peer folder `DataManagement`. Afterwards, a multitude of trees with nodes brings more light on the existing similarities to highlight a general pattern of sub-folders that is strictly followed as a result of obtaining maintainable code. Lastly, `BackendMapAPI’s` involvement in the microservice and and how its behaviour deviates from its two peer folders is briefly discussed.

### Clarification Table 📝

| DEFINITION | DESCRIPTION | CODE USAGE | FOLDER USAGE | POTENTIAL FUTURE EXTENSIONS |
| ------ | ------ | ------ | ------ | ------ |
|   Topic Artifacts     |    A general type in which responses and requests in the code are handled    |    **Clinic:**<br>Register Clinic<br>Delete Clinic<br>Add Dentist<br>Delete Dentist <br><br> **Map:** Radius range (return clinics within range) <br> Fixed number (return N closest clinics)   |   ![folder pict1](https://i.ibb.co/g9RK18m/folder2.png) <br> ![folder pic2](https://i.ibb.co/ZKmZrbK/folder3.png)     |    **Appointments folder:** A relevant domain that could be added in the future: <br><br> Display appointment information inside infowindow when clicking a clinic marker on the map    |
|    Artifact Subtypes    |    A specific type of an artifact    |    **Dental:** <br> A specific type of _Clinic_ artifact: Clinics with dentists as employees <br><br> **Nearby:** <br> A specific type of _Map_ artifact: Queries returning data-points nearest to a position   |   ![folder pic3](https://i.ibb.co/fY5YvTm/folder4.png)     |    **Clinic subtype:** <br> Adding more sub-types of Clinic artifact implies creating a folder containing the related classes. At the moment we have the folder `Dental`. An example of an extension that supports health clinics would imply creating a folder called `Health` <br><br> **Map subtype:** <br> Other operations that are directly related to returning nearby clinics to the user’s positions are: <br> - A* algorithm <br> - Breadth First Search <br> - Depth First Search <br><br> In the folder structure,we would have to create a new sub-folder `Multiplicity` (returns multiple graph paths) in `MapManagement`, since it's a map operation but distinguished from the functionality of solely returning clinics sorted by one dimension (range from reference position) in its peer folder `Nearby`


### Extending the code
Adding new features would imply that the developer strictly follows the laid out folder structure to keep things organized.

### Tree of extensions 🌳

* Black nodes --> Already existing folders
* Red nodes --> Example extensions of folders
* Documents --> Example extensions of scripts

![Extension tree](https://i.ibb.co/mJ0gBLQ/Extension-Tree.png)


The takeaway from the tree above is that `TopicManagement` and `DatabaseManagement/Schemas` must adhere to the following mathematical cardinalities to preserve code maintainability:

* Subfolder A: Contains **n**<sub><sup>**TopicArtifact**</sup></sub> folders (Clinic, Map and Appointment in the tree)
* Subfolder B: Contains **n**<sub><sup>**ArtifactSubType**</sup></sub> folders (Dental, Health, Nearby and Multiplicity in the tree)


### Class diagram extensions 🧩
This diagram provides further details on what was adressed in the children nodes of `TopicManagement` in the tree above:

* Green --> Already existing classes
* Yellow --> Demonstrations of further extensions of abstract classes that weren't mentioned in the tree above
* Red --> The red nodes in the tree above

![Class extensions](https://i.ibb.co/n126s1v/Class-Extension.png)


### Code flow 🔄

Purpose: Provide high-level overview of the code flow - Not all details (classes / folders) are included

**NOTE:** Only the most significant classes and methods to the codeflow are included in the diagrams

The colors in the 2 diagrams below represent the following operational levels:
* Red = Microservice
* Orange = Segment of service
* Yellow = Artifact
* Green = Artifact subtypes → Where the requested operation occurs and generates a response


#### Code flow: Folders 📂
Keywords of the mqtt topic defines the codeflow trajectory which has its end in the green area
![Folder Code flow tree](https://i.ibb.co/6n09TGn/Code-Flow-Folder-Tree.png)


#### Code flow: Classes 🏷️
The picture above expressed in `.java` classes rather than folders looks like this:
![Class Code flow tree](https://i.ibb.co/gRQTddL/Code-Flow-Class-Tree.png)


### BackendMapAPI folder 🗺️

This folder's structure and behaviour is vastly different from its two peer folders `TopicManagement` and `Datamanagement`, but plays a crucial role in the system. This folder is a self-contained nodejs runtime environment that acts as a childprocess executed when registering a clinic.

![nodejs-logo](https://i.ibb.co/NVw6RZQ/Clinic-Service-Nodejs.png)

In essence, its responsibilities are the following:
* Read `clinic.json` content (contains `clinic_name` and `position`)
* Perform a query using the attributes to find the desired real-world clinic
* Fetch the clinic's data (ratings, photoURL, address) and return to `TopicManagement` folder via `clinic.json`

**NOTE:** In the picture below the three folders are portrayed as independent services, which isn't the case. This is a high-level overview that conveys `TopicManagement`'s central role in the service as a whole and how it interacts with its peer folders. Nevertheless, it's important to distinguish between its issued requests. As stated above, `clinic.json` establishes a way of communicating to `BackendMapAPI`. However, `DatabaseManagement`'s involvement in the entirety of the service is observed in the form of an aggregation to `TopicManagement`, since its methods are called from its peer folder's classes.

![BackendMapAPI - Communication](https://i.ibb.co/25hf2f7/Backend-Map-API-Communication.png)


#### Registering a clinic to the system

As imagined, there are cases where a real-world clinic either isn't registered in Google API's database or when the inputted attributes don't link to any dental clinic. Ultimately, this creates two scenarios:

* **Scenario 1 - Real clinic:** Clinic was found and fetched data is displayed on infowindow

* **Scenario 2 - Fictitious clinic:** Clinic was not found and only employees are displayed on the infowindow


**Scenario 1 - Real clinic:**

![validated-clinic-pic](https://i.ibb.co/88TPzJm/Ratings-Clinic.png)


![validated-clinic-2](https://i.ibb.co/Px5xYY1/Ratings-Clinic2.png)

![validated-clinic-3](https://i.ibb.co/R62TXXC/Australian-Clinic.png)

![validated-clinic4](https://i.ibb.co/Y83PHBJ/Dubai-Clinic.png)


**Scenario 2 - Fictitious clinic:**

![fictitious-clinic-pic](https://i.ibb.co/KqWdq3V/No-Ratings-Clinic.png)


#### Finding a real clinic to register

When wanting to register a real-world clinic, two things must be inputted correctly:
* **clinic_name:** The string must be identical to what Google Maps API is operating with
* **position:** The global coordinates of the clinic must be inputted in a `,` separated string. Although the code accounts for margin of error and doesn't expect a pinpoint exact coordinate, it does require an approximate accuracy to find the desired clinic


Follow the procedure detailed below:

1. Open [Google Maps](https://www.google.com/maps/@59.804951,13.1472277,6z?entry=ttu)

2. Search for desired clinic (**clinic_name**)

3. Zoom in on the red marker

4. Right click on it to retrieve its coordinates (**position**)

5. Input the two attributes in `Dentist Client` component

(picture here)


**Recommendation:** Select clinics with more than 20 ratings/reviews to ensure that it’s an official establishment registered in Google API’s database
If you did any mistake in these steps, the code will not find the existing clinic and return a ‘fictitious’ one without ratings and pictures

**NOTE:** `Google Places API` does not support all clinics that [Google Maps](https://www.google.com/maps/@59.804951,13.1472277,6z?entry=ttu) does. The API has 100 million places in its database, which is a lot, but it doesn't cover all worldwide dental clinic establishments.

**If the clinic isn't found:** In some cases the string in which `Places API` expects is tricky. Even though the clinic is registered, this may be the cause you are experiencing issues. If that's the case, contact one of the developers. Each developer that has access to modify the code can uncomment the code-block in `childprocess-api.sh` so that its shell-window doesn't disappear after its execution is accomplished. In the window, the `clinic_name` strings of the clinics with similar coordinates are printed. These strings are the ones that the API checks for, and the output looks like this:

![Developer Printing](https://i.ibb.co/3NHy1S1/Developer-Printing.png)


#### Security

The feature of fetching data from real-world clinics can be extended as an additional layer of security to the system. By disallowing `Scenario 2 - Fictitious clinic` so that only established dental-clinic-coorporations stored in Google APIs database can be registered, scammers or clinics with bad user experience are rejected by the system. Clinics that aren't well-known (which tend to struggle with customer satisfaction) will most likely not be found by Google API. In this way, the system can leverage upon trustworthiness as a factor toward success. In addition, if the developers wish to make the system public and build a reputation of providing value beyond the users' expectations, they can define a star-rating threshold at which dental clinics are accepted to register themselves. Usally, high ratings indicate happy customers that boost the reputation and influence of the providing enterprise.

## Global constraints on clinics displayed on map

The system is capable of retrieving clinics regardless of global coordinates and send them back as a reponse to `Patient Client` where they are displayed. Two modes support this feature:
* **Radius:** Returns the clinics that are within a specified circular radius from a position. The position is usually set to the user's current global coordinates, but it can also represent the searched position if `Search mode` is activated in `Patient Client`. The upper limit of clinics that can be returned is **X**, where **X** is the number of existing clinics in the database. If no clinics are found within the radius an empty array is returned. Hence, the unpredictable number of clinics returned (_**A**_) is constrained accordingly:
```
0 <= A <= X
``````
* **Fixed:** Returns the **N** closest clinics to a position. **N** is a positive integer defined by the user's request. The upper limit is unbounded, meaning that it depends on the datatype that is used in the code. As for now, `Integer` is used, but it can easily be changed to `long` or `double` that store a greater range numerical values. In cases where the number of existing clinics in the database (_**n**_) is less than **N**, a response containing **n** clinics is issued. Note that `n >= 0` always holds true. As a result, the return statement is numerically bounded as follows:

```
n <= A <= N
``````

Below, a surface-level overview of the more interesting solution `Fixed` is provided:

In order to implement this feature, two steps needs to be done:

**1.** Calculate distance between user's position and a clinic

**2.** Store clinics that satisfy the criteria in a datastructure

### Step 1: Haversine Formula vs Euclidean Distance

Calculating the distance between two global points can be done in countless different ways. The question is what approach is best suited for this project. The developers had two ideas in mind: **Euclidiean Distance** and **Haversine Formula**.

#### Euclidiean Distance - Straight line between two points

![Euclidean-Distance](https://i.ibb.co/bJHSyyX/Euc-Formula3.png)


#### Haversine Formula - Spherical distance:

![earth-pic](https://i.ibb.co/nQw3yYs/Round-Earth-Haversine-Euc.png)

![haversine-formula](https://i.ibb.co/smpCgmL/Haversine-Formula.png)

In the end, the developers picked **Haversine Formula** by virtue of its inclusion of the radius of a sphere. Setting the radius variable to that of the Earth's, a comparison between distances worldwide is made possible.


### Step 2: Priority Queue & Max Heap

In the process of calculating the distances, the clinics are stored in a map with their distances as the key, and the entire clinic-document as a value. A priority queue with a max heap is used with a max-length of **N**. Adding elements when the queue had N elements would induce a `poll()` that deletes the element with the biggest key-value. 


![Binary-Heap](https://i.ibb.co/1vkCRD1/Binary-Heap.png)


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
