import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Display extends JFrame implements ActionListener, WindowListener
{
	public final int ASSET = 0;
	public final int LIABILITY = 1;
	public final int EQUITY = 2;
	public final int REVENUE = 3;
	public final int EXPENSES = 4;
	public final int DEBIT = 100;
	public final int CREDIT = 101;
	
	JMenuBar menuBar;
	JMenu editMenu;
	JMenuItem chartOfAccountsItem, initialBalancesItem, newTransactionItem;
	ChartOfAccount chartOfAccounts;
	ChartOfAccountGUI chartOfAccountWindow;
	InitialBalancesGUI initialBalancesWindow;
	Journal masterJournal;
	public Display ()
	{
		menuBar = new JMenuBar();
		editMenu = new JMenu("Edit");
		
	    chartOfAccountsItem = new JMenuItem("Chart of Accounts");
		chartOfAccountsItem.addActionListener(this);
		chartOfAccountsItem.setActionCommand("Open Chart of Accounts");
		
		initialBalancesItem = new JMenuItem("Initial Balances");
		initialBalancesItem.addActionListener(this);
		initialBalancesItem.setActionCommand("Set Initial Balances");
		
		newTransactionItem = new JMenuItem("New Transaction");
		newTransactionItem.addActionListener(this);
		newTransactionItem.setActionCommand("New Transaction");
		
		editMenu.add(chartOfAccountsItem);
		editMenu.add(initialBalancesItem);
		editMenu.add(newTransactionItem);
		
		menuBar.add(editMenu);
		
		this.setJMenuBar(menuBar);
		
		chartOfAccounts = new ChartOfAccount();
		chartOfAccountWindow = new ChartOfAccountGUI(chartOfAccounts);
		chartOfAccountWindow.addWindowListener(this);
		
		initialBalancesWindow = new InitialBalancesGUI(chartOfAccounts);
		initialBalancesWindow.addWindowListener(this);
		
		masterJournal = new Journal();
		
		setTitle ("AstralBooks Accounting");
		setSize (1280, 760);
		setResizable (false);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null);  
	}

	public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();
		if (cmd.equals("Open Chart of Accounts"))
		{
			chartOfAccountWindow.setVisible(true);
		}
		else
		if (cmd.equals("Set Initial Balances"))
		{
			initialBalancesWindow = new InitialBalancesGUI(chartOfAccounts);
			initialBalancesWindow.setVisible(true);
			if (initialBalancesWindow.getExit() == "Done")
			{
				chartOfAccounts = initialBalancesWindow.getChartOfAccounts();
			}
		}
		else
		if (cmd.equals("New Transaction"))
		{
			TransactionGUI transactionWindow = new TransactionGUI(chartOfAccounts, masterJournal.size());
			transactionWindow.setVisible(true);
			if (transactionWindow.getExit() == "Save")
			{
				Transaction temp = transactionWindow.getTransaction();
				updateBalanceOnTransaction(temp);
				masterJournal.add(temp);
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		if (e.getSource() == chartOfAccountWindow)
			chartOfAccounts = chartOfAccountWindow.getChartOfAccounts();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) 
	{
		
	}
	
	public void updateBalanceOnTransaction(Transaction trans)
	{
		for (int c = 0; c < trans.size(); c++)
		{
			Entry temp = trans.get(c);
			if (temp.getType() == DEBIT)
			{
				int mod = -1;
				if (temp.getAccount().getType() == ASSET || temp.getAccount().getType() == EXPENSES)
				{
					mod = 1;
				}
				chartOfAccounts.getAccount(temp.getAccount().getId()).addToBalance(mod*temp.getVal());
			}
			else
			{
				int mod = 1;
				if (temp.getAccount().getType() == ASSET || temp.getAccount().getType() == EXPENSES)
				{
					mod = -1;
				}
				chartOfAccounts.getAccount(temp.getAccount().getId()).addToBalance(mod*temp.getVal());
			}
		}
	}
}
