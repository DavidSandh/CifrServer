package serverMain;

import java.util.Date;

import javax.swing.ImageIcon;

public class Message {
	private ImageIcon image;
	private String sender;
	private String recipient;
	private Date date;
	
	public Message() {
		
	}
	
	public Message(String sender, String recipient, ImageIcon image) {
		this.sender = sender;
		this.recipient = recipient;
		this.image = image;
		this.date = new Date();
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getRecipient() {
		return recipient;
	}
	public Date getDate() {
		return date;
	}
}
