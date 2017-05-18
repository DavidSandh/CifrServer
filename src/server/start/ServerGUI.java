package server.start;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import server.main.ServerController;

/**
 * A simple GUI to monitor the server (connections, registrations etc).
 * @author Viktor Ekström
 *
 */
public class ServerGUI extends JPanel{
	private ButtonListener listener = new ButtonListener();
	private JButton btnStartStop = new JButton("Start server");
	private JTextArea logTextArea = new JTextArea();
	private JScrollPane scrollPaneLog = new JScrollPane(logTextArea);
	private Font font = new Font("Arial", Font.PLAIN,22);
	private ServerController serverController;
/**
 * Constructor for the ServerGUI class.
 */
	public ServerGUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800,600));
		add(mainPanel(), BorderLayout.CENTER);
		add(buttonPanel(), BorderLayout.NORTH);
		showFrame();
	}
	/**
	 * Creates a JPanel-object and returns it.
	 * @return JPanel
	 */
	private JPanel mainPanel(){
		JPanel panel = new JPanel();
		logTextArea.setEditable(false);
		scrollPaneLog.setPreferredSize(new Dimension(600,500));
		scrollPaneLog.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.setPreferredSize(new Dimension(800,600));
		panel.add(scrollPaneLog);
		return panel;
	}
	/**
	 * Shows the frame
	 */
	private void showFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
	/**
	 * Creates a panel with a Start/Stop-button.
	 * @return JPanel
	 */
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		btnStartStop.addActionListener(listener);
		btnStartStop.setPreferredSize(new Dimension(150,30));
		btnStartStop.setFont(font);
		panel.add(btnStartStop);
		return panel;
	}
	/**
	 * Recieves a ServerController object.
	 * @param serverController
	 */
	public void addController(ServerController serverController) {
		this.serverController = serverController;
	}
	/**
	 * Shows messages (from the LogHandler for example) in the textArea.
	 * @param message
	 */
	 public void append(String message) {
		LocalDateTime now = LocalDateTime.now();
		logTextArea.append(now.getHour() + ":" + now.getMinute() + ":"+ now.getSecond() + " " + message + "\n");
	}
	/**
	 * Inner listener-class for the Start/Stop button
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnStartStop) {
				if(btnStartStop.getText().equals("Start server")) {
					serverController.startServer();
					btnStartStop.setText("Stop server");
				} else if (btnStartStop.getText().equals("Stop server")) {
					serverController.stopServer();
					btnStartStop.setText("Start server");
				}
			}
		}
	}
}
