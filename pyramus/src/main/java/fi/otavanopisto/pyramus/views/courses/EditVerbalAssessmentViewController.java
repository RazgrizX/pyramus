package fi.otavanopisto.pyramus.views.courses;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.CreditDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of the Edit Course view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class EditVerbalAssessmentViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CreditDAO creditDAO = DAOFactory.getInstance().getCreditDAO();
    
    Long creditId = pageRequestContext.getLong("creditId");
    Credit credit = creditId != null ? creditDAO.findById(creditId) : null;

    if (credit != null)
      pageRequestContext.getRequest().setAttribute("verbalAssessment", credit.getVerbalAssessment());
      
    pageRequestContext.setIncludeJSP("/templates/courses/editverbalassessment.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing courses is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "courses.editVerbalAssessmentDialog.dialogTitle");
  }

}
