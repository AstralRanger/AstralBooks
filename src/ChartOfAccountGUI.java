import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChartOfAccountGUI extends JFrame implements ActionListener
{
	JPanel content, top, bottom;
	JScrollPane scrollPane;
	JButton uploadButton, exportButton, addButton, removeButton, editButton;
	@SuppressWarnings("rawtypes")
	JList chartDisplay;
	ChartOfAccount chartOfAccounts;
	
	@SuppressWarnings("rawtypes")
	public ChartOfAccountGUI (ChartOfAccount chartOfAccounts)
	{	
		this.chartOfAccounts = chartOfAccounts;
		
		content = new JPanel(new BorderLayout());
		top = new JPanel(new GridLayout(1, 2));
		bottom = new JPanel(new GridLayout(1, 3));
		
		uploadButton = new JButton("Upload");
		exportButton = new JButton("Export");
		addButton = new JButton("Add");
		removeButton = new JButton("Remove");
		editButton = new JButton("Edit");
		
		uploadButton.addActionListener(this);
		exportButton.addActionListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		editButton.addActionListener(this);
		
		uploadButton.setActionCommand("upload");
		exportButton.setActionCommand("export");
		addButton.setActionCommand("add");
		removeButton.setActionCommand("remove");
		editButton.setActionCommand("edit");
		
		chartDisplay = new JList ();
		top.add(uploadButton);
		top.add(exportButton);
		
		bottom.add(addButton);
		bottom.add(removeButton);
		bottom.add(editButton);
		
		JScrollPane scrollPane = new JScrollPane(chartDisplay);
		
		content.add(top, BorderLayout.PAGE_START);
		content.add(bottom, BorderLayout.PAGE_END);
		content.add(scrollPane, BorderLayout.CENTER);
		
		this.setContentPane(content);
		setTitle ("Chart of Accounts");
		setSize (780, 400);
		setResizable (false);
		setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo (null);  
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand() == "upload")
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showOpenDialog(ChartOfAccountGUI.this);
			if (res == JFileChooser.APPROVE_OPTION)
			{
				File info = browse.getSelectedFile();
				String path = info.getPath();
				String extension = path.substring(path.lastIndexOf('.') + 1);
				if (extension.equalsIgnoreCase("txt"))
				{
					try (BufferedReader br = new BufferedReader(new FileReader(info))) 
					{
					    String line;
					    boolean change = true;
					    ChartOfAccount tempChart = new ChartOfAccount();
					    while ((line = br.readLine()) != null) 
					    {
					    	String name;
					    	int id, type;
					    	int sep = line.indexOf('_');
					    	name = line.substring(0, sep);
					    	line = line.substring(sep + 1);
					    	
					    	sep = line.indexOf('_');
					    	id = Integer.parseInt(line.substring(0, sep));
					    	type = Integer.parseInt(line.substring(sep + 1));
					    	
					    	Account temp = new Account (id, name, type);
					    	if (tempChart.idExist(id))
					    	{
					    		change = false;
					    		JOptionPane.showMessageDialog(this, "Account IDs must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
					    		break;
					    	}
					    	tempChart.add(temp);    	
					    }
					    if (change)
					    {
					    	chartOfAccounts = tempChart;
					    	refreshChartOfAccount();
					    }
					} 
					catch (Exception e1) 
					{
						JOptionPane.showMessageDialog(this, "Your input is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
					} 
				}
			}			
		}
		else
		if (e.getActionCommand() == "export")
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showSaveDialog(ChartOfAccountGUI.this);
			if (res == JFileChooser.APPROVE_OPTION)
			{
		           try 
		           {
		        	   FileWriter fw = new FileWriter (browse.getSelectedFile() + ".txt");
		        	   for (int c = 0; c < chartOfAccounts.size(); c++)
		        		   fw.write(chartOfAccounts.get(c).getName() + "_" +
		        				    chartOfAccounts.get(c).getId() + "_" +
		        				    chartOfAccounts.get(c).getType() + System.getProperty("line.separator"));
		        	   fw.close();
		           }
		           catch (Exception e1)
		           {
		        	   System.out.println(e1.getMessage());
		           }
			}
		}
		else
		if (e.getActionCommand() == "add")
		{
			try
			{
				AddAccountGUI addAccountGUI = new AddAccountGUI();
				addAccountGUI.setVisible(true);
				if (addAccountGUI.getExit().equals("Save"))
				{
					if ((!(chartOfAccounts.idExist(addAccountGUI.getId()))))
					{
						chartOfAccounts.add(new Account(addAccountGUI.getId(),
														addAccountGUI.getName(),
														addAccountGUI.getAccType()));
						refreshChartOfAccount();
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Account IDs must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(this, "Invalid ID.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		if (e.getActionCommand() == "remove")
		{
			int selected = chartDisplay.getSelectedIndex();
			if (selected != -1)
			{
				int n = JOptionPane.showConfirmDialog(this, "Would you really like to remove: " + 
															chartOfAccounts.get(selected).toString(),
															"Remove Account", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
				{
					chartOfAccounts.remove(selected);		
					refreshChartOfAccount();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Invalid selection.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		if (e.getActionCommand() == "edit")
		{
			int selected = chartDisplay.getSelectedIndex();
			if (selected != -1)
			{
				try
				{
					EditAccountGUI editAccountGUI = new EditAccountGUI(chartOfAccounts.get(selected).getId(),
																	   chartOfAccounts.get(selected).getName(),
																	   chartOfAccounts.get(selected).getType());
					editAccountGUI.setVisible(true);
					if (editAccountGUI.getExit().equals("Save"))
					{
						if (!(chartOfAccounts.idExist(editAccountGUI.getId())))
						{
							chartOfAccounts.get(selected).setId(editAccountGUI.getId());
							chartOfAccounts.get(selected).setName(editAccountGUI.getName());
							chartOfAccounts.get(selected).setType(editAccountGUI.getAccType());
							refreshChartOfAccount();
						}
						else
						{
							JOptionPane.showMessageDialog(this, "Account IDs must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(this, "Invalid selection.", "Error", JOptionPane.ERROR_MESSAGE);
				}	
			}
		}
	}


	@SuppressWarnings("unchecked")
	public void refreshChartOfAccount ()
	{
		chartOfAccounts.sort(Account.AccountIdComparator);
		for (int c = 0; c < chartOfAccounts.size(); c++)
		{
			chartDisplay.setListData(chartOfAccounts.toArray());
		}
	}
	
	public ChartOfAccount getChartOfAccounts ()
	{
		return chartOfAccounts;
	}

}
