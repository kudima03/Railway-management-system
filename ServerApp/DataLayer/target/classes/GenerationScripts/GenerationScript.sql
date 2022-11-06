
CREATE TABLE IF NOT EXISTS `document_types`(
	`id` int NOT NULL auto_increment,
	`typeName` varchar(100) NOT NULL,
 CONSTRAINT `PK_document_types` PRIMARY KEY (`id` ASC)
);

CREATE TABLE IF NOT EXISTS `users`(
	`id` int NOT NULL  auto_increment,
	`login`  varchar(50) NOT NULL UNIQUE,
	`password` varchar(50) NOT NULL,
	`e-mail` varchar(50) NOT NULL,
	`userType` int NOT NULL,
 CONSTRAINT `PK_users` PRIMARY KEY (`id` ASC),
 CONSTRAINT `email check` CHECK  ((`e-mail` like '%___@___%.__%'))
);

CREATE TABLE IF NOT EXISTS `drivers`(
	`id` int NOT NULL auto_increment,
	`name` varchar(20) NOT NULL,
	`surname` varchar(20) NOT NULL,
	`patronymic` varchar(20) NOT NULL,
	`dateOfBirth` date NOT NULL,
	`experience` int NOT NULL,
	`photo` Longblob NULL,
    `userId` int NOT NULL,
 CONSTRAINT `PK_drivers` PRIMARY KEY (`id` ASC),
CONSTRAINT `FK_drivers_users` FOREIGN KEY(`userId`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS  `rules`(
	`id` int NOT NULL auto_increment,
	`text` Longtext NOT NULL,
 CONSTRAINT `PK_rules` PRIMARY KEY (`id` ASC)
);

CREATE TABLE IF NOT EXISTS `stations`(
	`id` int NOT NULL auto_increment,
	`name` varchar(50) NOT NULL,
	`address` varchar(150) NOT NULL,
	`kmFromMinsk` int NOT NULL,
 CONSTRAINT `PK_stations` PRIMARY KEY (`id` ASC)
);

CREATE TABLE IF NOT EXISTS  `train_types`(
	`id` int NOT NULL auto_increment,
	`name` varchar(50) NOT NULL,
	`costForStation` float NOT NULL,
 CONSTRAINT `PK_train_types` PRIMARY KEY (`id` ASC)
 );

CREATE TABLE IF NOT EXISTS  `trains`(
	`id` int NOT NULL auto_increment,
	`typeId` int NOT NULL,
	`number` int NOT NULL,
 CONSTRAINT `PK_trains` PRIMARY KEY (`id` ASC),
 CONSTRAINT `FK_trains_train_types` FOREIGN KEY(`typeId`) REFERENCES `train_types` (`id`)
);

CREATE TABLE IF NOT EXISTS `passengers`(
	`id` int NOT NULL auto_increment,
	`name` varchar(20) NOT NULL,
	`surname` varchar(20) NOT NULL,
	`patronymic` varchar(20) NOT NULL,
	`phoneNumber` varchar(13) NOT NULL,
	`documentTypeId` int NOT NULL,
	`documentNumber` varchar(50) NOT NULL,
	`e-mail` varchar(50) NOT NULL,
	`sex` int NOT NULL,
	`dateOfBirth` date NOT NULL,
 CONSTRAINT `PK_passengers` PRIMARY KEY (`id` ASC),
 CONSTRAINT `FK_passengers_document_types` FOREIGN KEY(`documentTypeId`) REFERENCES `document_types` (`id`),
 CONSTRAINT `EMail constraint` CHECK  ((`e-mail` LIKE '%___@___%.__%'))
);

CREATE TABLE IF NOT EXISTS `routes`(
	`id` int NOT NULL auto_increment,
	`trainId` int NOT NULL,
	`driverId` int NOT NULL,
	`number` int NOT NULL,
	`periodicity` int NOT NULL,
 CONSTRAINT `PK_routes` PRIMARY KEY (`id` ASC),
 CONSTRAINT `FK_routes_drivers` FOREIGN KEY(`driverId`)  REFERENCES `drivers` (`id`),
 CONSTRAINT `FK_routes_trains` FOREIGN KEY(`trainId`) REFERENCES `trains`(`id`)
);

CREATE TABLE IF NOT EXISTS `route_stations`(
	`routeId` int NOT NULL,
	`stationId` int NOT NULL,
	`ordinalNumber` int NOT NULL,
	`arrivalDateTime` datetime(3) NOT NULL,
	`stopTime` datetime(3) NOT NULL,
	`depatureDateTime` datetime(3) NOT NULL,
	`track` varchar(5) NOT NULL,
	`platform` int NOT NULL,
    CONSTRAINT `FK_route_stations_routes` FOREIGN KEY(`routeId`) REFERENCES `routes` (`id`),
    CONSTRAINT `FK_route_stations_stations` FOREIGN KEY(`stationId`) REFERENCES `stations` (`id`)
);

CREATE TABLE IF NOT EXISTS `user_passengers`(
	`userId` int NOT NULL,
	`passengerId` int NOT NULL,
    CONSTRAINT `FK_user_passengers_passengers` FOREIGN KEY(`passengerId`) REFERENCES `passengers` (`id`),
    CONSTRAINT `FK_user_passengers_users` FOREIGN KEY(`userId`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `tickets`(
	`id` int NOT NULL auto_increment,
	`passengerId` int NOT NULL,
	`routeId` int NOT NULL,
	`depatureStationId` int NOT NULL,
	`arrivalStationId` int NOT NULL,
	`cost` Double NOT NULL,
	`clearanceDateTime` datetime(3) NOT NULL,
	`status` int NOT NULL,
	`routeLength` int NOT NULL,
 CONSTRAINT `PK_Tickets` PRIMARY KEY (`id` ASC),
 CONSTRAINT `FK_Tickets_Passengers` FOREIGN KEY(`passengerId`) REFERENCES `Passengers` (`id`),
 CONSTRAINT `FK_Tickets_Routes` FOREIGN KEY(`routeId`) REFERENCES `Routes` (`id`),
 CONSTRAINT `FK_Tickets_Stations1` FOREIGN KEY(`arrivalStationId`) REFERENCES `Stations` (`id`),
 CONSTRAINT `FK_Tickets_Stations2` FOREIGN KEY(`depatureStationId`) REFERENCES `Stations` (`id`)
);

CREATE TABLE IF NOT EXISTS `users_tickets`(
	`userId` int NOT NULL,
	`ticketId` int NOT NULL,
    CONSTRAINT `FK_users_tickets_tickets` FOREIGN KEY(`ticketId`) REFERENCES `tickets` (`id`),
    CONSTRAINT `FK_users_tickets_users` FOREIGN KEY(`userId`) REFERENCES `users` (`id`)
);