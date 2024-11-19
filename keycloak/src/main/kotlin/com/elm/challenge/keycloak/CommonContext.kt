package com.elm.challenge.keycloak

import jakarta.ws.rs.core.Response
import org.keycloak.events.EventBuilder
import org.keycloak.forms.login.LoginFormsProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.UserModel
import org.keycloak.sessions.AuthenticationSessionModel

interface CommonContext {
    var form: LoginFormsProvider
    var event: EventBuilder
    var user: UserModel
    var session: KeycloakSession
    var authenticationSession: AuthenticationSessionModel
    fun challenge(response: Response)
}
