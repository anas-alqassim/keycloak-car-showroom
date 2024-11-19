document.addEventListener("DOMContentLoaded", () => {
    const submit = document.querySelector('.require-mandatory-fields');
    const inputs = [...document.querySelectorAll('.required-field')];

    if(!submit || !inputs.length) {
        return;
    }

    const setButtonState = () => {
        const values = inputs.map((input) => input.value);
        submit.disabled = values.some((v) => !v);
    }

    setButtonState();

    inputs.forEach((input) => {
        input.addEventListener('input', () => setButtonState())
    })
  });
