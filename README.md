# HOW TO START
To start microservice on GNU/Linux:
1. $ git clone https://github.com/saxa-xaker/alfaTask1.git
2. Go to alfatask1: $ cd alfatask1/
3. To create jar run: $ ./gradlew bootJar, and then start it with: java -jar build/libs/alfatask1-X.X.jar,
   where X.X - app version.
   To only start service run: $ ./gradlew bootRun
4. To start by Docker run: $ docker build -t alfatask1 . && docker run -p 8080:8080 --name alfatask1 alfatask1 
5. Requirements: git, java 11 or higher, gradle 6.8.2. To start with Docker need Docker.

# HOW TO USE
This service intended a return link with an animated gif, depending on today und yesterday currency rates.
If a today rate with Russian rouble (RUB) is higher, than yesterday rate, its return url with a gif from "Rich" category.
Another way is return gif from "Broke" category. Other way return message, something about, rate has not been changed.
If you want to get gif, send request with currency code, like "USD" (default value) or "GBP".
So request will be looks like: /getGiphy/USD
You can check available currencies codes, sending request to /getCurrencies
So good luck!