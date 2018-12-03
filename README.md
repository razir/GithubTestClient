# Test Android Github Notifications client

This client is made for test purpose only. It's not related to Gitbub Inc. in any way. 
# Features:
  - Login to github account using oauth2 
  - Load basic github profile info
  - Show your first 30 github notifications

# Libraries & Solutions used:
  - *Architecture*: MVVM
  - *MVVM Framework:* Android Architecture Components
  - *Database:* Room
  - *Mulithreading:* Kotlin Couroutines
  - *Dependency injection:* Koin
  - *Unit tests:* mockito-kotlin2 
  - *Android Instrumental tests:* Espresso + Kakao + mockito-kotlin2
  
# Key components description: 

### Data package:
- *main* 
  * All models
### Datasource package
 - *main* 
   * All repositories 
   * All room database related code
   * All API related initialization code
 - *test* 
   * Unit tests for some repositories
 - *androidTest* 
   * AndroidJUnit tests for untested by JUnit repositories 
   * AndroidJUnit tests for room db
   * AndroidJUnit tests for Http client Interceptor
### Dependency injection [DI] package
- *main* 
   * Koin modules definition related code
- *test* 
  * Koin modules definition Unit tests
### Domain.UseCase package
- *main*
  * All use cases 
- *test*
  * Some use cases JUnit tests
- *androidTest*
  * AndroidJUnit tests for the rest of untested by JUnit use cases
### Extensions package
- *main*
  * Kotlin extensions code
- *androidTest*
  * AndroidJUnit for some extensions
### Presentaion package
- *main*
  * All UI related code + ViewModels
- *test*
  * JUnit tests for ViewModels
- *androidTest*
  * Espresso+ Kakao tests for Activities and Fragments

## Instructions to run

#### To run from the sources you need: 
1) create your own github oauth application with redirect url ```https://google.com```
2) create text file ```github_client.properties``` in the root project folder with app details from step 1.  

client_id={your_client_id_here} . 

client_secret={your_client_secret_here}


#### To run from the apk: 
 1) Download the latest release from [here](https://github.com/razir/GithubTestClient/releases/tag/1.0) 
 
 
### Short video demostrantion of app workflow
   https://drive.google.com/file/d/1L9BRnS7qELimUwrrneMK-R3ndNktPEmO/view?usp=sharing
### Todos
 - Write more UI tests
 - Add more functionality
 
## Author
 [LinkedIn](https://www.linkedin.com/in/anton-hadutski-00954342/) . 
 
 [Facebook](https://facebook.com/razir.anton) 
