/**
 * @file: GetMime.java
 * 
 * @author: Chinmay Kamat <chinmaykamat@cmu.edu>
 * 
 * @date: Feb 14, 2013 11:45:22 PM EST
 * 
 */

import java.net.FileNameMap;
import java.net.URLConnection;

public class GetMime {

	/* Function which gets the MIME string and returns it */
	public static String getMimeType(String fileUrl) throws java.io.IOException {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type = fileNameMap.getContentTypeFor(fileUrl);
		return type;
	}

	public static void main(String args[]) throws Exception {
		/* Parse parameter and do args checking */
		if (args.length < 1) {
			System.err.println("Usage: java GetMime <filename>");
			System.exit(1);
		}
		System.out.println("File Name: " + args[0] + "\tMime Type: "
				+ GetMime.getMimeType(args[0]));
		System.exit(0);
	}
}