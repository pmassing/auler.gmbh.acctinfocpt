# ReadMe #

** Plugin-Name: AccountInfoCockpit for iDempiere **
** Plug-in Version: 1.0**
** Status: Testing **
** Licence: GPLv2 **
** Author: Patric Maßing (Hans Auler GmbH), Germany, pmassing(at)aulerlichtkabel.de**
** Date: 2017 **
** Required environment: iDempiere with PostgreSQL-Database **


** This Plug-in is distributed in the hope that it will be useful, **
** but WITHOUT ANY WARRANTY; without even the implied warranty of **
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the **
** GNU General Public License for more details. **

*ToDo: german translation*
*ToDo: better performance and keep resources by using TreeSummary,*
* target is always to get the result in seconds*

*What is New:* 
*New parameter periodtype*
*Own icons for the toolbar, created by myself*
*TreeSummary*
*Export as html*
*Export as text - column delimiter is tab*
*Zoom functionality for the accountview result*


** Description **
This plugin provides a custom form to display some special accounting information.

My decision was to create a custom form to have the full control over the functionality

and to extend the functionality step by step.


** Installation **

1. Install the plug-in 

2. In the sql-folder are two files to install in the db.

 2.1 pat_facource.sql -> Is the needed View.

 2.2 pat_accountcourse.sql -> The function for AccountCourse
 
  
** AccountCourse **
Shows the current balance up to date at this line for the defined Account in the

defined date range. The beginning balance is on the first line showed. The 

ending balance is on the last Account posting line.


** Summary **
Shows the balance per year, month or day.


** Accountsoverview **
Displays a overview to all accounts from the COA with the current balance.

A date range can be defined in different kinds.

1. DateFrom empty and only DateTo is filled.

2. DateFrom and DateTo are inserted.

Accountvalues are not needed.

*Can be time critical if there are many data!*
 

** Accountoverview **
Displays a overview to the defined account with the current balance.

A date range can be defined in different kinds.

1. DateFrom empty and only DateTo is filled.

2. DateFrom and DateTo are inserted.



** SummaryAccountDocument **
In the most cases each product generate a separate factline. If you

have 100 positions in your invoice as example, then the view of the 

accounting for a document can be a little bit confusing. This view 

show the posting as summary per account.




** BalanceOfAccountsList **
List of all accounts in factacct with the balance for each account in the defined

time range. Also the balance before defined the time range and the ending balance.
*Can be time critical if there are many data!*


** TreeSummary **
Displays a matrix of accounts(summary-entries) in horizontal representation. Balances 

per year, month and day.
*Can be time critical if there are many data!*


** Export as html **
Export the result as a html-file. To print the results as example or to store the results.


** Export as text **
Export the result as a txt-file, tab as delimiter and linebreak with \n.

So it is possible to import the file or copy and paste the information in your 

favorite spreadsheet program.


**Zoom**
In the accountview it is now possible to zoom to the document of the selected

fact_acct. 


** Planed extensions **
BPartner open to defined date. What is open at time X? 

Automatic Accounting Checks - tax as example.

BPartnerCource

Show a bar graph for the accountcourse to make it easier to see deviations.




 
