package com.duran.ian.transcriber;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddressActivity extends AppCompatActivity {

    private EditText editFirstNumber;
    private EditText editSecondNumber;
    private EditText editThirdNumber;
    private EditText editFourthNumber;

    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        this.editFirstNumber = (EditText) findViewById(R.id.edit_first_number);
        this.editSecondNumber = (EditText) findViewById(R.id.edit_second_number);
        this.editThirdNumber = (EditText) findViewById(R.id.edit_third_number);
        this.editFourthNumber = (EditText) findViewById(R.id.edit_fourth_number);

        this.btnSave = (Button) findViewById(R.id.btn_save);

        final SharedPreferences preferences = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);
        String currentAddress = preferences.getString(getString(R.string.address), "");

        if(!currentAddress.equals("")){
            String[] numbers = currentAddress.split(".");
            this.editFirstNumber.setHint(numbers[0]);
            this.editSecondNumber.setHint(numbers[1]);
            this.editThirdNumber.setHint(numbers[2]);
            this.editFourthNumber.setHint(numbers[3]);
        }

        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNumber = editFirstNumber.getText().toString();
                String secondNumber = editSecondNumber.getText().toString();
                String thirdNumber = editThirdNumber.getText().toString();
                String fourthNumber = editFourthNumber.getText().toString();

                if(!firstNumber.equals("") && !secondNumber.equals("") && !thirdNumber.equals("") && !fourthNumber.equals("")){
                    String newAddress = firstNumber + "." + secondNumber + "." + thirdNumber + "." + fourthNumber;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.address), newAddress);
                    editor.apply();
                }
            }
        });
    }
}
