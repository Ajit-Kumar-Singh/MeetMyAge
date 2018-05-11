
package data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePost {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("story")
    @Expose
    private String story;

    @SerializedName("work")
    @Expose
    private String work;

    public ProfilePost(String name, String story, String work) {
        this.name = name;
        this.story = story;
        this.work = work;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

}
