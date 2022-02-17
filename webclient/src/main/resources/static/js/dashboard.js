$(document).ready(function() {
    $("#wait").css('visibility', 'hidden');
    $(document).ajaxStart(function(){
      $("#wait").css('visibility', 'visible');
    });

    $(document).ajaxComplete(function(){
        $("#wait").css('visibility', 'hidden');
    });
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
        			$('#updateModal').modal('show');
        			$("#firstName").val(data.firstName);
        			$("#lastName").val(data.lastName);
        			$("#email").val(data.email);
        			$("#phone").val(data.phone);
        		},
        		error : function(data) {
        			console.log("ERROR");
        			alert("ERROR:" + data);
        			console.log(data);
        		}
        	});
    });
})