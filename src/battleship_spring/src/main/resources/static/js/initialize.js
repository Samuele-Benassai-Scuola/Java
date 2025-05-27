
async function initializeSetup() {
    const owners = (await $.ajax({
        url: '/api/owners',
        method: 'GET',
    }))
    .owners

    let currentOwner = 0
    let remainingShips = await $.ajax({
        url: '/api/remainingShips/' + owners[0],
        method: 'GET',
    })

    if (Object.keys(remainingShips).length === 0) {
        currentOwner = 1
        remainingShips = await $.ajax({
            url: '/api/remainingShips/' + owners[1],
            method: 'GET',
        })
    }

    const boardData = await $.ajax({
        url: '/api/board/' + owners[currentOwner],
        method: 'GET',
    })

    $('#sub-head').text( owners[currentOwner] + ', è il tuo turno di piazzare!' )

    createBoardsSetup(owners, currentOwner, boardData)
    createPlaceInputs(remainingShips)
    
    return 
}

async function initializePlays() {
    const owners = (await $.ajax({
        url: '/api/owners',
        method: 'GET',
    }))
    .owners

    const currentOwner = (await $.ajax({
            url: '/api/owners/current',
            method: 'GET',
        }))
        .owner

    const currentOwnerId = [0, 1].find(
        ownerId => owners[ownerId] == currentOwner
    )

    const currentBoardData = await $.ajax({
        url: '/api/board/' + owners[currentOwnerId],
        method: 'GET',
    })

    const hiddenBoardData = await $.ajax({
        url: '/api/board/hidden/' + owners.find(owner => owner !== currentOwner),
        method: 'GET',
    })

    $('#sub-head').text( currentOwner + ', è il tuo turno di colpire!' )

    transitionHider(currentOwner)

    createBoardsPlays(owners, currentOwnerId, currentBoardData, hiddenBoardData)

    return 
}

async function initializeEnd() {
    const owners = (await $.ajax({
        url: '/api/owners',
        method: 'GET',
    }))
    .owners

    const winner = (await $.ajax({
        url: '/api/winner',
        method: 'GET',
    }))
    .winner

    const owner0Data = await $.ajax({
        url: '/api/board/' + owners[0],
        method: 'GET',
    })

    const owner1Data = await $.ajax({
        url: '/api/board/' + owners[1],
        method: 'GET',
    })

    $('#sub-head').text( winner + ', hai vinto!' )

    createBoardsEnd(owners, owner0Data, owner1Data)

    return
} 

async function initializeDefault() {
    $('#sub-head').text( 'Digita i nomi dei giocatori!' )

    createInitializeOwners()

    return
}