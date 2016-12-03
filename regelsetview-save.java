package ch.ffhs.hdo.client.ui.regelset;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.builder.FormBuilder;

import ch.ffhs.hdo.client.ui.base.View;
import ch.ffhs.hdo.client.ui.base.executable.CloseViewOperation;
import ch.ffhs.hdo.client.ui.regelset.executable.RegelsetSaveOperation;
import ch.ffhs.hdo.client.ui.utils.ChooseDirectoryPathViewOperation;
import ch.ffhs.hdo.infrastructure.service.util.FileHandling;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/******************************************************
 * 
 * 
 * 
 * wird noch �berarbeitet !!!!!!!!!!!!!!!!
 * 
 * 
 * 
 * 
 * 
 * @author Daniel Crazzolara
 *
 */

public class RegelsetView extends View<RegelsetModel> {
	
	private static Logger LOGGER = LogManager.getLogger(FileHandling.class);

	private final String I18N = "hdo.regelset";
	private final String TITLE_KEY = I18N + ".title";
	private final String CONTEXT_COMBOBOXKEY = I18N + ".rule.context";
	private final String ATTRIBUTE_COMBOBOXKEY = I18N + ".rule.attribute";
	private JTextField regelsetNameTextField;
	private JTextField dateinamenKonfigurationTextField;
	private JTextField fromDateTextField[];
	private JTextField toDateTextField[];


	private JTextField targetDirectoryTextField;
	
	private JButton fileChooseButton;

    private JComboBox targetDirectoryComboBox;
    
     
    //  sind in Class RulePanel
	private JComboBox contextComboBox;
	private JComboBox attributeComboBox;

	
	private String contextComboBoxList[]  = new String[3];
	private String attributeComboBoxList[] = new String[3];
	private String targetDirectoryList[];

	private JButton addButton;
	private JButton deleteButton;

	private JButton saveButton;
	private JButton cancelButton;

	private JCheckBox statusCheckBox;

	JPanel rulePanel[] = new JPanel[4];
	private JTabbedPane tabbedPane;

	
	private static enum PanelTypeEnum {
		PANEL_CONTEXT, PANEL_CONTEXT_ATTRIBUTE, PANEL_COMPARISON;
	}	
	
	
	public RegelsetView(ResourceBundle resourceBundle) {
		super(resourceBundle);
		setTitle(getMessage(TITLE_KEY));

		initComponents();

	}

	private void initComponents() {
		createComponents();
		layoutForm();
	}

	
	private String[] getDirectories(String inboxDirectory, boolean recursiv) {
		//
		// TODO: nur tempor�r
		// 			Sp�ter werden die Directories direkt aus der Infra-Schicht geholt
		//
		//
		
		String []directories = {"c:\\daten\\temp\\rechunungen",
				"c:\\daten\\temp\\rechunungen",
				"c:\\daten\\temp\\rechunungen\\krankenkasse",
				"c:\\daten\\temp\\rechunungen\\cablecom",
				"c:\\daten\\temp\\fotos\\ferien2015",
				"c:\\daten\\temp\\fotos\\ferien2016",
				"c:\\daten\\temp\\divers",
				"c:\\daten\\temp\\wasWeissIch",
				"c:\\daten\\temp\\weiss der Gugger"}; 
		return directories;
	}
	
