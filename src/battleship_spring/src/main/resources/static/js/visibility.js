

function hideHider() {
    document.getElementById('hider').classList.add('d-none')
}

function showHider() {
    document.getElementById('hider').classList.remove('d-none')
}

function toggleHider() {
    document.getElementById('hider').classList.toggle('d-none')
}


function transitionHider(currentPlayer) {
    $('#current-player').text(currentPlayer)
    showHider()
}