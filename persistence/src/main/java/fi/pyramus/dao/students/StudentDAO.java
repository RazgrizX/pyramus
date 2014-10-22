package fi.pyramus.dao.students;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.users.UserVariableKeyDAO;
import fi.pyramus.domainmodel.base.ArchivableEntity;
import fi.pyramus.domainmodel.base.BillingDetails;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.domainmodel.students.Student_;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.UserVariable;
import fi.pyramus.domainmodel.users.UserVariableKey;
import fi.pyramus.domainmodel.users.UserVariable_;
import fi.pyramus.events.StudentArchivedEvent;
import fi.pyramus.events.StudentCreatedEvent;

@Stateless
public class StudentDAO extends PyramusEntityDAO<Student> {
  
  @Inject
  private Event<StudentCreatedEvent> studentCreatedEvent;
  
  @Inject
  private Event<StudentArchivedEvent> studentArchivedEvent;
  
  /**
   * Archives a student.
   * 
   * @param student
   *          The student to be archived
   */
  @Override
  public void archive(ArchivableEntity entity, User modifier) {
    super.archive(entity, modifier);
    
    if (entity instanceof Student) {
      Student student = (Student) entity;

      CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
      AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
  
      // Also archive course students of the archived student
      
      List<CourseStudent> courseStudents = courseStudentDAO.listByStudent(student);
      if (courseStudents.size() > 0) {
        for (CourseStudent courseStudent : courseStudents) {
          courseStudentDAO.archive(courseStudent);
        }
      }
  
      // This is necessary because AbstractStudent entity does not really
      // change in this operation but it still needs to be reindexed
  
      abstractStudentDAO.forceReindex(student.getAbstractStudent());
      
      studentArchivedEvent.fire(new StudentArchivedEvent(student.getId()));
    }
  }

  /**
   * Unarchives a student.
   * 
   * @param student
   *          The student to be unarchived
   */
  @Override
  public void unarchive(ArchivableEntity entity, User modifier) {
    super.unarchive(entity, modifier);
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    
    if (entity instanceof Student) {
      // This is necessary because AbstractStudent entity does not really
      // change in this operation but it still needs to be reindexed

      Student student = (Student) entity;

      abstractStudentDAO.forceReindex(student.getAbstractStudent());
    }
  }

  public Student create(AbstractStudent abstractStudent, String firstName, String lastName, String nickname, String additionalInfo,
      Date studyTimeEnd, StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, Double previousStudies,
      Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText, Boolean lodging) {

    EntityManager entityManager = getEntityManager();

    ContactInfo contactInfo = new ContactInfo();
    
    Student student = new Student();
    student.setFirstName(firstName);
    student.setLastName(lastName);
    student.setNickname(nickname);
    student.setAdditionalInfo(additionalInfo);
    student.setStudyTimeEnd(studyTimeEnd);
    student.setActivityType(activityType);
    student.setExaminationType(examinationType);
    student.setEducationalLevel(educationalLevel);
    student.setEducation(education);
    student.setNationality(nationality);
    student.setMunicipality(municipality);
    student.setLanguage(language);
    student.setSchool(school);
    student.setStudyProgramme(studyProgramme);
    student.setPreviousStudies(previousStudies);
    student.setStudyStartDate(studyStartDate);
    student.setStudyEndDate(studyEndDate);
    student.setStudyEndReason(studyEndReason);
    student.setStudyEndText(studyEndText);
    student.setLodging(lodging);
    student.setContactInfo(contactInfo);

    entityManager.persist(student);

    abstractStudent.addStudent(student);

    entityManager.persist(abstractStudent);
    
    studentCreatedEvent.fire(new StudentCreatedEvent(student.getId()));

    return student;
  }
  
  public void update(Student student, String firstName, String lastName, String nickname, String additionalInfo,
      Date studyTimeEnd, StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, Double previousStudies,
      Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText, Boolean lodging) {
    EntityManager entityManager = getEntityManager();

    student.setFirstName(firstName);
    student.setLastName(lastName);
    student.setNickname(nickname);
    student.setAdditionalInfo(additionalInfo);
    student.setStudyTimeEnd(studyTimeEnd);
    student.setActivityType(activityType);
    student.setExaminationType(examinationType);
    student.setEducationalLevel(educationalLevel);
    student.setEducation(education);
    student.setNationality(nationality);
    student.setMunicipality(municipality);
    student.setLanguage(language);
    student.setSchool(school);
    student.setStudyProgramme(studyProgramme);
    student.setPreviousStudies(previousStudies);
    student.setStudyStartDate(studyStartDate);
    student.setStudyEndDate(studyEndDate);
    student.setStudyEndReason(studyEndReason);
    student.setStudyEndText(studyEndText);
    student.setLodging(lodging);

    entityManager.persist(student);
  }

