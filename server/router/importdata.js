var fs=require('fs');
var PDFParser=require('pdf2json');
var bodyParser=require('body-parser');
const db = require('./config');
//var ecethird=require('./resource/ece/third.json');
//var csethird=require('./resource/cse/third.json');
//var path = osHomedir();
//var homepath = path.replace(new RegExp('\\' + path.sep, 'g'), '/');
//var pdfFilePath = homepath + '/Downloads/' + filename.pdf;
var pdfFilePath='./resource/cse/check.pdf';
var objcse;
var objece;

module.exports=function(app){
  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded( {extended: true}));

  app.get('/home/importdata/ece/3',function(req,res){
    var appData={}
    var ecelength3=0;
    fs.readFile('./resource/ece/third.json', 'utf8', function (err, data) {
        if (err) throw err;
        objece = JSON.parse(data);
        ecelength3=objece.length;
        var rollno;
        var status="R";
        var classs="E123";
        var group="E1";

        for(var i=0;i<ecelength3;i++)
        {
        rollno=objece[i].key_1;
        sno=objece[i].key_0;
        if(rollno[10]==7)
        {
            status="L";
        }
        else
        {
         status="R"; 
        }
        if(sno>=21)
        {
          classs="E123";
          group="E2"
        }
        var userData={
            "class":classs,
            "group":group,
            "rollno":objece[i].key_1,
            "name":objece[i].key_2,
            "status":status,
        }
          
        db.con.query('INSERT INTO studentece SET ?', userData, function(err, rows, fields) {
            if (!err) { 
              appData.error = 0;
              appData["data"] = "student registered successfully!";
            } 
            else {
              appData.error = 1;
              appData["data"] = "unsuccessfull";
              res.json(appData);
            }
          });

      }
    });
    res.send("Done");

  });
  app.get('/home/importdata/cse/3',function(req,res){
    var appData={}
    var cselength3=0;
    fs.readFile('./resource/cse/third.json', 'utf8', function (err, data) {
        if (err) throw err;
        objcse = JSON.parse(data);
        cselength3=objcse.length;
        var rollno;
        var status="R";
        var classs="C123";
        var group="C1";

        for(var i=0;i<cselength3;i++)
        {
        rollno=objcse[i].key_1;
        sno=objcse[i].key_0;
        if(rollno[10]==7)
        {
            status="L";
        }
        else
        {
         status="R"; 
        }
        if(sno>=21)
        {
          classs="C123";
          group="C2"
        }
        if(sno>=42){
          classs="C123";
          group="C3"
        }
        var userData={
            "class":classs,
            "group":group,
            "rollno":objcse[i].key_1,
            "name":objcse[i].key_2,
            "status":status,
        }
          
        db.con.query('INSERT INTO studentcse SET ?', userData, function(err, rows, fields) {
            if (!err) { 
              appData.error = 0;
              appData["data"] = "student registered successfully!";
            } 
            else {
              appData.error = 1;
              appData["data"] = "unsuccessfull";
              res.json(appData);
            }
          });

      }
    });
    res.send("Done");



  });


}
