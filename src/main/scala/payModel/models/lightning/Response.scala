package payModel.models.lightning

trait Response[T] {

  def id: Long
  def jsonrpc: String
  def result: T
}
