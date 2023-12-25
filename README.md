# Spring Boot Application

## Overview

The Yahoo! Finance Scraper Application, built with Spring Boot, is tailored to fetch and persist crucial financial data
from Yahoo! Finance for a given list of company tickers on a specific date.

## Prerequisites

Before initiating the application setup, ensure the following prerequisites are met:

- Java JDK
- Gradle
- MySQL Database installed and running
- Ensure `chromedriver` is installed and accessible in the system's PATH.

## Setup and Installation

To set up the Spring Boot application:

1. **Database Configuration**: Ensure your MySQL database is up and running. Create a database named `data_scrape_db`.

2. **Application Configuration**:
    - Navigate to the `application.properties` file in your Spring Boot project.
    - Add the following database connection details:
        - spring.datasource.url=jdbc:mysql://localhost:3306/data_scrape_db
        - spring.datasource.username=YOUR_USERNAME
        - spring.datasource.password=YOUR_PASSWORD
        - spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        - spring.jpa.hibernate.ddl-auto=update

3. **Chromedriver**: The setup details for `chromedriver` are stored in the `ScrapeConstants` class within the
   application. It's recommended to review and modify the constants there if necessary.

# Running a Spring Boot Application from IntelliJ IDEA

1. **Open the Project**:
    - Launch IntelliJ IDEA.
    - Navigate to `File` > `Open` and select your Spring Boot project directory.

2. **Configure Spring Boot Run Configuration**:
    - Ensure the `Spring Boot` plugin is installed and enabled for IntelliJ IDEA. This plugin provides enhanced support
      for Spring Boot applications.
    - In the project explorer, right-click on your main application class (typically annotated
      with `@SpringBootApplication`).
    - Select `Run 'DataScrapeApiApplication.main()`.

3. **Using the Toolbar**:
    - If the above option isn't available, use the toolbar:
        - Find the dropdown menu near the top-right corner of IntelliJ IDEA.
        - Select your main application class from this dropdown.
        - Click on the green play button next to it.

4. **Wait for Application Start**:
    - IntelliJ IDEA will compile and then initiate your Spring Boot application.
    - Monitor the `Run` or `Console` tab at the bottom of the IDE to see logs indicating the application's startup
      status.

5. **Accessing the Application**:
    - After the application starts, access it via a web browser by navigating to `http://localhost:8080` (or a different
      port if you've configured otherwise).

6. **Stopping the Application**:
    - To halt the application, click on the red square stop button in the IntelliJ IDEA toolbar or close the `Run`
      or `Console` tab.


