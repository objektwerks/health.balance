package objektwerks

final class Dispatcher(store: Store, emailer: Emailer):
  def dispatch[E <: Event](command: Command): Event = ???