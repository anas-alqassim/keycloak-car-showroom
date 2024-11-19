document.addEventListener("DOMContentLoaded", () => {
    const backLink = document.querySelector('.elm-back-link');
    backLink?.addEventListener("click", () => {
        history.back();
    })
  });
  