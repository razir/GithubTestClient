# Test Android Github Notifications client

# Libraries & Solutions used:
  - *Architecture*: MVVM
  - *MVVM Framework:* Android Architecture Components
  - *Database:* Room
  - *Mulithreading:* RxJava
  - *Dependency injection:* Dagger Android
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
 - *androidTest (in progress)*
   * AndroidJUnit tests for untested by JUnit repositories
   * AndroidJUnit tests for room db
   * AndroidJUnit tests for Http client Interceptor
### Dependency injection [DI] package
- *main* 
   * Dagger modules and components definition related code
### Domain.UseCase package
- *main*
  * All use cases 
- *test (in progress)*
  * Some use cases JUnit tests
- *androidTest (in progress)*
  * AndroidJUnit tests for the rest of untested by JUnit use cases
### Extensions package
- *main*
  * Kotlin extensions code
- *androidTest (in progress)*
  * AndroidJUnit for some extensions
### Presentaion package
- *main*
  * All UI related code + ViewModels
- *test (in progress)*
  * JUnit tests for ViewModels
- *androidTest (in progress)*
  * Espresso+ Kakao tests for Activities and Fragments