	private void createComponents() {

		regelsetNameTextField = new JTextField();

		targetDirectoryTextField = new JTextField();
		targetDirectoryTextField.setEditable(false);
		fileChooseButton = new JButton(getMessage("base.filechooser"));
		
		dateinamenKonfigurationTextField = new JTextField();
		fromDateTextField = new JTextField[4];
		toDateTextField = new JTextField[4];
		statusCheckBox = new JCheckBox(getMessage(I18N + ".checkbox.status"));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		contextComboBoxList[0] = getMessage(CONTEXT_COMBOBOXKEY + ".pdf.property");
		contextComboBoxList[1] = getMessage(CONTEXT_COMBOBOXKEY + ".file.attribute");
		contextComboBoxList[2] = getMessage(CONTEXT_COMBOBOXKEY + ".file.content");

		attributeComboBoxList[0] = getMessage(ATTRIBUTE_COMBOBOXKEY + ".date");
		attributeComboBoxList[1] = getMessage(ATTRIBUTE_COMBOBOXKEY + ".author");
		attributeComboBoxList[2] = getMessage(ATTRIBUTE_COMBOBOXKEY + ".size");
	/*
		attributeComboBox[0] = new JComboBox<String>(attributeComboBoxList);
		attributeComboBox[1] = new JComboBox<String>(attributeComboBoxList);
		attributeComboBox[2] = new JComboBox<String>(attributeComboBoxList);
		attributeComboBox[3] = new JComboBox<String>(attributeComboBoxList);
*/
		fromDateTextField[0] = new JTextField();
		fromDateTextField[1] = new JTextField();
		fromDateTextField[2] = new JTextField();
		fromDateTextField[3] = new JTextField();

		toDateTextField[0] = new JTextField();
		toDateTextField[1] = new JTextField();
		toDateTextField[2] = new JTextField();
		toDateTextField[3] = new JTextField();

		addButton = new JButton(getMessage(I18N + ".button.add.icon"));
		deleteButton = new JButton(getMessage(I18N + ".button.delete.icon"));

		saveButton = new JButton(getMessage("base.save"));
		cancelButton = new JButton(getMessage("base.cancel"));
		
		
		saveButton.addActionListener(new SaveRulesetAction());
		cancelButton.addActionListener(new CloseAction());
		fileChooseButton.addActionListener(new OpenDirectoryChooser());
	}

	
	/*
	 *  Besteht jeweils nur aus einem Panel, das wiederum aus mehreren Panels besteht
	 */
	JPanel makePanel(PanelTypeEnum panelType) {
		JPanel panel = null;
		
		FormBuilder panelBuilder = FormBuilder.create()
				.columns("right:pref, 5dlu,[20dlu, pref],5dlu,[20dlu, pref],5dlu, [20dlu, pref]")
				.rows("p, $lg, p, $lg, p, $lg, p, $lg, p , $lg, p , $lg, p");
		
		if (panelType == PanelTypeEnum.PANEL_CONTEXT)  {
			
			
			panel = panelBuilder.build();
		} else if (panelType == PanelTypeEnum.PANEL_CONTEXT_ATTRIBUTE) {
			
			
			panel = panelBuilder.build();
		} else if (panelType == PanelTypeEnum.PANEL_COMPARISON) {
			
			
			panel = panelBuilder.build();	
		} else {
			//logger.ERROR("TODO: unm�gliche Exception");
			System.out.println("ERROR - TODO: unm�gliche Exception");
			//TODO: throw ???
		}
		
		
		return panel;
		// nur Context-DropDown
	}
	

	
	
