const db=require('./config');
var bodyParser=require('body-parser');
module.exports=function(app){

		app.use(bodyParser.json());
		app.use(bodyParser.urlencoded( {extended: true}));
		
		
		

////////////////// cse students ////////////////////////
		//submit=1;
		var  completedata={}
		var periods=""
		var attendances="";	



/*
////////////////////////////////////////////////////
		app.post('/home/constructattendance',function(req,res){
			
      		var time =req.body.time;var str1=time.split(" ");console.log(str1[0]);
      		var str2= str1[0].split(":");var str3=parseInt(str2[0]);var str4=parseInt(str2[1]);
      		var time = parseFloat(str3+"."+str4);var subcode="NULL";
      		
      		var userData ={
			"date":req.body.date,
			"class": req.body.group,
			"teacherid":req.body.teacher_id,
			"subjectcode":subcode}

			if((time>9.15)&&(time<10.05)){periods="p1";}
      		else if((time>10.05)&&(time<10.55)){periods="p2";}
      		else if((time>10.55)&&(time<11.45)){periods="p3"}
      		else if((time>11.45)&&(time<12.35)){periods="p4"}
      		else if((time>1.15)&&(time<2.05)){periods="p5"}
      		else if((time>2.05)&&(time<2.55)){periods="p6"}
      		else if((time>2.55)&&(time<3.45)){periods="p7"}
      		else if((time>3.45)&&(time<4.40)){periods="p8"}
		var appData = {};
		completedata=userData;
		res.send("construct attendance  done")
		});
     	app.post('/home/attendance/theory practical',function(req,res){
     		console.log("")
     		var appData={};
     		var singleattendance=req.body.attendance;
     		db.con.query('SELECT * FROM studentcse WHERE rollno = ?', [singleattendance], function(err, rows, fields) {
			if (rows.length>0) {
				console.log(rows);
				appData.error = 0;
				appData["data"] = singleattendance + " is successfull";
				attendances=attendances+singleattendance+",";
				res.json(appData);
			} 
			else {
				appData.error = 1;
				appData["data"] = singleattendance+ " is unsuccessfull";
				res.json(appData);
			}
			
		});
     				
		
	});
////////////////////////////////////////////////////////////
app.get('/home/submit',function(req,res){
	submit=0;
	var appData={};
	completedata[periods]=attendances;
	db.con.query('INSERT INTO cseattendancet SET ?', completedata, function(err, rows, fields) {
			if (!err) {
				appData.error = 0;
				appData["data"] ="successfull";
				res.json(appData);
			} 
			else {
				appData.error = 1;
				appData["data"] = "unsuccessfull";
				res.json(appData);
			}
		});
	completedata={}
	periods=""
	attendances="";
	});  	
////////////////////////////////cse students practical////////////////////////////////////////
		app.post('/home/cseattendance/practical',function(req,res){
      		subcode="NULL";
      		var period=req.body.period;
      		period=period.toString;
    		var appData = {
			"error": 1,
			"data" :""
		};
		var userData = {
			"data":datetime,
			"class": req.body.class,
			"teacherid":req.body.teacherId,
			"subjectcode":subcode,
			 period:req.body.attendance
			}


		db.con.query('INSERT INTO cseattendancep SET ?', userData, function(err, rows, fields) {
			if (!err) {
				appData.error = 0;
				appData["data"] = req.body.rollno + " is successfull";
				res.json(appData);
			} 
			else {
				appData.error = 1;
				appData["data"] = req.body.rollno+ " is unsuccessfull";
				res.json(appData);

			}
		});

	});

///////////////////////ece students theory/////////////////

















*/

/////////////////////
      	app.post('/home/eceattendance/theory',function(req,res){
      		var status;
      		var subcode="null";
      		var mcode=req.body.code;
      		var singleattendance;
      		var sending={}
      		console.log(req.body);
      	
      		var pcode="3";
      		if(mcode==1){
			var time =req.body.time;var str1=time.split(" ");console.log(str1[0]);
      		var str2= str1[0].split(":");var str3=parseInt(str2[0]);var str4=parseInt(str2[1]);
      		var time = parseFloat(str3+"."+str4);var subcode="NULL";
      		time=time-4;
      		if((time>9.15)&&(time<10.05)){periods="p1";}
      		else if((time>10.05)&&(time<10.55)){periods="p2";}
      		else if((time>10.55)&&(time<11.45)){periods="p3"}
      		else if((time>11.45)&&(time<12.35)){periods="p4"}
      		else if((time>1.15)&&(time<2.05)){periods="p5"}
      		else if((time>2.05)&&(time<2.55)){periods="p6"}
      		else if((time>2.55)&&(time<3.45)){periods="p7"}
      		else if((time>3.45)&&(time<4.40)){periods="p8"}
      	
      			var userData ={
			"date":req.body.date,
			"class": req.body.group,
			"teacherid":req.body.teacher_id,
			"subjectcode":subcode}

      			sending=structures(userData,pcode);
      			res.json(sending);
      		}
      		else if(mcode==2){
      			singleattendance=req.body.attendance;

      			var appData={}

			
	     		db.con.query('SELECT * FROM studentece WHERE rollno = ?', [singleattendance], function(err, rows, fields) {
				console.log(rows);
				if (rows.length>0) {
					attendances=attendances+singleattendance+",";
					console.log("attendance exist");
					flagss=1;
					appData.error = 0;
					appData["data"] ="successfull";
					res.json({
						"error": "0",
						"data" :"successfull"
					});

				} 
				else  {
					//console.log(appData);
					
					console.log(" doesnt attendance exist");
					flagss=2;
					res.json({
						"error": "1",
						"data" :"attendance unsuccessfull"
					});


				}
				});



      		}
      		else if(mcode==3){
      			console.log("right space checking");
      			var appData={};
						completedata[periods]=attendances;
						console.log("completedata");
						console.log(completedata);
						db.con.query('INSERT INTO eceattendancet SET ?', completedata, function(err, rows, fields) {
								
								if (!err) {
									appData.error = 0;
									appData["data"] ="successfull";
									console.log(appData);
									res.json(appData);
								} 
								else {
									appData.error = 1;
									appData["data"] = "unsuccessfull";
									console.log(appData);
									res.json(appData);
								}
							});
						completedata={}
						periods=""
						attendances="";



					}

      		


      		
	});
/////////////////////////////////ece students practical////////////////////////////////////////
		app.post('/home/eceattendance/practical',function(req,res){
      		var pcode="3";

	});
////////////////////////////////////////////////////////////////////////
	
function structures(userData,pcode){

		var appData = {};
		completedata=userData;
		appData.error = 0;
		appData["data"] = "structure successfull";
		return appData;
		
}
		
	
/*
setTimeout(ravi,2000);
var appData={}
function ravi(){
 
		 console.log("fierr");
		 console.log(flagss);
		if(flagss==1){
		appData.error = 0;
		appData["data"] = "successfull";
				
		}
		else if(flagss==2){
		appData.error = 1;
		appData["data"] = "unsuccessfull";
				
		}
		//console.log(appData)
		}
		console.log("parseInt");

*/






function onsubmit(pcode){

	var appData={};
	completedata[periods]=attendances;
	var sqls="";
	if(pcode=3){
sqls='INSERT INTO eceattendancet SET ?'
	}
	else if(pcode==4){
sqls='INSERT INTO eceattendancep SET ?'
	}
	db.con.query(sqls, completedata, function(err, rows, fields) {
			if (!err) {
				appData.error = 0;
				appData["data"] ="successfull";
				return appData;
			} 
			else {
				appData.error = 1;
				appData["data"] = "unsuccessfull";
				return appData;
			}
		});
	completedata={}
	periods=""
	attendances="";



}

}