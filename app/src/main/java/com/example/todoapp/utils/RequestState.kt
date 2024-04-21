package com.example.todoapp.utils

/**
 * Sealed class representing different states of a request.
 * This class is used to encapsulate the state of a request, such as idle, loading, success, or error.
 * @param T The type of data associated with the request state.
 */
sealed class RequestState<out T> {
    /**
     * Represents the idle state where no request is ongoing.
     */
    data object Idle : RequestState<Nothing>()

    /**
     * Represents the loading state where a request is in progress.
     */
    data object Loading : RequestState<Nothing>()

    /**
     * Represents the success state where a request has been successfully completed.
     * @param data The data associated with the successful request.
     */
    data class Success<T>(val data: T) : RequestState<T>()

    /**
     * Represents the error state where a request has encountered an error.
     * @param error The throwable representing the error.
     */
    data class Error(val error: Throwable) : RequestState<Nothing>()
}