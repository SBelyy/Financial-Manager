package xyz.sbely.financemanager.model;

public class Category {

    private Integer name;
    private final Integer image;
    private String stringName;

    public Category(Integer name, Integer image) {
        this.name = name;
        this.image = image;
    }

    public Category(String name, Integer image) {
        stringName = name;
        this.image = image;
    }

    public Integer getName() {
        return name;
    }

    public String getStringName() { return stringName; }

    public Integer getImage() {
        return image;
    }
}
