from flask import *
from database import *
public=Blueprint('public',__name__)

@public.route('/')
def home():
	return render_template('public_home.html')

@public.route('/login',methods=['get','post'])
def login():
	session.clear()
	if "submit" in request.form:
		u=request.form['uname']
		p=request.form['password']
		q="select * from login where username='%s' and password='%s'"%(u,p)
		print(q)
		res=select(q)
		if res:
			session['lid']=res[0]['login_id']
			if res[0]['usertype']=="admin":
				flash("Logging in")			
				return redirect(url_for("admin.adminhome"))

			elif res[0]['usertype']=="organizer":
				q="select * from organizer where login_id='%s'"%(res[0]['login_id'])
				res1=select(q)
				if res1:
					session['oid']=res1[0]['organizer_id']
					print(session['oid'])
					flash("Logging in")
					return redirect(url_for("organizer.organizer_home"))

			



			# elif res[0]['type']=="customer":
			# 	q="select * from customer inner join login using (username) where username='%s' and status='0'"%(u)
			# 	res=select(q)
			# 	if res:
			# 		flash('inactive')
			# 	else:


			# 		q="select * from customer where username='%s'"%(lid)
			# 		res=select(q)
			# 		if res:
			# 			session['customer_id']=res[0]['customer_id']
			# 			cid=session['customer_id']
			# 		return redirect(url_for('customer.customer_home'))

			else:
				flash("Registration Under Process")
		flash("You are Not Registered")

	return render_template('login.html')




@public.route('/reg',methods=['get','post'])
def reg():
	data={}
	q="select * from place"
	data['places']=select(q)

	if 'submit' in request.form:
		
		name=request.form['name']
		place=request.form['place']
		phone=request.form['phone']
		email=request.form['email']
		uname=request.form['username']
		pas=request.form['password']
		q="insert into login values(null,'%s','%s','pending')"%(uname,pas)
		id=insert(q)
		print(q)
		q="insert into organizer values(null,'%s','%s','%s','%s','%s')"%(id,place,name,phone,email)
		insert(q)
		print(q)
		return redirect(url_for('public.login'))

	return render_template("userreg.html",data=data)