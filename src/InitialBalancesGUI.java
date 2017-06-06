import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class InitialBalancesGUI extends JDialog implements ActionListener
{
	public final int ASSET = 0;
	public final int LIABILITY = 1;
	public final int EQUITY = 2;
	public final int REVENUE = 3;
	public final int EXPENSES = 4;
	private ChartOfAccount chartOfAccounts, balanceSheetAccounts;
	private ArrayList <JLabel> accountLabels;
	private ArrayList <JTextField> balanceFields;
	private JPanel content, accountSetup, top, bottom;
	private JButton upload, export, done, cancel;
	private String exit;
	
	public InitialBalancesGUI (ChartOfAccount chartOfAccounts)
	{
		this.chartOfAccounts = chartOfAccounts;
		balanceSheetAccounts = filterBalanceSheetAccounts(chartOfAccounts);
		
		accountLabels = new ArrayList <JLabel>();
		balanceFields = new ArrayList <JTextField>();
		
		upload = new JButton("Upload");
		export = new JButton("Export");
		done = new JButton("Done");
		cancel = new JButton("Cancel");
		
		upload.addActionListener(this);
		export.addActionListener(this);
		done.addActionListener(this);
		cancel.addActionListener(this);
		
		upload.setActionCommand("Upload");
		export.setActionCommand("Export");
		done.setActionCommand("Done");
		cancel.setActionCommand("Cancel");
		
		content = new JPanel(new BorderLayout());
		top = new JPanel(new GridLayout (1, 6));
		bottom = new JPanel(new GridLayout (1, 6));
		accountSetup = new JPanel(new GridLayout(balanceSheetAccounts.size(), 2));
		
		top.add(new JLabel(""));
		top.add(upload);
		top.add(new JLabel(""));
		top.add(new JLabel(""));
		top.add(export);
		top.add(new JLabel(""));
		
		for (int c = 0; c < balanceSheetAccounts.size(); c++)
		{
			accountLabels.add(c, new JLabel(balanceSheetAccounts.get(c).getName()));
			balanceFields.add(c, new JTextField(20));
			balanceFields.get(c).setText("" + balanceSheetAccounts.get(c).getInitialBalance());
			
			accountSetup.add(accountLabels.get(c));
			accountSetup.add(balanceFields.get(c));
		}
		
		bottom.add(new JLabel(""));
		bottom.add(done);
		bottom.add(new JLabel(""));
		bottom.add(new JLabel(""));
		bottom.add(cancel);
		bottom.add(new JLabel(""));
		
		JScrollPane scrollPane = new JScrollPane(accountSetup);
		
		content.add(top, BorderLayout.PAGE_START);
		content.add(scrollPane, BorderLayout.CENTER);
		content.add(bottom, BorderLayout.PAGE_END);
		
		setContentPane(content);
		
		setTitle ("Initial Balances");
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
	
	private ChartOfAccount filterBalanceSheetAccounts (ChartOfAccount allAccounts)
	{
		ChartOfAccount temp = new ChartOfAccount ();
		for (int c = 0; c < allAccounts.size(); c++)
		{
			System.out.println(allAccounts.get(c).toString());
			if (allAccounts.get(c).getType() == ASSET ||
				allAccounts.get(c).getType() == LIABILITY ||
				allAccounts.get(c).getType() == EQUITY)
			{
				temp.add(allAccounts.get(c));
				System.out.println("x");
			}
		}
		return temp;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "Upload")
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showOpenDialog(InitialBalancesGUI.this);
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
					    ChartOfAccount tempChart = balanceSheetAccounts;
					    while ((line = br.readLine()) != null) 
					    {
					    	int id, initbal;
					    	int sep = line.indexOf('_');
					    	id = Integer.parseInt(line.substring(0, sep));
					    	initbal = Integer.parseInt(line.substring(sep + 1));
					    	if (!tempChart.setInitialbalance(id, initbal))
					    	{
					    		JOptionPane.showMessageDialog(this, "Your input is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
					    		change = false;
					    		break;
					    	}
					    }
					    if (change)
					    {
				    		for (int c = 0; c < balanceSheetAccounts.size(); c++)
				    		{
				    			balanceFields.get(c).setText("" + tempChart.get(c).getInitialBalance());
				    		}
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
		if (e.getActionCommand() == "Export")
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showSaveDialog(InitialBalancesGUI.this);
			if (res == JFileChooser.APPROVE_OPTION)
			{
		           try 
		           {
		        	   FileWriter fw = new FileWriter (browse.getSelectedFile() + ".txt");
		        	   for (int c = 0; c < balanceSheetAccounts.size(); c++)
		        		   fw.write(balanceSheetAccounts.get(c).getId() + "_" +
		        				    balanceFields.get(c).getText()  + System.getProperty("line.separator"));
		        	   fw.close();
		           }
		           catch (Exception e1)
		           {
		        	   System.out.println(e1.getMessage());
		           }
			}
		}
		else
		if (e.getActionCommand() == "Done") 
		{
			if (equationBalanced())
			{
				updateChartOfAccounts();
				exit = "Save";
				setVisible(false);
			}
			else
				JOptionPane.showMessageDialog(this, "Assets must equal Liabilities plus Equity.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		if (e.getActionCommand() == "Cancel")
		{
			setVisible(false);
		}
	}
	
	public boolean equationBalanced()
	{
		int left = 0, right = 0;
		for (int c = 0; c < balanceSheetAccounts.size(); c++)
		{
			if (balanceSheetAccounts.get(c).getType() == ASSET)
				left += Integer.parseInt(balanceFields.get(c).getText());
			else
				right += Integer.parseInt(balanceFields.get(c).getText());
		}
		return left == right;
	}
	
	public void updateChartOfAccounts()
	{
		for (int c = 0; c < balanceSheetAccounts.size(); c++)
		{
			int tempid = balanceSheetAccounts.get(c).getId();
			balanceSheetAccounts.get(c).setInitialBalance(Integer.parseInt(balanceFields.get(c).getText()));
			chartOfAccounts.getAccount(tempid).setInitialBalance(balanceSheetAccounts.get(c).getInitialBalance());
		}
	}
	
	public ChartOfAccount getChartOfAccounts()
	{
		return chartOfAccounts;
	}
	
	public String getExit()
	{
		return exit;
	}
}
