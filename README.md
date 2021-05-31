# BluetoothLowEnergyAndroid

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

Build the application:

1.	Create the required resources for this “m2m:uril” in Mobius: “onem2m/app_light1/light1_state_cont” or change in code at “MainActivity.java” -> “getLast()” and “onClick()”
2.	In “Constants.kt” file set the constants as you need. If you are run the app in an emulator and the Mobius server is in your localhost, set Mobius BASE_URL = http://10.0.2.2:7579/
3.	You need to set or add you Mobius server domain  in “res/xml/network_security_config.xml” file. Eg. <domain includeSubdomains="true">10.0.2.2</domain>

Android Application requirements

1.	Give Bluetooth and Location permissions to the application. Otherwise, the application will not work as intended
2.	The Bluetooth permission will be asked when the user starts the scanning process
3.	The Location permission will have to be granted manually, due to Android defined security reasons, via Definitions – Apps and Notifications – See [x number] applications – Choose the Application Name – Permissions – Enable Location Permissions
4.	Set the name of the Bluetooth Low Energy (BLE) device you are looking for before scanning if you wish to see and alter the associated form.
5.	You can do the first scan to see all the available BLE devices, and only after that write the name of the device you want and scan once more to see and be able to alter the form associated with the device
