package Project1_6713221;
import java.util.*;

public class Solver {
    // explicit stack สำหรับ backtracking
    private Deque<Moverecord> stack = new ArrayDeque<>();

    // เก็บ solution steps ที่หาได้ (list ของ MoveRecord ตามลำดับ)
    private List<Moverecord> solution = new ArrayList<>();

    // เก็บ board state ที่เคยเจอแล้ว เพื่อไม่ให้วนซ้ำ
    private Set<String> visitedStates = new HashSet<>();

    /**
     * Entry point — รับ board (อาจเป็น state ที่ user เดินมาบางส่วนแล้ว)
     * return true ถ้าหา solution ได้, false ถ้าไม่มี solution
     */
    public boolean solve(Board board) {
        stack.clear();
        solution.clear();
        visitedStates.clear();

        visitedStates.add(board.boardToString());

        return forwardSearch(board);
    }

    /**
     * Forwarding step:
     * 1. เช็ค goal
     * 2. หา list ของ marble ที่เดินได้ทั้งหมด
     * 3. เดิน marble แรก, push ลง stack, เก็บ option ที่เหลือไว้ใน record
     * 4. ถ้าเดินต่อไม่ได้ → backtrack
     */
    private boolean forwardSearch(Board board) {
        // ถ้าถึง goal แล้ว
        if (board.isGoal()) {
            // stack เก็บแบบ LIFO ต้อง reverse เพื่อให้ได้ลำดับ step ที่ถูกต้อง
            solution = new ArrayList<>(stack);
            java.util.Collections.reverse(solution);
            return true;
        }

        // หา marble ที่เดินได้ทั้งหมด
        List<String> options = board.getMovableMarbles();

        // กรอง option ที่จะทำให้เกิด state ซ้ำออก
        List<String> validOptions = new ArrayList<>();
        for (String marbleId : options) {
            Board testBoard = board.clone();
            testBoard.move(marbleId);
            if (!visitedStates.contains(testBoard.boardToString())) {
                validOptions.add(marbleId);
            }
        }

        // ไม่มี option ที่ valid → backtrack
        if (validOptions.isEmpty()) {
            return backtrack(board);
        }

        // เลือก option แรก
        String chosen = validOptions.get(0);
        List<String> remaining = new ArrayList<>(validOptions.subList(1, validOptions.size()));

        int oldPos = board.getMarbleById(chosen).getPosition();
        String moveType = board.move(chosen);

        Moverecord record = new Moverecord(chosen, oldPos, moveType, remaining);
        stack.push(record);
        visitedStates.add(board.boardToString());

        // วนต่อ (forwarding)
        if (forwardSearch(board)) return true;

        // ถ้า forwardSearch ข้างบน return false แปลว่า backtrack ทำงานไปแล้ว
        return false;
    }

    /**
     * Backtracking step:
     * 1. ถ้า stack ว่าง → ไม่มี solution
     * 2. Pop record บนสุด → undoMove
     * 3. ถ้า record นั้นยังมี option เหลือ → ลอง option ถัดไป
     * 4. ถ้าหมด option → backtrack ซ้ำ (pop ต่อ)
     */
    private boolean backtrack(Board board) {
        while (!stack.isEmpty()) {
            Moverecord record = stack.pop();

            // ย้อน move กลับ
            board.undoMove(record.getMarbleId(), record.getOldPosition());
            visitedStates.remove(board.boardToString());

            // ยังมี option เหลือใน step นี้ไหม
            List<String> remaining = record.getRemainingOptions();
            if (!remaining.isEmpty()) {
                // ลอง option ถัดไป
                String nextOption = remaining.get(0);
                List<String> newRemaining = new ArrayList<>(remaining.subList(1, remaining.size()));

                int oldPos = board.getMarbleById(nextOption).getPosition();
                String moveType = board.move(nextOption);

                Moverecord newRecord = new Moverecord(nextOption, oldPos, moveType, newRemaining);
                stack.push(newRecord);
                visitedStates.add(board.boardToString());

                // forwarding จาก state ใหม่
                if (forwardSearch(board)) return true;
            }
            // ถ้าหมด option → loop ต่อ (pop record ถัดไป)
        }

        // stack ว่างแล้ว ไม่มี solution
        return false;
    }

    /**
     * คืน solution เป็น list ของ MoveRecord ตามลำดับ
     * ใช้ใน GameController.runAutoMode() เพื่อแสดงผลทีละ step
     */
    public List<Moverecord> getSolution() {
        return solution;
    }

    /**
     * คืน stack ปัจจุบัน (ใช้สำหรับ debug หรือแสดงใน report)
     */
    public Deque<Moverecord> getStack() {
        return stack;
    }
}
