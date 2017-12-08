/******************************************************************************
 * Plug-in AccountInfoCockpit for iDempiere ERP & CRM Smart Business Solution *
 * Copyright (C) 2017  Patric Maßing (Hans Auler GmbH)                        *
 *                                                                            *
 * This plug-in is free software; you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation; either version 2 of the License, or          *
 * (at your option) any later version.                                        *
 *                                                                            *
 * This plug-in is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU General Public License for more details.                               *
 *                                                                            *
 * You should have received a copy of the GNU General Public License along    *
 * with this plug-in; If not, see <http://www.gnu.org/licenses/>.             *
 *****************************************************************************/
 
 /**
  * @author Patric Maßing (Hans Auler GmbH)
  * 2017
 */


package de.aulerlichtkabel.acctinfocpt.froms;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.ListCell;
import org.adempiere.webui.component.ListHead;
import org.adempiere.webui.component.ListHeader;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Tab;
import org.adempiere.webui.component.Tabbox;
import org.adempiere.webui.component.Tabpanel;
import org.adempiere.webui.component.Tabpanels;
import org.adempiere.webui.component.Tabs;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MElementValue;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrg;
import org.compiere.model.MTable;
import org.compiere.model.MTreeNode;
import org.compiere.model.Query;
import org.compiere.report.MReportTree;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Center;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Vbox;



import org.zkoss.zul.Label;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

import de.aulerlichtkabel.acctinfocpt.data.PAT_Data;
import de.aulerlichtkabel.acctinfocpt.data.PAT_Sqls;

