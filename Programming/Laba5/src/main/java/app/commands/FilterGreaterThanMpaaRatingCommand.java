package app.commands;

import app.CollectionManager;
import app.data.Movie;
import app.data.MpaaRating;

import java.util.List;

/**
 * The type Filter greater than mpaa rating command.
 */
public class FilterGreaterThanMpaaRatingCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Instantiates a new Filter greater than mpaa rating command.
     *
     * @param collectionManager the collection manager
     */
    public FilterGreaterThanMpaaRatingCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        MpaaRating mpaaRating = MpaaRating.input();
        List<Movie> movies = collectionManager.filterMovies(movie -> movie.getMpaaRating().compareRatings(mpaaRating) == 1);
        movies.forEach(System.out::println);
    }
}
