package pl.edu.pw.gis.dto;

import java.util.List;

public class Route {
    private long id;
    private List<Location> locations;

    public Route(long id, List<Location> locations) {
        this.id = id;
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setCoordinates(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", locations=" + locations +
                '}';
    }
}
