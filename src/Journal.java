import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;

public class Journal extends ArrayList <Transaction>
{
	public Journal ()
	{
		
	}
	
	public int read (InputStream info)
	{
		try (BufferedReader br = new BufferedReader(new InputStreamReader(info))) 
		{
		    String line;
		    
		    while ((line = br.readLine()) != null) 
		    {
		    	System.out.println(line);
		    	Transaction temp = new Transaction();
		    	temp.read(line);
		    	add(temp);
		    }
		    return 1;
		} 
		catch (Exception e1) 
		{
			System.out.println(e1.getMessage());
			return 0;
		} 
	}
	
	public int write (ZipOutputStream zos)
	{
		 try 
         {
      	   for (int c = 0; c < this.size(); c++)
      		  zos.write((this.get(c).write() + System.getProperty("line.separator")).getBytes());
      	   return 1;
         }
         catch (Exception e1)
         {
      	    return 0;
         }
	}

}
