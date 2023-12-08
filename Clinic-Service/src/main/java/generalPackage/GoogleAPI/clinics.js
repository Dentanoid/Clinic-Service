var fs = require('fs');
var validatedClinic = require('./validatedClinic.json')

function clinicQuery() {
    console.log('C - In clinic method // mr jex')
}

console.log('A - In clinic.js launch')

function executeGoogleAPIValidationQuery() { // Validate existence of clinic
    console.log('API Performs query here')

    // READ payload clinic values
    console.log('---------------------------------')
    console.log(validatedClinic)
    console.log(validatedClinic.clinic_name)
    console.log('---------------------------------')

    // Use Google API to query according to 'validatedClinic' object's attributes

    console.log('IF: Clinic was found:')
    // 1) Write to valdiatedClinic.json --> Fetched data (ratings, total_user_ratings, photoURL)

    console.log('ELSE IF: No clinic found')
    // 1) Write to validatedClinic.json: {-1, -1, "-1"}


    // console.log('Hardcoded TEMP:')
    /*
        TODO:
        Access 'ratings', 'total_user_ratings' and 'photoURL' attributes from 'data' object
    */

    // If clinic was found, return all attributes (currently hardcoded)
    var json = JSON.stringify(
    {
        "clinic_name": "my_clinic_name0",
        "clinic_id": "my_clinic_id",
        "position": "50.34,13.56",
        "employees": [],
        "ratings": 4.89,
        "total_user_ratings": 1569,
        "photoURL": "photo url here"
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