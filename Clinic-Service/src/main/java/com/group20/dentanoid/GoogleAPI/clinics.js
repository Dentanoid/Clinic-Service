var fs = require('fs');
var validatedClinic = require('./public/validatedClinic.json')

function executeGoogleAPIValidationQuery() {

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