package commands;

import data.Request;
import server.CollectionManager;
import data.Movie;
import server.Server;

import java.util.List;

/**
 * The type Add if max command.
 */
public class AddIfMaxCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Instantiates a new Add if max command.
     *
     * @param collectionManager the collection manager
     */
    public AddIfMaxCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(Request request) {
        if (request.object == null) {
            Server.error(request.client, "Не указан объект фильма");
            return;
        }
        Movie newMovie = (Movie) request.object;
        List<Movie> movies = collectionManager.filterMovies(movie -> movie.getLength() >= newMovie.getLength());
        if (!movies.isEmpty()) {
            Server.error(request.client, "Есть фильмы с большей длиной");
        }
        else {
            collectionManager.add(newMovie, request.login);
            Server.sendObject(request.client, collectionManager.getAll());
        }
    }
}
