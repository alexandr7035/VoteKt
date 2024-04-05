package by.alexandr7035.ethereum.errors

class RequestNotExecutedException(val error: String) : RuntimeException(error)