package ya.sensorprogram;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.hardware.*;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

public class MainActivity extends Activity {

    ImageView iv;
    SensorManager sm;
    Sensor s;
    SampleSensorEventListener sse;
    float val;
    Button bt;
    int flag;
    EditText et;

    TextView[] tv=new TextView[3];

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        setContentView(ll);


        bt= new Button(this);
        bt.setText("記録");
        flag=0;

        et=new EditText(this);
        et.setLines(3);


        for(int i=0; i<3; i++)
        {
            tv[i] = new TextView(this);
        }

        tv[0].setText("X方向");
        tv[1].setText("Y方向");
        tv[2].setText("Z方向");
//        tv[2].setText();

//        ll.addView(iv);
        for(int i=0; i<3; i++)
        {
            tv[i].setTextSize(25);
            ll.addView(tv[i]);

        }
        ll.addView(bt);
        bt.setOnClickListener(new SampleClickListener());

        sse=new SampleSensorEventListener();
    }

// ボタン操作の設定
    class SampleClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            if (flag==0)
            {
                bt.setText("停止");
                flag=1;
                try {
                    FileOutputStream fos =
                            openFileOutput("Sample.txt", Context.MODE_WORLD_READABLE);
                    BufferedWriter bw =
                            new BufferedWriter(
                                    new OutputStreamWriter(fos));
                    bw.write(et.getText().toString());
                    bw.flush();
                    fos.close();
                    tv[2].setText("WORK");
                } catch (FileNotFoundException e) {
                    tv[2].setText("FNE");

                } catch (IOException e) {
                    tv[2].setText("IOE");
                }
            }
            else
            {
                bt.setText("記録");
                flag=0;
            }
        }
    }

// センサー関係
       protected void onResume()
    {
        super.onResume();
        sm=(SensorManager)getSystemService(
                Context.SENSOR_SERVICE);
        s=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(sse,s,SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause()
    {
        super.onPause();
        sm.unregisterListener(sse);
    }
    class SampleSensorEventListener
        implements SensorEventListener
    {
        public void onSensorChanged(SensorEvent e)
        {
            DecimalFormat df1=new DecimalFormat("##0.00");

            if(e.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            {
                float tmp= e.values[0]+e.values[1]+e.values[2];

                tv[0].setText("X方向 "+df1.format(e.values[0]));
                tv[1].setText("Y方向 "+df1.format(e.values[1]));
//                tv[2].setText("Z方向 "+df1.format(e.values[2]));
                if(flag==0) {
//                    tv[2].setText(String.valueOf(flag));
                }
                else {
//                    tv[2].setText(String.valueOf(flag));
                }
            }
        }
        public void onAccuracyChanged(Sensor s,int accuracy){}
    }
}
