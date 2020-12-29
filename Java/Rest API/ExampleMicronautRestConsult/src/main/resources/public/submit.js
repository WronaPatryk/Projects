var video = "hurray";
routesAlreadyCalculated = false;

function hurrayJavaScript(e){
	console.log('VALIDATION');
    // walidacja
    if(isNaN(document.getElementById('textField6').value)){
        window.alert('Weight has to be numerical!');
        return;
    }
    else if ( document.getElementById('textField1').value == '' ||
        document.getElementById('textField2').value == '' ||
        document.getElementById('textField3_2').value == '' ||
        document.getElementById('textField5').value == '' ||
        document.getElementById('textField6').value == '' ||
        document.getElementById('textField6').value <= 0){
        window.alert('Please fill all fields: \n -> Name \n -> Surname \n -> Location (on map) \n -> Contact Number \n -> Weight >0')
        return;
    }

    console.log('ATTEMPTING TO SEND PACKAGE');

	// chcemu umozliwic nowe transakcje bez ponownego ładowania strony
	// document.getElementById("hurraySubmit").innerHTML  = '<button id = "transactionButton" type="button" class="Disabled">Already ordered :^)</button>'


	// save transaction !!!
	var name = document.getElementById('textField1').value;
	var surname = document.getElementById('textField2').value;
	var address = document.getElementById('textField3').value;
	var location = document.getElementById('textField3_2').value;
	var country = document.getElementById('textField4').value;
	var contactNumber = document.getElementById('textField5').value;
	var weight = document.getElementById('textField6').value;
	var isPickup = document.getElementById('transportType1').checked;

	names.push(name);
	surnames.push(surname);
	addresses.push(address);
	locations.push(location);
	countries.push(country);
	contactNumbers.push(contactNumber);
	isPickups.push(isPickup);
	weights.push(weight);
	ids.push(id);

	nOfOrders = names.length;

    //Location to lat & lng:
    loc = location.split(" "); // lokalizacja jako string dwóch liczb
    x = loc[0].substring(1).slice(0, -1); // longitude
    y = loc[1].substring(0).slice(0, -1); // latitude
    lngs.push(x);
    lats.push(y);


	// then posts it to /orders subdirectory
    sendOrder(id, x, y, name, surname, address, country, contactNumber, weight, isPickup)
    id++;
	var option = document.getElementById('transportType1').checked;

	if(option)
		video = "joke";
	else
		video = "hurray";

	document.getElementById('info').style.display='block' ;
	document.getElementById('js-map-container').style.display='none';

	document.getElementById('info').innerHTML =
		'<video id = "najlepszyProwadzacy" height = 400 autoplay>' +
			'<source src="video/' + video + '.mp4" type="video/mp4">' +
		'</video>';

    setSelectOrderBox()

	document.getElementById('mapCheckbox').disabled=true;
	document.getElementById('mapCheckbox').checked=false;
	var vid = document.getElementById("najlepszyProwadzacy");
	//console.log("VIDEO");
	document.getElementById('hurraySubmit').disabled = true;
    vid.play();
    vid.addEventListener('ended',function(){
		document.getElementById('mapCheckbox').disabled=false;
        document.getElementById('info').innerHTML =
		'</br></br></br></br></br></br></br><h1 style="color: gold; font-size: 50px">Form sent!</h1>' +
		'<button id = "again" type="button">One more?</button>';
		var butReplay = document.getElementById("again");
		butReplay.onclick = function(){
			document.getElementById('info').innerHTML = '<p>Pickup or maybe delivery? </br>' +
	'It makes no difference, because for us nothing is impossible! </p>' +
	'<img width = "300" src="img/magic_package.png" alt="sample package" title="package like THIS">' +
	'<p>Just enter the details of the action you want to perform below </br>' +
	'and we will complete your transaction within the specified time (up to 5 working days)</p>';
			document.getElementById('textField1').value = '';
			document.getElementById('textField2').value = '';
			document.getElementById('textField3').value = '';
			document.getElementById('textField3_2').value = '';
			document.getElementById('textField4').value = '';
			document.getElementById('textField5').value = '';
			document.getElementById('textField6').value = '';
			document.getElementById('transportType1').checked = false;
			document.getElementById('transportType2').checked = false;
		};

    });
    window.location.reload(true);

}

