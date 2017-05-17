package message;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Message class used for most of the communication between Server and Client. A variety of Message-types
 * controls what the server does.
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

    /**
     * Constructor add all information and put date when sent.
     *
     * @param sender    Sender of message
     * @param recipient Recipient of message
     * @param image     image to send
     */
    public Message(int type, String sender, String recipient, Object image) {
        this.sender = sender;
        this.type = type;
        this.recipient = recipient;
        this.image = image;
        this.date = new Date();
    }

    /**
     * Constructs a message with a type, username and a String "data" that is used for different purposes
     * depending on the type parameter.
     *
     * @param type
     * @param username
     * @param data
     */
    public Message(int type, String username, String data) {
        this.username = username;
        this.data = data;
        this.type = type;
    }

    /**
     * Constructor.
     *
     * @param type
     * @param status
     * @param contactList
     */
    public Message(int type, boolean status, String[] contactList) {
        this.status = status;
        this.type = type;
        this.contactList = contactList;
    }

    /**
     * Constructor used to notify client whether a request is valid or not.
     *
     * @param type
     * @param status
     */
    public Message(int type, boolean status) {
        this.status = status;
        this.type = type;

    }

    /**
     * Constructor used for a variety of purposes.
     *
     * @param type
     * @param username
     */
    public Message(int type, String username) {
        this.type = type;
        this.username = username;
    }

    /**
     * Constructor used to return a contact list to the CLient.
     *
     * @param type
     * @param contactList
     */
    public Message(int type, String[] contactList) {
        this.type = type;
        this.contactList = contactList;
    }

    /**
     * returns image
     *
     * @return image in message
     */
    public Object getImage() {
        return image;
    }

    /**
     * returns sender
     *
     * @return sender of message
     */
    public String getSender() {
        return sender;
    }

    /**
     * returns recipient
     *
     * @return recipient of message
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * returns Date object
     *
     * @return Date object
     */
    public String getDate() {
        //ska nog göras om till att retunera tid istället och inte objektet.
        return new SimpleDateFormat("dd/MM - hh:mm").format(date);
    }

    /**
     * Returns the date object.
     *
     * @return date
     */
    public Date getDateObject() {
        return date;
    }

    /**
     * returns Type.
     *
     * @return type to return
     */
    public int getType() {
        return type;
    }

    /**
     * returns username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * returns password
     *
     * @return password
     */
    public String getData() {
        return data;
    }

    /**
     * returns status
     *
     * @return status
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * returns contactList
     *
     * @return
     */
    public String[] getContactList() {
        return contactList;
    }
}
