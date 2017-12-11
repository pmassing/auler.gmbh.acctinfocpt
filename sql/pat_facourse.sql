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
 * with this plug-in; If not, see <http://www.gnu.org/licenses/>.              *
 *****************************************************************************/

/**
 * This View is a component to the Plugin AccountInfoCockpit
 * Plugin AccountInfoCockpit
 * 
 * @author Patric Maßing (Hans Auler GmbH)
 * 2017
 */


--drop view pat_facourse cascade;
create or replace view pat_facourse as

select 
       1 as row,
       fa.fact_acct_id,
       cl.name as clientname,
       org.name as orgname,       
       fa.account_id,
       ev.value as account_value, 
       ev.name as account_name, 
       fa.datetrx, 
       fa.dateacct, 
       COALESCE(fa.amtacctdr,0.00) as amtacctdr,
       COALESCE(fa.amtacctcr,0.00) as amtacctcr, 
       cast (0.00 as numeric) as current_balance, 
       pr.value as product_value,
       pr.description as product_description,
       proj.value as project_value,
       proj.description as project_description,
       bp.value as bpartner_value,
       bp.name as bpartner_description,       
       fa.description,
       sr.name as salesregion,
       tbl.tablename,
       fa.record_id,
       pr.m_product_id,
       bp.c_bpartner_id,
       sr.c_salesregion_id,
       proj.c_project_id,
       fa.c_acctschema_id,
       fa.ad_client_id,
       fa.c_period_id
       
       
       
  from fact_acct fa

	left join ad_client cl on cl.ad_client_id = fa.ad_client_id
	left join ad_org org on org.ad_org_id = fa.ad_org_id
	left join c_elementvalue ev on ev.c_elementvalue_id = fa.account_id
        left join m_product pr on pr.m_product_id=fa.m_product_id
	left join c_project proj on proj.c_project_id=fa.c_project_id
	left join c_bpartner bp on bp.c_bpartner_id=fa.c_bpartner_id
	left join c_salesregion sr on sr.c_salesregion_id=fa.c_salesregion_id
	left join ad_table tbl on tbl.ad_table_id=fa.ad_table_id


  group by
	cl.name,
	org.name,
	fa.fact_acct_id,
	fa.account_id,
	ev.value,
	ev.name,
	pr.value,
	pr.description,
	proj.value,
	proj.description,
	bp.value,
	bp.name,
	sr.name,
	tbl.tablename,
    pr.m_product_id,
    bp.c_bpartner_id,
    sr.c_salesregion_id,
    proj.c_project_id,
    fa.c_acctschema_id   

	
  order by fa.account_id, fa.dateacct, fa.fact_acct_id;