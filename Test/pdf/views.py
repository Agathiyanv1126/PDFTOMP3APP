from django.shortcuts import render,redirect,get_object_or_404
from django.http import HttpResponse, Http404
from .forms import Registrationform
from django.contrib.auth import authenticate,login as auth,logout
from django.urls import reverse_lazy
from django.core.serializers import serialize
from django.http import JsonResponse
from celery.result import AsyncResult

from django.contrib.auth.models import User
import os

from .models import PDFFile, Profile
from .forms import PDFUploadForm,FavoriteForm
from django.core.exceptions import ObjectDoesNotExist
from .models import PDFFile,Profile,MP3File,Favorite

import random
from django.core.mail import send_mail
from django.utils.timezone import now, timedelta
from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
from django.contrib.auth.hashers import make_password
from .models import PDFFile, Profile, Favorite  # ✅ Removed MP3File



from django.contrib import messages
from django.utils.timezone import now

# Create your views here.

def login(request):
    if(request.POST):
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=username, password=password)
        if user is not None:
            auth(request,user)
            profile=Profile.objects.filter(user=user).values('id', 'username','passwords','email').first()
            return JsonResponse({"Status": True, "message": "Login successfully", "data": profile})
    return JsonResponse({"Status":False,"message":"Login failed","data":""})

def signup(request):
    form=Registrationform()
    if(request.method=="POST"):
        form=Registrationform(request.POST)
        print(form.is_valid())
        if(form.is_valid()):
            #return redirect(reverse_lazy(""))
            print(form.cleaned_data['email'])
            otp = random.randint(1000, 9999)

            # Send OTP via email
            send_mail(
                  'Your OTP Code',
             
                 f'Your OTP code is: {otp}',
                 'your_email@gmail.com',  # Sender's email (matches EMAIL_HOST_USER in settings.py)
            
                  [form.cleaned_data['email']],       # Receiver's email
                    fail_silently=False,
                )
            
            print(form.cleaned_data)
            print("OTP:", otp)
            request.session['form_data'] = form.cleaned_data
            # Save the OTP to session or database for verification
            request.session['otp'] = otp
            request.session.modified = True  # Mark session as modified
            request.session.save()
            return JsonResponse({"Status":True,"message":" Send otp for authentication","data":{"otp":otp}})
    else:
        print(form.errors) 
    return JsonResponse({"Status":"False","message":"failed to signup","data":""})  
from django.http import JsonResponse
from .forms import Registrationform

def signin_verification(request):
    if request.method == "POST":
        print("Request POST data:", request.POST)

        # ✅ Get the OTP from the request
        entered_otp = str(request.POST.get("otp", "")).strip()
        saved_otp = request.session.get("otp")

        print(f"Entered OTP: {entered_otp}, Saved OTP: {saved_otp}")

        # ✅ Check if OTP is valid
        if entered_otp and saved_otp and entered_otp == str(saved_otp):
            print("✅ OTP verified successfully!")
            # ✅ Retrieve form data from the session
            form_data = request.session.get("form_data")
            if not form_data:
                return JsonResponse({"Status": False, "message": "No form data found in session"})

            # ✅ Process and validate the form
            form = Registrationform(form_data)
            if form.is_valid():
                user = form.save()  # Save the form data to the database

                # ✅ Fetch profile details safely
                profile = Profile.objects.filter(user=user).values("id", "user__username", "passwords").first()

                # ✅ Clear session data after successful registration
                request.session.pop("form_data", None)
                request.session.pop("otp", None)

                return JsonResponse({"Status": True, "message": "Recorded successfully", "data": profile})

            return JsonResponse({"Status": False, "message": "Form data is invalid", "errors": form.errors})

        print("❌ Invalid OTP")
        return JsonResponse({"Status": False, "message": "Invalid OTP"})

    return JsonResponse({"Status": False, "message": "Only POST requests are allowed"})



