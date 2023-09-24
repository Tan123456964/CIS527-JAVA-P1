# YAMOTD
Yet another "message of the day" - yamotd is a java program for returning messages of the day already stored within the program.

### GROUP INFO
UMich CIS527 Group: 30

### CONTRIBUTORS:
Tapon Das and Patrick Imoh

### DESCRIPTION
These programs are written in java programming language and can be run on either a linux or unix environment. There is a client "Client.java" as well as server "Server.java" programs. The communication between the server and client happens through TCP using the port number 6333. The server receives requests through this socket, acts on those requests, and returns the results to the requester. The client also creates a socket in the internet domain, send requests to the SERVER_PORT of a computer specified on the command-line, and receive responses through this socket from a server. Only one connection between a client and the server is possible at this time.

It performs four functions;
1. It returns a message of the day on the client to any user that sends the server a MSGGET message.
1. It allows a user, whose identity has been verified by the server to send the server a MSGSTORE message to upload one or more messages of the day to the server. These messages are stored on the file "word.txt" and can be returned to other clients that later send the MSGGET messages to the server.
1. It allows the "root" user to send a SHUTDOWN message to the server which will cause the server to close any open sockets and then terminate.
1. It verifies the identity of a user using LOGIN.

### HOW TO INSTALL AND RUN THE PROJECT ON UMICH INTRANET / NETWORK
####  Connect to VPN:
```bash
Use the PaloAlto GlobalProtect VPN application and connect using the portal address: "umvpn.umd.umich.edu".
```
#### Connect to UMich Server:
```bash
$ ssh username@login.umd.umich.edu -p 22
# Username is UMich ID; e.g. username = 'john' where email is john@umich.edu
# Use your UMich password
# Authenticate with Duo application
```
#### Copy files to server:
```bash
$ scp -rv source -P 22 username@login.umd.umich.edu: destination-path
# Use your UMich password
# Authenticate with Duo application
```
#### Run code with makefile:
```bash
$ make Server.class      # Creates server class file 
$ make Client.class      # Creates client class file  
```
#### Execute java code:
```bash
$ java Server
$ java Client IP  #e.g., $ java Client 127.0.0.1
```
### HOW TO USE THE YAMOTD
The server begins execution by reading the "word.txt" file, which initially has five (5) messages of the day stored in it. Once executed, the server would wait for connection requests from the client.

The client operates by sending any of the commands; MSGGET, MSGSTORE, SHUTDOWN, LOGIN, LOGOUT and QUIT to the server. You should use the carriage-return after inputting any of these commands.

#### MSGGET
The MSGGET command is used to request a new message of the day from the server. When input, the server would return a "200 OK" if the correct command is entered, as well as one of the messages of the day stored in the "word.txt" file on the next line of the console. It's operation is shown below. 
```bash
Enter command: MSGGET
200 OK
We don't stop going to school when we graduate
```
#### MSGSTORE
The MSGSTORE command is used to send one message of the day by a logged in user to the server, which is stored in the "word.txt" file. If a user is not logged in, the operation is not possible and an error code with message "401 You are not currently logged in, login first." is displayed. A "200 OK" message is displayed when the correct command: "MSGSTORE" is parsed as well as when a new message is successfully sent to the server. The operation of this command is shown below.
```bash
Enter command: MSGSTORE
401 You are not currently logged in, login first.

Enter command:LOGIN username password
200 OK

Enter command: MSGSTORE
200 OK

Enter a new message: This is a new message of the day.
200 OK
```
#### SHUTDOWN
The SHUTDOWN command is used to close the connections and sockets, and can only be performed by the "root" user. If no user is logged in or the logged in user isn't "root", the command is rejected. See below for usage.
```bash
Enter command: SHUTDOWN # when logged in user is not "root"
402 User not allowed to execute this command. # result when logged in user is not "root"

Enter command: LOGOUT
200 OK

Enter command: LOGIN root root01
200 OK

Enter command: SHUTDOWN1
300 message format error.

Enter command: SHUTDOWN
200 OK
***End of client program.***
```
#### LOGIN
The LOGIN command is used to authenticate a user with the server. The server checks and only authenticates users whose username and password match its records. A "200 OK" messages signifies a successful login whereas "410 Wrong UserID or Password" means that either a wrong username or password has been sent. The usage of this command is demonstrated below.
```bash
Enter command: LOGIN wrong_username wrong_password
410 Wrong UserID or Password.

Enter command: LOGIN username password
200 OK
```
#### LOGOUT
The LOGOUT command is used to terminate the session of a logged in user.
```bash
Enter command: LOGIN username password
200 OK

Enter command: LOGOUT
200 OK

Enter command: LOGOUT
409 there are no logged in users.
```
#### QUIT
The QUIT command is used to terminate the session between the client and the server. If the command is correctly sent and successful, the server returns a "200 OK" message. The operation of the LOGOUT command is shown below.
```bash
Enter command: QUIT
200 OK
***End of client program.***
```
### LICENSE
This project is licensed under the GPL.