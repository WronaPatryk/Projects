


function roundTo(value, places){
     var power = Math.pow(10, places);
     return Math.round(value * power) / power;
 }

var marker;
var mapLoaded = false;
var mymap;
var polylines = [];
var miniMarkers = [];
var myMovingMarkers = [];
const myIcon = L.icon({
   iconUrl: 'img/marker_icon.png',
});
myIcon.options.iconSize = [30, 30];

const myIcon2 = L.icon({
   iconUrl: 'img/package.png',
});
myIcon2.options.iconSize = [20, 20];

const airportIcon = L.icon({
	iconUrl: 'img/plane.png',
});
var airportX = 2.5425;
var airportY = 49.0139;
airportIcon.options.iconSize = [30, 30];
var planeMarker;

const airplaneIcon = L.icon({
	iconUrl: 'img/mini-plane.png',
});
airplaneIcon.options.iconSize = [20, 20];
airplaneIcon.options.iconAnchor = [10,10];

function loadMarkers()
{
	// nOfOrders = names.length;
	miniMarkers = new Array(nOfOrders);
	//console.log(nOfOrders);
	var xx, yy, mess, locc;

	for(i = 0; i < nOfOrders; i++)
	{
		locc = locations[i].split(" ");
		xx = locc[0].substring(1).slice(0, -1);
		yy = locc[1].substring(0).slice(0, -1);
		//console.log(xx);
		//console.log(yy);

		miniMarkers[i] = new L.marker([xx, yy], {icon: myIcon2}).addTo(mymap);
		mess = "Orderer name: " + names[i] + ' ' + surnames[i] + ' (' + countries[i] + ')';
		miniMarkers[i].bindPopup(mess).openPopup();
	}
}

function loadMap() {
	mymap = L.map('js-map-container').setView([2.54, 49],2);

	L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png ',
		{
			attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>',
			maxZoom: 19,
			id: 'osm.tiles'
		}).addTo(mymap);

	planeMarker = new L.marker([airportY, airportX], {icon: airportIcon}).addTo(mymap);

	mymap.on('click', function(e){
		var latlng = mymap.mouseEventToLatLng(e.originalEvent);
		var x = roundTo(latlng.lng, 6);
		var y = roundTo(latlng.lat, 6);
		var coordinates = '(' + x + ', ' + y + ')';
		document.getElementById("textField3_2").value = coordinates;

		if(mymap.hasLayer(marker))
			mymap.removeLayer(marker);
		marker = new L.marker([y, x], {icon: myIcon}).addTo(mymap);

		var message = "Here we deliver the package! <br> Location: " + document.getElementById('textField3_2').value;
		var option = document.getElementById('transportType1').checked;
		if(option)
			message = "Here you can get the package! <br> Location: " + document.getElementById('textField3_2').value;
		marker.bindPopup(message).openPopup();

		var audio = new Audio('audio/tototo.mp3');
		audio.play();
	});

	loadMarkers();
}

document.addEventListener("DOMContentLoaded", function (event) {
	var _selector = document.querySelector('input[name=mapCheckbox]');
	_selector.addEventListener('change', function (event) {
		if (_selector.checked) {
			document.getElementById('info').style.display='none' ;
			document.getElementById('js-map-container').style.display='block';

			window.dispatchEvent(new Event('resize'));
			if(!mapLoaded) {
				loadMap();
				mapLoaded = true;
			}
		} else {
			document.getElementById('info').style.display='block' ;
			document.getElementById('js-map-container').style.display='none';
		}
	});
});


function getRandomColor() {
    var letters = '0123456789ABC';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 13)];
    }
    return color;
}

function drawLines() {
	if(polylines.length > 0) {
		for (i = 0; i < polylines.length; i++)
			polylines[i].remove(mymap);
		polylines = [];
	}

	var colorHere;
	for(i = 0; i < nOfRoutes; i++) {
		colorHere = getRandomColor();

		var routeHere = [];
		for(j = 0; j < routes[i].length; j++)
			routeHere.push(routes[i][j]);

		console.log(routeHere);
		polylines.push(L.polyline(routeHere, {color: colorHere}).addTo(mymap));
	}
}

function airplaneTakeOff(){
    airPaths = [];

    for(s = 0; s < routes.length; s++){
        airPath = [];
        bearing = [];
        latLngRoute = latLngReverse(routes[s]);
        durations = calculateRouteDurations(routes[s], velocities[s]);
        bearings = calculateRouteBearings(routes[s]);
        // angles

        for(i = 0; i < latLngRoute.length; i++){
            airPath.push({"latLng": latLngRoute[i], "bearing": bearings[i], "duration": durations[i]});
        }
        airPaths.push(airPath);
    }
    for(j = 0; j < airPaths.length; j ++){
        airplaneTakeOffByPath(airPaths[j]);
    }

}


function airplaneTakeOffByPath(path){
                var m = L.marker(path[0].latLng, {
                    icon: airplaneIcon
                }).addTo(mymap);

                var drivePath= path.slice(1);

                function simulate() {
                    // var duration= 3000;
                    if(!drivePath.length) {
                        //drivePath= path.slice(1);
                        duration= 0;
                    }

                    var point= drivePath.shift();
                    if(!point) return;
                    m.slideTo(point.latLng, {
                        duration: point.duration,
                        rotationAngle: point.bearing
                    })
                    setTimeout(simulate, point.duration);
                }
                simulate();

}

function latLngReverse(route){
    table = [];
    for(var i = 0; i < route.length; i++){
        table.push([route[i].lat, route[i].lng]);
    }
    return table;
}

function calculateRouteDurations(route, velocity){
    distances = [];
    distances.push(0); // initial distance
    for(i = 1; i < route.length; i++){
        distances.push(300/velocity*1000*Math.sqrt(Math.pow((route[i].lat-route[i-1].lat),2)+Math.pow((route[i].lng-route[i-1].lng),2)));
    }
    return distances;
}
function calculateRouteBearings(route){
    bearings = [];
    bearings.push(0); // initial distance
    for(i = 1; i < route.length; i++){
        var x1 = route[i-1].lng;
        var x2 = route[i].lng;
        var y1 = route[i-1].lat;
        var y2 = route[i].lat;

        if((x2 >= x1) && (y2 >= y1)){
            var angle = Math.atan((x2-x1)/(y2-y1))*180/3.141592653589;
        }
        else if((x2 > x1) && (y2 < y1)){
            var angle = Math.atan((y1-y2)/(x2-x1))*180/3.141592653589 + 90;
             }
        else if((x2 <= x1) && (y2 <= y1)){
            var angle = Math.atan((x1-x2)/(y1-y2))*180/3.141592653589 + 180;
             }
        else if((x2 < x1) && (y2 > y1)){
            var angle = Math.atan((y2-y1)/(x1-x2))*180/3.141592653589 + 270;
              }
        bearings.push(angle);
    }
    return bearings;
}

