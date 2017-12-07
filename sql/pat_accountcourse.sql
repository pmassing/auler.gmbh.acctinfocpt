-- Function: pat_accountcourse(character varying, character varying, date, date)

-- DROP FUNCTION pat_accountcourse(character varying, character varying, date, date, character varying);

CREATE OR REPLACE FUNCTION pat_accountcourse(accountfrom character varying, accountto character varying, datefrom date, dateto date, period_type character varying)
  RETURNS SETOF pat_facourse AS
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
 entrys pat_FaCourse%ROWTYPE; 
 ent RECORD;
 seq INTEGER;
 sum_AmtAcctdr NUMERIC;
 sum_AmtAcctcr NUMERIC;
 balance NUMERIC;
 currentBalance NUMERIC;

 count NUMERIC;
 
BEGIN
    --select * from pat_accountcourse('3300','3300','10.01.2014','10.01.2014')
    seq=0;
    sum_AmtAcctdr := 0;
    sum_AmtAcctcr := 0;
    balance := 0;
    currentBalance := 0;
    count := 0;
    
    SELECT INTO currentBalance COALESCE(sum(amtacctdr),0.00) - COALESCE(sum(amtacctcr),0.00) from  pat_facourse fac 
                    
    				left join c_period p on p.c_period_id = fac.c_period_id and p.ad_client_id = fac.ad_client_id
    				
    				where account_value=accountfrom 
    						and account_value=accountto 
    						and dateacct<datefrom
    						and p.periodtype like period_type;

    SELECT INTO count count(fact_acct_id) from  pat_facourse fac 
    
    				left join c_period p on p.c_period_id = fac.c_period_id and p.ad_client_id = fac.ad_client_id

    				where account_value=accountfrom 
                    		and account_value=accountto 
                    		and dateacct>=datefrom 
                    		and dateacct<=dateto
    						and p.periodtype like period_type;


                    		
    IF (count > 0) OR (currentBalance > 0) THEN
      entrys.current_balance:= currentBalance;
      entrys.account_value := COALESCE(accountfrom,'');
      entrys.row := count;
      
      return next entrys; 
    END IF;
                
    
    FOR entrys IN SELECT * FROM  pat_facourse fac 
    
    				left join c_period p on p.c_period_id = fac.c_period_id and p.ad_client_id = fac.ad_client_id

    				where account_value=accountfrom 
                    		and account_value=accountto 
                    		and dateacct>=datefrom 
                    		and dateacct<=dateto
     						and p.periodtype like period_type
                   
                    order by fac.account_id, fac.dateacct, fac.fact_acct_id
                    
    loop
        seq=seq+1;
        entrys.row:=seq;
        balance := COALESCE(entrys.amtacctdr,0.00) - COALESCE(entrys.amtacctcr,0.00);
        currentBalance := currentBalance + balance;
        entrys.current_balance:= ROUND(currentBalance,2);

        entrys.account_value := COALESCE(accountfrom,'');
        

        return next entrys; 

    END LOOP;  

END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION pat_accountcourse(character varying, character varying, date, date, character varying)
  OWNER TO adempiere;