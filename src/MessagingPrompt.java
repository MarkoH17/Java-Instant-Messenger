
//Program:			MessagingPrompt.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class shows a GUI displaying a request to message with a partner.

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class MessagingPrompt extends JFrame {
	private JPanel buttonBar;

	private JPanel contentPanel;

	private JPanel dialogPane;

	private JLabel label1;

	private JButton noButton;
	private final User requestingUser;
	private JButton yesButton;

	public MessagingPrompt(User requestingUser) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		initComponents();
		setupActionListeners();
		this.requestingUser = requestingUser;
		this.setTitle(MessageFormat.format("Chat with {0}?", requestingUser.getName()));
		label1.setText(MessageFormat.format("Would you like to message with {0}", requestingUser.getName()));
		this.getRootPane().setDefaultButton(yesButton);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void initComponents() {

		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		buttonBar = new JPanel();
		yesButton = new JButton();
		noButton = new JButton();

		setTitle("Prompt");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			{

				label1.setText("Would you like to message with");
				label1.setHorizontalAlignment(SwingConstants.CENTER);

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup().addContainerGap()
								.addComponent(label1, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
								.addContainerGap()));
				contentPanelLayout.setVerticalGroup(
						contentPanelLayout.createParallelGroup().addGroup(contentPanelLayout.createSequentialGroup()
								.addContainerGap().addComponent(label1).addContainerGap(25, Short.MAX_VALUE)));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] { 0, 85, 80 };
				((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] { 1.0, 0.0, 0.0 };

				yesButton.setText("Yes");
				buttonBar.add(yesButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				noButton.setText("No");
				buttonBar.add(noButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());

	}

	private void setupActionListeners() {
		yesButton.addActionListener(e -> {
			try {
				MessageClient.sendMessagingRequestApprove(JIM.user, requestingUser.getAddress(),
						requestingUser.getPort());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			MessagingApproveRequest messagingApproveRequest = new MessagingApproveRequest(requestingUser);
			messagingApproveRequest.setPrompted(true);
			messagingApproveRequest.process();
			dispose();
		});

		noButton.addActionListener(e -> {
			try {
				MessageClient.sendMessagingRequestDecline(JIM.user, requestingUser.getAddress(),
						requestingUser.getPort());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			dispose();
		});
	}

}
