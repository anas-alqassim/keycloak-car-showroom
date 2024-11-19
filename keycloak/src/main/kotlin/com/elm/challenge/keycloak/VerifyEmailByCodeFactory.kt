package com.elm.challenge.keycloak

import com.elm.challenge.keycloak.EmailCodeUtil.CONFIG_CODE_LENGTH
import com.elm.challenge.keycloak.EmailCodeUtil.CONFIG_CODE_SYMBOLS
import com.elm.challenge.keycloak.EmailCodeUtil.DEFAULT_CODE_LENGTH
import com.elm.challenge.keycloak.EmailCodeUtil.DEFAULT_CODE_SYMBOLS
import com.elm.challenge.keycloak.EmailCodeUtil.VERIFY_EMAIL_CODE
import org.keycloak.Config
import org.keycloak.authentication.RequiredActionFactory
import org.keycloak.authentication.RequiredActionProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.provider.ServerInfoAwareProviderFactory

class VerifyEmailByCodeFactory : RequiredActionFactory, ServerInfoAwareProviderFactory {

    override fun create(session: KeycloakSession): RequiredActionProvider =
        VerifyEmailByCodeProvider(codeLength, codeSymbols)

    override fun init(config: Config.Scope) {
        codeLength = config.getInt(CONFIG_CODE_LENGTH, DEFAULT_CODE_LENGTH)
        codeSymbols = config[CONFIG_CODE_SYMBOLS, DEFAULT_CODE_SYMBOLS]
    }

    override fun postInit(factory: KeycloakSessionFactory) {}

    override fun close() {}

    override fun getId(): String = VERIFY_EMAIL_CODE

    override fun getDisplayText(): String = "Verify email by code"

    override fun getOperationalInfo(): Map<String, String> = mapOf(
        "${VERIFY_EMAIL_CODE}.$CONFIG_CODE_LENGTH" to codeLength.toString(),
        "${VERIFY_EMAIL_CODE}.$CONFIG_CODE_SYMBOLS" to codeSymbols
    )

    companion object {
        private var codeLength: Int = DEFAULT_CODE_LENGTH
        private var codeSymbols: String = DEFAULT_CODE_SYMBOLS
    }
}
