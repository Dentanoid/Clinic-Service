console.log('query.js1')

const {Client} = require("@googlemaps/google-maps-services-js");

console.log('query.js2')

const client = new Client({});

console.log('query.js3')

// TODO: Make .env file with variable work and replace it with key-string below --> process.env.GOOGLE_MAPS_API_KEY

client
  .elevation({
    params: {
      locations: [{ lat: 40, lng: -102 }], // { lat: 45, lng: -110 }
      key: 'AIzaSyAddMO3fsDTHWzDI0uEG-ZFobf8NY7teBA',
    },
    timeout: 1000, // milliseconds
  })
  .then((r) => {
    console.log(r.data.results[0].elevation);
  })
  .catch((e) => {
    console.log(e.response.data.error_message);
  });

  /*
      request: {
        location: testCoordinates,
        radius: '300',
        type: ['dentist']
      }
  */

  const testCoordinates = { lat: 57.708870, lng: 11.974560 }

  // TODO: Research nodejs GoogleAPI nearby search implementation --> Github repo?
  client
  .placesNearby({
    params: {
        location: testCoordinates,
        radius: '300',
        type: ['dentist'],
        key: 'AIzaSyAddMO3fsDTHWzDI0uEG-ZFobf8NY7teBA'
      },
      timeout: 1000, // milliseconds
  })
  .then((r) => {
    console.log('yes')
    console.log(r.data);
    console.log(r.results)
  })
  .catch((e) => {
    console.log(e.response.data.error_message);
  });

console.log('query.js3')