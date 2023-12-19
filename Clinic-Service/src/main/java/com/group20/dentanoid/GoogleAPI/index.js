const express = require('express')
const app = express();

const path = require('path')

// SERVER: http://localhost:3000/childprocess
app.use('/childprocess', express.static(path.join(__dirname, 'public')))

app.listen(3000, () => {
    console.log("App listening on port 3000")
})

const clinics = require("./clinics.js")
clinics.executeGoogleAPIValidationQuery()