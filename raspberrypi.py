import socket
import serial
from serialArduino import *
import firebase_admin
from firebase_admin import db
from firebase_admin import credentials

command=""

try:
    cred_obj = credentials.Certificate('remote-car-controller-firebase-adminsdk-49e31-24ca25a900.json')
    firebase_admin.initialize_app(cred_obj,{"databaseURL":"https://remote-car-controller-default-rtdb.firebaseio.com/"})
    ref = db.reference("/")
    print("Connected to database")
except:
    print("Error connecting to database")
    
s = socket.socket()        
print ("Socket successfully created")

ip="192.168.11.22"
port = 3310

s.bind((ip, port))        
print ("socket binded to %s" %(port))
 
s.listen(5)    
print ("socket is listening")

try:
    usb = serial.Serial("/dev/ttyUSB0", 9600, timeout=2)
    print("Connected To Serial port")
        
except Exception as e:
    print(e)
    print("ERROR - Could not open USB serial port.  Please check your port name and permissions.")
    exit()
 
while True:
    
    c, addr = s.accept()
    print ('Got connection from', addr )
    msg=c.recv(1024).decode()
    message=msg[2:]    
    
    if message != "":
        try:
            usb.write(b'{0}'.format(message))
            print(message + " ACTION ON THE CAR")
        except:
            print(message + " action not possible")
    
    command=usb.readline()
    command=command.decode()
    command=command.replace("\r\n","")
    users_ref = ref.child('sensorData')
    
    while(usb.readline()):
        if(command != ""):
            users_ref.push({
                'distance': command
            })
            print("Command from arduino is: "+ str(command))       
    
    c.close()