package ServerGui;

import serverEndPoint.Server;

import java.io.IOException;

public class ServerProcessingThread extends Thread {

    private Server server;

    public ServerProcessingThread(int port) throws Exception {

        //Создаем задаем ему порт,
        //чтобы он его слушал на предмет входящих подключений
        server = new Server(port);
    }

    @Override
    public void run() {

        try {

            //Запускаем
            server.runServer();
            super.run();

        } catch (IOException e){ //Если мы словили ошибку, просто кидаем ее в рантайм,
            // словим ее в родительском потоке (В итоге вся консоль в красном, да-да)

            throw new RuntimeException(e);
        }

    }

    @Override
    public void interrupt() {
        try {

            //Прерываем выполнение
            server.stopServer();
            super.interrupt();
        } catch (Exception e) { // Точно так же перекидываем в рантайм
            throw new RuntimeException(e);
        }

    }

    public int getAmountOfConnectedClients(){
        return server.getAmountOfConnectedClients();
    }
}
