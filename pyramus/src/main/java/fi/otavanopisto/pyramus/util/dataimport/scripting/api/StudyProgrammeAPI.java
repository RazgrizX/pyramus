package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import java.util.ArrayList;
import java.util.List;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StudyProgrammeAPI {

  public StudyProgrammeAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String name, Long categoryId, String code) throws InvalidScriptException {
    StudyProgrammeCategory category = null;
    if (categoryId != null) {
      category = DAOFactory.getInstance().getStudyProgrammeCategoryDAO().findById(categoryId);
      if (category == null) {
        throw new InvalidScriptException("StudyProgrammeCategory #" + categoryId + " not found.");
      }
    }
    
    return (DAOFactory.getInstance().getStudyProgrammeDAO().create(name, category, code).getId());
  }
  
  public Long findIdByCode(String code) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    
    StudyProgramme studyProgramme = studyProgrammeDAO.findByCode(code);
    return studyProgramme != null ? studyProgramme.getId() : null;
  }
  
  public Long[] listIds() {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
        
    List<Long> result = new ArrayList<>();
    
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    for (StudyProgramme studyProgramme : studyProgrammes) {
      result.add(studyProgramme.getId());
    }
    
    return result.toArray(new Long[0]); 
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
