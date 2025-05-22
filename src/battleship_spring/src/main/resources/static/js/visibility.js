

function hideHider() {
    document.getElementById('hider').classList.add('d-none')
}

function showHider() {
    document.getElementById('hider').classList.remove('d-none')
}

function toggleHider() {
    document.getElementById('hider').classList.toggle('d-none')
}


function hideLog() {
    document.getElementById('log').classList.add('d-none')
}

function showLog() {
    document.getElementById('log').classList.remove('d-none')
}

function toggleLog() {
    document.getElementById('log').classList.toggle('d-none')
}


function transitionHider(currentOwner) {
    $('#current-owner').text(currentOwner)
    showHider()
}

function transitionLog(msg) {
    $('#log-text').text(msg)

    showLog()
}