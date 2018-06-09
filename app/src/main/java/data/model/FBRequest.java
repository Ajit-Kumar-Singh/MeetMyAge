
package data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FBRequest {
    public FBRequest(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    @SerializedName("facebookToken")
    @Expose
    private String facebookToken;

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

}
