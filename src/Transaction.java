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
}
