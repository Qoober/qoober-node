function formatDate(date){
  let match = date.match(/^(\d{4})-(\d{2})-(\d{2}) (\d{2}:\d{2}):\d{2}$/);
  return match[3]+'.'+match[2]+'.'+match[1]+' '+match[4];
}

function isValidEmail(email){
	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
}

function formatAmount(amount, decimals){
	if( !(decimals > 0 && decimals < 8) ) decimals = 8;
	
	amount = parseFloat(amount);
	
	amount = (amount).toFixed(decimals);
	amount = amount.replace(/0+$/,'');
	
	if(amount.substr(-1) == '.'){
		amount = amount.substr(0,amount.length-1);
	}
	
	return amount;
}
function numberFormat(number){
	number = number_format(number / 100);
	/*if(number.substr(-3) == '.00'){
		number = number.substr(0,number.length-3);
	}*/
	return number;
}
function formatNQT(amount){
	amount = number_format(amount / 100);
	return amount;
}
function formatNQT2(amount){
	amount = number_format(amount / 100);
	amount = amount.toString().replace(/0*$/,"");
	if(amount.substr(-1) == '.'){
		amount = amount.substr(0,amount.length-1);
	}
	return amount;
}
function formatCoef(amount){
	amount = amount.toString().replace(/0*$/,"");
	if(amount.substr(-1) == '.'){
		amount = amount.substr(0,amount.length-1);
	}
	return amount;
}
function formatTime(unix_timestamp){
	let d = new Date(unix_timestamp);
	let cDay = ('0' + d.getDate()).slice(-2);
	let cMonth = ('0' + (d.getMonth()+1)).slice(-2);
	let cYear = d.getFullYear();
	let cHour = ('0' + d.getHours()).slice(-2);
	let cMin = ('0' + d.getMinutes()).slice(-2);
	let cSec = ('0' + d.getSeconds()).slice(-2);

	return cDay + '.' + cMonth + '.' + cYear + ' ' + cHour + ':' + cMin + ':' + cSec;
}

function formatTimeServerAge(time, serverTime){
	let diff = (serverTime - time) / 1000;
	
	let diff_years = Math.floor(diff / 31536000);
	diff = diff % 31536000;

	let diff_months = Math.floor(diff / 2592000);
	diff = diff % 2592000;

	let diff_days = Math.floor(diff / 86400);
	diff = diff % 86400;

	let diff_hours = Math.floor(diff / 3600);
	diff = diff % 3600;

	let diff_mins = Math.floor(diff / 60);
	let diff_secs = Math.floor(diff % 60);
	
	if(diff_years > 0){
		return diff_years + ' years ago';
	}
	
	if(diff_months > 0){
		return diff_months + ' months ago';
	}

	if(diff_days > 0){
		return diff_days + ' days ago';
	}

	if(diff_hours > 0){
		return diff_hours + ' hours ago';
	}

	if(diff_mins > 0){
		return diff_mins + ' mins ago';
	}

	return diff_secs + ' secs ago';
}

function formatTimeForging(time){
	let diff = time;

	let diff_years = Math.floor(diff / 31536000);
	diff = diff % 31536000;

	let diff_months = Math.floor(diff / 2592000);
	diff = diff % 2592000;

	let diff_days = Math.floor(diff / 86400);
	diff = diff % 86400;

	let diff_hours = Math.floor(diff / 3600);
	diff = diff % 3600;

	let diff_mins = Math.floor(diff / 60);
	let diff_secs = Math.floor(diff % 60);
	
	if(diff_years > 0){
		return diff_years + ' years';
	}
	
	if(diff_months > 0){
		return diff_months + ' months';
	}

	if(diff_days > 0){
		return diff_days + ' days';
	}

	if(diff_hours > 0){
		return diff_hours + ' hours';
	}

	if(diff_mins > 0){
		return diff_mins + ' mins';
	}

	return diff_secs + ' secs';
}


function tsToTime(unix_timestamp){
	var date = new Date(unix_timestamp);
	var hours = date.getHours();
	var minutes = "0" + date.getMinutes();
	var seconds = "0" + date.getSeconds();
	return hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
}

function rand(min, max){
	if(min < 0 || max > 2147483647){
		min = 0; max = 2147483647;
	}
  return Math.floor(Math.random() * (max - min + 1)) + min
}

function number_format( number, decimals, dec_point, thousands_sep ) {	// Format a number with grouped thousands
	// 
	// +   original by: Jonas Raoni Soares Silva (http://www.jsfromhell.com)
	// +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
	// +	 bugfix by: Michael White (http://crestidg.com)

	var i, j, kw, kd, km;

	// input sanitation & defaults
	if( isNaN(decimals = Math.abs(decimals)) ){
		decimals = 2;
	}
	if( dec_point == undefined ){
		dec_point = ".";
	}
	if( thousands_sep == undefined ){
		thousands_sep = " ";
	}

	i = parseInt(number = (+number || 0).toFixed(decimals)) + "";

	if( (j = i.length) > 3 ){
		j = j % 3;
	} else{
		j = 0;
	}

	km = (j ? i.substr(0, j) + thousands_sep : "");
	kw = i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousands_sep);
	//kd = (decimals ? dec_point + Math.abs(number - i).toFixed(decimals).slice(2) : "");
	kd = (decimals ? dec_point + Math.abs(number - i).toFixed(decimals).replace(/-/, 0).slice(2) : "");


	return km + kw + kd;
}
