# HOW TO START
To start microservice on GNU/Linux:
1. $ git clone https://github.com/saxa-xaker/alfaTask1.git
2. Go to alfatask1: $ cd alfatask1/
3. To create jar run: $ ./gradlew bootJar, and then start it with: java -jar build/libs/alfatask1-XX.jar,
   where XX - app version.
   To only start service run: $ ./gradlew bootRun
4. To start by Docker run: $ docker build -t alfatask1 . && docker run -p 8080:8080 --name alfatask1 alfatask1 
5. Requirements: git, java 11 or higher, gradle 6.8.2. To start with Docker need Docker.

#HOW TO USE
Send request to endpoint /getGiphy/xxx, where xxx - currency code. For example: /getGiphy/usd