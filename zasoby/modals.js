let modals = [];

window.onload = async function () {
    modals = document.getElementsByClassName('modal');
};

// sterowanie scrollowaniem strony
const setScrolling = (bool) => {
    if (bool === false) {
        document.body.classList.add("noscroll");
        document.documentElement.classList.add("noscroll");
        return
    }
    document.body.classList.remove("noscroll");
    document.documentElement.classList.remove("noscroll");
}

// otwieranie pop-upu
const openModal = (modalName) => {
    for (let i = 0; i < modals.length; i++) {
        modals[i].style.display = "none";
    }
    let modal;
    console.log(modalName);
    switch (modalName) {
        case "modal1": {
            modal = document.getElementById(modalName);
            break;
        }
        case "modal2": {
            modal = document.getElementById(serverData?.guildInfo?.roles?.length ? "modal2-roles" : "modal2-missingRoles");
            break;
        }
    }

    if (modal != undefined) {
        modal.style.display = "block";
        setScrolling(false);
        modal.animate([
            { opacity: '0' },
            { opacity: '1' }
        ], {
            duration: 350, easing: "ease-out"
        });
    }
}

let modalCloseAnimation;
// zamykanie pop-upu
const closeModal = (modal) => {
    if (modalCloseAnimation?.playState !== 'running') {
        modalCloseAnimation = modal.animate([
            { opacity: '1' },
            { opacity: '0' }
        ], {
            duration: 350, easing: "ease-out"
        });
        modalCloseAnimation.onfinish = () => {
            modal.style.display = "none";
            setScrolling(true);
        }
    }
}
// po kliknieciu na krzyżyk pop-upu
const modalOnClose = (caller) => {
    let modal = document.getElementById(caller.parentElement.parentElement.parentElement.id);
    closeModal(modal);
}
window.onclick = (event) => {
    // po kliknięciu w tło pop-upu
    if (event.target.className === 'modal') {
        closeModal(event.target);
    }
}
