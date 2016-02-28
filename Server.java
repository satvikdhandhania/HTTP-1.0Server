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
	private static String notImplementedHTML = "<!DOCTYPE html><html><body>Not Implemented</body></html>";
	private static String fileNotFoundStatusLine = "HTTP/1.0 404 Not Found\r\n";
	private static String fileNotFoundHTML = "<head><title>Error response</title></head>"
			+ "<body><h1>Error response</h1><p>Error code 404.<p>Message: Not Found."
			+ "<p>Error code explanation: 404 = Nothing matches the given URI.</body>";

	public static void main(String args[]) {
		int port = 8001;
		
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
		processIncoming();
		
	}
	static void processIncoming()
	{
		String buffer = null;
		int c;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader inStream = null;
		DataOutputStream outStream = null;

		Socket clientSock;

		while (true) {
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
					}
				}
				String fileName= null;

				buffer = stringBuilder.toString();
				System.out.print("Read from client "
						+ clientSock.getInetAddress() + ":"
						+ clientSock.getPort() + " " + buffer);
				String brokenRequest[];
				brokenRequest = buffer.split("\r\n");
				if(brokenRequest.length>1)
				{
					boolean flag = false;
					String statusLine[];
					statusLine = brokenRequest[0].split(" ");
					System.out.println(statusLine[0]);
					if(statusLine[0].equals("GET")||statusLine[0].equals("HEAD"))
					{	




						/*
						 * 
						 * 
						 * 
						 * 
						 * Change HTTP 1.0
						 */
						if(statusLine[2].equals("HTTP/1.1")||statusLine[2].equals("HTTP/1.0"))
						{
							flag=true;
						}
					}
					if(flag)
					{
						if(statusLine[1].length()>2)
						{
							fileName = statusLine[1].substring(1);

						}
						else
							fileName = "index.html";
					}
					else
					{
						buffer = notImplementedStatusLine+serverLine+endRequest+notImplementedHTML;
						outStream.writeBytes(buffer);
						outStream.flush();
						/* Interaction with this client complete, close() the socket */
						clientSock.close();
						continue;
					}
					System.out.println(fileName);
				}
				else
					continue;

				buffer = null;
				FileInputStream fin;
				StringBuilder s1 = new StringBuilder();
				try{
					fin = new FileInputStream(fileName);
					while((c=fin.read())!=-1){
						s1.append((char)c);
					}
					fin.close();
				}catch(Exception e)
				{
					e.printStackTrace();
					buffer = fileNotFoundStatusLine+serverLine+endRequest+fileNotFoundHTML;
					outStream.writeBytes(buffer);
					outStream.flush();
					clientSock.close();
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

