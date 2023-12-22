let map;
let service;
let clinicNameRequest;
let clinicIsValidated;
let validatedClinicData = []
let myTestVariable = 4

function initMap() {
  fetch('./validatedClinic.json')
  .then(response => response.json())
  .then(clinicRequestData => {
    simulateMap(clinicRequestData)
  })
  .catch(error => console.error('Error fetching JSON:', error));
}

// Generate the back-end map dependencies to access global coordinates of its nodes (dental clinics)
function simulateMap(clinicRequestData) {
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

async function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {

    clinicIsValidated = false

    for (var i = 0; i < results.length; i++) {
      const potentialClinic = results[i]

      if (potentialClinic.name === clinicNameRequest) {
        console.warn('CLINIC VALIDATED!')
        console.warn(potentialClinic)
        clinicIsValidated = true

        // TODO: Add address: 'vicinity' and possibly 'opening_hours'
        validatedClinicData.push(potentialClinic.rating)
        validatedClinicData.push(potentialClinic.user_ratings_total)
        validatedClinicData.push(potentialClinic.photos[0].getUrl())
        console.warn(validatedClinicData)

        fetch('./validatedClinic.json')
        .then(response => response.json())
        .then(clinicRequestData => {
          clinicRequestData["ratings"] = potentialClinic.rating
          clinicRequestData["total_user_ratings"] = potentialClinic.user_ratings_total
          clinicRequestData["photoURL"] = potentialClinic.photos[0].getUrl()

          const jsonToReturn = clinicRequestData
          console.warn(jsonToReturn)

          // TODO:
          // 1) Store 'jsonToReturn' in localStorage
          // 2) In 'clinics.js' where nodejs runtime environment is, we do 'fs.Writefile(jsonToReturn)' and update 'validatedClinic.json'
        })
        .catch(error => console.error('Error fetching JSON:', error));


        /*
        let myJsonTest = 'Joel'
        var textBlob = new Blob([myJsonTest], {type: 'application/json'}); // type: 'text/plain' WORKS
        console.warn(textBlob)
        console.warn(textBlob.toString())

        const link = document.querySelector('#temp');
        link.setAttribute('href', URL.createObjectURL(textBlob));
        link.setAttribute('download', `${name.toLowerCase()}.json`);

        const test = link.attributes
        console.warn(test)
        */

        // let [fileHandle] = await window
        // writeFile(fileHandle, 'my json content')

        // IDEA: axios.post to clinics.js
        // http://localhost:3000/childprocess

        /*
        let data = { a: 'aaa' , b: 'bbb' }
        let blob = new Blob([JSON.stringify(data)], { type: 'application/json' })
        saveAs(blob, 'export.json')
        */


        // IDEA: Launch .java file located in 'public' folder that writes to file

        /*
        const file=fopen('./validatedClinic.json', 0); // 'fopen' not defined
        const str = fread(file,flength(file))

        console.warn(str)

        file = fopen('./validatedClinic.json', 3);// opens the file for writing
        fwrite(file, str);// str is the content that is to be written into the file.
        */
      }
    }
  }
}

async function writeFile(fileHandle, contents) {
  // Create a FileSystemWritableFileStream to write to.
  const writable = await fileHandle.createWritable();

  // Write the contents of the file to the stream.
  await writable.write(contents);

  // Close the file and write the contents to disk.
  await writable.close();
}

function myTestFunc() {
  console.log('in myTestFunc() - apiQuery.js')
  return 5
}

window.initMap = initMap;
