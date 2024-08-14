package com.example.geofencing.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CustomLatLng implements Parcelable {
    private double latitude;
    private double longitude;

    public CustomLatLng() {

    }

    public CustomLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected CustomLatLng(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<CustomLatLng> CREATOR = new Creator<CustomLatLng>() {
        @Override
        public CustomLatLng createFromParcel(Parcel in) {
            return new CustomLatLng(in);
        }

        @Override
        public CustomLatLng[] newArray(int size) {
            return new CustomLatLng[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}