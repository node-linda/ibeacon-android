package org.shokai.linda.ibeacon;

import org.shokai.ibeacon.Beacon;
import android.net.http.AndroidHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.util.ArrayList;
import android.util.Log;

class Door{

  val baseUrl = "http://node-linda-base.herokuapp.com"
  val client = AndroidHttpClient.newInstance("linda-ibeacon-android")
  val tuple = "{\"type\":\"door\",\"cmd\":\"open\"}"
  val space = "delta"
  var lastOpenAt:Long = 0

  def open(beacon:Beacon){
    if(!beacon.uuid.equals("B0FC4601-14A6-43A1-ABCD-CB9CFDDB4013")) return
    if(!beacon.major.equals("0001")) return
    if(!beacon.minor.equals("0094")) return
    if(beacon.rssi < -60) return

    print(s"UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")

    print((System.currentTimeMillis - lastOpenAt).toString())
    if(System.currentTimeMillis() - lastOpenAt < 10000){
      lastOpenAt = System.currentTimeMillis()
      return
    }

    print(s"door open ${space}")
    val post = new HttpPost(s"${baseUrl}/${space}")
    post.setEntity(new UrlEncodedFormEntity( 
      java.util.Arrays.asList( new BasicNameValuePair("tuple", tuple) )
    ))
    client.execute(post)

    lastOpenAt = System.currentTimeMillis()
  }

  def print(msg:String){
    Log.v("linda-iBeacon", msg)
  }

}
