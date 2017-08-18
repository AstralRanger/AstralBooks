import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ViewJournalGUI extends JDialog implements ActionListener {
	private Journal generalJournal;
	private int size;
	private JSpinner min, max;
	private SpinnerModel minModel, maxModel;
	private JButton generateButton, cancelButton;
	private JPanel content, mid, bot;
	
	
	public ViewJournalGUI(Journal generalJournal)
	{
		this.generalJournal = generalJournal;
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
		
		mid = new JPanel(new GridLayout(1, 4));
		bot = new JPanel (new GridLayout(1, 8));
		content = new JPanel(new BorderLayout());
		
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
		setTitle ("View Journal");
		setSize (780, 90);
		setResizable (false);
		
		setModal (true);
	    setAlwaysOnTop (true);
		setModalityType (ModalityType.APPLICATION_MODAL);
		
		setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
		setLocationRelativeTo (null);  
	}
	
	
	public void actionPerformed(ActionEvent e)  {
		
		if (e.getActionCommand() == "Generate")
		{
			try
			{
				JFileChooser browse = new JFileChooser();
				int res = browse.showSaveDialog(ViewJournalGUI.this);
				if (res == JFileChooser.APPROVE_OPTION)
				{
				      XSSFWorkbook workbook = new XSSFWorkbook(); 
				      XSSFSheet spreadsheet = workbook.createSheet(" General Journal ");
				      spreadsheet.setColumnWidth(0, 1500);
				      spreadsheet.setColumnWidth(1, 16000);
				      spreadsheet.setColumnWidth(2, 3000);
				      spreadsheet.setColumnWidth(3, 3000);
				      ArrayList <XSSFRow> rows = new ArrayList <XSSFRow>();
				      int currentRow = 0;
				      for (int c = (int) min.getValue() - 1; c <= (int) max.getValue() - 1; c++)
				      {
				    	  for (int d = 0; d < generalJournal.get(c).size(); d++)
				    	  {
				    		  rows.add(spreadsheet.createRow(currentRow));
					    	  if (d == 0)
					    		  rows.get(currentRow).createCell(0).setCellValue(c + 1);
					    	  if (generalJournal.get(c).get(d).getType() == generalJournal.get(c).get(d).DEBIT)
					    	  {
					    		  rows.get(currentRow).createCell(1).setCellValue
					    		  (generalJournal.get(c).get(d).getAccount().getName());
					    		  rows.get(currentRow).createCell(2).setCellValue
					    		  (generalJournal.get(c).get(d).getVal());
					    	  }
					    	  else
					    	  {
					    		  rows.get(currentRow).createCell(1).setCellValue
					    		  ("   " + generalJournal.get(c).get(d).getAccount().getName());
					    		  rows.get(currentRow).createCell(3).setCellValue
					    		  (generalJournal.get(c).get(d).getVal());
					    	  }
					    	  currentRow++;
				    	  }
				      }
				      
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
		setVisible(false);
	}

}
