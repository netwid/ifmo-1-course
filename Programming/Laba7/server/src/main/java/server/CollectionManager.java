package server;

import data.Movie;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type Collection manager.
 */
public class CollectionManager {
    /**
     * The Movies.
     */
    HashSet<Movie> movies = new HashSet<>();
    /**
     * The Init time.
     */
    ZonedDateTime initTime = ZonedDateTime.now();
    DatabaseManager dm = DatabaseManager.getInstance();
    ReadWriteLock lock = new ReentrantReadWriteLock(true);

    /**
     * Info about collection.
     */
    public String info() {
        return "Тип: " + movies.getClass() + "\n" +
               "Дата инициализации: " + initTime + "\n" +
               "Количество элементов: " + movies.size() + "\n";
    }

    public void load() {
        movies = new HashSet<>(dm.getAll());
    }

    /**
     * Add new movie.
     *
     * @param movie the movie
     */
    public boolean add(Movie movie, String login) {
        if (lock.writeLock().tryLock()) {
            try {
                int id = dm.add(movie, login);
                if (id > 0) {
                    movie.setId(id);
                    movies.add(movie);
                    return true;
                }
                return false;
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            System.out.println("Запись уже идёт");
            return false;
        }
    }

    /**
     * Clear movies.
     */
    public void clear() {
        if (dm.clear())
            movies.clear();
    }

    /**
     * Get all movies.
     */
    public HashSet<Movie> getAll() {
        return movies;
    }

    /**
     * Update element by id.
     *
     * @param id       the id
     * @param newMovie the new movie
     * @return status
     */
    public boolean update(int id, Movie newMovie, String login) {
        if (lock.writeLock().tryLock()) {
            try {
                if (!dm.update(id, newMovie, login))
                    return false;
                for (Movie movie : movies) {
                    if (movie.getId() == id) {
                        movies.remove(movie);
                        newMovie.setId(id);
                        movies.add(newMovie);
                        return true;
                    }
                }
                return false;
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            System.out.println("Запись уже идёт");
            return false;
        }
    }

    /**
     * Remove movie by id.
     *
     * @param id the id
     * @return status
     */
    public boolean removeById(int id, String login) {
        if (lock.writeLock().tryLock()) {
            try {
                if (!dm.removeById(id, login))
                    return false;
                for (Movie movie : movies) {
                    if (movie.getId() == id) {
                        movies.remove(movie);
                        return true;
                    }
                }
                return false;
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            System.out.println("Запись уже идёт");
            return false;
        }
    }

    /**
     * Remove movie with lower length.
     *
     * @param length the length
     */
    public void removeLower(int length, String login) {
        if (lock.writeLock().tryLock()) {
            try {
                if (dm.removeLower(length))
                    movies.removeIf(movie -> movie.getLength() < length);
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            System.out.println("Запись уже идёт");
        }
    }


    /**
     * Contains passport boolean.
     *
     * @param passportId the passport id
     * @return the boolean
     */
    public boolean containsPassport(String passportId) {
        return movies
            .stream()
            .anyMatch(movie -> movie.getScreenwriter().getPassportID().equals(passportId));
    }

    /**
     * Filter movies list by lambda.
     *
     * @param filter the filter
     * @return the movies list
     */
    public List<Movie> filterMovies(Predicate<Movie> filter) {
        return movies
            .stream()
            .filter(filter)
            .collect(Collectors.toList());
    }

    /**
     * Print field descending genre.
     */
    public String printFieldDescendingGenre() {
        Map<Integer, String> genres = new TreeMap<>();
        for (Movie movie : movies) {
            genres.put(movie.getGenre().ordinal(), movie.getGenre().name());
        }

        StringBuilder sb = new StringBuilder("");
        for (String genre : genres.values()) {
            sb.append(genre).append('\n');
        }
        return String.valueOf(sb);
    }
}
