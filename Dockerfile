FROM openjdk:11
#
RUN apt-get update
#
RUN apt-get install wget -y
#
RUN apt-get install unzip -y
#
RUN wget https://downloads.gradle-dn.com/distributions/gradle-6.8.2-bin.zip
RUN unzip gradle-6.8.2-bin.zip
ENV GRADLE_HOME /gradle-6.8.2
ENV PATH $PATH:/gradle-6.8.2/bin
#
RUN apt-get install git -y
#
RUN git clone https://github.com/saxa-xaker/alfaTask1.git
#
WORKDIR alfaTask1
#
RUN gradle bootJar --rerun-tasks --no-build-cache
#
ENTRYPOINT ["java", "-jar", "build/libs/alfaTask1-0.9.jar"]