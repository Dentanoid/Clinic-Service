var fs = require('fs');
var validatedClinic = require('./validatedClinic.json')
var apiQuery = require('./apiQuery.js')

function clinicQuery() {
    console.log('C - In clinic method // mr jex')
}

console.log('A - In clinic.js launch')

function executeGoogleAPIValidationQuery() {
    console.log('API Performs query here')

    // READ payload clinic values
    console.log('---------------------------------')
    console.log(validatedClinic)
    console.log(validatedClinic.clinic_name)
    console.log('---------------------------------')

    // IN PROGRES:
    // console.log(document.getElementById('myTest')) // 'document' not defined
    // apiQuery.initMap()


    // CASES IN DEVELOPMENT:
    // console.log('IF: Clinic was found:') // --> Write to valdiatedClinic.json --> Fetched data (ratings, total_user_ratings, photoURL)
    // console.log('ELSE IF: No clinic found') // --> Write to validatedClinic.json: {-1, -1, "-1"}
    

    // TODO: Access 'ratings', 'total_user_ratings' and 'photoURL' attributes from 'data' object

    // Current hardcoded solution ()
    // If clinic was found, return all attributes (currently hardcoded)
    var json = JSON.stringify(
    {
        "clinic_name": "my_clinic_name0",
        "clinic_id": "my_clinic_id",
        "position": "50.34,13.56",
        "employees": [],
        "ratings": 4.89, //                <---    API: Validated clinic
        "total_user_ratings": 1569, //     <---    API: Validated clinic
        "photoURL": "photo url here" //    <---    API: Validated clinic
    })

   fs.writeFile("GoogleAPI\\validatedClinic.json", json, function(err) {
        if(err) {
            return console.error(err);
        }
        console.log("File saved successfully!");
   });
}

exports.clinicQuery = clinicQuery
exports.executeGoogleAPIValidationQuery = executeGoogleAPIValidationQuery