

function createNewBoard(board) {
    $(board).empty()

    for (let x = 0; x < 10; x++) {
        for (let y = 0; y < 10; y++) {
            $(board).append('<div class="cell" data-tile-x="' + x + ' data-tile-y="' + y + '"></div>')
        }
    }
}

function createInitializeOwners() {
    document.body.innerHTML += 
        `
        <div id="initialize-owners" class="w-75 text-center mx-auto">
            <div class="mb-3">
                <label for="ownerFirst" class="form-label">Giocatore 1</label>
                <input id="owner-0" type="text">
            </div>
            <div class="mb-3">
                <label for="ownerSecond" class="form-label">Giocatore 2</label>
                <input id="owner-1" type="text">
            </div>
            <button id="submit-owners" type="button" class="btn btn-primary">
                INIZIA!
            </button>
        </div>
        `
    
    $('#submit-owners').click(() => {
        $.ajax({
            url: '/api/initialize',
            method: 'POST',
            contentType: 'application/json', // Tells the server you're sending JSON
            data: JSON.stringify( ['A', 'B'] ),
            error: function() {
                alert('Error in initializaiton.')
            }
        })
        .then(() => {
            window.location.reload()
        })
    })
}

function initializeHider() {
    $('#close-hider').click(() => {
        hideHider()
    })
}