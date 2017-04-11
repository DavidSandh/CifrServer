package server.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 
 * @author Viktor
 *
 */
public class ServerGUI extends JPanel{
	private ButtonListener listener = new ButtonListener();
	private JButton btnStartStop = new JButton("Start server");
	private JTextArea logTextArea = new JTextArea();
	private Font font = new Font("Arial", Font.PLAIN,22);
	private ServerController serverController;
	
	public ServerGUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800,600));
		add(mainPanel(), BorderLayout.CENTER);
		add(buttonPanel(), BorderLayout.NORTH);
		showFrame();
	}
	
	private JPanel mainPanel(){
		JPanel panel = new JPanel();
		logTextArea.setPreferredSize(new Dimension(600,500));
		panel.setPreferredSize(new Dimension(800,600));
		panel.add(logTextArea);
		return panel;
	}
	
	private void showFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
	
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		btnStartStop.addActionListener(listener);
		btnStartStop.setPreferredSize(new Dimension(150,30));
		btnStartStop.setFont(font);
		panel.add(btnStartStop);
		return panel;
	}
	
	protected void addController(ServerController serverController) {
		this.serverController = serverController;
	}

	protected void append(String message) {
		LocalDateTime now = LocalDateTime.now();
		logTextArea.append(now.getHour() + ":" + now.getMinute() + ":"+ now.getSecond() + " " + message + "\n");
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
