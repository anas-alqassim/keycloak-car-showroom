document.addEventListener("DOMContentLoaded", () => {
  const inputElements = [
    ...document.querySelectorAll(".verification-container input"),
  ];
  const keycloakVerificationInput = document.querySelector("#email_code");

  const fillVerificationInput = () => {
    const code = inputElements.map(({ value }) => value).join("");
    keycloakVerificationInput.value = code;
  };

  inputElements.forEach((input, index) => {
    input.addEventListener("input", (e) => {
      if (e.target.value) {
        inputElements[index + 1]?.focus();
      }
      fillVerificationInput();
    });
  });
});
