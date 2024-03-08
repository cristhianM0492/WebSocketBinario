package co.edu.uceva.websocketbinario.config;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

/**
 * Clase que se encarga de manejar los mensajes binarios que llegan al servidor
 * Este manejador WebSocket procesa los datos binarios recibidos del cliente y los reenvía al mismo cliente.
 */
public class EchoBinaryHandler extends BinaryWebSocketHandler {

    /**
     * Este metodo se encarga de manejar los mensajes binarios que llegan al servidor
     * @param session sesion del cliente
     * @param message mensaje binario que llega al servidor
     */
    @Override
    public void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage message) {
        try {
            // Se obtienen los datos binarios enviados por el cliente.
            byte[] binaryData = message.getPayload().array();

            // A continuación, los datos recibidos se reenvían nuevamente al cliente, como un eco.
            session.sendMessage(new BinaryMessage(binaryData));
        } catch (Exception e) {
            System.out.println("Se cerro la conexion websocket");
        }
    }
}