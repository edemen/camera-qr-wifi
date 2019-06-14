# camera-qr-wifi
Read QR from camera and connect to WiFi if possible.

## Usecase
* I want to have a hidden SSID with a very long password, but I need it to be easy to add new devices to the network
* Tools to encode SSID and password into a QR code are readily available
* On a mobile phone, you can use [Barcode Scanner](https://play.google.com/store/apps/details?id=com.google.zxing.client.android) or iOS camera to scan this QR code and get a prompt to connect to the WiFi network
* On a laptop or Macbook there does not seem a convenient solution for this

## Limitations
* We expect the format of the QR code contents to follow [ZXing reader guidelines](https://github.com/zxing/zxing/wiki/Barcode-Contents#wi-fi-network-config-android-ios-11)
* Cross-platform is implemented with executing platform specific commands based on `os.name`
* Only OS X platform implemented for now

## Execution
### With Maven
If you don't want to mess with building it into a JAR, you could clone and execute with Maven.
Provided you have JDK 8+ and Maven installed, you should be able to do:
~~~~
git clone git@github.com:edemen/camera-qr-wifi.git
cd camera-qr-wifi
mvn exec:java '-Dexec.mainClass="tech.utilis.cameraqrwifi.Main"'
~~~~
### From JAR
Coming.
