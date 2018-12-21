package object app {

  type Requests = Map[City, Forecast]

  object Requests {
    def empty: Requests = Map.empty

    def hottest(request: Requests): (City, Forecast) = ???
  }

}
