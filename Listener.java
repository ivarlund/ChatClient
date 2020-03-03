import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Listener class of ChatClient.
 * Extends thread and listens for incoming
 * data until shut down by main class.
 * 
 * @author Ivar Lund
 * ivarnilslund@gmail.com
 * 
 */
public class Listener extends Thread {

	private InputStreamReader input;
	private BufferedReader reader;
	private final Socket clientSocket;
	private boolean alive = true;

	/**
	 * Class constructor. Takes one parameter provided at initialization.
	 * 
	 * @param socket the socket to be listened too.
	 */
	public Listener(Socket socket) {
		this.clientSocket = socket;
		setupListener();
	}
	
	/**
	 * Worker method of this class. listens to input stream for data and displays
	 * data to STDOUT. Lifespan determined by while loop.
	 */
	public void run() {
		System.out.println("Input ready!");
		String msg = null;
		while (alive) {
			try {
				msg = reader.readLine();
			} catch (IOException e) {
				System.out.println("Server connection lost");
				System.exit(1);
				e.printStackTrace(System.out);
			}
			System.out.println(msg);
		}
	}

	/**
	 * Setter for variable determining the lifespan of the method run()
	 * 
	 * @param alive pass false to kill thread.
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Initiates input stream and buffer.
	 */
	private void setupListener() {
		try {
			input = new InputStreamReader(clientSocket.getInputStream());
			reader = new BufferedReader(input);
		} catch (IOException e) {
			System.out.println("Error: could not set up input.");
			e.printStackTrace();
		}
	}

}
