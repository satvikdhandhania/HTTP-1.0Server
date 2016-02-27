/**
 * @file: Client.java
 * 
 * @author: Chinmay Kamat <chinmaykamat@cmu.edu>
 * 
 * @date: Feb 15, 2013 1:14:09 AM EST
 * 
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		Socket sock;
		int port = 8080;
		InetAddress addr = null;
		BufferedReader inStream = null;
		DataOutputStream outStream = null;
		String buffer = null;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		/* Parse parameter and do args checking */
		if (args.length < 2) {
			System.err.println("Usage: java Client <server_ip> <server_port>");
			System.exit(1);
		}

		try {
			port = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.err.println("Usage: java Client <server_ip> <server_port>");
			System.exit(1);
		}

		if (port > 65535 || port < 1024) {
			System.err.println("Port number must be in between 1024 and 65535");
			System.exit(1);
		}

		try {
			/* Get the server adder in InetAddr format */
			addr = InetAddress.getByName(args[0]);
			System.out.println(addr);
			
		} catch (UnknownHostException e) {
			System.err.println("Invalid address provided for server");
			System.exit(1);
		}

		while (true) {
			try {
				/* Read data from the user */
				//buffer = br.readLine();
				buffer ="GET / HTTP/1.1\nHost: localhost:8080\r\n"
						+"\nUser-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
						+"\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
						+"\nAccept-Language: en-US,en;q=0.5\r\n"
						+"\nAccept-Encoding: gzip, deflate\r\n"
						+"\nConnection: keep-alive\r\n\r\n";
				/*
				 * connect() to the server at addr:port. The server needs to be
				 * listen() in order for this to succeed. This call initiates
				 * the SYN-SYN/ACK-ACK handshake
				 */
				sock = new Socket(addr, port);
			} catch (Exception e) {
				System.err.println("Unable to reach server");
				continue;
			}
			try {
				inStream = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				/* Write the date to the server */
				outStream = new DataOutputStream(sock.getOutputStream());
				outStream.writeChars(buffer.toString());
				outStream.writeChar('\n');
				outStream.flush();
				/* Read the data echoed by the server */
				buffer =  null;
				StringBuilder stringBuilder = new StringBuilder();
				int c;
				while(inStream.ready()==true)
				{				
					if((c = inStream.read())!=-1)
					{	
						stringBuilder.append((char)c);
						buffer = stringBuilder.toString();
					}
				}
				System.out.println("Received : " + buffer.toString());
				sock.close();
			} catch (IOException e) {
				continue;
			}
		}
	}
}
