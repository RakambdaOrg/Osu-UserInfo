package fr.mrcraftcod.listeners.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import fr.mrcraftcod.interfaces.InterfaceSettings;
import fr.mrcraftcod.utils.Utils;

public class ItemSettingsActionListener extends AbstractAction
{
	private static final long serialVersionUID = 151605064904946278L;

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Utils.configFrame = new InterfaceSettings();
	}
}