def single_upload(request):
    id = request.POST.get('id', '')
    if request.method == 'POST':
        form = PDFUploadForm(request.POST, request.FILES)
        if form.is_valid():
            pdf_file = form.save(commit=False)
            profile = Profile.objects.get(id=id)
            pdf_file.user = profile
            pdf_file.title = pdf_file.pdf_file.name
            pdf_file.save()
            print("pdf_file:",pdf_file.title)
            return JsonResponse({"Status":True,"message":"sucessfully uploaded in db","data":{"id":pdf_file.id,'title':pdf_file.title,'date':pdf_file.date}})
        else:
            # Handle form errors
            print(request.POST,"\n",request.FILES)
            return JsonResponse({"Status":False,"message":"failed to upload in db","data":""})



def logout_view(request):
    username = str(request.user)
    if request.method=="POST":
        logout(request)
        return JsonResponse({"Status": True, "message": "Logout Sucessfully","data":username})
    return JsonResponse({"Status": False, "message": "Logout failed","data":""})


import random
from django.core.mail import send_mail
from django.contrib.auth.models import User
from django.http import JsonResponse

def send_otp(request):
    if request.method == "POST":
        receiver_email = request.POST.get('email')
        receiver_username = request.POST.get('username')

        try:
            # Check if user exists
            user = User.objects.get(email=receiver_email, username=receiver_username)
            print(user)
            
            # Generate OTP
            otp = random.randint(1000, 9999)

            # Send OTP via email
            try:
                send_mail(
                    'Your OTP Code',
                    f'Your OTP code is: {otp}',
                    'your_email@gmail.com',  # Use your email configured in settings.py
                    [receiver_email],
                    fail_silently=False,
                )
                
                # Save OTP to session
                request.session['otp'] = otp
                request.session['receiver_email'] = receiver_email
                request.session['username'] = receiver_username
                request.session.modified = True  # Mark session as modified
                request.session.save()
                print(f"OTP: {otp}")

                return JsonResponse({"Status": True, "message": "Email sent successfully.", "data": {"OTP": otp}})
            
            except Exception as e:
                print(f"Email sending failed: {e}")
                return JsonResponse({"Status": False, "message": "Email sending failed.", "data": {}})
        
        except User.DoesNotExist:
            return JsonResponse({"Status": False, "message": "User does not exist.", "data": {}})
    
    return JsonResponse({"Status": False, "message": "Invalid request method.", "data": {}})


from django.http import JsonResponse
from django.contrib.auth.models import User
from .models import Profile

def delete_profile(request):
    id = request.POST.get('id', '')
    try:
        # Fetch the profile and related user instance
        profile = Profile.objects.get(id=id)
        user = profile.user
        
        # Delete the profile and the user account
        profile.delete()
        user.delete()
        
        return JsonResponse({"Status": True, "message": "Profile deleted successfully.","data":{"username":profile.username,"password":profile.passwords}})
    except Profile.DoesNotExist:
        return JsonResponse({"Status": False, "message": "Profile does not exist."})
    except Exception as e:
        return JsonResponse({"Status": False, "message": f"An error occurred: {str(e)}"})



def verify_otp(request):
    if request.method == "POST":
        print("Request POST data:", request.POST)

        # ✅ Get the OTP from the request
        entered_otp = str(request.POST.get("otp", "")).strip()
        saved_otp = request.session.get("otp")

        print(f"Entered OTP: {entered_otp}, Saved OTP: {saved_otp}")

        # ✅ Check if OTP is valid
        if entered_otp and saved_otp and entered_otp == str(saved_otp):
            print("✅ OTP verified successfully!")
                # ✅ Clear session data after su

            return JsonResponse({"Status": True, "message": "Verify OTP", "data": {"OTP": saved_otp}})
        print("❌ Invalid OTP")
        return JsonResponse({"Status": False, "message": "Invalid OTP"})

    return JsonResponse({"Status": False, "message": "Only POST requests are allowed"})



from django.contrib.auth.hashers import make_password
from django.contrib.auth.models import User
from django.http import JsonResponse
from django.core.serializers import serialize
from django.contrib import messages
import json

