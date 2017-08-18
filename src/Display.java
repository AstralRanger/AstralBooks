import java.awt.Image;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	JMenu fileMenu, editMenu, viewMenu;
	JMenuItem loadFileItem, saveFileItem, chartOfAccountsItem, initialBalancesItem,
			  newTransactionItem, viewJournalItem, viewLedgerItem, viewTBItem, viewBSItem, viewISItem;
	ChartOfAccount chartOfAccounts;
	ChartOfAccountGUI chartOfAccountWindow;
	InitialBalancesGUI initialBalancesWindow;
	Journal masterJournal;
	public Display ()
	{
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		viewMenu = new JMenu("View");
		
		loadFileItem = new JMenuItem("Load File");
		loadFileItem.addActionListener(this);
		loadFileItem.setActionCommand("Load File");
		
		saveFileItem = new JMenuItem("Save File");
		saveFileItem.addActionListener(this);
		saveFileItem.setActionCommand("Save File");
		
		fileMenu.add(loadFileItem);
		fileMenu.add(saveFileItem);
		
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
		
		viewJournalItem = new JMenuItem("Journal");
		viewJournalItem.addActionListener(this);
		viewJournalItem.setActionCommand("View Journal");
		
		viewLedgerItem = new JMenuItem("Ledger");
		viewLedgerItem.addActionListener(this);
		viewLedgerItem.setActionCommand("View Ledger");
		
		viewTBItem = new JMenuItem("Trial Balance");
		viewTBItem.addActionListener(this);
		viewTBItem.setActionCommand("View TB");
		
		viewBSItem = new JMenuItem("Balance Sheet");
		viewBSItem.addActionListener(this);
		viewBSItem.setActionCommand("View BS");
		
		viewISItem = new JMenuItem("Income Statement");
		viewISItem.addActionListener(this);
		viewISItem.setActionCommand("View IS");
		
		viewMenu.add(viewJournalItem);
		viewMenu.add(viewLedgerItem);
		viewMenu.add(viewTBItem);
		viewMenu.add(viewBSItem);
		viewMenu.add(viewISItem);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		
		this.setJMenuBar(menuBar);
		
		chartOfAccounts = new ChartOfAccount();
		chartOfAccountWindow = new ChartOfAccountGUI(chartOfAccounts);
		chartOfAccountWindow.addWindowListener(this);
		
		initialBalancesWindow = new InitialBalancesGUI(chartOfAccounts);
		initialBalancesWindow.addWindowListener(this);
		
		masterJournal = new Journal();
		
		
		setTitle ("AstralBooks Accounting");
		setSize (1280, 760);
		JLabel background;
		Image temp = null;
		try
	    {
			temp = ImageIO.read (new File ("images/bg.png")); // load file into Image object     
	    }
	    catch (IOException e) // Catch exception if image does not exist
	    {
	    }
		background = new JLabel(new ImageIcon(temp));
		setContentPane(background);
		setResizable (false);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null);  
	}

	public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();
		if (cmd.equals("Open Chart of Accounts"))
		{
			chartOfAccountWindow = new ChartOfAccountGUI(chartOfAccounts);
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
			TransactionGUI transactionWindow = new TransactionGUI(chartOfAccounts, masterJournal.size() + 1);
			transactionWindow.setVisible(true);
			if (transactionWindow.getExit() == "Save")
			{
				Transaction temp = transactionWindow.getTransaction();
				updateBalanceOnTransaction(temp);
				masterJournal.add(temp);
			}
		}
		else
		if (cmd.equals("View Ledger"))
		{
			if (masterJournal.size() == 0)
				JOptionPane.showMessageDialog(this, "No transactions found.", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				ViewLedgerGUI viewLedgerWindow = new ViewLedgerGUI(chartOfAccounts, masterJournal);
				viewLedgerWindow.setVisible(true);
			}
		}
		else
		if (cmd.equals("View Journal"))
		{
			if (masterJournal.size() == 0)
				JOptionPane.showMessageDialog(this, "No transactions found.", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				ViewJournalGUI viewJournalWindow = new ViewJournalGUI(masterJournal);
				viewJournalWindow.setVisible(true);
			}
		}
		else
		if (cmd.equals("View TB"))
		{
			generateTB();
		}
		else
		if (cmd.equals("View IS"))
		{
			generateIS();
		}
		else
		if (cmd.equals("View BS"))
		{
			generateBS();
		}
		if (cmd.equals("Save File"))
		{
			try
			{
				JFileChooser browse = new JFileChooser();
				int res = browse.showSaveDialog(Display.this);
				if (res == JFileChooser.APPROVE_OPTION)
				{
					FileOutputStream fos = new FileOutputStream(browse.getSelectedFile() + ".zip");
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					ZipOutputStream zos = new ZipOutputStream(bos);
					
					try
					{		
						zos.putNextEntry(new ZipEntry(browse.getSelectedFile().getName() + "_coa.txt"));
						chartOfAccounts.write(zos);
						zos.closeEntry();
						
						zos.putNextEntry(new ZipEntry(browse.getSelectedFile().getName() + "_initbal.txt"));
						chartOfAccounts.writeBalances(zos);
						zos.closeEntry();
						
						zos.putNextEntry(new ZipEntry(browse.getSelectedFile().getName() + "_journalentries.txt"));
						masterJournal.write(zos);
						zos.closeEntry();
					}
					finally
					{
						zos.close();
					}
				}
			}
			catch (Exception e1)
			{
				System.out.println("Hi" + e1.getMessage());
			}
		}
		else
		if (cmd.equals("Load File"))
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showOpenDialog(Display.this);
			if (res == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					File info = browse.getSelectedFile();
					String path = info.getPath();
					String extension = path.substring(path.lastIndexOf('.') + 1);
					System.out.println(info.getName());
					if (extension.equalsIgnoreCase("zip") || extension.equalsIgnoreCase("rar"))
					{
						FileInputStream fis = new FileInputStream(info);
						ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
						ZipFile zipfile = new ZipFile(info);
						
						ZipEntry chartEntry = zis.getNextEntry();
						InputStream inputChart = zipfile.getInputStream(chartEntry);
						chartOfAccounts.read(inputChart);
						inputChart.close();
						
						chartEntry = zis.getNextEntry();
						InputStream inputBalance = zipfile.getInputStream(chartEntry);
						chartOfAccounts.readBalances(inputBalance);
						inputBalance.close();
						
						chartEntry = zis.getNextEntry();
						InputStream inputJournal = zipfile.getInputStream(chartEntry);
						masterJournal.read(inputJournal);
						inputJournal.close();
						
					}
				}
				catch (Exception e1)
				{
					System.out.println(e1.getMessage());
				}
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
	@SuppressWarnings("deprecation")
	public void generateTB()
	{
		Hashtable<Integer, Integer> accountBalances = new Hashtable<Integer, Integer>();
		
		for (int c = 0; c < chartOfAccounts.size(); c++)
		{
			accountBalances.put(new Integer(chartOfAccounts.get(c).getId()), new Integer(chartOfAccounts.get(c).getInitialBalance()));
		}
		for (int c = 0; c < masterJournal.size(); c++)
		{
			for (int d = 0; d < masterJournal.get(c).size(); d++)
			{
				if (masterJournal.get(c).get(d).getType() == masterJournal.get(c).DEBIT)
				{
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().ASSET ||
							masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().EXPENSES)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance + masterJournal.get(c).get(d).getVal());
									  
					}
					else
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance - masterJournal.get(c).get(d).getVal());
					}
				}
				else
				{
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().ASSET ||
							masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().EXPENSES)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance - masterJournal.get(c).get(d).getVal());
					}
					else
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance + masterJournal.get(c).get(d).getVal());
					}
				}
			}
		}
		try
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showSaveDialog(Display.this);
			if (res == JFileChooser.APPROVE_OPTION)
			{
				  int totalCR = 0, totalDR = 0;
			      XSSFWorkbook workbook = new XSSFWorkbook(); 
			      XSSFSheet spreadsheet = workbook.createSheet(" General Journal ");
			      spreadsheet.setColumnWidth(0, 16000);
			      spreadsheet.setColumnWidth(1, 3000);
			      spreadsheet.setColumnWidth(2, 3000);
			      ArrayList <XSSFRow> rows = new ArrayList <XSSFRow>();
			      int currentRow = 0;
			      Enumeration<Integer> listOfIds = accountBalances.keys();
			      List<Integer> list = Collections.list(listOfIds);
			      Collections.sort(list);
			      
			      XSSFCellStyle topBorder = workbook.createCellStyle();
			      topBorder.setBorderTop(XSSFCellStyle.BORDER_THICK);
			      for (Integer tempId : list)
			      {
			    	  rows.add(spreadsheet.createRow(currentRow));
			    	  rows.get(currentRow).createCell(0).setCellValue(chartOfAccounts.getAccount(tempId).toString());;
			    	  if (chartOfAccounts.getAccount(tempId).getType() == chartOfAccounts.getAccount(tempId).ASSET
			    			  || chartOfAccounts.getAccount(tempId).getType() == chartOfAccounts.getAccount(tempId).EXPENSES)
			    	  {
			    		  rows.get(currentRow).createCell(1).setCellValue(accountBalances.get(new Integer (tempId)));
			    		  rows.get(currentRow).createCell(2).setCellValue(0);
			    	  }
			    	  else
			    	  {
			    		  rows.get(currentRow).createCell(1).setCellValue(0);
			    		  rows.get(currentRow).createCell(2).setCellValue(accountBalances.get(new Integer (tempId)));
			    	  } 	  
			    	  currentRow++;
			      }
			      for (int c = 0; c < rows.size(); c++)
			      {
			    	  totalDR += rows.get(c).getCell(1).getNumericCellValue();
			    	  totalCR += rows.get(c).getCell(2).getNumericCellValue();
			      }
			      rows.add(spreadsheet.createRow(currentRow));
			      rows.get(currentRow).createCell(1).setCellValue(totalDR);
			      rows.get(currentRow).createCell(2).setCellValue(totalCR);
			      rows.get(currentRow).getCell(1).setCellStyle(topBorder);
			      rows.get(currentRow).getCell(2).setCellStyle(topBorder);
			      FileOutputStream out = new FileOutputStream(browse.getSelectedFile() + ".xlsx");
			      workbook.write(out);
			      out.close();
			}
		}
		catch (Exception e1)
		{
			System.out.println(e1.getMessage());
			
		}
		
	}
	
	public void generateBS()
	{
		Hashtable<Integer, Integer> accountBalances = new Hashtable<Integer, Integer>();
		
		for (int c = 0; c < chartOfAccounts.size(); c++)
		{
			if (chartOfAccounts.get(c).getType() == chartOfAccounts.get(c).ASSET || 
					chartOfAccounts.get(c).getType() == chartOfAccounts.get(c).LIABILITY)
				accountBalances.put(new Integer(chartOfAccounts.get(c).getId()), new Integer(chartOfAccounts.get(c).getInitialBalance()));
		}

		for (int c = 0; c < masterJournal.size(); c++)
		{
			for (int d = 0; d < masterJournal.get(c).size(); d++)
			{
				if (masterJournal.get(c).get(d).getType() == masterJournal.get(c).DEBIT)
				{
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().ASSET)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance + masterJournal.get(c).get(d).getVal());
									  
					}
					else
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().LIABILITY)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance - masterJournal.get(c).get(d).getVal());
					}
				}
				else
				{
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().ASSET)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance - masterJournal.get(c).get(d).getVal());
					}
					else
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().LIABILITY)
					{ 
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance + masterJournal.get(c).get(d).getVal());
					}
				}
			}
		}
		
		try
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showSaveDialog(Display.this);
			if (res == JFileChooser.APPROVE_OPTION)
			{
				  System.out.println("d");
				  int totalAssets = 0, totalLiabilities = 0;
			      XSSFWorkbook workbook = new XSSFWorkbook(); 
			      XSSFSheet spreadsheet = workbook.createSheet(" Balance Sheet ");
			      spreadsheet.setColumnWidth(0, 16000);
			      spreadsheet.setColumnWidth(1, 3000);
			      ArrayList <XSSFRow> rows = new ArrayList <XSSFRow>();
			      int currentRow = 1;
			      Enumeration<Integer> listOfIds = accountBalances.keys();
			      List<Integer> list = Collections.list(listOfIds);
			      Collections.sort(list);
			      
			      XSSFCellStyle topBorder = workbook.createCellStyle();
			      topBorder.setBorderTop(XSSFCellStyle.BORDER_THICK);
			      
			      rows.add(spreadsheet.createRow(0));
			      rows.get(0).createCell(0).setCellValue("ASSETS");
			      
			      System.out.println("e");
			      for (Integer tempId : list)
			      {
			    	  if (chartOfAccounts.getAccount(tempId).getType() == chartOfAccounts.getAccount(tempId).ASSET)
			    	  {
			    		  rows.add(spreadsheet.createRow(currentRow));
				    	  rows.get(currentRow).createCell(0).setCellValue(chartOfAccounts.getAccount(tempId).toString());;
			    		  rows.get(currentRow).createCell(1).setCellValue(accountBalances.get(new Integer (tempId)));
			    		  totalAssets += accountBalances.get(new Integer (tempId));
			    		  currentRow++;
			    	  }    
			      }
			      rows.add(spreadsheet.createRow(currentRow));
			      rows.get(currentRow).createCell(1).setCellValue(totalAssets);
			      rows.get(currentRow).getCell(1).setCellStyle(topBorder);
			      currentRow++;
			      
			      rows.add(spreadsheet.createRow(currentRow));
			      rows.get(currentRow).createCell(0).setCellValue("LIABILITIES");
			      currentRow++;
			      
			      
			      for (Integer tempId : list)
			      {
			    	  if (chartOfAccounts.getAccount(tempId).getType() == chartOfAccounts.getAccount(tempId).LIABILITY)
			    	  {
			    		  rows.add(spreadsheet.createRow(currentRow));
				    	  rows.get(currentRow).createCell(0).setCellValue(chartOfAccounts.getAccount(tempId).toString());;
			    		  rows.get(currentRow).createCell(1).setCellValue(accountBalances.get(new Integer (tempId)));
			    		  totalLiabilities += accountBalances.get(new Integer (tempId));
			    		  currentRow++;
			    	  }    
			      }
			      
			      rows.add(spreadsheet.createRow(currentRow));
			      rows.get(currentRow).createCell(1).setCellValue(totalLiabilities);
			      rows.get(currentRow).getCell(1).setCellStyle(topBorder);
			      currentRow++;
			      rows.add(spreadsheet.createRow(currentRow));

			      rows.get(currentRow).createCell(0).setCellValue("Owner's Equity");
				  rows.get(currentRow).createCell(1).setCellValue(totalAssets - totalLiabilities);
			      rows.get(currentRow).getCell(1).setCellStyle(topBorder);
			      
			      System.out.println("f");
			      FileOutputStream out = new FileOutputStream(browse.getSelectedFile() + ".xlsx");
			      workbook.write(out);
			      out.close();
			      System.out.println("h");
			}
		}
		catch (Exception e1)
		{
			System.out.println(e1.getMessage());
			
		}
	}
	public void generateIS()
	{
		Hashtable<Integer, Integer> accountBalances = new Hashtable<Integer, Integer>();
		
		for (int c = 0; c < chartOfAccounts.size(); c++)
		{
			if (chartOfAccounts.get(c).getType() == chartOfAccounts.get(c).REVENUE || 
					chartOfAccounts.get(c).getType() == chartOfAccounts.get(c).EXPENSES)
				accountBalances.put(new Integer(chartOfAccounts.get(c).getId()), new Integer(chartOfAccounts.get(c).getInitialBalance()));
		}

		for (int c = 0; c < masterJournal.size(); c++)
		{
			for (int d = 0; d < masterJournal.get(c).size(); d++)
			{
				if (masterJournal.get(c).get(d).getType() == masterJournal.get(c).DEBIT)
				{
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().EXPENSES)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance + masterJournal.get(c).get(d).getVal());
									  
					}
					else
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().REVENUE)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance - masterJournal.get(c).get(d).getVal());
					}
				}
				else
				{
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().EXPENSES)
					{
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance - masterJournal.get(c).get(d).getVal());
					}
					else
					if (masterJournal.get(c).get(d).getAccount().getType() == masterJournal.get(c).get(d).getAccount().REVENUE)
					{ 
						int balance = accountBalances.get(masterJournal.get(c).get(d).getAccount().getId());
						accountBalances.put(masterJournal.get(c).get(d).getAccount().getId(), 
											balance + masterJournal.get(c).get(d).getVal());
					}
				}
			}
		}
		
		try
		{
			JFileChooser browse = new JFileChooser();
			int res = browse.showSaveDialog(Display.this);
			if (res == JFileChooser.APPROVE_OPTION)
			{
				  System.out.println("d");
				  int totalRevenue = 0, totalExpenses = 0;
			      XSSFWorkbook workbook = new XSSFWorkbook(); 
			      XSSFSheet spreadsheet = workbook.createSheet(" Income Statement ");
			      spreadsheet.setColumnWidth(0, 16000);
			      spreadsheet.setColumnWidth(1, 3000);
			      ArrayList <XSSFRow> rows = new ArrayList <XSSFRow>();
			      int currentRow = 1;
			      Enumeration<Integer> listOfIds = accountBalances.keys();
			      List<Integer> list = Collections.list(listOfIds);
			      Collections.sort(list);
			      
			      XSSFCellStyle topBorder = workbook.createCellStyle();
			      topBorder.setBorderTop(XSSFCellStyle.BORDER_THICK);
			      
			      rows.add(spreadsheet.createRow(0));
			      rows.get(0).createCell(0).setCellValue("ASSETS");
			      
			      System.out.println("e");
			      for (Integer tempId : list)
			      {
			    	  if (chartOfAccounts.getAccount(tempId).getType() == chartOfAccounts.getAccount(tempId).REVENUE)
			    	  {
			    		  rows.add(spreadsheet.createRow(currentRow));
				    	  rows.get(currentRow).createCell(0).setCellValue(chartOfAccounts.getAccount(tempId).toString());;
			    		  rows.get(currentRow).createCell(1).setCellValue(accountBalances.get(new Integer (tempId)));
			    		  totalRevenue += accountBalances.get(new Integer (tempId));
			    		  currentRow++;
			    	  }    
			      }
			      rows.add(spreadsheet.createRow(currentRow));
			      rows.get(currentRow).createCell(1).setCellValue(totalRevenue);
			      rows.get(currentRow).getCell(1).setCellStyle(topBorder);
			      currentRow++;
			      
			      rows.add(spreadsheet.createRow(currentRow));
			      rows.get(currentRow).createCell(0).setCellValue("EXPENSES");
			      currentRow++;
			      
			      
			      for (Integer tempId : list)
			      {
			    	  if (chartOfAccounts.getAccount(tempId).getType() == chartOfAccounts.getAccount(tempId).EXPENSES)
			    	  {
			    		  rows.add(spreadsheet.createRow(currentRow));
				    	  rows.get(currentRow).createCell(0).setCellValue(chartOfAccounts.getAccount(tempId).toString());;
			    		  rows.get(currentRow).createCell(1).setCellValue(accountBalances.get(new Integer (tempId)));
			    		  totalExpenses += accountBalances.get(new Integer (tempId));
			    		  currentRow++;
			    	  }    
			      }
			      
			      rows.add(spreadsheet.createRow(currentRow));
			      rows.get(currentRow).createCell(1).setCellValue(totalExpenses);
			      rows.get(currentRow).getCell(1).setCellStyle(topBorder);
			      currentRow++;
			      rows.add(spreadsheet.createRow(currentRow));
			      if (totalRevenue - totalExpenses >= 0)
			      {
 
				      rows.get(currentRow).createCell(0).setCellValue("Net Income");
				      rows.get(currentRow).createCell(1).setCellValue(totalRevenue - totalExpenses);
			      }
			      else
			      {
			    	  rows.get(currentRow).createCell(0).setCellValue("Net Loss");
				      rows.get(currentRow).createCell(1).setCellValue(totalExpenses - totalRevenue);
			      }
			      rows.get(currentRow).getCell(1).setCellStyle(topBorder);
			      
			      System.out.println("f");
			      FileOutputStream out = new FileOutputStream(browse.getSelectedFile() + ".xlsx");
			      workbook.write(out);
			      out.close();
			      System.out.println("h");
			}
		}
		catch (Exception e1)
		{
			System.out.println(e1.getMessage());
			
		}
	}
}
