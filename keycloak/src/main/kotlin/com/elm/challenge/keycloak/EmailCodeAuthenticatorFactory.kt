package com.elm.challenge.keycloak

import com.elm.challenge.keycloak.EmailCodeUtil.CONFIG_CODE_LENGTH
import com.elm.challenge.keycloak.EmailCodeUtil.CONFIG_CODE_SYMBOLS
import com.elm.challenge.keycloak.EmailCodeUtil.DEFAULT_CODE_LENGTH
import com.elm.challenge.keycloak.EmailCodeUtil.DEFAULT_CODE_SYMBOLS
import org.keycloak.Config
import org.keycloak.authentication.AuthenticatorFactory
import org.keycloak.models.AuthenticationExecutionModel.Requirement.ALTERNATIVE
import org.keycloak.models.AuthenticationExecutionModel.Requirement.CONDITIONAL
import org.keycloak.models.AuthenticationExecutionModel.Requirement.DISABLED
import org.keycloak.models.AuthenticationExecutionModel.Requirement.REQUIRED
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.provider.ProviderConfigProperty

@Suppress("TooManyFunctions")
class EmailCodeAuthenticatorFactory : AuthenticatorFactory {

    override fun create(session: KeycloakSession) = EmailCodeAuthenticator(codeLength, codeSymbols)

    override fun init(config: Config.Scope) {
        codeLength = config.getInt(CONFIG_CODE_LENGTH, DEFAULT_CODE_LENGTH)
        codeSymbols = config[CONFIG_CODE_SYMBOLS, DEFAULT_CODE_SYMBOLS]
    }

    override fun postInit(factory: KeycloakSessionFactory) {}

    override fun close() {}

    override fun getId() = "email-code-authenticator"

    override fun getHelpText() = "Authenticator that sends a verification code to user's email"

    override fun getConfigProperties() = listOf<ProviderConfigProperty>()

    override fun getDisplayType() = "Send verification code via email"

    override fun getReferenceCategory() = null

    override fun isConfigurable() = false

    override fun getRequirementChoices() = arrayOf(REQUIRED, ALTERNATIVE, CONDITIONAL, DISABLED)

    override fun isUserSetupAllowed() = false

    companion object {
        private var codeLength: Int = DEFAULT_CODE_LENGTH
        private var codeSymbols: String = DEFAULT_CODE_SYMBOLS
    }
}
