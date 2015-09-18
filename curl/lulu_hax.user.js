//add this to your greasemonkey plugin on firefox. prevents alerts and onbeforeunload popups.
window.alert = function() {};
alert = function(){};
window.onbeforeunload = function(){};
if($jq){//$jq is what lulu named their jquery rather than just using '$' this may change?
	if($jq("#studioBody").length>0){//this is for skipping the page that offers you to use their cover wizard.
    console.log("yo studbod");
			atag = document.getElementById("feedback0").getElementsByTagName("a")[1];    
			if(atag!=undefined){
        console.log("yo is here.");
				document.fCoverOptionsForm.fAction.value="switchToOnePieceCover"; 
				$jq("#switchCoverTypePopupSpan").text("screw the wizard!!! one piece!"); 
				showSwitchingToOnePieceDialog(); 
				setTimeout ( "document.fCoverOptionsForm.submit();", 500 );
			}
	}
}

function check2(){ //for checking to see that you have a mod file and a regular encyclopedia file after select2 in stage 7 
	var files_added = document.getElementById("documentList").getElementsByClassName("ui-sortable")[0].getElementsByClassName('documentTitle');
	var a_file;
	console.log(files_added.length);
	if(files_added.length==1){
		a_file = files_added[0].innerHTML;
		console.log(a_file);
		if(a_file.match(/(mod\d*\.pdf)/) == null){
			console.log("the file here is not a mod");
			return true;
		}
		else{
			console.log("the file here is a mod.");
			return false;
		}
	}
	else if(files_added.length>2){
		return "over";
	}
	else if(files_added.length==2){
		a_file = files_added[0].innerHTML;
		b_file = files_added[1].innerHTML;
		console.log(a_file);
		if(a_file.match(/(mod\d*\.pdf)/) == null || b_file.match(/(mod\d*\.pdf)/) == null){
			// console.log("the file here is not a mod");
			return "over";
		}
		return "okay";
	}
	else if(files_added.length==0){
		return "over";
	}
}
	



	

