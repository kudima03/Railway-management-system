package services;


import customContainers.Pair;
import dataLayer.dbManagers.DataRepository;
import databaseEntities.Classes.RouteStation;
import services.exceptions.InfoNotFoundException;

import java.util.ArrayList;
import java.util.Date;

public class TimeTableService {

    public ArrayList<Pair<RouteStation, RouteStation>> getRouteDistanceInfo(int departureStationId, int arrivalStationId, Date date) throws InfoNotFoundException, Exception {

        var departuresFromStation = DataRepository.routeStationsRepository.get(departureStationId, date);

        var arrivalsToStations = DataRepository.routeStationsRepository.get(arrivalStationId, date);

        ArrayList<Pair<RouteStation, RouteStation>> pairsOfDestAndArrivals = new ArrayList<>();

        for (var departure : departuresFromStation) {

            var arrivalInfoList = arrivalsToStations.stream().filter(
                    (var obj) -> obj.getRoute().getId() == departure.getRoute().getId()
                            && obj.getArrivalDateTime().after(departure.getDepartureDateTime())
            ).toList();
            if (arrivalInfoList.size() == 0) continue;
            var arrivalInfo = arrivalInfoList.get(0);
            var pair = new Pair<RouteStation, RouteStation>();
            pair.setFirstValue(departure);
            pair.setSecondValue(arrivalInfo);
            pairsOfDestAndArrivals.add(pair);
        }
        return pairsOfDestAndArrivals;
    }
}
