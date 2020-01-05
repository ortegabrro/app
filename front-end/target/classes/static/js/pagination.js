$(document).ready(
		function() { 
		pageSize = 1;
	    pagesCount = $(".content").length;
	    var currentPage = 1;
	   
	    /////////// PREPARE NAV ///////////////
	    var nav = '';
	    var totalPages = Math.ceil(pagesCount / pageSize);
	    for (var s=0; s<totalPages; s++){
	        nav += '<li class="num page-item"><a class="page-link" href="#">'+(s+1)+'</a></li>';
	    }
	    $(".prev").after(nav);
	    $(".num").first().addClass("active");
	    //////////////////////////////////////
	    
	    showPage = function() {
	        $(".content").hide().each(function(n) {
	            if (n >= pageSize * (currentPage - 1) && n < pageSize * currentPage)
	                $(this).show();
	        });
	    }
	    showPage();


	    $("#pag-card li.num").click(function() {
	        $("#pag-card li").removeClass("active");
	        $(this).addClass("active");
	        currentPage = parseInt($(this).text());
	        showPage();
	    });

	    $("#pag-card li.prev").click(function() {
	        if($(this).next().is('.active')) return;
	        $('.num.active').removeClass('active').prev().addClass('active');
	        currentPage = currentPage > 1 ? (currentPage-1) : 1;
	        showPage();
	    });

	    $("#pag-card li.next").click(function() {
	        if($(this).prev().is('.active')) return;
	        $('.num.active').removeClass('active').next().addClass('active');
	        currentPage = currentPage < totalPages ? (currentPage+1) : totalPages;
	        showPage();
	    });

} );
