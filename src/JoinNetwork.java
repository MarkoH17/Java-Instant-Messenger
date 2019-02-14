
//Program:			JoinNetwork.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class displays the GUI that allows users to join the network.

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JoinNetwork extends JFrame {

	private JPanel buttonBar;

	private JPanel contentPanel;

	private JPanel dialogPane;

	private JLabel label1;

	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JButton okButton;
	private JSeparator separator1;
	private JTextField textField1;
	private JTextField textField2;

	public JoinNetwork() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		initComponents();
		setupActionListeners();
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getRootPane().setDefaultButton(okButton);
	}

	private String generateEncryptionKey(String input) {
		StringBuilder sb = new StringBuilder();
		for (byte b : MessageUtils.generateHash(input)) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString().substring(0, 16);
	}

	private void initComponents() {

		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		separator1 = new JSeparator();
		label2 = new JLabel();
		textField1 = new JTextField();
		label4 = new JLabel();
		textField2 = new JTextField();
		buttonBar = new JPanel();
		label3 = new JLabel();
		okButton = new JButton();

		setTitle("JIM - Join Network");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			{

				label1.setText("To Join the JIM LAN network, enter your name below.");
				label1.setHorizontalAlignment(SwingConstants.CENTER);

				label2.setText("Name:");

				label4.setText("Key:");

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(contentPanelLayout.createParallelGroup().addGroup(
						contentPanelLayout.createSequentialGroup().addContainerGap().addGroup(contentPanelLayout
								.createParallelGroup()
								.addComponent(label1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
								.addComponent(separator1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
								.addGroup(contentPanelLayout.createSequentialGroup().addComponent(label2)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(textField1, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
								.addGroup(contentPanelLayout.createSequentialGroup().addComponent(label4)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(textField2, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)))
								.addContainerGap()));
				contentPanelLayout.setVerticalGroup(contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup().addContainerGap().addComponent(label1)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label2).addComponent(textField1, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label4).addComponent(textField2, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(3, Short.MAX_VALUE)));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] { 0, 80 };
				((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] { 1.0, 0.0 };
				buttonBar.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				okButton.setText("OK");
				okButton.setEnabled(false);
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());

	}

	private void setupActionListeners() {
		okButton.addActionListener(e -> {
			try {
				JIM.user = new User(textField1.getText(), InetAddress.getLocalHost().getHostAddress(), JIM.CHAT_PORT);
				JIM.key = generateEncryptionKey(textField2.getText());
				JIM.messageServer = new MessageServer(JIM.CHAT_PORT);
				Thread serverThread = new Thread(JIM.messageServer);
				serverThread.start();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			ViewUsers viewUsersWindow = new ViewUsers();
			JIM.viewUsersWindow = viewUsersWindow;
			dispose();
		});

		DocumentListener okButtonEnabler = new DocumentListener() {
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
				if (textField1.getText().length() > 0 && textField2.getText().length() > 0) {
					okButton.setEnabled(true);
				}
			}
		};

		textField1.getDocument().addDocumentListener(okButtonEnabler);
		textField2.getDocument().addDocumentListener(okButtonEnabler);

	}

}
