
@SuppressWarnings("serial")
public class EditAccountGUI extends AddAccountGUI
{
	public EditAccountGUI(int id, String name, int type)
	{
		super();
		nameField.setText(name);
		idField.setText("" + id);
		typeField.setSelectedItem(typeField.getItemAt(type));
		setTitle ("Edit An Account");
	}
}