	JPanel makeRulePanel() {
		
		// ein Rule Panel wird dynamisch...
		//
		// je nach Conext-DropDown ist noch ein zus�tzliches ContentAttr-DropDown n�tig
		//
		// Comparison Teil ("von-bis" oder nur equal-Textbox ist abh. von den 2 Combos...
		//
		// in der Rule sind alle Elems drin; aber nicht alle sichtbar
		//	das n�tige wird dynamisch sichtbar gemacht / verschoben
		//  
		// wichtig ist das z.B. von/bis-Values erhalten bleiben bei Spielerei auf Equal..
		// 
		
		
		
		FormBuilder paneBuilder = FormBuilder.create()
				.columns("right:pref, 5dlu,[20dlu, pref],5dlu,[20dlu, pref],5dlu, [20dlu, pref]")
				.rows("p, $lg, p, $lg, p, $lg, p, $lg, p , $lg, p , $lg, p, , p , $lg, p , $lg, p");

		paneBuilder.addLabel(getMessage(I18N + ".label.sortrule")).rcw(1, 1, 7);

		// Panel besteht aus 3 Teilen
	//	JPanel contextPanel = makePanel(CONTEXT);
		//JPanel contextAttribtePanel = makePanel(CONTEXT_ATTIBUTE);
		//JPanel comparisonPanel = makePanel(COMPARISON);
		
		
		contextComboBox = new JComboBox<String>(contextComboBoxList);
		attributeComboBox = new JComboBox<String>(attributeComboBoxList);
	//	paneBuilder.add(contextPanel).rcw(3, 1, 1);

		// TODO: es wird immer die gleich ComoBox genommen => anpassen
		paneBuilder.add(attributeComboBox).rcw(3, 4, 3);

		paneBuilder.addLabel(getMessage(I18N + ".label.rule.dynamic")).rcw(5, 1, 7);
		paneBuilder.addLabel(getMessage(I18N + ".label.rule.from")).rcw(7, 1, 7);
		paneBuilder.addLabel(getMessage(I18N + ".label.rule.to")).rcw(7, 4, 3);

		// JFrame frame=new JFrame("date display");
		JDatePickerImpl datePickerFrom;
		JDatePickerImpl datePickerTo;
		UtilDateModel model = new UtilDateModel();

		model.setDate(2016, 11, 16);
		model.setSelected(true);
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		datePickerFrom = new JDatePickerImpl(datePanel, null);
		datePickerTo = new JDatePickerImpl(datePanel, null);

		// paneBuilder.add(fromDateTextField[i]).rcw(9, 1, 2);
		// paneBuilder.add(toDateTextField[i]).rcw(9, 4, 3);
		paneBuilder.add(datePickerFrom).rcw(9, 1, 2);
		paneBuilder.add(datePickerTo).rcw(9, 4, 3);

		paneBuilder.padding(new EmptyBorder(5, 5, 5, 5));
		
		return paneBuilder.build();
	}
	
	
	JPanel makePdfRulePanel() {
		FormBuilder paneBuilder = FormBuilder.create()
				.columns("right:pref, 5dlu,[20dlu, pref],5dlu,[20dlu, pref],5dlu, [20dlu, pref]")
				.rows("p, $lg, p, $lg, p, $lg, p, $lg, p , $lg, p , $lg, p");

		paneBuilder.addLabel(getMessage(I18N + ".label.sortrule")).rcw(1, 1, 7);

		contextComboBox = new JComboBox<String>(contextComboBoxList);
		paneBuilder.add(contextComboBox).rcw(3, 1, 1);

		// TODO: es wird immer die gleich ComoBox genommen => anpassen
		paneBuilder.add(attributeComboBox).rcw(3, 4, 3);

		paneBuilder.addLabel(getMessage(I18N + ".label.rule.dynamic")).rcw(5, 1, 7);
		paneBuilder.addLabel(getMessage(I18N + ".label.rule.from")).rcw(7, 1, 7);
		paneBuilder.addLabel(getMessage(I18N + ".label.rule.to")).rcw(7, 4, 3);

		// JFrame frame=new JFrame("date display");
		JDatePickerImpl datePickerFrom;
		JDatePickerImpl datePickerTo;
		UtilDateModel model = new UtilDateModel();

		model.setDate(2016, 11, 16);
		model.setSelected(true);
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		datePickerFrom = new JDatePickerImpl(datePanel, null);
		datePickerTo = new JDatePickerImpl(datePanel, null);

		// paneBuilder.add(fromDateTextField[i]).rcw(9, 1, 2);
		// paneBuilder.add(toDateTextField[i]).rcw(9, 4, 3);
		paneBuilder.add(datePickerFrom).rcw(9, 1, 2);
		paneBuilder.add(datePickerTo).rcw(9, 4, 3);

		paneBuilder.padding(new EmptyBorder(5, 5, 5, 5));
		
		return paneBuilder.build();
	}
	
	

	
	
