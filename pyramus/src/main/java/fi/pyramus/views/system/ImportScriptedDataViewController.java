package fi.pyramus.views.system;

import java.io.InputStreamReader;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.dataimport.scripting.ScriptedImporter;

public class ImportScriptedDataViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    if ("SUCCESS".equals(requestContext.getString("status"))) {
      requestContext.getRequest().setAttribute("statusMessage", Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.importscripteddata.successMessage"));
    }
    
    requestContext.setIncludeJSP("/templates/system/importscripteddata.jsp");
  }

  
  @Override
  public void processSend(PageRequestContext requestContext) {
    ScriptedImporter dataImporter = new ScriptedImporter();
    try {
      dataImporter.importDataFromReader(requestContext.getLoggedUserId(), new InputStreamReader(requestContext.getFile("file").getInputStream(), "UTF-8"));
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/system/importscripteddata.page?status=SUCCESS");
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
 
  
}