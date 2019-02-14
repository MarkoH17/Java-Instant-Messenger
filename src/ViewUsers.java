
//Program:			ViewUsers.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the GUI used to connect the other users on the network running JIM

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ViewUsers extends JFrame {
	private JPanel buttonBar;
	private JPanel contentPanel;
	private JPanel dialogPane;

	private HashSet<User> knownUsers = new HashSet<>();

	private JLabel label3;

	private JLabel label4;

	private JLabel label5;

	private JLabel label6;

	private JLabel label7;
	private JLabel label8;
	private JList list1;
	private final DefaultListModel listModel = new DefaultListModel();
	private Neighbors localNeighbors;
	private JButton okButton;
	private JPanel panel1;
	private JPanel panel2;
	private JScrollPane scrollPane1;
	private JSeparator separator1;
	private JTabbedPane tabbedPane1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JTextField textField5;

	public ViewUsers() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		initComponents();
		setupActionListeners();
		list1.setCellRenderer(new ViewUserListRenderer());
		getOnlineUsers();
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getRootPane().setDefaultButton(okButton);

		textField4.setText(JIM.user.getAddress());
		textField5.setText(String.valueOf(JIM.user.getPort()));
	}

	private void displayOnlineUsers(HashSet<User> onlineUsers) {
		onlineUsers.removeAll(knownUsers);
		for (User onlineUser : onlineUsers) {
			listModel.addElement(onlineUser);
			knownUsers.add(onlineUser);
		}
	}

	private void getOnlineUsers() {
		try {
			localNeighbors = new Neighbors(JIM.NEIGHBORS_ADDRESS, JIM.NEIGHBORS_PORT);
			java.util.Timer getOnlineUsersTimer = new Timer();
			TimerTask getOnlineUsers = new TimerTask() {
				@Override
				public void run() {
					if (localNeighbors.isClientRunning() && localNeighbors.isServerRunning()) {
						HashSet<User> users = localNeighbors.getNeighboringUsers();
						displayOnlineUsers(users);
					} else {
						list1.setCellRenderer(new DefaultListCellRenderer());
						listModel.addElement("Unable to participate in neighbor search");
						getOnlineUsersTimer.cancel();
						getOnlineUsersTimer.purge();
					}
				}
			};
			getOnlineUsersTimer.scheduleAtFixedRate(getOnlineUsers, 0, 250);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void initComponents() {

		dialogPane = new JPanel();
		contentPanel = new JPanel();
		tabbedPane1 = new JTabbedPane();
		panel1 = new JPanel();
		scrollPane1 = new JScrollPane();
		list1 = new JList(listModel);
		panel2 = new JPanel();
		label5 = new JLabel();
		label3 = new JLabel();
		textField2 = new JTextField();
		label4 = new JLabel();
		textField3 = new JTextField();
		separator1 = new JSeparator();
		label6 = new JLabel();
		label7 = new JLabel();
		textField4 = new JTextField();
		label8 = new JLabel();
		textField5 = new JTextField();
		buttonBar = new JPanel();
		okButton = new JButton();

		setTitle("JIM - View Online Users");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			{

				{

					{
						panel1.setLayout(new BorderLayout());

						{
							scrollPane1.setViewportView(list1);
						}
						panel1.add(scrollPane1, BorderLayout.CENTER);
					}
					tabbedPane1.addTab("Neighboring Users", panel1);

					{

						label5.setText("Connect with Peer");
						label5.setHorizontalAlignment(SwingConstants.CENTER);
						label5.setFont(label5.getFont().deriveFont(label5.getFont().getStyle() | Font.BOLD));

						label3.setText("Address:");

						label4.setText("Port:");

						textField3.setText("5704");

						label6.setText("My Information");
						label6.setHorizontalAlignment(SwingConstants.CENTER);
						label6.setFont(label6.getFont().deriveFont(label6.getFont().getStyle() | Font.BOLD));

						label7.setText("Address:");

						textField4.setEnabled(false);

						label8.setText("Port:");

						textField5.setEnabled(false);

						GroupLayout panel2Layout = new GroupLayout(panel2);
						panel2.setLayout(panel2Layout);
						panel2Layout.setHorizontalGroup(panel2Layout.createParallelGroup()
								.addGroup(panel2Layout.createSequentialGroup().addContainerGap()
										.addGroup(panel2Layout.createParallelGroup()
												.addComponent(label5, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(panel2Layout.createSequentialGroup().addComponent(label3)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(textField2)
														.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(label4)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(textField3, GroupLayout.PREFERRED_SIZE, 63,
																GroupLayout.PREFERRED_SIZE))
												.addGroup(GroupLayout.Alignment.TRAILING, panel2Layout
														.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
														.addGroup(panel2Layout.createParallelGroup()
																.addComponent(label6, GroupLayout.Alignment.TRAILING,
																		GroupLayout.PREFERRED_SIZE, 360,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(separator1,
																		GroupLayout.Alignment.TRAILING,
																		GroupLayout.PREFERRED_SIZE, 360,
																		GroupLayout.PREFERRED_SIZE)))
												.addGroup(panel2Layout.createSequentialGroup().addComponent(label7)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(textField4, GroupLayout.PREFERRED_SIZE, 203,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(label8)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(textField5, GroupLayout.PREFERRED_SIZE, 63,
																GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
						panel2Layout.setVerticalGroup(panel2Layout.createParallelGroup().addGroup(panel2Layout
								.createSequentialGroup().addContainerGap().addComponent(label5)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label3)
										.addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(label4).addComponent(textField2, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(12, 12, 12)
								.addComponent(separator1, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(label6)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label7)
										.addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(textField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(label8))
								.addContainerGap(43, Short.MAX_VALUE)));
					}
					tabbedPane1.addTab("Manual Connection", panel2);
				}

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout
						.setHorizontalGroup(contentPanelLayout.createParallelGroup().addComponent(tabbedPane1));
				contentPanelLayout.setVerticalGroup(contentPanelLayout.createParallelGroup().addComponent(tabbedPane1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] { 0, 80 };
				((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] { 1.0, 0.0 };

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
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				localNeighbors.stopAllNeighbors();
				JIM.viewUsersWindow = null;
				super.windowClosing(e);
			}
		});
		okButton.addActionListener(e -> {
			int selectedTab = tabbedPane1.getSelectedIndex();

			if (selectedTab == 0) {
				int selectedUser = list1.getSelectedIndex();
				User partner = (User) listModel.getElementAt(selectedUser);
				Thread sendMessagingRequest = new Thread(() -> {
					try {
						MessageClient.sendMessagingRequest(JIM.user, MessageUtils.generateHash(JIM.key),
								partner.getAddress(), partner.getPort());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(this, "Could not connect to " + partner.getName());
					}
				});
				sendMessagingRequest.start();
			} else if (selectedTab == 1) {
				String address = textField2.getText();
				int port = Integer.valueOf(textField3.getText());
				Thread sendMessagingRequest = new Thread(() -> {
					try {
						MessageClient.sendMessagingRequest(JIM.user, MessageUtils.generateHash(JIM.key), address, port);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(this, "Could not connect to peer!");
					}
				});
				sendMessagingRequest.start();
			}
		});
		list1.addListSelectionListener(e -> {
			if (list1.getSelectedIndex() != -1) {
				okButton.setEnabled(true);
				okButton.requestFocus();
			} else {
				okButton.setEnabled(false);
			}
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
				if (textField2.getText().length() > 0 && textField3.getText().length() > 0) {
					okButton.setEnabled(true);
				} else {
					okButton.setEnabled(false);
				}
			}
		};

		textField2.getDocument().addDocumentListener(okButtonEnabler);
		textField3.getDocument().addDocumentListener(okButtonEnabler);
	}

}
// Program: ViewUserListRenderer.java
// Author: Mark Hedrick
// Last modified: April 21, 2018
// Desc: This class represents the custom ListCellRenderer used to display
// neighboring users online.

class ViewUserListRenderer extends JLabel implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		User user = (User) value;
		if (user.equals(JIM.user))
			return this;
		setText(MessageFormat.format("{0} - {1}", user.getName(), user.getAddress()));
		setOpaque(isSelected);
		return this;
	}
}