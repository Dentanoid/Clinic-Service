const { Client } = require("@googlemaps/google-maps-services-js");
const client = new Client({});

const fs = require('fs')
const { writeFile } = require('fs') // Necessary?

// TODO: Make .env file with variable work and replace it with key-string below --> process.env.GOOGLE_MAPS_API_KEY

  fs.readFile("./nodejsTest/clinic.json", "utf8", (error, data) => {
    if (error) {
      console.log(error);
    }

    const payloadObject = JSON.parse(data)
    const convertedCoordinates = payloadObject.position.split(',')
    const payloadCoordinates = { lat: convertedCoordinates[0], lng: convertedCoordinates[1] }

    client
    .placesNearby({
      params: {
          location: payloadCoordinates,
          radius: '300',
          type: ['dentist'],
          key: 'AIzaSyAddMO3fsDTHWzDI0uEG-ZFobf8NY7teBA'
        },
        timeout: 1000,
    })
    .then((r) => {

      payloadObject["status"] = 404

      r.data.results.forEach((currentClinic) => {
          if (currentClinic.name === payloadObject.clinic_name) {
              payloadObject["ratings"] = currentClinic.rating.toString() // PREVIOUS: currentClinic.rating
              payloadObject["total_user_ratings"] = currentClinic.user_ratings_total
              // payloadObject["photoURL"] = getPhotoUrl(currentClinic.photos[0]) // TODO
              payloadObject["status"] = 200
          }
      })

      writeFile("./nodejsTest/clinic.json", JSON.stringify(payloadObject, null, 2), (err) => {
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



function getPhotoUrl(photo) { // TODO
    /*
    https://maps.googleapis.com/maps/api/place/photo?photoreference=PHOTO_REFERENCE&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=YOUR_API_KEY
    */
}