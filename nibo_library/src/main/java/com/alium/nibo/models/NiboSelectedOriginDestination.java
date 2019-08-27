package com.alium.nibo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.alium.nibo.autocompletesearchbar.NiboSearchSuggestionItem;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by abdulmujibaliu on 9/12/17.
 */

public class NiboSelectedOriginDestination implements Parcelable {
    private NiboSearchSuggestionItem originItem;
    private NiboSearchSuggestionItem destinationItem;

    private LatLng originLatLng;
    private LatLng destinationLatLng;
    private String startDate, endDate, journeyTime;

    public boolean isOriginValid() {
        if (originItem != null && originLatLng != null) {
            return true;
        } else
            return false;
    }

    public boolean isDestinationValid() {
        if (destinationItem != null && destinationLatLng != null) {
            return true;
        } else
            return false;
    }

    public void setOriginItem(NiboSearchSuggestionItem originItem) {
        this.originItem = originItem;
    }

    public void setDestinationItem(NiboSearchSuggestionItem destinationItem) {
        this.destinationItem = destinationItem;
    }

    public String getOriginShortName() {
        return originItem.getShortTitle();
    }

    public String getDestinationShortName() {
        return destinationItem.getShortTitle();
    }

    public String getDestinationLongName() {
        return destinationItem.getLongTitle();
    }

    public String getOriginLongName() {
        return originItem.getLongTitle();
    }

    public String getOriginFullName() {
        return originItem.getFullTitle();
    }

    public String getDestinationFullName() {
        return destinationItem.getFullTitle();
    }

    public String getDestinationPlaceID() {
        return destinationItem.getPlaceID();
    }

    public String getOriginPlaceID() {
        return originItem.getPlaceID();
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

    public LatLng getOriginLatLng() {
        return originLatLng;
    }

    public void setOriginLatLng(LatLng originLatLng) {
        this.originLatLng = originLatLng;
    }

    public LatLng getDestinationLatLng() {
        return destinationLatLng;
    }

    public void setDestinationLatLng(LatLng destinationLatLng) {
        this.destinationLatLng = destinationLatLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.originItem, flags);
        dest.writeParcelable(this.destinationItem, flags);
        dest.writeParcelable(this.originLatLng, flags);
        dest.writeParcelable(this.destinationLatLng, flags);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.journeyTime);
    }

    public NiboSelectedOriginDestination() {
    }

    protected NiboSelectedOriginDestination(Parcel in) {
        this.originItem = in.readParcelable(NiboSearchSuggestionItem.class.getClassLoader());
        this.destinationItem = in.readParcelable(NiboSearchSuggestionItem.class.getClassLoader());
        this.originLatLng = in.readParcelable(LatLng.class.getClassLoader());
        this.destinationLatLng = in.readParcelable(LatLng.class.getClassLoader());
        this.startDate=in.readString();
        this.endDate=in.readString();
        this.journeyTime=in.readString();
    }

    public static final Parcelable.Creator<NiboSelectedOriginDestination> CREATOR = new Parcelable.Creator<NiboSelectedOriginDestination>() {
        @Override
        public NiboSelectedOriginDestination createFromParcel(Parcel source) {
            return new NiboSelectedOriginDestination(source);
        }

        @Override
        public NiboSelectedOriginDestination[] newArray(int size) {
            return new NiboSelectedOriginDestination[size];
        }
    };

    @Override
    public String toString() {
        return "NiboSelectedOriginDestination{" +
                "originLatLng=" + originLatLng +
                ", destinationLatLng=" + destinationLatLng +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", journeyTime='" + journeyTime + '\'' +
                '}';
    }
}
