let map;
let service;
let infowindow;

function initMap() {
  const pyrmont = new google.maps.LatLng(57.708870, 11.974560);

  map = new google.maps.Map(document.getElementById('map'), {
      center: pyrmont,
      zoom: 15
  });

  var request = {
    location: pyrmont,
    radius: '400',
    type: ['dentist']
  };

  service = new google.maps.places.PlacesService(map);
  service.nearbySearch(request, callback);
}

function callback(results, status) {
  console.warn('in callback()')
  console.warn(results.length)
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
      console.warn(results[i])

      // TODO: Check if 'results[i].name' === payload name
      console.warn(results[i].name)
    }
  }
}

window.initMap = initMap;