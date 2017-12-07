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


package de.aulerlichtkabel.acctinfocpt.data;


import java.util.Map;

import org.compiere.util.Env;


public class PAT_Sqls {
	
	
	public PAT_Sqls() {
		super();
	}

	public StringBuilder getSqlOnYear(Map<String, Object> params) {

		StringBuilder sql = new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"
						+ "null as datetrx,"
						+ "(date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'YY'::character varying)"
						+ "::timestamp with time zone) + 12::double precision * '1 mon'::interval - 1::numeric)::timestamp "
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

						+ " left join c_period p on p.c_period_id = pat_facourse.c_period_id and p.ad_client_id = pat_facourse.ad_client_id "

						+ " where account_value between ? and ? "

						+ " and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "' "

						+ " group by "

						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"

						+ "date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'YY'::character varying)"
						+ "::timestamp with time zone) + 12::double precision * '1 mon'::interval - 1::numeric "

						+ "Order by " + "account_value");

		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf("and orgname = ? "), sql.indexOf("and orgname = ? ")+("and orgname = ? ").length());

		return sql;

	}

	public StringBuilder getSqlOnMonth(Map<String, Object> params) {

		StringBuilder sql = new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"
						+ "null as datetrx,"
						+ "(date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'MM'::character varying)"
						+ "::timestamp with time zone) + 1::double precision * '1 mon'::interval - 1::numeric)::timestamp "
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

						+ " left join c_period p on p.c_period_id = pat_facourse.c_period_id and p.ad_client_id = pat_facourse.ad_client_id "

						+ " where account_value between ? and ?  and dateacct between ? and ? "

						+ " and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"

						+ " group by "

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

		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf("and orgname = ? "), sql.indexOf("and orgname = ? ")+("and orgname = ? ").length());

		return sql;

	}

	public StringBuilder getSqlOnDay(Map<String, Object> params) {

		StringBuilder sql = new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"
						+ "null as datetrx, "
						+ "firstof(dateacct::timestamp with time zone, 'DD'::character varying)::timestamp AS dateacct,"
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

						+ " left join c_period p on p.c_period_id = pat_facourse.c_period_id and p.ad_client_id = pat_facourse.ad_client_id "

						+ " where account_value between ? and ?  and dateacct between ? and ? "

						+ " and clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

					+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"

					+ " group by "

						+ "clientname,"
						+ "orgname,"
						+ "account_id,"
						+ "account_value,"
						+ "account_name,"

						+ "firstof(dateacct::timestamp with time zone, 'DD'::character varying) "

						+ "Order by "
						
						+ "firstof(min(dateacct)::timestamp with time zone, 'DD'::character varying) ");

		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf("and orgname = ? "), sql.indexOf("and orgname = ? ")+("and orgname = ? ").length());

		return sql;

	}

	public StringBuilder getSqlAccountCourse(Map<String, Object> params) {

		StringBuilder sql =  new StringBuilder()
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
						
						+ "from pat_accountcourse(?,?,?,?," + "'" + params.get("isWithAdjustmentPeriod").toString() + "'" + ") " 
						
						+ "where "

						+ " clientname = ? "

						+ " and orgname = ? "

						+ " and c_acctschema_id = ? "

						+ " group by " 
				
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
		
		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf("and orgname = ? "), sql.indexOf("and orgname = ? ")+("and orgname = ? ").length());

		return sql;
		
	}


	public StringBuilder getSqlDocumentAcct() {

		return new StringBuilder().append("select "

				+ "1 as row," 
				+ "null as fact_acct_id," 
				+ "clientname," 
				+ "orgname,"
				+ "account_id," 
				+ "account_value," 
				+ "account_name,"
				+ "null as datetrx," 
				+ "f.dateacct,"
				+ "round(sum(amtacctdr),2) as amtacctdr,"
				+ "round(sum(amtacctcr),2) as amtacctcr,"
				+ "round(sum(amtacctdr-amtacctcr),2) as current_balance,"
				+ "null as product_value," 
				+ "null as product_description,"
				+ "null as project_value," 
				+ "null as project_description," 
				+ "null as bpartner_value,"
				+ "null as bpartner_description," 
				+ "null as description,"
				+ "null as salesregion," 
				+ "null as tablename," 
				+ "null as record_id "

				+ "from pat_facourse f "


				+ "where f.tablename = ? "

				+ "and f.record_id = ? "

				+ "and clientname = ? "

				+ "and orgname = ? "

				+ "group by account_value," + "clientname," + "orgname,"
				+ "account_id," + "account_value," + "account_name,"
				+ "f.dateacct, f.record_id "
				
				+ " order by account_value");

	}
	
	public StringBuilder getSqlAccountsOverView(Map<String, Object> params) {

		StringBuilder sql =  new StringBuilder()
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

						+ " left join c_period p on p.c_period_id = f.c_period_id and p.ad_client_id = f.ad_client_id "

						+ " where f.account_id in "
						+ "       (select account_id from fact_acct where ad_client_id ="
						+           Env.getAD_Client_ID(Env.getCtx())
						+ "       )"
						
						+ " and dateacct between ? and ? "

						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? ) "

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "

						+ " and f.c_acctschema_id = ? "
						
						+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"

						+ "group by "
						
						+ "f.account_id,f.ad_client_id,f.ad_org_id "

						+ "order by (select value from c_elementvalue e where e.c_elementvalue_id = f.account_id)");

		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "), sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ")+(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ").length());

		return sql;
		
	}

	public StringBuilder getSqlAccountOverView(Map<String, Object> params) {

		StringBuilder sql = new StringBuilder().append("select "

				+ "null as row, " 
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
				+ "record_id "

				+ " from pat_facourse "

				+ " left join c_period p on p.c_period_id = pat_facourse.c_period_id and p.ad_client_id = pat_facourse.ad_client_id "

				+ " where account_value = ? "

				+ " and dateacct between ? and ? "

				+ " and clientname = ? "

				+ " and orgname = ? "

				+ " and c_acctschema_id = ? "

				+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"

				+ " order by dateacct ");
				
		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf("and orgname = ? "), sql.indexOf("and orgname = ? ")+("and orgname = ? ").length());

		return sql;
		
	}

	public StringBuilder getSqlBalanceOfAccountsList(Map<String, Object> params) {

		StringBuilder sql =  new StringBuilder()
		
				.append("select "
						+ "null as row, " 
						+ "null as fact_acct_id, " 
						+ "(select name from ad_client where ad_client_id=f.ad_client_id) as clientname, "
						+ "(select name from ad_org where ad_org_id =f.ad_org_id) as orgname, "
						+ "account_id, " 
						+ " (select value from c_elementvalue where c_elementvalue_id=f.account_id) as value,"
						+ " (select name from c_elementvalue where c_elementvalue_id=f.account_id) as name,"
						+ "null as datetrx," 
						
						+ " ("
						+ "    select sum(amtacctdr-amtacctcr) "
						+ "      from fact_acct f2"		
						+ "       left join c_period p2 on p2.c_period_id = f2.c_period_id and p2.ad_client_id = f2.ad_client_id"						
						+ "      where f2.dateacct < " + "'" + params.get("dateFrom")  + "'" 
						+ "            and f2.account_id=f.account_id and f2.ad_org_id = f.ad_org_id "
						+ "            and p2.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"
						+ " )::numeric as balance_carried_forward,"

						
						+ " ("
						+ "    select sum(f2.amtacctdr) "
						+ "      from fact_acct f2"
						+ "       left join c_period p2 on p2.c_period_id = f2.c_period_id and p2.ad_client_id = f2.ad_client_id"												
						+ "      where f2.dateacct between  " + "'" + params.get("dateFrom")  + "'" + " and  " + "'" + params.get("dateTo")  + "'" 
						+ "            and f2.account_id=f.account_id and f2.ad_org_id = f.ad_org_id"
						+ "            and p2.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"
						+ " )::numeric as debit,"
						
						
						+ " (select sum(f2.amtacctcr) from fact_acct f2"
						+ "       left join c_period p2 on p2.c_period_id = f2.c_period_id and p2.ad_client_id = f2.ad_client_id"												
						+ "      where f2.dateacct between  " + "'" + params.get("dateFrom")  + "'" + " and  " + "'" + params.get("dateTo")  + "'" 
						+ "            and f2.account_id=f.account_id and f2.ad_org_id = f.ad_org_id"
						+ "            and p2.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"
						+ " )::numeric as credit,"

						
						+ " (select sum(f2.amtacctdr-f2.amtacctcr) from fact_acct f2"
						+ "       left join c_period p2 on p2.c_period_id = f2.c_period_id and p2.ad_client_id = f2.ad_client_id"												
						+ "      where f2.dateacct between  " + "'" + params.get("dateFrom")  + "'" + " and  " + "'" + params.get("dateTo")  + "'" 
						+ "            and f2.account_id=f.account_id and f2.ad_org_id = f.ad_org_id"
						+ "            and p2.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"
						+ " )::numeric as current_balance,"

						
						+ " (select sum(f2.amtacctdr-f2.amtacctcr) from fact_acct f2"
						+ "       left join c_period p2 on p2.c_period_id = f2.c_period_id and p2.ad_client_id = f2.ad_client_id"												
						+ "      where f2.dateacct <=  " + "'" + params.get("dateTo")  + "'" 
						+ "            and f2.account_id=f.account_id and f2.ad_org_id = f.ad_org_id"
						+ "            and p2.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"
						+ " )::numeric as end_balance,"

						
						+ "null as product_description, "
						+ "null as project_value, " 
						+ "null as project_description, "
						+ "null as bpartner_value, " 
						+ "null as bpartner_description, "
						+ "null as salesregion, " 
						+ "null as tablename, " 
						+ "null as record_id "
						
						+ " from fact_acct f "
						
						+ " left join c_period p on p.c_period_id = f.c_period_id and p.ad_client_id = f.ad_client_id"

						+ " where f.account_id in (select f2.account_id from fact_acct f2 where f2.ad_client_id = " + Env.getAD_Client_ID(Env.getCtx()) + " group by f2.account_id)"

						+ " and f.dateacct between ? and ? "

						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? )"

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "
						
						+ " and f.c_acctschema_id = ? "
						
						+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "'"
						
						+ " group by account_id, f.ad_client_id, f.ad_org_id "
						
						+ " order by (select value from c_elementvalue where c_elementvalue_id=f.account_id)");
		
		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "), sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ")+(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ").length());

		return sql;

	}

	public StringBuilder getSqlTreeOnYear(Map<String, Object> params) {

		StringBuilder sql = new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "null as clientname,"
						+ "null as orgname,"
						+ "null as account_id,"
						+ "null as account_value,"
						+ "null as account_name,"
						+ "null as datetrx,"
						+ "(date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'YY'::character varying)"
						+ "::timestamp with time zone) + 12::double precision * '1 mon'::interval - 1::numeric)::timestamp "
						+ "AS dateacct,"
						+ "null as amtacctdr,"
						+ "null as amtacctcr,"
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

						+ "from fact_acct f "

						+ " left join c_period p on p.c_period_id = f.c_period_id and p.ad_client_id = f.ad_client_id "

						+ " where " + params.get("treeSummary")

						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? )"

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "

						+ " and c_acctschema_id = ? "

						+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "' "

						+ " group by "

						+ "clientname,"
						+ "orgname,"

						+ "date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'YY'::character varying)"
						+ "::timestamp with time zone) + 12::double precision * '1 mon'::interval - 1::numeric "

						+ "Order by "
						
						+ "date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'YY'::character varying)"
						+ "::timestamp with time zone) + 12::double precision * '1 mon'::interval - 1::numeric "

						);

		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "), sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ")+(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ").length());

		return sql;

	}
	
	public StringBuilder getSqlTreeOnMonth(Map<String, Object> params) {

		StringBuilder sql = new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "null as clientname,"
						+ "null as orgname,"
						+ "null as account_id,"
						+ "null as account_value,"
						+ "null as account_name,"
						+ "null as datetrx,"
						+ "(date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'MM'::character varying)"
						+ "::timestamp with time zone) + 1::double precision * '1 mon'::interval - 1::numeric)::timestamp "
						+ "AS dateacct,"
						+ "null as amtacctdr,"
						+ "null as amtacctcr,"
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

						+ "from fact_acct f "

						+ " left join c_period p on p.c_period_id = f.c_period_id and p.ad_client_id = f.ad_client_id "

						+ " where " + params.get("treeSummary")

						+ " and dateacct between ? and ? "
						
						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? )"

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "

						+ " and c_acctschema_id = ? "

						+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "' "

						+ " group by "

						+ "clientname,"
						+ "orgname,"

						+ "(date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'MM'::character varying)"
						+ "::timestamp with time zone) + 1::double precision * '1 mon'::interval - 1::numeric)::timestamp "



						+ "Order by "
						
						+ "(date_trunc('month'::text, "
						+ "firstof(dateacct::timestamp with time zone, 'MM'::character varying)"
						+ "::timestamp with time zone) + 1::double precision * '1 mon'::interval - 1::numeric)::timestamp "

						);

		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "), sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ")+(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ").length());

		return sql;

	}

	public StringBuilder getSqlTreeOnDay(Map<String, Object> params) {

		StringBuilder sql = new StringBuilder()
				.append("select "
						+ "1 as row,"
						+ "null as fact_acct_id,"
						+ "null as clientname,"
						+ "null as orgname,"
						+ "null as account_id,"
						+ "null as account_value,"
						+ "null as account_name,"
						+ "null as datetrx,"
						+ "firstof(dateacct::timestamp with time zone, 'DD'::character varying)::timestamp AS dateacct,"
						+ "null as amtacctdr,"
						+ "null as amtacctcr,"
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

						+ "from fact_acct f "

						+ " left join c_period p on p.c_period_id = f.c_period_id and p.ad_client_id = f.ad_client_id "

						+ " where " + params.get("treeSummary")

						+ " and dateacct between ? and ? "
						
						+ " and f.ad_client_id = (select ad_client_id from ad_client where name= ? )"

						+ " and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "

						+ " and c_acctschema_id = ? "

						+ " and p.periodtype like " + "'" + params.get("isWithAdjustmentPeriod").toString() + "' "

						+ " group by "

						+ "clientname,"
						+ "orgname,"

						+ "firstof(dateacct::timestamp with time zone, 'DD'::character varying)::timestamp "

						+ "Order by "
						
						+ "firstof(min(dateacct)::timestamp with time zone, 'DD'::character varying)::timestamp "

						);

		if(((String) params.get("organisation")).equals("*"))
			sql.delete(sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) "), sql.indexOf(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ")+(" and f.ad_org_id = (select ad_org_id from ad_org where name= ? ) ").length());

		return sql;

	}
	
	public StringBuilder getSQLAccountIDsInFactAcct(){
		
		return new StringBuilder()
		.append("select account_id from fact_acct where ad_client_id =")
		.append(Env.getAD_Client_ID(Env.getCtx()))
		.append(" group by account_id");
		
	}
}
