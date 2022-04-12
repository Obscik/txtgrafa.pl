
const toggleDisplay = (item) => {
    if(item.classList.contains(`hide`)) {
        return;
    }
    if(item.classList.contains(`hidden`)){
        item.classList.remove(`hidden`);
        item.classList.add(`show`);
        item.getElementsByClassName("stats-content")[0].style.maxHeight =  item.getElementsByClassName("stats-content")[0].scrollHeight + "px";
    } else {
        item.getElementsByClassName("stats-content")[0].style.maxHeight =  null;
        item.classList.remove(`show`);
        item.classList.add(`hide`);

        setTimeout(() => {
            item.classList.add("remove-margin");
        }, 100);
        setTimeout(() => {
            item.classList.add(`hidden`);
            item.classList.remove(`hide`);

        }, 200);

        setTimeout(() => {
            item.classList.remove("remove-margin");
        }, 200);
    }
}

const toggleSvg = (svg) => {
    if(svg.style.transform === `` || svg.style.transform === `rotate(-180deg)`){
        svg.style.transform = 'rotate(0deg)';
    } else {
        svg.style.transform = 'rotate(-180deg)';
    }
}

const togglePage = (label) => {
    toggleSvg(label.querySelectorAll("svg")[0]);
    toggleDisplay(label.parentElement);
}
