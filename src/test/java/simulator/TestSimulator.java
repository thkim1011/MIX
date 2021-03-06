package simulator;

import assembler.Assembler;
import main.Word;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSimulator {
    @Test
    public void testRun() {
        // Set up
        Assembler asm = new Assembler();
        Simulator sim = new Simulator();
        Register rA = sim.getRegisterA();
        Register rI1 = sim.getIndexRegister(1);
        Register rX = sim.getRegisterX();

        sim.setMemory(0, new Word(false, 1, 2, 3, 4, 5));
        sim.setMemory(1000, new Word(true, 0, 0, 0, 31, 16));
        sim.setMemory(2000, new Word(false, 5, 10, 15, 20, 25));

        // Test Load
        sim.run(asm.assemble(" LDA 0"));
        Word actual1 = rA.getWord();
        Word expected1 = new Word(false, 1, 2, 3, 4, 5);
        assertEquals(expected1, actual1);

        sim.run(asm.assemble(" LD1 1000"));
        Word actual2 = rI1.getWord();
        Word expected2 = new Word(true, 0, 0, 0, 31, 16);
        assertEquals(expected2, actual2);

        sim.run(asm.assemble(" LDX 1000"));
        Word actual3 = rX.getWord();
        Word expected3 = new Word(true, 0, 0, 0, 31, 16);
        assertEquals(expected3, actual3);

        sim.run(asm.assemble(" LDA 0,1"));
        Word actual4 = rA.getWord();
        Word expected4 = new Word(false, 5, 10, 15, 20, 25);
        assertEquals(expected4, actual4);

        sim.run(asm.assemble(" LDX 0,1(2:4)"));
        Word actual5 = rX.getWord();
        Word expected5 = new Word(true, 0, 0, 10, 15, 20);
        assertEquals(expected5, actual5);

        sim.run(asm.assemble(" LDAN 0,1(2:4)"));
        Word actual6 = rA.getWord();
        Word expected6 = new Word(false, 0, 0, 10, 15, 20);
        assertEquals(expected6, actual6);

        // Test Store
        sim.setMemory(2000, new Word(false, 1, 2, 3, 4, 5));
        rA.setWord(new Word(true, 6, 7, 8, 9, 0));

        sim.run(asm.assemble(" STA 2000"));
        Word actual11 = sim.getMemory(2000);
        Word expected11 = new Word(true, 6, 7, 8, 9, 0);
        assertEquals(expected11, actual11);

        // Test Add
        rA.setWord(new Word(false, 1, 2, 3, 4, 5));
        sim.setMemory(0, new Word(false, 1, 2, 3, 4, 5));
        sim.run(asm.assemble(" ADD 0"));
        Word actual21 = rA.getWord();
        Word expected21 = new Word(false, 2, 4, 6, 8, 10);
        assertEquals(expected21, actual21);

        sim.run(asm.assemble(" STA 2000"));
        sim.run(asm.assemble(" LDA 2000(5:5)"));
        sim.run(asm.assemble(" ADD 2000(4:4)"));
        sim.run(asm.assemble(" ADD 2000(3:3)"));
        sim.run(asm.assemble(" ADD 2000(2:2)"));
        sim.run(asm.assemble(" ADD 2000(1:1)"));

        Word actual22 = rA.getWord();
        Word expected22 = new Word(true, 0, 0, 0, 0, 30);
        assertEquals(expected22, actual22);

        // Sums to 0 test
        sim.setMemory(1, new Word(false, 0, 0, 0, 0, 30));
        sim.run(asm.assemble(" ADD 1"));
        Word actual23 = rA.getWord();
        Word expected23 = new Word(true, 0, 0, 0, 0, 0);
        assertEquals(expected23, actual23);

        sim.run(asm.assemble(" LDA  1"));
        sim.run(asm.assemble(" LDXN 1"));
        sim.run(asm.assemble(" STX  1"));
        sim.run(asm.assemble(" ADD  1"));
        Word actual24 = rA.getWord();
        Word expected24 = new Word(false, 0, 0, 0, 0, 0);
        assertEquals(expected24, actual24);

        // Test Sub
        rA.setWord(new Word(false, 19, 18, 0, 0, 9));
        sim.setMemory(1000, new Word(false, 31, 16, 2, 22, 0));
        sim.run(asm.assemble(" SUB 1000"));
        Word actual25 = rA.getWord();
        Word expected25 = new Word(true, 11, 62, 2, 21, 55);
        assertEquals(expected25, actual25);

        // Test MUL
        rA.setWord(new Word(true, 1, 1, 1, 1, 1));
        sim.setMemory(1000, new Word(true, 1, 1, 1, 1, 1));
        sim.run(asm.assemble(" MUL 1000"));
        Word actual26 = rA.getWord();
        Word actual27 = rX.getWord();
        Word expected26 = new Word(true, 0, 1, 2, 3, 4);
        Word expected27 = new Word(true, 5, 4, 3, 2, 1);
        assertEquals(expected26, actual26);
        assertEquals(expected27, actual27);

        rA.setWord(new Word(-112));
        sim.setMemory(1000, new Word(false, 2, 0, 0, 0, 0));
        sim.run(asm.assemble(" MUL 1000(1:1)"));
        Word actual28 = rA.getWord();
        Word actual29 = rX.getWord();
        Word expected28 = new Word(false, 0, 0, 0, 0, 0);
        Word expected29 = new Word(-224);
        assertEquals(expected28, actual28);
        assertEquals(expected29, actual29);

        // TODO: Add the other MUL DIV tests.


        // Test NUM and CHAR
        sim.getRegisterA().setWord(new Word(false, 0, 0, 31, 32, 39));
        sim.getRegisterX().setWord(new Word(true, 37, 57, 47, 30, 30));
        sim.run(asm.assemble(" NUM 0"));
        Word actual51 = sim.getRegisterA().getWord();
        Word expected51 = new Word(-12977700);
        assertEquals(expected51, actual51);

        sim.run(asm.assemble(" INCA 1"));
        Word actual52 = sim.getRegisterA().getWord();
        Word expected52 = new Word(-12977699);
        assertEquals(expected52, actual52);

        sim.run(asm.assemble(" CHAR 0"));
        Word actual53 = sim.getRegisterA().getWord();
        Word actual54 = sim.getRegisterX().getWord();
        Word expected53 = new Word(false, 30, 30, 31, 32, 39);
        Word expected54 = new Word(true, 37, 37, 36, 39, 39);
        assertEquals(expected53, actual53);
        assertEquals(expected54, actual54);
    }

    @Test
    public void testLoad() {
        Simulator sim = new Simulator();
        Register rA = sim.getRegisterA();
        Register rX = sim.getRegisterX();
        Register rI1 = sim.getIndexRegister(1);

        // Test LDA
        sim.setMemory(2000, new Word(false, 1, 16, 3, 5, 4));

        sim.load(rA, 2000, 5, false);
        Word actual1 = rA.getWord();
        Word expected1 = new Word(false, 1, 16, 3, 5, 4);
        assertEquals(expected1, actual1);

        sim.load(rA, 2000, 8+5, false);
        Word actual2 = rA.getWord();
        Word expected2 = new Word(true, 1, 16, 3, 5, 4);
        assertEquals(expected2, actual2);

        sim.load(rA, 2000, 3*8+5, false);
        Word actual3 = rA.getWord();
        Word expected3 = new Word(true, 0, 0, 3, 5, 4);
        assertEquals(expected3, actual3);

        sim.load(rA, 2000, 3, false);
        Word actual4 = rA.getWord();
        Word expected4 = new Word(false, 0, 0, 1, 16, 3);
        assertEquals(expected4, actual4);

        sim.load(rA, 2000, 4*8+4, false);
        Word actual5 = rA.getWord();
        Word expected5 = new Word(true, 0, 0, 0, 0, 5);
        assertEquals(expected5, actual5);

        sim.load(rA, 2000, 0, false);
        Word actual6 = rA.getWord();
        Word expected6 = new Word(false, 0, 0, 0, 0, 0);
        assertEquals(expected6, actual6);

        sim.load(rA, 2000, 8+1, false);
        Word actual7 = rA.getWord();
        Word expected7 = new Word(true, 0, 0, 0, 0, 1);
        assertEquals(expected7, actual7);

        // Test LDX
        sim.setMemory(1000, new Word(true, 1, 2, 3, 4, 5));

        sim.load(rX, 1000, 0, false);
        Word actual8 = rX.getWord();
        Word expected8 = new Word();
        assertEquals(expected8, actual8);

        sim.load(rX, 1000, 5, false);
        Word actual9 = rX.getWord();
        Word expected9 = new Word(true, 1, 2, 3, 4, 5);
        assertEquals(expected9, actual9);

        sim.load(rX, 1000, 4, false);
        Word actual10 = rX.getWord();
        Word expected10 = new Word(true, 0, 1, 2, 3, 4);
        assertEquals(expected10, actual10);

        // Test LD1
        sim.load(rI1, 1000, 2, false);
        Word actual11 = rI1.getWord();
        Word expected11 = new Word(true, 0, 0, 0, 1, 2);
        assertEquals(expected11, actual11);

        // Test Negative Loads
        sim.load(rA, 1000, 5, true);
        Word actual12 = rA.getWord();
        Word expected12 = new Word(false, 1, 2, 3, 4, 5);
        assertEquals(expected12, actual12);
    }

    @Test
    public void testStore() {
        Simulator sim = new Simulator();
        Register rA = sim.getRegisterA();

        // Test 1: STA 2000
        sim.setMemory(2000, new Word(false, 1, 2, 3, 4, 5));
        rA.setWord(new Word(true, 6, 7, 8, 9, 0));

        sim.store(rA, 2000, 5);
        Word actual1 = sim.getMemory(2000);
        Word expected1 = new Word(true, 6, 7, 8, 9, 0);
        assertEquals(expected1, actual1);

        // Test 2: STA 2000(1:5)
        sim.setMemory(2000, new Word(false, 1, 2, 3, 4, 5));
        rA.setWord(new Word(true, 6, 7, 8, 9, 0));

        sim.store(rA, 2000, 13);
        Word actual2 = sim.getMemory(2000);
        Word expected2 = new Word(false, 6, 7, 8, 9, 0);
        assertEquals(expected2, actual2);

        // Test 3: STA 2000(5:5)
        sim.setMemory(2000, new Word(false, 1, 2, 3, 4, 5));
        rA.setWord(new Word(true, 6, 7, 8, 9, 0));

        sim.store(rA, 2000, 45);
        Word actual3 = sim.getMemory(2000);
        Word expected3 = new Word(false, 1, 2, 3, 4, 0);
        assertEquals(expected3, actual3);

        // Test 4: STA 2000(2:2)
        sim.setMemory(2000, new Word(false, 1, 2, 3, 4, 5));
        rA.setWord(new Word(true, 6, 7, 8, 9, 0));

        sim.store(rA, 2000, 18);
        Word actual4 = sim.getMemory(2000);
        Word expected4 = new Word(false, 1, 0, 3, 4, 5);
        assertEquals(expected4, actual4);

        // Test 5: STA 2000(2:3)
        sim.setMemory(2000, new Word(false, 1, 2, 3, 4, 5));
        rA.setWord(new Word(true, 6, 7, 8, 9, 0));

        sim.store(rA, 2000, 19);
        Word actual5 = sim.getMemory(2000);
        Word expected5 = new Word(false, 1, 9, 0, 4, 5);
        assertEquals(expected5, actual5);

        // Test 6: STA 2000(0:1)
        sim.setMemory(2000, new Word(false, 1, 2, 3, 4, 5));
        rA.setWord(new Word(true, 6, 7, 8, 9, 0));

        sim.store(rA, 2000, 1);
        Word actual6 = sim.getMemory(2000);
        Word expected6 = new Word(true, 0, 2, 3, 4, 5);
        assertEquals(expected6, actual6);
    }

    @Test
    public void testEnter() {
        // Set up
        Assembler asm = new Assembler();
        Simulator sim = new Simulator();
        Register rA = sim.getRegisterA();
        Register rI1 = sim.getIndexRegister(1);
        Register rX = sim.getRegisterX();

        // General Tests
        sim.enter(rA, 123, false);
        Word actual1 = rA.getWord();
        Word expected1 = new Word(true, 0, 0, 0, 1, 59);
        assertEquals(expected1, actual1);

        // Edge Cases
        // TODO: Figure out whether I should account for +0 vs -0.
    }

    /**
     * Tests from TAOCP Vol 1 Page 135
     */
    @Test
    public void testShift() {
        Simulator sim = new Simulator();

        Register regA = sim.getRegisterA();
        Register regX = sim.getRegisterX();

        regA.setWord(new Word(true, 1, 2, 3, 4, 5));
        regX.setWord(new Word(false, 6, 7, 8, 9, 10));

        // Test 1
        sim.shift(1, 3);

        Word expected1 = new Word(true, 0, 1, 2, 3, 4);
        Word expected2 = new Word(false, 5, 6, 7, 8, 9);

        assertEquals(expected1, regA.getWord());
        assertEquals(expected2, regX.getWord());

        // Test 2
        sim.shift(2, 0);

        Word expected3 = new Word(true, 2, 3, 4, 0, 0);
        Word expected4 = new Word(false, 5, 6, 7, 8, 9);

        assertEquals(expected3, regA.getWord());
        assertEquals(expected4, regX.getWord());

        // Test 3
        sim.shift(4, 5);

        Word expected5 = new Word(true, 6, 7, 8, 9, 2);
        Word expected6 = new Word(false, 3, 4, 0, 0, 5);

        assertEquals(expected5, regA.getWord());
        assertEquals(expected6, regX.getWord());

        // Test 4
        sim.shift(2, 1);
        Word expected7 = new Word(true, 0, 0, 6, 7, 8);
        Word expected8 = new Word(false, 3, 4, 0, 0, 5);

        assertEquals(expected7, regA.getWord());
        assertEquals(expected8, regX.getWord());

        // Test 5
        sim.shift(501, 4);
        Word expected9 = new Word(true, 0, 6, 7, 8, 3);
        Word expected10 = new Word(false, 4, 0, 0, 5, 0);

        assertEquals(expected9, regA.getWord());
        assertEquals(expected10, regX.getWord());
    }
}
