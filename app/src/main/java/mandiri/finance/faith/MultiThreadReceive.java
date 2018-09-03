package mandiri.finance.faith;

import java.net.*;
import java.io.*;


public class MultiThreadReceive extends Thread{
	private Socket socket = null;
	String result=null;
	
    public MultiThreadReceive(Socket socket) {
		super("MultiThreadReceive");
		this.socket = socket;
    }

    public void run() {

		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			result = input.readLine();
			GlobalVariable.setResult(result);
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	
    }
}
