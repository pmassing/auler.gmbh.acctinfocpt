--drop FUNCTION pat_balanceofaccountslist(character varying, date, date); 
create or replace function pat_balanceofaccountslist(pdatefrom date, pdateto date)
returns setof pat_facourse as
$BODY$
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
 * This Function is a component to the Plug-in AccountInfoCockpit
 * Plugin AccountInfoCockpit
 * 
 * @author Patric Maßing (Hans Auler GmbH)
 * 2017
 * 
 *  
 */
declare

 entrys pat_facourse%ROWTYPE; 
 accountvalue varchar;
 balance_carried_forward numeric; 
 debit numeric;
 credit numeric;
 balance numeric;
 end_balance numeric;
 accountval varchar;

BEGIN

	FOR accountval IN select value from c_elementvalue loop
   
     entrys.account_value := accountval;
    
     SELECT INTO entrys.balance_carried_forward (COALESCE(sum(pfa.amtacctdr),0.00)- COALESCE(sum(pfa.amtacctcr),0.00)) FROM pat_facourse pfa
       WHERE pfa.dateacct<pdatefrom AND pfa.account_value=accountval;

     SELECT INTO entrys.amtacctdr COALESCE(sum(pfa.amtacctdr),0.00) FROM pat_facourse pfa
       WHERE pfa.dateacct>=pdatefrom AND pfa.dateacct<=pdateto AND pfa.account_value=accountval;

     SELECT INTO entrys.amtacctcr COALESCE(sum(pfa.amtacctcr),0.00) FROM pat_facourse pfa
       WHERE pfa.dateacct>=pdatefrom AND pfa.dateacct<=pdateto AND pfa.account_value=accountval;
                
     SELECT INTO entrys.current_balance (COALESCE(sum(pfa.amtacctdr),0.00)- COALESCE(sum(pfa.amtacctcr),0.00)) FROM pat_facourse pfa
       WHERE pfa.dateacct>=pdatefrom AND pfa.dateacct<=pdateto AND pfa.account_value=accountval;

     SELECT INTO entrys.end_balance (COALESCE(sum(pfa.amtacctdr),0.00)- COALESCE(sum(pfa.amtacctcr),0.00)) FROM pat_facourse pfa
       WHERE pfa.dateacct<=pdateto AND pfa.account_value=accountval;

     return next entrys;

  END LOOP;
  
END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
ALTER FUNCTION pat_balanceofaccountslist(date, date)
  OWNER TO adempiere;
