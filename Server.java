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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static ServerSocket srvSock;
	private static String successStatusLine = "HTTP/1.0 200 OK\r\n";
	private static String serverLine = "Server: Simple/1.0\r\n";
	private static String contentType = "Content-Type:";
	private static String endRequest = "\r\n\r\n";
	private static String notImplementedStatusLine = "HTTP/1.0 501 Not Implemented\r\n";

	public static void main(String args[]) {
		String buffer = null;
		//String 
		int port = 8080;
		int c;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader inStream = null;
		DataOutputStream outStream = null;

		/* Parse parameter and do args checking */
		if (args.length < 1) {
			System.err.println("Usage: java Server <port_number>");
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

		while (true) {
			Socket clientSock;
			try {
				/*
				 * Get a sock for further communication with the client. This
				 * socket is sure for this client. Further connections are still
				 * accepted on srvSock
				 */
				buffer = null;
				stringBuilder.setLength(0);
				clientSock = srvSock.accept();
				System.out.println("Accpeted new connection from "
						+ clientSock.getInetAddress() + ":"
						+ clientSock.getPort());
			} catch (IOException e) {
				continue;
			}
			try {
				inStream = new BufferedReader(new InputStreamReader(
						clientSock.getInputStream()));
				outStream = new DataOutputStream(clientSock.getOutputStream());
				/* Read the data send by the client */
				while(inStream.ready()==true)
				{				
					if((c = inStream.read())!=-1)
					{	
						stringBuilder.append((char)c);
						buffer = stringBuilder.toString();
					}
				}

				System.out.println("Read from client "
						+ clientSock.getInetAddress() + ":"
						+ clientSock.getPort() + " " + buffer);
				buffer = null;
				String fileName= "10.png";
				StringBuilder s1 = new StringBuilder();
				try{
					FileInputStream fin = new FileInputStream(fileName);
					while((c=fin.read())!=-1){
						s1.append((char)c);
					}
				}catch(Exception e)
				{
					e.printStackTrace();
					continue;
				}
				String filecontents = s1.toString();
				buffer = successStatusLine+serverLine+contentType+GetMime.getMimeType(fileName)+endRequest+filecontents;

				/*
				 * Echo the data back and flush the stream to make sure that the
				 * data is sent immediately
				 */
				outStream.writeBytes(buffer);
				outStream.flush();
				/* Interaction with this client complete, close() the socket */
				clientSock.close();
			} catch (IOException e) {
				clientSock = null;
				continue;
			}
		}
	}
}
