from flask import * 
from database import* 
import uuid

api=Blueprint('api',__name__)

@api.route('/login')
def login():
	data={}
	u=request.args['username']
	p=request.args['password']
	q1="select * from login where username='%s' and `password`='%s'"%(u,p)
	print(q1)
	res=select(q1)
	if res:
		data['data']=res
		data['status']='success'
	else:
		data['status']='failed'
	return str(data)

@api.route('/userregister')
def userregister():
	data={}
	f=request.args['fname']
	l=request.args['lname']
	
	pl=request.args['place']
	
	ph=request.args['phone']
	e=request.args['email']
	u=request.args['username']
	p=request.args['password']
	a=request.args['address']
	q="select * from login where username='%s' and password='%s'"%(u,p)
	res=select(q)
	if res:
		data['status']='already'
	else:
		q="insert into login values(NULL,'%s','%s','user')"%(u,p)
		lid=insert(q)
		r="insert into client values(NULL,'%s','%s','%s','%s','%s','%s','%s')"%(lid,f,l,pl,ph,e,a)#change sql table licence to address
		insert(r)
		data['status']="success"
	return str(data)


@api.route('/viewusers')
def viewusers():
	data={}
	login_id=request.args['lid']
	q="select * from client inner join login using(login_id) where login_id='%s'"%(login_id)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
		data['method']='viewusers'
	return str(data)

@api.route('/updateuser')	
def updateuser():
	data={}
	name=request.args['name']
	place=request.args['place']
	phone=request.args['Phone']
	email=request.args['email']
	licence=request.args['licence']
	login_id=request.args['login_id']
	password=request.args['password']

	q="update `client` set fname='%s',place='%s',phone='%s',email='%s',address='%s' where login_id='%s'"%(name,place,phone,email,licence,login_id)
	update(q)
	q="update login set password='%s' where login_id='%s'"%(password,login_id)
	update(q)
	print(q)
	data['status']='success'
	data['method']='updateuser'
	return str(data)


@api.route('/viewevent')
def viewevent():
	data={}
	
	q="SELECT * FROM `event` INNER JOIN `event_type` USING (`event_type_id`) INNER JOIN `organizer` USING (`organizer_id`) where status='pending'"
	res=select(q)
	print(q)
	
	data['status']='success'
	data['data']=res
		
	return str(data)


@api.route('/searchevent')
def searchevent():
	data={}
	search=request.args['search']+'%'
	q="SELECT * FROM `event` INNER JOIN `event_type` USING (`event_type_id`) INNER JOIN `organizer` USING (`organizer_id`) where event like '%s' and status='pending'"%(search)
	res=select(q)
	print(q)
	
	data['status']='success'
	data['data']=res
		
	return str(data)


@api.route('/sviewevent')
def sviewevent():
	data={}
	
	q="SELECT * FROM `event` INNER JOIN `event_type` USING (`event_type_id`) INNER JOIN `organizer` USING (`organizer_id`)"
	res=select(q)
	print(q)
	
	data['status']='success'
	data['data']=res
		
	return str(data)


@api.route('/ssearchevent')
def ssearchevent():
	data={}
	search=request.args['search']+'%'
	q="SELECT * FROM `event` INNER JOIN `event_type` USING (`event_type_id`) INNER JOIN `organizer` USING (`organizer_id`) where event like '%s'"%(search)
	res=select(q)
	print(q)
	
	data['status']='success'
	data['data']=res
		
	return str(data)


@api.route('/Viewservice')
def Viewservice():
	data={}

	eid=request.args['eid']
	
	q="SELECT * FROM event_service INNER JOIN `event` USING (`event_id`) inner join service using (service_id) where event_id='%s'"%(eid)#add event_service in web side
	res=select(q)
	print(q)
	
	data['status']='success'
	data['data']=res
		
	return str(data)



@api.route('/Booknow')
def Booknow():
	data={}

	eid=request.args['eid']
	lid=request.args['login_id']
	
	q="insert into client_booking values(null,'%s',(select client_id from client where login_id='%s'),curdate(),'pending','0')"%(eid,lid)
	insert(q)
	q="update event set status='Booked' where event_id='%s'"%(eid)
	update(q)
	print(q)
	
	data['status']='success'
	
		
	return str(data)


@api.route('/staffViewbookings')
def staffViewbookings():
	data={}

	
	
	q="SELECT * FROM client_booking INNER JOIN `event` USING (`event_id`) inner join client using (client_id)"
	res=select(q)
	print(q)
	
	data['status']='success'
	data['data']=res
		
	return str(data)


@api.route('/userViewbookings')
def userViewbookings():
	data={}
	log_id=request.args['log_id']
	q="SELECT * FROM client_booking INNER JOIN `event` USING (`event_id`) inner join client using (client_id) where client_booking.status='organizer' and client_id=(select client_id FROM client where login_id='%s')"%(log_id) #this details comes only where client_booking status='organizer'
	res=select(q)
	print(q)
	
	data['status']='success'
	data['data']=res
		
	return str(data)


@api.route('/AndroidBarcodeQrExample')
def AndroidBarcodeQrExample():
	data={}
	lid=request.args['contents']
	



	q="select * from client_booking inner join event using (event_id) where booking_id='%s'"%(lid)

	res=select(q)
	amt=res[0]['amount']



	q="insert into payment values(null,'%s','%s',curdate())"%(lid,amt)
	print(q)
	id=insert(q)
	
	q="update client_booking set status='Paid' where booking_id='%s'"%(lid)
	update(q)
	data['status']="success"

	return str(data)



@api.route('/View_my_qr')
def View_my_qr():
	data={}
	bid=request.args['bid']
	q="SELECT * FROM `client_booking` WHERE `booking_id`='%s'"%(bid)
	res=select(q)
	data['data']=res[0]['image']
	data['status']="success"
	return str(data)

@api.route('/Staffviewuser')
def Staffviewuser():
	data={}
	
	q="SELECT * FROM `client` inner join login using (login_id)"
	res=select(q)
	data['data']=res
	data['status']="success"
	return str(data)


@api.route('/accept')
def accept():
	data={}
	lid=request.args['lid']
	
	q="update login set usertype='user' where login_id='%s'"%(lid)
	update(q)
	
	data['status']="success"
	return str(data)

@api.route('/reject')
def reject():
	data={}
	lid=request.args['lid']
	
	q="update login set usertype='reject' where login_id='%s'"%(lid)
	update(q)
	
	data['status']="success"
	return str(data)











