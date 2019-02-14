
//Program:			Messenger.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class displays a GUI used for viewing and sending messages and file transfers.

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Messenger extends JFrame {
	public JTextArea textArea1;

	private JPanel buttonBar;

	private JPanel contentPanel;

	private JPanel dialogPane;

	private JMenu menu1;

	private JMenuBar menuBar1;

	private JMenuItem quitMenuItem;
	private JScrollPane scrollPane1;
	private JMenuItem sendFileMenuItem;
	private JButton sendMessage;

	private JTextField textField1;
	final User partner;

	public Messenger(User partner, boolean promptedFirst) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		initComponents();
		setupActionListeners();
		this.partner = partner;
		this.setTitle(MessageFormat.format("Messaging with - {0}", partner.getName()));
		this.getRootPane().setDefaultButton(sendMessage);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JIM.messageClient = new MessageClient(JIM.user, partner.getAddress(), JIM.CHAT_PORT, JIM.key);
		if (promptedFirst) {
			HelloRequest helloRequest = new HelloRequest(partner, JIM.key);
			helloRequest.setMessenger(this);
			helloRequest.process();
		}
		try {
			JIM.messageClient.sendHello();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disableMessaging() {
		textField1.setEditable(false);
		sendMessage.setEnabled(false);
	}

	private void closeMessenger() {
		try {
			JIM.messageClient.sendGoodbye();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (this.isShowing()) {
			dispose();
		}
	}

	private void initComponents() {

		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		sendFileMenuItem = new JMenuItem();
		quitMenuItem = new JMenuItem();
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		buttonBar = new JPanel();
		textField1 = new JTextField();
		sendMessage = new JButton();

		setTitle("Messaging with ");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		{

			{
				menu1.setText("File");

				sendFileMenuItem.setText("Send File...");
				menu1.add(sendFileMenuItem);

				quitMenuItem.setText("Quit");
				menu1.add(quitMenuItem);
			}
			menuBar1.add(menu1);
		}
		setJMenuBar(menuBar1);

		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			{

				{

					textArea1.setEditable(false);
					scrollPane1.setViewportView(textArea1);
				}

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
								.addContainerGap()));
				contentPanelLayout.setVerticalGroup(contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
								.addContainerGap()));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] { 0, 80 };
				((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] { 1.0, 0.0 };
				buttonBar.add(textField1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				sendMessage.setText("Send Message");
				sendMessage.setEnabled(false);
				buttonBar.add(sendMessage, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());

	}

	private void setupActionListeners() {
		sendMessage.addActionListener(e -> {
			try {
				String message = textField1.getText();
				JIM.messageClient.sendMessage(message);
				MessageUtils.displayMessage(textArea1,
						MessageUtils.formatMessage(JIM.user.getName(), textField1.getText()));
				textField1.setText("");
			} catch (IOException e1) {
				MessageUtils.displayMessage(textArea1, MessageUtils.formatMessage("SERVER",
						MessageFormat.format("Could not send message to {0}", partner.getName())));
				e1.printStackTrace();
			}
		});
		textField1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				checkValues();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkValues();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkValues();
			}

			private void checkValues() {
				if (textField1.getText().length() > 0)
					sendMessage.setEnabled(true);
				else
					sendMessage.setEnabled(false);
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeMessenger();
			}
		});
		quitMenuItem.addActionListener(e -> closeMessenger());
		sendFileMenuItem.addActionListener(e -> {
			JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));

			int returnValue = jFileChooser.showOpenDialog(this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jFileChooser.getSelectedFile();
				try {
					JIM.messageClient.sendFileTransferRequest(JIM.user, selectedFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
