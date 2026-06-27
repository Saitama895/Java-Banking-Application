# Java Banking Application

A console-based banking application built using **Core Java** to demonstrate object-oriented programming, layered architecture, Java Collections, and the Streams API.

## Features

* Customer registration
* Account creation
* Deposit and withdrawal
* Fund transfer between accounts
* Transaction history
* Search accounts by customer name
* Input validation
* Custom exception handling

## Technologies

* Java
* IntelliJ IDEA
* Java Collections Framework
* Java Streams API

## Project Structure

```text
src
├── app
├── domain
├── repository
├── service
│   └── impl
├── util
└── exceptions
```

## Design

The application follows a layered architecture:

```
Presentation (Main)
        │
        ▼
Service Layer
        │
        ▼
Repository Layer
        │
        ▼
In-Memory Data Store (HashMap / ArrayList)
```

## Getting Started

### Prerequisites

* Java 17+ (or the JDK version used by the project)
* IntelliJ IDEA or any Java IDE

### Run

```bash
git clone https://github.com/Saitama895/Java-Banking-Application.git
```

Open the project in your IDE and run:

```text
src/app/Main.java
```

## Future Improvements

* Spring Boot
* REST APIs
* MySQL
* Spring Data JPA
* Spring Security
* Docker
