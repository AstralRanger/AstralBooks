import java.util.Comparator;

public class Account 
{
	public final int ASSET = 0;
	public final int LIABILITY = 1;
	public final int EQUITY = 2;
	public final int REVENUE = 3;
	public final int EXPENSES = 4;
	private String name;
	private int id, initialBalance, type, bal;
	
	public Account (int id, String name, int type)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		initialBalance = 0;
	}
	
	public Account (int id, String name, int type, int balance)
	{
		this(id, name, type);
		this.initialBalance = balance;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getInitialBalance()
	{
		return initialBalance;
	}
	
	public void setInitialBalance(int balance)
	{
		initialBalance = balance;
		bal = balance;
	}
	
	public void addToBalance (int val)
	{
		bal += val;
	}
	
	public String toString()
	{
		return name + " (" + id + ")";
	}
	
	public int compareTo(Account acc)
	{
		return this.id - acc.getId();
	}
	
	public boolean isBalanceSheetAccount ()
	{
		return (type == ASSET || type == LIABILITY || type == EQUITY);
	}
	
	public static Comparator<Account> AccountIdComparator = new Comparator <Account>()
			{
				public int compare(Account a1, Account a2) 
				{
					return a1.compareTo(a2);
				}
			};
	
}
