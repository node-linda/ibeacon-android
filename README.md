Linda iBeacon Android
=====================
- https://github.com/node-linda/ibeacon-android


Dependencies
------------
- android-18 SDK (for Android4.3)
- sbt
- [scala android-sdk-plugin](https://github.com/pfn/android-sdk-plugin)


Install Dependencies
--------------------

    % brew update
    % brew install scala sbt


Build
-----

    % android update project --path `pwd`
    % sbt android:package-debug


Install
-------

    % adb devices
    % adb install -r bin/linda-iBeacon-debug.apk


Develop
-------

    % sbt "~ android:run"
