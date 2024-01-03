require('dotenv').config()
const { Client } = require("@googlemaps/google-maps-services-js");
const client = new Client({});
const fs = require('fs')
const { writeFile } = require('fs')

/*
// Assuming the dental clinic owner doesn't include all the decimals when inputting the clinic's location,
the value below measured in KM serves as the margin of error
*/
const radiusRange = '300'

// Measured in pixels
const photoHeight = 80
const photoWidth = 120

let payloadObject

console.log(process.env.GOOGLE_MAPS_API_KEY)

fs.readFile("./clinic.json", "utf8", (error, data) => {
  if (error) {
    console.log(error);
  }

  payloadObject = JSON.parse(data)

  client
  .placesNearby({ // Define parameters to use in the request
    params: {
        location: getClinicCoordinates(),
        radius: radiusRange,
        type: ['dentist'],
        key: process.env.GOOGLE_MAPS_API_KEY
      },
      timeout: 1000,
  })
  .then((r) => { // If a response was successfully recieved

    payloadObject["status"] = 404

    r.data.results.forEach((currentClinic) => { // Iterate through each clinic that was returned in the response
      printOutputForDevelopers(currentClinic)

        if (currentClinic.name === payloadObject.clinic_name) { // If the specified clinic was found, we fetch its data
          fetchData(currentClinic)
        }
    })

    // Write to the JSON file as a way of communicating with DentalClinic.java on the state of the query
    writeFile("./clinic.json", JSON.stringify(payloadObject, null, 2), (err) => {
      if (err) {
        console.log('Failed to write updated data to file')
      }
      console.log('Updated file successfully')
    });
  })
  .catch((e) => {
    console.log(e.response.data.error_message);
  });
});

// Fetch data in an object that will be sent as a response in a seperate JSON file.
function fetchData(clinic) {
  if (clinic.rating) {
    payloadObject["ratings"] = clinic.rating.toString()
  }

  if (clinic.photos) {
    payloadObject["photoURL"] = getPhotoUrl(clinic.photos[0].photo_reference)
  }

  if (clinic.vicinity) {
    payloadObject["address"] = clinic.vicinity
  }

  payloadObject["status"] = 200
}

function getPhotoUrl(photoReference) {
  return `https://maps.googleapis.com/maps/api/place/photo?photoreference=${photoReference}&sensor=false&maxheight=${photoHeight}&maxwidth=${photoWidth}&key=${process.env.GOOGLE_MAPS_API_KEY}`
}

/*
 If developers wishes to see output, uncomment the block in childprocess-api.sh
 to set the duration of the terminal's display from its execution time (approximately 0.1s)
 to 20s. The developer may want to see the output in the following scenarios:

    1) Failed attempt to fetch data from clinic:
       The inputted name of the clinic is not identical to the string that the API checks for
    
    2) The search for existing clinics to easily fetch data from
*/
function printOutputForDevelopers(clinic) {
  console.log('--------------------------')
  console.log(clinic.name)
  console.log('--------------------------')
}

// Return the inputted clinic's global coordinates from payload
function getClinicCoordinates() {
  const convertedCoordinates = payloadObject.position.split(',')
  return { lat: convertedCoordinates[0], lng: convertedCoordinates[1] }
}