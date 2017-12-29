<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
function searchAjaxData(){
	var departmentName = $("#departmentName").val();
	//var country = $("#country").val();
$.ajax({
    	   type: "POST",
    	   url: "searchData",
    	   dataType: "json", 
    	  /*  async: false, */ 
    	   data:{ departmentName: departmentName },
    	   success: function(response){
    		   alert(response);
    		   var rows = '';
               $.each( response , function( index, item ) {
       	  	  	rows += '<tr><td>' + item.departmentId + '</td>';
       	  	  	rows += '<td>' + item.departmentName + '</td></tr>';
       	  		if(item.departmentName == departmentName){
       	  			
       	  			adddataAjaxData(item.departmentId);
       	  		}else{
       	  			
       	  		}
       	  	  });
       	  	  $('#tblProducts2').html(rows);
    	   		
    	   },
    	   error: function(){      
    	    alert('Error while request..');
    	   }
    	  });
}
function insertAjaxData(){
	/* alert('hi'); */
	/* var departmentId = $("#departmentId").val();   */
	var un = $("#username").val();
	var pass = $("#password").val();
	var email = $("#email").val();
	var name = $("#name").val();
	//var country = $("#country").val();
	//searchAjaxData();
$.ajax({
    	   type: "post",
    	   url: "insertUser",
    	   dataType: "json",  
    	   data:{ 
    		   username: un , 
    		   password: pass , 
    		   email: email , 
    		   name: name },
    	   success: function(response){
    	   		alert(response);
    	   		showAll(); 
    	  /*  alert('hi');  */
    	   if(response == 1 )
    	    		{
    	    			alert('Inserted Successfully');
    					showAll(); 
    	    	} 
    	   },
    	   error: function(){      
    	    alert('Error while request..');
    	   }
    	  });
}
function showAll(){
    $.ajax({
            type:"GET",
            url:"showAll",
            dataType: "json",
            success:function(response)
            { alert('response')
          	  var rows = '';
                $.each( response , function( index, item ) {
        	  	  	rows += '<tr><td>' + item.user.id + '</td>';
        	  	  	rows += '<td>' + item.group.id + '</td>';
        	  	  	rows += '<td>' + item.user.username + '</td>';
        	  	  	rows += '<td>' + item.user.password + '</td>';
        	  	  	rows += '<td>' + item.user.email + '</td>';
        	  	  	rows += '<td>' + item.group.name + '</td>';
        	  		/* rows += '<td>' + item.university.country + '</td>'; */
        	  	  });
        	  	  $('#tblProducts').html(rows);
            },
            error:function(xmlHttpRequest, textStatus, errorThrown)
            {
                   alert("error");
            }
    });
}

</script>   
</head>
<body>
 <button type="button"
onclick="showAll();">
showAll</button>
 <table>
  <thead>
  	<tr>
	  <th> UserId </th>
	  <th> GroupId </th>
	  <th> Username </th>
	  <th> Password </th>
	  <th> email </th>
	  <th> name </th>
	</tr>
  </thead>
  <tbody id="tblProducts">
  
  </tbody>
 </table> 
 <!--<table>
  <thead>
  	<tr>
	  <th> Id </th>
	  <th> DepartmentName </th>
	</tr>
  </thead>
  <tbody id="tblProducts2">
  
  </tbody>
</table>   -->

 <!--  <form action="insertUser" method="post">  -->
	<table>
		 <tr>
			<td>
				<label>Username</label>
			</td>
			<td>
				<input id="username" type="text" name="username" value=""/>
			</td>
		</tr> 
		<tr>
			<td>
				<label>Password</label>
			</td>
			<td>
				<input id="password" type="password" name="password" value=""/>
			</td>
		</tr>
		<tr>
			<td>
				<label>Email</label>
			</td>
			<td>
				<input id="email" type="text" name="email" value=""/>
			</td>
		</tr>
		<tr>
			<td>
				<label>Name</label>
			</td>
			<td>
				<input id="name" type="text" name="name" value=""/>
			</td>
		</tr>
		<!-- <tr>
			<td>
				<label>DepartmentName</label>
			</td>
			<td>
				<input id="departmentName" type="text" name="departmentName" value=""/>
			</td>
		</tr> --> 
		<!-- <tr>
			<td>
				<label>Country</label>
			</td>
			<td>
				<input id="country" type="text" name="country" value=""/>
			</td>
		</tr> -->
		<tr>
			<td>
				<input type="submit" name="submit" value="Insert" onclick="insertAjaxData();" />
			</td>
		</tr>
	</table>
<!--  </form>  -->
</body>
</html>