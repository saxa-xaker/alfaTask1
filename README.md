# HOW TO START
 To start microservice on GNU/Linux(tested on Debian):
1. Requirements: git, java 11 or higher, gradle 6.8.2. To start with Docker, you will need a Docker.
2. Clone this repo to your directory: $ git clone https://github.com/saxa-xaker/alfaTask1.git
3. Go to alfatask1: $ cd alfatask1/
4. To create jar run: $ ./gradlew bootJar, and then start it with: java -jar build/libs/alfatask1-1.0-RC1.jar. To only
   start service, run: $ ./gradlew bootRun
5. To start by Docker run: $ docker build -t alfatask1 . && docker run -p 8080:8080 --name alfatask1 alfatask1
6. Settings available at the {application.properties}. You have to check {application.properties}, replace
   given app_id und api_key with your credentials. You can also set server port, as you need.
   The default port number is 8080.
7. If you want increase or decrease number of days to back, you can to set FEIGN_DAYSTOBACK,
   as you wish at a reasonable limit.
   Parameter must have type: "int". Default value equals 1.
# HOW TO USE
This service intended a return an animated gif, depending on today und yesterday currency rates.
If a today rate with Russian rouble (RUB) is higher, than yesterday rate, its return a gif from "Rich" category.
Another way is return gif from "Broke" category.
Other way return message, something about, rate has not been changed.
If you want to get gif, send request with currency code, like "USD" (default value) or "GBP".
So, request will be looks like: http(s)://your_server_address:8080/api/v1/getGiphy/USD.
You can check available currency codes, sending request to /api/v1/getCurrencyList.
So, good luck!
