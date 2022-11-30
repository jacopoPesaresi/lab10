package it.unibo.oop.lab.streams;

import java.security.KeyStore.Entry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new HashMap<>();
    private final Set<Song> songs = new HashSet<>();

    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }

    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }

    @Override
    public Stream<String> orderedSongNames() {
        return songs.stream().map(Song::getSongName).sorted(String::compareTo);
    }

    @Override
    public Stream<String> albumNames() {
        return albums.keySet().stream();
    }

    @Override
    public Stream<String> albumInYear(final int year) {
        return albumNames().filter(x -> albums.get(x) == year);
    }

    @Override
    public int countSongs(final String albumName) {
        //return (int) songs.stream().filter(x -> x.getAlbumName().get() == albumName).count(); 
        //non faccio il controllo se l'opzionale è null
        return (int) songs.stream()
                        .filter(x -> x.getAlbumName().isPresent())
                        .filter(x -> x.getAlbumName().get().equals(albumName))
                        .count();
                        //devo fare una map?
    }

    @Override
    public int countSongsInNoAlbum() {
        return (int) songs.stream().filter(x -> x.albumName.isEmpty()).count();
    }

    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
        /*return OptionalDouble.of((songs.stream()
                                        .filter(x -> x.albumName.isPresent() && x.albumName.get() == albumName)
                                        .map(Song::getDuration)
                                        .reduce((x, y) -> x+y)
                                        .get() / countSongs(albumName)));*/
        return songs.stream()
            .filter(x -> x.albumName.isPresent() && x.albumName.get().equals(albumName))
            .mapToDouble(Song::getDuration)
            .average();
    }

    @Override
    public Optional<String> longestSong() {
        //return Optional.of(songs.stream().reduce((x, y) -> x.duration > y.duration ? x : y).get().songName);
        //return Optional.of(songs.stream().max((x, y) -> x.duration > y.duration ? 
        //1 : x.duration == y.duration ? 0 : -1).get().getSongName());
        return songs.stream().max(Comparator.comparingDouble(Song::getDuration)).map(Song::getSongName);
    }

    @Override
    public Optional<String> longestAlbum() {
        /*
        return songs.stream()
        .filter(x -> x.getAlbumName().isPresent())
        .map(Song::getAlbumName)
        .max((x, y) -> x.get().length() > y.get().length() ? 1 : x.get().length() == y.get().length() ? 0 : -1)
        .get();*/
        return songs.stream()
        .collect(Collectors.toMap(Song::getAlbumName, Song::getDuration, (x,y) -> x+=y ))
        .entrySet().stream().max(Comparator.comparingDouble(w -> w.getValue())).get().getKey();
        //.collect(Collectors.groupingBy(Song::getAlbumName))
        
        //.forEach(null);
        //.entrySet().stream().max( (x,y) -> x.getValue().stream().sum)
        //.merge(Song::getAlbumName, Song::getDuration, (x, y) -> { return x+y;});
        //max();
        //.collect(Collector.of(() -> x, y -> 1, (a ,b) -> a+b,  null));
        //.collect(Collectors.counting(HashMap::new, HashMap::put, ));
        //.entrySet().stream().max(Comparator.comparing( (a, b) -> a.values ));
    }

    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;

        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }

        public String getSongName() {
            return songName;
        }

        public Optional<String> getAlbumName() {
            return albumName;
        }

        public double getDuration() {
            return duration;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = songName.hashCode() ^ albumName.hashCode() ^ Double.hashCode(duration);
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Song) {
                final Song other = (Song) obj;
                return albumName.equals(other.albumName) && songName.equals(other.songName)
                        && duration == other.duration;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}
