package ws ;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation {
	private static final String PREFIX = "User";
    private static final AtomicInteger sequence = new AtomicInteger(0);
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();
    private final String username;
    private Session session;

    public WebSocketAnnotation() {
        username = "User" + sequence.getAndIncrement();
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        users.add(this);
        String message = "*" + username + "* connected.";
        sendMessage(message);
    }

    @OnClose
    public void end() {
    	users.remove(this);
    	String message = "*" + username + "* disconnected .";
    	sendMessage(message);
    }

    @OnMessage
    public void receiveMessage(String message) {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
    	String upperCaseMessage = message.toUpperCase();
    	sendMessage("[" + username + "] " + upperCaseMessage);
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	for( WebSocketAnnotation client : users ) {
	    	try {
	    		synchronized (client) {
	    			client.session.getBasicRemote().sendText(text);
				}
			} catch (IOException e) {
				users.remove(client);
				try {
					client.session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				String message = String . format ("* %s %s",client.username," has been disconnected.");
				sendMessage(message);
			}
    	}
    }
}
