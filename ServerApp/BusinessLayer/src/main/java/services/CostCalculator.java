package services;

import databaseEntities.Classes.Route;
import databaseEntities.Classes.RouteStation;


public class CostCalculator {

    public static float calculateCost(Route route, RouteStation departureStation, RouteStation arrivalStation){

        var costForStation =  route.getTrain().getType().getCostForStation();
        var amountOfStation = arrivalStation.getOrdinalNumber() - departureStation.getOrdinalNumber();
        return costForStation * amountOfStation;
    }
}
