const express= require('express');
const app=express();
//const port=process.env.PORT||7000;
const attendance = require('./router/attendance');
attendance(app)
app.listen(7000)