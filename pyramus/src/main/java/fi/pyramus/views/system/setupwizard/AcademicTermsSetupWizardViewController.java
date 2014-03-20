package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;

public class AcademicTermsSetupWizardViewController extends SetupWizardController {

  public AcademicTermsSetupWizardViewController() {
    super("academicterms");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();

    int rowCount = requestContext.getInteger("termsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "termsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      Date startDate =  requestContext.getDate(colPrefix + ".startDate");
      Date endDate = requestContext.getDate(colPrefix + ".endDate");
      
      academicTermDAO.create(name, startDate, endDate); 
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    return !academicTermDAO.listAll().isEmpty();
  }

}
