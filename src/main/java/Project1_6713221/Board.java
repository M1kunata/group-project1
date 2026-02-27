package Project1_6713221;

//6713115 Kornchanok Phutrakul
//6713117 Nuttha Limkhunthammo
//6713221 jakkarin roemtangsakul

import java.util.ArrayList;
import java.util.List;
public class Board {
    private int n;
    // cells[i] = Marble object or null (empty space)
    private Marble[] cells;
    private int emptyPos;

    // Constructor: สร้างกระดานเริ่มต้น w0 w1 ... __ b0 b1 ...
    public Board(int n) {
        this.n = n;
        this.cells = new Marble[2 * n + 1];
        createBoard();
    }

    // สร้างกระดานเริ่มต้น
    private void createBoard() {
        // วางหินขาวทางซ้าย
        for (int i = 0; i < n; i++) {
            Marble m = new Marble("w" + i, "white", i);
            cells[i] = m;
        }
        // ช่องว่างตรงกลาง
        cells[n] = null;
        emptyPos = n;
        // วางหินดำทางขวา
        for (int i = 0; i < n; i++) {
            Marble m = new Marble("b" + i, "black", n + 1 + i);
            cells[n + 1 + i] = m;
        }
    }

    // Constructor สำหรับ clone
    private Board(int n, Marble[] cells, int emptyPos) {
        this.n = n;
        this.emptyPos = emptyPos;
        this.cells = new Marble[cells.length];
        for (int i = 0; i < cells.length; i++) {
            this.cells[i] = (cells[i] == null) ? null : cells[i].clone();
        }
    }

    // Deep copy ของกระดาน
    public Board clone() {
        return new Board(n, cells, emptyPos);
    }

    // ดึง Marble จาก ID เช่น "w0"
    public Marble getMarbleById(String id) {
        for (Marble m : cells) {
            if (m != null && m.getId().equals(id)) return m;
        }
        return null;
    }

    public int getEmptyPos() { return emptyPos; }
    public int getSize() { return cells.length; }
    public int getN() { return n; }
    public Marble[] getCells() { return cells; }

    // เช็คว่าหินตัวนี้เดินได้ไหม
    public boolean isMovable(String marbleId) {
        Marble m = getMarbleById(marbleId);
        if (m == null) return false;
        int pos = m.getPosition();
        int size = cells.length;

        if (m.isWhite()) {
            // เดินขวา 1 ช่อง
            if (pos + 1 < size && pos + 1 == emptyPos) return true;
            // กระโดดข้ามหินดำไปขวา
            if (pos + 2 < size && pos + 2 == emptyPos
                    && cells[pos + 1] != null && cells[pos + 1].isBlack()) return true;
        } else {
            // เดินซ้าย 1 ช่อง
            if (pos - 1 >= 0 && pos - 1 == emptyPos) return true;
            // กระโดดข้ามหินขาวไปซ้าย
            if (pos - 2 >= 0 && pos - 2 == emptyPos
                    && cells[pos - 1] != null && cells[pos - 1].isWhite()) return true;
        }
        return false;
    }

    // เดินหิน return "Move right" / "Jump right" / "Move left" / "Jump left"
    public String move(String marbleId) {
        Marble m = getMarbleById(marbleId);
        if (m == null || !isMovable(marbleId)) return null;

        int pos = m.getPosition();
        String result;

        if (m.isWhite()) {
            if (pos + 1 == emptyPos) {
                result = "Move right";
            } else {
                result = "Jump right";
            }
        } else {
            if (pos - 1 == emptyPos) {
                result = "Move left";
            } else {
                result = "Jump left";
            }
        }

        // สลับตำแหน่งหินกับช่องว่าง
        cells[emptyPos] = m;
        cells[pos] = null;
        m.setPosition(emptyPos);
        emptyPos = pos;

        return result;
    }

    // ย้อน move กลับ โดยรับตำแหน่งเดิมของหินก่อนเดิน
    public void undoMove(String marbleId, int oldPosition) {
        Marble m = getMarbleById(marbleId);
        if (m == null) return;

        int currentPos = m.getPosition();
        cells[oldPosition] = m;
        cells[currentPos] = null;
        m.setPosition(oldPosition);
        emptyPos = currentPos;
    }

    // คืน list ของ marble ID ที่เดินได้ทั้งหมด
    public List<String> getMovableMarbles() {
        List<String> movable = new ArrayList<>();
        for (Marble m : cells) {
            if (m != null && isMovable(m.getId())) {
                movable.add(m.getId());
            }
        }
        return movable;
    }

    // เช็ค goal state: หินดำอยู่ซ้าย หินขาวอยู่ขวา
    public boolean isGoal() {
        if (cells[n] != null) return false;
        for (int i = 0; i < n; i++) {
            if (cells[i] == null || !cells[i].isBlack()) return false;
        }
        for (int i = n + 1; i < cells.length; i++) {
            if (cells[i] == null || !cells[i].isWhite()) return false;
        }
        return true;
    }

    // แสดงกระดานออกหน้าจอ
    public void display() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == null) {
                sb.append("__");
            } else {
                sb.append(cells[i].getId());
            }
            if (i < cells.length - 1) sb.append(" ");
        }
        System.out.println(sb.toString());
    }

    // แสดงกระดานแบบ return String (ใช้ใน Solver เพื่อเก็บ history)
    public String boardToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == null) {
                sb.append("__");
            } else {
                sb.append(cells[i].getId());
            }
            if (i < cells.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}