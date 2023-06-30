package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    EditText inputEditText;
    TextView resultTextView;
    Button addButton, subtractButton, multiplyButton, divideButton;
    Button clearButton, deleteButton, equalsButton, decimalButton;
    Button[] numberButtons = new Button[10];
    int[] numberButtonIds = {
            R.id.num0, R.id.num1, R.id.num2, R.id.num3, R.id.num4,
            R.id.num5, R.id.num6, R.id.num7, R.id.num8, R.id.num9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEditText = (EditText) findViewById(R.id.input);
        resultTextView = (TextView) findViewById(R.id.ans);

        //Disabling Virtual Keyboard
        View.OnTouchListener otl = new View.OnTouchListener() {
            public boolean onTouch (View v, MotionEvent event) {
                return true; // the listener has consumed the event
            }
        };
        inputEditText.setOnTouchListener(otl);


        addButton = findViewById(R.id.add);
        subtractButton = findViewById(R.id.sub);
        multiplyButton = findViewById(R.id.mul);
        divideButton = findViewById(R.id.div);

        clearButton = findViewById(R.id.clr);
        deleteButton = findViewById(R.id.del);
        equalsButton = findViewById(R.id.equals);
        decimalButton = findViewById(R.id.decimal);

        for (int i = 0; i < 10; i++) {
            final int number = i;
            numberButtons[i] = findViewById(numberButtonIds[i]);
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputEditText.append(String.valueOf(number));
                }
            });
        }

        //Adding Event Listeners to Respective Buttons
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOperation('+');
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOperation('-');
            }
        });

        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOperation('*');
            }
        });

        divideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOperation('/');
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEditText.setText("");
                resultTextView.setText("0");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputEditText.getText().toString();
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                    inputEditText.setText(input);
                }
            }
        });

        equalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = inputEditText.getText().toString();
                evaluate(expression);
            }
        });

        decimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputEditText.getText().toString();
                if (!input.contains(".")) {
                    inputEditText.append(".");
                }
            }
        });

        inputEditText.addTextChangedListener(textWatcher);
    }


    //Operators Appending
    void performOperation(char operator) {
        String input = inputEditText.getText().toString();
        if (!input.isEmpty()) {

            //Checking for Explicit Decimal at End
            if (input.charAt(input.length() - 1) == '.') {
                input = input.substring(0, input.length() - 1);
            }

            //Simply Append the operator
            inputEditText.setText(input + operator);
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // this function is called before text is edited
            // no need
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String expression = inputEditText.getText().toString();
            evaluate(expression);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // this function is called after text is edited
            // no need
        }
    };

    void evaluate(String expression){

        String input = inputEditText.getText().toString();

        try {
            if(input.charAt(input.length() - 1) == '+' || input.charAt(input.length() - 1) == '-' || input.charAt(input.length() - 1) == '/' || input.charAt(input.length() - 1) == '*'){
                if (input.charAt(input.length() - 2) == '+' || input.charAt(input.length() - 2) == '-' || input.charAt(input.length() - 2) == '/' || input.charAt(input.length() - 2) == '*') {
                    resultTextView.setText("Error");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);

            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, expression, "Javascript", 1, null).toString();
            resultTextView.setText(finalResult);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}