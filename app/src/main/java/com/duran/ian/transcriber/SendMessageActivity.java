package com.duran.ian.transcriber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SendMessageActivity extends AppCompatActivity {

    private EditText editMessage;

    private Communicator communicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        this.editMessage = (EditText) findViewById(R.id.edit_message);

        if(savedInstanceState != null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                String message = extras.getString("message");
                editMessage.setText(message);
            }
        }
    }
}
