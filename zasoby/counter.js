

const getTimeout = (timeout) => {
    let time = 100 - timeout;
    if(time < 1) return 1000*2;
    if(time < 2) return 500*1.75;
    if(time < 3) return 250*1.5;
    if(time < 5) return 200;
    if(time < 10) return 150;
    if(time < 25) return 100;
    if(time < 50) return 50;
    if(time < 750) return 10;
    return 5;
}

const format = (data) => {
    let classes = document.getElementsByClassName("data-container");
    for (let i = 0; i <classes.length; i++) {
        let replaced = classes[i]?.innerHTML;
        replaced = replaced
            .replace(`%%GENERAL_USER_COUNT%%`, `${data.botObject.userCount}`)
            .replace(`%%GENERAL_GUILD_COUNT%%`, `${data.botObject.serverCount}`)
            .replace(`%%GENERAL_ONLINE_TIME%%`, `${data.botObject.onlineSince}`)
        ;
        classes[i].innerHTML = replaced;
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

const reloadCounters = () =>{
    const counterObjects = document.getElementsByClassName("countable");
    for (let i = 0; i < counterObjects.length; i++) {
        const obj = counterObjects[i];
        const counter = +obj.getAttribute("counter");
        let v = 100-Math.abs(Math.min(counter - 100, 100));
        const delay = () => {
            var newCounter = `${(counter-100+v+1 < 0 ? 0 : counter-100+v+1)}`;
            if(obj.innerHTML !== newCounter) obj.innerHTML = newCounter;
            if(++v < 100){
                let timeout = getTimeout(v);
                setTimeout(delay, timeout);
            }
        }
        delay()

    }
}

const setup = async () => {
    const data = await getJson("https://api.pantruskawka045.me/api/v1/botStats?bot=KC_STATS");
    format(data);
    reloadCounters();

    const formatTime = () => {
        const counterObjects = document.getElementsByClassName("time-countable");
        for (let i = 0; i < counterObjects.length; i++) {
            const obj = counterObjects[i];
            var time = Date.now() - (+obj.getAttribute("timestamp"));






            obj.innerHTML = parseTime(time);
        }
        setTimeout(formatTime, 500);
    }

    formatTime();

}

setup()

function parseTime(time) {
    var diffSeconds = Math.floor(time / 1000 % 60);
    var diffMinutes = Math.floor(time / (60 * 1000) % 60);
    var diffHours = Math.floor(time / (60 * 60 * 1000) % 24);
    var diffDays = Math.floor(time / (24 * 60 * 60 * 1000));
    if(diffDays > 0) return diffDays + " dni, " + diffHours + " godzin";
    if(diffHours > 0) return diffHours + " godzin, " + diffMinutes + " minut";
    if(diffMinutes > 0) return diffMinutes + " minut, " + diffSeconds + " sekund";
    if(diffSeconds > 0) return diffSeconds + " sekund";
    return "Teraz";
}






// reloadCounters()