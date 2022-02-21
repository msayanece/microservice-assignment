$(document).ready(function() {
    $("#wait").css('visibility', 'hidden');
    $(document).ajaxStart(function(){
      $("#wait").css('visibility', 'visible');
    });

    $(document).ajaxComplete(function(){
        $("#wait").css('visibility', 'hidden');
    });
    $('#errorOk').click(function() {
        location.reload();
    });
    $("#updateBtn").click(function(event) {
        $("#error").text("")
        if(!$("#firstName").val()){
            $("#error").text("First name mandatory.")
            return
        }
        if(!$("#lastName").val()){
            $("#error").text("Last name mandatory.")
            return
        }
        if(!$("#email").val()){
            $("#error").text("Email address mandatory.")
            return
        }
        if(!$("#phone").val()){
            $("#error").text("Phone number mandatory.")
            return
        }
        if(!$("#phone").val().match('[789][0-9]{9}')){
            $("#error").text("Phone number must be of 10 digit.")
            return
        }
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
        			if(data){
                        $('#updateModal').modal('show');
                        $("#firstName").val(data.firstName);
                        $("#lastName").val(data.lastName);
                        $("#email").val(data.email);
                        $("#phone").val(data.phone);
        			}else{
                        $('#errorModal').modal('show');
                        $("#firstName").val('Error loading');
                        $("#lastName").val('Error loading');
                        $("#email").val('Error loading');
                        $("#phone").val('Error loading');
        			}
        		},
        		error : function(data) {
        			console.log("ERROR");
        			console.log(data);
        			$('#errorModal').modal('show');
                    $("#firstName").val('Error loading');
                    $("#lastName").val('Error loading');
                    $("#email").val('Error loading');
                    $("#phone").val('Error loading');
        		}
        	});
    });
})