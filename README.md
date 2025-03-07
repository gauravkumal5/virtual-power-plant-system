# Virtual Power Plant System

## Overview

The Virtual Power Plant (VPP) System aggregates distributed power sources into a single cloud-based energy provider. It
integrates multiple power
resources to provide grid power, allowing small energy resources to be aggregated and marketed to utilities.

This solution allows battery information (name, postcode, and watt capacity) to be stored in a database and queried
efficiently via a REST API.

## Core Features

1. **Battery Registration Endpoint:**
    - A REST API endpoint accepts a list of batteries with attributes: `name`, `postcode`, and `watt capacity`.
    - The data is stored in a relational database such as MySQL.

2. **Postcode Range Filter Endpoint:**
    - An endpoint accepts a postcode range and returns a list of battery names that fall within this range.
    - The response also includes statistics on the batteries returned: total and average watt capacity.

## Optional Features (Implemented)

1. **Postcode Range with Watt Capacity Filters:**
    - The postcode range query allows additional filtering based on minimum or maximum watt capacity.

2. **Logging Framework Integration:**
    - A logging framework (e.g., SLF4J with Logback) has been integrated for logging.

3. **Integration Tests with Test Containers:**
    - Integration tests are included, leveraging test containers to test repository classes with the selected database
    - configured via profile.

## Technologies Used

- **Backend:** Spring Boot (Java)
- **Database:** MySQL (Used MySQL but choose one based on your implementation)
- **Logging:** SLF4J
- **Testing:** JUnit, Mockito, TestContainers
- **Build Tool:** Maven

## Setup Instructions

### Prerequisites

Ensure you have the following installed:

- Java 17 or later
- Maven
- MySQL

### Database Setup

1. Create a database for the project in your database system (MySQL).
2. Update the `application.properties` (or `application.yml`) file in the `src/main/resources` (Set profile based on
   your requirements.)
3. directory with the appropriate database connection
   details.

### Running the Application

- Clone
- cd virtual-power-plant-system
- ./mvnw spring-boot:run
- ./mvnw test

1. POST battery/save: Register a list of batteries.
   {
   "batteryRequestList": [
   {
   "name": "Lesmurdie",
   "postcode": "6076",
   "capacity": 13500
   },
   {
   "name": "Kalamunda",
   "postcode": "6076",
   "capacity": 13500
   },
   ]
   }

    2. POST: battery/grid Query batteries within a postcode range with optional filtering by watt capacity
       {
       "startPostCode":"6000",
       "endPostCode":"9000",
       "minCapacity":"12000",
       "maxCapacity":"14000"
       }

# ADR-001: Choosing MYSQL as the Database

## Context

Database required for the application.

## Decision

MYSQL as a database.

## Consequences

- MYSQL lacks some JSON support and advanced indexing features.

# ADR-002: Configuring a Separate Executor for Saving List Data

## Context

We might have to save large lists of data efficiently.
Performing this operation synchronously could lead to performance bottlenecks,
increased response times, and potential thread starvation in the application.
To improve system responsiveness and throughput, decided to use a separate executor for handling these operations
asynchronously.

## Decision

ThreadPoolTaskExecutor in Spring Boot to handle the saving of list data in a separate thread pool.
This executor will:
Use a fixed number of threads to process list-saving tasks.
Prevent long-running I/O operations from blocking the main application thread.
Ensure better utilization of system resources by managing thread execution efficiently.

## Alternatives Considered

Synchronous Processing:

- Simpler to implement but could block threads and slow down API responses.

## Consequences

- Requires additional thread pool management.
- Potential risk of increased memory usage if queue capacity is too large.






