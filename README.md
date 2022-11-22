REQUIREMENTS
------------
-MySQL Server


INSTALLATION
------------
-Copy your jdbc connection string, database name and credentials to Server->DataLayer->resources->config.properties.

-Start server (During startup server creates schema `railway_database` if it's not exists)

-Perform such statement in your MySQL manager:

insert into railway_database.users (login, password, `e-mail`, userType) values ('admin', 'admin', 'yourEmail@example.com', 1);

-Now you can run client application and authorize as admin or register as user

SOME SCREENSHOTS
----------------
![image](https://user-images.githubusercontent.com/93078951/203259767-2f8d9c24-3dc9-4a21-8ef8-4ea5748f104b.png)
![image](https://user-images.githubusercontent.com/93078951/203259869-1d3715bd-5bed-46e3-95af-78201674dc94.png)
![image](https://user-images.githubusercontent.com/93078951/203259999-41015450-18ec-4b70-8e13-26a1a1ed9ca5.png)
![image](https://user-images.githubusercontent.com/93078951/203262981-d9d95e34-9737-4861-b121-986221faf827.png)
![image](https://user-images.githubusercontent.com/93078951/203260081-05385bed-dda9-4c5f-9b38-0f3af02d044c.png)
![image](https://user-images.githubusercontent.com/93078951/203261614-def27b65-993e-4084-b606-aae6fad4fc9d.png)
![image](https://user-images.githubusercontent.com/93078951/203261673-a241dfbe-f887-4b28-832e-49cb64adfa4c.png)
![image](https://user-images.githubusercontent.com/93078951/203261723-efe80d96-8ede-4765-9330-1cc59deeea4e.png)
![image](https://user-images.githubusercontent.com/93078951/203261808-59592f6d-0a09-47b2-ba34-3aea4015a7c4.png)
![Безымянный](https://user-images.githubusercontent.com/93078951/203262626-769e364d-7a99-4d08-944a-aec17adbd0f6.png)
![image](https://user-images.githubusercontent.com/93078951/203262724-b1378970-56e9-426e-b844-78946683dc4a.png)
![image](https://user-images.githubusercontent.com/93078951/203262842-338999dc-3e27-4043-b2fa-24e712901671.png)
