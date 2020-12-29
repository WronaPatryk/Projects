package pl.edu.pw.gis.services;

import io.micronaut.context.annotation.Primary;
import pl.edu.pw.gis.dto.Airplane;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Primary
@Singleton
public class AirplanesInMemoryStorage implements AirplanesStorage {
    private final List<Airplane> airplanes;
    private long counter = 0;

    public final double airportX = 2.5425;
    public final double airportY = 49.0139;

    public AirplanesInMemoryStorage() {
        Airplane a1 = new Airplane(counter++, "Boeing 787-8", 5510, 250,
                airportX, airportY);
        Airplane a2 = new Airplane(counter++, "Boeing 697-7", 6270, 400,
                airportX, airportY);
        Airplane a3 = new Airplane(counter++, "Embraer 175", 2270, 200,
                airportX, airportY);
        Airplane a4 = new Airplane(counter++, "Embraer 185", 2770, 200,
                airportX, airportY);
        Airplane a5 = new Airplane(counter++, "Super Aircraft 2020", 12770, 350,
                airportX, airportY);
        airplanes = new ArrayList<>();
        airplanes.add(a1); airplanes.add(a2); airplanes.add(a3); airplanes.add(a4); airplanes.add(a5);
    }

    @Override
    public List<Airplane> getAirplanes() {
        return airplanes;
    }

    @Override
    public boolean addAirplane(Airplane airplane) {
        airplane.setId(counter++);
        return airplanes.add( airplane );
    }

    @Override
    public boolean deleteAirplane(int airplaneId) {
        if(airplanes.get( airplaneId ) != null) {
            airplanes.remove( airplaneId);
            return true;
        }
        return false;
    }

    @Override
    public boolean putAirplane(int airplaneId, Airplane airplane) {
        if(airplanes.get( airplaneId ) != null){
            airplanes.get( airplaneId ).setCapacity( airplane.getCapacity() );
            airplanes.get( airplaneId ).setType( airplane.getType() );
            return true;
        } else return false;
    }


}
