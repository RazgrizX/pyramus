package fi.pyramus.rest.model;

import org.joda.time.DateTime;

public class AbstractStudent {

  public AbstractStudent() {
    super();
  }

  public AbstractStudent(Long id, DateTime birthday, String socialSecurityNumber, Sex sex, Boolean secureInfo, String basicInfo) {
    super();
    this.id = id;
    this.birthday = birthday;
    this.socialSecurityNumber = socialSecurityNumber;
    this.sex = sex;
    this.secureInfo = secureInfo;
    this.basicInfo = basicInfo;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DateTime getBirthday() {
    return birthday;
  }

  public void setBirthday(DateTime birthday) {
    this.birthday = birthday;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public Boolean getSecureInfo() {
    return secureInfo;
  }

  public void setSecureInfo(Boolean secureInfo) {
    this.secureInfo = secureInfo;
  }

  public String getBasicInfo() {
    return basicInfo;
  }

  public void setBasicInfo(String basicInfo) {
    this.basicInfo = basicInfo;
  }

  private Long id;
  private DateTime birthday;
  private String socialSecurityNumber;
  private Sex sex;
  private Boolean secureInfo;
  private String basicInfo;
}