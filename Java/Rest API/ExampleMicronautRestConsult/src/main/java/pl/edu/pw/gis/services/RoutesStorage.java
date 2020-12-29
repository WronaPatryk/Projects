package pl.edu.pw.gis.services;

import pl.edu.pw.gis.dto.Route;
import java.util.List;

public interface RoutesStorage {
    List<Route> getRoutes();
    boolean addRoute(Route Route);
    boolean deleteRoute(int routeId);
    boolean putRoute(int routeId, Route route);
    boolean deleteAllRoutes();
}
