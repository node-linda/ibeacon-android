package org.shokai.linda.ibeacon;

import org.shokai.ibeacon.{IBeacon, Beacon};
import android.app.{Service, IntentService};
import android.os.{Bundle, IBinder, Looper, Handler, HandlerThread, Process, Message};
import android.content.Intent;
import android.util.Log;

class LindaService extends Service{

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val iBeacon:IBeacon = new IBeacon(this)
  lazy val notifer:Notifer = new Notifer(appName, this)
  val door = new Door()
  val uuid = "B0FC4601-14A6-43A1-ABCD-CB9CFDDB4013"
  val major = "0001"
  val minor = "0094"

  var handler:Handler = null

  override def onCreate{
    print("start service")

    var thread:HandlerThread = new HandlerThread(appName, Process.THREAD_PRIORITY_BACKGROUND)
    thread.start()
    val looper = thread.getLooper()
    handler = new Handler(looper){
      override def handleMessage(msg:Message){
        iBeacon.onRegion(uuid, major, minor, Range(-90, -70), (beacon:Beacon) => {
          print(s"outside of DeltaS112 (RSSI:${beacon.rssi})")
        })

        iBeacon.onRegion(uuid, major, minor, Range(-70, 0), (beacon:Beacon) => {
          print(s"door open DeltaS112 (RSSI:${beacon.rssi})")
          door.open()
        })
      }
    }
  }

  override def onStartCommand(intent:Intent, flags:Int, startId:Int):Int = {
    var msg:Message = handler.obtainMessage()
    msg.arg1 = startId
    handler.sendMessage(msg)
    return Service.START_STICKY
  }

  override def onBind(intent:Intent):IBinder = {
    return null
  }

  override def onDestroy(){
    print("stop service")
  }


  def print(msg:String){
    Log.v(appName, msg)
    notifer.popup(msg)
  }

}
