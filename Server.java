/*
 * Server.java
 */

import java.io.*;
import java.net.*;
import java.util.*;


public class Server {

	/**
	 * 
	 * @filename :"file path"
	 * @returns : read from file and stroed them in arrayList
	 */
	public static ArrayList<String> readFromFile(String filename) throws IOException {

		ArrayList<String> data = new ArrayList<String>();

		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));

		String line = "";

		while ((line = br.readLine()) != null) {
			if (line.trim().isEmpty())
				continue; // don't insert empty lines
			data.add(line);
		}

		br.close();
		return data;
	}

	/**
	 * @filename :"file path"
	 * @returns <void> : writes (append) text to a file
	 *          Used to store message and user sessoin
	 */

	public static void writeTofile(String filename, String text) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename), true));
		writer.write(text);
		writer.close();
	}

	public static final int SERVER_PORT = 5432;

	public static void main(String args[]) {

		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		String line;
		ServerSocket myServerice = null;
		Socket serviceSocket = null;

        // list of user name and password 
		Map <String, String> userInfo = new HashMap<String, String>();
		userInfo.put("root","root01");
		userInfo.put("john","john01");
		userInfo.put("david","david01");
		userInfo.put("mary","mary01");

		// Try to open a server socket
		try {
			myServerice = new ServerSocket(SERVER_PORT);

		} catch (Exception e) {
			System.out.println(e);
		}

		// Create a socket object from the ServerSocket to listen and accept
		// connections.
		// Open input and output streams

		while (true) {
			try {
				serviceSocket = myServerice.accept();
				inputStreamReader = new InputStreamReader(serviceSocket.getInputStream());
				outputStreamWriter = new OutputStreamWriter(serviceSocket.getOutputStream());

				bufferedReader = new BufferedReader(inputStreamReader);
				bufferedWriter = new BufferedWriter(outputStreamWriter);

				// word of teh day 
			    int wordNum = 0;
				ArrayList<String> word = readFromFile("word.txt");
				Map <String, String> session = new HashMap<String, String>();

				// As long as we receive data, echo that data back to the client.
				while ((line = bufferedReader.readLine()) != null) {

					System.out.println("Client CMD:"+line);

					
					if (line.equals("MSGGET")) {

						System.out.println("insider msgget");

						
						// writes back to client
						
						bufferedWriter.write("200 OK");
						bufferedWriter.write(word.get(wordNum%word.size()));
						wordNum++;
					
					} else if (line.equals("MSGSTORE")) { //TODO:
						if(session.size() == 1){
							bufferedWriter.write("200 OK");

						}

						
					} else if (line.contains("LOGIN")) {
						String login[] = line.split(" "); // LOGIN USER PASS => [LOGIN,USER,PASS]
						if(login.length < 3){
							bufferedWriter.write("404 incomplete command.");
						}
						else if(userInfo.containsKey(login[1]) && userInfo.get(login[1]).equals(login[2])){
							session.put(login[1],login[2]);
						}
						else{
							bufferedWriter.write("404 BD request.");
						}

					} else if (line.equals("SHUTDOWN")) {

						if(session.size() > 0 && session.containsKey("root")){
							bufferedWriter.write("200 OK");
								bufferedReader.close();
								bufferedWriter.close();
								serviceSocket.close();
								myServerice.close();
								session.clear();
								word.clear();

								System.out.println("Server is Successfully Shutdown");
								System.exit(0);

						}
						else{
							bufferedWriter.write("300 user must be root.");
						}
					
						
					} else if (line.equals("LOGOUT")) { // TODO:

					} else if (line.equals("QUIT")) {
						// delete login session file
						bufferedWriter.write("200 OK.");
						break;

					} else {
						bufferedWriter.write("404 invalid command.");
						System.out.println("insider else");
						// writes back to client any invalid messages
					}
				}

				// close input and output stream and socket
				bufferedReader.close();
				bufferedWriter.close();
				serviceSocket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
