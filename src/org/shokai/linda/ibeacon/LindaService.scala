package org.shokai.linda.ibeacon;

import org.shokai.ibeacon.{IBeacon, Beacon};
import android.app.{Service, IntentService};
import android.os.{Bundle, IBinder, Looper, Handler, HandlerThread, Process, Message};
import android.content.{Context, Intent, SharedPreferences};
import android.util.Log;

class LindaService extends Service{

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val iBeacon:IBeacon = new IBeacon(this)
  lazy val notifer:Notifer = new Notifer(appName, this)
  lazy val region = new Region(getResources().getString(R.string.linda),
                               getResources().getString(R.string.tuplespace),
                               "shokai")
  lazy val rssiRange:Range = Range(getResources().getInteger(R.integer.rssi_min),
                                   getResources().getInteger(R.integer.rssi_max))
  lazy val pref:SharedPreferences = getSharedPreferences(appName, Context.MODE_PRIVATE)
  lazy val threshold:Int = pref.getInt("threshold", rssiRange(rssiRange.size/2))

  lazy val uuid = getResources().getString(R.string.uuid)
  lazy val major = getResources().getString(R.string.major)
  lazy val minor = getResources().getString(R.string.minor)

  var handler:Handler = null

  override def onCreate{
    alert(s"start service (threshold:${threshold})")

    var thread:HandlerThread = new HandlerThread(appName, Process.THREAD_PRIORITY_BACKGROUND)
    thread.start()
    val looper = thread.getLooper()
    handler = new Handler(looper){
      override def handleMessage(msg:Message){
        print(s"watch UUID=${uuid} major=${major} minor=${minor}")
        iBeacon.onRegion(uuid, major, minor, Range(-90, threshold), (beacon:Beacon) => {
          alert(s"outside of DeltaS112 (RSSI:${beacon.rssi})")
          region.leave()
        })

        iBeacon.onRegion(uuid, major, minor, Range(threshold, 0), (beacon:Beacon) => {
          alert(s"door open DeltaS112 (RSSI:${beacon.rssi})")
          region.enter()
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
    alert("stop service")
  }

  def print(msg:String){
    Log.v(appName, msg)
  }

  def alert(msg:String){
    print(msg)
    notifer.popup(msg)
  }

}
