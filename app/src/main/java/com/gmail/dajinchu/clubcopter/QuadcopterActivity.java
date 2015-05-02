package com.gmail.dajinchu.clubcopter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectInputStream;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class QuadcopterActivity extends IOIOActivity {
    Button go,stop;
    boolean on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quad);

        go = (Button) findViewById(R.id.go);
        stop = (Button) findViewById(R.id.stop);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = true;
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = false;
            }
        });

        Intent intent = getIntent();
    }

    class Looper extends BaseIOIOLooper{

        private PwmOutput motor1;
        private PwmOutput motor2;
        private PwmOutput motor3;
        private PwmOutput motor4;
        private DigitalOutput led;

        @Override
        public void setup() throws ConnectionLostException{
            motor1 = ioio_.openPwmOutput(1,50);
            motor2 = ioio_.openPwmOutput(2,50);
            motor3 = ioio_.openPwmOutput(3,50);
            motor4 = ioio_.openPwmOutput(4,50);
            led = ioio_.openDigitalOutput(IOIO.LED_PIN);

        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException{
            if(on){
                setAll(1200);
            }else{
                setAll(1000);
            }
            led.write(!on);
        }

        @Override
        public void disconnected(){
            //nothing
        }

        public void setAll(int value) throws ConnectionLostException {
            motor1.setPulseWidth(value);
            motor2.setPulseWidth(value);
            motor3.setPulseWidth(value);
            motor4.setPulseWidth(value);
        }
    }

    @Override
    protected IOIOLooper createIOIOLooper(){
        return new Looper();
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
