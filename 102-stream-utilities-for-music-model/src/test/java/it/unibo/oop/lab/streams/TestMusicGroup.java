package it.unibo.oop.lab.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static java.util.stream.Collectors.toList;

/*
 * CHECKSTYLE: MagicNumber OFF
 * The above comment shuts down checkstyle: in a test suite, magic numbers may be tolerated.
 */
/**
 *
 *
 */
final class TestMusicGroup {

    private static final String UNTITLED = "untitled";
    private static final String III = "III";
    private static final String II = "II";
    private static final String I = "I";
    private MusicGroup lz;

    /**
     * Sets up the testing.
     */
    @BeforeEach
    void setUp() {
        lz = new MusicGroupImpl();
        lz.addAlbum(I, 1969);
        lz.addAlbum(II, 1969);
        lz.addAlbum(III, 1970);
        lz.addAlbum(UNTITLED, 1971);
        lz.addSong("Dazed and Confused", Optional.of(I), 6.5);
        lz.addSong("I Can't Quit You Baby", Optional.of(I), 4.6);
        lz.addSong("Whole Lotta Love", Optional.of(II), 5.5);
        lz.addSong("Ramble On", Optional.of(II), 4.6);
        lz.addSong("Immigrant Song", Optional.of(III), 2.4);
        lz.addSong("That's the Way", Optional.of(III), 5.4);
        lz.addSong("Black Dog", Optional.of(UNTITLED), 4.9);
        lz.addSong("When the Levee Breaks", Optional.of(UNTITLED), 7.1);
        lz.addSong("Travelling Riverside Blues", Optional.empty(), 5.2);
    }
    /**
     * Tests album names.
     */
    @Test
    void testAlbumNames() {
        final List<String> result = new ArrayList<>();
        result.add(II);
        result.add(UNTITLED);
        result.add(III);
        result.add(I);
        final List<String> actual = lz.albumNames().collect(toList());
        assertTrue(actual.containsAll(result));
    }

    /**
     * Tests ordering of song names.
     */
    @Test
    void testOrderedSongNames() {
        final List<String> result = Arrays.asList(new String[] {
                "Black Dog",
                "Dazed and Confused",
                "I Can't Quit You Baby",
                "Immigrant Song",
                "Ramble On",
                "That's the Way",
                "Travelling Riverside Blues",
                "When the Levee Breaks",
                "Whole Lotta Love" });
        final List<String> actual = lz.orderedSongNames().collect(toList());
        assertEquals(result, actual);
    }

    /**
     * Tests albums in year.
     */
    @Test
    void testAlbumInYear() {
        final List<String> result = Arrays.asList(new String[] { II, I });
        final List<String> actual = lz.albumInYear(1969).collect(toList());
        assertEquals(result, actual);
    }

    /**
     * Tests song counting.
     */
    @Test
    void testCountSongs() {
        assertEquals(2, lz.countSongs(I));
    }

    /**
     * Tests ordering song not in albums.
     */
    @Test
    void testCountSongsInNoAlbum() {
        assertEquals(1, lz.countSongsInNoAlbum());
    }

    /**
     * Tests average duration.
     */
    @Test
    void testAverageDuration() {
        assertEquals(6.0, lz.averageDurationOfSongs(UNTITLED).getAsDouble(), 0.0);
    }

    /**
     * Tests selecting the longest song.
     */
    @Test
    void testLongest() {
        assertEquals("When the Levee Breaks", lz.longestSong().get());
        assertEquals(UNTITLED, lz.longestAlbum().get());
    }

}
