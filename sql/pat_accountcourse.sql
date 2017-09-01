-- Function: pat_accountcourse(character varying, character varying, date, date)

-- DROP FUNCTION pat_accountcourse(character varying, character varying, date, date, character varying);

CREATE OR REPLACE FUNCTION pat_accountcourse(accountfrom character varying, accountto character varying, datefrom date, dateto date, periodtype character varying)
  RETURNS SETOF pat_facourse AS
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
    						and p.periodtype like periodtype;

    SELECT INTO count count(fact_acct_id) from  pat_facourse fac 
    
    				left join c_period p on p.c_period_id = fac.c_period_id and p.ad_client_id = fac.ad_client_id

    				where account_value=accountfrom 
                    		and account_value=accountto 
                    		and dateacct>=datefrom 
                    		and dateacct<=dateto
    						and p.periodtype like periodtype;


                    		
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
     						and p.periodtype like periodtype
                   
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
