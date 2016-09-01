package arc.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import arc.main.ArcServer;

public class ServerProtocol implements Runnable{
	private static ArcServer arcWorld;
	private static EntityEncoder encoder;
	//For networking
	private static String IP;
	private static int portNumber;
	private static ServerSocketChannel serverSocketChannel;
	private static Selector selector;
	
	
	public ServerProtocol(String IP, int portNumber, ArcServer arcWorld, EntityEncoder encoder) throws IOException{
		ServerProtocol.IP = IP;
		ServerProtocol.portNumber = portNumber;
		ServerProtocol.arcWorld = arcWorld;
		ServerProtocol.encoder = encoder;
		//Initialize server socket channel and a selector.
    	serverSocketChannel = ServerSocketChannel.open();
    	selector=Selector.open();
    	serverSocketChannel.bind(new InetSocketAddress(IP,portNumber));
    	serverSocketChannel.configureBlocking(false);
    	serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
    
    
    public void write(SocketChannel sc, String message, int buffer){
    	try {
			ByteBuffer buf = ByteBuffer.allocate(buffer);
			buf.clear();
			buf.put(message.getBytes());
			buf.flip();
			while(buf.hasRemaining()){
				sc.write(buf);
			}
		} catch (IOException e) {
			try {
				sc.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    	
    }
    
    public void closeConnection() throws IOException{
    	ServerProtocol.selector.close();
    	ServerProtocol.serverSocketChannel.close();
    }
    
    public void run(){
    	while (!Thread.currentThread().isInterrupted()){
    		
    		try {
    			int ready = selector.select();
    			if(ready == 0) continue;
			} 
    		catch (IOException e1) {
				e1.printStackTrace();
			}
    		
	    	Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
	    	while (keys.hasNext()){
	    		SelectionKey key = keys.next();
	    		keys.remove();
	    		if(!key.isValid()){
	    			continue;
	    		}
	    		if(key.isAcceptable()){	    			
	    			//We obtain a serversocketchannel for the key and get ready to write.
	    			try{
		    			SocketChannel sc = ((ServerSocketChannel)key.channel()).accept();
		    			System.out.println("Accepting connection from " + sc.getRemoteAddress());
		    			sc.configureBlocking(false);
		    			sc.register(selector, SelectionKey.OP_WRITE);
		    			write(sc, encoder.entityInitEncode(ArcServer.getEntitymanager().getAllEntities()), 512);
	    			}
	    			catch(IOException e){
	    				e.printStackTrace();
	    			}
	    		}
	    		//We only write to client when needed, currently on object collisions.
	    		if(key.isWritable() && ArcServer.isUpdateNeccessary()){
	    			//We obtain a socketchannel for the key and get ready to write.
	    			SocketChannel sc = ((SocketChannel)key.channel());
	    			write(sc, encoder.entityUpdateEncode(ArcServer.getEntitymanager().getAllEntities()), 252);
	    		}
	    	}
	    	ArcServer.setUpdateNeccessary(false);
	    
	    }
    	try{
    		closeConnection();
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}
    	
    }
}
