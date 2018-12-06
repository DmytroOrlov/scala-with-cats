package object app {

  type Requests = Map[City, Forcast]

  object Requests {
    def empty: Requests = Map.empty

    def hottest(request: Requests): (City, Forcast) = ???
  }

}
