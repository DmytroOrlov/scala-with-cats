package object app {

  type Requests = Map[City, Forecast]

  object Requests {
    def empty: Requests = Map.empty

    def hottest(request: Requests): (City, Forecast) = request.maxBy {
      case (_, Forecast(Temperature(f, Fahren))) => (f - 32).toDouble * 5/9
      case (_, Forecast(Temperature(c, Celcius))) => c
    }
  }

}
