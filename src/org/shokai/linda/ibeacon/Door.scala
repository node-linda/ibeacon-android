package org.shokai.linda.ibeacon;

import org.shokai.ibeacon.Beacon;
import android.net.http.AndroidHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.util.ArrayList;
import android.util.Log;

class Door(baseUrl:String, space:String){

  val client = AndroidHttpClient.newInstance("linda-ibeacon-android")
  val tuple = "{\"type\":\"door\",\"cmd\":\"open\"}"

  def open(){
    val post = new HttpPost(s"${baseUrl}/${space}")
    post.setEntity(new UrlEncodedFormEntity( 
      java.util.Arrays.asList( new BasicNameValuePair("tuple", tuple) )
    ))
    client.execute(post)
  }

}
