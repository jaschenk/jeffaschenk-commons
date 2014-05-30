package jeffaschenk.commons.touchpoint.model.serviceprovider;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * FacebookLocationProfile
 *
 * Provides a JSON driven class to use for Restlet calls to
 * pull a Location from FB.
 * <p/>
 * Additional Information to set within an Application.
 *
 * @author jeffaschenk@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookLocationProfile {

    @JsonProperty("id")
    long id;

    @JsonProperty("name")
    String name;

    @JsonProperty("picture")
    String picture;

    @JsonProperty("link")
    String link;

    @JsonProperty("category")
    String category;

     @JsonProperty("is_community_page")
    boolean communityPage;

      @JsonProperty("description")
    String description;

     @JsonProperty("likes")
    long likes;

    public FacebookLocationProfile() { }


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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCommunityPage() {
        return communityPage;
    }

    public void setCommunityPage(boolean communityPage) {
        this.communityPage = communityPage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    /**
     * toString
     * @return String representation of this Object.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" Id:["+ this.id +"]");
        sb.append(", Name:["+ this.name +"]");
        sb.append(", Category:["+ this.category +"]");
        sb.append(", Picture:["+ this.picture +"]");
        sb.append(", Link:["+ this.link +"]");
        sb.append(", Community Page:["+ this.communityPage +"]");
        sb.append(", Description:["+ this.description +"]");
        sb.append(", Likes:["+ this.likes +"]");
        return sb.toString();
    }
}
