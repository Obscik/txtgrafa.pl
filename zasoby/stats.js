
/*
* Logika strony
*/
const loadingScreen = document.getElementById("loading-screen");
const searchScreen = document.getElementById("search-player");
const content = document.getElementById("content");
const errorPlayerNotFound = document.getElementById("player-not-found");
const errorOther = document.getElementById("unknown-error");
const searchBox = document.getElementById("player-search-input");

const url = `https://api.pantruskawka045.me`;

searchBox.addEventListener("keyup", event => {
    if(event.key === "Enter" || event.keyCode === 13){
        window.location=`?player=${document.getElementById('player-search-input').value}`;
    }
});

const format = (data) => {
    let classes = document.getElementsByClassName("data-container");
    for (let i = 0; i <classes.length; i++) {
        data.values.forEach((pair) => {
            let replaced = classes[i]?.innerHTML;
            replaced = replaced.replace(`%%${pair.key}%%`, `${pair.value}`);
            classes[i].innerHTML = replaced;
        });
    }
}


const getJson = async (path) => {
    try {
        let response = await fetch(new Request(path));
        return await response.json();
    } catch (e) {
        console.error('Error:', e)
        return null;
    }
}

const loadPage = async (params) => {
    if (!params.has("player")) {
        loadingScreen.style.display = 'none';
        searchScreen.style.display = 'block';
        return;
    }
    let data = await getJson(`${url}/api/v1/playerData?player=${params.get("player")}`)
    console.log(data);
    if (data == null) {
        loadingScreen.style.display = 'none';
        searchScreen.style.display = 'block';
        errorOther.style.display = 'block';
        return;
    }
    if(!data.success){
        loadingScreen.style.display = 'none';
        searchScreen.style.display = 'block';
        switch (data.code){
            case 404:
                errorPlayerNotFound.style.display = 'block';
                return;
            default:
                errorOther.style.display = 'block';
                return;
        }
        return;
    }

    format(data);
    loadingScreen.style.display = 'none';
    content.style.display = 'flex';
    format(data);
    switch (params.get("open")){
        case "sw": {
            togglePage(document.getElementById("skywars-header"))
            break;
        }
        case "bw": {
            togglePage(document.getElementById("bedwars-header"))

            break;
        }
        case "hw": {
            togglePage(document.getElementById("headwars-header"))
            break;
        }
        case "tb": {
            togglePage(document.getElementById("thebridge-header"))
            break;
        }
        case "tg": {
            togglePage(document.getElementById("tntgames-header"))
            break;
        }
    }
    history.replaceState && history.replaceState(
        null, '', location.pathname + location.search.replace(/[\?&]open=[^&]+/, '').replace(/^&/, '?')
    ); //I <3 Stackoverflow

}

loadPage(new URLSearchParams(window.location.search));




