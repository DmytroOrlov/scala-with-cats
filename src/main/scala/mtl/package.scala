import cats.Applicative
import cats.mtl.{ApplicativeAsk, DefaultApplicativeAsk}

package object mtl {
  def constant[F[_] : Applicative, E](e: E): ApplicativeAsk[F, E] =
    new DefaultApplicativeAsk[F, E] {
      val applicative: Applicative[F] = Applicative[F]

      def ask: F[E] = applicative.pure(e)
    }
}