	private void layoutForm() {

		FormBuilder builder = FormBuilder.create()
				.columns("right:pref, 5dlu,[20dlu, pref],5dlu,[20dlu, pref],5dlu, [20dlu, pref]")
				.rows("p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p, $lg, p");
				

		builder.addLabel(getMessage(I18N + ".label.rulesetName")).rcw(1, 1, 7);
		builder.add(regelsetNameTextField).rcw(3, 1, 3);
		
		//builder.addLabel(getMessage(I18N + ".label.targetDirectory")).rcw(5, 1, 7);
		//builder.add(targetDirectoryTextField).rcw(7, 1, 3);
		//builder.add(fileChooseButton).rcw(7, 5, 1);
		targetDirectoryList = getDirectories("c:\\daten???????????", true);
		targetDirectoryComboBox = new JComboBox<String>(targetDirectoryList);
		builder.add(targetDirectoryComboBox).rcw(7, 1, 1);
		
		builder.addLabel(getMessage(I18N + ".label.filenameConfigure")).rcw(9, 1, 7);
		builder.add(dateinamenKonfigurationTextField).rcw(11, 1, 3);

		builder.addLabel(getMessage(I18N + ".label.status")).rcw(13, 1, 3);
		builder.add(statusCheckBox).rcw(15, 1, 3);

		// ----------------------
		// mit Builder "innere" verschachtelte JPane f�r Regel_<x>
		//
		//
		// MUSS in END-Version aus Model "gezogen" werden
		//
		for (int i = 0; i < 4; i++) {
			/*
			 *  Besteht jeweils nur aus einem Panel, das wiederum aus mehreren Panels besteht
			 */
			/***

			FormBuilder paneBuilder = FormBuilder.create()
					.columns("right:pref, 5dlu,[20dlu, pref],5dlu,[20dlu, pref],5dlu, [20dlu, pref]")
					.rows("p, $lg, p, $lg, p, $lg, p, $lg, p , $lg, p , $lg, p");

			paneBuilder.addLabel(getMessage(I18N + ".label.sortrule")).rcw(1, 1, 7);

			contextComboBox = new JComboBox<String>(contextComboBoxList);
			paneBuilder.add(contextComboBox).rcw(3, 1, 1);

			paneBuilder.add(attributeComboBox[i]).rcw(3, 4, 3);

			paneBuilder.addLabel(getMessage(I18N + ".label.rule.dynamic")).rcw(5, 1, 7);
			paneBuilder.addLabel(getMessage(I18N + ".label.rule.from")).rcw(7, 1, 7);
			paneBuilder.addLabel(getMessage(I18N + ".label.rule.to")).rcw(7, 4, 3);

			// JFrame frame=new JFrame("date display");
			JDatePickerImpl datePickerFrom;
			JDatePickerImpl datePickerTo;
			UtilDateModel model = new UtilDateModel();

			model.setDate(2016, 11, 16);
			model.setSelected(true);
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			datePickerFrom = new JDatePickerImpl(datePanel, null);
			datePickerTo = new JDatePickerImpl(datePanel, null);

			// paneBuilder.add(fromDateTextField[i]).rcw(9, 1, 2);
			// paneBuilder.add(toDateTextField[i]).rcw(9, 4, 3);
			paneBuilder.add(datePickerFrom).rcw(9, 1, 2);
			paneBuilder.add(datePickerTo).rcw(9, 4, 3);

			paneBuilder.padding(new EmptyBorder(5, 5, 5, 5));

			rulePanel[i] = paneBuilder.build();
			tabbedPane.addTab("Test_" + i, rulePanel[i]);
			
			**/
			
			tabbedPane.addTab("Test_" + i, new RulePanel());
			
			
		}

		builder.addSeparator(null).rcw(23, 1, 7);
		builder.add(tabbedPane).rcw(25, 1, 1);
		builder.addSeparator(null).rcw(31, 1, 7);

		builder.add(addButton).rcw(33, 1, 1);
		builder.add(deleteButton).rcw(33, 3, 1);
		builder.add(saveButton).rcw(33, 5, 1);
		builder.add(cancelButton).rcw(33, 7, 1);

		builder.padding(new EmptyBorder(5, 5, 5, 5));
		JPanel build = builder.build();

		getFrame().add(build, BorderLayout.CENTER);

		setDimension(800, 600);
	}

