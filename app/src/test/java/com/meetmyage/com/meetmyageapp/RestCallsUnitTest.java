package com.meetmyage.com.meetmyageapp;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import data.ApiClient;
import data.ApiInterface;
import data.GMapApiClient;
import data.GMapApiInterface;
import data.model.Group;
import data.model.gmaps.GmapResponse;
import data.model.gmaps.Result;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestCallsUnitTest {
    public RestCallsUnitTest() {
    }

    @Test
    public void testLoadRecommendedGroupsForProfile() {
        Retrofit myApiClient = ApiClient.getClient();
        ApiInterface myApiInterface = myApiClient.create(ApiInterface.class);
        Call<List<Group>> myRecommendedGroups =  myApiInterface.getRecommendedGroupsForProfile(4);
        try {
             Response<List<Group>> myResponses = myRecommendedGroups.execute();
             List<Group> myGroups =  myResponses.body();
             for (Group myTempGroup:myGroups) {
                 System.out.println("GROUP_NAME:"+myTempGroup.getGroupName() + "  GROUP_STORY:" + myTempGroup.getGroupStory());
             }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testGetLocationForLatLong() {
        Retrofit myApiClient = GMapApiClient.getClientForGMAP();
        GMapApiInterface myApiInterface = myApiClient.create(GMapApiInterface.class);
        String myLatLong = "17.465338,78.368573";
        Call<GmapResponse> mySuggestedPlaces =  myApiInterface.getLocationForLatLong(myLatLong, GMapApiClient.GMAP_KEY);
        try {
            Response<GmapResponse> myResponses = mySuggestedPlaces.execute();
            GmapResponse myLocation =  myResponses.body();
            List<Result> myLocations = myLocation.getResults();
            for (Result myTempLocation:myLocations) {
                System.out.println(myTempLocation.getFormattedAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
