package com.gmail.dajinchu.clubcopter;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private WifiP2pManager mWiFiManager;
    private WifiP2pManager.Channel mWiFiChannel;
    private WiFiBroadcastReceiver receiver;

    private List peers = new ArrayList();
    private ListView peerListView;
    private ArrayAdapter<String> peerAdapter;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mWiFiManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        mWiFiChannel = mWiFiManager.initialize(this, getMainLooper(), null);

        peerListView = (ListView) findViewById(R.id.devices);
        peerAdapter = new ArrayAdapter<String>(this, R.layout.peer_adapter,peers);
        peerListView.setAdapter(peerAdapter);
    }

    public void beginDiscovery(View v){
        mWiFiManager.discoverPeers(mWiFiChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {
                Log.d("MainActivity", "Failed to begin P2p discover. Error code " + reason);
            }
        });
    }

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            peerAdapter.notifyDataSetChanged();

        }
    };

    private WifiP2pManager.ConnectionInfoListener connectionListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            Log.d("MainActivity", "connected to " + info.groupOwnerAddress.getHostAddress());
            if(info.isGroupOwner){

            }
        }
    };

    public void connect(View v){
        WifiP2pDevice device = (WifiP2pDevice) peers.get(0);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mWiFiManager.connect(mWiFiChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i("MainActivity", "Success connection I think");
            }

            @Override
            public void onFailure(int reason) {
                Log.i("MainActivity", "failed to connect because "+reason);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        receiver = new WiFiBroadcastReceiver(mWiFiManager,mWiFiChannel,peerListListener, connectionListener);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
