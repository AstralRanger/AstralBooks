import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ChartOfAccount extends ArrayList <Account> 
{
	public ChartOfAccount ()
	{
		
	}
	
	public void quickSort ()
	{
		quickSort (0, this.size() - 1);
	}
	
	private void quickSort (int low, int high)
	{
		int i = low;
		int j = high;
		Account pivot = this.get((low + high)/2);
		while (i <= j)
		{
			while (this.get(i).getId() < pivot.getId())
				i++;
			while (this.get(j).getId() > pivot.getId())
				j--;
			while (i <= j)
			{
				Account temp = this.get(i);
				this.set(i, this.get(j));
				this.set(j, temp);
				i++;
				j--;
			}
		}
		if (low < i - 1)
			quickSort (low, i - 1);
		if (i < high)
			quickSort (i, high);
	}
	
	public boolean idExist (int id)
	{
		for (int c = 0; c < this.size(); c++)
		{
			if (this.get(c).getId() == id)
				return true;
		}
		return false;
	}
	
	public boolean setInitialbalance (int id, int initbal)
	{
		for (int c = 0; c < this.size(); c++)
		{
			if (this.get(c).getId() == id)
			{
				this.get(c).setInitialBalance(initbal);
				return true;
			}
		}
		return false;
	}
	
	public Account getAccount (int id)
	{
		for (int c = 0; c < this.size(); c++)
		{
			if (this.get(c).getId() == id)
				return this.get(c);
		}
		return null;
	}
	
	public int getIndex (int id)
	{
		for (int c = 0; c < this.size(); c++)
		{
			if (this.get(c).getId() == id)
				return c;
		}
		return -1;
	}
	
	// Return 0 if error, 1 if successful
	public int read (InputStream info)
	{
		this.clear();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(info))) 
		{
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		    	System.out.println(line);
		    	String name;
		    	int id, type;
		    	int sep = line.indexOf('_');
		    	name = line.substring(0, sep);
		    	line = line.substring(sep + 1);
		    	
		    	sep = line.indexOf('_');
		    	id = Integer.parseInt(line.substring(0, sep));
		    	type = Integer.parseInt(line.substring(sep + 1));
		    	
		    	Account temp = new Account (id, name, type);
		    	if (this.idExist(id))
		    	{
		    		this.clear();
		    		return 0;
		    	}
		    	this.add(temp);    	
		    }
		    return 1;
		} 
		catch (Exception e1) 
		{
			return 0;
		} 
	}
	
	public int write (ZipOutputStream fw)
	{
		 try 
         {
      	   for (int c = 0; c < this.size(); c++)
      		   fw.write((this.get(c).getName() + "_" +
      				    this.get(c).getId() + "_" +
      				    this.get(c).getType() + System.getProperty("line.separator")).getBytes());
      	   return 1;
         }
         catch (Exception e1)
         {
      	    return 0;
         }
	}
	
	public int readBalances (InputStream info)
	{
		try (BufferedReader br = new BufferedReader(new InputStreamReader(info))) 
		{
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		    	int id, initbal;
		    	int sep = line.indexOf('_');
		    	id = Integer.parseInt(line.substring(0, sep));
		    	initbal = Integer.parseInt(line.substring(sep + 1));
		    	if (!this.setInitialbalance(id, initbal))
		    	{
		    		for (int c = 0; c < this.size(); c++)
		    		{
		    			this.get(c).setInitialBalance(0);
		    		}
		    		return 0;
		    	}
		    }
		    return 1;
		} 
		catch (Exception e1) 
		{
			return 0;
		} 
	}
	
	public int writeBalances (ZipOutputStream fw)
	{
		try 
        {
     	   for (int c = 0; c < this.size(); c++)
     		   fw.write((this.get(c).getId() + "_" +
     				    this.get(c).getInitialBalance()  + System.getProperty("line.separator")).getBytes());
     	   return 1;
        }
        catch (Exception e1)
        {
     	   return 0;
        }
	}
}
