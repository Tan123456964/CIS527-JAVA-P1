/*
 * Server.java
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	public static void writeToClient(BufferedWriter br, String message) throws IOException{
		br.write(message);
		br.newLine();
		br.flush();
	}

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

		ServerSocket myServerice = null;
		Socket serviceSocket = null;

		String line;

		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		// list of user name and password
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("root", "root01");
		userInfo.put("john", "john01");
		userInfo.put("david", "david01");
		userInfo.put("mary", "mary01");

		// Try to open a server socket
		try {
			myServerice = new ServerSocket(SERVER_PORT);

		} catch (Exception e) {
			System.out.println(e);
		}

		// Create a socket object from the ServerSocket to listen and accept
		// connections.
		// Open input and output streams

		if (myServerice != null) {

			while (true) {
				try {
					serviceSocket = myServerice.accept();

					inputStreamReader = new InputStreamReader(serviceSocket.getInputStream());
					outputStreamWriter = new OutputStreamWriter(serviceSocket.getOutputStream());

					bufferedReader = new BufferedReader(inputStreamReader);
					bufferedWriter = new BufferedWriter(outputStreamWriter);

					// word of the day
					int wordNum = 0;
					ArrayList<String> word = readFromFile("wordoftheday.txt"); // [0,2,3]
					Map<String, String> session = new HashMap<String, String>();

					// As long as we receive data, echo that data back to the client.
					while (true) {

						line = bufferedReader.readLine();

						System.out.println("Client CMD"+line);

						if (line != null && line.equals("MSGGET")) {

							System.out.println("Inside MESSAGEGET.");
							// writes back to client
							String wordOfTheDay ="200 OK%n" + word.get(wordNum % word.size());
							writeToClient(bufferedWriter, wordOfTheDay);
							wordNum++;
						}
						
						else if (line != null && line.equals("MSGSTORE")) {

							if (session.size() == 1) {
								writeToClient(bufferedWriter, "200 OK");
							}
							else{
								writeToClient(bufferedWriter, "400 a user must login first.");
							}

						
						} else if (line != null && line.contains("LOGIN")) {
							System.out.println("Inside LOGIN.");

							String login[] = line.split(" "); 
							if(session.size() > 0){
								String msg = "404 user "+ session.keySet().toArray()[0] +" is already logged in.";
								writeToClient(bufferedWriter, msg);
							}
							else if (login.length < 3) {
								writeToClient(bufferedWriter, "404 incomplete command.");
							} else if (userInfo.containsKey(login[1]) && userInfo.get(login[1]).equals(login[2])) {
								session.put(login[1], login[2]);
								writeToClient(bufferedWriter, "200 OK");
							} else {
								writeToClient(bufferedWriter, "404 Username or password is incorrect.");
							}

						} else if (line != null && line.equals("SHUTDOWN")) {

							if(session.size() > 0 && session.containsKey("root")){

						     	writeToClient(bufferedWriter, "200 OK");
								myServerice.close();
								myServerice = null;
								break;
							}
							else{
								writeToClient(bufferedWriter, "404 User must be a root user.");
							}

						} else if (line != null && line.equals("LOGOUT")) {
							if(session.size() > 0){
								session.clear();
								writeToClient(bufferedWriter, "200 OK");
							}
							else{
								writeToClient(bufferedWriter, "404 there are no users.");
							}

						} else if (line != null && line.equals("QUIT")) {
							// delete login session file
							writeToClient(bufferedWriter, "200 OK");
							break;

						} else {
							if(line != null && line.contains("%:MSGSTORE:%")){
								String msg = line.replaceAll("%:MSGSTORE:%", "");
								word.add(msg);
								writeToClient(bufferedWriter, "-----***200***----");
							}
							else{
								writeToClient(bufferedWriter, "404 Invalid command.");
							}

						}
					}

                    // clear/close all connection
					inputStreamReader.close();
					outputStreamWriter.close();
					bufferedReader.close();
					bufferedWriter.close();
					serviceSocket.close();
					session.clear();
					wordNum = 0;
                    
					// exiting the program if server is closed 
					if(myServerice == null){
						break;
					}

					
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	    
       System.out.println("***END OF SERVER***");
	
	}
}
