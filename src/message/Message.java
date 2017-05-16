package message;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Message class which contains the message being sent from and to server/client.
 * @author Lucas Knutsäter, David Sandh
 *
 */
public class Message implements Serializable {
    public static final int LOGIN = 0, REGISTER = 1, MESSAGE = 2, STATUS = 3, SEARCH = 4, CONTACTLIST_ADD = 5, CONTACTLIST_REMOVE = 6, CONTACTLIST = 7;
    private Object image;
    private String sender;
    private String recipient;
    private String username;
    private String data;
    private boolean status;
    private Date date;
    private int type;
    private String[] contactList;
    private String key;

    /**
     *  Constructor add all information and put date when sent.
     * @param sender Sender of message
     * @param recipient Recipient of message
     * @param image image to send
     */
    public Message(int type, String sender, String recipient, Object image) {
        this.sender = sender;
        this.type = type;
        this.recipient = recipient;
        this.image= image;
        this.date = new Date();
    }

    public Message(int type, String username, String data) {
        this.username = username;
        this.data = data;
        this.type = type;
    }

    public Message(int type, String username, String key, String data) {
        this.username = username;
        this.data = data;
        this.type = type;
        this.key = key;
    }

    public Message(int type, boolean status, String[] contactList) {
        this.status = status;
        this.type = type;
        this.contactList=contactList;
    }

    public Message(int type, boolean status) {
        this.status = status;
        this.type = type;

    }

    public Message(int type, String username) {
        this.type = type;
        this.username = username;
    }
    public Message(int type, String[] contactList) {
        this.type = type;
        this.contactList = contactList;
    }

    /**
     * returns image
     * @return image in message
     */
    public Object getImage() {
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
    public String getDate() {
        return new SimpleDateFormat("dd/MM - hh:mm").format(date);
    }

    /**
     * returns Type.
     * @return type to return
     */
    public int getType() {
        return type;
    }

    /**
     * returns username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * returns password
     * @return password
     */
    public String getData() {
        return data;
    }

    /**
     * returns status
     * @return status
     */
    public boolean getStatus() {
        return status;
    }
    public String[] getContactList() {
        return contactList;
    }

    public String getKey() {
        return key;
    }
}
