from flask import *
from database import *
import uuid   

admin=Blueprint('admin',__name__)

@admin.route('/admin_home')
def adminhome():
	if session.get("lid"):
		return render_template('admin_home.html')
	else:
		return redirect(url_for("public.login"))

@admin.route('/admin_manage_place',methods=['get','post'])
def admin_manage_place():
	if not session.get("lid") is None:
		data={}
		q="SELECT * FROM place "
		res=select(q)
		data['faculty']=res

		if 'submit' in request.form:
			place=request.form['place']

			q="insert into place values(null,'%s')"%(place)
			insert(q)
			flash('inserted successfully')
			return redirect(url_for('admin.admin_manage_place'))

		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="delete":
			q="delete from place  where place_id='%s'"%(id)
			update(q)
			flash('deleted successfully')
			return redirect(url_for('admin.admin_manage_place'))

		if action=="update":
			q="select * from place where place_id='%s'"%(id)
			print(q)
			res=select(q)
			data['updater']=res

		if 'update' in request.form:
			
		
			place=request.form['place']
		
		
			q="update place set place='%s' where place_id='%s'"%(place,id)
			update(q)
			flash('updated successfully')
			return redirect(url_for('admin.admin_manage_place'))
	else:
		return redirect(url_for("public.login"))

	return render_template('admin_manage_place.html',data=data)



@admin.route('/admin_manage_organizer',methods=['get','post'])
def admin_manage_organizer():
		data={}
		q="SELECT * FROM organizer inner join login using(login_id) inner join place using(place_id)"
		res=select(q)
		data['faculty']=res


		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="accept":
			q="update login set usertype='organizer' where login_id='%s'"%(id)
			update(q)
			flash('updated successfully')
			return redirect(url_for('admin.admin_manage_organizer'))

		if action=="reject":
			q="update login set usertype='reject'   where login_id='%s'"%(id)
			update(q)
			flash('updated successfully')
			return redirect(url_for('admin.admin_manage_organizer'))
		return render_template('admin_manage_organizer.html',data=data)


@admin.route('/admin_view_events',methods=['get','post'])
def admin_view_events():
		data={}
		id=request.args['id']
		q="SELECT * FROM event where organizer_id='%s' "%(id)
		res=select(q)
		data['faculty']=res
		return render_template('admin_view_events.html',data=data)

@admin.route('/admin_manage_eventtype',methods=['get','post'])
def admin_manage_eventtype():
	if not session.get("lid") is None:
		data={}
		q="SELECT * FROM event_type "
		res=select(q)
		data['faculty']=res

		if 'submit' in request.form:
			place=request.form['place']

			q="insert into event_type values(null,'%s')"%(place)
			insert(q)
			flash('inserted successfully')
			return redirect(url_for('admin.admin_manage_eventtype'))

		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="delete":
			q="delete from event_type  where event_type_id='%s'"%(id)
			update(q)
			flash('deleted successfully')
			return redirect(url_for('admin.admin_manage_eventtype'))

		if action=="update":
			q="select * from event_type where event_type_id='%s'"%(id)
			print(q)
			res=select(q)
			data['updater']=res

		if 'update' in request.form:
			
		
			place=request.form['place']
		
		
			q="update event_type set event_type='%s' where event_type_id='%s'"%(place,id)
			update(q)
			flash('updated successfully')
			return redirect(url_for('admin.admin_manage_eventtype'))
	else:
		return redirect(url_for("public.login"))

	return render_template('admin_manage_eventtype.html',data=data)



@admin.route('/admin_manage_service',methods=['get','post'])
def admin_manage_service():
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
			return redirect(url_for('admin.admin_manage_service'))

		if 'action' in request.args:
			action=request.args['action']
			id=request.args['id']
			
		else:
			action=None

		if action=="delete":
			q="delete from service  where service_id='%s'"%(id)
			update(q)
			flash('deleted successfully')
			return redirect(url_for('admin.admin_manage_service'))

		if action=="update":
			q="select * from service where service_id='%s'"%(id)
			print(q)
			res=select(q)
			data['updater']=res

		if 'update' in request.form:
			
		
			place=request.form['place']
			detail=request.form['detail']
		
		
			q="update service set service='%s',detail='%s' where service_id='%s'"%(place,detail,id)
			update(q)
			flash('updated successfully')
			return redirect(url_for('admin.admin_manage_service'))
	else:
		return redirect(url_for("public.login"))

	return render_template('admin_manage_service.html',data=data)

@admin.route('/admin_view_client',methods=['get','post'])
def admin_view_client():
		data={}
		
		q="SELECT * FROM client "
		res=select(q)
		data['faculty']=res
		return render_template('admin_view_client.html',data=data)


