package org.shokai.linda.ibeacon;

import android.content.{Context, BroadcastReceiver, Intent}
import android.util.Log;

class BootReceiver extends BroadcastReceiver {

  override def onReceive(context:Context, intent:Intent){
    val intent:Intent = new Intent(context, classOf[LindaService])
    context.startService(intent)
  }

}