public class PAT_WAcctInfCptForm
		implements IFormController, EventListener<Event>, WTableModelListener, ValueChangeListener {

	private CustomForm mForm = new CustomForm();

	public int m_WindowNo = 0;

	private Tabbox tabBox = new Tabbox();
	private Tabs tabs = new Tabs();

	private Tab tabParameter = new Tab();
	private Tab tabResult = new Tab();

	private Tabpanels tabpanels = new Tabpanels();
	private Tabpanel tabpanelResult = new Tabpanel();
	private Tabpanel tabpanelParameter = new Tabpanel();

	private Toolbar toolBar = new Toolbar();
	private ToolBarButton tButtonAccountCourse = new ToolBarButton();
	private ToolBarButton tButtonAccountOverView = new ToolBarButton();
	private ToolBarButton tButtonAccountsOverView = new ToolBarButton();
	private ToolBarButton tButtonSummaryDocument = new ToolBarButton();
	private ToolBarButton tButtonSummary = new ToolBarButton();
	private ToolBarButton tBalanceOfAccountsList = new ToolBarButton();
	private ToolBarButton tButtonTreeSummary = new ToolBarButton();
	private ToolBarButton tButtonExportHtml = new ToolBarButton();
	private ToolBarButton tButtonExportText = new ToolBarButton();

	Vbox vBox = new Vbox();

	Listbox listboxResult = new Listbox();
	Borderlayout layout = new Borderlayout();

	Frozen frozen = new Frozen();

	private Panel parameterPanel = new Panel();
	private Grid parameterLayout = GridFactory.newGridLayout();

	Rows rows = parameterLayout.newRows();
	Row rowCheckbox = rows.newRow();
	Row rowClientAndOrg = rows.newRow();
	Row rowAcctSchema = rows.newRow();	
	Row rowAdjustmentPeriod = rows.newRow();
	Row rowAccountDocument = rows.newRow();
	Row rowDate = rows.newRow();
	Row rowAcctValue = rows.newRow();
	Row rowDimension = rows.newRow();
	Row rowDimension2 = rows.newRow();

	private Grid southLayout = GridFactory.newGridLayout();
	Rows sumRows = southLayout.newRows();
	Row sumRowOne = sumRows.newRow();

	private Label labelClient = new Label();
	private Label labelOrganisation = new Label();
	private Textbox textboxClient = new Textbox();
	private Listbox listboxOrganisation = ListboxFactory.newDropdownListbox();

	private Label labelAcctSchema = new Label();
	private Listbox listboxSelAcctSchema = new Listbox();

	private Label labelDateFrom = new Label();
	private Datebox dateboxDateFrom = new Datebox();

	private Label labelDateTo = new Label();
	private Datebox dateboxDateTo = new Datebox();
	
	private Label labelWithAdjustmentPeriod = new Label();
	private Listbox listboxWithAdjustmentPeriod= ListboxFactory.newDropdownListbox();

	private Label labelValueFrom = new Label();
	private WSearchEditor searchEditorValueFrom;
	private Label labelValueTo = new Label();
	private WSearchEditor searchEditorValueTo;

	private Checkbox checkboxOnYear = new Checkbox();
	private Checkbox checkboxOnMonth = new Checkbox();
	private Checkbox checkboxOnDay = new Checkbox();

	private Label labelSummaryAccountDocument = new Label();
	private Textbox textboxSummaryAccountDocument = new Textbox();
	private Listbox listboxSummaryTable = ListboxFactory.newDropdownListbox();
	private Button buttonSummaryAccountDocument = new Button();

	private Label labelProduct = new Label();
	private WSearchEditor searchEditorProduct;

	private Label labelBPartner = new Label();
	private WSearchEditor searchEditorBPartner;

	private Label labelSalesRegion = new Label();
	private WSearchEditor searchEditorSalesRegion;

	private Label labelProject = new Label();
	private WSearchEditor searchEditorProject;

	private Panel southPanel = new Panel();;
	private ConfirmPanel buttonPanel = new ConfirmPanel(false, true, false, false, false, true, false);
	private Grid buttonLayout = GridFactory.newGridLayout();

	private Button bRefresh = buttonPanel.getButton(ConfirmPanel.A_REFRESH);
	private Button bOkay = buttonPanel.getButton(ConfirmPanel.A_OK);
	private Button bZoom = buttonPanel.getButton(ConfirmPanel.A_ZOOM);

	private Label labelDebit = new Label();
	private Label labelCredit = new Label();
	private Label labelBalance = new Label();

	private PAT_Sqls sqls = new PAT_Sqls();
	private PAT_Data p_data = new PAT_Data();

	// Format
	DecimalFormat numberFormat = DisplayType.getNumberFormat(DisplayType.Amount);

	private boolean isAccountCourse = false;
	private boolean isAccountsOverView = false;
	private boolean isAccountOverView = false;
	private boolean isSummaryDocument = false;
	private boolean isSummary = false;
	private boolean isBalanceOfAccountsList = false;
	private boolean isTreeSummary = false;

	private String ISNUMERIC = "<N>";
	
	Map<String, Object> params = new HashMap<String, Object>();
	

	/** Logger */
	public static CLogger log = CLogger.getCLogger(PAT_WAcctInfCptForm.class);

	public PAT_WAcctInfCptForm() {

		init();
		createTabs();
		createToolbar();
		createParameterPanel();
		createResultPanel();
		createSouthPanel();

	}

	private void init() {
		
		textboxClient.setReadonly(true);

		MLookup acctVaL = MLookupFactory.get(Env.getCtx(), mForm.getWindowNo(), 0, 207736, DisplayType.Search);
		searchEditorValueFrom = new WSearchEditor("C_ElementValue_ID", false, false, true, acctVaL);
		searchEditorValueTo = new WSearchEditor("C_ElementValue_ID", false, false, true, acctVaL);

		labelProduct.setValue(Msg.translate(Env.getCtx(), "Product"));
		MLookup productVaL = MLookupFactory.get(Env.getCtx(), mForm.getWindowNo(), 0, 1402, DisplayType.Search);
		searchEditorProduct = new WSearchEditor("M_Product_ID", false, false, true, productVaL);

		labelBPartner.setValue(Msg.translate(Env.getCtx(), "Businesspartner"));
		MLookup bPartnerVaL = MLookupFactory.get(Env.getCtx(), mForm.getWindowNo(), 0, 2893, DisplayType.Search);
		searchEditorBPartner = new WSearchEditor("C_BPartner_ID", false, false, true, bPartnerVaL);

		labelSalesRegion.setValue(Msg.translate(Env.getCtx(), "SalesRegion"));
		MLookup salesRegionVaL = MLookupFactory.get(Env.getCtx(), mForm.getWindowNo(), 0, 1823, DisplayType.Search);
		searchEditorSalesRegion = new WSearchEditor("C_SalesRegion_ID", false, false, true, salesRegionVaL);

		labelProject.setValue(Msg.translate(Env.getCtx(), "Project"));
		MLookup projectVaL = MLookupFactory.get(Env.getCtx(), mForm.getWindowNo(), 0, 1349, DisplayType.Search);
		searchEditorProject = new WSearchEditor("C_Project_ID", false, false, true, projectVaL);
		
		listboxResult.setSizedByContent(true);
		
		LayoutUtils.addSclass("AccountInfoCockpit", mForm);

		mForm.appendChild(layout);

	}

	private void createTabs() {

		tabParameter.addEventListener(Events.ON_SELECT, this);
		tabParameter.setLabel(Msg.translate(Env.getCtx(), "ViewerQuery").replaceAll("[&]", ""));

		tabResult.addEventListener(Events.ON_SELECT, this);
		tabResult.setLabel(Msg.translate(Env.getCtx(), "ViewerResult").replaceAll("[&]", ""));

		tabs.appendChild(tabParameter);
		tabs.appendChild(tabResult);

		tabpanels.appendChild(tabpanelParameter);
		tabpanels.appendChild(tabpanelResult);
		tabpanels.setVflex("1");

		tabpanelResult.appendChild(listboxResult);

		tabBox.appendChild(tabs);
		tabBox.appendChild(tabpanels);
		tabBox.appendChild(createToolbar());
		tabBox.setVflex("1");

		Center center = new Center();
		center.setAutoscroll(true);
		center.setHflex("1");
		center.setVflex("1");
		center.setParent(layout);

		tabBox.setParent(center);

	}
	
	private Image getImageFromLocalSource(String path){
		
		String resourceName = path.substring(path.lastIndexOf("/"));
		
		Image image = null;
		
		try
		{

			byte[] buffer = new byte[1024*8];
			int length = -1;

		    URLConnection urlConnection = this.getClass().getClassLoader().getResource(path).openConnection();
			urlConnection.setUseCaches(false);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			while ((length = urlConnection.getInputStream().read(buffer)) != -1)
				outputStream.write(buffer, 0, length);
			
			outputStream.close();
			
			image =  new AImage(resourceName, outputStream.toByteArray());
			
		}
		catch (Exception e)
		{
			if (log.isLoggable(Level.CONFIG)) log.config (e.toString());
		}
		
		
		return image;
		
	}		

	

	private Toolbar createToolbar() {

		tButtonAccountCourse.setImageContent(getImageFromLocalSource("images/AccountCourse.png"));
		tButtonAccountCourse.setTooltip("AccountCourse");
		tButtonAccountCourse.addEventListener(Events.ON_CLICK, this);

		tButtonAccountsOverView.setImageContent(getImageFromLocalSource("images/AccountsOverview.png"));
		tButtonAccountsOverView.addEventListener(Events.ON_CLICK, this);

		tButtonAccountOverView.setImageContent(getImageFromLocalSource("images/AccountOverview.png"));
		tButtonAccountOverView.addEventListener(Events.ON_CLICK, this);

		tButtonSummaryDocument.setImageContent(getImageFromLocalSource("images/DocumentSummary.png"));
		tButtonSummaryDocument.addEventListener(Events.ON_CLICK, this);

		tButtonSummary.setImageContent(getImageFromLocalSource("images/AccountSummary.png"));
		tButtonSummary.addEventListener(Events.ON_CLICK, this);
		
		tBalanceOfAccountsList.setImageContent(getImageFromLocalSource("images/BalanceOfAccountsList.png"));
		tBalanceOfAccountsList.addEventListener(Events.ON_CLICK, this);
		
		tButtonTreeSummary.setImageContent(getImageFromLocalSource("images/AccountTreeSummary.png"));
		tButtonTreeSummary.addEventListener(Events.ON_CLICK, this);
		
		tButtonExportHtml.setImageContent(getImageFromLocalSource("images/ExportResultAsHtml.png"));
		tButtonExportHtml.addEventListener(Events.ON_CLICK, this);
		
		tButtonExportText.setImageContent(getImageFromLocalSource("images/ExportResultAsText.png"));
		tButtonExportText.addEventListener(Events.ON_CLICK, this);
		
		
		toolBar.appendChild(tButtonAccountCourse);
		toolBar.appendChild(tButtonAccountsOverView);
		toolBar.appendChild(tButtonAccountOverView);
		toolBar.appendChild(tButtonSummaryDocument);
		toolBar.appendChild(tButtonSummary);
		toolBar.appendChild(tBalanceOfAccountsList);
		toolBar.appendChild(tButtonTreeSummary);
		toolBar.appendChild(tButtonExportHtml);
		toolBar.appendChild(tButtonExportText);
		
		return toolBar;
	}

	private void createParameterPanel() {

		parameterPanel.appendChild(parameterLayout);

		labelClient.setValue(Msg.translate(Env.getCtx(), "Client"));
		textboxClient.setValue(p_data.getClientname());
		textboxClient.setWidth("100px");
		// textboxClient.setStyle("text-align: right");

		labelOrganisation.setValue(Msg.translate(Env.getCtx(), "Organisation"));

		listboxOrganisation.appendItem("*",null);
		for (MOrg org : p_data.getOrganisations())
			listboxOrganisation.appendItem(org.getName(), org.getAD_Org_ID());
		listboxOrganisation.setWidth("100px");
		// listboxOrganisation.setStyle("text-align: right");

		labelAcctSchema.setValue(Msg.translate(Env.getCtx(), "C_AcctSchema_ID"));
		listboxSelAcctSchema.setMold("select");
		listboxSelAcctSchema.setRows(1);
		listboxSelAcctSchema.setWidth("200px");
		listboxSelAcctSchema.addEventListener(Events.ON_SELECT, this);
		p_data.getAcctSchema(listboxSelAcctSchema);
		listboxSelAcctSchema.setSelectedIndex(0);

		labelDateFrom.setValue(Msg.translate(Env.getCtx(), "DateFrom"));
		dateboxDateFrom.addEventListener(Events.ON_CHANGE, this);

		labelDateTo.setValue(Msg.translate(Env.getCtx(), "DateTo"));
		
		labelWithAdjustmentPeriod.setValue(Msg.translate(Env.getCtx(), "Period Type"));
		listboxWithAdjustmentPeriod.appendItem(Msg.translate(Env.getCtx(),"*"), "%");
		listboxWithAdjustmentPeriod.appendItem(Msg.translate(Env.getCtx(),"Standard Period"), "S");
		listboxWithAdjustmentPeriod.appendItem(Msg.translate(Env.getCtx(),"Adjustment Period"), "A");
		
		
		labelValueFrom.setValue(Msg.translate(Env.getCtx(), "ValueFrom"));
		searchEditorValueFrom.addValueChangeListener(this);

		labelValueTo.setValue(Msg.translate(Env.getCtx(), "ValueTo"));
		searchEditorValueTo.addValueChangeListener(this);

		checkboxOnYear.setLabel(Msg.translate(Env.getCtx(), "OnYear"));
		checkboxOnYear.addActionListener(this);

		checkboxOnMonth.setLabel(Msg.translate(Env.getCtx(), "OnMonth"));
		checkboxOnMonth.addActionListener(this);

		checkboxOnDay.setLabel(Msg.translate(Env.getCtx(), "OnDay"));
		checkboxOnDay.addActionListener(this);

		listboxSummaryTable.setWidth("100px");
		labelSummaryAccountDocument.setValue(Msg.translate(Env.getCtx(), "DocumentNo").replaceAll("[&]", ""));
		labelSummaryAccountDocument.setStyle("font-weight: bold");

		buttonSummaryAccountDocument.setWidth("25px");
		buttonSummaryAccountDocument.addActionListener(this);
		buttonSummaryAccountDocument.setImage(ThemeManager.getThemeResource("images/Find16.png"));

		for (ValueNamePair table : p_data.getTables()) {

			String tableNameTRL = Msg.translate(Env.getCtx(), table.getName() + "_ID");
			listboxSummaryTable.appendItem(tableNameTRL, table.getID());

		}

		rowCheckbox.appendChild(checkboxOnYear);
		rowCheckbox.appendChild(checkboxOnMonth);
		rowCheckbox.appendChild(checkboxOnDay);
		rowCheckbox.appendChild(new Space());

		rowClientAndOrg.appendChild(labelClient);
		rowClientAndOrg.appendChild(textboxClient);
		rowClientAndOrg.appendChild(labelOrganisation);
		rowClientAndOrg.appendChild(listboxOrganisation);
		rowClientAndOrg.appendChild(new Space());
		
		rowAcctSchema.appendChild(labelAcctSchema);
		rowAcctSchema.appendChild(listboxSelAcctSchema);
		rowAcctSchema.appendChild(new Space());
		rowAcctSchema.appendChild(new Space());

		rowAdjustmentPeriod.appendChild(labelWithAdjustmentPeriod);
		rowAdjustmentPeriod.appendChild(listboxWithAdjustmentPeriod);
		
		rowAccountDocument.appendChild(listboxSummaryTable);
		rowAccountDocument.appendChild(labelSummaryAccountDocument);
		rowAccountDocument.appendChild(buttonSummaryAccountDocument);
		rowAccountDocument.appendChild(new Space());

		rowDate.appendChild(labelDateFrom);
		rowDate.appendChild(dateboxDateFrom);
		rowDate.appendChild(labelDateTo);
		rowDate.appendChild(dateboxDateTo);

		rowAcctValue.appendChild(labelValueFrom);
		rowAcctValue.appendChild(searchEditorValueFrom.getComponent());
		
		// ==> not needed at the moment
//		rowAcctValue.appendChild(labelValueTo);
//		rowAcctValue.appendChild(searchEditorValueTo.getComponent());
		rowAccountDocument.appendChild(new Space());
		rowAccountDocument.appendChild(new Space());
		// <== not needed at the moment

		rowDimension.appendChild(labelProduct);
		rowDimension.appendChild(searchEditorProduct.getComponent());
		rowDimension.appendChild(labelBPartner);
		rowDimension.appendChild(searchEditorBPartner.getComponent());
		
		rowDimension2.appendChild(labelSalesRegion);
		rowDimension2.appendChild(searchEditorSalesRegion.getComponent());
		rowDimension2.appendChild(labelProject);
		rowDimension2.appendChild(searchEditorProject.getComponent());

		tabpanelParameter.appendChild(parameterPanel);

		rowCheckbox.setVisible(false);
		rowClientAndOrg.setVisible(false);
		rowAcctSchema.setVisible(false);
		rowAdjustmentPeriod.setVisible(false);
		rowAccountDocument.setVisible(false);
		rowAcctValue.setVisible(false);
		rowDate.setVisible(false);
		rowDimension.setVisible(false);
		rowDimension2.setVisible(false);

	}

	private void clearParameters() {

		dateboxDateFrom.setValue(null);
		dateboxDateTo.setValue(null);
		listboxWithAdjustmentPeriod.setSelectedIndex(0);
		searchEditorValueFrom.setValue(null);
		searchEditorValueTo.setValue(null);
		checkboxOnYear.setChecked(false);
		checkboxOnMonth.setChecked(false);
		checkboxOnDay.setChecked(false);
		textboxSummaryAccountDocument.setValue(null);

		searchEditorBPartner.setValue(null);
		searchEditorProduct.setValue(null);
		searchEditorProject.setValue(null);
		searchEditorSalesRegion.setValue(null);

		labelDebit.setValue(Msg.translate(Env.getCtx(), "Debit"));
		labelCredit.setValue(Msg.translate(Env.getCtx(), "Credit"));
		labelBalance.setValue(Msg.translate(Env.getCtx(), "Balance"));
		labelBalance.setStyle("color:black;font-weight: bold");

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", searchEditorValueFrom.getValue());
		params.put("valueTo", searchEditorValueTo.getValue());
		params.put("dateFrom", dateboxDateFrom.getValue());
		params.put("dateTo", dateboxDateTo.getValue());
		params.put("product_id", searchEditorProduct.getValue());
		params.put("bpartner_id", searchEditorBPartner.getValue());
		params.put("salesregion_id", searchEditorSalesRegion.getValue());
		params.put("project_id", searchEditorProject.getValue());
		params.put("doctable", listboxSummaryTable.getSelectedItem().getLabel());
		params.put("docno", labelSummaryAccountDocument.getValue());
		params.put("summaryDocument", isSummaryDocument);
		params.put("treeSummary", "");
		params.put("treeValue", null);
		
		bZoom.setVisible(false);


	}

	private void createSouthPanel() {

		buttonPanel.appendChild(buttonLayout);

		labelDebit.setValue(Msg.translate(Env.getCtx(), "Debit"));
		labelDebit.setStyle("font-weight: bold");
		labelCredit.setValue(Msg.translate(Env.getCtx(), "Credit"));
		labelCredit.setStyle("font-weight: bold");
		labelBalance.setValue(Msg.translate(Env.getCtx(), "Balance"));
		labelBalance.setStyle("font-weight: bold");

		Space spaceLeft = new Space();
		spaceLeft.setWidth("10%");

		southPanel.appendChild(spaceLeft);

		southPanel.appendChild(labelDebit);
		southPanel.appendChild(new Space());
		southPanel.appendChild(new Space());
		southPanel.appendChild(labelCredit);
		southPanel.appendChild(new Space());
		southPanel.appendChild(new Space());
		southPanel.appendChild(labelBalance);
		southPanel.appendChild(buttonPanel);

		South south = new South();
		south.setStyle("border: none");
		layout.appendChild(south);
		south.appendChild(southPanel);

		bRefresh.addActionListener(this);
		bOkay.addActionListener(this);
		bZoom.addActionListener(this);
		bZoom.setVisible(false);

	}

	private void createResultPanel() {

		listboxResult.setAutopaging(true);
		// mold: allowed: [paging, select, default]
		listboxResult.setMold("paging");
		listboxResult.setSizedByContent(true);
		listboxResult.setVflex("1");
		listboxResult.setHflex("1");

		listboxResult.appendChild(createHeader(headColumns()));

	}

	private List<String> headColumns() {

		List<String> headColumn = new ArrayList<String>();
		headColumn.add(Msg.translate(Env.getCtx(), "Client"));
		headColumn.add(Msg.translate(Env.getCtx(), "Organisation"));
		headColumn.add(Msg.translate(Env.getCtx(), "Account"));
		headColumn.add(Msg.translate(Env.getCtx(), "Name"));
		// In this case balance_carried_forward, debit, credit, current_balance, ending_balance
		if(isBalanceOfAccountsList){
			headColumn.add(Msg.translate(Env.getCtx(), "BalanceCarriedForward"));
			headColumn.add(Msg.translate(Env.getCtx(), "Debit"));
			headColumn.add(Msg.translate(Env.getCtx(), "Credit"));
			headColumn.add(Msg.translate(Env.getCtx(), "Balance"));
			headColumn.add(Msg.translate(Env.getCtx(), "CurrentBalance"));
			headColumn.add(Msg.translate(Env.getCtx(), "EndingBalance"));
			headColumn.add(Msg.translate(Env.getCtx(), ""));
			headColumn.add(Msg.translate(Env.getCtx(), ""));
			headColumn.add(Msg.translate(Env.getCtx(), ""));
		}else{
			headColumn.add(Msg.translate(Env.getCtx(), "DateAcct"));
			headColumn.add(Msg.translate(Env.getCtx(), "Debit"));
			headColumn.add(Msg.translate(Env.getCtx(), "Credit"));
			headColumn.add(Msg.translate(Env.getCtx(), "Balance"));
			headColumn.add(Msg.translate(Env.getCtx(), new String("Product").replaceAll("[&]", "")));			
			headColumn.add(Msg.translate(Env.getCtx(), new String("BPartner").replaceAll("[&]", "")));
			headColumn.add(Msg.translate(Env.getCtx(), "SalesRegion"));
			headColumn.add(Msg.translate(Env.getCtx(), "Project"));
			headColumn.add(Msg.translate(Env.getCtx(), "Table"));
		}

		return headColumn;

	}

	private void createEmptyColumns(int columncount) {

		for(Component listhead : listboxResult.getHeads())
			if(listhead instanceof ListHead)
				for(int column=1;column<=columncount;column++)					
					listhead.appendChild(new ListHeader());

	}
	
	
	private int getHeadColumnsCount(){
		
		int count=0;
		
		for(Component listhead : listboxResult.getHeads())
			if(listhead instanceof ListHead)
				for (Component listheader : listhead.getChildren())
					if(listheader instanceof ListHeader)
						count++;
		
		return count--;
		
	}

	private List<String> getHeadColumns(){
		
		List<String> headColumns = new ArrayList<String>();
		
		for(Component listhead : listboxResult.getHeads())
			if(listhead instanceof ListHead)
				for (Component listheader : listhead.getChildren()){
					if(listheader instanceof ListHeader){
						if(((ListHeader)listheader).getLabel().equals(Msg.translate(Env.getCtx(), "BalanceCarriedForward"))
									||((ListHeader)listheader).getLabel().equals(Msg.translate(Env.getCtx(), "Debit"))
									||((ListHeader)listheader).getLabel().equals(Msg.translate(Env.getCtx(), "Credit"))
									||((ListHeader)listheader).getLabel().equals(Msg.translate(Env.getCtx(), "Balance"))
									||((ListHeader)listheader).getLabel().equals(Msg.translate(Env.getCtx(), "CurrentBalance"))
									||((ListHeader)listheader).getLabel().equals(Msg.translate(Env.getCtx(), "EndingBalance")))
							headColumns.add(ISNUMERIC+((ListHeader)listheader).getLabel());
						else
							headColumns.add(((ListHeader)listheader).getLabel());							
					}
				}
		
		return headColumns;
		
	}
	
	private List<List<Object>> getRows(){
		
		List<List<Object>> rows = new ArrayList<List<Object>>();			
		
		for(Component litem : listboxResult.getChildren()){
			
			List<Object> column = new ArrayList<Object>();
			
			if(litem instanceof ListItem)
				for(Component lcell : ((ListItem)litem).getChildren())
					if(lcell instanceof ListCell)
							column.add(((ListCell)lcell).getValue());
				
			rows.add(column);
			
		}

		
		return rows;
		
	}
	
	private void clearHeadColumns(){

		for(Component listhead : listboxResult.getHeads())
			if(listhead instanceof ListHead)
				for (Component listheader : ((Component)listhead).getChildren())				
					if(listheader instanceof ListHeader)
						((ListHeader)listheader).setLabel("");

	}
	
	private void clearList() {
		
		listboxResult.removeAllItems();
		listboxResult.removeChild(frozen);
		listboxResult.setSizedByContent(true);
		
		for(Component listhead : listboxResult.getHeads())
			if(listhead instanceof ListHead)
				listboxResult.removeChild((ListHead)listhead);
		
		listboxResult.appendChild(createHeader(headColumns()));

	}

	private ListHead createHeader(List<String> columns) {

		ListHead listhead = new ListHead();
		listhead.setSizable(true);

		for (String column : columns) {

			ListHeader lstclolumn = new ListHeader();
			
			lstclolumn.setLabel(Msg.getMsg(Env.getCtx(), column));
			if (column.equals("DateAcct") || column.equals("Debit") || column.equals("Credit")
					|| column.equals("Balance"))
				lstclolumn.setAlign("right");
			listhead.appendChild(lstclolumn);

		}

		return listhead;

	}
	

	private void setLabelOfColumn(int pos, String label){
		
		int p = 0;		

		for(Component listhead : listboxResult.getHeads())
			if(listhead instanceof ListHead)
				for (Component listheader : ((ListHead)listhead).getChildren()){
					if(listheader instanceof ListHeader){
						
						if(p==pos){
							
							((ListHeader)listheader).setLabel(Msg.getMsg(Env.getCtx(), label));

						}
						
						if (p!=0){
							
							((ListHeader)listheader).setAlign("right");
							((ListHeader)listheader).setWidth("100px");
						}
					}
					
					p++;
				}		

	}
	
	
	private void refreshHeader(){
		
		int p = 0;
		
		for(Component listhead : listboxResult.getHeads())
			if(listhead instanceof ListHead)
				for (Component listheader : ((ListHead)listhead).getChildren()){
					
					if(listheader instanceof ListHeader)
						((ListHeader)listheader).setLabel(headColumns().get(p));
					
					p++;

				}

	}
	

	private void addRecords(List<List<Object>> objs) {

		clearList();
		for (List<Object> obj : objs) {
			listboxResult.appendChild(addRecord(obj));

		}

	}

	private ListItem addRecord(List<Object> row) {

		ListItem item = new ListItem();
		int c = 1;

		for (int pos = 0; pos < row.size(); pos ++) {

			Object o = row.get(pos);
			
			ListCell cell = new ListCell();
			
			if (o instanceof BigDecimal)			
				cell.setLabel(numberFormat.format(o));
			else if (o instanceof Timestamp)
				cell.setLabel(Env.getLanguage(Env.getCtx()).getDateFormat().format(o));
			else
				cell.setLabel(o.toString());
			
			cell.setValue(o);
			
			// account value
			if (c == 3)
				cell.setStyle("font-weight: bold");

			// dateacct
			if (c == 5)
				cell.setStyle("text-align: right");

			// debit, credit, balance
			if (c >= 6 && c <= 8) {

				try {

					if(!o.toString().equals("")){
						
						if (numberFormat.parse(o.toString()).floatValue() < 0)
							cell.setStyle("color:red;font-weight: bold;text-align: right");
						else
							cell.setStyle("font-weight: bold;text-align: right");
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				
			}
			
			if(isBalanceOfAccountsList){
				
				// In this case balance_carried_forward, debit, credit, current_balance, ending_balance
				if (c >= 5 && c <= 9) {
					
					if(!o.toString().equals("")){

						if((cell.getLabel() != null) && (c==5 || c==9))
							cell.setLabel(o.toString());

						try {

							if (numberFormat.parse(o.toString()).floatValue() < 0)
								cell.setStyle("color:red;font-weight: bold;text-align: right");
							else
								cell.setStyle("font-weight: bold;text-align: right");

						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
				}
				
			}			
			
			item.appendChild(cell);
			c++;
		}

		return item;

	}

	@Override
	public ADForm getForm() {
		return mForm;
	}

	@Override
	public void tableChanged(WTableModelEvent event) {

	}

	/**
	 * Dispose
	 */
	public void dispose() {
		SessionManager.getAppDesktop().closeActiveWindow();
	}// dispose

	@Override
	public void onEvent(Event event) throws Exception {
		

		if (event.getTarget() == bOkay)
			dispose();



		
		if (event.getTarget() == dateboxDateFrom) {
			
			dateboxDateTo.setEnabled(true);

			if(dateboxDateFrom.getValue() != null){
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateboxDateFrom.getValue());
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				dateboxDateTo.setValue(cal.getTime());
				
			}
			
			if (checkboxOnMonth.isChecked() && isTreeSummary) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateboxDateFrom.getValue());
				cal.set(Calendar.MONTH, Calendar.DECEMBER);
				cal.set(Calendar.DAY_OF_MONTH,
						cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				dateboxDateTo.setValue(cal.getTime());
				dateboxDateTo.setEnabled(false);
			}

			if (checkboxOnDay.isChecked() && isTreeSummary)
				dateboxDateTo.setEnabled(false);

		}
		
		
		if (event.getTarget() == buttonSummaryAccountDocument) {

			p_data.getDocument(this.m_WindowNo, listboxSummaryTable.getSelectedItem().getLabel(),
					labelSummaryAccountDocument, listboxSummaryTable);
		}

		if (event.getTarget() == tButtonAccountCourse) {
			
			isAccountCourse = true;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = false;
			isTreeSummary = false;
			
			clearParameters();
			clearList();
			refreshHeader();			
			
			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(true);
			rowDate.setVisible(true);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);
			rowAdjustmentPeriod.setVisible(true);
			
			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountCourse").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountCourse").replaceAll("[&]", ""));

		}

		if (event.getTarget() == tButtonAccountsOverView) {
			
			isAccountCourse = false;
			isAccountsOverView = true;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = false;
			isTreeSummary = false;

			clearParameters();
			clearList();
			refreshHeader();
			
			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(false);
			rowDate.setVisible(false);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);
			rowAdjustmentPeriod.setVisible(true);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountsOverView").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountsOverView").replaceAll("[&]", ""));

		}

		if (event.getTarget() == tButtonAccountOverView) {
			
			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = true;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = false;
			isTreeSummary = false;

			clearParameters();
			clearList();
			refreshHeader();
			
			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(true);
			rowDate.setVisible(true);
			rowDimension.setVisible(true);
			rowDimension2.setVisible(true);
			rowAdjustmentPeriod.setVisible(true);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountOverView").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountOverView").replaceAll("[&]", ""));

		}

		if (event.getTarget() == tButtonSummaryDocument) {
			
			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = true;
			isSummary = false;
			isBalanceOfAccountsList = false;
			isTreeSummary = false;

			clearParameters();
			clearList();
			refreshHeader();
			
			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(false);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(true);
			rowAcctValue.setVisible(false);
			rowDate.setVisible(false);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);
			rowAdjustmentPeriod.setVisible(false);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "SummaryDocument").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "SummaryDocument").replaceAll("[&]", ""));
		}

		if (event.getTarget() == tButtonSummary) {
			

			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = true;
			isBalanceOfAccountsList = false;
			isTreeSummary = false;

			clearParameters();
			clearList();
			refreshHeader();
			
			setCheckboxOnYear();

			rowCheckbox.setVisible(true);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(true);
			rowDate.setVisible(false);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);
			rowAdjustmentPeriod.setVisible(true);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "Summary").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "Summary").replaceAll("[&]", ""));

		}
		
		
		if (event.getTarget() == tBalanceOfAccountsList) {

			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = true;
			isTreeSummary = false;

			clearParameters();
			clearList();
			refreshHeader();
			
			setCheckboxOnYear();
			
			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(false);
			rowDate.setVisible(true);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);
			rowAdjustmentPeriod.setVisible(true);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "BalanceOfAccountsList").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "BalanceOfAccountsList").replaceAll("[&]", ""));

		}
		
		if (event.getTarget() == tButtonTreeSummary) {

			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = false;
			isTreeSummary = true;

			clearParameters();
			clearList();
			refreshHeader();
			
			setCheckboxOnYear();
			
			rowCheckbox.setVisible(true);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(false);
			rowDate.setVisible(false);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);
			rowAdjustmentPeriod.setVisible(true);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "TreeSummary").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "TreeSummary").replaceAll("[&]", ""));

		}

		
		if (event.getTarget() == tButtonExportHtml) {
			exportAsHtml();
		}
		
		if (event.getTarget() == tButtonExportText) {
			exportAsText();
		}
		
		if (event.getTarget() == checkboxOnYear) {
			setCheckboxOnYear();
		}

		if (event.getTarget() == checkboxOnMonth)
			setCheckboxOnMonth();

		if (event.getTarget() == checkboxOnDay)
			setCheckboxOnDay();

		if (event.getTarget() == bRefresh) {

			if (isAccountOverView)
				bZoom.setVisible(true);
			else
				bZoom.setVisible(false);
			
			
			if (isAccountCourse) {

				accountCourse();

			}

			if (isSummary) {
				if (checkboxOnYear.isChecked())
					onYear();
				if (checkboxOnMonth.isChecked())
					onMonth();
				if (checkboxOnDay.isChecked())
					onDay();
			}
			
			
			if (isSummaryDocument) {

				summaryAccountDocument();

			}


			if (isAccountsOverView) {

				accountsOverView();
			}

			if (isAccountOverView) {

				accountOverView();
			}

				
			
			if (isBalanceOfAccountsList){
				BalanceOfAccountsList();
			}

			if (isTreeSummary){
				treeSummary();
			}

			tabBox.setSelectedIndex(1);

		}
		

		if (event.getTarget() == bZoom) {

			zoomToDoc();

		}
		
		event.stopPropagation();

	}

	private void zoomToDoc() {

		List<ListCell> cells = null;
		int table_id = -1;

		ListItem litem = listboxResult.getSelectedItem();

		if (litem != null) {
			cells = litem.getChildren();
		}

		if (!cells.isEmpty()) {

			table_id = new Query(Env.getCtx(), MTable.Table_Name,
					MTable.COLUMNNAME_TableName + "=? ", null).setParameters(
					cells.get(12).getLabel()).firstId();

			if((table_id != -1) && ((Integer) cells.get(13).getValue() != -1))
				AEnv.zoom(table_id, (Integer) cells.get(13).getValue());

		}

	}

	private void exportAsHtml() {

		p_data.exportAsHtml(tabResult.getLabel(), getHeadColumns(), getRows());
		
	}

	private void exportAsText() {

		p_data.exportAsTxt(tabResult.getLabel(), getHeadColumns(), getRows());
		
	}

	
	private void setCheckboxOnYear() {
		dateboxDateFrom.setValue(null);
		dateboxDateTo.setValue(null);
		rowDate.setVisible(false);
		checkboxOnYear.setChecked(true);
		checkboxOnMonth.setChecked(false);
		checkboxOnDay.setChecked(false);
	}

	private void setCheckboxOnMonth() {

		dateboxDateFrom.setValue(null);
		dateboxDateTo.setValue(null);
		rowDate.setVisible(true);
		checkboxOnYear.setChecked(false);
		checkboxOnDay.setChecked(false);
	}

	private void setCheckboxOnDay() {

		dateboxDateFrom.setValue(null);
		dateboxDateTo.setValue(null);
		rowDate.setVisible(true);
		checkboxOnYear.setChecked(false);
		checkboxOnMonth.setChecked(false);
	}

	private void accountCourse() {

		StringBuilder valueFrom = new StringBuilder();
		StringBuilder valueTo = new StringBuilder();

		if (searchEditorValueFrom.getValue() != null)
			valueFrom.append(p_data.getElementValue((Integer) searchEditorValueFrom.getValue()).getValue());

		if (searchEditorValueTo.getValue() != null)
			valueTo.append(p_data.getElementValue((Integer) searchEditorValueTo.getValue()).getValue());

		Date dateFrom = dateboxDateFrom.getValue();
		Date dateTo = dateboxDateTo.getValue();

		Timestamp tDateFrom = new Timestamp(0);

		Timestamp tDateTo = new Timestamp(0);

		if (dateFrom != null)
			tDateFrom = new Timestamp(dateFrom.getTime());

		if (dateFrom == null)
			tDateFrom.setTime(tDateFrom.getTime() + 10000);

		if (dateTo == null) 
			tDateTo = new Timestamp(Env.getContextAsDate(Env.getCtx(), "Date").getTime());
		else
			tDateTo = new Timestamp(dateTo.getTime());

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", valueFrom.toString());
		params.put("valueTo", valueTo.toString());
		params.put("dateFrom", tDateFrom);
		params.put("dateTo", tDateTo);

		if ((searchEditorValueFrom.getValue() != null) || (searchEditorValueTo.getValue() != null)) {
			addRecords(p_data.getRecords(sqls.getSqlAccountCourse(params), params));

		} else {

			FDialog.error(m_WindowNo, mForm, "ParameterError",
					Msg.translate(Env.getCtx(), "AccountValues are needed !"));

		}

		labelDebit.setValue("");
		labelCredit.setValue("");
		labelBalance.setValue("");

	}

	private void accountsOverView() {

		setLabelOfColumn(4, "");
		setLabelOfColumn(8, "");
		setLabelOfColumn(9, "");
		setLabelOfColumn(10, "");
		setLabelOfColumn(11, "");
		setLabelOfColumn(12, "");

		
		Date dateFrom = dateboxDateFrom.getValue();
		Date dateTo = dateboxDateTo.getValue();

		Timestamp tDateFrom = new Timestamp(0);

		Timestamp tDateTo = new Timestamp(0);

		if (dateFrom != null)
			tDateFrom = new Timestamp(dateFrom.getTime());

		if (dateFrom == null)
			tDateFrom.setTime(tDateFrom.getTime() + 10000);

		if (dateTo == null) 
			tDateTo = new Timestamp(Env.getContextAsDate(Env.getCtx(), "Date").getTime());
		else
			tDateTo = new Timestamp(dateTo.getTime());

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", "");
		params.put("valueTo", "");
		params.put("dateFrom", tDateFrom);
		params.put("dateTo", tDateTo);

		addRecords(p_data.getRecords(sqls.getSqlAccountsOverView(params), params));

		setBalanceofAccountslist(p_data.getDebit(),p_data.getCredit(),p_data.getBalance());

	}
	


	private void accountOverView() {

		StringBuilder valueFrom = new StringBuilder();

		if (searchEditorValueFrom.getValue() != null)
			valueFrom.append(p_data.getElementValue((Integer) searchEditorValueFrom.getValue()).getValue());

		Date dateFrom = dateboxDateFrom.getValue();
		Date dateTo = dateboxDateTo.getValue();

		Timestamp tDateFrom = new Timestamp(0);

		Timestamp tDateTo = new Timestamp(0);

		if (dateFrom != null)
			tDateFrom = new Timestamp(dateFrom.getTime());

		if (dateFrom == null)
			tDateFrom.setTime(tDateFrom.getTime() + 10000);

		if (dateTo == null) 
			tDateTo = new Timestamp(Env.getContextAsDate(Env.getCtx(), "Date").getTime());
		else
			tDateTo = new Timestamp(dateTo.getTime());

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", valueFrom.toString());
		params.put("valueTo", "");
		params.put("dateFrom", tDateFrom);
		params.put("dateTo", tDateTo);
		params.put("product_id", searchEditorProduct.getValue());
		params.put("bpartner_id", searchEditorBPartner.getValue());
		params.put("salesregion_id", searchEditorSalesRegion.getValue());
		params.put("project_id", searchEditorProject.getValue());

		if ((searchEditorValueFrom.getValue() != null)) {
			addRecords(p_data.getRecords(sqls.getSqlAccountOverView(params), params));
			setBalanceofAccountslist(p_data.getDebit(),p_data.getCredit(),p_data.getBalance());
		} else
			FDialog.error(m_WindowNo, mForm, "ParameterError",
					Msg.translate(Env.getCtx(), "AccountValues are needed !"));

	}

	private void onYear() {

		StringBuilder valueFrom = new StringBuilder();
		StringBuilder valueTo = new StringBuilder();

		if (searchEditorValueFrom.getValue() != null)
			valueFrom.append(p_data.getElementValue((Integer) searchEditorValueFrom.getValue()).getValue());

		if (searchEditorValueTo.getValue() != null)
			valueTo.append(p_data.getElementValue((Integer) searchEditorValueTo.getValue()).getValue());

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", valueFrom.toString());
		params.put("valueTo", valueTo.toString());
		params.put("dateFrom", new Timestamp(0));
		params.put("dateTo", new Timestamp(0));

		if ((searchEditorValueFrom.getValue() != null) || (searchEditorValueTo.getValue() != null)) {
			addRecords(p_data.getRecords(sqls.getSqlOnYear(params), params));
			setBalanceofAccountslist(p_data.getDebit(),p_data.getCredit(),p_data.getBalance());
		} else
			FDialog.error(m_WindowNo, mForm, "ParameterError",
					Msg.translate(Env.getCtx(), "AccountValues are needed !"));

	}

	private void onMonth() {

		StringBuilder valueFrom = new StringBuilder();
		StringBuilder valueTo = new StringBuilder();

		if (searchEditorValueFrom.getValue() != null)
			valueFrom.append(p_data.getElementValue((Integer) searchEditorValueFrom.getValue()).getValue());

		if (searchEditorValueTo.getValue() != null)
			valueTo.append(p_data.getElementValue((Integer) searchEditorValueTo.getValue()).getValue());

		Date dateFrom = dateboxDateFrom.getValue();
		Date dateTo = dateboxDateTo.getValue();

		Timestamp tDateFrom = new Timestamp(0);

		Timestamp tDateTo = new Timestamp(0);

		if (dateFrom != null)
			tDateFrom = new Timestamp(dateFrom.getTime());

		if (dateFrom == null)
			tDateFrom.setTime(tDateFrom.getTime() + 10000);

		if (dateTo == null) 
			tDateTo = new Timestamp(Env.getContextAsDate(Env.getCtx(), "Date").getTime());
		else
			tDateTo = new Timestamp(dateTo.getTime());

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", valueFrom.toString());
		params.put("valueTo", valueTo.toString());
		params.put("dateFrom", tDateFrom);
		params.put("dateTo", tDateTo);

		if ((searchEditorValueFrom.getValue() != null) || (searchEditorValueTo.getValue() != null)) {

			addRecords(p_data.getRecords(sqls.getSqlOnMonth(params), params));
			setBalanceofAccountslist(p_data.getDebit(),p_data.getCredit(),p_data.getBalance());

		} else
			FDialog.error(m_WindowNo, mForm, "ParameterError",
					Msg.translate(Env.getCtx(), "AccountValues are needed !"));

	}

	private void onDay() {

		StringBuilder valueFrom = new StringBuilder();
		StringBuilder valueTo = new StringBuilder();

		if (searchEditorValueFrom.getValue() != null)
			valueFrom.append(p_data.getElementValue((Integer) searchEditorValueFrom.getValue()).getValue());

		if (searchEditorValueTo.getValue() != null)
			valueTo.append(p_data.getElementValue((Integer) searchEditorValueTo.getValue()).getValue());

		Date dateFrom = dateboxDateFrom.getValue();
		Date dateTo = dateboxDateTo.getValue();

		Timestamp tDateFrom = new Timestamp(0);

		Timestamp tDateTo = new Timestamp(0);

		if (dateFrom != null)
			tDateFrom = new Timestamp(dateFrom.getTime());

		if (dateFrom == null)
			tDateFrom.setTime(tDateFrom.getTime() + 10000);

		if (dateTo == null) 
			tDateTo = new Timestamp(Env.getContextAsDate(Env.getCtx(), "Date").getTime());
		else
			tDateTo = new Timestamp(dateTo.getTime());

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", valueFrom.toString());
		params.put("valueTo", valueTo.toString());
		params.put("dateFrom", tDateFrom);
		params.put("dateTo", tDateTo);

		if ((searchEditorValueFrom.getValue() != null) || (searchEditorValueTo.getValue() != null)) {

			addRecords(p_data.getRecords(sqls.getSqlOnDay(params), params));
			setBalanceofAccountslist(p_data.getDebit(),p_data.getCredit(),p_data.getBalance());

		} else
			FDialog.error(m_WindowNo, mForm, "ParameterError",
					Msg.translate(Env.getCtx(), "AccountValues are needed !"));

	}

	private void summaryAccountDocument() {

		params.put("valueFrom", "");
		params.put("valueTo", "");
		params.put("dateFrom", new Timestamp(0));
		params.put("dateTo", new Timestamp(0));
		params.put("doctable", listboxSummaryTable.getSelectedItem().getLabel());
		params.put("docno", labelSummaryAccountDocument.getValue());
		params.put("summaryDocument", isSummaryDocument);

		if (!params.get("doctable").equals("") && !params.get("docno").equals("")) {

			addRecords(p_data.getRecords(sqls.getSqlDocumentAcct(), params));

			setBalanceofAccountslist(p_data.getDebit(),p_data.getCredit(),p_data.getBalance());

		} else
			FDialog.error(m_WindowNo, mForm, "ParameterError",
					Msg.translate(Env.getCtx(), "No dates and accountValues are needed, but doctype and documentno!"));

	}	
	
	private void BalanceOfAccountsList() {
		
		//balance_carried_forward, debit, credit, current_balance, ending_balance
		setLabelOfColumn(4, Msg.translate(Env.getCtx(),"Balance carried forward"));
		setLabelOfColumn(7, Msg.translate(Env.getCtx(),"Current Balance"));
		setLabelOfColumn(8, Msg.translate(Env.getCtx(),"Ending Balance"));		
		setLabelOfColumn(9, "");
		setLabelOfColumn(10, "");
		setLabelOfColumn(11, "");
		setLabelOfColumn(12, "");

		
		Date dateFrom = dateboxDateFrom.getValue();
		Date dateTo = dateboxDateTo.getValue();

		Timestamp tDateFrom = new Timestamp(0);

		Timestamp tDateTo = new Timestamp(0);

		if (dateFrom != null)
			tDateFrom = new Timestamp(dateFrom.getTime());

		if (dateFrom == null)
			tDateFrom.setTime(tDateFrom.getTime() + 10000);

		if (dateTo == null) 
			tDateTo = new Timestamp(Env.getContextAsDate(Env.getCtx(), "Date").getTime());
		else
			tDateTo = new Timestamp(dateTo.getTime());

		params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
		params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
		params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
		params.put("valueFrom", "");
		params.put("valueTo", "");
		params.put("dateFrom", tDateFrom);
		params.put("dateTo", tDateTo);

		addRecords(p_data.getRecords(sqls.getSqlBalanceOfAccountsList(params), params));
		
		setBalanceofAccountslist(p_data.getDebit(),p_data.getCredit(),p_data.getBalance());
		
	}

	private void setBalanceofAccountslist(BigDecimal debit,BigDecimal credit,BigDecimal balance){
		
		labelDebit.setValue(numberFormat.format(debit));
		labelCredit.setValue(numberFormat.format(credit));
		labelBalance.setValue(numberFormat.format(balance));	

			if (balance.compareTo(Env.ZERO) == -1)
				labelBalance.setStyle("color:red;font-weight: bold");
			else
				labelBalance.setStyle("color:black;font-weight: bold");
	}	
	
	
	private void treeSummary(){
		
		clearList();
		
		clearHeadColumns();
		
		listboxResult.setSizedByContent(true);
		listboxResult.appendChild(frozen);
		frozen.setColumns(1);
		
		MTreeNode rootNode = p_data.getTreeSummary();
		
		if(rootNode != null)
			fillTreeSummary(rootNode.children());	
		
	}


	private void fillTreeSummary(Enumeration<?> nodeEnum) {
		
		
		setLabelOfColumn(0, Msg.getMsg(Env.getCtx(), "SummaryAccounts"));
		
		
		while (nodeEnum.hasMoreElements()) {

			MTreeNode mChildNode = (MTreeNode) nodeEnum.nextElement();

			if (mChildNode.isSummary()) {

				StringBuffer summaryLabel = new StringBuffer();

				for (int x = 2; x <= mChildNode.getLevel(); x++)
					summaryLabel.append("\t ");
				
				summaryLabel.append(mChildNode.getName());

					
				ListItem item = new ListItem();
				
				ListCell cell = new ListCell();
				cell.setLabel(summaryLabel.toString());
				
				
				
				if(mChildNode.getLevel() == 1)
					cell.setStyle("color:black;font-weight: bold");
				else
					cell.setStyle("color:#5a5a5a;font-weight: bold");
				
				item.appendChild(cell);

				params.put("organisation", listboxOrganisation.getSelectedItem().getLabel());
				params.put("acctschema", listboxSelAcctSchema.getSelectedItem().getValue());
				params.put("isWithAdjustmentPeriod", listboxWithAdjustmentPeriod.getSelectedItem());		
				params.put("valueFrom", "");
				params.put("valueTo", "");
				
				String list = p_data.acctListshorten(MReportTree.getWhereClause(Env.getCtx(),0, MAcctSchemaElement.ELEMENTTYPE_Account, mChildNode.getNode_ID()));		
				MElementValue eValue = new MElementValue(Env.getCtx(),mChildNode.getNode_ID(),null);
				
				params.put("treeSummary", list);
				params.put("treeValue", eValue);							

				
				try {
					
					if (checkboxOnYear.isChecked()){
						params.put("dateFrom", new Timestamp(0));
						params.put("dateTo", new Timestamp(0));
						
						for(int c=1;c<getHeadColumnsCount();c++){
							
							ListCell ce = new ListCell();	
							ce.setLabel("");
							item.appendChild(ce);
							
						}
						
						addRecordsH(p_data.getRecords(sqls.getSqlTreeOnYear(params), params),item);
					}
					
					if (checkboxOnMonth.isChecked()){
						
						if(dateboxDateFrom.getValue() == null){
							FDialog.error(m_WindowNo, mForm, "ParameterError",
									Msg.translate(Env.getCtx(), "DateFrom needed !"));
							break;
						}else{
							createColumnsMonth(item);
							params.put("dateFrom", new Timestamp(dateboxDateFrom.getValue().getTime()));
							params.put("dateTo", new Timestamp(dateboxDateTo.getValue().getTime()));
							addRecordsH(p_data.getRecords(sqls.getSqlTreeOnMonth(params), params), item);
						}
					}
					
					if (checkboxOnDay.isChecked()){

						if(dateboxDateFrom.getValue() == null){
							FDialog.error(m_WindowNo, mForm, "ParameterError",
									Msg.translate(Env.getCtx(), "DateFrom needed !"));
							break;
						}else{
							createColumnsDay(item);
							params.put("dateFrom", new Timestamp(dateboxDateFrom.getValue().getTime()));
							params.put("dateTo", new Timestamp(dateboxDateTo.getValue().getTime()));
							addRecordsH(p_data.getRecords(sqls.getSqlTreeOnDay(params), params), item);
						}
					}
					
				} catch (ParseException e) {
					
					e.printStackTrace();
				}

				listboxResult.appendChild(item);
				
				if (mChildNode.getChildCount() > 0)
					fillTreeSummary(mChildNode.children());
				

			}
			
		}

	}
	



	private void createColumnsMonth(ListItem item) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateboxDateFrom.getValue());
		

		for (int month= cal.get(Calendar.MONTH); month <= 12; month++ ){
			
			if(getHeadColumnsCount()<12)
				createEmptyColumns(1);

			setLabelOfColumn(month, String.valueOf(month));	

		
			ListCell cell = new ListCell();
				
			cell.setLabel("");
			
			item.appendChild(cell);
			
				
		
		}

		
	}	
	
	private void createColumnsDay(ListItem item) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateboxDateFrom.getValue());

		for (int day = cal.get(Calendar.DAY_OF_MONTH); day <= cal
				.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {

			if (getHeadColumnsCount() <= cal
					.getActualMaximum(Calendar.DAY_OF_MONTH))
				createEmptyColumns(1);

			setLabelOfColumn(day, String.valueOf(day));

			ListCell cell = new ListCell();

			cell.setLabel("");

			item.appendChild(cell);

		}
	}	
	
	private void addRecordsH(List<List<Object>> objs,ListItem item) throws ParseException {
		
		int sortYear = 0;
		
		for (List<Object> obj : objs) {

			if (checkboxOnMonth.isChecked()) {

				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) obj.get(4));
				
				setCell(cal.get(Calendar.MONTH)+1, item, numberFormat.format(obj.get(7)));

			}
			
			if (checkboxOnDay.isChecked()) {

				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) obj.get(4));
				
				setCell(cal.get(Calendar.DAY_OF_MONTH)+1, item, numberFormat.format(obj.get(7)));

			}

			if (checkboxOnYear.isChecked()) {
				

				if(getHeadColumnsCount()<sortYear)
					createEmptyColumns(1);
				
				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) obj.get(4));
				
				if(cal.get(Calendar.YEAR)< sortYear)
					sortYear--;
				else if(cal.get(Calendar.YEAR)>sortYear)
					sortYear++;
				
				
				setLabelOfColumn(sortYear, String.valueOf(cal.get(Calendar.YEAR)));	
							
				setCell(sortYear, item, numberFormat.format(obj.get(7)));
				
			}
			

		}

	}

	private void setCell(int col, ListItem item, String label) {

		int y = 0;

		for (Component cell : item.getChildren()) {

				if (y == col){
					
						try {
							if (numberFormat.parse(label).floatValue() < 0)
								((ListCell) cell).setStyle("color:red;font-weight: bold;text-align: right");
							else
								((ListCell) cell).setStyle("font-weight: bold;text-align: right");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					
					((ListCell) cell).setLabel(label);
					
				}
			

			y++;
		}

	}
	
	@Override
	public void valueChange(ValueChangeEvent evt) {

		if (evt.getPropertyName().equals("C_ElementValue_ID")) {
			searchEditorValueTo.setValue(evt.getNewValue());
		}

	}

}
