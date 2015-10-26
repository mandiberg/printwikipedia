//create script element with whereFile function
// loader = document.createElement('script');
// loader.src = "http://code.jquery.com/jquery-1.11.3.min.js";
// loader.type = 'text/javascript';
// document.getElementsByTagName('head')[0].appendChild(loader);

//if you need to use jquery for any reason ^^^^

code = document.createElement('script');
code.type='text/javascript';
var mycode_inner = 'function whereFile(inputf){    console.log(inputf); var files_on_page = document.getElementById("myFilesFrame").contentDocument.getElementById("my_files_table").getElementsByClassName("name");console.log(files_on_page); var len_files = files_on_page.length; console.log(len_files); var file_name = ""; var file_pos = 0;     if(files_on_page[0]==undefined){return false;}; for(var i = len_files - 1; i >= 0; i--){console.log("im in here bitch");file_name = files_on_page[i].childNodes[1].innerHTML; console.log(file_name);file_pos = i;    if(file_name.substr(0,4)==inputf.substr(0,4)){ console.log("fukken found iut");break;    } else{    file_name=false; file_pos = false;} } if(file_pos===false){return false;    }else{    return file_pos+1;} };';
 mycode_inner += 'function check2(){var files_added = document.getElementById("documentList").getElementsByClassName("ui-sortable")[0].getElementsByClassName("documentTitle");var a_file;console.log(files_added.length);if(files_added.length==1){a_file = files_added[0].innerHTML;console.log(a_file);if(a_file.match(/(mod\d*\.pdf)/) == null){console.log("the file here is not a mod");return true;}else{console.log("the file here is a mod.");return false;	}}else if(files_added.length>2){return "over";}else if(files_added.length==2){a_file = files_added[0].innerHTML;b_file = files_added[1].innerHTML;console.log(a_file);if((a_file.match(/(mod\d*\.pdf)/) != null && b_file.match(/(mod\d*\.pdf)/) == null)||(a_file.match(/(mod\d*\.pdf)/) == null && b_file.match(/(mod\d*\.pdf)/) == null)){return "over";}else{return "okay";	}}else if(files_added.length==0){return "over";}};';

code.innerHTML=mycode_inner;

document.getElementsByTagName("body")[0].appendChild(code); // append code to body.


//below is code written out in more human readable format.

// function check2(){
// 	var files_added = document.getElementById("documentList").getElementsByClassName("ui-sortable")[0].getElementsByClassName('documentTitle');
// 	var a_file;
// 	console.log(files_added.length);
// 	if(files_added.length==1){
// 		a_file = files_added[0].innerHTML;
// 		console.log(a_file);
// 		if(a_file.match(/(mod\d*\.pdf)/) == null){
// 			console.log("the file here is not a mod");
// 			return true;
// 		}
// 		else{
// 			console.log("the file here is a mod.");
// 			return false;
// 		}
// 	}
// 	else if(files_added.length>2){
// 		return "over";
// 	}
// 	else if(files_added.length==2){
// 		a_file = files_added[0].innerHTML;
// 		b_file = files_added[1].innerHTML;
// 		console.log(a_file);
// 		if(a_file.match(/(mod\d*\.pdf)/) && null || b_file.match(/(mod\d*\.pdf)/) == null){
// 			// console.log("the file here is not a mod");
// 			return "over";
// 		}
// 		else{
// 			return "okay";	
// 		}
		
// 	}
// 	else if(files_added.length==0){
// 		return "over";
// 	}
// }
