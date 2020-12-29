package pl.edu.pw.gis.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import pl.edu.pw.gis.dto.Airplane;
import pl.edu.pw.gis.dto.Location;
import pl.edu.pw.gis.dto.Order;
import pl.edu.pw.gis.dto.Route;
import pl.edu.pw.gis.services.AirplanesInMemoryStorage;
import pl.edu.pw.gis.services.Calculator;
import pl.edu.pw.gis.services.RoutesStorage;

import java.util.Collections;
import java.util.List;

@Controller("/routes")
public class RoutesController {
    public static RoutesStorage _routesStorage;

    public RoutesController(RoutesStorage routesStorage) {
        this._routesStorage = routesStorage;
    }

    @Get()
    HttpResponse<List<Route>> getRoutes() {
        return HttpResponse.ok( _routesStorage.getRoutes());
    }

    @Delete("/{id}")
    HttpResponse<Route> deleteRoute(@PathVariable Integer id){
        if(_routesStorage.deleteRoute( id )){
            Calculator c = new Calculator();
            c.findRoutes();
            return HttpResponse.ok();
        } else {
            return HttpResponse.notModified();
        }
    }

    @Delete()
    HttpResponse<Route> deleteAllRoutes(){
        if(_routesStorage.deleteAllRoutes()){
            return HttpResponse.ok();
        } else {
            return HttpResponse.notModified();
        }
    }

    @Put("/{id}")
    HttpResponse<Route> putRoute(@PathVariable Integer id, Route route) {
        if (_routesStorage.putRoute( id, route )) {
            return HttpResponse.created( route );
        } else {
            return HttpResponse.badRequest();
        }
    }

    @Post()
    HttpResponse<Route> postRoute(Route route) {
        // dodaj
        System.out.println("IM ALIVE");
        if (_routesStorage.addRoute(route)) {
            return HttpResponse.created( route );
        } else {
            return HttpResponse.badRequest();
        }
    }

    // dł listy = dł listy samolotów
    // Route =  id, lista (Location)


}
