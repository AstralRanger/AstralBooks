import java.util.ArrayList;

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
	
}
