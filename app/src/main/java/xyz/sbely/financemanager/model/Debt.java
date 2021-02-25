package xyz.sbely.financemanager.model;

public class Debt {

    private String date;
    private String name;
    private String comment;
    private String sum;
    private String id;

    public Debt(String name, String date, String sum, String comment, String id) {
        this.date = date;
        this.name = name;
        this.comment = comment;
        this.sum = sum;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getNameD() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getSum() {
        return sum;
    }

    public String getId() {
        return id;
    }
}
