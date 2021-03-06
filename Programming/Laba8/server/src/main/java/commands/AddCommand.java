package commands;

import data.Movie;
import data.Request;
import server.CollectionManager;
import server.Server;

/**
 * The type Add command.
 */
public class AddCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Instantiates a new Add command.
     *
     * @param collectionManager the collection manager
     */
    public AddCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(Request request) {
        if (request.object == null) {
            Server.error(request.client, "Не указан объект фильма");
            return;
        }
        Movie movie = (Movie) request.object;
        if (collectionManager.add(movie, request.login)) {
            Server.sendObject(request.client, collectionManager.getAll());
        } else {
            Server.error(request.client, "Не удалось добавить элемент");
        }
    }
}
