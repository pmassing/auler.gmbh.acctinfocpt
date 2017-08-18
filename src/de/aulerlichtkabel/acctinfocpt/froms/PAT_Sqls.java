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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.compiere.util.DisplayType;
import org.compiere.util.Env;

public class PAT_Sqls {

	
	private SimpleDateFormat dateFormat = DisplayType.getDateFormat(DisplayType.Date);
	
	
	public PAT_Sqls() {
		super();
	}

	public StringBuilder getSqlOnYear() {

		return new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"
						+ "null as datetrx,"
						+ "date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'YY'::character varying)"
						+ "::timestamp with time zone) + 12::double precision * '1 mon'::interval - 1::numeric "
						+ "AS dateacct,"
						+ "round(sum(amtacctdr),2),"
						+ "round(sum(amtacctcr),2),"
						+ "round(sum(amtacctdr-amtacctcr),2),"
						+ "null as product_value,"
						+ "null as product_description,"
						+ "null as project_value,"
						+ "null as project_description,"
						+ "null as bpartner_value,"
						+ "null as bpartner_description,"
						+ "null as description,"
						+ "null as salesregion,"
						+ "null as tablename "

						+ "from pat_facourse "

						+ "where account_value between ? and ? "

						+ "and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ "group by "

						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"

						+ "date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'YY'::character varying)"
						+ "::timestamp with time zone) + 12::double precision * '1 mon'::interval - 1::numeric "

						+ "Order by " + "account_value");

	}

	public StringBuilder getSqlOnYearBalance() {

		return new StringBuilder().append("select "

		+ "null as row, " + "null as fact_acct_id, " + "null as clientname, "
				+ "null as orgname, " + "null as account_id, "
				+ "null as account_value, " + "null as account_name, "
				+ "null as datetrx," + "null as dateacct,"
				+ "round(sum(amtacctdr),2)," + "round(sum(amtacctcr),2),"
				+ "round(sum(amtacctdr-amtacctcr),2), "
				+ "null as product_value, " + "null as product_description, "
				+ "null as project_value, " + "null as project_description, "
				+ "null as bpartner_value, " + "null as bpartner_description, "
				+ "null as salesregion, " + "null as tablename, "
				+ "null as record_id "

				+ "from pat_facourse "

				+ "where account_value between ? and ? "

				+ "and clientname = ? "

				+ " and orgname = ? "

				+ " and c_acctschema_id = ? "

				+ "group by " + "row");

	}

	public StringBuilder getSqlOnMonth() {

		return new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"
						+ "null as datetrx,"
						+ "date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'MM'::character varying)"
						+ "::timestamp with time zone) + 1::double precision * '1 mon'::interval - 1::numeric "
						+ "AS dateacct,"
						+ "round(sum(amtacctdr),2),"
						+ "round(sum(amtacctcr),2),"
						+ "round(sum(amtacctdr-amtacctcr),2) as current_balance,"
						+ "null as product_value,"
						+ "null as product_description,"
						+ "null as project_value,"
						+ "null as project_description,"
						+ "null as bpartner_value,"
						+ "null as bpartner_description,"
						+ "null as description,"
						+ "null as salesregion,"
						+ "null as tablename "

						+ "from pat_facourse "

						+ "where account_value between ? and ?  and dateacct between ? and ? "

						+ "and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ "group by "

						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"

						+ "date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'MM'::character varying)"
						+ "::timestamp with time zone) + 1::double precision * '1 mon'::interval - 1::numeric "

						+ "Order by " 						
						
						+ "firstof(min(dateacct)::timestamp with time zone, 'MM'::character varying)");

	}

	public StringBuilder getSqlOnMonthBalance() {

		return new StringBuilder()
				.append("select "
						+ "null as row, "
						+ "null as fact_acct_id, "
						+ "null as clientname, "
						+ "null as orgname, "
						+ "null as account_id, "
						+ "null as account_value, "
						+ "null as account_name, "
						+ "null as datetrx,"
						+ "null as dateacct,"
						+ "round(sum(amtacctdr),2),"
						+ "round(sum(amtacctcr),2),"
						+ "round(sum(amtacctdr-amtacctcr),2), "
						+ "null as product_value, "
						+ "null as product_description, "
						+ "null as project_value, "
						+ "null as project_description, "
						+ "null as bpartner_value, "
						+ "null as bpartner_description, "
						+ "null as salesregion, "
						+ "null as tablename, "
						+ "null as record_id "

						+ "from pat_facourse f "

						+ "where account_value between ? and ?  and dateacct between ? and ? "

						+ "and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ "group by " + "row");

	}

	public StringBuilder getSqlOnDay() {

		return new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"
						+ "null as datetrx, "
						+ "firstof(dateacct::timestamp with time zone, 'DD'::character varying) AS dateacct,"
						+ "round(sum(amtacctdr),2),"
						+ "round(sum(amtacctcr),2),"
						+ "round(sum(amtacctdr-amtacctcr),2) as current_balance,"
						+ "null as product_value,"
						+ "null as product_description,"
						+ "null as project_value,"
						+ "null as project_description,"
						+ "null as bpartner_value,"
						+ "null as bpartner_description,"
						+ "null as description,"
						+ "null as salesregion,"
						+ "null as tablename "

						+ "from pat_facourse "

						+ "where account_value between ? and ?  and dateacct between ? and ? "

						+ "and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ "group by "

						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"

						+ "firstof(dateacct::timestamp with time zone, 'DD'::character varying) "

						+ "Order by "
						
						+ "firstof(min(dateacct)::timestamp with time zone, 'DD'::character varying) ");

	}

	public StringBuilder getSqlOnDayBalance() {

		return new StringBuilder()
				.append("select "
						+ "null as row, "
						+ "null as fact_acct_id, "
						+ "null as clientname, "
						+ "null as orgname, "
						+ "null as account_id, "
						+ "null as account_value, "
						+ "null as account_name, "
						+ "null as datetrx,"
						+ "null as dateacct,"
						+ "round(sum(amtacctdr),2),"
						+ "round(sum(amtacctcr),2),"
						+ "round(sum(amtacctdr-amtacctcr),2), "
						+ "null as product_value, "
						+ "null as product_description, "
						+ "null as project_value, "
						+ "null as project_description, "
						+ "null as bpartner_value, "
						+ "null as bpartner_description, "
						+ "null as salesregion, "
						+ "null as tablename, "
						+ "null as record_id "

						+ "from pat_facourse f "
						+ "where account_value between ? and ?  and dateacct between ? and ? "

						+ "and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ "group by " + "row");

	}

	public StringBuilder getSqlAccountCourse() {

		return new StringBuilder()
				.append("select "

						+ "row, "
						+ "fact_acct_id, "
						+ "clientname, "
						+ "orgname, "
						+ "account_id, "
						+ "account_value, "
						+ "account_name, "
						+ "datetrx,"
						+ "dateacct,"
						+ "amtacctdr, "
						+ "amtacctcr, "
						+ "current_balance, "
						+ "product_value, "
						+ "product_description, "
						+ "project_value, "
						+ "project_description, "
						+ "bpartner_value, "
						+ "bpartner_description, "
						+ "salesregion, "
						+ "tablename, "
						+ "record_id "	
						
						+ "from pat_accountcourse(?,?,?,?) " + "WHERE "

				+ " clientname = ? "

				+ " and orgname = ? "

				+ " and c_acctschema_id = ? "

				+ "group by " 
				
						+ "row, "
						+ "fact_acct_id, "
						+ "clientname, "
						+ "orgname, "
						+ "account_id, "
						+ "account_value, "
						+ "account_name, "
						+ "datetrx,"
						+ "dateacct,"
						+ "amtacctdr, "
						+ "amtacctcr, "
						+ "current_balance, "
						+ "product_value, "
						+ "product_description, "
						+ "project_value, "
						+ "project_description, "
						+ "bpartner_value, "
						+ "bpartner_description, "
						+ "salesregion, "
						+ "tablename, "
						+ "record_id "
						
						+ " order by dateacct");
		
		
	}


	public StringBuilder getSqlDocumentAcct() {

		return new StringBuilder().append("select "

		+ "1 as row," + "null as fact_acct_id," + "clientname," + "orgname,"
				+ "account_id," + "account_value," + "account_name,"
				+ "null as datetrx," + "f.dateacct,"
				+ "round(sum(amtacctdr),2) as amtacctdr,"
				+ "round(sum(amtacctcr),2) as amtacctcr,"
				+ "round(sum(amtacctdr-amtacctcr),2) as current_balance,"
				+ "null as product_value," + "null as product_description,"
				+ "project_value," + "project_description," + "bpartner_value,"
				+ "bpartner_description," + "null as description,"
				+ "salesregion," + "tablename," + "record_id "

				+ "from pat_facourse f "


				+ "where f.tablename = ? "

				+ "and f.record_id = ? "

				+ "and clientname = ? "

				+ "and orgname = ? "

				+ "group by account_value," + "clientname," + "orgname,"
				+ "account_id," + "account_value," + "account_name,"
				+ "f.dateacct," + "project_value," + "project_description,"
				+ "bpartner_value," + "bpartner_description," + "salesregion,"
				+ "tablename," + "record_id"
				
				+ " order by account_value");

	}

	public StringBuilder getSqlDocumentAcctBalance() {

		return new StringBuilder().append("select " + "null as row, "
				+ "null as fact_acct_id, " + "null as clientname, "
				+ "null as orgname, " + "null as account_id, "
				+ "null as account_value, " + "null as account_name, "
				+ "null as datetrx," + "null as dateacct,"
				+ "round(sum(amtacctdr),2)," + "round(sum(amtacctcr),2),"
				+ "sum(round(amtacctdr-amtacctcr,2)), "
				+ "null as product_value, " + "null as product_description, "
				+ "null as project_value, " + "null as project_description, "
				+ "null as bpartner_value, " + "null as bpartner_description, "
				+ "null as salesregion, " + "null as tablename, "
				+ "null as record_id "

				+ "from pat_facourse f "

				+ "where f.tablename = ? "

				+ "and f.record_id = ? "

				+ "and clientname = ? "

				+ "and orgname = ? "
				
				+ "group by row");

	}
	
	public StringBuilder getSqlAccountsOverView() {

		return new StringBuilder()
				.append("select "

						+ "null as row, "
						+ "null as fact_acct_id, "
						+ "(select name from ad_client where ad_client_id=f.ad_client_id) as clientname, "
						+ "(select name from ad_org where ad_org_id =f.ad_org_id) as orgname, "
						+ "account_id, "
						+ "(select value from c_elementvalue e where e.c_elementvalue_id = f.account_id) as account_value, "
						+ "(select name from c_elementvalue e where e.c_elementvalue_id = f.account_id) as account_name, "
						+ "null as datetrx, "
						+ "null as dateacct, "
						+ "round(sum(f.amtacctdr),2) as amtacctdr, "
						+ "round(sum(f.amtacctcr),2) as amtacctcr, "
						+ "round(sum(f.amtacctdr-f.amtacctcr),2) as current_balance, "
						+ "null as product_value, "
						+ "null as product_description, "
						+ "null as project_value, "
						+ "null as project_description, "
						+ "null as bpartner_value, "
						+ "null as bpartner_description, "
						+ "null as salesregion, "
						+ "null as tablename, "
						+ "null as record_id "

						+ "from fact_acct f "

						+ "where f.account_id in (select account_id from fact_acct where ad_client_id ="
						+ Env.getAD_Client_ID(Env.getCtx())
						+ " )"
						+ " and dateacct between ? and ? "

						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? ) "

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "

						+ " and f.c_acctschema_id = ? "
						
						+ "group by "
						+ "f.account_id,f.ad_client_id,f.ad_org_id "

						+ "order by (select value from c_elementvalue e where e.c_elementvalue_id = f.account_id)");

	}

	public StringBuilder getSqlAccountsOverViewBalance() {

		return new StringBuilder()
				.append("select "
						+ "null as row, "
						+ "null as fact_acct_id, "
						+ "null as clientname, "
						+ "null as orgname, "
						+ "null as account_id, "
						+ "null as account_value, "
						+ "null as account_name, "
						+ "null as datetrx, "
						+ "null as dateacct, "
						+ "round(sum(f.amtacctdr),2) as amtacctdr, "
						+ "round(sum(f.amtacctcr),2) as amtacctcr, "
						+ "round(sum(f.amtacctdr-f.amtacctcr),2) as current_balance, "
						+ "null as product_value, "
						+ "null as product_description, "
						+ "null as project_value, "
						+ "null as project_description, "
						+ "null as bpartner_value, "
						+ "null as bpartner_description, "
						+ "null as salesregion, "
						+ "null as tablename, "
						+ "null as record_id "

						+ "from fact_acct f "

						+ "where f.account_id in (select account_id from fact_acct where ad_client_id ="
						+ Env.getAD_Client_ID(Env.getCtx())
						+ " )"
						+ " and dateacct between ? and ? "

						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? ) "

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "

						+ " and c_acctschema_id = ? "

						+ "group by " + "row");

	}

	public StringBuilder getSqlAccountOverView() {

		return new StringBuilder().append("select "

				+ "row, " 
				+ "null as fact_acct_id, " 
				+ "clientname, " 
				+ "orgname, "
				+ "account_id, " 
				+ "account_value, " 
				+ "account_name, "
				+ "datetrx," 
				+ "dateacct,"
				+ "round(amtacctdr,2) as amtacctdr, "
				+ "round(amtacctcr,2) as amtacctcr, "
				+ "round(amtacctdr-amtacctcr,2) as current_balance, "
				+ "product_value, " 
				+ "null as product_description, "
				+ "project_value, " 
				+ "null as project_description, "
				+ "bpartner_value, " 
				+ "null as bpartner_description, "
				+ "salesregion, " 
				+ "tablename, " 
				+ "null as record_id "

				+ "from pat_facourse "

				+ "where account_value = ? "

				+ "and dateacct between ? and ? "

				+ "and clientname = ? "

				+ "and orgname = ? "

				+ " and c_acctschema_id = ? "

				+ "group by " + "row, " + "clientname, " + "orgname, "
				+ "account_id, " + "account_value, " + "account_name, "
				+ "amtacctdr, " + "amtacctcr, " + "datetrx, " + "dateacct, "
				+ "product_value, " + "project_value, " + "bpartner_value, "
				+ "salesregion, " + "tablename "

				+ "order by dateacct ");

	}

	public StringBuilder getSqlAccountOverViewBalance() {

		return new StringBuilder().append("select "

		+ "null as row, " + "null as fact_acct_id, " + "null as clientname, "
				+ "null as orgname, " + "null as account_id, "
				+ "null as account_value, " + "null as account_name, "
				+ "null as datetrx," + "null as dateacct,"
				+ "round(sum(amtacctdr),2)," + "round(sum(amtacctcr),2),"
				+ "round(sum(amtacctdr-amtacctcr),2), "
				+ "null as product_value, " + "null as product_description, "
				+ "null as project_value, " + "null as project_description, "
				+ "null as bpartner_value, " + "null as bpartner_description, "
				+ "null as salesregion, " + "null as tablename, "
				+ "null as record_id "

				+ "from pat_facourse f "

				+ "where account_value = ? "

				+ "and dateacct between ? and ? "

				+ "and clientname = ? "

				+ "and orgname = ? "

				+ " and c_acctschema_id = ? "

				+ "group by " + "row");

	}
	


	public StringBuilder getSqlBalanceOfAccountsList(Timestamp dateFrom, Timestamp dateTo) {

		
		return new StringBuilder()
		
				.append("select "
						+ "null as row, " 
						+ "null as fact_acct_id, " 
						+ "(select name from ad_client where ad_client_id=f.ad_client_id) as clientname, "
						+ "(select name from ad_org where ad_org_id =f.ad_org_id) as orgname, "
						+ "account_id, " 
						+ " (select value from c_elementvalue where c_elementvalue_id=f.account_id) as value,"
						+ " (select name from c_elementvalue where c_elementvalue_id=f.account_id) as name,"
						+ "null as datetrx," 
						+ " (select sum(amtacctdr-amtacctcr) from fact_acct f2 where f2.dateacct < " + "'" + dateFormat.format(dateFrom)  + "'" + " and f2.account_id=f.account_id)::numeric as balance_carried_forward,"
						+ " (select sum(f2.amtacctdr) from fact_acct f2 where f2.dateacct between  " + "'" + dateFormat.format(dateFrom)  + "'" + " and  " + "'" + dateFormat.format(dateTo)  + "'" + " and f2.account_id=f.account_id)::numeric as debit,"
						+ " (select sum(f2.amtacctcr) from fact_acct f2 where f2.dateacct between  " + "'" + dateFormat.format(dateFrom)  + "'" + " and  " + "'" + dateFormat.format(dateTo)  + "'" + " and f2.account_id=f.account_id)::numeric as credit,"
						+ " (select sum(f2.amtacctdr-f2.amtacctcr) from fact_acct f2 where f2.dateacct between  " + "'" + dateFormat.format(dateFrom)  + "'" + " and  " + "'" + dateFormat.format(dateTo)  + "'" + " and f2.account_id=f.account_id)::numeric as current_balance,"
						+ " (select sum(f2.amtacctdr-f2.amtacctcr) from fact_acct f2 where f2.dateacct <=  " + "'" + dateFormat.format(dateTo)  + "'" + " and f2.account_id=f.account_id)::numeric as end_balance,"
						+ "null as product_description, "
						+ "null as project_value, " 
						+ "null as project_description, "
						+ "null as bpartner_value, " 
						+ "null as bpartner_description, "
						+ "null as salesregion, " 
						+ "null as tablename, " 
						+ "null as record_id "
						
						+ " from fact_acct f"

						+ " where f.account_id in (select f2.account_id from fact_acct f2 where f2.ad_client_id = " + Env.getAD_Client_ID(Env.getCtx()) + " group by f2.account_id)"

						+ " and f.dateacct between ? and ? "

						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? )"

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "
						
						+ " and c_acctschema_id = ? "

						+ " group by account_id, ad_client_id, ad_org_id "
						
						+ " order by (select value from c_elementvalue where c_elementvalue_id=f.account_id)");
		
	}
}
