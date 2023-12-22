var fs = require('fs');
var validatedClinic = require('./public/validatedClinic.json')

// var apiQuery = require('./public/apiQuery.js')

console.log('in clinics.js')


// SOLUTION (BAD):
// 1) Setinterval(x) --> check is attributes in validatedClinic.json has changed --> Either check existing attributes '-1' or 'ratings = 4.6', or create a new attribute: 'clinicQuried: true/false'
// 2) If attribute value indicates that apiQuery.js has completed its query, then fs.write() to validatedClinic.json

// SOLUTION (GOOD):
// Run async/await method to wait for the clinic's response

function executeGoogleAPIValidationQuery() {

    // IDEA: Async & await - Run apiQuery.js from here

    // READ payload clinic values
    /*
    console.log('---------------------------------')
    console.log(validatedClinic)
    console.log('---------------------------------')
    */


    // TODO - CASES:
    // console.log('IF: Clinic was found:') // --> Write to valdiatedClinic.json --> Fetched data (ratings, total_user_ratings, photoURL)
    // console.log('ELSE IF: No clinic found') // --> Write to validatedClinic.json: {-1, -1, "-1"}
    

    // If clinic was found, return all attributes (currently hardcoded)
    /*
    var json = JSON.stringify(
    {
        "clinic_name": "MyClinicName",
        "clinic_id": "my_clinic_id43",
        "position": "22.34,22.56",
        "employees": [],
        "ratings": 1.89, //                <---    API: Validated clinic
        "total_user_ratings": 1169, //     <---    API: Validated clinic
        "photoURL": "photo url here" //    <---    API: Validated clinic
    })

   fs.writeFile("GoogleAPI\\public\\validatedClinic.json", json, function(err) {
    if(err) {
        return console.error(err);
    }
   });
   */
}

exports.executeGoogleAPIValidationQuery = executeGoogleAPIValidationQuery