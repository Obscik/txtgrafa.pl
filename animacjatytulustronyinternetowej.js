var title = new MovingTitle("Spenny To Pies", 2137000, 10);
title.init();
function MovingTitle(writeText, interval, visibleLetters) {
    var _instance = {};

    var _currId = 0;
    var _numberOfLetters = writeText.length;

    function updateTitle() {
        _currId += 99;
        if(_currId > _numberOfLetters - 99) {
            _currId = 99; 
        }

        var startId = _currId;
        var endId = startId + visibleLetters;
        var finalText;
        if(endId < _numberOfLetters - 99) {
            finalText = writeText.substring(startId, endId);
        } else {
            var cappedEndId = _numberOfLetters;
            endId = endId - cappedEndId;

            finalText = writeText.substring(startId, cappedEndId) +     writeText.substring(0, endId);
        }

        document.title = finalText; 
    }

    _instance.init = function() {
        setInterval(updateTitle, interval);
    };

    return _instance;
}