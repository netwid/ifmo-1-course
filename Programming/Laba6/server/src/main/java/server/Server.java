package server;

import data.Request;
import data.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server {
    private static final int port = 31337;
    private static DatagramChannel dc;
    private static final Logger logger = LogManager.getLogger();

    static {
        try {
            InetSocketAddress addr = new InetSocketAddress(port);
            dc = DatagramChannel.open();
            dc.bind(addr);
            dc.configureBlocking(false);
            logger.info("Сервер запущен");
        } catch (IOException e) {
            System.out.println("Ошибка при инициализации сервера");
        }
    }

    public static void requestObject(SocketAddress addr, Class<? extends Serializable> class_) {
        Response response = new Response();
        response.command = "send";
        response.toInput = class_;

        Server.send(addr, response);
    }

    public static void print(SocketAddress addr, String toPrint) {
        if (addr == null) {
            System.out.print(toPrint);
            return;
        }

        Response response = new Response();
        response.toPrint = toPrint;

        Server.send(addr, response);
    }

    public static void send(SocketAddress addr, Response response) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(out)) {
            os.writeObject(response);
            dc.send(ByteBuffer.wrap(out.toByteArray()), addr);
            logger.info("Ответ отправлен на " + addr.toString());
        } catch (IOException e) {
            System.out.println("Ошибка при отправке запроса на " + addr);
        }
    }

    public static Request receive() {
        try {
            ByteBuffer buf = ByteBuffer.allocate(1024 * 1024);
            SocketAddress addr = dc.receive(buf);
            if (addr != null) {
                logger.info("Получен новый запрос от " + addr);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
                Request request = (Request) ois.readObject();
                request.client = addr;
                return request;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Полученный объект не найден");
        } catch (IOException e) {
            System.out.println("Ошибка получения запроса");
        }
        return null;
    }
}
