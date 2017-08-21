# MockNeat 

**MockNeat** is a Java 8+ library that facilitates the generation of arbitrary data for your applications.

[![Build Status](https://travis-ci.org/nomemory/mockneat.svg?branch=master)](https://travis-ci.org/nomemory/mockneat.svg?branch=master) [![codecov](https://codecov.io/gh/nomemory/mockneat/branch/master/graph/badge.svg)](https://codecov.io/gh/nomemory/mockneat)

## Installing

The library is available in [jcenter](https://bintray.com/nomemory/maven/mockneat) as a maven or gradle dependency.

For detailed information on how you can include **MockNeat** in your projects please check the **[wiki page](https://github.com/nomemory/mockneat/wiki/Installing)** 

## Contributing

Any type of contributions, suggestions or new ideas are stongly appreciated. Please check this **[wiki page](https://github.com/nomemory/mockneat/wiki/Contributing)** for more information.

## Documentation 

For extensive documentation please visit the **[wiki](https://github.com/nomemory/mockneat/wiki)**. 

Quick links: 

- [Using the library in your applications](https://github.com/nomemory/mockneat/wiki/Installing)
- [Creating MockNeat Objects](https://github.com/nomemory/mockneat/wiki/Creating--a-MockNeat-object)
- [The MockNeat Class](https://github.com/nomemory/mockneat/wiki/MockNeat);
- [The MockUnit Interface](https://github.com/nomemory/mockneat/wiki/MockUnits)

Real world examples:

- [Creating CSV files with arbitrary data](https://github.com/nomemory/mockneat/wiki/Creating-CSV-files-with-arbitrary-data);

- [Programatically populate MySQL Schema with arbitrary test data](https://github.com/nomemory/mockneat/wiki/Programatically-populate-MySQL-Schema-with-arbitrary-test-data).

## Examples

### 1. Mocking Real-World Objects

Example for Generating a List<Employee> of 1000 arbitrary employees that work for a fictional company called "Company.com":

```java
// Creates a MockNeat object that internally uses
// a ThreadLocalRandom.
MockNeat m = MockNeat.threadLocal();

List<Employee> companyEmployees =
                m.reflect(Employee.class) // The class we are mocking
                 .field("uniqueId",
                        m.uuids()) // Generates a random unique identifier
                 .field("id",
                        m.longSeq()) // Generates long numbers in a sequence
                 .field("fullName",
                        m.names().full()) // Generates a full name for the employer
                 .field("companyEmail",
                        m.emails().domain("company.com")) // Generates a company email with a given domain
                 .field("personalEmail",
                        m.emails()) // Generates an arbitrary email without domain constraints
                 .field("salaryCreditCard",
                        m.creditCards().types(AMERICAN_EXPRESS, MASTERCARD)) // Generate credit card numbers of 'types'
                 .field("external",
                        m.bools().probability(20.0)) // Generates Boolean values with 20% probability of obtaining True
                 .field("hireDate",
                        m.localDates().past(of(1999, 1, 1))) // Generatest a date in the past, but greater than 01.01.1987
                 .field("birthDate",
                        m.localDates().between(of(1950, 1, 1), of(1994, 1, 1))) // Generates a data in the given range
                 .field("pcs",
                        m.reflect(EmployeePC.class) // Mock an EmployeePC object
                         .field("uuid",
                                m.uuids()) // Generates an unique identifier
                         .field("username",
                                m.users()) // Generates an arbitrary username
                         .field("operatingSystem",
                                m.from(new String[]{"Linux", "Windows 10", "Windows 8"})) // Randomly selects an OS from the given List
                         .field("ipAddress",
                                m.ipv4s().type(CLASS_B)) // Generates a CLASS B IPv4 Address
                         .field("macAddress",
                                m.macs()) // Generates a MAC Address
                         .list(2)) // Creates a List<EmployeePC> with 2 values
                 .list(1000) // Creates a List<Employee> with 1000 values
                 .val(); // Returns the list
```

### 2. Writing CSV files

Example for creating a CSV file with arbitrary data that has the following structure:

`id, firstName, lastName, email, salary (euro)`

The file should contain 1000 lines.

```java
MockNeat m = MockNeat.threadLocal();
final Path path = Paths.get("./test.csv");

m.fmt("#{id},#{first},#{last},#{email},#{salary}")
 .param("id", m.longSeq()) // Replaces #{id} with a long number in a sequence
 .param("first", m.names().first()) // Replaces #{first} with a first name 
 .param("last", m.names().last()) // Replaces #{last} with a last name
 .param("email", m.emails()) // Replaces #{email} with an arbitrary email 
 .param("salary", m.money().locale(GERMANY).range(2000, 5000)) // Replace #{salary} with a sum of money (EUR) in the given range
 .list(1000) // Generates a list of 1000 Strings 
 .consume(list -> { 
            try { Files.write(path, list, CREATE, WRITE); }
            catch (IOException e) { e.printStackTrace(); }
 }); // Writes the list to a file
```

### 3. Probabilities

Example for generating numbers in intervals based on probabilities: 
- Generating a number in interval [0, 100) - 20% chance;
- Generating a number in interval [100, 200) - 50% chance;
- Generating a number in interval [200, 300) - 30% chance;

```java
Integer x = m.probabilites(Integer.class)
             .add(0.2, m.ints().range(0, 100))
             .add(0.5, m.ints().range(100, 200))
             .add(0.3, m.ints().range(200, 300))
             .val();
```

