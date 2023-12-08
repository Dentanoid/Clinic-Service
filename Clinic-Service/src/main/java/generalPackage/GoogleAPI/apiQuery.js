let map;
let service;
let infowindow;

function initMap() { // Search for registered dental clinic with specified paramaters in payload
    const sydney = new google.maps.LatLng(-33.867, 151.195); // 'google' not defined

    infowindow = new google.maps.InfoWindow();
    map = new google.maps.Map(document.getElementById("map"), {
      center: sydney,
      zoom: 15, // redundant
    });
  
    const request = {
      query: "Museum of Contemporary Art Australia",
      fields: ["name", "geometry"],
    };
  
    service = new google.maps.places.PlacesService(map);
    service.findPlaceFromQuery(request, (results, status) => {
      if (status === google.maps.places.PlacesServiceStatus.OK && results) {
        for (let i = 0; i < results.length; i++) {
          console.log(results[i])
          // createMarker(results[i]);
        }
  
        // map.setCenter(results[0].geometry.location);
      }
    });

    window.initMap = initMap; //
}

/*
function createMarker(place) {
    if (!place.geometry || !place.geometry.location) return;
  
    const marker = new google.maps.Marker({
      map,
      position: place.geometry.location,
    });
  
    google.maps.event.addListener(marker, "click", () => {
      infowindow.setContent(place.name || "");
      infowindow.open(map);
    });
}
*/
exports.initMap = initMap