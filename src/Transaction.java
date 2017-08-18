import java.util.ArrayList;

@SuppressWarnings("serial")
public class Transaction extends ArrayList <Entry>
{
	public final int DEBIT = 100;
	public final int CREDIT = 101;
	public boolean isBalanced()
	{
		if (this.size() == 0)
			return false;
		int totalDR = 0, totalCR = 0;
		for (int c = 0; c < this.size(); c++)
		{
			if (this.get(c).getType() == DEBIT)
				totalDR += this.get(c).getVal();
			else
				totalCR += this.get(c).getVal();
		}
		return totalDR == totalCR;
	}
	
	public void read (String line)
	{
		while (line.length() > 0)
		{
			System.out.println(line);
			int sepIndex = line.indexOf("_");
			String entryStr = line.substring(0, sepIndex);
			
			
			int id = Integer.parseInt(entryStr.substring(0, entryStr.indexOf("*")));
			entryStr = entryStr.substring(entryStr.indexOf("*") + 1);
			System.out.println(entryStr);
			String name = entryStr.substring(0, entryStr.indexOf("*"));
			entryStr = entryStr.substring(entryStr.indexOf("*") + 1);
			System.out.println(entryStr);
			int type = Integer.parseInt(entryStr.substring(0, entryStr.indexOf("*")));
			entryStr = entryStr.substring(entryStr.indexOf("*") + 1);
			
			System.out.println(entryStr);
			int initBal = Integer.parseInt(entryStr.substring(0, entryStr.indexOf("*")));
			entryStr = entryStr.substring(entryStr.indexOf("*") + 1);
			System.out.println(entryStr);
			int val = Integer.parseInt(entryStr.substring(0, entryStr.indexOf("*")));
			entryStr = entryStr.substring(entryStr.indexOf("*") + 1);
			System.out.println(entryStr);
			int entryType = Integer.parseInt(entryStr);
			System.out.println("HI");
			
			this.add(new Entry(new Account(id, name, type, initBal), val, entryType));
			line = line.substring(sepIndex + 1);
		}
	}	
	
	public String write() 
	{
		String temp = "";
		for (int c = 0; c < this.size(); c++)
		{
			temp = temp + this.get(c).getAccount().getId() + "*" +
						  this.get(c).getAccount().getName() + "*" +
						  this.get(c).getAccount().getType() + "*" +
						  this.get(c).getAccount().getInitialBalance() + "*" + 
						  this.get(c).getVal() + "*" +
						  this.get(c).getType() + "_";
		}
		return temp;
	}
}
