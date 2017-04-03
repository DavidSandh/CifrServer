package serverMain;

import java.util.Date;

import javax.swing.ImageIcon;
/**
 * Message class which contains the message being sent from and to server/client.
 * @author Lucas Knutsäter
 *
 */
public class Message {
	private ImageIcon image;
	private String sender;
	private String recipient;
	private Date date;
	
	/**
	 *  Constructor add all information and put date when sent.
	 * @param sender Sender of message
	 * @param recipient Recipient of message
	 * @param image image to send
	 */
	public Message(String sender, String recipient, ImageIcon image) {
		this.sender = sender;
		this.recipient = recipient;
		this.image = image;
		this.date = new Date();
	}
	/**
	 * returns image
	 * @return image in message
	 */
	public ImageIcon getImage() {
		return image;
	}
	/**
	 * returns sender
	 * @return sender of message
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * returns recipient
	 * @return recipient of message
	 */
	public String getRecipient() {
		return recipient;
	}
	/**
	 * returns Date object
	 * @return Date object
	 */
	public Date getDate() {
		//ska nog göras om till att retunera tid istället och inte objektet.
		return date;
	}
}
