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


public class PAT_ExportAsText {

	final String ISNUMERIC = "<N>";

	final String FILE_SUFFIX = ".txt";  
       
    String title = null;
    List<String> docContentHeader;
    List<String> columnHeadNames;
    List<List<Object>> columnsList;
    
    final String DELIMETER = "\t";
    final String LINEBREAK = "\n";
    
    final String LINE = "--------------------------------------------";
    
	// Format
	DecimalFormat numberFormat = DisplayType.getNumberFormat(DisplayType.Amount);
    
    
	public PAT_ExportAsText() {

	}
	
	
	public String createContent(String Content){
		
		StringBuilder table = new StringBuilder();

		table.append(Content);
						
		return table.toString();
		
	}
	
	public String createTxt(String docContentHeader, String docContent, String docContentFooter){
		
		
		StringBuilder body = new StringBuilder();
		
		body.append(docContentHeader);
		
		body.append(docContent);

		body.append(docContentFooter);
		
		return body.toString();
	}
	

	
	public String createTextContentHeader (List<String> contentHeader){
		
		StringBuilder docheader = new StringBuilder();

		docheader.append(contentHeader);
		
		return docheader.toString();
		
		
	}	
	
	public String createTextContent(String content){
		
		StringBuilder docontent = new StringBuilder();

		docontent.append(content);
		
		return docontent.toString();
	}
	
	
	public String createTextContentFooter (List<String> contentFooter){
		
		StringBuilder docfooter = new StringBuilder();

		docfooter.append(contentFooter);
		
		return docfooter.toString();
		
		
	}	
	
	
	public String createHeadColumns(List<String> columnHeadNames){
		
		StringBuilder tableHead = new StringBuilder();

		for(String columnHead : columnHeadNames){			
			tableHead.append(columnHead.replaceAll(ISNUMERIC, "") + DELIMETER);
		}
		
		tableHead.append(LINEBREAK);

		return tableHead.toString();

	}
	
	public String createTableRows(List<List<Object>> tableRows){
		
		StringBuilder row = new StringBuilder();

		for(List<Object> tableRow : tableRows){

			for(Object column : tableRow){				
				
				if (column instanceof BigDecimal)			
					row.append(numberFormat.format(column));
				else if (column instanceof Timestamp)
					row.append(Env.getLanguage(Env.getCtx()).getDateFormat().format(column));
				else if (column instanceof String)
					row.append(((String)column));

				row.append(DELIMETER);

			}
			
			row.append(LINEBREAK);
		
		}
		
		return row.toString();

	}
	
	public String createTableFooter(List<String> columnFooterColumns){
		
		StringBuilder tableFooter = new StringBuilder();

		tableFooter.append(LINE);
		tableFooter.append(LINEBREAK);

		for(String columnFooterColumn : columnFooterColumns)
			tableFooter.append(columnFooterColumn);
		
		tableFooter.append(LINE);
		tableFooter.append(LINEBREAK);
		
		return tableFooter.toString();

	}
	
	
	public void downloadTxtFile(String filename, String content){
		
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
	
	public void setColumnList(List<List<Object>> columnsList){
		
		this.columnsList = columnsList;
	}

}
