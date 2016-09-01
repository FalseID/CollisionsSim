package arc.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.scene.paint.Color;
import arc.core.EllipseBody;
import arc.core.Entity;
import arc.main.ArcClient;
import arc.math2D.Vector;

public class ClientProtocol implements Runnable{
	private static ArcClient arcClient;
	private static EntityDecoder decoder;
	//For networking
	private static String IP;
	private static int portNumber;
	private static SocketChannel clientSocketChannel;
	private static Selector selector;
	//If read() has excess data then we save it to this buffer to add to the next message.
	//Otherwise it cuts off our message.
	private String readbuffer;
	
	public ClientProtocol(String IP, int portNumber, ArcClient arcClient, EntityDecoder decoder){
		ClientProtocol.arcClient = arcClient;
		ClientProtocol.decoder = decoder;
		ClientProtocol.IP = IP;
		ClientProtocol.portNumber = portNumber;
		readbuffer="";
		//Initalise server socket channel and a selector.
	    try {
	    	clientSocketChannel = SocketChannel.open(new InetSocketAddress(IP,portNumber));
	    	selector=Selector.open();
	    	clientSocketChannel.configureBlocking(false);
	    	clientSocketChannel.register(selector, SelectionKey.OP_READ);
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() throws IOException{
    	ClientProtocol.selector.close();
    	ClientProtocol.clientSocketChannel.close();
    }
    
    
    public void run(){
    	while (!Thread.currentThread().isInterrupted()){
    		try {
    			int ready = selector.select();
    			if(ready == 0) continue;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		
    		//Clients update with information from the server.
    	    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
    	    while (keys.hasNext()){
	    		SelectionKey key = keys.next();
	    		keys.remove();
	    		if(!key.isValid()){
	    			continue;
	    		}
	    		if (key.isReadable()){
	                decode(read(key, 512));
	            }
	    	}
    	}
    	
    	try {
			closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void decode(String code){
    	//Problem with connection, clear simulation.
    	if(code.equals("")){
    		ArcClient.clearAll();
    		return;
    	}
    	//Check EntityEncoder for information on how data is encoded.
    	//Init packet if 0, update packet if 1.
    	if (code.charAt(1) == '0'){
	    	ArcClient.InitEntities(decoder.entityInitDecode(code));
    	}
    	else if (code.charAt(1) == '1'){
    		ArcClient.updateEntities(decoder.entityUpdateDecode(code));
    	}
        
    }

    private String read(SelectionKey key, int buffer){
   	SocketChannel sc = (SocketChannel) key.channel();
        try {
            if (sc.isConnectionPending()){
                sc.finishConnect();
            }
            sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
				
        }
	     catch (IOException e) {
				e.printStackTrace();
	     }
        
        ByteBuffer readBuffer = ByteBuffer.allocate(buffer);
        readBuffer.clear();
        int length = -1;
        try{
        	length = sc.read(readBuffer);
        } catch (IOException e){
            System.out.println("Reading problem, closing connection");
            key.cancel();
            try {
				sc.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            return "";
        }
        if (length == -1){
            System.out.println("Nothing was read from server");
            try {
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            key.cancel();
            return "";
        }
        readBuffer.flip();
        byte[] buff = new byte[buffer];
        readBuffer.get(buff, 0, length);
        
        String readData = new String(buff);
        try{
        	String usefulData = readData.substring(readData.indexOf('!'), readData.indexOf('#'));
        	System.out.println(usefulData);
        	return usefulData;
        }catch(IndexOutOfBoundsException e){
        	return " ";
        }
        
   }

}
