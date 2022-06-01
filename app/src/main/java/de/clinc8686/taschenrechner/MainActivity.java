package de.clinc8686.taschenrechner;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void action(View view) {
        //not multiple times allowed
        List<String> characterList = Arrays.asList("€", "/", "*", "+", "-", ".", "±");

        Button bt = (Button) view;
        String input = bt.getText().toString();
        TextView result = findViewById(R.id.result);
        String subResult = result.getText().toString();
        boolean greaterZero = subResult.length() > 0;

        switch (input) {
            case "=":
                calculate();
                break;
            case "C":
                if (greaterZero)
                    result.setText(result.getText().toString().substring(0, result.length() - 1));
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        result.setText("");
                        return true;    //return wert gibt an ob noch wietere operationen durchgefhrt werden sollen, wie z.B. Dropdown Menü öffnen usw... (true: nix mehr, false: weitere operation)
                    }
                });
                break;
            case "±":
                if (!(subResult.length() > 0))
                    result.append("(-");
                else
                    result.append("*(-1)");
                break;
            case "1/X":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("1/");
                break;
            case "X^2":
                if (subResult.length() > 0)
                    result.append("*");
                else
                   Snackbar.make(result,"Ungültige Eingabe", Snackbar.LENGTH_SHORT).show();
                break;
            case "√":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("sqrt(");
                break;
            case "X!":
                break;
            case "SIN":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("sin(");
                break;
            case "COS":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("cos(");
                break;
            case "TAN":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("tan(");
                break;
            case "π":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("π");
                break;
            case "LOG":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("log(");
                break;
            case "E^X":
                if (subResult.length() > 0)
                    result.append("*");
                result.append("e^(");
                break;
            default:
                if (greaterZero) {
                    if (characterList.contains(subResult.substring(subResult.length()-1)) /*&& !subResult.endsWith("€")*/) {
                        if (characterList.contains(input)) {
                            result.setText(subResult.substring(0,result.length()-1));
                        }
                    }
                }

                result.append(input);
                break;
        }
    }

    private void calculate() {
        TextView result = findViewById(R.id.result);
        String subResult = result.getText().toString();

        int counter = 0;
        for (char c : subResult.toCharArray())  {
            if (c == '(' )
                counter ++;

            if (c == ')')
                counter--;
        }
        for (int i = counter; i > 0; i--) {
            subResult = subResult + (")");
        }
        try {
            Expression calc = new ExpressionBuilder(subResult).build();
            result.setText(calc.evaluate() + "");
        } catch (IllegalArgumentException e) {
            Snackbar.make(result,"Da hat etwas nicht geklappt", Snackbar.LENGTH_SHORT).show();
            Log.e("Calculation failed", e.toString());
        } catch (ArithmeticException e) {
            Snackbar.make(result,"Division durch 0 nicht erlaubt", Snackbar.LENGTH_SHORT).show();
            Log.e("Calculation failed", e.toString());
        }
    }

    double oldAcceleration;
    double acceleration;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            oldAcceleration = acceleration;

            acceleration = Math.sqrt(x * x + y * y + z * z);
            double diff = acceleration - oldAcceleration;

            if (diff > 10 || diff < -10) {
                Log.e("sensor",diff+"");
                Log.e("sensor", "calculate");
                calculate();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}