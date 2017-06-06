import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddAccountGUI extends JDialog implements ActionListener
{
	protected final String accTypes[] = {"Asset", "Liability", "Equity", "Revenue", "Expense"};
	protected JPanel namePanel, idPanel, typePanel, buttonPanel;
	protected JLabel nameLabel, idLabel, typeLabel;
	protected JButton save, cancel;
	protected JTextField nameField, idField;
	protected JComboBox<Object> typeField;
	protected String exit;
	public AddAccountGUI ()
	{
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout (content, BoxLayout.Y_AXIS));
		
		namePanel = new JPanel();
		idPanel = new JPanel();
		typePanel = new JPanel(); 
		buttonPanel = new JPanel();
		
		nameLabel = new JLabel("Account Name: ");
		idLabel = new JLabel("Account ID (Numbers Only): ");
		typeLabel = new JLabel("Account Type: ");
		
		nameField = new JTextField(20);
		idField = new JTextField(20);
		typeField = new JComboBox<Object>(accTypes);
		
		save = new JButton("Save");
		cancel = new JButton("Cancel");
		save.addActionListener(this);
		cancel.addActionListener(this);
		save.setActionCommand("Save");
		exit = "Cancel";
		
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		idPanel.add(idLabel);
		idPanel.add(idField);
		
		typePanel.add(typeLabel);
		typePanel.add(typeField);
		
		buttonPanel.add(save);
		buttonPanel.add(cancel);
		
		content.add(namePanel);
		content.add(idPanel);
		content.add(typePanel);
		content.add(buttonPanel);
		
	    setContentPane(content);
		setTitle ("Add An Account");
		setSize (500, 200);
		setResizable (false);
		
		setModal (true);
	    setAlwaysOnTop (true);
		setModalityType (ModalityType.APPLICATION_MODAL);
		
		setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
		setLocationRelativeTo (null);  
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equals("Save"))
		{
			exit = "Save";
		}
		setVisible(false);
	}	
	
	public String getExit()
	{
		return exit;
	}
	
	public String getName()
	{
		return nameField.getText();
	}
	
	public int getId()
	{
		return Integer.parseInt(idField.getText());
	}
	
	public int getAccType()
	{
		return typeField.getSelectedIndex();
	}
}
