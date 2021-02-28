package xyz.sbely.financemanager.model;

public class GraphPoint {
    private float sum;
    private float date;

    public GraphPoint(float sum, float date) {
        this.sum = sum;
        this.date = date;
    }

    public float getSum() {
        return sum;
    }

    public float getDate() {
        return date;
    }

    public void setSum(float sum){
        this.sum = sum;
    }
}
