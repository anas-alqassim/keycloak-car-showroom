package com.elm.challenge.keycloak

import com.elm.challenge.keycloak.EmailCodeUtil.EMAIL_CODE
import com.elm.challenge.keycloak.EmailCodeUtil.EmailInputVerifyEmail
import com.elm.challenge.keycloak.EmailCodeUtil.INVALID_CODE
import com.elm.challenge.keycloak.EmailCodeUtil.VERIFY_EMAIL_CODE
import com.elm.challenge.keycloak.EmailCodeUtil.createFormChallenge
import com.elm.challenge.keycloak.EmailCodeUtil.sendCodeToEmail
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriBuilderException
import org.keycloak.authentication.RequiredActionContext
import org.keycloak.authentication.RequiredActionProvider
import org.keycloak.events.Details
import org.keycloak.events.EventBuilder
import org.keycloak.events.EventType
import org.keycloak.forms.login.LoginFormsProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.UserModel
import org.keycloak.models.utils.FormMessage
import org.keycloak.sessions.AuthenticationSessionModel
import mu.KLogging

class VerifyEmailByCodeProvider(private val codeLength: Int, private val codeSymbols: String) : RequiredActionProvider {

    override fun close() {}

    override fun evaluateTriggers(context: RequiredActionContext) {
        if (context.realm.isVerifyEmail && !context.user.isEmailVerified) {
            context.user.addRequiredAction(VERIFY_EMAIL_CODE)
            logger.debug("User is required to verify email")
        }
    }

    override fun requiredActionChallenge(context: RequiredActionContext) {
        if (context.user.isEmailVerified || context.user.username.startsWith("service-account-")) {
            context.authenticationSession.removeAuthNote(EMAIL_CODE)
            context.success()
        } else if (context.user.email.isNullOrBlank()) {
            context.ignore()
        } else {
            sendVerifyEmailAndCreateForm(context)
        }
    }

    override fun processAction(context: RequiredActionContext) {
        val code = context.authenticationSession.getAuthNote(EMAIL_CODE)
        if (code == null) {
            requiredActionChallenge(context)
            return
        }

        val emailCode = context.httpRequest.decodedFormParameters.getFirst(EMAIL_CODE)
        val event = context.event.clone().event(EventType.VERIFY_EMAIL).detail(Details.EMAIL, context.user.email)
        if (code != emailCode) {
            createFormChallenge(RequiredActionContextAdapter(context), FormMessage(EMAIL_CODE, INVALID_CODE))
            event.error(INVALID_CODE)
        } else {
            context.user.isEmailVerified = true
            context.authenticationSession.removeAuthNote(EMAIL_CODE)
            event.success()
            context.success()
        }
    }

    @Throws(UriBuilderException::class, IllegalArgumentException::class)
    private fun sendVerifyEmailAndCreateForm(context: RequiredActionContext) {
        sendCodeToEmail(RequiredActionContextAdapter(context), codeLength, codeSymbols, EmailInputVerifyEmail)
        createFormChallenge(RequiredActionContextAdapter(context))
    }

    companion object : KLogging()

}

class RequiredActionContextAdapter(private val context: RequiredActionContext) : CommonContext {
    override var form: LoginFormsProvider = context.form()
    override var event: EventBuilder = context.event
    override var user: UserModel = context.user
    override var session: KeycloakSession = context.session
    override var authenticationSession: AuthenticationSessionModel = context.authenticationSession
    override fun challenge(response: Response) = context.challenge(response)
}
