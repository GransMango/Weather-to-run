package no.uio.ifi.in2000.team53.weatherapp.exceptions

// Don't think this really needs any KDoc, it's pretty self-explanatory,
// Exception that is thrown when authentication fails.
class AuthenticationException(message : String) : Exception(message)