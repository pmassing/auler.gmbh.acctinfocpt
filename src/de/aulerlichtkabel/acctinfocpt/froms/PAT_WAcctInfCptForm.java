/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

/**
 * Plugin AccountInfoCockpit
 * 
 * @author Patric Maßing (Hans Auler GmbH)
 * 2017
 * 
 *  
 */

package de.aulerlichtkabel.acctinfocpt.froms;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.LayoutUtils;
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
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrg;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Center;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

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

	Vbox vBox = new Vbox();

	Listbox listboxResult = new Listbox();
	Borderlayout layout = new Borderlayout();

	private Panel parameterPanel = new Panel();
	private Grid parameterLayout = GridFactory.newGridLayout();

	Rows rows = parameterLayout.newRows();
	Row rowCheckbox = rows.newRow();
	Row rowClientAndOrg = rows.newRow();
	Row rowAcctSchema = rows.newRow();	
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
	private ConfirmPanel buttonPanel = new ConfirmPanel(true, true, false, false, false, false, false);
	private Grid buttonLayout = GridFactory.newGridLayout();

	private Button bCancel = buttonPanel.getButton(ConfirmPanel.A_CANCEL);
	private Button bRefresh = buttonPanel.getButton(ConfirmPanel.A_REFRESH);
	private Button bOkay = buttonPanel.getButton(ConfirmPanel.A_OK);

	private Label labelDebit = new Label();
	private Label labelCredit = new Label();
	private Label labelBalance = new Label();

	private PAT_Sqls sqls = new PAT_Sqls();
	private PAT_Data p_data = new PAT_Data();

	// Format
	public DecimalFormat numberFormat = DisplayType.getNumberFormat(DisplayType.Amount);

	private boolean isAccountCourse = false;
	private boolean isAccountsOverView = false;
	private boolean isAccountOverView = false;
	private boolean isSummaryDocument = false;
	private boolean isSummary = false;
	private boolean isBalanceOfAccountsList = false;
	
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

		LayoutUtils.addSclass("Document Management", mForm);

		mForm.appendChild(layout);

	}

	private void createTabs() {

		tabParameter.addEventListener(Events.ON_SELECT, this);
		tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "ViewerQuery").replaceAll("[&]", ""));

		tabResult.addEventListener(Events.ON_SELECT, this);
		tabResult.setLabel(Msg.getMsg(Env.getCtx(), "ViewerResult").replaceAll("[&]", ""));

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
		center.setAutoscroll(false);
		center.setHflex("1");
		center.setVflex("1");
		center.setParent(layout);

		tabBox.setParent(center);

	}

	private Toolbar createToolbar() {

		tButtonAccountCourse.setImage(ThemeManager.getThemeResource("images/InfoAccount24.png"));
		tButtonAccountCourse.setTooltip("AccountCourse");
		tButtonAccountCourse.addEventListener(Events.ON_CLICK, this);

		tButtonAccountsOverView.setImage(ThemeManager.getThemeResource("images/Account24.png"));
		tButtonAccountsOverView.addEventListener(Events.ON_CLICK, this);

		tButtonAccountOverView.setImage(ThemeManager.getThemeResource("images/Account24.png"));
		tButtonAccountOverView.addEventListener(Events.ON_CLICK, this);

		tButtonSummaryDocument.setImage(ThemeManager.getThemeResource("images/Report24.png"));
		tButtonSummaryDocument.addEventListener(Events.ON_CLICK, this);

		tButtonSummary.setImage(ThemeManager.getThemeResource("images/Summary24.png"));
		tButtonSummary.addEventListener(Events.ON_CLICK, this);
		
		tBalanceOfAccountsList.setImage(ThemeManager.getThemeResource("images/Report24.png"));
		tBalanceOfAccountsList.addEventListener(Events.ON_CLICK, this);
		
		
		toolBar.appendChild(tButtonAccountCourse);
		toolBar.appendChild(tButtonAccountsOverView);
		toolBar.appendChild(tButtonAccountOverView);
		toolBar.appendChild(tButtonSummaryDocument);
		toolBar.appendChild(tButtonSummary);
		toolBar.appendChild(tBalanceOfAccountsList);

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
		
		rowAcctSchema.appendChild(labelAcctSchema);
		rowAcctSchema.appendChild(listboxSelAcctSchema);
		rowAcctSchema.appendChild(new Space());
		rowAcctSchema.appendChild(new Space());

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
		rowAccountDocument.setVisible(false);
		rowAcctValue.setVisible(false);
		rowDate.setVisible(false);
		rowDimension.setVisible(false);
		rowDimension2.setVisible(false);

	}

	private void clearParameters() {

		dateboxDateFrom.setValue(null);
		dateboxDateTo.setValue(null);
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
		bCancel.addActionListener(this);
		bOkay.addActionListener(this);

	}

	private void createResultPanel() {

		listboxResult.setAutopaging(true);
		// mold: allowed: [paging, select, default]
		listboxResult.setMold("paging");
		listboxResult.setVflex("1");

		listboxResult.appendChild(createHeader(headColumns()));

	}

	private List<String> headColumns() {

		List<String> headColumn = new ArrayList<String>();
		headColumn.add(Msg.translate(Env.getCtx(), "Client"));
		headColumn.add(Msg.translate(Env.getCtx(), "Organisation"));
		headColumn.add(Msg.translate(Env.getCtx(), "Account"));
		headColumn.add(Msg.translate(Env.getCtx(), "Name"));
		headColumn.add(Msg.translate(Env.getCtx(), "DateAcct"));
		headColumn.add(Msg.translate(Env.getCtx(), "Debit"));
		headColumn.add(Msg.translate(Env.getCtx(), "Credit"));
		headColumn.add(Msg.translate(Env.getCtx(), "Balance"));
		headColumn.add(Msg.translate(Env.getCtx(), new String("Product").replaceAll("[&]", "")));
		headColumn.add(Msg.translate(Env.getCtx(), new String("BPartner").replaceAll("[&]", "")));
		headColumn.add(Msg.translate(Env.getCtx(), "SalesRegion"));
		headColumn.add(Msg.translate(Env.getCtx(), "Project"));
		headColumn.add(Msg.translate(Env.getCtx(), "Table"));

		return headColumn;

	}

	private void clearList() {
		
		listboxResult.removeAllItems();

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
		
		ListHead listhead = null;		

		for(Component c : listboxResult.getHeads()){

			if(c instanceof ListHead){
				
				listhead = (ListHead)c;
	
				for (Component c2 : listhead.getChildren()){
					
					ListHeader listheader = null;
					
					if(c2 instanceof ListHeader){
						
						listheader = (ListHeader)c2;
						
						if(p==pos)
							listheader.setLabel(Msg.getMsg(Env.getCtx(), label));
						
					}

					p++;
				}

			}
		}	
		

	}
	
	private void refreshHeader(){
		int p = 0;
		
		ListHead listhead = null;		

		for(Component c : listboxResult.getHeads()){

			if(c instanceof ListHead){
				
				listhead = (ListHead)c;
	
				for (Component c2 : listhead.getChildren()){
					
					ListHeader listheader = null;
					
					if(c2 instanceof ListHeader){
						
						listheader = (ListHeader)c2;
						listheader.setLabel(headColumns().get(p));
						
					}
					
					p++;

				}

			}
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

		for (Object o : row) {

			ListCell cell = new ListCell();
			cell.setLabel(o.toString());

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

						if((cell.getValue() != null) && (c==5 || c==9))
							cell.setValue(numberFormat.format(cell.getValue()));

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
		

		if (event.getTarget() == bCancel || event.getTarget() == bOkay)
			dispose();

		if (event.getTarget() == dateboxDateFrom) {
			dateboxDateTo.setValue(dateboxDateFrom.getValue());
		}

		if (event.getTarget() == buttonSummaryAccountDocument) {

			p_data.getDocument(this.m_WindowNo, listboxSummaryTable.getSelectedItem().getLabel(),
					labelSummaryAccountDocument, listboxSummaryTable);
		}

		if (event.getTarget() == tButtonAccountCourse) {


			clearParameters();
			clearList();
			refreshHeader();
			
			isAccountCourse = true;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = false;

			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(true);
			rowDate.setVisible(true);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountCourse").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountCourse").replaceAll("[&]", ""));

		}

		if (event.getTarget() == tButtonAccountsOverView) {

			clearParameters();
			clearList();
			refreshHeader();
			
			isAccountCourse = false;
			isAccountsOverView = true;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = false;

			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(false);
			rowDate.setVisible(true);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountsOverView").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountsOverView").replaceAll("[&]", ""));

		}

		if (event.getTarget() == tButtonAccountOverView) {

			clearParameters();
			clearList();
			refreshHeader();
			
			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = true;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = false;

			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(true);
			rowDate.setVisible(true);
			rowDimension.setVisible(true);
			rowDimension2.setVisible(true);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountOverView").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "AccountOverView").replaceAll("[&]", ""));

		}

		if (event.getTarget() == tButtonSummaryDocument) {

			clearParameters();
			clearList();
			refreshHeader();
			
			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = true;
			isSummary = false;
			isBalanceOfAccountsList = false;

			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(false);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(true);
			rowAcctValue.setVisible(false);
			rowDate.setVisible(false);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "SummaryDocument").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "SummaryDocument").replaceAll("[&]", ""));
		}

		if (event.getTarget() == tButtonSummary) {

			clearParameters();
			clearList();
			refreshHeader();
			
			setCheckboxOnYear();

			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = true;
			isBalanceOfAccountsList = false;

			rowCheckbox.setVisible(true);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(true);
			rowDate.setVisible(false);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "Summary").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "Summary").replaceAll("[&]", ""));

		}
		
		
		if (event.getTarget() == tBalanceOfAccountsList) {

			clearParameters();
			clearList();
			refreshHeader();
			
			setCheckboxOnYear();

			isAccountCourse = false;
			isAccountsOverView = false;
			isAccountOverView = false;
			isSummaryDocument = false;
			isSummary = false;
			isBalanceOfAccountsList = true;

			rowCheckbox.setVisible(false);
			rowClientAndOrg.setVisible(true);
			rowAcctSchema.setVisible(true);
			rowAccountDocument.setVisible(false);
			rowAcctValue.setVisible(false);
			rowDate.setVisible(true);
			rowDimension.setVisible(false);
			rowDimension2.setVisible(false);

			tabBox.setSelectedIndex(0);

			tabResult.setLabel(Msg.getMsg(Env.getCtx(), "Result").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "BalanceOfAccountsList").replaceAll("[&]", ""));
			tabParameter.setLabel(Msg.getMsg(Env.getCtx(), "Query").replaceAll("[&]", "") + " - "
					+ Msg.getMsg(Env.getCtx(), "BalanceOfAccountsList").replaceAll("[&]", ""));

		}
		

		if (event.getTarget() == checkboxOnYear) {
			setCheckboxOnYear();
		}

		if (event.getTarget() == checkboxOnMonth)
			setCheckboxOnMonth();

		if (event.getTarget() == checkboxOnDay)
			setCheckboxOnDay();

		if (event.getTarget() == bRefresh) {

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

			tabBox.setSelectedIndex(1);

		}

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

		rowDate.setVisible(true);
		checkboxOnYear.setChecked(false);
		checkboxOnDay.setChecked(false);
	}

	private void setCheckboxOnDay() {

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
	
	@Override
	public void valueChange(ValueChangeEvent evt) {

		if (evt.getPropertyName().equals("C_ElementValue_ID")) {
			searchEditorValueTo.setValue(evt.getNewValue());
		}

	}

}
