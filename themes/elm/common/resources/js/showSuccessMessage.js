document.addEventListener("DOMContentLoaded", () => {
    const searchParams = new URLSearchParams(window.location.search);
    const toast = document.querySelector('#ngi-toast');
    const showMessage = searchParams.has('user_profile_deleted');

    if(toast && showMessage) {
        const btn = toast.querySelector('button');
        btn.addEventListener('click', () => {
            toast.style.display = 'none'    
        })
        toast.style.display = 'flex'
        setTimeout(() => {
            toast.style.display = 'none'
        }, 5_000)
    }
  });
  