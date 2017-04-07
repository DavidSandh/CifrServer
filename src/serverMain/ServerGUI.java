package serverMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
/**
 * 
 * @author Viktor
 *
 */
public class ServerGUI extends JPanel{
	private ButtonListener listener = new ButtonListener();
	private JButton btnStartStop = new JButton("Start server");
	private JTextArea txtLog = new JTextArea();
	private Font font1 = new Font("Arial", Font.PLAIN,22);
	private ServerController serverController;
	
	public ServerGUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800,600));
		add(mainPanel(), BorderLayout.CENTER);
		add(buttonPanel(), BorderLayout.NORTH);
		showFrame();
	}
	protected void addController(ServerController serverController) {
		this.serverController = serverController;
	}
	protected JPanel buttonPanel() {
		JPanel panel = new JPanel();
		btnStartStop.addActionListener(listener);
		btnStartStop.setPreferredSize(new Dimension(150,30));
		btnStartStop.setFont(font1);
		panel.add(btnStartStop);
		return panel;
	}
	protected void append(String message) {
		LocalDateTime now = LocalDateTime.now();
		txtLog.append(now.getHour() + ":" + now.getMinute() + ":"+ now.getSecond() + " " + message + "\n");
	}
	
	protected JPanel mainPanel(){
		JPanel panel = new JPanel();
		txtLog.setPreferredSize(new Dimension(600,500));
		panel.setPreferredSize(new Dimension(800,600));
		panel.add(txtLog);
		return panel;
	}
	
	protected void showFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
	
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
