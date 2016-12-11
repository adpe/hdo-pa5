package ch.ffhs.hdo.client.ui.einstellungen.executable;

import ch.ffhs.hdo.client.ui.base.viewhandler.Executable;
import ch.ffhs.hdo.client.ui.einstellungen.OptionController;
import ch.ffhs.hdo.client.ui.einstellungen.OptionModel;
import ch.ffhs.hdo.client.ui.hauptfenster.FolderModel;
import ch.ffhs.hdo.infrastructure.option.OptionFacade;

public class OptionViewStartExecutable implements Executable<FolderModel> {

	public void execute(FolderModel folderModel) {

		OptionFacade facade = new OptionFacade();
		OptionModel model = facade.getModel();
		model.setFolderModel(folderModel);
		OptionController optionController = new OptionController(model, folderModel);

		optionController.show();
	}

}
