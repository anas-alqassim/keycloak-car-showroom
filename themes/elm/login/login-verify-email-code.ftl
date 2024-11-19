<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true displayMessage=!messagesPerField.exists('email_code'); section>

    <#if section = "header">
        ${msg("emailVerifyTitle")}
    <#elseif section = "description" >
        <a class="elm-back-link">
            <img class="mb-m" src="${url.resourcesPath}/img/back.svg" alt="logo" width="32" height="32">
        </a>

        <h1>${msg("emailVerificationTitle")}</h1>
        <p class="mb-m">
            ${msg("emailVerifyInstruction1", user.email)}
        </p>

    <#elseif section = "form">
        <div class="verification-container">
            <h1 class="mt-l mb-m">${msg("emailVerificationEnterCode")}</h1>

            <div class="inputs">
                <input maxlength="1"/>
                <input maxlength="1"/>
                <input maxlength="1"/>
                <input maxlength="1"/>
            </div>
        </div>

        <form id="kc-verify-email-code-form" class="${properties.kcFormClass!}" action="${url.loginAction}"
              method="post">
            <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('email_code',properties.kcFormGroupErrorClass!)}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="email_code" class="${properties.kcLabelClass!}">${msg("email_code")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="hidden" id="email_code" name="email_code" class="${properties.kcInputClass!}"
                           aria-invalid="<#if messagesPerField.exists('email_code')>true</#if>"
                    />

                    <#if messagesPerField.exists('email_code')>
                        <span id="input-error-email_code" class="${properties.kcInputErrorMessageClass!}"
                              aria-live="polite">
                                ${kcSanitize(messagesPerField.get('email_code'))?no_esc}
                            </span>
                    </#if>
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <#if isAppInitiatedAction??>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}"
                               type="submit" value="${msg("doSubmit")}"/>
                        <button
                        class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}"
                        type="submit" name="cancel-aia" value="true" />${msg("doCancel")}</button>
                    <#else>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                               type="submit" value="${msg("doSubmit")}"/>
                    </#if>
                </div>
            </div>
        </form>
    <#elseif section = "info">
        <p class="instruction mt-m">
            ${msg("emailVerifyInstruction2")}
            <a href="">${msg("doResend")}</a>
        </p>
    </#if>
</@layout.registrationLayout>