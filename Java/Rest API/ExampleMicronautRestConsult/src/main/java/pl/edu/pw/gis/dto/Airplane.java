package pl.edu.pw.gis.dto;

public class Airplane {

    private long id;
    private String type;
    private double capacity;
    private double velocity;
    private double currentLng;
    private double currentLat;

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(double currentLng) {
        this.currentLng = currentLng;
    }

    public Airplane() {
    }

    public Airplane(long id, String type, double capacity, double velocity, double currentLng, double currentLat) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
        this.velocity = velocity;
        this.currentLat = currentLat;
        this.currentLng = currentLng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", currentLng=" + currentLng +
                ", currentLat=" + currentLat +
                '}';
    }
}
