const express = require('express')
const app = express();

const path = require('path')

// SERVER: http://localhost:3000/childprocess
app.use('/childprocess', express.static(path.join(__dirname, 'public')))

app.listen(3000, () => {
    console.log("App listening on port 3000")
})

app.post('/childprocess', async function (req, res, next) {
    try {
        // fs.writeFile()   
        // const user = req.body;
        // await User.create(user);
        // res.status(201).json(user);
    }
    catch (error) {
        next(error)
    }
})



const clinics = require("./clinics.js")
clinics.executeGoogleAPIValidationQuery()

// app.get() { await apiQuery.methodOrVariableRetrievalHere }

/*
// TODO: fetch data from browser
const BASE_URL = 'http://localhost:3000/childprocess'; // PREVIOUS: 'https://jsonplaceholder.typicode.com'

const getTodoItems = async () => {
  try {
    const response = await axios.get(`${BASE_URL}`); // PREVIOUS: `${BASE_URL}/todos?_limit=5`
    const todoItems = response.data;

    console.log(`GET: Here's the list of todos`, todoItems);

    return todoItems;
  } catch (errors) {
    console.error(errors);
  }
};

const main = async () => {

    console.warn('main1')
    // updateTodoList(await getTodoItems());
    const clinicDataVariable = await getTodoItems()
    console.warn('main2')
};

main();
*/