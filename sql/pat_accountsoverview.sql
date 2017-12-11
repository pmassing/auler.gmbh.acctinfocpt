--drop FUNCTION pat_accountsoverview(date, date); 
create or replace function pat_accountsoverview(pdatefrom date, pdateto date)
returns setof pat_facourse as
$BODY$
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
 * This Function is a component to the Plugin AccountInfoCockpit
 * Plugin AccountInfoCockpit
 * 
 * @author Patric Maßing (Hans Auler GmbH)
 * 2017
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
    
/*     SELECT INTO entrys.balance_carried_forward (COALESCE(sum(pfa.amtacctdr),0.00)- COALESCE(sum(pfa.amtacctcr),0.00)) FROM pat_facourse pfa
       WHERE pfa.dateacct<pdatefrom AND pfa.account_value=accountval;
*/
     SELECT INTO entrys.amtacctdr COALESCE(sum(pfa.amtacctdr),0.00) FROM pat_facourse pfa
       WHERE pfa.dateacct>=pdatefrom AND pfa.dateacct<=pdateto AND pfa.account_value=accountval;

     SELECT INTO entrys.amtacctcr COALESCE(sum(pfa.amtacctcr),0.00) FROM pat_facourse pfa
       WHERE pfa.dateacct>=pdatefrom AND pfa.dateacct<=pdateto AND pfa.account_value=accountval;
                
     SELECT INTO entrys.current_balance (COALESCE(sum(pfa.amtacctdr),0.00)- COALESCE(sum(pfa.amtacctcr),0.00)) FROM pat_facourse pfa
       WHERE pfa.dateacct>=pdatefrom AND pfa.dateacct<=pdateto AND pfa.account_value=accountval;

/*     SELECT INTO entrys.end_balance (COALESCE(sum(pfa.amtacctdr),0.00)- COALESCE(sum(pfa.amtacctcr),0.00)) FROM pat_facourse pfa
       WHERE pfa.dateacct<=pdateto AND pfa.account_value=accountval;
*/
     return next entrys;

  END LOOP;
  
END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
ALTER FUNCTION pat_accountsoverview(date, date)
  OWNER TO adempiere;
