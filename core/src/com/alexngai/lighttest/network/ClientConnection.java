package com.alexngai.lighttest.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.alexngai.lighttest.GameWorld;
import com.alexngai.lighttest.LightTest;
import com.alexngai.lighttest.gameobjects.GameCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;


public class ClientConnection {
	
	private GameWorld gameWorld;
	private GameCharacter gameCharacter;
	private String serverIP = "192.168.1.148";
	private String thisIP;
	private List<String> commands = new ArrayList<String>(); 
	private Socket socket;
	private int ID = -1;
	
	final String join = "J";
	final String leave = "L";
	final String initiate = "I"; //command code to join/initiate game
	final String position = "P"; //code to signify own position
	final String others = "O";
	
	Json json = new Json();

	public String outMessage = "";
	public AtomicReference<String> commandString = new AtomicReference<String>();
	
	public ClientConnection(GameWorld gameWorld){

		this.gameWorld = gameWorld;
		commandString.set("");
		List<String> addresses = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			for(NetworkInterface ni : Collections.list(interfaces)){
				for(InetAddress address : Collections.list(ni.getInetAddresses()))
				{
					if(address instanceof Inet4Address){
						addresses.add(address.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		// Print the contents of our array to a string.  Yeah, should have used StringBuilder
		String ipAddress = new String("");
		for(String str:addresses)
		{
			ipAddress = ipAddress + str + "\n";
			if (!str.contains("127")) thisIP = ipAddress;
		}	
		Gdx.app.log("IP", "IP: " + thisIP);

		connectToServer(9021);
	}


	public void update(float delta){
		formOutMsg();
		sendUpdate();
		updateCommands();
	}

	private void formOutMsg(){
		if (gameCharacter != null && ID > 0){
			Player player = new Player(ID, gameCharacter.getPosition(), gameCharacter.getVelocity(), gameCharacter.getAcceleration());
			String str = json.toJson(player, Player.class);
			str = position + "/" + str + ";";
			outMessage.concat(str);
		}
	}
	
	private void sendUpdate(){
		try {
			// write our entered message to the stream
			if (socket != null && !outMessage.equals("")){
				socket.getOutputStream().write(outMessage.getBytes());
				outMessage = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void connectToServer(final int socketNo){
		new Thread(new Runnable(){

			@Override
			public void run() {
				SocketHints socketHint = new SocketHints();
				// 0 means no timeout. 
				socketHint.connectTimeout = 4000;

				try {
					socket = Gdx.net.newClientSocket(Protocol.TCP, serverIP, socketNo, socketHint);
				} catch (GdxRuntimeException e) {
					Gdx.app.log("ClientSocket", "Could not make socket connection");
				}
			
				//prepare initiation message
				String str = initiate + "/" + thisIP + ";";
				outMessage.concat(str);
				
				//send initiation and join request message 
				try {
					// write our entered message to the stream
					if (socket != null && !outMessage.equals("")){
						socket.getOutputStream().write(outMessage.getBytes());
						outMessage = "";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// Loop forever
				while(true){

					BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
					
					try {  
						//pass max five incoming commands to prevent overflow lag
						while (buffer.ready()){
							String command = buffer.readLine();
							Gdx.app.log("IP msg", command);

							String prevVal = commandString.get();
							String newVal = commandString.get() + command + ";";
							boolean worked = commandString.compareAndSet(prevVal, newVal);
							//handleCommand(command);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						java.lang.Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start(); 
	}
	
	private void updateCommands(){
		String text = commandString.getAndSet("");
		//if (text != "") Gdx.app.log("thread listener", text);
		String[] commands = text.split(";");
		
		for (String s : commands){
			String[] vals = s.split("/");
			if (vals.length == 2) handleCommand(vals[0], vals[1]);
		}
	}
	
	private void handleCommand(String s1, String s2){
		switch (s1){
			//case initiate: //server sends player ID
			//	break;
			case join: //send request to join ? receive request
				Player player = json.fromJson(Player.class, s2);
				if (player.ID != ID) 
					//this approach won't generally work, need to figure out how to broadcast new member and give each member an ID
				break;
			case position: //send player JSON with positional information
				break;
			case leave: //send request to leave
				break;
		}
	}
	

}
