package com.leithcarsreports.presentation.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.io.IOException

abstract class BaseViewModel<S, E>(initialState: S) : ViewModel(), BaseInteractionListener {

    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected val _effect = MutableSharedFlow<E?>()
    val effect = _effect.asSharedFlow().throttleFirst(500).mapNotNull { it }

    protected fun <T> tryToExecute(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope
    ): Job {
        return runWithErrorCheck(onError, inScope) {
            val result = function()
            Log.e("Log", "tryToExecute: $result")
            onSuccess(result)
        }
    }

    protected fun <T> tryToCollect(
        function: suspend () -> Flow<T>,
        onNewValue: (T) -> Unit,
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope
    ): Job {
        return runWithErrorCheck(onError, inScope) {
            function()
                .distinctUntilChanged()
                .collectLatest {
                    Log.e("Log", "tryToExecute: $it")
                    onNewValue(it)
                }
        }
    }

    @OptIn(FlowPreview::class)
    protected fun collectSearch(
        flow: suspend () -> Flow<String>,
        onNewValue: (String) -> Unit,
        onEmptyQuery: () -> Unit,
        inScope: CoroutineScope = viewModelScope
    ): Job {
        return inScope.launch {
            flow().debounce(300).distinctUntilChanged()
                .collectLatest { query ->
                    Log.e("Log", "tryToExecute: $query")
                    onNewValue(query)
                    if (query.isBlank()) {
                        onEmptyQuery()
                    }
                }
        }
    }

