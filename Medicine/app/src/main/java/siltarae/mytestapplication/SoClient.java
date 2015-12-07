package siltarae.mytestapplication;

/**
 * Created by So-Yeong on 2015-06-09.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SoClient extends Thread {

    public final static String HOST = null;
    public final static int PORT = -1;

    private static Socket socket;

    public static Socket getSocket() throws IOException{
        if(socket == null)
            socket = new Socket();
        if(!socket.isConnected())
            socket.connect(new InetSocketAddress(HOST,PORT));

        return socket;
    }

    public static void closeSocket() throws IOException{
        if(socket != null)
            socket.close();
    }

    public static void sendMsg(String msg) throws IOException{

    }

}
