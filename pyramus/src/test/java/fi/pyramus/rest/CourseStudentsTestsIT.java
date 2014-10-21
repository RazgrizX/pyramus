package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.CourseOptionality;
import fi.pyramus.rest.model.CourseStudent;

public class CourseStudentsTestsIT extends AbstractRESTServiceTest {
  
  private static final long COURSE_ID = 1000;

  @Test
  public void testCreateCourseStudent() {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, 3l, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("enrolmentTime", is(entity.getEnrolmentTime().toString() ))
      .body("archived", is(false))
      .body("participationTypeId", is(entity.getParticipationTypeId().intValue() ))
      .body("enrolmentTypeId", is(entity.getEnrolmentTypeId().intValue() ))
      .body("lodging", is(entity.getLodging() ))
      .body("optionality", is(entity.getOptionality().toString()));
    
    int id = response.body().jsonPath().getInt("id");

    given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListCourseStudents() {
    given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/students", COURSE_ID)
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(3))
      .body("archived[0]", is(false))
      .body("enrolmentTime[0]", is(getDate(2010, 1, 1).toString()))
      .body("participationTypeId[0]", is(1))
      .body("enrolmentTypeId[0]", is(1))
      .body("lodging[0]", is(false))
      .body("optionality[0]", is("OPTIONAL"))
      .body("id[1]", is(4))
      .body("archived[1]", is(false))
      .body("enrolmentTime[1]", is(getDate(2011, 1, 1).toString()))
      .body("participationTypeId[1]", is(2))
      .body("enrolmentTypeId[1]", is(2))
      .body("lodging[1]", is(true))
      .body("optionality[1]", is("MANDATORY"));
  }
  
  @Test
  public void testFindCourseStudent() {
    given().headers(getAuthHeaders())
    .get("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, 3l)
    .then()
    .statusCode(200)
      .body("id", is(3))
      .body("archived", is(false))
      .body("enrolmentTime", is(getDate(2010, 1, 1).toString()))
      .body("participationTypeId", is(1))
      .body("enrolmentTypeId", is(1))
      .body("lodging", is(false))
      .body("optionality", is("OPTIONAL"));
  }
  
  @Test
  public void testUpdateCourseStudent() {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, 3l, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("enrolmentTime", is(entity.getEnrolmentTime().toString() ))
      .body("archived", is(false))
      .body("participationTypeId", is(entity.getParticipationTypeId().intValue() ))
      .body("enrolmentTypeId", is(entity.getEnrolmentTypeId().intValue() ))
      .body("lodging", is(entity.getLodging() ))
      .body("optionality", is(entity.getOptionality().toString()));

    Long id = response.body().jsonPath().getLong("id");

    CourseStudent updateEntity = new CourseStudent(id, COURSE_ID, 3l, getDate(2012, 5, 6), false, 2l, 2l, true, CourseOptionality.OPTIONAL, null);
    
    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateEntity)
      .put("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, updateEntity.getId())
      .then()
      .statusCode(200)
      .body("enrolmentTime", is(updateEntity.getEnrolmentTime().toString() ))
      .body("archived", is(updateEntity.getArchived()))
      .body("participationTypeId", is(updateEntity.getParticipationTypeId().intValue() ))
      .body("enrolmentTypeId", is(updateEntity.getEnrolmentTypeId().intValue() ))
      .body("lodging", is(updateEntity.getLodging() ))
      .body("optionality", is(updateEntity.getOptionality().toString()));

    given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourseStudent() {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, 3l, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("enrolmentTime", is(entity.getEnrolmentTime().toString() ))
      .body("archived", is(false))
      .body("participationTypeId", is(entity.getParticipationTypeId().intValue() ))
      .body("enrolmentTypeId", is(entity.getEnrolmentTypeId().intValue() ))
      .body("lodging", is(entity.getLodging() ))
      .body("optionality", is(entity.getOptionality().toString()));
  
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id)
      .then()
      .statusCode(404);
  }
}