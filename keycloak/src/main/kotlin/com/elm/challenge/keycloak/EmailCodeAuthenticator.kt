package com.elm.challenge.keycloak

import com.elm.challenge.keycloak.EmailCodeUtil.EMAIL_CODE
import com.elm.challenge.keycloak.EmailCodeUtil.EmailInputResetCredentials
import com.elm.challenge.keycloak.EmailCodeUtil.INVALID_CODE
import com.elm.challenge.keycloak.EmailCodeUtil.createFormChallenge
import com.elm.challenge.keycloak.EmailCodeUtil.sendCodeToEmail
import jakarta.ws.rs.core.Response
import org.keycloak.authentication.AuthenticationFlowContext
import org.keycloak.authentication.Authenticator
import org.keycloak.events.EventBuilder
import org.keycloak.forms.login.LoginFormsProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.models.utils.FormMessage
import org.keycloak.sessions.AuthenticationSessionModel

class EmailCodeAuthenticator(private val codeLength: Int, private val codeSymbols: String) : Authenticator {

    override fun authenticate(context: AuthenticationFlowContext) {
        sendCodeToEmail(AuthenticationFlowContextAdapter(context), codeLength, codeSymbols, EmailInputResetCredentials)
        createFormChallenge(AuthenticationFlowContextAdapter(context))
    }

    override fun close() {}

    override fun action(context: AuthenticationFlowContext) {
        val enteredOtp: String? = context.httpRequest.decodedFormParameters.getFirst(EMAIL_CODE)
        val expectedOtp: String? = context.authenticationSession.getAuthNote(EMAIL_CODE)

        if (expectedOtp != null && expectedOtp == enteredOtp) {
            context.success()
        } else {
            createFormChallenge(AuthenticationFlowContextAdapter(context), FormMessage(EMAIL_CODE, INVALID_CODE))
        }
    }

    override fun requiresUser() = true

    override fun configuredFor(session: KeycloakSession, realmModel: RealmModel, userModel: UserModel) = true

    override fun setRequiredActions(session: KeycloakSession, realmModel: RealmModel, userModel: UserModel) {}

}

class AuthenticationFlowContextAdapter(private val context: AuthenticationFlowContext) : CommonContext {
    override var form: LoginFormsProvider = context.form()
    override var event: EventBuilder = context.event
    override var user: UserModel = context.user
    override var session: KeycloakSession = context.session
    override var authenticationSession: AuthenticationSessionModel = context.authenticationSession
    override fun challenge(response: Response) = context.challenge(response)
}
