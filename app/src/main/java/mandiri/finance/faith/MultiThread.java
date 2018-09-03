package mandiri.finance.faith;

import java.net.*;
import java.io.*;

public class MultiThread extends Thread{
	private Socket socket = null;
	private String nilai;
	//static final long delay = 60000 * 15; //1 menit = 60000 
	//private boolean isRunning = false;

    public MultiThread(Socket socket, String nilai) {
		super("MultiThread");
		this.socket = socket;
		this.nilai = nilai;
    }

    public void run() {
		try {
			OutputStream output = socket.getOutputStream();
			
			output.write(nilai.getBytes());
			
			nilai="";
			output.close();
			socket.close();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	
    }
}
