
/**
 * Client of this chat implementation.
 * Connects to a server via a Socket and
 * can send and receive messages from the server.
 * 
 * @author Ivar Lund
 * ivarnilslund@gmail.com
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main class. Needs a server to operate.
 * 
 * @author Ivar Lund
 *
 */
public class Client {

	private Socket socket;
	private PrintWriter output;

	private Scanner scan = new Scanner(System.in);
	private Listener listener;

	/**
	 * Class constructor. Takes two parameters definable by user at startup but may
	 * be left out to use predetermined values.
	 * 
	 * @param hostName the host address for socket.
	 * @param portNr   the port number for socket.
	 */
	public Client(String hostName, int portNr) {
		setupIO(hostName, portNr);
		sendMsg();
	}

	/**
	 * Initiates I/O, socket and listener thread.
	 * 
	 * @param hostName passed from class constructor
	 * @param portNr   passed from class constructor
	 */
	private void setupIO(String hostName, int portNr) {
		try {
			socket = new Socket(hostName, portNr);
			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Connection setup failed... \nterminated");
			System.exit(1);
		}
		listener = new Listener(socket);
		listener.start();
	}

	/**
	 * Worker method of client. Runs continuously to output messages. 'quit' command
	 * to terminate program.
	 */
	private void sendMsg() {
		if (socket.isConnected()) {
			System.out.println("Output ready.");
			while (true) {
				String msg = scan.nextLine();
				if (msg.equalsIgnoreCase("quit"))
					killIO();
				output.println(msg);
			}
		}
	}

	/**
	 * Closes socket & streams as well as listener thread and then terminates
	 * program.
	 */
	private void killIO() {
		try {
			listener.setAlive(false);
			socket.close();
			System.out.println("Thank you for using this chattclient \nSuccessful termination");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Could not close socket properly \nUnsuccessful termination");
			System.exit(1);
		}
	}

	/**
	 * Main method. takes hostName as 1st argument and portNr as second argument.
	 * 
	 * @param args holds arguments from user.
	 */
	public static void main(String[] args) {
		String hostName = args.length > 0 ? args[0] : "localhost";
		int portNr = args.length > 1 ? Integer.parseInt(args[1]) : 2000;

		new Client(hostName, portNr);
	}
}
