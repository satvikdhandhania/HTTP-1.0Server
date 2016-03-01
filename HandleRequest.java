import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HandleRequest implements Runnable{

	private Socket clientSock;
	private static String path;

	private static String successStatusLine = "HTTP/1.0 200 OK\r\n";
	private static String serverLine = "Server: Simple/1.0\r\nConnection: Close\r\n";
	private static String dateHeader = "Date:";
	private static String contentType = "\r\nContent-Type:";
	private static String contentLength = "\r\nContent-Length:";
	private static String endRequest = "\r\n\r\n";
	private static String notImplementedStatusLine = "HTTP/1.0 501 Not Implemented\r\n";
	private static String fileNotFoundStatusLine = "HTTP/1.0 404 Not Found\r\n";
	private static String serverUnavailableStatusLine = "HTTP/1.0 503 Service Unavailable\r\n";
	private static String internalServerErrorStatusLine = "HTTP/1.0 500 Internal Server Error\r\n";
	private static String fileNotFoundHTML = "<head><title>Error response</title></head>"
			+ "<body><h1>Error response</h1><p>Error code 404.<p>Message: Not Found."
			+ "<p>Error code explanation: 404 = Nothing matches the given URI.</body>";
	private static String notImplementedHTML = "<head><title>Error response</title></head>"
			+ "<body><h1>Error response</h1><p>Error code 501.<p>Message: Not Implemented."
			+ "<p>Error code explanation: 501 = Method Not Implemented.</body>";
	private static String internalServerErrorHTML = "<head><title>Error response</title></head>"
			+ "<body><h1>Error response</h1><p>Error code 500.<p>Message: Internal Server Error."
			+ "<p>Error code explanation: 500 = syscall failure right now/other failures.</body>";
	private static String serverUnavailableErrorHTML = "<head><title>Error response</title></head>"
			+ "<body><h1>Error response</h1><p>Error code 503.<p>Message: Server Unavailable."
			+ "<p>Error code explanation: 503 = Server Busy.</body>";

	public HandleRequest(Socket clientSock, String path) {
		super();
		this.clientSock = clientSock;
		this.path = path;
	}

	@Override
	public void run() {
		SynchronizedCounter.increment();
		System.out.println(SynchronizedCounter.getValue());
		if( SynchronizedCounter.getValue() > Server.THREAD_POOL_SIZE)
		{
			serverUnavailable();
			SynchronizedCounter.decrement();
		}
		else
		{
			processIncoming();
			SynchronizedCounter.decrement();
		}
	}



	private void processIncoming()
	{
		String buffer = null;
		int c;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader inStream = null;
		DataOutputStream outStream = null;
		String statusLine[];


		try {
			inStream = new BufferedReader(new InputStreamReader(
					clientSock.getInputStream()));
			outStream = new DataOutputStream(clientSock.getOutputStream());
			String line;
			while(inStream.ready()==true)
			{				
				if((line = inStream.readLine())!=null)
				{	
					stringBuilder.append(line);
					stringBuilder.append("\r\n");
				}
			}


			String fileName= null;


			buffer = stringBuilder.toString();
//			System.out.print("Read from client "
//					+ clientSock.getInetAddress() + ":"
//					+ clientSock.getPort() + "\n" + buffer);
			String brokenRequest[];
			brokenRequest = buffer.split("\r\n");
			if(brokenRequest.length>1)
			{
				boolean flag = false;
				statusLine = brokenRequest[0].split(" ");
				//System.out.println(statusLine[0]);
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
						int len = statusLine[1].length()-1;
						char z = statusLine[1].charAt(len);

						//If relative path to folder append index.html to it
						if(z == '/')
							fileName = statusLine[1].substring(1)+"index.html";
						else
							fileName = statusLine[1].substring(1);

					}
					else
						fileName = "index.html";
				}
				else
				{
					buffer = notImplementedStatusLine+serverLine+dateHeader+getDate()+endRequest+notImplementedHTML;
					outStream.writeBytes(buffer);
					outStream.flush();
					/* Interaction with this client complete, close() the socket */
					clientSock.close();
					return;
				}
				//System.out.println(fileName);
			}
			else
			{
				clientSock.close();
				return;
			}
			buffer = null;
			FileInputStream fin;
			StringBuilder s1 = new StringBuilder();
			try{
				fin = new FileInputStream(path+fileName);
				while((c=fin.read())!=-1){
					s1.append((char)c);
				}
				fin.close();
			}catch(FileNotFoundException e)
			{
				e.printStackTrace();
				if(statusLine[0].equals("GET"))
					buffer = fileNotFoundStatusLine+serverLine+dateHeader+getDate()+endRequest+fileNotFoundHTML;
				else if(statusLine[0].equals("HEAD"))
					buffer = fileNotFoundStatusLine+serverLine+dateHeader+getDate()+endRequest;
				outStream.writeBytes(buffer);
				outStream.flush();
				clientSock.close();
				return;
			}catch (Exception e) {
				e.printStackTrace();
				if(statusLine[0].equals("GET"))
					buffer = internalServerErrorStatusLine+serverLine+dateHeader+getDate()+endRequest+internalServerErrorHTML;
				else if(statusLine[0].equals("HEAD"))
					buffer = internalServerErrorStatusLine+serverLine+dateHeader+getDate()+endRequest;
				outStream.writeBytes(buffer);
				outStream.flush();
				clientSock.close();
				return;
			}
			String filecontents = s1.toString();
			int fileSize = filecontents.length();
			if(statusLine[0].equals("GET"))
			{
				buffer = successStatusLine+serverLine+dateHeader+getDate()+contentType+GetMime.getMimeType(fileName)+contentLength+fileSize+endRequest+filecontents;
				//System.out.println(buffer);
			}else if(statusLine[0].equals("HEAD"))
				buffer = successStatusLine+serverLine+dateHeader+getDate()+contentType+GetMime.getMimeType(fileName)+contentLength+fileSize+endRequest;

			/*
			 * Echo the data back and flush the stream to make sure that the
			 * data is sent immediately
			 */
			outStream.writeBytes(buffer);
			outStream.flush();
			/* Interaction with this client complete, close() the socket */
			clientSock.close();
		} catch (IOException e) {
			try {
				clientSock.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}	

	private static String getDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");	
		Date date = new Date();
		return dateFormat.format(date);
	}
	private void serverUnavailable()
	{
		String buffer;
		//System.out.println("\ngckfjgncrkbmcthny\ncnhnynyny\nncnycnynytgvg\nService undfksgbfksdjnfsjhbdcnkxjdsnx cmnsdxdsvcs");
		
		DataOutputStream outStream;
		try {
			outStream = new DataOutputStream(clientSock.getOutputStream());
			buffer = serverUnavailableStatusLine+serverLine+dateHeader+getDate()+endRequest;//+serverUnavailableErrorHTML;
			outStream.writeBytes(buffer);
			outStream.flush();
			/* Interaction with this client complete, close() the socket */
			clientSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
			private static String handleCGI(String path)
			{
				Process process = new ProcessBuilder(
						path,"param1","param2").start();
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;

				//System.out.printf("Output of running %s is:", Arrays.toString(args));

				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
	 */
}
