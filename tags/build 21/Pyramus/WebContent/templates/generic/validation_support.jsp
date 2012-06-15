<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
  <c:when test="${validationSupportIncluded != true}">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixvalidation/ixvalidation.js"></script>
    <link rel="StyleSheet" href="${pageContext.request.contextPath}/css/validation.css" type="text/css"></link>
    <script type="text/javascript">
      function revalidateTableCell(tableComponent, row, column) {
        var cellEditor = tableComponent.getCellEditor(row, column);
        var validationDelegator = IxValidationDelegatorVault.getDelegator(cellEditor);
        if (validationDelegator)
          validationDelegator.validate(true);
      }
    
      function initializeTable(tableComponent) {
        for (var i = 0, l = tableComponent.getRowCount(); i < l; i++) {
          initializeValidation(event.tableComponent.getRowElement(i)); 
        };

        tableComponent.addListener("rowAdd", function (event) {
          var table = event.tableComponent;
          if (table.isDetachedFromDom()) {
            var row = event.row;
            var onAfterReattachToDom = function (e) {
              e.tableComponent.removeListener("afterReattachToDom", onAfterReattachToDom);
              initializeValidation(e.tableComponent.getRowElement(row));
            };
            table.addListener("afterReattachToDom", onAfterReattachToDom); 
          } else {
            initializeValidation(event.tableComponent.getRowElement(event.row));
          }
        });
        
        tableComponent.addListener("beforeRowDelete", function (event) {
          deinitializeValidation(event.tableComponent.getRowElement(event.row)); 
        });

        // tableComponent.addListener("beforeRowDelete", function (event) {
        //  deinitializeValidation(event.tableComponent.domNode); 
        // });

        // tableComponent.addListener("rowDelete", function (event) {
        //  initializeValidation(event.tableComponent.domNode); 
        //  revalidateAll();  
        // });

        tableComponent.addListener("cellValueChange", function (event) {
          revalidateTableCell(event.tableComponent, event.row, event.column);
        });

        tableComponent.addListener("cellEditableChanged", function (event) {
          var rowElement = event.tableComponent.getRowElement(event.row);
          deinitializeValidation(rowElement); 
          initializeValidation(rowElement);   
        });

        tableComponent.addListener("cellDataTypeChanged", function (event) {
          var rowElement = event.tableComponent.getRowElement(event.row);
          deinitializeValidation(rowElement); 
          initializeValidation(rowElement);  
        });
      };

      document.observe("dom:loaded", function(event) {
        // Initialize normal validation
        initializeValidation();

        // If ixtable.js is loaded initialize validation for existing tables
        if ((typeof getIxTables) == 'function') {
	        var tables = getIxTables();
	        for (var i = 0, l = tables.length; i < l; i++) {
	          initializeTable(tables[i]);
	        };
        }
        
        // Initialize validation for tables that will come
        Event.observe(document, "ix:tableAdd", function (event) {
          initializeTable(event.memo.tableComponent);
        });

        // Start listening for draft restoring

        Event.observe(document, "ix:draftRestore", function (event) {
          revalidateAll(true);
        });
      });
    </script>
    
    <c:set scope="request" var="validationSupportIncluded" value="true"/>
  </c:when>
</c:choose>