const express= require('express');
const app=express();
const port=process.env.PORT||7000;
const attendance = require('./router/attendance');
const login=require('./router/login');
const download=require('./router/download');
const importdata=require('./router/importdata');



///////////////////////////////////////////////////////
login(app);
importdata(app);
download(app);
attendance(app);
app.listen(7000);
console.log("app is lisntening on port "+port);
