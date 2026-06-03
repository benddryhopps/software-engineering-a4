public class Driver {

    private String driverID;
    private String name;
    private String streetNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private String dob;
    private int experienceYears;
    private String licenseType;

    public Driver(
            String driverID,
            String name,
            String streetNumber,
            String streetName,
            String city,
            String state,
            String country,
            String dob,
            int experienceYears,
            String licenseType) {

        this.driverID = driverID;
        this.name = name;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.dob = dob;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getName() {
        return name;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getLicenseType() {
        return licenseType;
    }
}
