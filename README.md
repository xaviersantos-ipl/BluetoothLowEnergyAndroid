# BluetoothLowEnergyAndroid

Help to test the application in dev environment

Remember this is a proof-of-concept prototype so it has fixed resource name in code , to solve this we need to set a few things.

Install Mobius

1.	https://github.com/beaver71/Mobius (forked from IoTKETI/Mobius + basic auth)
2.	Download and install it (a Linux machine will be always our reference)
3.	git clone https://github.com/beaver71/Mobius.git
4.	Also install mysql and mosquito

Launch Mobius

1.	cd Mobius
2.	In some cases we need to alter mysql password in sql shell:
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root'
3.	Create “mobiusdb”  database and Import: ./mobius/mobiusdb.sql
4.	npm install
5.	Configuration, in the conf.json file edit this prams:  
{ 
     "cseid": "/onem2m",
     "csebaseport": "7579",
     "csetype": "mn", 
     "superadm_usr": "superadmin", 
    "superadm_pwd": "your_pass"
}
6.	node mobius.js

Build the application in a development environment:

If you have your own Mobius server set up and you would like to change the address in the app, we can do it as follows:

1.	In “Constants.kt” file set the constants as you need. If you are run the app in an emulator and the Mobius server is in your localhost, set Mobius BASE_URL = http://10.0.2.2:7579/

2.	You need to set or add you Mobius server domain  in “res/xml/network_security_config.xml” file. E.g., <domain includeSubdomains="true">10.0.2.2</domain>

Android Application requirements

1.	Give Bluetooth and Location permissions to the application. Otherwise, the application will not work as intended. The Location permission will have to be granted manually, due to Android defined security reasons, via Definitions – Apps and Notifications – See [x number] applications – Choose the Application Name – Permissions – Enable Location Permissions

2.	Activate both GPS and Bluetooth in your smartphone

3.	The Bluetooth permission and activation will be asked, when the user starts the scanning process, if the user did not grant it or activate it before

4.	Set the name of the Bluetooth Low Energy (BLE) device you are looking for, the distance you are from it, and the container for the OneM2M server before scanning if you wish to see and alter the associated form

5.	When the advertiser you set is found by the scanner a form shall appear, as well as a list of all advertisers found within the set distance 
 
6.	The information in the form can then be altered and submitted to the selected container. Also we can add a new container with “+” button

7.	After the submission, the data can be seen in the container using any program like Postman and inserting the following URL: http://20.56.11.86:7579/onem2m/scanner_app/secondary/la
 
