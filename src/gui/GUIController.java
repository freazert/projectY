package gui;

public class GUIController
{

	public GUIController()
	{

	}

	public void refreshList(String dir)
	{
		GUIModel gui_model = new GUIModel();
		gui_model.refreshList(dir);
	}

}
