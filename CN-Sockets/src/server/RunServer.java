package server;

import java.io.IOException;

public class RunServer {

	public static void main(String[] args) {
		try {
			ServerHTTP server = new ServerHTTP("UTF-8");
			server.listenForConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
