package com.rungta.campus.model;

public class Institution {
    private String id;
    private String name;
    private String shortName;
    private String type;
    private String description;
    private String website;
    private int establishedYear;
    private boolean isVerified;
    private String sourceUrl;

    public Institution() {}

    public Institution(String id, String name, String shortName, String type, String description, String website, int establishedYear, boolean isVerified, String sourceUrl) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.type = type;
        this.description = description;
        this.website = website;
        this.establishedYear = establishedYear;
        this.isVerified = isVerified;
        this.sourceUrl = sourceUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public int getEstablishedYear() { return establishedYear; }
    public void setEstablishedYear(int establishedYear) { this.establishedYear = establishedYear; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
}
