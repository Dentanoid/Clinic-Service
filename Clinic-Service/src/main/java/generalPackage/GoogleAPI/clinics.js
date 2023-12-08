var fs = require('fs');

function clinicQuery() {
    console.log('C - In clinic method // mr jex')
}

console.log('A - In clinic.js launch')

function executeGoogleAPIValidationQuery() { // Validate existence of clinic
    console.log('API Performs query here')

    console.log('IF: Clinic was found:')
    // 1) Write to valdiatedClinic.json --> Fetched data (ratings, total_user_ratings, photoURL)

    console.log('ELSE IF: No clinic found')
    // 1) Write to validatedClinic.json: {-1, -1, "-1"}


    console.log('Hardcoded TEMP:')
    /*
        TODO:
        Access 'ratings', 'total_user_ratings' and 'photoURL' attributes from 'data' object
        that the API returns and push them onto 'table'
    */

   var json = JSON.stringify({"ratings": 2.89, "total_user_ratings": 2568, "photoURL": "photo url here"})

   fs.writeFile("GoogleAPI\\validatedClinic.json", json, function(err) {
        if(err) {
            return console.error(err);
        }
        console.log("File saved successfully!");
   });
}

exports.clinicQuery = clinicQuery
exports.executeGoogleAPIValidationQuery = executeGoogleAPIValidationQuery