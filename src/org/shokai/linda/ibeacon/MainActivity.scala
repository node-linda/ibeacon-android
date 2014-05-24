package org.shokai.linda.ibeacon;

import android.app.{Activity, PendingIntent};
import android.os.Bundle;
import android.content.{Context, Intent, SharedPreferences};
import android.widget.{Button, SeekBar, TextView};
import android.view.View;
import android.util.Log;

class MainActivity extends Activity{
  import scala.collection.immutable.Range;

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val version:String = getResources().getString(R.string.version)
  lazy val btnStart:Button = findViewById(R.id.btnStart).asInstanceOf[Button]
  lazy val btnStop:Button = findViewById(R.id.btnStop).asInstanceOf[Button]
  lazy val serviceIntent:Intent = new Intent(this, classOf[LindaService])

  lazy val pref:SharedPreferences = getSharedPreferences(appName, Context.MODE_PRIVATE)
  lazy val prefEdit:SharedPreferences.Editor = pref.edit()
  lazy val rssiRange:Range = Range(getResources().getInteger(R.integer.rssi_min),
                                   getResources().getInteger(R.integer.rssi_max))
  lazy val seekBarRssi:SeekBar = findViewById(R.id.seekBarRssi).asInstanceOf[SeekBar]
  lazy val textViewRssi:TextView = findViewById(R.id.textViewRssi).asInstanceOf[TextView]

  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    var threshold:Int = pref.getInt("threshold", rssiRange(rssiRange.size/2))

    print(s"app start (threshold:${threshold})")

    val self = this

    setTitle(s"${appName} v${version}")

    btnStart.setOnClickListener( new View.OnClickListener(){
      override def onClick(v:View){
        prefEdit.putInt("threshold", threshold)
        prefEdit.commit()
        self.startService(serviceIntent)
      }
    })

    btnStop.setOnClickListener( new View.OnClickListener(){
      override def onClick(v:View){
        self.stopService(serviceIntent)
      }
    })

    textViewRssi.setText(s"Beacon RSSI Threshold : ${threshold}")
    seekBarRssi.setMax(rssiRange.max - rssiRange.min + 2)
    seekBarRssi.setProgress(threshold - rssiRange.min)

    seekBarRssi.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
      override def onProgressChanged(seekBar:SeekBar, value:Int, fromTouch:Boolean){
        threshold = rssiRange.min+value
        textViewRssi.setText(s"Beacon RSSI Threshold : ${threshold}")
      }
      override def onStartTrackingTouch(seekBar:SeekBar){
      }
      override def onStopTrackingTouch(seekBar:SeekBar){
      }
    })

  }

  def print(msg:String){
    Log.v(appName, msg)
  }
}
