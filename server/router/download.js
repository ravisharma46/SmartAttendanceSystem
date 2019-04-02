const db=require('./config');
var bodyParser=require('body-parser');
var fs = require('fs');
var pdf = require('html-pdf');
var nodemailer = require('nodemailer');
var express=  require('express');
var path = require('path');
var flag=true;
var html='';
var transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: 'smartattendances@gmail.com',
    pass: 'smart000111'
  }
});

var mailOptions = {
  from: 'smartattendances@gmail.com',
  to: 'ravi.ravi.sharma46@gmail.com',
  subject: 'Sending Email using Node.js',
  text: 'yahoo',
  attachments: [{ path: './template/ravip.pdf'}]
}
var options = { format: 'Letter' };
/*
app.get('/home/teacher/ssyllabus',function(req,res){
      var links=req.query.link
      console.log(links);
	 file='uploads/syllabus/'+links;
	  res.download(file); 
	});
*/

///////////////////////////////////function html///////////////////
function sethtmlcse(x,date,classs,teacherid){
		console.log(date,classs,teacherid);
		var html1='<html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td {padding: 5px;}th {text-align: left;}</style></head>'
		var html2='<body><p><h1 style="font-size:25px;color:red;">ATTENDANCE OF COMPUTER SCIENCE ENGINEERING</h1> <hr><hr><hr></p>'
		var tables='<table style="width:80%"><tr><th>Sno</th><th>Rollno</th> <th>Name</th><th>Status</th></tr>'
		var last='</table></body></html>';
		html = html1+html2+tables;
			var appData = {
			"error": 1,
			"data" :""
		};

		db.con.query('SELECT * FROM cseattendancet where date =? and teacherid=? and class=?',["24 jan 2019"],[teacherid],[classs],function(err,rows,fields){
                    		if(err){
								appData.error = 1;
								appData["data"] = "Error Occured!";
								res.status(400).json(appData);
								console.log("Error Occured!");
							}
							console.log(rows);
							for(var i=0;i<rows.length;i++)
							{
								console.log("RAVI");

								attendance=rows.
								 html=html+'<tr><td>'

						db.con.query('SELECT * from cseattendancet where date =? and teacherid=? and class=?',["24 jan 2019"],["texh12"],["CSE123"],function(err,rows,fields){


						});

									
									
								 +rows[i].Sno+'</td><td>'
								 +rows[i].Rollno+'</td><td>'
								 +rows[i].Status+'</td></tr>'
							}
							html=html+last;
							flag= false;
							pdf.create(html, options).toFile('./template/cseattendance.pdf', function(err, res) {
							  if (err) return console.log(err);
							  });

								transporter.sendMail(mailOptions, function(error, info){
								  if (error) {
								    console.log(error);
								  } else {
								    console.log('Email sent: ' + info.response);
								  }
								});
							

							

                    	});
}
/////////////////////////////////////////////////////////////////////////////


function sethtmlece(x){
		var html1='<html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td {padding: 5px;}th {text-align: left;}</style></head>'
		var html2='<body><p><h1 style="font-size:25px;color:red;">ATTENDANCE OF ELECTRONICS AND COMMUNICATION ENGENEERING</h1> <hr><hr><hr></p>'
		var tables='<table style="width:80%"><tr><th>Sno</th><th>Rollno</th> <th>Name</th><th>Status</th></tr>'
		var last='</table></body></html>';
		html = html1+html2+tables;


			var appData = {
			"error": 1,
			"data" :""
		};
			db.con.query('SELECT * FROM eceattendancet ',
                    	function(err,rows,fields){
                    		if(err){
								appData.error = 1;
								appData["data"] = "Error Occured!";
								res.status(400).json(appData);
								console.log("Error Occured!");
							}
							for(var i=0;i<rows.length;i++)
							{
								
								html=html+'<tr><td>'+rows[i].Sno +'</td><td>'+rows[i].Rollno+'</td><td>'+rows[i].Name+'</td><td>'+rows[i].Status+'</td></tr>'

							}
							html=html+last;
							flag= false;
							if(x==1){

							pdf.create(html, options).toFile('./template/eceattendance.pdf', function(err, res) {
							  if (err) return console.log(err);
							  });
							

							}
							else if (x==2){
								pdf.create(html, options).toFile('./template/eceattendance.pdf', function(err, res) {
							  if (err) return console.log(err);
							  });

								transporter.sendMail(mailOptions, function(error, info){
								  if (error) {
								    console.log(error);
								  } else {
								    console.log('Email sent: ' + info.response);
								  }
								});
							}

							

                    	});
}







///////////////////////////////////finish html//////////////////////////////////


module.exports=function(app){
		app.use(bodyParser.json());
		app.use(bodyParser.urlencoded( {extended: true}));
		/*app.use('/home/download/eceattendance',function(req,res,next){
		sethtmlece(1);
		console.log("download export pdf");
		while(flag);
		next();
		},express.static(path.join(__dirname, "../template/eceattendance.pdf")));

		*/
	app.post('/home/email/cseattendance',function(req,res){
		console.log("email CSE attendance");
		var date=req.body.date;
		console.log(date);
		if(date=="today"){
			date = new Date();
			console.log(date);
		}
		else if(date=="all"){

		}
		var classs=req.body.class;
		var teacherid=req.body.teacherid;
		console.log(teacherid);
		sethtmlcse(date,classs,teacherid);

	});

	app.post('/home/email/eceattendance',function(req,res){
		console.log("email ECE attendance");
		var date=req.body.date;
		var classs=req.body.class;
		var teacherid=req.body.teacherid;
		console.log(teacherid);
		sethtmlece(date,classs,teacherid);
		

	});





}