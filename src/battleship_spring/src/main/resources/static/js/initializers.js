

function createNewBoard(id) {
    const element = document.querySelector(id)
    element.innerHTML = ''

    for (let x = 0; x < 10; x++) {
        for (let y = 0; y < 10; y++) {
            element.innerHTML += '<div class="cell" data-tile-x="' + x + ' data-tile-y="' + y + '"></div>'
        }
    }
}