package com.duran.ian.transcriber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class SendMessageActivity extends AppCompatActivity {


    private EditText editMessage;
    private Button btnSendMessage;
    private Button btnChangeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        this.editMessage = (EditText) findViewById(R.id.edit_message);
        this.btnSendMessage = (Button) findViewById(R.id.btn_dispatch);
        this.btnChangeAddress = (Button) findViewById(R.id.btn_change_address);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String message = extras.getString("message");
            editMessage.setText(message);
        }


        this.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();
                SharedPreferences preferences = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);
                String address = preferences.getString(getString(R.string.address), "");

                if(message.equals(""))
                    Toast.makeText(SendMessageActivity.this, "There's no text", Toast.LENGTH_LONG).show();

                else if(address.equals(""))
                    Toast.makeText(SendMessageActivity.this, "There's no saved address", Toast.LENGTH_LONG).show();

                else
                    new SendMessageTask().execute(editMessage.getText().toString(), address);
            }
        });

        this.btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SendMessageActivity.this, AddressActivity.class);
                startActivity(i);
            }
        });
    }

    private static class SendMessageTask extends AsyncTask<String, Integer, Void>{

        private final int PORT = 6666;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Socket socket = new Socket(strings[1], PORT);
                PrintStream output = new PrintStream(socket.getOutputStream());
                output.print(strings[0]);
                output.flush();
                output.close();
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
