/* 
 * Client.java
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	public static void writeToServer(BufferedWriter br, String message) throws IOException{
		br.write(message);
		br.newLine();
		br.flush();
	}

	public static final int SERVER_PORT = 5432;

	public static void main(String[] args) {
		Socket clientSocket = null;

		String userInput = null;
		String serverInput = null;

		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;

		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		// Check the number of command line parameters
		if (args.length < 1) {
		System.out.println("Usage: client <Server IP Address>");
		System.exit(1);
		}

		// Try to open a socket on SERVER_PORT
		// Try to open input and output streams
		try {
			clientSocket = new Socket(args[0], SERVER_PORT);

			inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
			outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());

			bufferedReader = new BufferedReader(inputStreamReader);
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			Scanner scanner = new Scanner(System.in);

			// If everything has been initialized then we want to write some data
			// to the socket we have opened a connection to on port 25

			if (clientSocket != null) {

				while (true) {
					// Get user input
					System.out.print("Enter command:");
					userInput = scanner.nextLine();

					// send a request to server
					writeToServer(bufferedWriter,userInput);

					serverInput = bufferedReader.readLine();
					System.out.println(serverInput.replace("%n","\n"));

				
					if (serverInput != null && (serverInput.equals("200 OK") || serverInput.contains("200 OK"))) {

						if (userInput != null && (userInput.equals("QUIT") || userInput.equals("SHUTDOWN"))) {
							scanner.close();
							break;
						}
						else if(userInput.equals("MSGSTORE")){
						
							
							System.out.print("Enter a new message:");
							String msg = scanner.nextLine();

							writeToServer(bufferedWriter,"%:MSGSTORE:%"+msg);
							
						}
						else{
							// do nothing 
						}

					}

				}
			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: hostname");
		} finally {
			try {
				if (clientSocket != null)
					clientSocket.close();
				if (inputStreamReader != null)
					inputStreamReader.close();
				if (outputStreamWriter != null)
					outputStreamWriter.close();
				if (bufferedReader != null)
					bufferedReader.close();
				if (bufferedWriter != null)
					bufferedWriter.close();

			} catch (Exception error) {
				error.printStackTrace();
			}
		}

		System.err.println("***End of client program.***");

	}

}
