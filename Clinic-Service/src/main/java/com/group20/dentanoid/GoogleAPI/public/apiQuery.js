let map;
let service;
let clinicNameRequest;

function initMap() {
  // Keep in mind: Reading from validatedClinic.json has to happen after its content is updated from DentalClinic.java
  fetch('./validatedClinic.json')
  .then(response => response.json())
  .then(clinicRequestData => {
    simulateMap(clinicRequestData)
  })
  .catch(error => console.error('Error fetching JSON:', error));
}

// Generate the back-end map dependencies to access global coordinates of its nodes (dental clinics)
function simulateMap(clinicRequestData) {
/*
 TODO:
 1) Research how to write to JSON text file in a similar way as the technique above, where 'require fs module' isn't needed
*/
  
clinicNameRequest = clinicRequestData.clinic_name
  const payloadCoordinates = clinicRequestData.position.split(',')

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
}

function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
      const potentialClinic = results[i].name

      if (potentialClinic === clinicNameRequest) {
        console.warn('CLINIC VALIDATED!')
      }
    }
  }
}

window.initMap = initMap;