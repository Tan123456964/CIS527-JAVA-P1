/*
 * Server.java
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	// write message to client
	public static void writeToClient(BufferedWriter br, String message) throws IOException {
		br.write(message);
		br.newLine();
		br.flush();
	}

	/**
	 * 
	 * @filename :"file path"
	 * @returns : read from file and store them in an arrayList
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
	 * 
	 * @filename :"file path"
	 * @returns <void> : writes (append) text to a file
	 *          used to save messages to a file (doesn't override content of the file)
	 */

	public static void writeToFile(String filename, String text) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename), true));
		writer.write(text);
		writer.newLine();
		writer.close();
	}

	public static final int SERVER_PORT = 6333;

	public static void main(String args[]) {

		ServerSocket myService = null;
		Socket serviceSocket = null;

		String line;

		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		// list of usernames and passwords
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("root", "root01");
		userInfo.put("john", "john01");
		userInfo.put("david", "david01");
		userInfo.put("mary", "mary01");

		// try to open a server socket
		try {
			myService = new ServerSocket(SERVER_PORT);

		} catch (Exception e) {
			System.out.println(e);
		}

		// creates a socket object from the ServerSocket to listen and accept
		// connections.
		// open input and output streams

		if (myService != null) {

			while (true) {
				try {
					serviceSocket = myService.accept();

					inputStreamReader = new InputStreamReader(serviceSocket.getInputStream());
					outputStreamWriter = new OutputStreamWriter(serviceSocket.getOutputStream());

					bufferedReader = new BufferedReader(inputStreamReader);
					bufferedWriter = new BufferedWriter(outputStreamWriter);

					// message store command
					String msgStoreCMD = ""; 

					// word of the day
					int wordNum = 0;
					ArrayList<String> word = readFromFile("word.txt");

					// save logged in user
					Map<String, String> session = new HashMap<String, String>();

					// as long as we receive data, echo that data back to the client.
					while (true) {

						line = bufferedReader.readLine();

						System.out.println("Client CMD: " + line);

						if (line != null && (line.equals("MSGSTORE") ||  msgStoreCMD.equals("MSGSTORE"))) {

							if (session.size() == 1) {
								if ( msgStoreCMD.equals("MSGSTORE")) {
									word.add(line);
									writeToFile("word.txt", line);
									 msgStoreCMD = "";
								} else {
									 msgStoreCMD = line;
								}
								writeToClient(bufferedWriter, "200 OK");
							} else {
								writeToClient(bufferedWriter, "401 You are not currently logged in, login first.");
							}
						}
						else if (line != null && line.equals("MSGGET")) {
							// write back to client
							writeToClient(bufferedWriter, "200 OK");
							writeToClient(bufferedWriter, word.get(wordNum % word.size()));
							wordNum++;
						} 
						else if (line != null && line.contains("LOGIN")) {

							String login[] = line.split(" ");
							if (session.size() > 0) {
								String msg = "409 user " + session.keySet().toArray()[0] + " is already logged in.";
								writeToClient(bufferedWriter, msg);
							} else if (login.length < 3) {
								writeToClient(bufferedWriter, "300 message format error.");
							} else if (userInfo.containsKey(login[1]) && userInfo.get(login[1]).equals(login[2])) {
								session.put(login[1], login[2]);
								writeToClient(bufferedWriter, "200 OK");
							} else {
								writeToClient(bufferedWriter, "410 Wrong UserID or Password.");
							}
						} 
						else if (line != null && line.equals("SHUTDOWN")) {

							if (session.size() > 0 && session.containsKey("root")) {

								writeToClient(bufferedWriter, "200 OK");
								myService.close();
								myService = null;
								break;
							} else {
								writeToClient(bufferedWriter, "402 User not allowed to execute this command.");
							}
						} 
						else if (line != null && line.equals("LOGOUT")) {
							if (session.size() > 0) {
								session.clear();
								writeToClient(bufferedWriter, "200 OK");
							} else {
								writeToClient(bufferedWriter, "409 there are no logged in users.");
							}
						} 
						else if (line != null && line.equals("QUIT")) {
							// delete login session file
							writeToClient(bufferedWriter, "200 OK");
							break;
						} 
						else {
							writeToClient(bufferedWriter, "300 message format error.");
						}
					}

					// clear or close all connections
					inputStreamReader.close();
					outputStreamWriter.close();
					bufferedReader.close();
					bufferedWriter.close();
					serviceSocket.close();
					session.clear();
					wordNum = 0;

					// exit the program if server is closed
					if (myService == null) {
						break;
					}
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}

		System.out.println("***Server Terminated Successfully***");

	}
}