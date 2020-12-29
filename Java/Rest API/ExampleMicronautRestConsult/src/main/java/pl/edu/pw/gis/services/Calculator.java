package pl.edu.pw.gis.services;

import pl.edu.pw.gis.controller.AirplanesController;
import pl.edu.pw.gis.controller.OrdersController;
import pl.edu.pw.gis.dto.Airplane;
import pl.edu.pw.gis.dto.Location;
import pl.edu.pw.gis.dto.Order;
import pl.edu.pw.gis.dto.Route;

import java.util.ArrayList;
import java.util.List;

import static pl.edu.pw.gis.controller.RoutesController._routesStorage;
import static pl.edu.pw.gis.controller.AirplanesController._airplanesStorage;

public class Calculator {

    public void findRoutes(){





        System.out.println("IM FINDING ROUTES");
        _routesStorage.getRoutes().clear();
        List<Airplane> airplanes = new ArrayList<>();
        airplanes = new ArrayList<>(_airplanesStorage.getAirplanes());

        double[] capacities = new double[airplanes.size()];
        for ( int i = 0; i < airplanes.size(); i++ ) {
            capacities[i] = airplanes.get( i ).getCapacity();
        }

        for ( Airplane airplane: airplanes ) {
            List<Location> list = new ArrayList<>();
            list.add( new Location(2.5425, 49.0139) );
            _routesStorage.addRoute( new Route(airplane.getId(), list) );
        }
        // printing
        _routesStorage.getRoutes().forEach( p -> System.out.println("ROUTE ID: " + p.getId()) );
        airplanes.forEach( a -> System.out.println("AIRPLANE ID: " + a.getId()) );
        System.out.println("");


        List<Order> orders = new ArrayList<>(OrdersController._ordersStorage.getOrders());

        double lng = 2.5425; // airportX
        double lat = 49.0139; // airportX

        if(orders.isEmpty() || airplanes.isEmpty() || _routesStorage.getRoutes().isEmpty()){
            return;
        }


        Order nearest = findTheNearest( lng, lat, orders );
        Airplane bestAirplane = findBestAirplane( nearest, airplanes );
        if(bestAirplane != null){
            _routesStorage.getRoutes().get((int) bestAirplane.getId()).getLocations().add( new Location(nearest.getLng(), nearest.getLat()));
            System.out.println("BEST AIRPLANE ID: " + bestAirplane.getId());
            lng = nearest.getLng();
            lat = nearest.getLat();
            bestAirplane.setCurrentLat( lat );  // bestAirplane
            bestAirplane.setCurrentLng( lng );
        }
        orders.remove( nearest );

        while(!orders.isEmpty()){
            System.out.println("orders are not empty");
            nearest = findTheNearest( lng, lat, orders );  // znajdz najblizszy order
            bestAirplane = findBestAirplane( nearest, airplanes );  // znajdz najblizszy samolot
            if(bestAirplane == null) {orders.remove( nearest ); continue;}
            System.out.println("BEST AIRPLANE ID: " + bestAirplane.getId());
            _routesStorage.getRoutes().get((int) bestAirplane.getId()).getLocations().add( new Location(nearest.getLng(), nearest.getLat()));

            lng = nearest.getLng(); // zgarnij koordynaty ordera
            lat = nearest.getLat();
            bestAirplane.setCurrentLat( lat );  // przesun samolot
            bestAirplane.setCurrentLng( lng );
            orders.remove( nearest );
        }
        int k = 0;
        for ( int i = 0; i < airplanes.size(); i++ ) {
            airplanes.get( i ).setCapacity( capacities[i] );
        }

        System.out.println("\nALL ROUTES:");
        _routesStorage.getRoutes().forEach( r -> System.out.println(r) );
    }

    public Order findTheNearest(double lng, double lat, List<Order> orders){
        double mini = Double.MAX_VALUE;
        double distance;
        Order result = null;
        for(Order order: orders){
            distance = Math.sqrt(Math.pow(lng-order.getLng(), 2) + Math.pow(lat-order.getLat(), 2));
            if(distance < mini) { // UWAGA! Pamietajmy zeby nie porownywac z samym soba
                mini = distance;
                result = order;
            }
        }
        return result;
    }

    public Airplane findBestAirplane(Order order, List<Airplane> airplanes){
        double mini = Double.MAX_VALUE;
        double distance;
        Airplane result = null;
        for(Airplane airplane: airplanes){
            distance = Math.sqrt(Math.pow( order.getLat() - airplane.getCurrentLat(),2 ) + Math.pow( order.getLng() - airplane.getCurrentLng() ,2 ));
            if(distance < mini && (((airplane.getCapacity() - order.getWeight()) >=0))) { // UWAGA! Pamietajmy zeby nie porownywac z samym soba
                mini = distance;
                result = airplane;
            }
        }
        if(result != null) result.setCapacity( result.getCapacity() - order.getWeight() );
        return result;
    }



}
