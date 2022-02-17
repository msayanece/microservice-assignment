$(document).ready(function() {
    $("#updateBtn").click(function(event) {
        event.preventDefault();
        var data = {
            "firstName": $("#firstName").val(),
            "lastName": $("#lastName").val(),
            "email": $("#email").val(),
            "phone": $("#phone").val(),
        }
        console.log("DATA: ", data)
        var jsonData = JSON.stringify(data)
        console.log(jsonData)
        $.ajax({
        		type : "POST",
        		url : "updateProfile",
        		data : jsonData,
        		headers : {
        			'Accept' : 'application/json',
        			'Content-Type' : 'application/json'
        		},
        		success : function(data) {
        			console.log("SUCCESS UPDATE PROFILE");
        			console.log(data);
        			$("#firstName").val("Test");
        			$('#updateModal').modal('show');
        		},
        		error : function(data) {
        			console.log("ERROR");
        			alert("ERROR:" + data);
        			console.log(data);
        		}
        	});
    });
})