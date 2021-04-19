package sample;

public class TableRow {
    private String uName;
    private String fName;
    private String lName;
    private int gWon;
    private int gPlayed;
    private double winRate;

    public TableRow(String uName, String fName, String lName, int gWon, int gPlayed) {//a row in box score
        this.uName = uName;
        this.fName = fName;
        this.lName = lName;
        this.gWon = gWon;
        this.gPlayed = gPlayed;
        double temp=0;
        if (gPlayed>0)
            temp=((double) gWon) /gPlayed;
        double scale = Math.pow(10, 3);//rouding score to three decimal
        double result = Math.round(temp * scale) / scale;
        this.winRate=result;
    }

    public String getUName() {
        return uName;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public int getGWon() {
        return gWon;
    }

    public int getGPlayed() {
        return gPlayed;
    }

    public double getWinRate() {
        return winRate;
    }

    @Override
    public String toString() {
        return "TableRow{" +
                "uName='" + uName + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", gWon=" + gWon +
                ", gPlayed=" + gPlayed +
                ", winRate=" + winRate +
                '}';
    }

}
