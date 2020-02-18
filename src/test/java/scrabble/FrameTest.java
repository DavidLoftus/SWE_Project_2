package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FrameTest {
    @Test
    void removeTile() {
        Frame frame = new Frame();

        FakePool pool = new FakePool();
        pool.add(Tile.BLANK);
        pool.add(Tile.A);
        pool.add(Tile.B);
        pool.add(Tile.C);
        pool.add(Tile.G);
        pool.add(Tile.E);
        pool.add(Tile.BLANK);

        assertThrows(NoSuchElementException.class, () -> frame.removeTile(Tile.A));

        frame.refill(pool);

        assertThrows(NoSuchElementException.class, () -> frame.removeTile(Tile.F));

        assertTrue(frame.hasTile(Tile.BLANK));

        frame.removeTile(Tile.BLANK);
        assertTrue(frame.hasTile(Tile.BLANK));

        frame.removeTile(Tile.BLANK);
        assertFalse(frame.hasTile(Tile.BLANK));

        assertThrows(NoSuchElementException.class, () -> frame.removeTile(Tile.BLANK));

        assertTrue(frame.hasTile(Tile.A));

        frame.removeTile(Tile.A);
        assertFalse(frame.hasTile(Tile.A));
    }

    @Test
    void hasTile() {
        Frame frame = new Frame();

        FakePool pool = new FakePool();
        pool.add(Tile.BLANK);
        pool.add(Tile.A);
        pool.add(Tile.B);
        pool.add(Tile.C);
        pool.add(Tile.G);
        pool.add(Tile.E);
        pool.add(Tile.BLANK);

        frame.refill(pool);

        assertTrue(frame.hasTile(Tile.BLANK));
        assertTrue(frame.hasTile(Tile.A));
        assertTrue(frame.hasTile(Tile.B));
        assertTrue(frame.hasTile(Tile.C));
        assertTrue(frame.hasTile(Tile.E));
        assertTrue(frame.hasTile(Tile.G));

        assertFalse(frame.hasTile(Tile.F));
        assertFalse(frame.hasTile(Tile.Z));
    }

    @Test
    void refill() {
        Frame frame = new Frame();

        FakePool pool = new FakePool();
        pool.add(Tile.BLANK);
        pool.add(Tile.A);
        pool.add(Tile.B);
        pool.add(Tile.C);
        pool.add(Tile.G);
        pool.add(Tile.E);
        pool.add(Tile.BLANK);

        assertFalse(pool.isEmpty());

        frame.refill(pool);

        assertTrue(pool.isEmpty());
    }

    @Test
    void isEmpty() {
        Frame frame = new Frame();
        assertTrue(frame.isEmpty());

        Pool pool = new Pool();
        frame.refill(pool);

        assertFalse(frame.isEmpty());
    }

    @Test
    void testToString() {}

    private List<Tile> stringToTileList(String tiles) {
        if (tiles == null) {
            return null;
        }
        return tiles.chars().mapToObj(c -> Tile.parseTile((char) c)).collect(Collectors.toList());
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                "FOOTE?H,F,F",
                "FOOTE?H,FOOT,FOOT",
                "FOOTE?H,TOE,TOE",
                "FOOTE?H,HOE,HOE",
                "FOOTE?H,PHOTO,?HOTO",
                "F?OTE?H,PHOTO,?HOT?",
                "F?OTE?H,PORT,?O?T",
                "FOOTE?H,CAR,null",
                "FOOTE?H,FART,null",
                "F?OTE?H,PORTER,null",
            },
            nullValues = {"null"})
    void getTilesToPlace(String frameContents, String neededTiles, String expectedResult) {
        List<Tile> frameContentsList = stringToTileList(frameContents),
                neededTilesList = stringToTileList(neededTiles),
                expectedResultList = stringToTileList(expectedResult);

        FakePool pool = new FakePool();
        for (Tile tile : frameContentsList) {
            pool.add(tile);
        }

        Frame frame = new Frame();
        frame.refill(pool);

        assertEquals(expectedResultList, frame.getTilesToPlace(neededTilesList));
    }
}
