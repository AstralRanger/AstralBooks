import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TransactionGUI extends JDialog implements ActionListener
{
	public final String[] entryTypes = {"DR", "CR"};
	private ChartOfAccount chartOfAccounts;
	private String exit;
	private ArrayList <JComboBox<Object>> accountLists;
	private ArrayList <JComboBox<String>> dRCR;
	private ArrayList <JTextField> valueFields;
	private JButton addRow, save, cancel;
	private JPanel content, main, bottom;
	private JScrollPane scroll;
	private int rows;
	private Transaction trans;
	public final int DEBIT = 100;
	public final int CREDIT = 101;
	
	public TransactionGUI(ChartOfAccount chartOfAccounts, int num)
	{
		this.chartOfAccounts = chartOfAccounts;
		
		accountLists = new ArrayList <JComboBox<Object>>();
		dRCR = new ArrayList <JComboBox<String>>();
		valueFields = new ArrayList <JTextField>();
		
		addRow = new JButton("Add Row");
		addRow.addActionListener(this);
		addRow.setActionCommand("Add Row");
		
		save = new JButton("Save");
		save.addActionListener(this);
		save.setActionCommand("Save");
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("Cancel");
		
		
		content = new JPanel(new BorderLayout());
		
		main = new JPanel();
		main.setLayout(new BoxLayout (main, BoxLayout.Y_AXIS));
		
		rows = 2;
		
		for (int c = 0; c < rows; c++)
		{
			JPanel temp = new JPanel();
			accountLists.add(new JComboBox<Object> (chartOfAccounts.toArray()));
			valueFields.add(new JTextField(20));
			dRCR.add(new JComboBox<String> (entryTypes));
			temp.add(accountLists.get(c));
			temp.add(valueFields.get(c));
			temp.add(dRCR.get(c));
			main.add(temp);
		}
		
		bottom = new JPanel(new GridLayout(1, 7));
		bottom.add(new JLabel());
		bottom.add(addRow);
		bottom.add(new JLabel());
		bottom.add(save);
		bottom.add(new JLabel());
		bottom.add(cancel);
		bottom.add(new JLabel());
		
		scroll = new JScrollPane(main);
		content.add(scroll, BorderLayout.CENTER);
		content.add(bottom, BorderLayout.PAGE_END);	
		setContentPane(content);
		trans = null;
		
		setTitle ("Transaction #" + num);
		setSize (780, 200);
		setResizable (false);
		setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo (null);  
		exit = "Cancel";
		
		setModal (true);
	    setAlwaysOnTop (true);
		setModalityType (ModalityType.APPLICATION_MODAL);
		
		setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
		setLocationRelativeTo (null);  
	}
	
	public String getExit()
	{
		return exit;
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand() == "Add Row")
		{
			JPanel temp = new JPanel();
			accountLists.add(new JComboBox<Object> (chartOfAccounts.toArray()));
			valueFields.add(new JTextField(20));
			dRCR.add(new JComboBox<String> (entryTypes));
			temp.add(accountLists.get(rows));
			temp.add(valueFields.get(rows));
			temp.add(dRCR.get(rows));
			main.add(temp);
			rows++;
			main.revalidate();
		}
		else
		if (e.getActionCommand() == "Save")
		{
			if (isBalanced())
			{
				exit = "Save";
				trans = calcTrans();
				setVisible(false);
			}
			else
				JOptionPane.showMessageDialog(this, "Total debits must equal total credits.", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
		else
			setVisible(false);
			
	}
	
	public boolean isBalanced()
	{
		int totalDR = 0, totalCR = 0;
		try
		{
			for (int c = 0; c < rows; c++)
			{
				if (dRCR.get(c).getSelectedItem() == "DR")
				{
					totalDR += Integer.parseInt(valueFields.get(c).getText());
				}
				else
				{
					totalCR += Integer.parseInt(valueFields.get(c).getText());
				}
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return totalDR == totalCR;
	}
	
	private Transaction calcTrans()
	{
		Transaction trans = new Transaction();
		for (int c = 0; c < rows; c++)
		{
			Account tempAcc = (Account) accountLists.get(c).getSelectedItem();
			int tempBal = Integer.parseInt(valueFields.get(c).getText());
			String tempType = (String) dRCR.get(c).getSelectedItem();
			if (tempType == "DR")
				trans.add(new Entry(tempAcc, tempBal, DEBIT));
			else
				trans.add(new Entry(tempAcc, tempBal, CREDIT));
		}
		return trans;
	}
	
	public Transaction getTransaction()
	{
		return trans;
	}
}
