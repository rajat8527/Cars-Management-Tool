# Cars API

Sample Spring Boot REST API Project with Test Cases 

## Getting Started

### Prerequisites

Kindly ensure you have the following installed on your machine:

- [ ] [Java 8](https://www.java.com/en/download/help/download_options.xml)
- [ ] [Maven 3.5](https://maven.apache.org/install.html)
- [ ] [Git]()
- [ ] An IDE or Editor of your choice

### Running the Application

1. Clone the repository
```
$ git clone https://github.com/rajat8527/Cars-API.git
```

2. Check into the cloned repository
```
$ cd cars
```

3. Install the dependencies and package the application
```
$ mvn package
```

4. Run the API
```
mvn spring-boot:run
```

5. View the documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Project Architecture

Let us assume that we are building an app that helps users maintain a list of their cars. They will be able to add new cars, update existing cars, and even remove cars that they no longer possess. This application will be available for both Android and iOS devices and also as a web application.

Using the Spring Boot Framework, we can build a single API that can serve all the three applications, or clients, simultaneously.

Our journey starts at the Spring Initializer tool that helps us quickly bootstrap our Spring Boot API in a matter of minutes. There are a lot of dependencies and packages that help us achieve various functionality in our APIs and the Spring Initializer tool helps integrate them in our starter project.

This is aimed at easing our development process and letting us direct our attention to the logic of our application:

Spring Initializr
The tool allows us to choose between Maven and Gradle, which are tools to helps us automate some aspects of our build workflow such us testing, running, and packaging our Java application. We also get the option to choose between using Java or Kotlin when building our API using Spring Boot for which we can specify the version.

When we click on "Switch to the full version" we get more options to bundle into our API. A lot of these options come in handy when building Microservices such as "Cloud Config" and "Cloud Discovery" sections.

For our API, we will pick the following dependencies:

Web to help us develop a web-based API
MySQL which will help us connect to our MySQL database,
JPA which is the Java Persistence API to meet our database interaction needs, and
Actuator to help us maintain and monitor our web application.
With the dependencies set, we click the "Generate Project" button to get a zip containing our boilerplate code.


Let us identify what comes in the package using the tree command:

$ tree .
.
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pbcopy
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── cars
    │   │               └── CarsApplication.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── example
                    └── cars
                        └── CarsApplicationTests.java
                        
At the root folder, there is a pom.xml file that contains the project configuration for our Spring Boot API. If we used Gradle, we would have a build.gradle file instead. It includes information such as the details of our new API and all its dependencies.

We will mostly work in the main and test folders inside the source (src) folder. This is where we will place our controllers, models, utility classes among others.

Let us start by creating our database and configuring our API to use it. Follow this guide to install and verify that MySQL is running.

Once ready, let us create our database as follows:

$ mysql -u root -p

mysql> CREATE DATABASE cars_database;
Query OK, 1 row affected (0.08 sec)

Some details of our service will be different from environment to environment. For example, the database we use during development will not be the same one that the end users will use to store their information.

Configuration files make it easy for us to switch such details making our API easy to migrate and modify. This achieved through the configuration file, which in a Spring Boot API is the application.properties file that is located in the src/main/resources folder.

To enable our JPA dependency to access and modify our database, we modify the configuration file by adding the properties:

### Database Properties
spring.datasource.url = jdbc:mysql://localhost:3306/cars_database?useSSL=false
spring.datasource.username = root
spring.datasource.password = password

### Hibernate Properties

### The SQL dialect makes Hibernate generate better SQL for the chosen database

spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect

### Hibernate ddl auto (create, create-drop, validate, update)

spring.jpa.hibernate.ddl-auto = update

We now need an entity class to define our API's resources and their details as they will be saved in our database. A Car is our resource on this API and what this means is that it represents our object or real life item whose information we will perform actions on. Such actions include Create, Read, Update, and Delete, simply put as CRUD operations.

These operations are behind the HTTP Methods or Verbs that refer to various operations that an API can expose. They include:

GET which is a read operation that only fetches the specified data,
POST which enables the creation of resources by supplying their information as part of the request,
PUT which allows us to modify a resource, and
DELETE which we use to remove a resource and its information from our API.
To better organize our code, we will introduce some more folders in our project at the src/main/java/com/example/cars/ level. We will add a folder called models to host the classes that define our objects.

The other folders to be added include a controllers folder that contains our controllers, a repository folder for the database management classes and a utils folder for any helper classes we might need to add to our project. The resulting folder structure will be:

$ tree .
.
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pbcopy
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── cars
    │   │               ├── CarsApplication.java
    │   │               ├── controllers
    │   │               ├── models
    │   │               ├── repository
    │   │               └── utils
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── example
                    └── cars
                        └── CarsApplicationTests.java


### Domain Model

Let us define our Car class in the models folder:

/**
* This class will represent our car and its attributes
*/
@Entity
@Table(name="cars") // the table in the database tht will contain our cars data
@EntityListeners(AuditingEntityListener.class)
public class Car {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id; // Each car will be given an auto-generated unique identifier when stored

    @Column(name="car_name", nullable=false)
    private String carName; // We will also save the name of the car

    @Column(name="doors", nullable=false)
    private int doors; // We will also save the number of doors that a car has

    // getters and setters
}

Note: I have stripped off the imports to make the code snippet shorter. Please refer to the Github repo attached at the end of the article for the full code.

### DAO

With our car model ready, let us now create the CarRepository file that will be used in the interaction with the database:

public interface CarRepository extends JpaRepository<Car, Long> { }
Writing Tests
We can now expose the functionality of our API through our controller, but in the spirit of Test-Driven Development (TDD), let us write the tests first in the CarsApplicationTests file:

// These are a subset of the tests, the full test file is available on the Github repo attached at the end of this article
....

    /**
     * Here we test that we can get all the cars in the database
     * using the GET method
     */
    @Test
    public void testGetAllCars() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/cars",
            HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
    }

    /**
     * Here we test that we can fetch a single car using its id
     */
    @Test
    public void testGetCarById() {
        Car car = restTemplate.getForObject(getRootUrl() + "/cars/1", Car.class);
        System.out.println(car.getCarName());
        Assert.assertNotNull(car);
    }

    /**
     * Here we test that we can create a car using the POST method
     */
    @Test
    public void testCreateCar() {
        Car car = new Car();
        car.setCarName("Prius");
        car.setDoors(4);

        ResponseEntity<Car> postResponse = restTemplate.postForEntity(getRootUrl() + "/cars", car, Car.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
    }

    /**
     * Here we test that we can update a car's information using the PUT method
     */
    @Test
    public void testUpdateCar() {
        int id = 1;
        Car car = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        car.setCarName("Tesla");
        car.setDoors(2);

        restTemplate.put(getRootUrl() + "/cars/" + id, car);

        Car updatedCar = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        Assert.assertNotNull(updatedCar);
    }
    
The tests simulate various actions that are possible on our API and this is our way of verifying that the API works as expected. If a change was to made tomorrow, the tests will help determine if any of the functionality of the API is broken and in doing so prevent us from breaking functionality when effecting changes.

Think of tests as a shopping list when going into the supermarket. Without it, we might end up picking almost everything we come across that we think might be useful. It might take us a long time to get everything we need. If we had a shopping list, we would be able to buy exactly what we need and finish shopping faster. Tests do the same for our APIs, they help us define the scope of the API so that we do not implement functionality that was not in the plans or not needed.

When we run our tests using the mvn test command, we will see errors raised and this is because we have not yet implemented the functionality that satisfies our test cases.

In TDD, we write tests first, run them to ensure they initially fail, then implement the functionality to make the tests pass.

TDD is an iterative process of writing tests and implementing the functionality to make the tests pass. If we introduce any changes in the future, we will write the tests first, then implement the changes to make the new tests pass.

### Controller

Let us now implement our API functionality in a CarController which goes into the controllers folder:

@RestController
@RequestMapping("/api/v1")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    // GET Method for reading operation
    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // GET Method for Read operation
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarsById(@PathVariable(value = "id") Long carId)
        throws ResourceNotFoundException {

        Car car = carRepository
                  .findById(carId)
                  .orElseThrow(() -> new ResourceNotFoundException("Car not found on :: " + carId));
        return ResponseEntity.ok().body(car);
    }

    // POST Method for Create operation
    @PostMapping("/cars")
    public Car createCar(@Valid @RequestBody Car car) {
        return carRepository.save(car);
    }

    // PUT Method for Update operation
    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(
        @PathVariable(value = "id") Long carId, @Valid @RequestBody Car carDetails)
        throws ResourceNotFoundException {
            Car car = carRepository
                      .findById(carId)
                      .orElseThrow(() -> new ResourceNotFoundException("Car " + carId + " not found"));

        car.setCarName(carDetails.getCarName());
        car.setDoors(carDetails.getDoors());

        final Car updatedCar = carRepository.save(car);
        return ResponseEntity.ok(updatedCar);
    }

    // DELETE Method for Delete operation
    @DeleteMapping("/car/{id}")
    public Map<String, Boolean> deleteCar(@PathVariable(value = "id") Long carId) throws Exception {
        Car car = carRepository
                  .findById(carId)
                  .orElseThrow(() -> new ResourceNotFoundException("Car " + carId + " not found"));

        carRepository.delete(car);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}

At the top, we have the @RestController annotation to define our CarController class as the controller for our Spring Boot API. What follows is the @RequestMapping where we specify the base path of our API URL as /api/v1. This also includes the version.

Versioning is good practice in an API to enhance backward compatibility. If the functionality changes and we already have other people consuming our APIs, we can create a new version and have them both running concurrently to give them ample time to migrate to the new API.

Earlier, we learned about the Create, Read, Update, and Delete operations in an API and how they are mapped to HTTP Methods. These methods are accommodated in the Spring framework as PostMapping, GetMapping, PutMapping and DeleteMapping annotations, respectively. Each of these annotations helps us expose endpoints that only perform the CRUD operation specified.

We can also have a single endpoint that handles various HTTP Methods:

@RequestMapping(value="/cars", method = { RequestMethod.GET, RequestMethod.POST })
Now that we have implemented the functionality, let us run our tests:

### Test results

The passing tests show us that we have implemented the functionality as desired when writing the tests and our API works.

Let us interact with our API via Postman, which is a tool that helps interact with APIs when developing or consuming them.

We start by fetching all the cars we have stored in our database:

We start by fetching all the cars we have stored in our database:

Get cars empty
At the start, we have no cars stored. Let us add our first car:

Post first car
The response is the id and details of the car we have just added. If we add some more cars and fetch all the cars we have saved:

Get all cars
These are the cars we have created using our Spring Boot API. A quick check on the database returns the same list:

Database all cars

### Swagger UI

We have built and tested our API using TDD and now to make our API better, we are going to document it using Swagger UI, which allows us to create an auto-generated interface for other users to interact with and learn about our API.

First, let us add the following dependencies in our pom.xml:

<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger2</artifactId>
  <version>2.7.0</version>
</dependency>

<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger-ui</artifactId>
  <version>2.7.0</version>
</dependency>
Next, we will create a SwaggerConfig.java in the same folder as CarsApplication.java, which is the entry point to our API.

The SwaggerConfig.java file allows to also add some information about our API:

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.cars"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(metadata());
    }

    /**
     * Adds metadata to Swagger
     *
     * @return
     */
    private ApiInfo metadata() {
        return new ApiInfoBuilder()
            .title("Cars API")
            .description("An API to store car details built using Spring Boot")
            .build();
    }
}

