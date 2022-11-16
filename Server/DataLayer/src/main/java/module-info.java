module DataLayer {
    requires CommonEntities;
    requires java.sql;

    exports dataLayer.dbManagers;
    exports dataLayer.repositories.implementations;
    exports dataLayer.repositories.interfaces;
}