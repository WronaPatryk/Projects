package pl.edu.pw.gis.services;


import pl.edu.pw.gis.dto.Airplane;

import java.util.List;

public interface AirplanesStorage {
    List<Airplane> getAirplanes();
    boolean addAirplane(Airplane airplane);
    boolean deleteAirplane(int airplaneId);
    boolean putAirplane(int airplaneId, Airplane airplane);
}
