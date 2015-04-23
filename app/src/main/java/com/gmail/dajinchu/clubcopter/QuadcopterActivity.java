package com.gmail.dajinchu.clubcopter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.io.ObjectInputStream;

public class QuadcopterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

    }

    class Receive implements Runnable{

        private final ObjectInputStream readStream;

        public Receive(ObjectInputStream stream){
            readStream = stream;
        }
        @Override
        public void run() {
            String data;
            while(true){
                try {
                    if((data = readStream.readUTF())!=null){

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
