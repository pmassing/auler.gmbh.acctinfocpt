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

package de.aulerlichtkabel.acctinfocpt.component;

import java.util.logging.Level;

import org.adempiere.webui.factory.DefaultFormFactory;
import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.util.CLogger;

public class PAT_DocFormFactory implements IFormFactory {

	private final CLogger log = CLogger.getCLogger(DefaultFormFactory.class);

	@Override
	public ADForm newFormInstance(String formName) {

		if (formName.startsWith("de.aulerlichtkabel.acctinfocpt.froms")) {

			Object form = null;
			Class<?> clazz = null;
			ClassLoader loader = Thread.currentThread().getContextClassLoader();

			if (loader != null) {
				try {
					clazz = loader.loadClass(formName);
				} catch (Exception e) {
					if (log.isLoggable(Level.INFO))
						log.log(Level.INFO, e.getLocalizedMessage(), e);
				}
			}

			if (clazz == null) {
				loader = this.getClass().getClassLoader();
				try {
					// Create instance w/o parameters
					clazz = loader.loadClass(formName);
				} catch (Exception e) {
					if (log.isLoggable(Level.INFO))
						log.log(Level.INFO, e.getLocalizedMessage(), e);
				}
			}

			if (clazz != null) {
				try {
					form = clazz.newInstance();
				} catch (Exception e) {
					if (log.isLoggable(Level.WARNING))
						log.log(Level.WARNING, e.getLocalizedMessage(), e);
				}
			}

			if (form != null) {
				if (form instanceof ADForm) {
					return (ADForm) form;
				} else if (form instanceof IFormController) {
					IFormController controller = (IFormController) form;
					ADForm adForm = controller.getForm();
					adForm.setICustomForm(controller);
					return adForm;
				}
			}

		}

		if (log.isLoggable(Level.INFO))
			log.info(formName + " not found at extension registry and classpath");

		return null;
	}

}
