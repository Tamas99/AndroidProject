# Where to eat application

## 1. Introduction

  Inside this android project we've got to make an application where the users can select restaurants from many different cities and read informations about them like how expensive is that restaurant, where is it's location, what is it's phone number and so on. The users can register and log into their profiles, they can put restaurants into favorites so later they can check them out. They can upload profile picture about themselves or about something else, they are able to also delete their account or maybe make another one if they want.
  
## 2. Structure
  
  The application contains a splashscreen at the very start, which starts with the application and disappear after 3 seconds. After that, we are navigated to our home screen where we have an overview about the restaurants in the selected city. The restaurants are displayed in a recyclerview and contain some basic information. But if we are interested in one, we simply click them and the application navigates us to a detailed overview where are shown all the informations about the restaurant.
  There's a little bottom navigation bar between home screen and profile screen. If we click on profile, we will be able to log in, register or check out our profile information if we were already logged in.
  There is also a Room database containing necessary informations for our application, making it able to run properly.
This database has three entities: User, FavoriteRestaurant and ProfilePicture. Their purposes are already shown by their names.

## 3. Components

  ### a. Activity
  ### b. Fragment
  ### c. RecyclerView
  ### d. Room Database
  ### e. ConstraintLayout, LinearLayout
  ### f. Retrofit
  ### g. Glide
  
## 4. Functionality

  MainActivity - This is the single, big Activity in the application, this Acitvity contains all the Fragments, Interfaces and Database.
  
  SplashScreenFragment - It appears only when the application starts for 3 seconds and navigates us to the OverviewFragment after.
  
  OverviewFragment - Shows the restaurants in the RecyclerView which has an adapter named PhotosGridAdapter. The Fragment initializes a ViewModel named OverviewViewModel which requests the restaurants datas from the webpage with a GET API request. This will return a JSON which will be converted into a Kotlin object using Retrofit and Moshi Converter. These GET requests are the type of Deferred ones, which allow us to run them on a background thread and do not block the UI thread to cause slowly respond times.
We can select a restaurant for further details and to add them to favorites. Otherwise from the Bottom Navigation Bar selecting the Profile screen we will be able to log in or register. After that will be shown up our profile with the informations we gave and with names of our favorite restaurants if we've added some before.
The JSON which we retrieve from webservice are restaurant informations and every restaurant contains an image URL. These restaurant images are displayed using the Glide image loading library.

  OverviewViewModel - This ViewModel contains a couple of properties having the type of MutableLiveData. These MutableLiveDatas' purposes are to being able to observe them, so when their values have changed on a backthread, we will be notified and will be able to use them on the Main thread so the Main thread will not be blocked.
  
## 5. Issues

  The app may still runs into unexpected issues which will cause it to crash and reopen it. They may appear at some database operaion when the id's value reaches the upper limit or at uploading a profile picture where the image's quality is too high. In other words, the app still need further maintenance to handle exceptions.
