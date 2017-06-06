
public class Entry {
	private Account acc;
	private int val, type;
	public final int DEBIT = 100;
	public final int CREDIT = 101;
	
	public Entry (Account acc, int val, int type)
	{
		this.acc = acc;
		this.val = val;
		this.type = type;
	}
	
	public int getVal()
	{
		return val;
	}
	
	public int getType()
	{
		return type;
	}
	
	public Account getAccount()
	{
		return acc;
	}
}