Now we annotate our endpoints so that they appear on the Swagger UI interface that will be generated. This is achieved as follows:

// Add this import in our controller file...
import io.swagger.annotations.ApiOperation;

// ...then annotate our HTTP Methods
@ApiOperation(value="Fetches all cars in the database", response=Car.class)
@PostMapping("/...") // Our endpoint
We have specified our response class as the Car class since it is the one that will be used to populate the details of our responses. We have done this because Swagger UI allows us to add information about the request payloads and response details. This will help provide more information about the payloads such as the kind of values that our API requires and the kind of response that will be returned. We can also specify mandatory fields in the documentation.

In our case, we will also be using the Car class to format and validate our request parameters. Therefore, we annotate its "getters" as follows:

    @ApiModelProperty(name="id",
                      value="The id of the car",
                      example="1")
    public long getId() {
        return id;
    }

    @ApiModelProperty(name="carName",
                      value="The name of the car to be saved",
                      example="Bugatti",
                      required=true)
    public String getCarName() {
        return carName;
    }

    @ApiModelProperty(name="doors",
                      value="The number of doors that the car has",
                      example="2",
                      required=true)
    public int getDoors() {
        return doors;
    }
    
That's it! Our documentation is ready. When we run our API using mvn spring-boot:run and navigate to http://localhost:8080/swagger-ui.html we can see our API's documentation:

### Swagger UI final

Swagger UI has documented all our endpoints and even provided functionality to interact with our API directly from the documentation. As can be seen on the lower right section of the screenshot, our example values have been pre-filled so that we can quickly test out the API without having to rewrite the values.

### Conclusion

Java is a powerful language and we have harnessed its power to build an Application Programming Interface, or API, using the Spring Boot framework. We have been able to implement four of the HTTP Methods to handle the various Create, Read, Update and Delete operations on the details about our cars.

Swagger UI has also enabled us to document our API in a simple yet verbose and manner and have this documentation exposed as an endpoint in our service. Having noted the advantages of Test-Driven development, we went ahead and wrote tests for our endpoints and made sure our functionality and tests are aligned.

## Contribution

Please feel free to raise issues using this [template](./.github/ISSUE_TEMPLATE.md) and I'll get back to you.

You can also fork the repository, make changes and submit a Pull Request using this [template](./.github/PULL_REQUEST_TEMPLATE.md).