    protected fun launchDelayed(duration: Long, block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            delay(duration)
            block()
        }
    }

    protected fun updateState(updater: (S) -> S) {
        _state.update(updater)
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(effect)
        }
    }

    private fun runWithErrorCheck(
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        function: suspend () -> Unit
    ): Job {
        return inScope.launch(dispatcher) {
            try {
                function()
            } catch (exception: Exception) {
                when (exception) {
                    is NoInternetException -> onError(ErrorState.NoInternet)
                    is PermissionDenied -> onError(ErrorState.HasNoPermission)
                    is ServerSideException -> onError(ErrorState.RequestFailed)
                    is UserNotFoundException -> onError(ErrorState.UserNotExist(exception.message.toString()))
                    is NotFoundedException -> onError(ErrorState.NotFound)
                    is AuthorizationException -> onError(ErrorState.UnAuthorized)
                    is WrongPasswordException -> onError(ErrorState.WrongPassword(exception.message.toString()))
                    is InvalidCredentialsException -> onError(
                        ErrorState.InvalidCredentials(
                            exception.message.toString()
                        )
                    )

                    is UnknownErrorException -> onError(ErrorState.UnknownError(exception.message.toString()))
                    is InvalidUserNameOrPasswordException -> onError(ErrorState.InvalidUserNameOrPassword)
                    is UnAuthorizedAccessException -> onError(ErrorState.UnAuthorized)
                    is InvalidWalletProductNameException -> onError(ErrorState.InvalidProductName)
                    is InvalidWalletProductPriceException -> onError(ErrorState.InvalidProductPrice)
                    is InvalidRequestParameterException -> onError(ErrorState.InvalidRequestParameter)
                    is InvalidWalletProductQuantityException -> onError(ErrorState.InvalidProductQuantity)
                    is InvalidServiceNameException -> onError(ErrorState.InvalidServiceName)
                    is InvalidServicePriceException -> onError(ErrorState.InvalidProductPrice)
                    is WalletNotFoundException -> onError(ErrorState.WalletNotFound)
                    is InvalidUserNameException -> onError(ErrorState.InvalidUserName)
                    is InvalidFullNameException -> onError(ErrorState.InvalidFullName)
                    is InvalidPhoneException -> onError(ErrorState.InvalidPhone)
                    is InvalidEmailException -> onError(ErrorState.InvalidEmail)
                    is InvalidPasswordException -> onError(ErrorState.InvalidPassword)
                    is InvalidRoleException -> onError(ErrorState.InvalidRole)
                    is InvalidDeadlineException -> onError(ErrorState.InvalidDeadline)
                    is UserNameAlreadyExistException -> onError(ErrorState.UserNameAlreadyExist)
                    is InvalidBedTypeNameException -> onError(ErrorState.InvalidBedTypeName)
                    is InvalidBedTypeCodeException -> onError(ErrorState.InvalidBedTypeCode)
                    is InvalidBedTypeDescriptionException -> onError(ErrorState.InvalidBedTypeDescription)
                    is InvalidRoomTypeCodeException -> onError(ErrorState.InvalidRoomTypeCode)
                    is InvalidMaxNumException -> onError(ErrorState.InvalidMaxNum)
                    is InvalidMaxAdultNumException -> onError(ErrorState.InvalidMaxAdultNum)
                    is InvalidMaxChildNumException -> onError(ErrorState.InvalidMaxChildNum)
                    is InvalidMaxInfantNumException -> onError(ErrorState.InvalidMaxInfantNum)
                    is InvalidRoomTypePriceException -> onError(ErrorState.InvalidRoomTypePrice)
                    is InvalidRoomCodeException -> onError(ErrorState.InvalidRoomCodeException)
                    is InvalidTaskNameException -> onError(ErrorState.InvalidTaskNameException)
                    is InvalidTaskAdminNotesException -> onError(ErrorState.InvalidTaskAdminNotes)
                    is InvalidTaskWorkerNotesException -> onError(ErrorState.InvalidTaskWorkerNotes)
                    is InvalidDateException -> onError(ErrorState.InvalidDate)
                    is InvalidGuestNameException -> onError(ErrorState.InvalidGuestName)
                    is EmptySettingsException -> onError(ErrorState.EmptySettings)
                    is AccountNotFoundException -> onError(ErrorState.InvalidAccount)
                    is InvalidOldPasswordException -> onError(ErrorState.InvalidOldPassword)
                    is IOException -> onError(ErrorState.NoInternet)
                    is InvalidReservationExistsException -> onError(ErrorState.InvalidReservationExists)
                    is InvalidCheckoutException -> onError(ErrorState.InvalidCheckout)
                    is InvalidDeleteRoleException -> onError(ErrorState.InvalidDeleteRole)

                    else -> onError(ErrorState.UnknownError(exception.message.toString()))
                }
            }
        }
    }

    protected fun <T> collectFlow(
        flow: Flow<T>,
        updateState: S.(T) -> S
    ) {
        viewModelScope.launch {
            flow.collect { value ->
                _state.update { it.updateState(value) }
            }
        }
    }

    private fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
        require(periodMillis > 0)
        return flow {
            var lastTime = 0L
            collect { value ->
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (currentTime - lastTime >= periodMillis) {
                    lastTime = currentTime
                    emit(value)
                }
            }
        }
    }

    fun <T : Any> tryToExecutePaging(
        call: suspend () -> Flow<PagingData<T>>,
        onSuccess: suspend (PagingData<T>) -> Unit,
        onError: (ErrorState) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                val result = call().cachedIn(viewModelScope)
                result.collect { data ->
                    onSuccess(data)
                }
            } catch (exception: Exception) {
                when (exception) {
                    is NoInternetException -> onError(ErrorState.NoInternet)
                    is PermissionDenied -> onError(ErrorState.HasNoPermission)
                    is ServerSideException -> onError(ErrorState.RequestFailed)
                    is UserNotFoundException -> onError(ErrorState.UserNotExist(exception.message.toString()))
                    is NotFoundedException -> onError(ErrorState.NotFound)
                    is AuthorizationException -> onError(ErrorState.UnAuthorized)
                    is WrongPasswordException -> onError(ErrorState.WrongPassword(exception.message.toString()))
                    is InvalidCredentialsException -> onError(
                        ErrorState.InvalidCredentials(
                            exception.message.toString()
                        )
                    )

                    is UnknownErrorException -> onError(ErrorState.UnknownError(exception.message.toString()))
                    is InvalidUserNameOrPasswordException -> onError(ErrorState.InvalidUserNameOrPassword)
                    is UnAuthorizedAccessException -> onError(ErrorState.UnAuthorized)
                    is InvalidWalletProductNameException -> onError(ErrorState.InvalidProductName)
                    is InvalidWalletProductPriceException -> onError(ErrorState.InvalidProductPrice)
                    is InvalidRequestParameterException -> onError(ErrorState.InvalidRequestParameter)
                    is InvalidWalletProductQuantityException -> onError(ErrorState.InvalidProductQuantity)
                    is InvalidServiceNameException -> onError(ErrorState.InvalidServiceName)
                    is InvalidServicePriceException -> onError(ErrorState.InvalidProductPrice)
                    is WalletNotFoundException -> onError(ErrorState.WalletNotFound)
                    is InvalidUserNameException -> onError(ErrorState.InvalidUserName)
                    is InvalidFullNameException -> onError(ErrorState.InvalidFullName)
                    is InvalidPhoneException -> onError(ErrorState.InvalidPhone)
                    is InvalidEmailException -> onError(ErrorState.InvalidEmail)
                    is InvalidPasswordException -> onError(ErrorState.InvalidPassword)
                    is InvalidRoleException -> onError(ErrorState.InvalidRole)
                    is InvalidDeadlineException -> onError(ErrorState.InvalidDeadline)
                    is UserNameAlreadyExistException -> onError(ErrorState.UserNameAlreadyExist)
                    is InvalidBedTypeNameException -> onError(ErrorState.InvalidBedTypeName)
                    is InvalidBedTypeCodeException -> onError(ErrorState.InvalidBedTypeCode)
                    is InvalidBedTypeDescriptionException -> onError(ErrorState.InvalidBedTypeDescription)
                    is InvalidRoomTypeCodeException -> onError(ErrorState.InvalidRoomTypeCode)
                    is InvalidMaxNumException -> onError(ErrorState.InvalidMaxNum)
                    is InvalidMaxAdultNumException -> onError(ErrorState.InvalidMaxAdultNum)
                    is InvalidMaxChildNumException -> onError(ErrorState.InvalidMaxChildNum)
                    is InvalidMaxInfantNumException -> onError(ErrorState.InvalidMaxInfantNum)
                    is InvalidRoomTypePriceException -> onError(ErrorState.InvalidRoomTypePrice)
                    is InvalidRoomCodeException -> onError(ErrorState.InvalidRoomCodeException)
                    is InvalidTaskNameException -> onError(ErrorState.InvalidTaskNameException)
                    is InvalidTaskAdminNotesException -> onError(ErrorState.InvalidTaskAdminNotes)
                    is InvalidTaskWorkerNotesException -> onError(ErrorState.InvalidTaskWorkerNotes)
                    is InvalidDateException -> onError(ErrorState.InvalidDate)
                    is InvalidGuestNameException -> onError(ErrorState.InvalidGuestName)
                    is EmptySettingsException -> onError(ErrorState.EmptySettings)
                    is AccountNotFoundException -> onError(ErrorState.InvalidAccount)
                    is InvalidOldPasswordException -> onError(ErrorState.InvalidOldPassword)
                    is IOException -> onError(ErrorState.NoInternet)
                    else -> onError(ErrorState.UnknownError(exception.message.toString()))
                }
            }
        }
    }


    companion object {
        const val MAX_PAGE_SIZE = 10
    }
}