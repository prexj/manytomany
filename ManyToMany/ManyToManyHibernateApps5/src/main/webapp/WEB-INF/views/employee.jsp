<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<title>Insert title here</title>
<!-- <script type="text/javascript">
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
	var fn = $("#firstname").val();
	var ln = $("#lastname").val();
	var section = $("#section").val();
	var departmentName = $("#departmentName").val();
	//var country = $("#country").val();
	//searchAjaxData();
$.ajax({
    	   type: "post",
    	   url: "insertData",
    	   dataType: "json",  
    	   data:{ 
    		   firstname: fn , 
    		   lastname: ln , 
    		   section: section , 
    		   departmentName: departmentName },
    	   success: function(response){
    	   		alert(response)
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
        	  	  	rows += '<tr><td>' + item.department.departmentId + '</td>';
        	  	  	rows += '<td>' + item.firstname + '</td>';
        	  	  	rows += '<td>' + item.lastname + '</td>';
        	  	  	rows += '<td>' + item.section + '</td>';
        	  	  	rows += '<td>' + item.department.departmentName + '</td>';
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

</script>   -->
</head>
<body>
<!-- <button type="button"
onclick="showAll();">
showAll</button>
 <table>
  <thead>
  	<tr>
	  <th> Id </th>
	  <th> FirstName </th>
	  <th> LastName </th>
	</tr>
  </thead>
  <tbody id="tblProducts">
  
  </tbody>
</table>  -->
<!-- <table>
  <thead>
  	<tr>
	  <th> Id </th>
	  <th> DepartmentName </th>
	</tr>
  </thead>
  <tbody id="tblProducts2">
  
  </tbody> 
</table> -->

  <form action="addd" method="post"> 
	<table>
		 <tr>
			<td>
				<label>FirstName</label>
			</td>
			<td>
				<input id="firstname" type="text" name="firstname" value=""/>
			</td>
		</tr> 
		<tr>
			<td>
				<label>LastName</label>
			</td>
			<td>
				<input id="lastname" type="text" name="lastname" value=""/>
			</td>
		</tr>
		<tr>
			<td>
				<label>Subject</label>
			</td>
			<td>
				<input id="subject" type="text" name="subject" value=""/>
			</td>
		</tr>
		<tr>
			<td>
				<label>MeetingDate</label>
			</td>
			<td>
				<input id="meetingDate" type="text" name="meetingDate" value=""/>
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
				<input type="submit" name="submit" value="Insert"/><!-- onclick="insertAjaxData();" --> 
			</td>
		</tr>
	</table>
 </form> 
</body>
</html>