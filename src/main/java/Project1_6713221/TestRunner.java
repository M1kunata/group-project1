package Project1_6713221;
import java.util.List;
public class TestRunner {
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   EGCO221 Project1 - Test Runner");
        System.out.println("========================================\n");

        testMarble();
        testBoardCreation();
        testBoardDisplay();
        testIsMovable();
        testMove();
        testUndoMove();
        testGetMovableMarbles();
        testIsGoal();
        testSolverNormalState();
        testSolverInitialState1();  // w0 w1 __ b0 b1
        testSolverInitialState2();  // w0 b0 __ w1 b1
        testSolverNoSolution();

        System.out.println("\n========================================");
        System.out.printf("   Results: %d passed, %d failed%n", passed, failed);
        System.out.println("========================================");
    }

    // ─── Marble Tests ───────────────────────────────────────────

    static void testMarble() {
        System.out.println("--- Marble Tests ---");

        Marble w = new Marble("w0", "white", 0);
        assertTrue("Marble id", w.getId().equals("w0"));
        assertTrue("Marble isWhite", w.isWhite());
        assertTrue("Marble isBlack false", !w.isBlack());
        assertTrue("Marble position", w.getPosition() == 0);

        Marble b = new Marble("b0", "black", 3);
        assertTrue("Marble isBlack", b.isBlack());

        // test clone
        Marble wc = w.clone();
        wc.setPosition(99);
        assertTrue("Clone independent", w.getPosition() == 0); // original ไม่เปลี่ยน

        System.out.println();
    }

    // ─── Board Creation Tests ────────────────────────────────────

    static void testBoardCreation() {
        System.out.println("--- Board Creation Tests ---");

        Board b = new Board(2);
        assertTrue("Board size n=2", b.getSize() == 5);
        assertTrue("Empty at center", b.getEmptyPos() == 2);
        assertTrue("w0 at pos 0", b.getMarbleById("w0").getPosition() == 0);
        assertTrue("w1 at pos 1", b.getMarbleById("w1").getPosition() == 1);
        assertTrue("b0 at pos 3", b.getMarbleById("b0").getPosition() == 3);
        assertTrue("b1 at pos 4", b.getMarbleById("b1").getPosition() == 4);

        Board b3 = new Board(3);
        assertTrue("Board size n=3", b3.getSize() == 7);
        assertTrue("Empty at center n=3", b3.getEmptyPos() == 3);

        System.out.println();
    }

    // ─── Board Display Tests ─────────────────────────────────────

    static void testBoardDisplay() {
        System.out.println("--- Board Display Tests ---");

        Board b = new Board(2);
        String s = b.boardToString();
        assertTrue("Initial board n=2", s.equals("w0 w1 __ b0 b1"));
        System.out.println("  Display: " + s);

        System.out.println();
    }

    // ─── isMovable Tests ─────────────────────────────────────────

    static void testIsMovable() {
        System.out.println("--- isMovable Tests ---");

        // w0 w1 __ b0 b1
        Board b = new Board(2);

        // w0 อยู่ pos 0, empty pos 2 → ห่างกัน 2 แต่ cells[1] = w1 (white ไม่ใช่ black) → เดินไม่ได้
        assertTrue("w0 not movable (blocked by w1)", !b.isMovable("w0"));

        // w1 อยู่ pos 1, empty pos 2 → ห่าง 1 → เดินได้
        assertTrue("w1 movable (move right)", b.isMovable("w1"));

        // b0 อยู่ pos 3, empty pos 2 → ห่าง 1 → เดินได้
        assertTrue("b0 movable (move left)", b.isMovable("b0"));

        // b1 อยู่ pos 4, empty pos 2 → ห่าง 2 แต่ cells[3] = b0 (black ไม่ใช่ white) → เดินไม่ได้
        assertTrue("b1 not movable (blocked by b0)", !b.isMovable("b1"));

        // invalid ID
        assertTrue("invalid id not movable", !b.isMovable("x9"));

        System.out.println();
    }

    // ─── move Tests ──────────────────────────────────────────────

    static void testMove() {
        System.out.println("--- move Tests ---");

        // w0 w1 __ b0 b1 → เดิน w1
        Board b = new Board(2);
        String result = b.move("w1");
        assertTrue("w1 move type", result.equals("Move right"));
        assertTrue("w1 new position", b.getMarbleById("w1").getPosition() == 2);
        assertTrue("empty moved to 1", b.getEmptyPos() == 1);
        System.out.println("  After w1 move: " + b.boardToString());

        // เดิน b0 → jump ข้าม w1
        // state: w0 __ w1 b0 b1 → b0 อยู่ pos 3, empty pos 1, cells[2]=w1(white) → jump ได้
        Board b2 = new Board(2);
        b2.move("w1"); // w0 __ w1 b0 b1
        String result2 = b2.move("b0");
        assertTrue("b0 jump type", result2.equals("Jump left"));
        assertTrue("b0 jumped to pos 1", b2.getMarbleById("b0").getPosition() == 1);
        System.out.println("  After b0 jump: " + b2.boardToString());

        System.out.println();
    }

    // ─── undoMove Tests ──────────────────────────────────────────

    static void testUndoMove() {
        System.out.println("--- undoMove Tests ---");

        Board b = new Board(2);
        String before = b.boardToString();

        // เดิน w1 แล้วย้อนกลับ
        int oldPos = b.getMarbleById("w1").getPosition(); // 1
        b.move("w1");
        b.undoMove("w1", oldPos);

        assertTrue("Board restored after undo", b.boardToString().equals(before));
        assertTrue("Empty restored", b.getEmptyPos() == 2);
        System.out.println("  Before: " + before);
        System.out.println("  After undo: " + b.boardToString());

        System.out.println();
    }

    // ─── getMovableMarbles Tests ──────────────────────────────────

    static void testGetMovableMarbles() {
        System.out.println("--- getMovableMarbles Tests ---");

        // w0 w1 __ b0 b1 → w1 และ b0 เดินได้
        Board b = new Board(2);
        List<String> movable = b.getMovableMarbles();
        assertTrue("2 movable marbles", movable.size() == 2);
        assertTrue("w1 in movable", movable.contains("w1"));
        assertTrue("b0 in movable", movable.contains("b0"));
        System.out.println("  Movable: " + movable);

        System.out.println();
    }

    // ─── isGoal Tests ─────────────────────────────────────────────

    static void testIsGoal() {
        System.out.println("--- isGoal Tests ---");

        Board b = new Board(2);
        assertTrue("Initial is not goal", !b.isGoal());

        // สร้าง goal state: b0 b1 __ w0 w1 ด้วยมือ
        // ใช้ clone แล้วจัดตำแหน่ง
        Board goal = new Board(2);
        Marble[] cells = goal.getCells();
        // จัด manually
        cells[0] = new Marble("b0", "black", 0);
        cells[1] = new Marble("b1", "black", 1);
        cells[2] = null;
        cells[3] = new Marble("w0", "white", 3);
        cells[4] = new Marble("w1", "white", 4);

        assertTrue("Goal state detected", goal.isGoal());
        System.out.println("  Goal state: " + goal.boardToString());

        System.out.println();
    }

    // ─── Solver Tests ─────────────────────────────────────────────

    static void testSolverNormalState() {
        System.out.println("--- Solver: Normal Initial State n=2 ---");

        Board b = new Board(2);
        Board verify = b.clone(); // clone ก่อน solve เพราะ solve จะ mutate board
        Solver solver = new Solver();
        System.out.println("  Start: " + b.boardToString());

        boolean found = solver.solve(b);
        assertTrue("Solution found for n=2", found);

        if (found) {
            List<Moverecord> sol = solver.getSolution();
            System.out.println("  Steps: " + sol.size());
            for (int i = 0; i < sol.size(); i++) {
                System.out.printf("  Auto %2d >> %s%n", i + 1, sol.get(i));
            }
            // verify โดยเล่นซ้ำจาก board เริ่มต้น
            for (Moverecord r : sol) {
                verify.move(r.getMarbleId());
            }
            assertTrue("Solver reaches goal", verify.isGoal());
            System.out.println("  Final: " + verify.boardToString());
        }

        System.out.println();
    }

    // Initial state (1) จากโจทย์: w0 w1 __ b0 b1
    static void testSolverInitialState1() {
        System.out.println("--- Solver: Initial State (1) w0 w1 __ b0 b1 ---");

        Board b = new Board(2); // ตรงกับ initial state (1) พอดี
        Board verify = b.clone();
        Solver solver = new Solver();

        boolean found = solver.solve(b);
        assertTrue("Solution found for state 1", found);

        if (found) {
            List<Moverecord> sol = solver.getSolution();
            System.out.println("  Steps: " + sol.size());
            for (Moverecord r : sol) {
                String type = verify.move(r.getMarbleId());
                System.out.println("  " + r.getMarbleId() + " → " + type + " → " + verify.boardToString());
            }
            assertTrue("State1 reaches goal", verify.isGoal());
        }

        System.out.println();
    }

    // Initial state (2) จากโจทย์: w0 b0 __ w1 b1
    static void testSolverInitialState2() {
        System.out.println("--- Solver: Initial State (2) w0 b0 __ w1 b1 ---");

        // สร้าง board แบบ custom state
        Board b = new Board(2);
        Marble[] cells = b.getCells();
        cells[0] = new Marble("w0", "white", 0);
        cells[1] = new Marble("b0", "black", 1);
        cells[2] = null;
        cells[3] = new Marble("w1", "white", 3);
        cells[4] = new Marble("b1", "black", 4);

        System.out.println("  Start: " + b.boardToString());
        Board verify2 = b.clone();
        Solver solver = new Solver();
        boolean found = solver.solve(b);

        System.out.println("  Result: " + (found ? "Solution found" : "No solution"));

        if (found) {
            List<Moverecord> sol = solver.getSolution();
            System.out.println("  Steps: " + sol.size());
            for (Moverecord r : sol) {
                String type = verify2.move(r.getMarbleId());
                System.out.println("  " + r.getMarbleId() + " → " + type + " → " + verify2.boardToString());
            }
            assertTrue("State2 reaches goal if solution found", verify2.isGoal());
        }

        System.out.println();
    }

    // ทดสอบ state ที่ไม่มี solution
    static void testSolverNoSolution() {
        System.out.println("--- Solver: No Solution State ---");

        // สร้าง state ที่ stuck: หินขาวและดำสลับกันแบบที่เดินต่อไม่ได้
        // w0 b0 w1 b1 __ (ไม่มีช่องว่างให้เดิน)
        // จริงๆ ทุก state ที่ช่องว่างอยู่ขวาสุดและหินดำอยู่ซ้ายหินขาว
        // ใช้ state จากตัวอย่างในโจทย์: w0 b0 __ w1 b1 ที่ no solution
        // (โจทย์แสดงตัวอย่างว่า 3 steps แล้ว No solution !!)
        Board b = new Board(2);
        Marble[] cells = b.getCells();
        // สร้าง deadlock state: w0 b0 w1 __ b1
        cells[0] = new Marble("w0", "white", 0);
        cells[1] = new Marble("b0", "black", 1);
        cells[2] = new Marble("w1", "white", 2);
        cells[3] = null;
        cells[4] = new Marble("b1", "black", 4);

        System.out.println("  Start: " + b.boardToString());
        Solver solver = new Solver();
        boolean found = solver.solve(b);
        System.out.println("  Result: " + (found ? "Solution found" : "No solution"));
        // แค่ print ผล ไม่ assert เพราะขึ้นกับ state

        System.out.println();
    }

    // ─── Helper ──────────────────────────────────────────────────

    static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName);
            failed++;
        }
    }
}
