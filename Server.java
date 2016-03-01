/**
 * @file: Server.java
 * 
 * @author: Chinmay Kamat <chinmaykamat@cmu.edu>
 * 
 * @date: Feb 15, 2013 1:13:37 AM EST
 * 
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
	private static ServerSocket srvSock;
	// Default Path for My System
	private static String path = null;
	public static final int THREAD_POOL_SIZE = 25;
	
	public static void main(String args[]) {
		int port = 8001;
		Socket clientSock;
		/* Parse parameter and do args checking */
		if (args.length < 1) {
			System.err.println("Usage: java Server <port_number> <Absolute Path to www Directory>");
			System.exit(1);
		}

		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.err.println("Usage: java Server <port_number>");
			System.exit(1);
		}

		if (port > 65535 || port < 1024) {
			System.err.println("Port number must be in between 1024 and 65535");
			System.exit(1);
		}
		path = System.getProperty("user.dir")+"/www/";
		if(args.length ==2)
			path = args[1];
		try {
			/*
			 * Create a socket to accept() client connections. This combines
			 * socket(), bind() and listen() into one call. Any connection
			 * attempts before this are terminated with RST.
			 */
			srvSock = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Unable to listen on port " + port);
			System.exit(1);
		}
		//ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		
		
		
		while (true) {
			try {
				clientSock = srvSock.accept();
//				System.out.println("Accpeted new connection from "
//						+ clientSock.getInetAddress() + ":"
//						+ clientSock.getPort());
				//Runnable requestHandler = new HandleRequest(clientSock, path);
				//executorService.execute(requestHandler);
		
				Thread t1 = new Thread(new HandleRequest(clientSock, path));
				t1.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
	}
	
	
}

