# Test Android Github Notifications client

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


