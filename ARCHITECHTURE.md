# Architecture
The architecture of the app is based on the Model-View-ViewModel (MVVM) pattern. 
The MVVM pattern is used to separate the user interface logic from the business logic. 
We believe our application is well structured and separates concerns, mainly into three layers:
- The data layer, here we have the repositories and data sources.
- Business logic layer, here we have the view models.
- Presentation layer, here we have the screens.

## Data layer
Our data layer consists of the repositories, data sources, and partly the model classes.
The API calls are made in the data sources, which are then used by the repositories to fetch the data.
We then expose functions in the repositories that the view models can call to fetch the data.
The model classes are used to represent the data that is fetched from the network or the local database, allowing us to easily deserialize the JSON data from the network and utilize it in the application.

## Business logic layer
To handle business logic, we have implemented viewmodels. The view models are responsible for fetching data from the repositories, updating user preferences, and exposing state flows to the screens.
By using viewmodels our code is more testable and maintainable, as the business logic is separated from the user interface logic.
This makes it easier to spot bugs and make changes to the code.

## Presentation layer
The presentation layer consists of the screens that are responsible for displaying the data to the user.
The screens utilize the view models to visualize the data to the user.



## How we have implemented MVVM
The MVVM pattern is implemented in the following way:
- We have created screens that are responsible for displaying the data to the user.
- The screens displaying business logic utilize a view model to separate the business logic from the user interface logic.
- The view model fetches data from the repository, updates user preferences and exposes state flows to the screens.
- We have implemented repositories that are responsible for fetching data from the network or the local database. An example would be the LocationRepository that fetches the user's location from location services or stored location depending on if the user has given the app permission to access the location.
- We then have data sources which fetch the information that the repositories need.
- We have also heavily utilized model classes to represent the data that is fetched from the network or the local database. This has provided us with a simple way to deserialize the JSON data from the network and utilize it in the application.

## How we have implemented UDF
We have tried to implement the UDF pattern to our best ability, mostly by exposing state flows from the view models to the screens.


## Low coupling and high cohesion
We have tried to keep our codebase as modular as possible to achieve low coupling and high cohesion.
By utilizing the MVVM pattern we already have a low coupling between the business logic and the user interface logic.
We have tried to keep modularity by extracting some utility functions into the utils package.
We have also tried to make each class as independent as possible, so that they can be easily reused in other parts of the application.

We feel that we have reached high cohesion within the data layer, and business logic layer. We have had some challenges with the presentation layer.
Our screens could be broken even further down into smaller components to achieve higher cohesion, which would potentially make the code simpler, more reusable, and easier to maintain.
This would be an area of improvement in the future for our application.

# For future maintenance
Our application utilizes the following technologies:
- Kotlin
- Android Studio
- MIN Android API level 26
- Dagger Hilt

In addition to this we have setup API's which use the following technologies:
- Python
- Flask
- Azure SQL
- Azure App Service
- Azure Blob Storage

## How to further develop the solution
If you intend to continue the development of our application you will need to first deploy the API's our application uses.
The API's are located in the following repositories:
- [IN2000 Pollen API](https://github.com/GransMango/IN2000-regionAndPollen)
- [IN2000 Activity API](https://github.com/GransMango/in2000-sql-api)

The API's have documentation on how to deploy them in their respective repositories.
To summarize you will need to obtain an api key from NAAF to use the pollen API, setup an Azure SQL database, and deploy the API's to an Azure App Service.

When you have setup the API's you will need to change the urls in the data sources to match the urls of your deployed API's.
Then you're ready to continue the development of our application!

Some of our goals for future development are:
- Improve the UI/UX of the application
- Add the option to backup user preferences and user activities to the Azure DB
- Implement OAuth2 for user authentication
- Use the open API from NILU (https://api.nilu.no/) to obtain and include data of local air pollution

We hope you enjoy working on our application and wish you the best of luck with the development!



