<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table>
  <thead>
	  <th> UserId </th>
	  <th> GroupId </th>
	  <th> UserName </th>
	  <th> PassWord </th>
	  <th> Email</th>
	  <th> Name </th>
	 <!--  <th> Country </th> -->
	  
  </thead>
  <tbody>
  	<c:forEach items="${show}" var="item">
	  <tr> <p>${item}</p> 
		  <td>${item.user.id }</td>
		  <td>${item.group.id }</td>
		  <td>${item.user.username }</td>
		  <td>${item.user.password }</td>
		  <td>${item.user.email }</td>
		  <td>${item.group.name }</td>
			<%-- <c:forEach items="${employee.address}" var="meet"> --%>
         		
		  		<%-- <td>${item.university.country }</td>  --%>       
			<%-- </c:forEach> --%>
		 
		
		  <%-- <td>
		  <a href="editEmployee?id=${item.id }">EDIT</a>
			  <form action="editStudent" method="get">
			  <input type="hidden" name="id" value="${item.id }">
			  <input type="submit" name="submit" value="edit">
			  </form> 
		  </td>
		  <td>
			  <form action="deletingStudent" method="get">
			  <input type="hidden" name="id" value="${item.id }">
			  <input type="submit" name="submit" value="delete">
			  </form>
		  </td> --%>
	  </tr>
	</c:forEach> 
  </tbody>
</table>
</body>
</html>