$(document).ready(function() {
	$(".form-addmovie").submit(function(event) {
        // Prevent the form from submitting via the browser.
		$(".postResultDiv").empty();
        event.preventDefault();
        ajaxAddMovie();
    });

	$(".form-editmovie").submit(function(event) {
		event.preventDefault();
		ajaxEditMovie();
	});

	$(".form-deletemovie").submit(function(event) {
		event.preventDefault();
		ajaxDeleteMovie();
	});

	$(".form-addemployee").submit(function(event) {
		event.preventDefault();
		ajaxAddEmployee();
	});

	$(".form-editemployee").submit(function(event) {
		event.preventDefault();
		ajaxEditEmployee();
	});

	$(".form-deleteemployee").submit(function(event) {
		event.preventDefault();
		ajaxDeleteEmployee();
	});
	
	$(".form-moviequeue").submit(function(event) {
		event.preventDefault();
		ajaxMovieQueue();
	});
	
	$(".form-salesreport").submit(function(event) {
		event.preventDefault();
		ajaxSalesReport();
	});

	$(".form-searchmoviename").submit(function(event) {
		event.preventDefault();
		ajaxSearchMovieName();
	});

	$(".form-searchmovietype").submit(function(event) {
		event.preventDefault();
		ajaxSearchMovieType();
	});

	$(".form-searchmoviecust").submit(function(event) {
		event.preventDefault();
		ajaxSearchMovieCust();
	});

	$(".form-recordorder").submit(function(event) {
		event.preventDefault();
		ajaxRecordOrder();
	});
	$(".form-addcustomer").submit(function(event) {
		event.preventDefault();
		ajaxAddCustomer();
	});
	$(".form-editcustomer").submit(function(event) {
		event.preventDefault();
		ajaxEditCustomer();
	});
	$(".form-deletecustomer").submit(function(event) {
		event.preventDefault();
		ajaxDeleteCustomer();
	});
	
	$(".form-suggestmovie").submit(function(event) {
		event.preventDefault();
		ajaxSuggestMovie();
	});
	
	$(".form-ratemovie").submit(function(event) {
		event.preventDefault();
		ajaxRateMovie();
	});
	
	$(".form-anyquery").submit(function(event) {
		event.preventDefault();
		var query = $('#q23_query').val();
		$('div.postResultDiv').empty();
		var keyword = query.substr(0, query.indexOf(' ')).toUpperCase();
		if (keyword == "SELECT") {
			ajaxGetQuery();
		}
		else {
			ajaxPostQuery();
		}
	});
	



	function ajaxAddMovie() {
		var dataTu = { id: $('#q1a_id').val(),
		name: $('#q1a_name').val(),
		type: $('#q1a_type').val(),
		rating: $('#q1a_rating').val(),
		distrfee: $('#q1a_distrfee').val(),
		numcopies: $('#q1a_numcopies').val() };	

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/1a",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You added a new movie into the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}

	function ajaxEditMovie() {
		var dataTu = { id: $('#q1b_id').val(),
		attr: $('#q1b_attr').val(),
		attrval: $('#q1b_attrval').val() };	


		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/1b",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You editted a movie from the database. </div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}

	function ajaxDeleteMovie() {
		var deleteid = parseInt($('#q1c_id').val());
		console.log(deleteid);
		//event.preventDefault();
		$.ajax({
			type : "DELETE",
			url : "/query/1c/" + deleteid,
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You deleted a movie from the database. </div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
//				alert("Error!")
//				console.log("ERROR: ", e);
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}

	function ajaxAddEmployee() {
		var dataTu = { id: $('#q2a_id').val(),
		ssn: $('#q2a_ssn').val(),
		startdate: $('#q2a_startdate').val(),
		hourlyrate: $('#q2a_hourlyrate').val(),
		managerstatus: $('#q2a_managerstatus').val() };	

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/2a",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You added an employee into the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
//				alert("Error!")
//				console.log("ERROR: ", e);
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}

	function ajaxEditEmployee() {
		var dataTu = { id: $('#q2b_id').val(),
		attr: $('#q2b_attr').val(),
		attrval: $('#q2b_attrval').val() };	

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/2b",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You editted an employee from the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
//				alert("Error!")
//				console.log("ERROR: ", e);
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}

	function ajaxDeleteEmployee() {
		//event.preventDefault();
		$.ajax({
			type : "DELETE",
			url : "/query/2c/" + $('#q2c_ssn').val(),
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You deleted an employee from the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}
	
	function ajaxMovieQueue() {
		var cookieid = Cookies.get('id');
		
		var dataTu = { accountid: cookieid,
		movieid: $('#q14b_movieid').val() };	
		

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/14b",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You added an employee into the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
//				alert("Error!")
//				console.log("ERROR: ", e);
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}
	
	function ajaxSalesReport() {
		$.ajax({
			type : "GET",
			url : "/query/3/" + $('#salesreport').val(),
			success: function(json) {
				$("tbody.query3").empty();
				$.each(json, function() {
					$("tbody.query3").append('<tr style="text-align: center;">');
					$.each(this, function(key, value) {
						$("tbody.query3").find("tr").last().append('<td>' + value + '</td>');
					});
					$("tbody.query3").append('</tr>');
				});
			}
		});
	}


	function ajaxSearchMovieName() {
		$.ajax({
			type : "GET",
			url : "/query/5a/" + $('#searchmoviename').val(),
			success: function(json) {
				$("tbody.query5a").empty();
				$.each(json, function() {
					$("tbody.query5a").append('<tr>');
					$.each(this, function(key, value) {
						$("tbody.query5a").find("tr").last().append('<td>' + value + '</td>');
					});
					$("tbody.query5a").append('</tr>');
				});
			}
		});
	}

	function ajaxSearchMovieType() {
		$.ajax({
			type : "GET",
			url : "/query/5b/" + $('#searchmovietype').val(),
			success: function(json) {
				$("tbody.query5b").empty();
				$.each(json, function() {
					$("tbody.query5b").append('<tr>');
					$.each(this, function(key, value) {
						$("tbody.query5b").find("tr").last().append('<td>' + value + '</td>');
					});
					$("tbody.query5b").append('</tr>');
				});
			}
		});
	}

	function ajaxSearchMovieCust() {
		$.ajax({
			type : "GET",
			url : "/query/5c/" + $('#searchmoviecust').val(),
			success: function(json) {
				$("tbody.query5c").empty();
				$.each(json, function() {
					$("tbody.query5c").append('<tr>');
					$.each(this, function(key, value) {
						$("tbody.query5c").find("tr").last().append('<td>' + value + '</td>');
					});
					$("tbody.query5c").append('</tr>');
				});
			}
		});
	}

	function ajaxRecordOrder() {
		var dataTu = { accountid: $('#q9_accountid').val(),
		custrepid: $('#q9_custrepid').val(),
		orderid: $('#q9_orderid').val(),
		movieid: $('#q9_movieid').val(),
		datetime: $('#q9_datetime').val(),
		returndate: $('#q9_returndate').val() };	

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/9",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You recorded a new order into the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}

	function ajaxAddCustomer() {
		var dataTu = { username: $('#q10a_username').val(),
			email: $('#q10a_email').val(),
			password: $('#q10a_password').val(),
			firstname: $('#q10a_firstname').val(),
			lastname: $('#q10a_lastname').val(),
			ssn: $('#q10a_socialsecuritynumber').val(),
			creditcardnumber: $('#q10a_creditcardnumber').val(),
			address: $('#q10a_address').val(),
			zipcode: $('#q10a_zipcode').val(),
			telephone: $('#q10a_telephone').val(),
			city: $('#q10a_city').val(),
			state: $('#q10a_state').val(),
	        rating: $('#q10a_rating').val(),
	 	    accounttype: $('#q10a_accounttype').val() 
 		}

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/10a",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You added a new customer.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}

	function ajaxEditCustomer() {
		var dataTu = { id: $('#q10b_id').val(),
		attr: $('#q10b_attr').val(),
		attrval: $('#q10b_attrval').val() };	

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/10b",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You editted a customer from the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}
	
	function ajaxSuggestMovie() {
		var check = $('#suggestmovie').val();
		$.ajax({
			type : "GET",
			url : "/query/12/" + $('#suggestmovie').val(),
			success: function(json) {
				$("tbody.query12").empty();
				$.each(json, function() {
					$("tbody.query12").append('<tr>');
					$.each(this, function(key, value) {
						$("tbody.query12").find("tr").last().append('<td>' + value + '</td>');
					});
					$("tbody.query12").append('</tr>');
				});
			}
		});
	}

	function ajaxDeleteCustomer() {
		$.ajax({
			type : "DELETE",
			url : "/query/10c/" + $('#q10c_id').val(),
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You deleted a customer from the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}
	
	
	
	function ajaxRateMovie() {
		var dataTu = { rating : $('#q22_rating').val(),
				moviename: $('#q22_moviename').val() };	
		

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/22",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You rated a movie in the database</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}
	
	function ajaxGetQuery() {
		var dataTu = { query : $('#q23_query').val() };	

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/23a",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success: function(json) {
				var theadcreated = 0;
				$("thead.query23").empty();
				$("tbody.query23").empty();
				$.each(json, function() {
					$("thead.query23").append('<tr>');
					$("tbody.query23").append('<tr>');
					$.each(this, function(key, value) {
						if (theadcreated == 0) {
							$("thead.query23").find("tr").last().append('<th>' + key + '</th>');
						}
						$("tbody.query23").find("tr").last().append('<td>' + value + '</td>');
					});
					theadcreated = 1;
					$("tbody.query23").append('</tr>');
					$("tbody.query23").append('</tr>');
				});
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}
	
	function ajaxPostQuery() {
		var dataTu = { query : $('#q23_query').val() };	

		//event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/query/23b",
			data : JSON.stringify(dataTu),
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					$(".postResultDiv").html('<div class="alert alert-success" role="alert"><strong>Success!</strong> You made a query into the database.</div>');
				}else{
					$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
				}
				console.log(result);
			},
			error : function(e) {
				$(".postResultDiv").html('<div class="alert alert-danger" role="alert"><strong>Oops!</strong> Something went wrong, please re-check your input fields. </div>');
			}
		});
	}



});


	