# SpeditionOrganisationSystem
Desktop application with usage of Gradle, backend written in Java, GUI made by JavaFx, database PostgresSQL.
System, which main aim is to enable spedition managers to conveniently deal with client requests.

We recognise roles :
1. Administrator
2. Client
3. Order Manager
4. Factory Manager
5. Transport Provider

Class diagram:

<img src="/ClassDiagram.png" width="805" height="345">
UseCasesDiagram

<img src="/UseCasesDiagram.png" width="970,5" height="720,5">

Firstly, user has to login in. According to the role different options will be available.

<img src="/1.png" width="477" height="393">

Administrator can manage all users in system (delete, update and add). 

<img src="/22.png" width="624" height="390,5">

<img src="/21.png" width="248" height="269"> <img src="/20.png" width="245" height="266">

Client has more options. 

<img src="/22.png" width="624" height="390,5"> 

He can create a new order by adding to the basket items from the categories. 

<img src="/2.png" width="623" height="389">
<img src="/3.png" width="623" height="389">
<img src="/4.png" width="623" height="389">
<img src="/5.png" width="420" height="212">

After collection of the all items. Order can be placed. 

<img src="/6.png" width="259" height="350"> <img src="/7.png" width="257" height="161">

Current orders / History orders can be viewed. Also all details of each order. 

<img src="/8.png" width="623" height="389">

<img src="/9.png" width="273" height="350">

Order Manager can create a offer for a client. 

<img src="/14.png" width="623" height="389">

For each produt manager can set the factory and on the end the transport provider which will take that order. 

<img src="/16.png" width="623" height="389">

After the total cost is calculated based on product prices and cost of the transport in that area. 

<img src="/17.png" width="623" height="389">

After placing the offer the status is changing for pending. 

<img src="/18.png" width="622" height="288">

Tranport Provider has to confirm if he will be able to handle the order. 

<img src="/23.png" width="623" height="389">

Then the complete offer is send to the Client. After acception transport provider is responsible for delivery order. 

<img src="/24.png" width="622" height="288">

Factory Manager can change the details and add new products for sale. Factory Manager has to set amount of stock, which is avaiable in factory.  
