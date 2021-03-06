package com.example.pulsometer.Logic.sapProvider;/*
 * Copyright (c) 2014 Samsung Electronics Co., Ltd. All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that 
 * the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright notice, 
 *       this list of conditions and the following disclaimer in the documentation and/or 
 *       other materials provided with the distribution. 
 *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its contributors may be used to endorse or 
 *       promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Model.PedometerSqlite;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAAuthenticationToken;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.security.cert.X509Certificate;

public class SAPServiceProvider extends SAAgent {
    public static final int SERVICE_CONNECTION_RESULT_OK = 0;
    public static final int CHANNEL_ID = 123;
    public static final String TAG = "Pulsometer";
    public Boolean isAuthentication = false;
    public Context mContext = null;

    private final IBinder mBinder = new LocalBinder();
//    private int authCount = 1;

    HashMap<Integer, SAPServiceProviderConnection> mConnectionsMap = null;

    public SAPServiceProvider() {
        super(TAG, SAPServiceProviderConnection.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SA mAccessory = new SA();
        try {
            mAccessory.initialize(this);
        } catch (SsdkUnsupportedException e) {
            // Error Handling
        } catch (Exception e1) {
            e1.printStackTrace();
            /*
             * Your application can not use Samsung Accessory SDK. You application should work smoothly
             * without using this SDK, or you may want to notify user and close your application gracefully (release
             * resources, stop Service threads, close UI thread, etc.)
             */
            stopSelf();
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    protected void onAuthenticationResponse(SAPeerAgent uPeerAgent, SAAuthenticationToken authToken, int error) {
        if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_CERTIFICATE_X509) {
            mContext = getApplicationContext();
            byte[] myAppKey = getApplicationCertificate(mContext);

            if (authToken.getKey() != null) {
                boolean matched = true;
                if (authToken.getKey().length != myAppKey.length) {
                    matched = false;
                } else {
                    for (int i = 0; i < authToken.getKey().length; i++) {
                        if (authToken.getKey()[i] != myAppKey[i]) {
                            matched = false;
                        }
                    }
                }
                if (matched) {
                    acceptServiceConnectionRequest(uPeerAgent);
                }
            }
        } else if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_NONE)
            Log.e(TAG, "onAuthenticationResponse : CERT_TYPE(NONE)");
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket thisConnection, int result) {
        if (result == CONNECTION_SUCCESS) {
            if (thisConnection != null) {
                SAPServiceProviderConnection myConnection = (SAPServiceProviderConnection) thisConnection;

                if (mConnectionsMap == null) {
                    mConnectionsMap = new HashMap<Integer, SAPServiceProviderConnection>();
                }
                myConnection.mConnectionId = (int) (System.currentTimeMillis() & 255);
                mConnectionsMap.put(myConnection.mConnectionId, myConnection);
            }
        }
        else if (result == CONNECTION_ALREADY_EXIST) {
            Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");
        }
    }

    @Override
    protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {
        /*
         * The authenticatePeerAgent(peerAgent) API may not be working properly depending on the firmware
         * version of accessory device. Recommend to upgrade accessory device firmware if possible.
         */

        // if(authCount%2 == 1)
        // isAuthentication = false;
        // else
        // isAuthentication = true;
        // authCount++;

        isAuthentication = false;

        if (isAuthentication) {
            Toast.makeText(getBaseContext(), "Authentication On!", Toast.LENGTH_SHORT).show();
            authenticatePeerAgent(peerAgent);
        }
        else {
            Toast.makeText(getBaseContext(), "Authentication Off!", Toast.LENGTH_SHORT).show();
            acceptServiceConnectionRequest(peerAgent);
        }
    }

    private static byte[] getApplicationCertificate(Context context) {
        if (context == null) {
            return null;
        }
        byte[] cert = null;
        String packageName = context.getPackageName();
        if (context != null) {
            try {
                PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                if (pkgInfo == null) {
                    return null;
                }
                Signature[] sigs = pkgInfo.signatures;
                if (sigs == null) {
                } else {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    ByteArrayInputStream stream = new ByteArrayInputStream(sigs[0].toByteArray());
                    X509Certificate x509cert = X509Certificate.getInstance(stream);
                    cert = x509cert.getPublicKey().getEncoded();
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (javax.security.cert.CertificateException e) {
                e.printStackTrace();
            }
        }
        return cert;
    }

    public class LocalBinder extends Binder {
        public SAPServiceProvider getService() {
            return SAPServiceProvider.this;
        }
    }

    public class SAPServiceProviderConnection extends SASocket {
        private int mConnectionId;
        private java.util.Date date = new java.util.Date();
        public SAPServiceProviderConnection() {
            super(SAPServiceProviderConnection.class.getName());

        }

        @Override
        public void onError(int channelId, String errorString, int error) {
        }

        @Override
        public void onReceive(int channelId, byte[] data) {
            //Time time = new Time();
            //time.set(System.currentTimeMillis());
            //String timeStr = " " + String.valueOf(time.minute) + ":" + String.valueOf(time.second);
            //String strToUpdateUI = new String(data);
            //final String message = strToUpdateUI.concat(timeStr);
            final SAPServiceProviderConnection uHandler = mConnectionsMap.get(Integer.parseInt(String.valueOf(mConnectionId)));
            if (uHandler == null) {
                return;
            }
            try {
                String response = new String(data, "UTF-8");
                if (response.contains("H")) {
                    GlobalVariables.Pulses.add(Integer.parseInt(response.replace("H ", "")));
                } else if (response.contains("P")) {
                    GlobalVariables.StepsNumber = (Long.parseLong(response.replace("P ", "")));
                    Date dateNow = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    boolean exist = new Select()
                            .from(PedometerSqlite.class)
                            .where("Date = ?", dateFormat.format(dateNow))
                            .exists();
                    if (!exist) {
                        PedometerSqlite pedometerSqlite = new PedometerSqlite();
                        pedometerSqlite.Date = dateFormat.format(dateNow);
                        pedometerSqlite.StepsNumber = GlobalVariables.StepsNumber;
                        pedometerSqlite.save();
                    } else {
                        PedometerSqlite pedometerSqlite = new Select()
                                .from(PedometerSqlite.class)
                                .where("Date = ?", dateFormat.format(dateNow))
                                .executeSingle();
                        new Delete()
                                .from(PedometerSqlite.class)
                                .where("Date = ?", dateFormat.format(dateNow))
                                .execute();
                        pedometerSqlite.StepsNumber = GlobalVariables.StepsNumber;
                        pedometerSqlite.save();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            /*new Thread(new Runnable() {
                public void run() {
                    try {
                        uHandler.send(CHANNEL_ID, message.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/
        }

        @Override
        protected void onServiceConnectionLost(int errorCode) {
            if (mConnectionsMap != null) {
                mConnectionsMap.remove(mConnectionId);
            }
        }
    }
}
