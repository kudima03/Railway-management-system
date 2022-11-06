package serverEndPoint;

import entityManagers.UsersManager;
import serverEndPoint.threads.ClientProcessingThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    //Порт который будет слушать наш серв
    private int serverPort;

    //Сокет, который слушает порт и принимает входящие подключения
    private final ServerSocket acceptingSocket;

    //Коллекция ВЫПОЛНЯЮЩИХСЯ потоков клиентов
    private List<ClientProcessingThread> processingThreads;

    //Так как мы опять запускаем дочерние потоки, нам нужно отлавливать в них исключения
    //Создаем улавливатель ошибок в дочерних потоках
    Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable exception) {
            //ВАЖНО!
            //Из этого класса запускаются потоки клиентов (ClientProcessingThread)
            //Они выбрасывают исключение если !клиент отключился! или произошла другая
            //ошибка которую невозможно обработать в том потоке

            processingThreads.remove(Integer.parseInt(thread.getName()));

            //Поэтому мы убираем поток из выполнения и
            // !!Перекидываем исключение в родительский поток (ServerController)!!!
            new RuntimeException(exception);
            //И уже там выводим в консоль или в окошко

        }
    };

    public Server(int serverPort) throws Exception {
        this.serverPort = serverPort;
        acceptingSocket = new ServerSocket(serverPort);
        processingThreads = new ArrayList<ClientProcessingThread>();
        new UsersManager().get("");
    }

    public void runServer() throws IOException {

        while (true) {

            //Принимаем запрос на подключение
            var newClientSocket = acceptingSocket.accept();
            //Инициализируем клиента
            var newClient = new ConnectedClientInfo(newClientSocket);
            //ЗАПУСКАЕМ ПОТОК КЛИЕНТА
            var newThread = new ClientProcessingThread(newClient);
            //Ставим ему имя чтобы дай бог разобраться
            newThread.setName(String.valueOf(processingThreads.size()));
            //Ставим ему наш улавливатель
            newThread.setUncaughtExceptionHandler(exceptionHandler);
            //Стартуем
            newThread.start();
            processingThreads.add(newThread);
        }
    }

    public void stopServer() throws IOException {

        //Закрываем принимающий сокет
        acceptingSocket.close();
        for (var thread : processingThreads) {
            //Прерываем выполнение дочерних потоков
            thread.interrupt();
        }
    }

    public int getAmountOfConnectedClients(){
        return processingThreads.size();
    }

}
