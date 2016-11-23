package node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javafx.scene.control.TextArea;
import java.rmi.RemoteException;
import javafx.application.Application;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import interfaces.IInitNodes;
import interfaces.IWrapper;
import sun.misc.IOUtils;

public class Main extends Application{

	public static void main(String args[]) throws MalformedURLException, RemoteException, NotBoundException {
	
		/*String name;
		if(args.length > 0) {
			name = args[0]; 
		} else {
			name = "noName";
		}
		IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.16" + "/hash"); // objectname
			
		System.out.println("connected");
		//obj.createNode(name, ip)
		// in
		//obj.createNode(name);
		String ip = obj.getFileNode("lalala");
		System.out.println(ip);
		boolean test = ping(ip);
		if(test) {
			System.out.println("yowza");
		}*/
		launch(args);
	}

	public static boolean ping(String ip) {
		
		InetAddress inet;

	    try {
			inet = InetAddress.getByName(ip);
		    if(inet.isReachable(5000)) 
		    return true;
		    return false;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
	    
		
		/*
		try {
			String command = "ping  " + ip;
			Process process = Runtime.getRuntime().exec(command);
			Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			System.out.print(result);

		} catch (Exception e) {
		}

		System.out.println("done");
		// String ip = obj.getIp("Jeroen");

		// System.out.print(ip);

*/
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button btn = new Button();
		TextArea textArea = new TextArea();


		btn.setText("Start agent");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				MulticastClient mc = new MulticastClient();
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
//TESt = 30538 => solution = 192.168.56.1
//Jeroen = 23658 => solution = 192.168.1.1
//test = 17074 => solution = 192.168.56.1