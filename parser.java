
public class parser {


	public static void main(String[] args) {
		String s =  "GET / HTTP/1.1\r\nHost: localhost:8080\r\nUser-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\nAccept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection: keep-alive\r\n\r\n";

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
			
		}
	}

	void doSomething()
	{
		
	}
}
