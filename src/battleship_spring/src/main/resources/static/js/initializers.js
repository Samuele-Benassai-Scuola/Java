

function createNewBoard(board) {
    $(board).empty()

    for (let y = 0; y < 10; y++) {
        for (let x = 0; x < 10; x++) {
            $(board).append('<div class="cell" data-pos-x="' + x + '" data-pos-y="' + y + '"></div>')
        }
    }
}

function setupClickableCells(owner, gameStage) {
    $(document).on('click', '.clickable', function() {
        const x = $(this).data('pos-x')
        const y = $(this).data('pos-y')

        switch (gameStage) {
            case 'SETUP':
                const orientation = $('input[name="orientation"]:checked').val()
                const size = $('input[name="size"]:checked').val()

                $.ajax({
                    url: '/api/place',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        owner: owner,
                        x: x,
                        y: y,
                        size: size,
                        orientation: orientation
                    }),
                    success: () => {
                        restart()
                    },
                    error: () => {
                        alert('input invalido')
                    }
                })
                break
            case 'PLAYS':
                $.ajax({
                    url: '/api/shoot',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        x: x,
                        y: y
                    }),
                    success: (res) => {
                        let msg
                        if (res.hit) {
                            msg = 'Colpito!'
                            if (res.sunk) {
                                msg += ' E affondato!'
                            }
                        }
                        else {
                            msg = 'Mancato!'
                        }

                        transitionLog(msg)
                    },
                    error: () => {
                        alert('input invalido')
                    }
                })
                break
            default:
                break
        }
    })
}

function drawBoard(ownerId, boardData) {
    const colors = [
        "#f58c84",
        "#f7ac57",
        "#cfed55",
        "#55eda6",
        "#64f0f5",
        "#462da8",
        "#c564f5",
        "#ff8cd7",
        "#a86f1e",
    ]

    boardData.shot.forEach(pos => {
        $(
            '#owner-' + ownerId + ' .cell[data-pos-x="' + pos.x + '"][data-pos-y="' + pos.y + '"]'
        ).addClass('shot')
    })

    Object.entries(boardData).filter(([key, value]) => key !== 'shot').forEach(([key, value]) => {
        const color = colors.pop()

        value.forEach(pos => {
            $(
                '#owner-' + ownerId + ' .cell[data-pos-x="' + pos.x + '"][data-pos-y="' + pos.y + '"]'
            ).css('background-color', color)

            $(
                '#owner-' + ownerId + ' .cell[data-pos-x="' + pos.x + '"][data-pos-y="' + pos.y + '"]'
            ).addClass('ship')
        })
    })
}

function drawBoardHidden(ownerId, boardData) {
    if (boardData.miss) {
        boardData.miss.forEach(pos => {
            $(
                '#owner-' + ownerId + ' .cell[data-pos-x="' + pos.x + '"][data-pos-y="' + pos.y + '"]'
            ).addClass('shot')
        })
    }
    if (boardData.hit) {
        boardData.hit.forEach(pos => {
            $(
                '#owner-' + ownerId + ' .cell[data-pos-x="' + pos.x + '"][data-pos-y="' + pos.y + '"]'
            ).addClass('hit')
        })
    }
}

function createBoardsSetup(owners, currentOwnerId, boardData) {
    document.getElementById('master-container').innerHTML +=
        `
        <div id="boards" class="row w-75 d-flex justify-content-center mx-auto text-center">
            <div id="owner-0" class="col-6 text-center">
                <h3 class="title">${owners[0]}</h3>
                <div class="board justify-content-center"></div>
            </div>
            <div id="owner-1" class="col-6 text-center">
                <h3 class="title">${owners[1]}</h3>
                <div class="board justify-content-center"></div>
            </div>
        </div>
        `
    
    createNewBoard('#owner-0 .board')
    createNewBoard('#owner-1 .board')

    $('#owner-' + currentOwnerId + ' .cell').each(function() {
        $(this).addClass('clickable')
    })

    setupClickableCells(owners[currentOwnerId], 'SETUP')
    drawBoard(currentOwnerId, boardData)
}

