<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="system.managepermissions.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>

    <script type="text/javascript">
      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head>
  
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="system.importexportpermissions.pageTitle"/></h1>
    
    <div class="genericFormContainer"> 
      <form action="importexportpermissions.page" method="post">
  
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#permissions">
            <fmt:message key="system.importexportpermissions.tabLabel"/>
          </a>
        </div>
        
        <div id="permissions" class="tabContent">
            <textarea rows="30" cols="200" name="permissionJson">${permissionJson}</textarea>
        </div>
  
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="system.importexportpermissions.saveBtn"/>">
        </div>

      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>