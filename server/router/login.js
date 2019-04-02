const db = require('./config');
var bodyParser=require('body-parser');
module.exports=function(app){

	app.use(bodyParser.json());
	app.use(bodyParser.urlencoded( {extended: true}));

	app.get('/getScse',function(req,res){
		console.log("sduhwiuedhwkj");
		db.con.query("SELECT * FROM cse", function (err, result, fields) {
			if (err) throw err;
			res.json(result);
		});
	});
	app.get('/getSece',function(req,res){
		db.con.query("SELECT * FROM ece", function (err, result, fields) {
			if (err) throw err;
			res.json(result);
		});
	});
	app.get('/getTeachers',function(req,res){
		db.con.query("SELECT * FROM teachers", function (err, result, fields) {
			if (err) throw err;
			res.json(result);
		});
	});
	app.get('/getdepart',function(req,res){
		db.con.query("SELECT * FROM department", function (err, result, fields) {
			if (err) throw err;
			res.json(result);
		});
	});

	app.post('/home/Teacherlogin',function(req,res){
			var appData = {};
			var name = req.body.username;
			var password = req.body.password;
			console.log(name);
			console.log(password);
			
					db.con.query('SELECT * FROM teachers WHERE teacherid = ?', [name],	 
						function(err, rows, fields) {
							if (err) {
								//console.log(err);
								appData.error = 1;
								appData["data"] = "Error Occured!";
								res.status(400).json(appData);
								console.log("Error Occured!");
							} 
							else {
								if (rows.length > 0) {
									if (rows[0].password == password) {
										appData.error = 2;
										appData["data"] = "permission granted";
										res.status(200).json(appData);
										console.log("permission granted");
										
									} else {
										appData.error = 1;
										appData["data"] = "username and Password does not match";
										res.status(200).json(appData);
										console.log("username and Password does not match");
										
									}
								} else {
									appData.error = 1;
										appData["data"] = "user does not exists!";
										res.status(200).json(appData);
										console.log("user does not exists!");
									
								}
							}
						});
					

			
		});
}
