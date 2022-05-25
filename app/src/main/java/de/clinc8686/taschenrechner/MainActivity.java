package de.clinc8686.taschenrechner;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String term = "5+6";
        Expression calc = new ExpressionBuilder(term).build();
        //test.setText(calc.evaluate() + "");
    }

    void action(View view) {
        Button bt = (Button) view;
        String input = bt.getText().toString();
        TextView result = findViewById(R.id.result);
        result.append(input);
        Expression calc = new ExpressionBuilder(result.getText().toString()).build();

    }

}