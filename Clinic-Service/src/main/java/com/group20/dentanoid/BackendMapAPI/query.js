const { Client } = require("@googlemaps/google-maps-services-js");
const client = new Client({});

const fs = require('fs')
const { writeFile } = require('fs')
let payloadObject

/*
// Assuming the dental clinic owner doesn't include all the decimals when inputting the clinic's location,
the value below measured in KM serves as the margin of error
*/
const radiusRange = '300'

// TODO: Make .env file with variable work and replace it with key-string below --> process.env.GOOGLE_MAPS_API_KEY

  fs.readFile("./BackendMapAPI/clinic.json", "utf8", (error, data) => {
    if (error) {
      console.log(error);
    }

    // const payloadObject = JSON.parse(data)
    payloadObject = JSON.parse(data)

    // TODO: Make function 'readCoordinates()'
    const convertedCoordinates = payloadObject.position.split(',')
    const payloadCoordinates = { lat: convertedCoordinates[0], lng: convertedCoordinates[1] }

    client
    .placesNearby({
      params: {
          location: payloadCoordinates,
          radius: radiusRange,
          type: ['dentist'],
          key: 'AIzaSyAddMO3fsDTHWzDI0uEG-ZFobf8NY7teBA'
        },
        timeout: 1000,
    })
    .then((r) => {

      payloadObject["status"] = 404

      r.data.results.forEach((currentClinic) => {
        printOutputForDevelopers(currentClinic)

          if (currentClinic.name === payloadObject.clinic_name) { // Clinic found
            fetchData(currentClinic)
          }
      })

      writeFile("./BackendMapAPI/clinic.json", JSON.stringify(payloadObject, null, 2), (err) => {
        if (err) {
          console.log('Failed to write updated data to file');
          return;
        }
        console.log('Updated file successfully');
      });
    })
    .catch((e) => {
      console.log(e.response.data.error_message);
    });
  });

function fetchData(clinic) {
  payloadObject["ratings"] = clinic.rating.toString()
  payloadObject["photoURL"] = getPhotoUrl(clinic.photos[0].photo_reference)
  payloadObject["address"] = clinic.vicinity
  payloadObject["status"] = 200
}

function getPhotoUrl(photoReference) {
    const apiKey = 'AIzaSyAddMO3fsDTHWzDI0uEG-ZFobf8NY7teBA'
    return `https://maps.googleapis.com/maps/api/place/photo?photoreference=${photoReference}&sensor=false&maxheight=${80}&maxwidth=${120}&key=${apiKey}`
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