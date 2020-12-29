//this sets initializeApplication an entry point for javascript application
document.addEventListener( "DOMContentLoaded", initializeApplication, false );
function initializeApplication() {

    logOrders();  // takie sobie
    logAirplanes();
    logRoutes();


}

function logOrders() {
    fetch('orders')
        //gets the JSON out of the response
        .then(response => response.json())
        //prints it in the browsers console
        .then(displayData);
}
function displayData(data) {

    for (var i = 0; i < data.length; ++i) {
        // var p = document.createElement('p');
        // p.innerHTML = data[i].id + ': ' + data[i].lat + ',' + data[i].lng;
        // document.body.appendChild(p);


        // creating table
        ids[i] = data[i].id;
        names[i] = data[i].name;
        surnames[i] = data[i].surname;
        lats[i] = data[i].lat;
        lngs[i] = data[i].lng;
        addresses[i] = data[i].address;
        locations[i] = "(" + data[i].lat + ", " + data[i].lng +")";
        countries[i] = data[i].country;
        contactNumbers[i] = data[i].contactNumber;
        weights[i] = data[i].weight;
        isPickups[i] = data[i].pickup;


    }
    id = ids[ids.length-1];
    nOfOrders = names.length;
    setSelectOrderBox();
}

function logAirplanes() {
    fetch('airplanes')
        //gets the JSON out of the response
        .then(response => response.json())
        //prints it in the browsers console
        .then(displayAirplanes);
}
function displayAirplanes(data) {

    for (var i = 0; i < data.length; ++i) {
        ids2[i] = data[i].id;
        types[i] = data[i].type;
        capacities[i] = data[i].capacity;
        velocities[i] = data[i].velocity;
        currentLngs[i] = data[i].lng;
        currentLats[i] = data[i].lat;
    }
    nOfAirplanes = types.length;
    setAirplaneBox();
}

function logRoutes() {
    fetch('routes')
        //gets the JSON out of the response
        .then(response => response.json())
        //prints it in the browsers console
        .then(displayRoutes);
}
function displayRoutes(data) {

    for (var i = 0; i < data.length; ++i) {
        ids3[i] = data[i].id;
        routes[i] = data[i].locations;
    }
    nOfRoutes = routes.length;
    // setSelectOrderBox();
}


function setSelectOrderBox(){
    document.getElementById('deleteSelect').innerHTML = '';
    for(i = 0; i < nOfOrders; i++) {
        document.getElementById('deleteSelect').innerHTML += '<option value=' + i + '>' + names[i] + ' ' + surnames[i] + '</option>';
    }
}

function setAirplaneBox(){
    document.getElementById('airplaneSelect').innerHTML = '';
    for(i = 0; i < nOfAirplanes; i++) {
        document.getElementById('airplaneSelect').innerHTML += '<option value=' + i + '>' + types[i] + ', cap = ' + capacities[i] + '</option>';
    }
}