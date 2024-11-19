package com.elm.challenge.keycloak

import mu.KLogging
import org.keycloak.common.util.SecretGenerator
import org.keycloak.email.EmailException
import org.keycloak.email.EmailTemplateProvider
import org.keycloak.email.freemarker.beans.ProfileBean
import org.keycloak.events.Details
import org.keycloak.events.Errors
import org.keycloak.events.EventType.SEND_VERIFY_EMAIL
import org.keycloak.models.utils.FormMessage

object EmailCodeUtil : KLogging() {

    const val CONFIG_CODE_LENGTH = "code-length"
    const val CONFIG_CODE_SYMBOLS = "code-symbols"
    const val DEFAULT_CODE_LENGTH = 4
    val DEFAULT_CODE_SYMBOLS = String(SecretGenerator.DIGITS)
    const val EMAIL_CODE = "email_code"
    const val VERIFY_EMAIL_CODE = "verify-email-code"
    const val INVALID_CODE = "VerifyEmailInvalidCode"
    private const val LOGIN_VERIFY_EMAIL_CODE_TEMPLATE = "login-verify-email-code.ftl"
    private val supportEmail = System.getenv()["SUPPORT_EMAIL"] ?: ""
    private val helpCenterLink = System.getenv()["HELP_CENTER_LINK"] ?: ""

    fun sendCodeToEmail(
        context: CommonContext,
        codeLength: Int = DEFAULT_CODE_LENGTH,
        codeSymbols: String = DEFAULT_CODE_SYMBOLS,
        input: EmailInput
    ) {
        val event = context.event.clone().event(SEND_VERIFY_EMAIL).detail(Details.EMAIL, context.user.email)
        val code = SecretGenerator.getInstance().randomString(codeLength, codeSymbols.toCharArray())
        context.authenticationSession.setAuthNote(EMAIL_CODE, code)
        try {
            context.session
                .getProvider(EmailTemplateProvider::class.java)
                .setAuthenticationSession(context.authenticationSession)
                .setRealm(context.session.context.realm)
                .setUser(context.user)
                .send(
                    input.subjectKey(),
                    input.template(),
                    hashMapOf<String, Any>(
                        EMAIL_CODE to code,
                        "supportEmail" to supportEmail,
                        "helpCenterLink" to helpCenterLink
                    )
                )
            event.success()
        } catch (e: EmailException) {
            logger.error("Failed to send verification email for '${context.authenticationSession.action}'", e)
            event.error(Errors.EMAIL_SEND_FAILED)
            context.form.setError(Errors.EMAIL_SEND_FAILED)
        }
    }

    fun createFormChallenge(context: CommonContext, errorMessage: FormMessage? = null) {
        context.form.apply {
            errorMessage?.let { addError(FormMessage(EMAIL_CODE, INVALID_CODE)) }
            setAttribute("user", ProfileBean(context.user, context.session))
        }.createForm(LOGIN_VERIFY_EMAIL_CODE_TEMPLATE).let {
            context.challenge(it)
        }
    }

    interface EmailInput {
        fun subjectKey(): String
        fun template(): String
    }

    object EmailInputVerifyEmail : EmailInput {
        override fun subjectKey() = "emailVerificationSubject"
        override fun template() = "email-verification-with-code.ftl"
    }

    object EmailInputResetCredentials : EmailInput {
        override fun subjectKey() = "passwordResetSubject"
        override fun template() = "reset-password-with-code.ftl"
    }

}
