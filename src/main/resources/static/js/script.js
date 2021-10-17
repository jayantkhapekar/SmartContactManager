console.log("this is script file")

const togglesSidebar = () => {


	if ($(".sidebar").is(":visible")) {

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");

	}

	else {

		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");

	}


}; 

const search = () => {

		 

	let query = $("#search-input").val();

	

	if(query == ""){

		$(".search-result").hide();

	}else{

		console.log(query);

		$(".search-result").show();

	}


};