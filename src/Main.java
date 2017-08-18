
public class Main 
{
	public final int ASSET = 0;
	public final int LIABILITY = 1;
	public final int EQUITY = 2;
	public final int REVENUE = 3;
	public final int EXPENSES = 4;
	public final int DEBIT = 100;
	public final int CREDIT = 101;
	public final String accTypes[] = {"Asset", "Liability", "Equity", "Revenue", "Expense"};
	
	public static void main (String[] args)
    {
		Display display = new Display ();
		display.setVisible(true);	
    }
}
