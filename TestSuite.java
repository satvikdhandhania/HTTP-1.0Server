import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestSuite {
	public static void main(String[] args) {
		Socket sock =null;
		int port = 8001;
		InetAddress addr = null;
		BufferedReader inStream = null;
		DataOutputStream outStream = null;
		String buffer = null;


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
			//System.out.println(addr);
		} catch (UnknownHostException e) {
			System.err.println("Invalid address provided for server");
			System.exit(1);
		}

		int i = 0;
		String[] testCases = new String[10];

		testCases[0]="GET /index.html HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		testCases[1]="HEAD /index.html HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		testCases[2]="GET / HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		testCases[3]="HEAD / HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		testCases[4]="POST /index.html HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		testCases[5]="POST / HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		testCases[6]="GET /ind.html HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: textgt/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";

		testCases[7]="GET /cgi-bin/a.out?a=satvik HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: textgt/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";

		testCases[8]="GET /cgi-bin/a.outq HTTP/1.0\r\n"
				+"Host:localhost:9001\r\n"
				+"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\n"
				+"Accept: textgt/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
				+"Accept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		while (i<9) 
		{
			try {

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
				buffer = testCases[i];
				outStream.writeBytes(buffer);

				outStream.flush();
				/* Read the data echoed by the server */
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while((line = inStream.readLine())!=null)
				{	
					stringBuilder.append(line);
					stringBuilder.append("\r\n");
				}
				System.out.print("TestCase : ");System.out.print(i);System.out.print("\n");
				System.out.println("Request : " + buffer);
				System.out.println("responseRecieved : " + stringBuilder.toString());
				/* Close the connection and wait for next input */
				sock.close();
			} catch (IOException e) {
				continue;
			}
			i++;
		}
	}
}
