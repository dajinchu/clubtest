package com.gmail.dajinchu.clubcopter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import static android.net.wifi.p2p.WifiP2pManager.*;

/**
 * Created by Da-Jin on 4/21/2015.
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver{
    private final WifiP2pManager mManager;
    private final Channel mChannel;
    private final PeerListListener peerListListener;
    private final ConnectionInfoListener connectionListener;
    String LOG = "wifiDirect";


    public WiFiBroadcastReceiver(WifiP2pManager manager, Channel channel, PeerListListener peerListener, ConnectionInfoListener connectionInfoListener){
        mManager=manager;
        mChannel=channel;
        peerListListener=peerListener;
        connectionListener = connectionInfoListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(EXTRA_WIFI_STATE, -1);
            if (state == WIFI_P2P_STATE_ENABLED) {
                Log.i(LOG, "wifip2p enabled");
            } else {
                Log.i(LOG, "wifip2p disabled");
            }
        } else if (WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.
            if(mManager!=null){
                mManager.requestPeers(mChannel,peerListListener);
            }
            Log.i(LOG, "wifip2p peer list changed");

        } else if (WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.
            Log.i(LOG, "connection state changed");

            if(mManager==null){
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(EXTRA_NETWORK_INFO);

            if(networkInfo.isConnected()){
                mManager.requestConnectionInfo(mChannel, connectionListener);
            }


        } else if (WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            intent.getParcelableExtra(
                    EXTRA_WIFI_P2P_DEVICE);
            Log.i(LOG, " this device changed");
        }
    }
}
