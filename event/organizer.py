from flask import *
from database import *
import uuid 
import qrcode  

organizer=Blueprint('organizer',__name__)

@organizer.route('/organizer_home')
def organizer_home():
	if session.get("lid"):
		return render_template('organizer_home.html')
	else:
		return redirect(url_for("public.login"))


@organizer.route('/organizer_manage_staff',methods=['get','post'])
def organizer_manage_staff():
	if not session.get("lid") is None:
		data={}
		q="SELECT * FROM staff "
		res=select(q)
		data['faculty']=res

		if 'submit' in request.form:
			fname=request.form['fname']
			lname=request.form['lname']
			place=request.form['place']
			phone=request.form['phone']
			email=request.form['email']
			uname=request.form['username']
			pas=request.form['password']
			q="insert into login values(null,'%s','%s','staff')"%(uname,pas)
			id=insert(q)
			print(q)
			q="insert into staff values(null,'%s','%s','%s','%s','%s','%s')"%(id,fname,lname,place,phone,email)
			insert(q)
			print(q)
			return redirect(url_for('organizer.organizer_manage_staff'))

		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="delete":
			q="delete from staff  where staff_id='%s'"%(id)
			update(q)
			flash('deleted successfully')
			return redirect(url_for('organizer.organizer_manage_staff'))

		if action=="update":
			q="select * from staff where staff_id='%s'"%(id)
			print(q)
			res=select(q)
			data['updater']=res

		if 'update' in request.form:
			
		
			fname=request.form['fname']
			lname=request.form['lname']
			place=request.form['place']
			phone=request.form['phone']
			email=request.form['email']
		
		
			q="update staff set fname='%s',lname='%s',place='%s',phone='%s',email='%s' where staff_id='%s'"%(fname,lname,place,phone,email,id)
			update(q)
			flash('updated successfully')
			return redirect(url_for('organizer.organizer_manage_staff'))
	else:
		return redirect(url_for("public.login"))

	return render_template('organizer_manage_staff.html',data=data)

@organizer.route('/organize_update',methods=['get','post'])
def organize_update():
		data={}
		
		q="SELECT * FROM organizer  inner join place using(place_id)"
		res=select(q)
		data['faculty']=res


		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="update":
			q="select * from organizer inner join place using(place_id) where organizer_id='%s'"%(id)
			print(q)
			res=select(q)
			data['updater']=res

		if 'update' in request.form:
			
		
			name=request.form['name']
			
			# place=request.form['place']
			phone=request.form['phone']
			email=request.form['email']
		
		
			q="update organizer set name='%s',phone='%s',email='%s' where organizer_id='%s'"%(name,phone,email,id)
			update(q)
			flash('updated successfully')
			return redirect(url_for('organizer.organize_update'))

		return render_template('organizer_update.html',data=data)


@organizer.route('/organizer_manage_event',methods=['get','post'])
def organizer_manage_event():
	if not session.get("lid") is None:
		data={}
		q="SELECT * FROM event inner join event_type using(event_type_id) "
		res=select(q)
		data['faculty']=res

		q="select * from event_type"
		data['event']=select(q)

		if 'submit' in request.form:
			event=request.form['event']
			eve=request.form['eve']
			place=request.form['venue']
			date=request.form['date']
			detail=request.form['detail']
			amt=request.form['amount']
			add=request.form['add']
			q="insert into event values(null,'%s','%s','%s','%s','%s','%s',curdate(),'%s','%s','pending')"%(eve,session['oid'],event,place,amt,add,date,detail)
			insert(q)
			print(q)
			return redirect(url_for('organizer.organizer_manage_event'))

		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="delete":
			q="delete from event  where event_id='%s'"%(id)
			update(q)
			flash('deleted successfully')
			return redirect(url_for('organizer.organizer_manage_event'))

	else:
		return redirect(url_for("public.login"))

	return render_template('organizer_manage_event.html',data=data)


@organizer.route('/organize_add_service',methods=['get','post'])
def organize_add_service():
	if not session.get("lid") is None:
		data={}
		q="SELECT * FROM service "
		res=select(q)
		data['faculty']=res

		if 'submit' in request.form:
			place=request.form['place']
			detail=request.form['detail']

			q="insert into service values(null,'%s','%s')"%(place,detail)
			insert(q)
			flash('inserted successfully')
			return redirect(url_for('admin.organize_add_service'))

		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="delete":
			q="delete from service  where service_id='%s'"%(id)
			update(q)
			flash('deleted successfully')
			return redirect(url_for('admin.organize_add_service'))

		
	else:
		return redirect(url_for("public.login"))

	return render_template('organize_add_service.html',data=data)

@organizer.route('/organizer_view_booking',methods=['get','post'])
def organizer_view_booking():
	
	data={}
	q="SELECT *,concat(client_booking.status) as cstatus FROM client_booking inner join event using(event_id) inner join client using (client_id)"
	res=select(q)
	data['faculty']=res


	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
		
	else:
		action=None

	if action=="accept":
		path="static/qr_code/"+str(uuid.uuid4())+".png"
		s=qrcode.make(str(res[0]['booking_id']))
		s.save(path)
		print(s)
		q="update client_booking set status='organizer',image='%s' where booking_id='%s'"%(path,id)
		update(q)
		print(q)
		flash('updated successfully')
		return redirect(url_for('organizer.organizer_view_booking'))

	if action=="reject":
		q="update client_booking set status='reject' where booking_id='%s'"%(id)
		update(q)
		flash('updated successfully')
		return redirect(url_for('organizer.organizer_view_booking'))
	return render_template('organizer_view_booking.html',data=data)


