package payModel.models.bitcoin

trait RpcResponse[T] {
  def result: T
  def id: String
}
