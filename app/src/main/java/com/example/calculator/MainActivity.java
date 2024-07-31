package com.example.calculator;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String[] numberArr = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "zero"};
        String[] opArr = {"mod", "residual", "multi", "sub", "push", "point"};
        TextView textView = findViewById(R.id.textView);
        setNumClick(numberArr, textView);
        setOpClick(opArr, textView);
        cleanText(textView);
        deleteChar(textView);
        getResult(textView);
    }

    private void getResult(TextView textView) {
        Button button = findViewById(R.id.equal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = textView.getText().toString();
                boolean negativeState = Pattern.compile("^-\\d+").matcher(s).find();
                Pattern compressionPattern = Pattern.compile("((?<=\\d)[+\\-*÷%])");
                Pattern numberPattern = Pattern.compile("\\d+\\.*\\d*");
                Matcher compressionMatcher = compressionPattern.matcher(s);
                Matcher numberMatcher = numberPattern.matcher(s);
                List<String> compressionList = new ArrayList<>();
                List<Double> numberList = new ArrayList<>();
                while (compressionMatcher.find()) {
                    compressionList.add(compressionMatcher.group());
                }
                while (numberMatcher.find()) {
                    numberList.add(Double.parseDouble(numberMatcher.group()));
                }
                if (numberList.size() == compressionList.size() + 1) {
                    double sum = 0;
                    for (int temp = 0; temp < numberList.size(); temp++) {
                        if (temp == 0) {
                            if (negativeState) {
                                sum -= numberList.get(temp);
                            } else {
                                sum += numberList.get(temp);
                            }
                        } else {
                            Log.d("lestia", compressionList.get(temp - 1));
                            Double number = numberList.get(temp);
                            switch (compressionList.get(temp - 1)) {
                                case "+":
                                    sum += number;
                                    break;
                                case "-":
                                    sum -= number;
                                    break;
                                case "*":
                                    sum *= number;
                                    break;
                                case "÷":
                                    sum /= number;
                                    break;
                                case "%":
                                    sum %= number;
                                    break;
                            }
                        }

                    }

                    if((int)sum != sum){
                        textView.setText(String.valueOf(sum));
                    }else{
                        textView.setText(String.valueOf((int)sum));
                    }

                }
            }
        });

    }


    private void deleteChar(TextView textView) {
        Button button = findViewById(R.id.delete);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = textView.getText().toString();
                if (str.length() == 1) {
                    textView.setText("0");
                } else {
                    String result = str.substring(0, str.length() - 1);
                    textView.setText(result);
                }
            }
        });
    }

    private void cleanText(TextView textView) {
        Button button = findViewById(R.id.clean);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textView.setText("0");
            }
        });
    }

    private void setOpClick(String[] opArr, TextView textView) {
        for (int temp = 0; temp < opArr.length; temp++) {
            String[] transArr = {"%", "÷", "*", "-", "+", "."};
            int resoureId = getResources().getIdentifier(opArr[temp], "id", getPackageName());
            Button button = findViewById(resoureId);
            int finalTemp = temp;
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    textView.append(transArr[finalTemp]);
                }
            });
        }
    }

    private void setNumClick(String[] numberArr, TextView textView) {
        for (int temp = 0; temp < numberArr.length; temp++) {
            String[] transArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
            int resoureId = getResources().getIdentifier(numberArr[temp], "id", getPackageName());
            Button button = findViewById(resoureId);
            int finalTemp = temp;
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (textView.getText().toString().equals("0")) {
                        textView.setText(transArr[finalTemp]);
                    } else {
                        textView.append(transArr[finalTemp]);
                    }
                }
            });
        }
    }
}