  public void updateStudentMunicipality(Student student, Municipality municipality) {
    EntityManager entityManager = getEntityManager();

    student.setMunicipality(municipality);

    entityManager.persist(student);

  }

  public void updateSchool(Student student, School school) {
    EntityManager entityManager = getEntityManager();
    student.setSchool(school);
    entityManager.persist(student);
  }

  public Student setStudentTags(Student student, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    student.setTags(tags);
    
    entityManager.persist(student);

    return student;
  }

  public Student updateBillingDetails(Student student, List<BillingDetails> billingDetails) {
    EntityManager entityManager = getEntityManager();
    
    student.setBillingDetails(billingDetails);
    
    entityManager.persist(student);

    return student;
  }

  public void endStudentStudies(Student student, Date endDate, StudentStudyEndReason endReason, String endReasonText) {
    EntityManager entityManager = getEntityManager();
    student.setStudyEndDate(endDate);
    student.setStudyEndReason(endReason);
    student.setStudyEndText(endReasonText);
    entityManager.persist(student);
  }
  
  public Long countByStudyEndReason(StudentStudyEndReason studyEndReason) {
    EntityManager entityManager = getEntityManager();
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.equal(root.get(Student_.studyEndReason), studyEndReason)
    );
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

  public List<Student> listActiveStudents() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.or(
                criteriaBuilder.isNull(root.get(Student_.studyEndDate)),
                criteriaBuilder.greaterThan(root.get(Student_.studyEndDate), new Date())
            )
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listByStudyProgramme(StudyProgramme studyProgramme) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.studyProgramme), studyProgramme)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listActiveStudentsByStudyProgramme(StudyProgramme studyProgramme) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.studyProgramme), studyProgramme),
            criteriaBuilder.or(
                criteriaBuilder.isNull(root.get(Student_.studyEndDate)),
                criteriaBuilder.greaterThan(root.get(Student_.studyEndDate), new Date())
            )
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<Student> listByAbstractStudent(AbstractStudent abstractStudent) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.abstractStudent), abstractStudent)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listActiveStudentsByAbstractStudent(AbstractStudent abstractStudent) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.abstractStudent), abstractStudent),
            criteriaBuilder.or(
                criteriaBuilder.isNull(root.get(Student_.studyEndDate)),
                criteriaBuilder.greaterThan(root.get(Student_.studyEndDate), new Date())
            )
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listByUserVariable(String key, String value) {
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    UserVariableKey UserVariableKey = variableKeyDAO.findByVariableKey(key);
    
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<UserVariable> variable = criteria.from(UserVariable.class);
    Root<Student> student = criteria.from(Student.class);

    criteria.select(student);
    criteria.where(
      criteriaBuilder.and(
          criteriaBuilder.equal(student, variable.get(UserVariable_.user)),
          criteriaBuilder.equal(student.get(Student_.archived), Boolean.FALSE),
          criteriaBuilder.equal(variable.get(UserVariable_.key), UserVariableKey),
          criteriaBuilder.equal(variable.get(UserVariable_.value), value)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public Student updateAbstractStudent(Student student, AbstractStudent abstractStudent) {
    AbstractStudent oldAbstractStudent = student.getAbstractStudent();
    if (oldAbstractStudent != null) {
      oldAbstractStudent.removeStudent(student);
      getEntityManager().persist(oldAbstractStudent);
    }
    
    if (abstractStudent != null) {
      abstractStudent.addStudent(student);
      getEntityManager().persist(abstractStudent);
    }

    return persist(student);
  }

  public Student removeTag(Student student, Tag tag) {
    student.removeTag(tag);
    return persist(student);
  }

  public Student addTag(Student student, Tag tag) {
    student.addTag(tag);
    return persist(student);
  }
  
}
