package com.meetmyage.com.meetmyageapp;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import data.ApiClient;
import data.ApiInterface;
import data.model.Group;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestGroupsUnitTest {
    public RestGroupsUnitTest() {
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
}
