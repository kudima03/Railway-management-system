module TransferLayer {
    requires java.sql;
    requires CommonEntities;
    requires BusinessLayer;

    exports serverEndPoint.threads;
    exports serverEndPoint;
}