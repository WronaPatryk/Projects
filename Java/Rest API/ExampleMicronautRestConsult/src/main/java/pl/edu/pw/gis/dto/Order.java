package pl.edu.pw.gis.dto;

public class Order {
    private long id;
    private double lng;
    private double lat;
    private String name;
    private String surname;
    private String address;
    private String country;
    private String contactNumber;
    private boolean isPickup;
    private double weight;


    private Order(){};

    public Order(long id, double lng, double lat, String name, String surname, String address, String country, String contactNumber, boolean isPickup, double weight) {
        this.id = id;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.country = country;
        this.contactNumber = contactNumber;
        this.isPickup = isPickup;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", lng=" + lng +
                ", lat=" + lat +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", isPickup=" + isPickup +
                ", weight=" + weight +
                '}';
    }
}
