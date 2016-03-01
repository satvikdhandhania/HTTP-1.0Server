
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class parser {


	public static void main(String[] args) {
		String s =  "GET /www/cgi-bin/a.out?a=10&b=satvik HTTP/1.0\r\nHost: localhost:8080\r\nUser-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\nAccept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";
		String path = System.getProperty("user.dir");
		String brokenRequest[];
		brokenRequest = s.split("\r\n");
		for(int i=0;i<brokenRequest.length;i++)
		{
			System.out.println(brokenRequest[i]);
		}
		if(brokenRequest.length>1)
		{
			boolean flag = false;
			String statusLine[];
			statusLine = brokenRequest[0].split(" ");
			System.out.println(statusLine[0]);
			if(statusLine[0].equals("GET")||statusLine[0].equals("HEAD"))
			{	
				if(statusLine[2].equals("HTTP/1.0"))
				{
					flag=true;
				}
			}
			if(flag)
			{
				if(statusLine[1].contains("cgi-bin"))
				{
					System.out.println("Contains CGI-BIN");
					ArrayList<String> list = new ArrayList<String>();
					String commands[];
					if(statusLine[1].substring(1).contains("?"))
					{
						commands = statusLine[1].substring(1).split("\\?");
						list.add(path+commands[0]);
						String parameters[] = commands[1].split("&");
						for(int i=0;i<parameters.length;i++)
						{
							String nameValue[] = parameters[i].split("=");
							list.add(nameValue[1]);
							System.out.println(nameValue[1]);
						}
					}
					else
					{
						list.add(path+statusLine[1].substring(1));
					}	

					ProcessBuilder pb = new ProcessBuilder(list);
					System.out.println(""+pb.command());
					Process process = null;
					try {
						process = pb.start();
						InputStream is = process.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);
						String l;
						StringBuilder sb = new StringBuilder();
						while ((l = br.readLine()) != null) {
							System.out.println(l);
							sb.append(l);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}		

					System.out.println("Program terminated!");


				}
			}
		}

	}
}
