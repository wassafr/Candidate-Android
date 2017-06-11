
package com.wassa.candidate.model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author khadir
 * @since 10/06/2017
 */
public class Place extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String id;
    @SerializedName("label")
    private String label;
    @SerializedName("country")
    private String country;
    @SerializedName("image_id")
    private String imageId;
    @SerializedName("image_author")
    private String imageAuthor;
    @SerializedName("image_credit")
    private String imageCredit;
    @SerializedName("description")
    private String description;
    @SerializedName("latitiude")
    private Double latitiude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("favorite")
    private boolean favorite = false;

    /**
     * No args constructor for use in serialization
     */
    public Place() {
    }

    /**
     * @param id
     * @param imageId
     * @param description
     * @param imageCredit
     * @param imageAuthor
     * @param longitude
     * @param label
     * @param latitiude
     * @param country
     */
    public Place(String id, String label, String country, String imageId, String imageAuthor, String imageCredit, String description, Double latitiude, Double longitude, boolean favorite) {
        super();
        this.id = id;
        this.label = label;
        this.country = country;
        this.imageId = imageId;
        this.imageAuthor = imageAuthor;
        this.imageCredit = imageCredit;
        this.description = description;
        this.latitiude = latitiude;
        this.longitude = longitude;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageAuthor() {
        return imageAuthor;
    }

    public void setImageAuthor(String imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    public String getImageCredit() {
        return imageCredit;
    }

    public void setImageCredit(String imageCredit) {
        this.imageCredit = imageCredit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitiude() {
        return latitiude;
    }

    public void setLatitiude(Double latitiude) {
        this.latitiude = latitiude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(label).append(country).append(imageId).append(imageAuthor).append(imageCredit).append(description).append(latitiude).append(longitude).append(favorite).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Place) == false) {
            return false;
        }
        Place rhs = ((Place) other);
        return new EqualsBuilder().append(id, rhs.id).append(label, rhs.label).append(country, rhs.country).append(imageId, rhs.imageId).append(imageAuthor, rhs.imageAuthor).append(imageCredit, rhs.imageCredit).append(description, rhs.description).append(latitiude, rhs.latitiude).append(longitude, rhs.longitude).append(favorite, rhs.favorite).isEquals();
    }

}
