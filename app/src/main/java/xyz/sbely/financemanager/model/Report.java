package xyz.sbely.financemanager.model;

public class Report {

    private String date;
    private String name;
    private String comment;
    private String sum;
    private String type;
    private int id;

    public Report(String type, String name, String date, String sum, String comment, int id) {
        this.date = date;
        this.name = name;
        this.comment = comment;
        this.sum = sum;
        this.type = type;
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

    public int getId() {
        return id;
    }

    public String getType(){ return type; }
}
