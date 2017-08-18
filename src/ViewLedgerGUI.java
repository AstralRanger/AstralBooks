import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ViewLedgerGUI extends JDialog implements ActionListener {
	private Account account;
	private Journal generalJournal;
	private int balance;
	
	private JSpinner min, max;
	private SpinnerModel minModel, maxModel;
	private JButton generateButton, cancelButton;
	private JPanel content, mid, bot;
	private int size;
	private ChartOfAccount chartOfAccount;
	private JComboBox<Object> accountLists;
	
	public ViewLedgerGUI(ChartOfAccount chartOfAccount, Journal generalJournal)
	{
		this.generalJournal = generalJournal;
		this.chartOfAccount = chartOfAccount;
		
		accountLists = new JComboBox<Object> (chartOfAccount.toArray());
		
		size = generalJournal.size();
		
		minModel = new SpinnerNumberModel(1, 1, size, 1); // Watch out for case where journal is empty
		maxModel = new SpinnerNumberModel(size, 1, size, 1);
		
		min = new JSpinner(minModel);
		max = new JSpinner(maxModel);

		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		generateButton.setActionCommand("Generate");
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		
		mid = new JPanel(new GridLayout(1, 5));
		bot = new JPanel (new GridLayout(1, 8));
		content = new JPanel(new BorderLayout());
		
		mid.add(accountLists);
		mid.add(new JLabel("Start Transaction: "));
		mid.add(min);
		mid.add(new JLabel("End Transaction: "));
		mid.add(max);
		
		bot.add(new JLabel());
		bot.add(generateButton);
		bot.add(new JLabel());
		bot.add(new JLabel());
		bot.add(cancelButton);
		bot.add(new JLabel());
		
		content.add(mid, BorderLayout.CENTER);
		content.add(bot, BorderLayout.PAGE_END);	
		
		setContentPane(content);
		setTitle ("View Ledger");
		setSize (900, 90);
		setResizable (false);
		
		setModal (true);
	    setAlwaysOnTop (true);
		setModalityType (ModalityType.APPLICATION_MODAL);
		
		setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
		setLocationRelativeTo (null);  
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Generate")
		{
			try
			{
				JFileChooser browse = new JFileChooser();
				int res = browse.showSaveDialog(ViewLedgerGUI.this);
				if (res == JFileChooser.APPROVE_OPTION)
				{
					  //System.out.println("a");
				      XSSFWorkbook workbook = new XSSFWorkbook(); 
				      XSSFSheet spreadsheet = workbook.createSheet(" Ledger ");
				      XSSFCellStyle centered = workbook.createCellStyle();
				      XSSFCellStyle leftBorder = workbook.createCellStyle();
				      XSSFCellStyle rightBorder = workbook.createCellStyle();
				      XSSFCellStyle topBorder = workbook.createCellStyle();
				      Account selected = (Account) accountLists.getSelectedItem();
				      centered.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				      leftBorder.setBorderLeft(XSSFCellStyle.BORDER_THICK);
				      rightBorder.setBorderRight(XSSFCellStyle.BORDER_THICK);
				      topBorder.setBorderTop(XSSFCellStyle.BORDER_THICK);
				      
				      spreadsheet.setColumnWidth(0, 10000);
				      spreadsheet.setColumnWidth(1, 10000);
				      ArrayList <XSSFRow> rows = new ArrayList <XSSFRow>();
				      //System.out.println("b");
				      rows.add(spreadsheet.createRow(0));
				      rows.add(spreadsheet.createRow(1));
				      rows.add(spreadsheet.createRow(2));
				      rows.get(0).createCell(0).setCellValue(accountLists.getSelectedItem().toString());
				      rows.get(0).getCell(0).setCellStyle(centered);
				      
				      spreadsheet.addMergedRegion(new CellRangeAddress (0, 0, 0, 1));
				      //System.out.println("c");
				      
				      rows.get(1).createCell(0).setCellValue("DR");
				      //System.out.println("c1");
				      rows.get(1).getCell(0).setCellStyle(centered);
				      //System.out.println("c2");
				      rows.get(1).createCell(1).setCellValue("CR");
				      //System.out.println("c3");
				      rows.get(1).getCell(1).setCellStyle(centered);
				      //System.out.println("c4");
				      
				      int currentBalance = 0;
				      // System.out.println("d");
				      if (selected.getType() == selected.ASSET || selected.getType() == selected.EXPENSES)
				      {
				    	  rows.get(2).createCell(0).setCellValue(selected.getInitialBalance());
				    	  currentBalance += selected.getInitialBalance();
				    	  rows.get(2).getCell(0).setCellStyle(rightBorder);
				      }
				      else
				      {
				    	  rows.get(2).createCell(1).setCellValue(selected.getInitialBalance());
				    	  currentBalance -= selected.getInitialBalance();
				    	  rows.get(2).getCell(2).setCellStyle(leftBorder);
				      }
				      //System.out.println("e");
				      int currentRow = 3;
				      
				      for (int c = (int) min.getValue() - 1; c <= (int) max.getValue() - 1; c++)
				      {
				    	  for (int d = 0; d < generalJournal.get(c).size(); d++)
				    	  {
				    		  if (generalJournal.get(c).get(d).getAccount().getId() == selected.getId())
				    		  {
				    			  rows.add(spreadsheet.createRow(currentRow));
				    			  if (generalJournal.get(c).get(d).getType() == generalJournal.get(c).get(d).DEBIT)
							      {
							    	  rows.get(currentRow).createCell(0).setCellValue(generalJournal.get(c).get(d).getVal());
							    	  rows.get(currentRow).getCell(0).setCellStyle(rightBorder);
							    	  currentBalance += generalJournal.get(c).get(d).getVal();
							      }
							      else
							      {
							    	  rows.get(currentRow).createCell(1).setCellValue(generalJournal.get(c).get(d).getVal());
							    	  rows.get(currentRow).getCell(1).setCellStyle(leftBorder);
							    	  currentBalance -= generalJournal.get(c).get(d).getVal();
							      }
				    			  currentRow++;
				    		  }
				    	  }
				      }
				      //System.out.println("f");
				      rows.add(spreadsheet.createRow(currentRow));
				      rows.get(currentRow).createCell(0).setCellStyle(topBorder);
				      rows.get(currentRow).createCell(1).setCellStyle(topBorder);
				      if (currentBalance >= 0)
				      {
				    	  rows.get(currentRow).getCell(0).setCellValue(currentBalance);
				      }
				      else
				      {
				    	  rows.get(currentRow).getCell(1).setCellValue(currentBalance*-1);
				      }
				      
				      //System.out.println("abc");
				      
				      FileOutputStream out = new FileOutputStream(browse.getSelectedFile() + ".xlsx");
				      workbook.write(out);
				      out.close();
				      
				      //System.out.println("def");
				}
			}
			catch (Exception e1)
			{
				System.out.println(e1.getMessage());
				
			}
		}
		setVisible (false);
		
	}
	
}
