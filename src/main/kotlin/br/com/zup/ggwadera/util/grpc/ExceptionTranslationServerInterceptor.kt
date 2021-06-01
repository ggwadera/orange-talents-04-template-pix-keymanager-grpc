package br.com.zup.ggwadera.util.grpc

import br.com.zup.ggwadera.util.exception.AlreadyExistsException
import io.grpc.*
import io.micronaut.http.client.exceptions.HttpClientResponseException
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@Suppress("unused")
class ExceptionTranslationServerInterceptor : ServerInterceptor {

    private class ExceptionTranslator<ReqT, RespT>(delegate: ServerCall<ReqT, RespT>) :
        ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(delegate) {

        override fun close(status: Status, trailers: Metadata) {
            if (status.isOk || status.code != Status.Code.UNKNOWN) {
                return super.close(status, trailers)
            }
            val cause = status.cause
            val translatedStatus = when (cause) {
                is IllegalArgumentException -> Status.INVALID_ARGUMENT
                is IllegalStateException -> Status.FAILED_PRECONDITION
                is AlreadyExistsException -> Status.ALREADY_EXISTS
                is HttpClientResponseException -> Status.INTERNAL
                is ConstraintViolationException -> Status.INVALID_ARGUMENT
                else -> Status.UNKNOWN
            }.withDescription(cause?.message).withCause(cause)
            return super.close(translatedStatus, trailers)
        }

    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        return next.startCall(ExceptionTranslator(call), headers)
    }
}