# Movinfo

An android application to discover the currently popular movies.
APK can be found [here](https://github.com/EddyMM/movinfo-app-mvvm/releases)

<img src="github_assets/imgs/splash_screen.png" alt="Movie list screen" width=250 height=450 />  <img src="github_assets/imgs/popular_movies_screen.png" alt="Movie list screen" width=250 height=450 />  <img src="github_assets/imgs/top_rated_movies_screen.png" alt="Movie list screen" width=250 height=450 />  
<img src="github_assets/imgs/example_detail_screen.png" alt="Movie list screen" width=250 height=450 /> <img src="github_assets/imgs/favorites_screen.png" alt="Movie list screen" width=250 height=450 />

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/EddyMM/movinfo-app-mvvm.git
```

### Adding an API key

- Create an account with [The Movie DB](https://www.themoviedb.org/)
- Get an API key from your account settings
- Add the API key to the **global `gradle.properties`** file 
and use the variable `TheMovieDbApiToken`
- For example:
`TheMovieDbApiToken="<Your API Key>"`

## Maintainers
This project is maintained by:
* [Eddy mwenda](https://github.com/EddyMM)


## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Push your branch (git push origin my-new-feature)
5. Create a new Pull Request

## Acknowledgements

Movie information provided by [TheMovieDb]((https://www.themoviedb.org/)) API