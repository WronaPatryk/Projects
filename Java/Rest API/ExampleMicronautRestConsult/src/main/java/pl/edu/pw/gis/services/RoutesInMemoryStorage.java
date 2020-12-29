package pl.edu.pw.gis.services;

import io.micronaut.context.annotation.Primary;
import pl.edu.pw.gis.dto.Location;
import pl.edu.pw.gis.dto.Route;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Primary
@Singleton
public class RoutesInMemoryStorage implements RoutesStorage {
    private final List<Route> routes;
    private long counter = 0;

    public RoutesInMemoryStorage() {
        routes = new ArrayList<Route>();

        List<Location> locationList = new ArrayList<Location>();
        locationList.add( new Location(15.9, 125.5) );
        locationList.add( new Location(45.9, 85.5) );
        Route r1 = new Route(counter++, locationList);
        routes.add(r1);

        locationList = new ArrayList<Location>();
        locationList.add( new Location(35.9, 125.15) );
        locationList.add( new Location(39.9, 25.5) );
        Route r2 = new Route(counter++, locationList);
        routes.add( r2 );
    }

    @Override
    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public boolean addRoute( Route route) {
        return routes.add( route );
    }

    @Override
    public boolean deleteRoute(int routeId) {
        if(routes.get( routeId ) != null) {
            routes.remove( routeId);
            return true;
        }
        return false;
    }

    @Override
    public boolean putRoute(int routeId, Route route) {
        if(routes.get( routeId ) != null){
            routes.get( routeId ).setCoordinates( route.getLocations() );
            return true;
        } else return false;
    }

    @Override
    public boolean deleteAllRoutes() {
        if(!routes.isEmpty()){
            routes.clear();
            return true;
        }
        return false;
    }


}