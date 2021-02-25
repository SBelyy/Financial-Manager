package xyz.sbely.financemanager.model;

public class HistoryDebt {
    private String type;
    private String sum;
    private String comment;
    private String date;

    public HistoryDebt(String type, String sum, String comment, String date) {
        this.type = type;
        this.sum = sum;
        this.comment = comment;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public String getSum() {
        return sum;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}
