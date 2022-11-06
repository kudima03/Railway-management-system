module ClientConnectionModule {

    requires CommonEntities;
    requires java.sql;

    exports clientConnectionModule.implementations;
    exports clientConnectionModule.interfaces;
}