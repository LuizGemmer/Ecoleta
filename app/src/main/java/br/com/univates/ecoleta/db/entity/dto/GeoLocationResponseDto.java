package br.com.univates.ecoleta.db.entity.dto;

import com.google.gson.annotations.SerializedName;

public class GeoLocationResponseDto {

    private String name;
    @SerializedName("display_name")
    private String displayName;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lon")
    private double longitude;

    // Getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
