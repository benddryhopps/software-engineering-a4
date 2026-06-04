package software_engineering;

public class Bus{

    // Instance properties
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType;

    // Instance constructor
    public Bus(String busID, int capacity, double fuelLevel, String fuelType){
	System.out.println("Initialising Bus instance");
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
	System.out.println(this + " has been initialised succesfully!");
    }

    // Getters and setters
    public String getBusID() {
        return busID;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getFuelType() {
        return fuelType;
    }

    public double getFuelLevel(){
	    return fuelLevel;
    }

}
