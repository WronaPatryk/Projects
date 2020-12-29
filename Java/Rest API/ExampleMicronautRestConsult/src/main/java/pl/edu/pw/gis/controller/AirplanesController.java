package pl.edu.pw.gis.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import pl.edu.pw.gis.dto.Airplane;
import pl.edu.pw.gis.services.AirplanesStorage;

import java.util.List;

@Controller("/airplanes")
public class AirplanesController {
    public static AirplanesStorage _airplanesStorage;

    public AirplanesController(AirplanesStorage airplanesStorage) {
        this._airplanesStorage = airplanesStorage;
    }

    @Get()
    HttpResponse<List<Airplane>> getAirplanes() {
        return HttpResponse.ok( _airplanesStorage.getAirplanes());
    }


    @Delete("/{id}")
    HttpResponse<Airplane> deleteAirplane(@PathVariable Integer id){
        if(_airplanesStorage.deleteAirplane( id )){
            return HttpResponse.ok();
        } else {
            return HttpResponse.notModified();
        }
    }
    @Put("/{id}")
    HttpResponse<Airplane> putAirplane(@PathVariable Integer id, Airplane airplane) {
        if (_airplanesStorage.putAirplane( id, airplane )) {
            return HttpResponse.created( airplane );
        } else {
            return HttpResponse.badRequest();
        }
    }

    @Post()
    HttpResponse<Airplane> postAirplane(Airplane airplane) {
        if (_airplanesStorage.addAirplane( airplane )) {
            return HttpResponse.created( airplane );
        } else {
            return HttpResponse.badRequest();
        }
    }
}
