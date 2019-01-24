package com.ts.mobilepicklist.network;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by ts250231 on 2015-04-29.
 */
public class RapTcpClient {

   private int rapPort = 9999;

    DataOutputStream out;
    DataInputStream in;

    private OnMessageReceived mMessageListener = null;
    private String serverMessage;

//    public RapTcpClient(OnMessageReceived listener){
//        this.mMessageListener = listener;
//    }
    public RapTcpClient(){
        //this.mMessageListener = listener;
    }

    public void sendMessageToRap(String message) {

        try {
            InetAddress serverAddr = InetAddress.getByName(RAP.getInstance().getRapIP());

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, rapPort);

            try {
                //send the message to the server
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                Log.e("TCP Client", "C: Sent.");
                if(message == "UPDATE"){
                    byte[] buffer = RAP.getInstance().imageChunk;
                    out.write(buffer, 0, buffer.length);
                    RAP.getInstance().imageChunkWasSend = true;
                }
                else if(message == "UPDATEEND"){
                    byte[] buffer = RAP.getInstance().imageChunk;

                    out.write(buffer, 0, buffer.length);
                    RAP.getInstance().imageChunkWasSend = true;
                    getResponseFromServer();
                }
                else {
                    byte[] one = new byte[]{(byte) 0x01, (byte) 0x01}; //hexStringToByteArray("3131");
                    byte[] sendData = message.getBytes(Charset.forName("UTF-16LE"));
                    byte[] buffer = new byte[sendData.length + 2];
                    System.arraycopy(one, 0, buffer, 0, one.length);
                    System.arraycopy(sendData, 0, buffer, one.length, sendData.length);
                    out.write(buffer, 0, buffer.length);
                    Log.e("TCP Client", "C: Done.");
                    getResponseFromServer();
                }
            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
                RAP.getInstance().isConnectedToRap = true;

            }

        } catch (Exception e) {
            RAP.getInstance().comunicationError = true;
            Log.e("TCP", "C: Error", e);

        }
    }

    public void getResponseFromServer(){
        try {
            byte[] bufferIN = new byte[1024];
            in.read(bufferIN);
            serverMessage = new String(bufferIN, "UTF-16LE");
            if (serverMessage != null && mMessageListener != null) {
                //call the method messageReceived from MyActivity class
                mMessageListener.messageReceived(serverMessage);
            }
            RAP.getInstance().convertMessageFromRap(serverMessage);
            serverMessage = null;
            Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
        }catch(Exception ex){
            Log.e("MobilePickList", ex.getMessage());
        }
    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
