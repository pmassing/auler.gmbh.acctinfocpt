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


package de.aulerlichtkabel.acctinfocpt.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.zkoss.zul.Filedownload;


public class PAT_ExportAsHtml {
	
	final String ISNUMERIC = "<N>";
	
	final String FILE_SUFFIX = ".html";
    
	final String HTMLDEF = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n\"http://www.w3.org/TR/html4/loose.dtd\">";
    
	final String HTML = "<html>";    
      
	final String HEAD = "<head>";
	final String HEADC = "</head>";
	final String TITLE = "<title>";
	final String TITLEC = "</title>";
	final String BODY = "<body>";
	final String BODYC = "</body>";
	final String HR = "<hr>";
	final String HRC = "</hr>";
	final String TABLE = "<table>";
	final String TABLEB1 = "<table border=1>";
	final String TABLEC = "</table>";
	final String TR = "<tr>";
	final String TRC = "</tr>";
	final String TD = "<td>";
	final String TDR = "<td align=\"right\">";
	final String TDC = "</td>";
	final String B = "<b>";
	final String BC = "</b>";
	final String TFOOT = "<tfoot>";
	final String TFOOTC = "</tfoot>";
       
    String title = null;
    List<String> docContentHeader;
    List<String> columnHeadNames;
    List<List<String>> columnsList;

	// Format
	DecimalFormat numberFormat = DisplayType.getNumberFormat(DisplayType.Amount);
    
    
	public PAT_ExportAsHtml() {

	}
	
	public String createHTMLHead(){
		
		
		StringBuilder head = new StringBuilder();
		
		head.append(HTMLDEF);
       	
		head.append(HTML);
		
		head.append(HEAD);

		head.append(TITLE);

		head.append(title);
		
		head.append(TITLEC);

		head.append(HEADC);
		
		return head.toString();
		
	}
	
	
	public String createHTMLBody(String docContentHeader, String docContent, String docContentFooter){
		
		
		StringBuilder body = new StringBuilder();
		
		body.append(BODY);

		body.append(docContentHeader);

		body.append(HR);
		
		body.append(docContent);

		body.append(HRC);

		body.append(docContentFooter);
		
		body.append(BODYC);

		return body.toString();
	}
	

	
	public String createHTMLContentHeader (List<String> contentHeader){
		
		StringBuilder docheader = new StringBuilder();

		docheader.append(contentHeader);
		
		return docheader.toString();
		
		
	}	
	
	public String createHTMLContent(String content){
		
		StringBuilder docontent = new StringBuilder();

		docontent.append(content);
		
		return docontent.toString();
	}
	
	
	public String createHTMLContentFooter (List<String> contentFooter){
		
		StringBuilder docfooter = new StringBuilder();

		docfooter.append(contentFooter);
		
		return docfooter.toString();
		
		
	}	
	

	public String createTable(String tableContent){
		
		StringBuilder table = new StringBuilder();

		table.append(TABLEB1);

		table.append(tableContent);
				
		table.append(TABLEC);
		
		return table.toString();
		
	}
	
	public String createTableHeadColumns(List<String> columnHeadNames){
		
		StringBuilder tableHead = new StringBuilder();

		tableHead.append(TR);

		for(String columnHead : columnHeadNames)			
			tableHead.append((columnHead.startsWith(ISNUMERIC)?TDR:TD) + "<b>" + columnHead.replace(ISNUMERIC, "") + "</b>" + TDC);
		
		tableHead.append(TRC);

		return tableHead.toString();

	}
	
	public String createTableRows(List<List<Object>> tableRows){
		
		StringBuilder tr = new StringBuilder();

		for(List<Object> tableRow : tableRows){

			tr.append(TR);

			for(Object column : tableRow){				
				
				if (column instanceof BigDecimal)			
					tr.append(TDR + numberFormat.format(column) + TDC);
				else if (column instanceof Timestamp)
					tr.append(TDR + Env.getLanguage(Env.getCtx()).getDateFormat().format(column) + TDC);
				else if (column instanceof String)
					tr.append(TD + ((String)column) + TDC);
					
			}
			
			tr.append(TRC);
		
		}
		
		return tr.toString();

	}
	
	public String createTableFooter(List<String> columnFooterColumns){
		
		StringBuilder tableFooter = new StringBuilder();

		tableFooter.append(TFOOT);
		tableFooter.append(TR);

		for(String columnFooterColumn : columnFooterColumns)
			tableFooter.append(TD + B + columnFooterColumn+ BC + TDC);
		
		tableFooter.append(TRC);
		tableFooter.append(TFOOTC);
		
		return tableFooter.toString();

	}
	
	
	public void downloadHtmlFile(String filename, String content){
		
		try {
			
			File tempFile = File.createTempFile(filename, FILE_SUFFIX);
			BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(tempFile));

			bof.write(content.getBytes());
			
			bof.flush();
			bof.close();
			
			Filedownload.save(new FileInputStream(tempFile), "plain/text", filename + FILE_SUFFIX);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setDocContentHeader(List<String> docContentHeader){
	
		this.docContentHeader = docContentHeader;
	}

	public void setColumnHeadNames(List<String> columnHeadNames){
		
		this.docContentHeader = columnHeadNames;
	}
	
	public void setColumnList(List<List<String>> columnsList){
		
		this.columnsList = columnsList;
	}

}
