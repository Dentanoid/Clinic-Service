let map;
let service;
let infowindow;
// let myTestVariable = 55

function integrateAPIKey() {
  (g=>{var h,a,k,p="The Google Maps JavaScript API",c="google",l="importLibrary",q="__ib__",m=document,b=window;b=b[c]||(b[c]={});var d=b.maps||(b.maps={}),r=new Set,e=new URLSearchParams,u=()=>h||(h=new Promise(async(f,n)=>{await (a=m.createElement("script"));e.set("libraries",[...r]+"");for(k in g)e.set(k.replace(/[A-Z]/g,t=>"_"+t[0].toLowerCase()),g[k]);e.set("callback",c+".maps."+q);a.src=`https://maps.${c}apis.com/maps/api/js?`+e;d[q]=f;a.onerror=()=>h=n(Error(p+" could not load."));a.nonce=m.querySelector("script[nonce]")?.nonce||"";m.head.append(a)}));d[l]?console.warn(p+" only loads once. Ignoring:",g):d[l]=(f,...n)=>r.add(f)&&u().then(()=>d[l](f,...n))})({
    key: "AIzaSyAddMO3fsDTHWzDI0uEG-ZFobf8NY7teBA",
    v: "weekly",
    // Use the 'v' parameter to indicate the version to use (weekly, beta, alpha, etc.).
    // Add other bootstrap parameters as needed, using camel case.
  });
}

// exports.myTestVariable = myTestVariable

// --------------

async function initMap() {

  console.warn('initMap()')
  // The location of Uluru
  const position = { lat: -25.344, lng: 131.031 }; // TODO: Read payload clinic_position here

  //@ts-ignore
  const { Map } = await google.maps.importLibrary("maps");
  const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
  const { Place } = await google.maps.importLibrary("places");

  map = new Map(document.getElementById("map"), {
    zoom: 4,
    center: position,
    mapId: "DEMO_MAP_ID",
  });

  const marker = new AdvancedMarkerElement({
    map: map,
    position: position,
    title: "Uluru",
  });

  var request = {
    location: position,
    radius: '500',
    type: ['restaurant'] // 'dentist'
  };

  console.warn('nearbySearch1')
  // const { places } = await Place.searchByText(request); // Place.searchByText() not available
  console.warn(places.length)
  console.warn(places)

  /*
  service = new google.maps.places.PlacesService(map); // 'PlacesService' undefined
  service.nearbySearch(request, callback);
  */

  console.warn('nearbySearch2')
}



// ------------ NEW ----------------

// initMap1();


// -------------- SEARCH (WORKS) ------------------
/*
function initMap() {
  console.warn('initmap() - Search')
  const sydney = new google.maps.LatLng(-33.867, 151.195);

  infowindow = new google.maps.InfoWindow();
  map = new google.maps.Map(document.getElementById("map"), {
    center: sydney,
    zoom: 15,
  });

  const request = { // WORKS, but the search query isn't optimal: Try using nearby query with positions, 'dentist', radius and possibly name
    query: "the boys", // Museum of Contemporary Art Australia
    fields: ["name", "geometry"]
  };

  service = new google.maps.places.PlacesService(map);
  service.findPlaceFromQuery(request, (results, status) => {
    if (status === google.maps.places.PlacesServiceStatus.OK && results) {
      for (let i = 0; i < results.length; i++) {
        console.warn(results[i])
        createMarker(results[i]);
      }

      map.setCenter(results[0].geometry.location);
    }
  });
}

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

window.initMap = initMap;
*/


// --------------NEARBY (DOESN'T WORK) -------------
/*
async function initMap() { // if error: change name to initMap()
  // var pyrmont = new google.maps.LatLng(-33.8665433,151.1956316);
  const pyrmont = { lat: -33.8665433, lng: 151.1956316 };

  map = new google.maps.Map(document.getElementById('map'), {
      center: pyrmont,
      zoom: 15
    });

  var request = {
    location: pyrmont,
    radius: '500',
    type: ['restaurant']
  };

  console.warn('NEARBY REQUEST:')

  service = new google.maps.places.PlacesService(map); // 'PlacesService undefined'
  service.nearbySearch(request, callback);
}
*/

function callback(results, status) {
  console.warn('in callback()')
  console.warn(results.length)
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
      console.warn(results[i])
      createMarker(results[i]);
    }
  }
}


initMap()