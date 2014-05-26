package org.shokai.linda.ibeacon;

import collection.JavaConversions._
import org.shokai.ibeacon.Beacon;
import android.net.http.AndroidHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import android.util.Log;
import org.json.JSONObject;

class Region(baseUrl:String, space:String, who:String){

  val client = AndroidHttpClient.newInstance("linda-ibeacon-android")

  val tuple = new JSONObject(
    Map("type" -> "region",
        "name" -> "delta",
        "who" -> who)
  )

  private def post(tuple:String){
    val post = new HttpPost(s"${baseUrl}/${space}")
    post.setEntity(new UrlEncodedFormEntity( 
      List( new BasicNameValuePair("tuple", tuple) )
    ))
    client.execute(post)
  }

  def enter(){
    post(tuple.put("action","enter").toString())
  }

  def leave(){
    post(tuple.put("action","leave").toString())
  }

}
