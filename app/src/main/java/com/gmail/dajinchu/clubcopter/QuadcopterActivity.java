package com.gmail.dajinchu.clubcopter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectInputStream;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Sequencer;
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
        private Sequencer.ChannelCuePwmSpeed motor1 = new Sequencer.ChannelCuePwmSpeed();
        private Sequencer.ChannelCue[] cues = new Sequencer.ChannelCue[]{motor1};
        private Sequencer mSequencer;
        private DigitalOutput led;

        @Override
        public void setup() throws ConnectionLostException{
            //At 2M hz, we need 40k time units to get to 50hz
            final Sequencer.ChannelConfigPwmSpeed m1 = new Sequencer.ChannelConfigPwmSpeed(
                    Sequencer.Clock.CLK_2M,40000,1000,new DigitalOutput.Spec(1));
            final Sequencer.ChannelConfig[] configs = new Sequencer.ChannelConfig[]{m1};
            led = ioio_.openDigitalOutput(IOIO.LED_PIN);

            mSequencer = ioio_.openSequencer(configs);
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException{
            if(on){
                motor1.pulseWidth=1200;
            }else{
                motor1.pulseWidth=1000;
            }
            led.write(!on);
            mSequencer.manualStart(cues);
        }

        @Override
        public void disconnected(){
            //nothing
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
