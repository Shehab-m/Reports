package com.leithcarsreports.presentation.base

open class XException(message: String) : Exception(message)

//region InternetException
class NoInternetException : XException("No internet connection")

//endregion
open class AuthorizationException(message: String) : XException(message)
class PermissionDenied(message: String) : AuthorizationException(message)


open class RequestException(message: String) : XException(message)
class ServerSideException : RequestException("")
class UnknownErrorException(val errorMessage: String) : RequestException(errorMessage)
class UserNotFoundException(val errorMessage: String) : RequestException(errorMessage)
class UserAlreadyExistsException(val errorMessage: String) : RequestException(errorMessage)
class WrongPasswordException(val errorMessage: String) : RequestException(errorMessage)
class UserNameAlreadyExistException : RequestException("")

class InvalidUserNameOrPasswordException : RequestException("")

class InvalidWalletProductNameException() : RequestException("")
class InvalidWalletProductQuantityException() : RequestException("")
class InvalidWalletProductPriceException() : RequestException("")
class InvalidRequestParameterException() : RequestException("")
class WalletNotFoundException() : RequestException("")
class InvalidServiceNameException() : RequestException("")
class InvalidRoleNameException() : RequestException("")
class InvalidRoomCodeException() : RequestException("")
class InvalidRoleDescriptionException() : RequestException("")
class InvalidRoleIdException() : RequestException("")
class InvalidBedTypeNameException() : RequestException("")
class InvalidBedTypeCodeException() : RequestException("")
class InvalidBedTypeDescriptionException() : RequestException("")
class InvalidRoomTypeCodeException() : RequestException("")
class InvalidMaxNumException() : RequestException("")
class InvalidMaxAdultNumException() : RequestException("")
class InvalidMaxInfantNumException() : RequestException("")
class InvalidMaxChildNumException() : RequestException("")

class InvalidRoomTypePriceException() : RequestException("")
class InvalidTaskNameException() : RequestException("")
class InvalidTaskAdminNotesException() : RequestException("")
class InvalidTaskWorkerNotesException() : RequestException("")

class InvalidServicePriceException() : RequestException("")
class InvalidServiceTypeNameException() : RequestException("")


class UnAuthorizedAccessException(val errorMessage: String) : RequestException(errorMessage)
class InvalidApiKeyException(val errorMessage: String) : RequestException(errorMessage)
class InvalidTokenException(val errorMessage: String) : RequestException(errorMessage)
class TokenExpiredException(val errorMessage: String) : RequestException(errorMessage)
class InvalidTokenTypeException(val errorMessage: String) : RequestException(errorMessage)
class AccountNotActiveException(val errorMessage: String) : RequestException(errorMessage)
class InvalidUserNameException() : RequestException("")
class InvalidFullNameException() : RequestException("")
class InvalidPasswordException() : RequestException("")
class InvalidReservationExistsException() : RequestException("")
class InvalidCheckoutException() : RequestException("")

class InvalidPhoneException() : RequestException("")
class InvalidEmailException() : RequestException("")
class InvalidRoleException() : RequestException("")
class InvalidDeadlineException() : RequestException("")
class InvalidDateException() : RequestException("")
class InvalidGuestNameException() : RequestException("")
class EmptySettingsException() : RequestException("")
class InvalidDeleteRoleException() : RequestException("")
class InvalidOldPasswordException() : RequestException("")
class AccountNotFoundException() : RequestException("")

open class InvalidCredentialsException() : XException("")

class NotFoundedException : XException("Not founded")

