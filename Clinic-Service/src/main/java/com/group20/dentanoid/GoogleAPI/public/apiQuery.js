let map;
let service;

function initMap() {
  // Keep in mind: Reading from validatedClinic.json has to happen after its content is updated from DentalClinic.java
  fetch('./validatedClinic.json')
  .then(response => response.json())
  .then(clinicRequestData => {
    console.warn(clinicRequestData)
  
    const payloadCoordinates = clinicRequestData.position.split(',')
  
  /*
   TODO:
   2) Read name of clinic and check it in 'callback()'
   3) Research how to write to JSON text file in a similar way as the technique above, where 'require fs module' isn't needed
  */

   const clinicCoordinates = new google.maps.LatLng(payloadCoordinates[0], payloadCoordinates[1]);

   // Generate the back-end map to access global coordinates of its nodes (dental clinics)
   map = new google.maps.Map(document.getElementById('map'), {
       center: clinicCoordinates
   });
 
   var request = {
     location: clinicCoordinates,
     radius: '300',
     type: ['dentist']
   };
 
   service = new google.maps.places.PlacesService(map);
   service.nearbySearch(request, callback);

  })
  .catch(error => console.error('Error fetching JSON:', error));
}

function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {

      console.warn(results[i])

      // TODO: Check if 'results[i].name' === payload name
      console.warn(results[i].name)
    }
  }
}

window.initMap = initMap;