def reset_password(request):
    if request.method == 'POST':
        print("POST request received")

        email = request.session.get('receiver_email')  # Fetch user ID from session
        username= request.session.get('username')  # Fetch user ID from session
        print(f"Email: {email}")
        print(f"Username: {username}")
        new_password = request.POST.get('new_password')
        confirm_password = request.POST.get('confirm_password')

        print(f"New Password: {new_password}")
        print(f"Confirm Password: {confirm_password}")

        # Validate user ID
        user = User.objects.get(username=username,email=email)  # Use get_object_or_404 for better error handling
        profile = Profile.objects.get(user=user)

        # Check if passwords match
        if new_password != confirm_password:
            print("Passwords do not match.")
            messages.error(request, "Passwords do not match.")
            return JsonResponse({"Status": False, "message": "Passwords do not match.", "data": ""})

        # Update the user's password
        # It's better to use a separate field for storing old passwords if needed
        profile.passwords = new_password  # Assuming 'passwords' is used for logging old passwords
        profile.save()

        user.password = make_password(new_password)  # Hash the password before saving
        user.save()

        # Serialize and return updated profile data
        serialized_data = serialize("json", [profile])
        profile_data = json.loads(serialized_data)[0]["fields"]

        print("Password updated successfully.")
        messages.success(request, "Password updated successfully.")
        return JsonResponse({"Status": True, "message": "Password updated successfully.", "data": profile_data})

    # Handle non-POST requests
    return JsonResponse({"Status": False, "message": "Invalid request method.", "data": ""})



from django.forms.models import model_to_dict
from django.http import JsonResponse
# hari check
def file_list(request):
    id = request.POST.get('id', '')
    profile = Profile.objects.get(id=id)
    # Convert QuerySets to lists of dictionaries
    pdf_files = list(PDFFile.objects.filter(user=profile).values('id','user_id', 'title', 'pdf_file', 'date'))
    mp3_files = list(MP3File.objects.filter(user=profile).values('id', 'user_id','title', 'mp3_file', 'date'))

    return JsonResponse({
        "Status": True,
        "message": "Files retrieved successfully.",
        "data": [
            {"PDF Files": pdf_files},
            {"MP3 Files": mp3_files},]
              })

from django.forms.models import model_to_dict
from django.http import JsonResponse
# hari check
def audiofile_list(request):
    id = request.POST.get('id', '')
    profile = Profile.objects.get(id=id)
    # Convert QuerySets to lists of dictionaries
    mp3_files = list(MP3File.objects.filter(user=profile).values('id', 'user_id','title', 'mp3_file', 'date'))

    return JsonResponse({
        "Status": True,
        "message": "Files retrieved successfully.",
        "data": [
            {"MP3 Files": mp3_files},]
              })

 # hari check  
from django.shortcuts import get_object_or_404, render, redirect
from .models import PDFFile, Profile
from .tasks import convert_pdf_to_mp3_task
from celery.result import AsyncResult
from . forms import ProfileUpdateForm

def Voicetype(request):
    id = request.POST.get('id')
    pdf_id= request.POST.get('pdf_id')
    profile = Profile.objects.get(id=id)
    pdf_file = get_object_or_404(PDFFile, id=pdf_id, user=profile)

    if request.method == 'POST':
            # Get the file path and title
            voice=request.POST.get("voiceType")
            print(voice)
            pdf_path = pdf_file.pdf_file.path  # Get the absolute path to the file
            title = pdf_file.title.replace(".pdf","")  # Assuming PDFFile has a title field
            try:
                types=''
                if voice=="Female":
                    types='en-US-JennyNeural'
                else:
                    types='en-US-GuyNeural'
                print("types",types)
                # Pass the path and IDs to the Celery task
                task=convert_pdf_to_mp3_task.delay(types,pdf_path, title,profile.id)
                request.session['task_id']=task.id
                return JsonResponse({"Status": True,"message": "Files retrieved successfully.","data": {"SlectedVoice":voice}})
            except Exception as e:
                print("error",e)
                return JsonResponse({"Status": True,"message": "Files retrieved successfully.","data": {"SlectedVoice":voice}})
    return JsonResponse({"Status": True,"message": "Files retrieved successfully.","data": {"you have slected voice":voice}})


from django.http import JsonResponse


