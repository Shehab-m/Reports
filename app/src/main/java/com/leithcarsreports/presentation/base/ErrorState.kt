package com.leithcarsreports.presentation.base

sealed interface ErrorState {
    object NoInternet : ErrorState
    object HasNoPermission : ErrorState
    object RequestFailed : ErrorState
    object UnAuthorized : ErrorState
    data class UserNotExist(val errorMessage: String) : ErrorState
    data class WrongPassword(val errorMessage: String) : ErrorState
    data class InvalidCredentials(val errorMessage: String) : ErrorState
    data class UnknownError(val errorMessage: String) : ErrorState
    object InvalidUserNameOrPassword : ErrorState
    data class InvalidRoleNameException(val errorMessage: String) : ErrorState
    object EmailAlreadyExist : ErrorState
    object AccountAlreadyExist : ErrorState
    object NoConnection : ErrorState
    object InvalidProductName : ErrorState
    object InvalidRequestParameter : ErrorState
    object InvalidProductPrice : ErrorState
    object InvalidProductQuantity : ErrorState
    object InvalidServiceName : ErrorState
    object WalletNotFound : ErrorState
    object InvalidUserName : ErrorState
    object InvalidFullName : ErrorState
    object InvalidPassword : ErrorState
    object InvalidPhone : ErrorState
    object InvalidEmail : ErrorState
    object InvalidRole : ErrorState
    object InvalidDeadline : ErrorState
    object UserNameAlreadyExist : ErrorState
    object InvalidBedTypeName : ErrorState
    object InvalidBedTypeCode : ErrorState
    object InvalidBedTypeDescription : ErrorState
    object InvalidRoomTypeCode : ErrorState
    object InvalidMaxNum : ErrorState
    object InvalidMaxAdultNum : ErrorState
    object InvalidMaxInfantNum : ErrorState
    object InvalidMaxChildNum : ErrorState

    object InvalidRoomTypePrice : ErrorState
    object InvalidRoomCodeException : ErrorState
    object InvalidTaskNameException : ErrorState
    object InvalidTaskAdminNotes : ErrorState
    object InvalidTaskWorkerNotes : ErrorState
    object InvalidDate : ErrorState
    object InvalidGuestName : ErrorState
    object EmptySettings : ErrorState
    object NotFound : ErrorState
    object InvalidDeleteRole : ErrorState
    object InvalidAccount : ErrorState
    object InvalidOldPassword : ErrorState

    object InvalidReservationExists: ErrorState
    object InvalidCheckout: ErrorState

}