function sendOrder(id, lng, lat, name, surname, address, country, contactNumber, weight, pickup) {
fetch('orders',
    {
        method: 'POST',
        body: JSON.stringify({id: id, lng: lng, lat: lat, name: name, surname: surname, address: address,
        country: country, contactNumber: contactNumber, weight: weight, pickup: pickup}),
        headers: {
            'Content-Type': 'application/json'
          },
        //this will come in handy later
        credentials: 'include',
        mode: 'cors'
    });
    // update select list
    var x = document.getElementById("deleteSelect");
    var option = document.createElement("option");
    option.text = name + " " + surname;
    option.value = x.options.length;
    x.add(option);
    routesAlreadyCalculated = false;
}
function delete1order(e){
    orderId = document.getElementById('deleteSelect').value;
    // perform deletion
    console.log("i was here & attempted deleting 1 order");
    fetch("orders/" + orderId, {
        method: 'DELETE'
      })
     .then(() => {
         console.log('order removed');
      })
      .catch(err => {
        console.error(err);
    });
    // update select list
    names.splice(index = orderId, howmany = 1);
    surnames.splice(index = orderId, howmany = 1);
    addresses.splice(index = orderId, howmany = 1);
    locations.splice(index = orderId, howmany = 1);
    countries.splice(index = orderId, howmany = 1);
    contactNumbers.splice(index = orderId, howmany = 1);
    weights.splice(index = orderId, howmany = 1);
    lngs.splice(index = orderId, howmany = 1);
    lats.splice(index = orderId, howmany = 1);
    isPickups.splice(index = orderId, howmany = 1);
    ids.splice(index = orderId, howmany = 1);
    nOfOrders--;
    console.log('Deletion complete, attempting to update selectorderbox')
    setSelectOrderBox();
    routesAlreadyCalculated = false;
}

// TODO   na ten moment tylko (a może aż) dodaje Marka Gągolewskiego z Australii --- ok could be...
function putOrder(e) {
    orderId = document.getElementById('deleteSelect').value; // który rekord?

    // na jakie wartości zmieniamy? Można pobierać odpowiednie wartości z formularza.
    id = ids[orderId]
    //Location to lat & lng:
    loc = locations[orderId].split(" "); // lokalizacja jako string dwóch liczb
    lng = loc[0].substring(1).slice(0, -1); // longitude
    lat = loc[1].substring(0).slice(0, -1); // latitude

    name = "Marek" // tu zmiana ręczna
    surname = "Gągolewski" // tu zmiana ręczna

    lng = 127.66; // Australia
    lat = -18.92; // Australia

    country = "Australia";
    names[orderId] = name;
    surnames[orderId] = surname;
    countries[orderId] = country;
    locations[orderId] = "127.66, -18.92"

    address = addresses[orderId];
    contactNumber = contactNumbers[orderId];
    weight = weights[orderId];
    pickup = isPickups[orderId];


    fetch('orders/' + orderId,      // tutaj id w pliku JSON
    {
        method: 'PUT',
        body: JSON.stringify({id: id,  lng: lng, lat: lat, name: name, surname: surname, address: address,
        country: country, contactNumber: contactNumber, weight: weight, pickup: pickup}),
        headers: {
            'Content-Type': 'application/json'
          },
        //this will come in handy later
        credentials: 'include',
        mode: 'cors'
    });
    // update select list
    var sel = document.getElementById("deleteSelect");
    sel.options[sel.selectedIndex].text = "Marek Gągolewski";
    routesAlreadyCalculated = false;
}


function postNewAirplane(e){
	// walidacja
	if ( document.getElementById('vehicleField1').value == '' ||
		document.getElementById('vehicleField2').value == '' ||
		document.getElementById('vehicleField2').value <= 0 ||
		document.getElementById('vehicleField3').value == '' ||
        document.getElementById('vehicleField3').value <= 0){
		window.alert('Please fill all fields: \n -> Type \n -> Capacity >0 \n -> Velocity >0')
		return;
	}
	// save transaction !!!
	var type = document.getElementById('vehicleField1').value;
	var capacity = document.getElementById('vehicleField2').value;
	var velocity = document.getElementById('vehicleField3').value;
	types.push(type);
	capacities.push(capacity);
	velocities.push(velocity);
	ids2.push(id2);
	currentLngs.push(2.5425); // airportX
	currentLats.push(49.0139); // airportY
	nOfAirplanes = types.length;
    id2++;

    sendAircraft(id2 , type, capacity,velocity);

    // setSelectOrderBox()
	document.getElementById('vehicleField1').value = '';
	document.getElementById('vehicleField2').value = '';
	document.getElementById('vehicleField3').value = '';
    routesAlreadyCalculated = false;
    window.location.reload(true);
}

function sendAircraft(id2, type, capacity, velocity) {
fetch('airplanes',
    {
        method: 'POST',
        body: JSON.stringify({id: id2, type: type, capacity: capacity, velocity: velocity,
                                    currentLng: 2.5425, currentLat: 49.0139}),
        headers: {
            'Content-Type': 'application/json'
          },
        //this will come in handy later
        credentials: 'include',
        mode: 'cors'
    });
}

function calculateRoutes(){
fetch("routes/0" , {
        method: 'DELETE'
      })
     .then(() => {
     console.log('Routes caulculated!');
     window.location.reload(true);
     routesAlreadyCalculated = true;
      })
      .catch(err => {
        console.error(err)
    });
}
