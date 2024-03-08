package co.edu.uceva.websocketbinario.config;

import org.springframework.lang.NonNull;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Este manejador WebSocket procesa y envía datos binarios al cliente. Se puede usar para enviar datos binarios al cliente
 */
public class RamdomBinaryHandler extends BinaryWebSocketHandler {

    private static ServerSocket server;
    //Puerto tcp en el cual el servidor va a escuchar
    private static int port = 12500;

    @Override
    public void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage message) {
        try {
            // Este manejador WebSocket procesa y envía datos binarios al cliente.
            //  dato ;
            byte[] binaryDataReceived = message.getPayload().array(); // Se reciben los datos del cliente (no se usan, pueden ser usados para recibir comandos del cliente)
            byte[] binaryData = new byte[200]; // Se crea un arreglo de bytes de 200 posiciones para enviar datos al cliente
            //Crea el objeto servidor en el puerto tcp 12500
            server = new ServerSocket(port);
            Socket socket;
            System.out.println("Esperando un cliente");
            socket = server.accept();
            System.out.println("Cliente conectado");
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            short i = 0;
            // Se genera una señal senoidal con una pequeña imperfeccion y se envian al cliente (se envian 100 datos enteros de 16 bits)
            while (true) {
                int dato;
                int msb;
                int lsb;
                char valor = (char) in.read();
                //System.out.println("valor: " + valor);
                int value = valor;
                if (valor >> 7 == 0) {
                    lsb = valor & 0x3F;
                    //System.out.println("lsb: " + lsb);
                    valor = (char) in.read();
                    if (valor >> 7 == 1) {
                        msb = valor & 0x3F;
                        //System.out.println("msb: " + msb);
                        dato = (msb << 6 | lsb);
                        System.out.println("" + dato);
                        if ( i < binaryData.length - 1) {
                            i += 2;
                        } else {
                            session.sendMessage(new BinaryMessage(binaryData)); // Se envian los datos al cliente
                            i=0;
                        }
                        binaryData[i] = (byte) ((short) dato >> 8); //Se envian los datos en formato big endian: los 8 bits mas significativos primero
                        binaryData[i + 1] = (byte) ((short) dato & 0x00FF); //Se envian los 8 bits menos significativos despues

                    } else {
                        //Error, no se recibio en secuencia
                        System.out.println("error en la secuencia");
                    }
                } else {
                    System.out.println("error no se recibio en secuencia");
                    //Error, no se recibio en secuencia
                }

            }


        } catch (IOException e) {
            System.out.println("Se cerro la conexion websocket");
        } catch (Exception e) {
            System.out.println("Se detuvo la temporizacion");
        }
    }
}
