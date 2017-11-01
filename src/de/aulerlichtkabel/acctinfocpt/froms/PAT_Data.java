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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.panel.InfoPanel;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MElementValue;
import org.compiere.model.MOrg;
import org.compiere.model.MTable;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.model.MTree_Base;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.ValueNamePair;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;

public class PAT_Data {

	private MAcctSchema[] a_Schema = null;
	private StringBuilder docTableName = new StringBuilder();
	private Integer docID = 0;
	private StringBuilder docOrgName = new StringBuilder();
	private boolean hasDocnoCol = false;
	
	private BigDecimal debit = new BigDecimal("0");
	private BigDecimal credit = new BigDecimal("0");
	private BigDecimal balance = new BigDecimal("0");

	private PAT_Sqls sqls = new PAT_Sqls();
	
	/** Logger */
	public static CLogger log = CLogger.getCLogger(PAT_WAcctInfCptForm.class);

	public ValueNamePair[] getTables() {

		String sql = "SELECT AD_Table_ID, TableName FROM AD_Table t " + "WHERE EXISTS (SELECT * FROM AD_Column c"
				+ " WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='Posted')" + " AND IsView='N' ";

		ValueNamePair[] arr = DB.getValueNamePairs(sql, false, null);

		return arr;

	}

	public void getAcctSchema(Listbox box) {

		a_Schema = MAcctSchema.getClientAcctSchema(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()));