def profile(request):
    id = request.POST.get('id', '')
    users=Profile.objects.get(id=id)
    return JsonResponse({"Status":True,"message":"sucessfully uploaded in db","data":{"id":users.id,'name':users.name,'username':users.username,'mobile_phone':users.mobile_phone,'email':users.email}})

from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import Profile
from .forms import ProfileUpdateForm
import json

@csrf_exempt
def edit(request):
    try:
        # Debugging: Print the received data
        print("Received Data:", request.POST.dict())

        id = request.POST.get('id', '')

        # Convert ID to integer to avoid query errors
        if not id.isdigit():
            return JsonResponse({"Status": False, "message": "Invalid ID format", "data": ""})

        id = int(id)

        try:
            profile = Profile.objects.get(id=id)
        except Profile.DoesNotExist:
            return JsonResponse({"Status": False, "message": "Profile not found", "data": ""})

        if request.method == 'POST':
            form = ProfileUpdateForm(request.POST, request.FILES, instance=profile)

            if form.is_valid():
                form.save()
                return JsonResponse({
                    "Status": True,
                    "message": "Successfully uploaded in db",
                    "data": {
                        "id": profile.id,
                        "name": profile.name,
                        "username": profile.username,
                        "mobile_phone": profile.mobile_phone,
                        "email": profile.email
                    }
                })
            else:
                print("Form Errors:", form.errors)  # Debugging
                return JsonResponse({
                    "Status": False,
                    "message": "Missing or invalid data",
                    "errors": form.errors
                })

        return JsonResponse({"Status": False, "message": "Failed to update", "data": ""})

    except Exception as e:
        return JsonResponse({"Status": False, "message": str(e)})


from django.http import JsonResponse
from django.shortcuts import get_object_or_404

def search(request):
    id = request.POST.get('id', '')
    query = request.POST.get('query', '')
    
    try:
        # Get the profile
        profile = get_object_or_404(Profile, id=id)
        
        # Filter PDFs and MP3s using the corrected `icontains` lookup
        pdffile = (profile.pdfs.filter(title__icontains=query).values('id', 'pdf_file', 'title', 'date')).first()
        mp3file = (profile.mp3s.filter(title__icontains=query).values('id', 'mp3_file', 'title', 'date')).first()
        
        return JsonResponse({
            "Status": True,
            "message": "Search completed successfully.",
            "data": {"PDF FILES": pdffile, "MP3 FILES": mp3file}
        })
    except Profile.DoesNotExist:
        return JsonResponse({"Status": False, "message": "Profile not found."})
    except Exception as e:
        return JsonResponse({"Status": False, "message": f"An error occurred: {str(e)}"})

def add_favorite(request):
    id = request.POST.get('id', '')
    mp3_id = request.POST.get('mp3_id', '')
    profile = Profile.objects.get(id=id)
    mp3 = MP3File.objects.get( id=mp3_id)
    Favorite.objects.create(user=profile, mp3file=mp3)
    mp3 = MP3File.objects.filter( id=mp3_id).values('id', 'mp3_file', 'title', 'date').first()
    return JsonResponse({
            "Status": True,
            "message": "favorite added successfully.",
            "data": { "MP3 FILES": mp3}
        })

from django.http import JsonResponse
from django.shortcuts import get_object_or_404
from .models import Profile, Favorite

def favorite(request):
    id = request.POST.get('id', '')
    profile = get_object_or_404(Profile, id=id)

    # Retrieve all Favorite objects for this user, including MP3 file details
    fav_mp3_files = Favorite.objects.filter(user=profile).select_related('mp3file')

    # Convert to dictionary format with ID as key
    data = [
        {   "id":fav.id,
            "title": fav.mp3file.title,
            "file_url": fav.mp3file.mp3_file.url if fav.mp3file.mp3_file else None,
            "date_added": fav.mp3file.date.strftime('%Y-%m-%d %H:%M:%S') if fav.mp3file.date else None
        }
        for fav in fav_mp3_files
    ]

    return JsonResponse({
        "status": True,
        "message": "Favorites retrieved successfully.",
        "data": data  # ✅ Now returned as a dictionary instead of a list
    })
