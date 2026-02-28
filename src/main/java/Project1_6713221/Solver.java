package Project1_6713221;

//6713115 Kornchanok Phutrakul
//6713117 Nuttha Limkhunthammo
//6713221 jakkarin roemtangsakul

import java.util.*;

public class Solver {
    // explicit stack สำหรับ backtracking
    private Deque<Moverecord> stack = new ArrayDeque<>();

    // เก็บ solution steps ที่หาได้
    private List<Moverecord> solution = new ArrayList<>();

    // เก็บ board state ที่เคยเจอแล้ว
    private Set<Long> visitedStates = new HashSet<>();

    // Zobrist table
    private long[][] zobrist;
    private int boardSize;

    private void initZobrist(int size) {
        if (boardSize == size) return;
        boardSize = size;
        zobrist = new long[size][2];
        Random rng = new Random(12345);
        for (int i = 0; i < size; i++) {
            zobrist[i][0] = rng.nextLong();
            zobrist[i][1] = rng.nextLong();
        }
    }

    private long hashBoard(Board board) {
        Marble[] cells = board.getCells();
        long hash = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] != null) {
                hash ^= zobrist[i][cells[i].isWhite() ? 0 : 1];
            }
        }
        return hash;
    }

    /**
     * Entry point
     * ใช้ iterative loop แทน recursion เพื่อป้องกัน StackOverflowError
     */
    public boolean solve(Board board) {
        stack.clear();
        solution.clear();
        visitedStates.clear();

        initZobrist(board.getSize());
        visitedStates.add(hashBoard(board));

        while (true) {
            if (board.isGoal()) {
                solution = new ArrayList<>(stack);
                Collections.reverse(solution);
                // reset board กลับ initial state
                for (Moverecord r : stack) {
                    board.undoMove(r.getMarbleId(), r.getOldPosition());
                }
                return true;
            }

            List<String> validOptions = getValidOptions(board);

            if (!validOptions.isEmpty()) {
                String chosen = validOptions.get(0);
                List<String> remaining = new ArrayList<>(validOptions.subList(1, validOptions.size()));

                int oldPos = board.getMarbleById(chosen).getPosition();
                String moveType = board.move(chosen);

                stack.push(new Moverecord(chosen, oldPos, moveType, remaining));
                visitedStates.add(hashBoard(board));

            } else {
                if (noSolution()) return false;

                boolean foundAlternative = false;
                while (!stack.isEmpty()) {
                    Moverecord record = stack.pop();
                    board.undoMove(record.getMarbleId(), record.getOldPosition());
                    visitedStates.remove(hashBoard(board));

                    List<String> remaining = record.getRemainingOptions();
                    if (!remaining.isEmpty()) {
                        String nextOption = remaining.get(0);
                        List<String> newRemaining = new ArrayList<>(remaining.subList(1, remaining.size()));

                        int oldPos = board.getMarbleById(nextOption).getPosition();
                        String moveType = board.move(nextOption);

                        stack.push(new Moverecord(nextOption, oldPos, moveType, newRemaining));
                        visitedStates.add(hashBoard(board));
                        foundAlternative = true;
                        break;
                    }
                }

                if (!foundAlternative) return false;
            }
        }
    }

    /**
     * สร้าง group sizes ตาม optimal pattern ของปริศนา Reversing Marbles
     *
     * Pattern: [1, 2, 3, ..., n, n, n, n-1, ..., 2, 1]  (2n+1 groups)
     * สลับสี W, B, W, B, ... เริ่มด้วย W
     * รวม n^2 + 2n steps
     */
    private int[] buildGroupSizes(int n) {
        int[] gs = new int[2 * n + 1];
        for (int i = 0; i < n; i++)        gs[i]         = i + 1;
        gs[n]     = n;
        gs[n + 1] = n;
        for (int i = 0; i < n - 1; i++)    gs[n + 2 + i] = n - 1 - i;
        return gs;
    }

    /**
     * คำนวณสีที่ควรเดิน ณ step ที่ stepsDone (0-indexed)
     */
    private String expectedColorAt(int n, int stepsDone) {
        int[] gs = buildGroupSizes(n);
        int acc = 0;
        for (int gi = 0; gi < gs.length; gi++) {
            acc += gs[gi];
            if (stepsDone < acc) {
                return (gi % 2 == 0) ? "white" : "black";
            }
        }
        return "white";
    }

    /**
     * คำนวณ priority ของ marble ที่จะเดิน (ยิ่งน้อยยิ่งดี)
     *
     * ใช้ pattern group-aware เพื่อ guide backtracking:
     *   - สีตรงกับที่ควรเดินใน step นี้ → penalty = 0
     *   - สีผิด → penalty = 100
     *   - tiebreak: jump ก่อน move
     *
     * ผลลัพธ์: สำหรับ initial board state ไม่ต้อง backtrack เลย (O(n^2))
     *          สำหรับ non-standard state: backtracking ยังทำงานได้ตามปกติ
     */
    private int priority(Board board, String marbleId, String moveType) {
        int n = board.getN();
        String expectedColor = expectedColorAt(n, stack.size());
        String thisColor = board.getMarbleById(marbleId).isWhite() ? "white" : "black";

        int colorPenalty = thisColor.equals(expectedColor) ? 0 : 100;
        int jumpBonus    = moveType.startsWith("Jump") ? 0 : 1;
        return colorPenalty + jumpBonus;
    }

    private List<String> getValidOptions(Board board) {
        List<String> marbleIds = new ArrayList<>();
        List<Integer> scores   = new ArrayList<>();

        for (String marbleId : board.getMovableMarbles()) {
            int oldPos = board.getMarbleById(marbleId).getPosition();
            String moveType = board.move(marbleId);
            long nextHash = hashBoard(board);
            board.undoMove(marbleId, oldPos);

            if (!visitedStates.contains(nextHash)) {
                marbleIds.add(marbleId);
                scores.add(priority(board, marbleId, moveType));
            }
        }

        // เรียงตาม priority น้อย → มาก
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < marbleIds.size(); i++) idx.add(i);
        idx.sort((a, b) -> scores.get(a) - scores.get(b));

        List<String> result = new ArrayList<>();
        for (int i : idx) result.add(marbleIds.get(i));
        return result;
    }

    public boolean noSolution() {
        return stack.isEmpty();
    }

    public List<Moverecord> getSolution() {
        return solution;
    }

    public Deque<Moverecord> getStack() {
        return stack;
    }
}