var names = [];
var surnames = [];
var addresses = [];
var locations = [];
var countries = [];
var contactNumbers = [];
var isPickups = [];
var lats = [];
var lngs = [];
var ids = [];
var weights = [];
var id = 40; // initial value

//AIRCRAFT DATA:
var id2 = 4;  // initial value
var ids2 = [];
var types = [];
var capacities = [];
var velocities = [];
var currentLngs = [];
var currentLats = [];

//ROUTES DATA:
var id3 = 2; // initial value
var routes = [];
var ids3 = [];

// funkcja wyświetlająca w okienku informacje o zamówieniach
function seeYourOrders(e){
	document.getElementById("form").innerHTML = '';
	document.getElementById("form").style.position = 'relative';
	document.getElementById("form").style.display = 'inline';

	for (var i = 0; i < names.length; i++) {
		document.getElementById("form").innerHTML += "---ORDER " + i + '---<br>';
		document.getElementById("form").innerHTML += "  Name: " + names[i] + '<br>';
		document.getElementById("form").innerHTML += "  Surname: " + surnames[i] + '<br>';
		document.getElementById("form").innerHTML += "  Address: " + addresses[i]+ '<br>';
		document.getElementById("form").innerHTML += "  Location: " + locations[i]+ '<br>';
		document.getElementById("form").innerHTML += "  Country: " + countries[i]+ '<br>';
		document.getElementById("form").innerHTML += "  Contact Number: " + contactNumbers[i]+ '<br>';
		document.getElementById("form").innerHTML += "  Weight: " + weights[i]+ '<br>';
		document.getElementById("form").innerHTML += "  Pickup: " + isPickups[i]+ '<br>';

		document.getElementById("form").innerHTML += "<br>";
	}


	document.getElementById("form").innerHTML += '<button type="button" onclick="comeBack()">' +
		'Come back</button>';

}


// come back to the view of new order
function comeBack(){
	document.getElementById("form").innerHTML = '<h3>Please give all related details.</h3>' +
		window.location.reload(true); // kolejne niepotrzebne w takim wariancie.
	'<div id="maininfo" >' +
	'<div id="questions">' +
	'<div><label for="textField1" >Name:</label></div>' +
	'<div><label for="textField2">Surname:</label></div>' +
	'<div><label for="textField3">Address:</label></div>' +
	'<div><label for="textField3_2">Location:</label></div>' +
	'<div><label for="textField4">Country:</label></div>' +
	'<div><label for="textField5">Contact number:</label></div>' +
	'<div><label for="textField6">Weight[kg]:</label></div>' +
	'<div><label for="mapCheckbox">Show map?</label></div>' +
	'</div>' +

	'<div id="userdata">' +
	'<div><input type="text" id="textField1" required="true"  name="nameField"></div>' +
	'<div><input type="text" id="textField2" required="true" name="surnameField"></div>' +
	'<div><input type="text" id="textField3" name="addressField" placeholder = "write or click on map"></div>' +
	'<div><input type="text" id="textField3_2" required="true" name="locationField"></div>' +
	'<div><input type="text" id="textField4" name="countryField"></div>' +
	'<div><input type="text" id="textField5" required="true" name="contactField"></div>' +
	'<div><input type="text" id="textField6" required="true" name="weightField"></div>' +
	'<div><input type="checkbox" id="mapCheckbox" name="mapCheckbox" value="Show map"></div>' +
	'</div>' +
	'</div>' +

	'<br> </br>' +
	'<div id="bottom">' +
	'<input type="radio" id="transportType1"' +
	'name="transport" value="pickup">' +
	'<label for="transportType1">pickup</label>' +

	'<input type="radio" id="transportType2"' +
	'name="transport" value="deliver">' +
	'<label for="transportType2">deliver</label>' +
	'</br></br>' +
	'(Costs of delivery to your home - 10 $.)' +

	'<h3>All right? For sure?</h3>' +
	'</div>' +

	'<form id="hurraySubmit" onclick = "hurrayJavaScript()">' +
	'<button type="button">Make a transaction</button>' +
	'</form>' +
	'<form id="seeOrders" onclick = "seeYourOrders()">' +
	'<button type="button">See your orders</button>' +
	'</form>' +

	'</br>';
}