	@Override
	public void configureBindings() {
	
		regelsetNameTextField.setText(getModel().getRulesetName());
		dateinamenKonfigurationTextField.setText(getModel().getFilenameKonfiguration());
		statusCheckBox.setSelected(getModel().isRuleActiv());
		targetDirectoryList = getDirectories("c:\\daten\\inbox????", true);
		targetDirectoryComboBox.setSelectedItem("c:\\daten\\temp\\divers");
		
		getModel().addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName() == "rulesetName") {
					regelsetNameTextField.setText(getModel().getTargetDirectory());
				} else if (evt.getPropertyName() == "targetDirectory") {
					targetDirectoryTextField.setText(getModel().getTargetDirectory());
				} else if (evt.getPropertyName() == "filenameKonfiguration") {
					dateinamenKonfigurationTextField.setText(getModel().getRulesetName());
				}
			}
		});
		

		statusCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getModel().setRuleActiv(statusCheckBox.isSelected());

			}
		});
		
		targetDirectoryComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: bei jeder Action? eher unsch�n?
				// 		sollte doch nur bei �NDERUND des selektierten Items
				String selectedDirectory = (String)targetDirectoryComboBox.getSelectedItem();
				System.out.println("TargetCombo" + selectedDirectory);
				getModel().setTargetDirectory(selectedDirectory);

			}
		});
		
	}
	
	private class SaveRulesetAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			
			// TODO: Geh�rt das hier hinein?
			getModel().setRulesetName(regelsetNameTextField.getText());
			getModel().setFilenameKonfiguration(dateinamenKonfigurationTextField.getText());
		
			getHandler().performOperation(RegelsetSaveOperation.class);
			getHandler().performOperation(CloseViewOperation.class);
		}
	}
	
	private class CloseAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {

			getHandler().performOperation(CloseViewOperation.class);
		}
	}

	private class OpenDirectoryChooser extends AbstractAction {

		public void actionPerformed(ActionEvent e) {

			getHandler().performOperationWithArgs(ChooseDirectoryPathViewOperation.class, true);
		}
	}

	
	private class RulePanel extends JPanel {
		
		// besteht aus 3 Teilen
		// Context Combo
		// evtl. ContextAttributeComb
		// Comparision
		// 			from / To - Object => Model.....
		
		
		
		
		//
		//  View-Element erst beim Speichern in Model
		// (=> Spielerein bleiben erhalten)
		// (tun sie sonst aber vielleicht auch)
		
		RulePanel() {
			super();			// wir haben das "haupt-Rule- Panel" => jetzt m�ssen noch 3 sub-Panels..
		
			FormBuilder paneBuilder = FormBuilder.create()
					.columns("right:pref, 5dlu, [20dlu, pref], 5dlu, [20dlu, pref], 5dlu, [20dlu, pref], 5dlu, [20dlu, pref], 5dlu, [20dlu, pref]")
					.rows("p, $lg, p, $lg, p, $lg, p, $lg, p , $lg, p , $lg, p");

			paneBuilder.addLabel(getMessage(I18N + ".label.sortrule") + "XXXXXXX").rcw(1, 1, 7);

			contextComboBox = new JComboBox<String>(contextComboBoxList);
			paneBuilder.add(contextComboBox).rcw(3, 1, 1);
			
			attributeComboBox = new JComboBox<String>(attributeComboBoxList);
			paneBuilder.add(attributeComboBox).rcw(3, 4, 4);

			paneBuilder.addLabel(getMessage(I18N + ".label.rule.dynamic")).rcw(5, 1, 7);
			paneBuilder.addLabel(getMessage(I18N + ".label.rule.from")).rcw(7, 1, 7);
			paneBuilder.addLabel(getMessage(I18N + ".label.rule.to")).rcw(7, 4, 2);

			// JFrame frame=new JFrame("date display");
			JDatePickerImpl datePickerFrom;
			JDatePickerImpl datePickerTo;
			UtilDateModel modelDateFrom = new UtilDateModel();
			UtilDateModel modelDateTo = new UtilDateModel();

			modelDateFrom.setDate(2016, 01, 01);
			modelDateFrom.setSelected(true);
			JDatePanelImpl dateFromPanel = new JDatePanelImpl(modelDateFrom);
			datePickerFrom = new JDatePickerImpl(dateFromPanel, null);
			
			modelDateTo.setDate(2016, 11, 30);
			modelDateTo.setSelected(true);
			JDatePanelImpl dateToPanel = new JDatePanelImpl(modelDateTo);
			datePickerFrom = new JDatePickerImpl(dateToPanel, null);
			datePickerTo = new JDatePickerImpl(dateToPanel, null);
			
			// paneBuilder.add(fromDateTextField[i]).rcw(9, 1, 2);
			// paneBuilder.add(toDateTextField[i]).rcw(9, 4, 3);
			paneBuilder.add(datePickerFrom).rcw(9, 1, 2);
			paneBuilder.add(datePickerTo).rcw(9, 4, 6);

			paneBuilder.padding(new EmptyBorder(5, 5, 5, 5));

			this.add(paneBuilder.build());
		}
		
		
		
	}
}
