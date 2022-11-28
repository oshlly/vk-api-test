
package models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "screen_name",
    "is_closed",
    "type",
    "is_admin",
    "admin_level",
    "is_member",
    "is_advertiser",
    "photo_50",
    "photo_100",
    "photo_200"
})

public class Response {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("is_closed")
    private Integer isClosed;
    @JsonProperty("type")
    private String type;
    @JsonProperty("is_admin")
    private Integer isAdmin;
    @JsonProperty("admin_level")
    private Integer adminLevel;
    @JsonProperty("is_member")
    private Integer isMember;
    @JsonProperty("is_advertiser")
    private Integer isAdvertiser;
    @JsonProperty("photo_50")
    private String photo50;
    @JsonProperty("photo_100")
    private String photo100;
    @JsonProperty("photo_200")
    private String photo200;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("screen_name")
    public String getScreenName() {
        return screenName;
    }

    @JsonProperty("screen_name")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @JsonProperty("is_closed")
    public Integer getIsClosed() {
        return isClosed;
    }

    @JsonProperty("is_closed")
    public void setIsClosed(Integer isClosed) {
        this.isClosed = isClosed;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("is_admin")
    public Integer getIsAdmin() {
        return isAdmin;
    }

    @JsonProperty("is_admin")
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    @JsonProperty("admin_level")
    public Integer getAdminLevel() {
        return adminLevel;
    }

    @JsonProperty("admin_level")
    public void setAdminLevel(Integer adminLevel) {
        this.adminLevel = adminLevel;
    }

    @JsonProperty("is_member")
    public Integer getIsMember() {
        return isMember;
    }

    @JsonProperty("is_member")
    public void setIsMember(Integer isMember) {
        this.isMember = isMember;
    }

    @JsonProperty("is_advertiser")
    public Integer getIsAdvertiser() {
        return isAdvertiser;
    }

    @JsonProperty("is_advertiser")
    public void setIsAdvertiser(Integer isAdvertiser) {
        this.isAdvertiser = isAdvertiser;
    }

    @JsonProperty("photo_50")
    public String getPhoto50() {
        return photo50;
    }

    @JsonProperty("photo_50")
    public void setPhoto50(String photo50) {
        this.photo50 = photo50;
    }

    @JsonProperty("photo_100")
    public String getPhoto100() {
        return photo100;
    }

    @JsonProperty("photo_100")
    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }

    @JsonProperty("photo_200")
    public String getPhoto200() {
        return photo200;
    }

    @JsonProperty("photo_200")
    public void setPhoto200(String photo200) {
        this.photo200 = photo200;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
