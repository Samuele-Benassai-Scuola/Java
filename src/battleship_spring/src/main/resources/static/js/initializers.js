

function createNewBoard(board) {
    $(board).empty()

    for (let x = 0; x < 10; x++) {
        for (let y = 0; y < 10; y++) {
            $(board).append('<div class="cell" data-tile-x="' + x + ' data-tile-y="' + y + '"></div>')
        }
    }
}

function createHider(hidden) {
    const text = $('<p></p>')

    $(document.body).append()
}