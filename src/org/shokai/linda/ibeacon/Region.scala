package org.shokai.linda.ibeacon;

import org.shokai.ibeacon.Beacon;
import android.net.http.AndroidHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.util.ArrayList;
import android.util.Log;

class Region(baseUrl:String, space:String, who:String){

  val client = AndroidHttpClient.newInstance("linda-ibeacon-android")
  val enterTuple = "{\"type\":\"region\",\"name\":\"delta\",\"who\":\""+who+"\",\"action\":\"enter\"}"
  val leaveTuple = "{\"type\":\"region\",\"name\":\"delta\",\"who\":\""+who+"\",\"action\":\"leave\"}"

  private def post(tuple:String){
    val post = new HttpPost(s"${baseUrl}/${space}")
    post.setEntity(new UrlEncodedFormEntity( 
      java.util.Arrays.asList( new BasicNameValuePair("tuple", tuple) )
    ))
    client.execute(post)
  }

  def enter(){
    post(enterTuple)
  }

  def leave(){
    post(leaveTuple)
  }

}