function createBoardsPlays(owners, currentOwnerId, currentBoardData, hiddenBoardData) {
    document.getElementById('master-container').innerHTML +=
        `
        <div id="boards" class="row w-75 d-flex justify-content-center mx-auto text-center">
            <div id="owner-0" class="col-6 text-center">
                <h3 class="title">${owners[0]}</h3>
                <div class="board justify-content-center"></div>
            </div>
            <div id="owner-1" class="col-6 text-center">
                <h3 class="title">${owners[1]}</h3>
                <div class="board justify-content-center"></div>
            </div>
        </div>
        `

    createNewBoard('#owner-0 .board')
    createNewBoard('#owner-1 .board')

    const hiddenOwnerId = [0, 1].find(owner => owner !== owners[currentOwnerId])

    $('#owner-' + hiddenOwnerId + ' .cell').each(function() {
        $(this).addClass('clickable')
    })

    setupClickableCells(owners[currentOwnerId], 'PLAYS')

    drawBoard(currentOwnerId, currentBoardData)
    drawBoardHidden(hiddenOwnerId, hiddenBoardData)
}

function createBoardsEnd(owners, owner0Data, owner1Data) {
    document.getElementById('master-container').innerHTML +=
        `
        <div id="boards" class="row w-75 d-flex justify-content-center mx-auto text-center">
            <div id="owner-${owners[0]}" class="col-6 text-center">
                <h3 class="title">${owners[0]}</h3>
                <div class="board justify-content-center"></div>
            </div>
            <div id="owner-${owners[1]}" class="col-6 text-center">
                <h3 class="title">${owners[1]}</h3>
                <div class="board justify-content-center"></div>
            </div>
        </div>
        `

    createNewBoard('#owner-' + owners[0] + ' .board')
    createNewBoard('#owner-' + owners[1] + ' .board')

    drawBoard(owners[0], owner0Data)
    drawBoard(owners[1], owner1Data)
}

function createPlaceInputs(remainingShips) {
    document.getElementById('master-container').innerHTML +=
        `
        <div class="row w-25 mx-auto mt-3">
            <div id="orientation-form" class="col-lg-6 text-center">
                <h4>
                    Orientamento
                </h4>
                <div class="form-check">
                    <input id="orientation-horizontal" class="form-check-input" type="radio" name="orientation" value="HORIZONTAL">
                    <label class="form-check-label" for="orientation-horizontal">
                        ORIZZONTALE
                    </label>
                </div>
                <div class="form-check">
                    <input id="orientation-vertical" class="form-check-input" type="radio" name="orientation" value="VERTICAL">
                    <label class="form-check-label" for="orientation-vertical">
                        VERTICALE
                    </label>
                </div>
            </div>
            <div id="size-form" class="col-lg-6 text-center">
                <h4>
                    Dimensione
                </h4>
            </div>
        </div>
        `

    Object.keys(remainingShips).forEach(size => {
        document.getElementById('size-form').innerHTML +=
            `
            <div class="form-check">
                <input id="size-${size}" class="form-check-input" type="radio" name="size" value="${size}">
                <label class="form-check-label" for="size-${size}">
                    ${size}
                </label>
            </div>
            `
    })
}

function createInitializeOwners() {
    document.getElementById('master-container').innerHTML += 
        `
        <div id="initialize-owners" class="w-75 text-center mx-auto">
            <div class="mb-3">
                <label for="owner-0" class="form-label">Giocatore 1</label>
                <input id="owner-0" type="text">
            </div>
            <div class="mb-3">
                <label for="owner-1" class="form-label">Giocatore 2</label>
                <input id="owner-1" type="text">
            </div>
            <button id="submit-owners" type="button" class="btn btn-primary">
                INIZIA!
            </button>
        </div>
        `
    
    $('#submit-owners').click(() => {
        const owners = [$('#owner-0').val(), $('#owner-1').val()]

        $.ajax({
            url: '/api/initialize',
            method: 'POST',
            contentType: 'application/json', // Tells the server you're sending JSON
            data: JSON.stringify( owners ),
            error: function() {
                alert('Error in initialization.')
            }
        })
        .then(() => {
            restart()
        })
    })
}

function initializeHider() {
    $('#close-hider').click(() => {
        hideHider()
    })
}

function initializeLog() {
    $('#close-log').click(() => {
        restart()
    })
}

function initializeReset() {
    $('#reset').click(() => {
        $.ajax({
            url: '/api/reset',
            method: 'DELETE',
        })
        .then(() => {
            restart()
        })
    })
}