		for (int i = 0; i < a_Schema.length; i++) {
			KeyNamePair keypair = new KeyNamePair(a_Schema[i].getC_AcctSchema_ID(), a_Schema[i].getName());
			box.appendItem(keypair.getName(), keypair.getKey());

		}
	}

	public void getDocument(int windowNo, String tableName, final Label label, final Listbox box) {

		docID = 0;
		docTableName.delete(0, docTableName.length());

		final MTable table = new MTable(Env.getCtx(), new Integer((String)box.getSelectedItem().getValue()), null);

		hasDocnoCol = false;
		if(table.getColumn("DocumentNo") != null)
			hasDocnoCol = true;
		
		
		if (table != null) {

			final InfoPanel info = InfoPanel.create(windowNo, table.getTableName(), table.getTableName() + "_ID", "",
					false, "");

			if (!info.loadedOK()) {
				label.setValue("");
				return;
			}

			info.setVisible(true);
			
			info.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {

					label.setValue(getDocNo(table.getTableName(), info.getSelectedSQL()));
					
					docID = 0;
					docID = getDocID(table.getTableName(), info.getSelectedSQL());
					
					docTableName.delete(0, docTableName.length());
					docTableName.append(table.getTableName());
					
					docOrgName.delete(0, docOrgName.length());
					docOrgName.append(getDocOrgName(table.getTableName(), info.getSelectedSQL()));

					if (log.isLoggable(Level.CONFIG))
						log.config(table.get_TableName() + "_ID" + " - " + (Integer) info.getSelectedKey());

				}
			});
			AEnv.showWindow(info);
			
			
		}
	}

	private String getDocNo(String tableName, String sql) {

		StringBuilder result = new StringBuilder();

		if (hasDocnoCol)
			result.append(DB.getSQLValueString(null, "SELECT DocumentNo FROM " + tableName + " WHERE " + sql));
		else
			result.append(DB.getSQLValueString(null, "SELECT Name FROM " + tableName + " WHERE " + sql));

		return result.toString();
	}

	private Integer getDocID(String tableName, String sql) {

		return DB.getSQLValue(null, "SELECT " + tableName + "_ID" + " FROM " + tableName + " WHERE " + sql);

	}

	private String getDocOrgName(String tableName, String sql) {

		String orgName = DB.getSQLValueString(null, "SELECT o.name" + " FROM " + tableName
				+ " JOIN AD_Org o ON o.AD_Org_ID = " + tableName + ".AD_Org_ID" + " WHERE " + sql);

		return orgName;

	}

	public StringBuilder getDocOrgName() {
		
		return docOrgName;
	}

	public Integer getDocID() {

		return docID;
	}

	public StringBuilder getDocTableName() {
		return docTableName;
	}

	public String getClientname() {

		return Env.getContext(Env.getCtx(), "#AD_Client_Name");
	}

	public MOrg[] getOrganisations() {

		List<MOrg> list = new Query(Env.getCtx(), MOrg.Table_Name, "AD_Client_ID=?", null)
				.setParameters(Env.getAD_Client_ID(Env.getCtx())).setOrderBy(MOrg.COLUMNNAME_Name).list();

		return list.toArray(new MOrg[list.size()]);

	}

	public MElementValue getElementValue(int ID) {

		return new Query(Env.getCtx(), MElementValue.Table_Name, "C_ElementValue_ID=?", null).setParameters(ID).first();

	}

	public List<List<Object>> getRecords(StringBuilder sql, Map<String, Object> params) {

		List<List<Object>> rows = new ArrayList<List<Object>>();

		DecimalFormat numberFormat = DisplayType.getNumberFormat(DisplayType.Amount);

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlApx = new StringBuilder();

			sqlApx.append(" 1 = 1 ");

			if (params.get("product_id") != null) {

				sqlApx.append(" and m_product_id = ? ");
			}

			if (params.get("bpartner_id") != null) {

				sqlApx.append(" and c_bpartner_id = ? ");
			}

			if (params.get("salesregion_id") != null) {

				sqlApx.append(" and c_salesregion_id = ? ");
			}

			if (params.get("project_id") != null) {

				sqlApx.append(" and c_project_id = ? ");
			}

			sqlApx.append(" and ");
			
			sql.insert(sql.indexOf("where ")+6, sqlApx);

			pstmt = DB.prepareStatement(sql.toString(), null);
			// System.out.println(sql);
			int idx = 1;

			if (!((String) params.get("valueFrom")).equals("")) {
				pstmt.setString(idx, (String) params.get("valueFrom"));
				idx++;
			}
			if (!((String) params.get("valueTo")).equals("")) {
				pstmt.setString(idx, (String) params.get("valueTo"));
				idx++;
			}

			if (!params.get("dateFrom").equals(new Timestamp(0))) {
				pstmt.setTimestamp(idx, (Timestamp) params.get("dateFrom"));
				idx++;
			}
			if (!params.get("dateTo").equals(new Timestamp(0))) {
				pstmt.setTimestamp(idx, (Timestamp) params.get("dateTo"));
				idx++;
			}

			if ((getDocTableName().length() > 0) && (boolean) params.get("summaryDocument")) {
				pstmt.setString(idx, getDocTableName().toString());
				idx++;
			}

			if (!getDocID().equals(0) && (boolean) params.get("summaryDocument")) {
				pstmt.setInt(idx, getDocID());
				idx++;
			}

			// Client
			pstmt.setString(idx, getClientname());
			idx++;

			// Organistation
			if ((boolean) params.get("summaryDocument"))
				pstmt.setString(idx, getDocOrgName().toString());
			else{
				
				if(!((String) params.get("organisation")).equals("*")){
					pstmt.setString(idx, (String) params.get("organisation"));
					idx++;
				}
					
			}

			// AcctSchema
			if (!(boolean) params.get("summaryDocument")) {
				pstmt.setInt(idx, (Integer) params.get("acctschema"));
				idx++;
			}

			if (params.get("product_id") != null) {
				pstmt.setInt(idx, (Integer) params.get("product_id"));
				idx++;
			}

			if (params.get("bpartner_id") != null) {
				pstmt.setInt(idx, (Integer) params.get("bpartner_id"));
				idx++;
			}

			if (params.get("salesregion_id") != null) {
				pstmt.setInt(idx, (Integer) params.get("salesregion_id"));
				idx++;
			}

			if (params.get("project_id") != null) {
				pstmt.setInt(idx, (Integer) params.get("project_id"));
			}

			rs = pstmt.executeQuery();
			
			debit=Env.ZERO;
			credit=Env.ZERO;
			balance=Env.ZERO;

			while (rs.next()) {
				List<Object> row = new ArrayList<Object>();

				// Client
				if (rs.getString(3) != null)
					row.add(rs.getString(3));
				else
					row.add("");
				// Organisation
				if (rs.getString(4) != null)
					row.add(rs.getString(4));
				else
					row.add("");
				// Account
				if (rs.getString(6) != null)
					row.add(rs.getString(6));
				else
					row.add("");
				// Name
				if (rs.getString(7) != null)
					row.add(rs.getString(7));
				else
					row.add("");

				// DateAcct or Balance Carried Forward
				if (rs.getObject(9) != null){
					
					if (rs.getObject(9) instanceof Timestamp)
						row.add(Env.getLanguage(Env.getCtx()).getDateFormat().format(rs.getTimestamp(9)));
				
					if (rs.getObject(9) instanceof BigDecimal)
						row.add(numberFormat.format(rs.getBigDecimal(9)));

				}
				else
					row.add("");
				// <== DateAcct or Balance Carried Forward
 				
				// Debit
				if (rs.getBigDecimal(10) != null){
					row.add(numberFormat.format(rs.getBigDecimal(10)));
					debit=debit.add(rs.getBigDecimal(10));
				}
				else
					row.add("");
				// Credit
				if (rs.getBigDecimal(11) != null){
					row.add(numberFormat.format(rs.getBigDecimal(11)));
					credit=credit.add(rs.getBigDecimal(11));
				}
				else
					row.add("");
				// Balance
				if (rs.getBigDecimal(12) != null){
					row.add(numberFormat.format(rs.getBigDecimal(12)));
					balance=balance.add(rs.getBigDecimal(12));
				}
				
				else
					row.add("");
				
				// Product or Ending Balance
				if (rs.getObject(13) != null){
					
					if (rs.getObject(13) instanceof String)				
						row.add(rs.getString(13));
				
					if (rs.getObject(13) instanceof BigDecimal)				
						row.add(numberFormat.format(rs.getBigDecimal(13)));

				}
				else
					row.add("");
				// <== Product or Ending Balance

				
				// BPartner
				if (rs.getString(17) != null)
					row.add(rs.getString(17));
				else
					row.add("");
				// SalesRegion
				if (rs.getString(19) != null)
					row.add(rs.getString(19));
				else
					row.add("");
				// Project
				if (rs.getString(15) != null)
					row.add(rs.getString(15));
				else
					row.add("");
				// Table
				if (rs.getString(20) != null)
					row.add(rs.getString(20));
				else
					row.add("");

				rows.add(row);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sql.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		return rows;

	}
	
	public BigDecimal getDebit(){
		
		return debit;
	}
	
	public BigDecimal getCredit(){
		
		return credit;
	}

	public BigDecimal getBalance(){
		
		return balance;
	}
	
	
	public MTreeNode getTreeSummary(){
		
		MTree_Base treebase = new Query(Env.getCtx(),MTree.Table_Name, MTree.COLUMNNAME_TreeType+ " ='EV'", null).setClient_ID().first();

        MTree AccountTree = new MTree(Env.getCtx(), treebase.getAD_Tree_ID(), false, true, null);        

        return AccountTree.getRoot();
	
	}	
	
	public List<Object> getAccountsListWhereClause(){		
		
		List<Object> valueList = DB.getSQLValueObjectsEx(null, sqls.getSQLAccountIDsInFactAcct().toString());
		
		return valueList;
		
	}
	
	public String acctListshorten(String list){
		
		for(Object accountid : getAccountsListWhereClause())
		if(!list.contains(String.valueOf(accountid)))
			list.replaceAll("OR Account_ID="+accountid, "");
		
		return list;
	}	
	
}
