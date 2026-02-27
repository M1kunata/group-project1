package Project1_6713221;

//6713115 Kornchanok Phutrakul
//6713117 Nuttha Limkhunthammo
//6713221 jakkarin roemtangsakul

import java.util.List;
public class Moverecord {
    private String marbleId;       // หินที่เดินใน step นี้
    private int oldPosition;       // ตำแหน่งเดิมของหินก่อนเดิน (ใช้ undoMove)
    private String moveType;       // "Move right" / "Jump left" ฯลฯ
    private List<String> remainingOptions; // option ที่ยังไม่ได้ลองใน step นี้

    public Moverecord(String marbleId, int oldPosition, String moveType, List<String> remainingOptions) {
        this.marbleId = marbleId;
        this.oldPosition = oldPosition;
        this.moveType = moveType;
        this.remainingOptions = remainingOptions;
    }

    public String getMarbleId() { return marbleId; }
    public int getOldPosition() { return oldPosition; }
    public String getMoveType() { return moveType; }
    public List<String> getRemainingOptions() { return remainingOptions; }

    @Override
    public String toString() {
        return marbleId + " (" + moveType + ")";
    }
}
