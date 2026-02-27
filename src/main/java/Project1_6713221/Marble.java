package Project1_6713221;

//6713115 Kornchanok Phutrakul
//6713117 Nuttha Limkhunthammo
//6713221 jakkarin roemtangsakul


public class Marble {
    private String id;       // e.g. "w0", "w1", "b0", "b1"
    private String color;    // ขาว ดำ
    private int position;

    public Marble(String id, String color, int position) {
        this.id = id;
        this.color = color;
        this.position = position;
    }

    public String getId() { return id; }
    public String getColor() { return color; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public boolean isWhite() { return color.equals("white"); }
    public boolean isBlack() { return color.equals("black"); }

    // Deep copy
    public Marble clone() {
        return new Marble(id, color, position);
    }

    @Override
    public String toString() { return id; }
}
