

function restart() {
    window.location.reload()
}



$(document).ready(async () => {

    // common setups

    
    initializeHider()
    initializeLog()
    initializeReset()
    createNewBoard('#owner-0 .board')
    createNewBoard('#owner-1 .board')

    // gameStage setup

    const gameStage = (await $.ajax({
            url: '/api/gameStage',
            method: 'GET',
        }))
        .gameStage

    switch (gameStage) {
        case 'SETUP':
            await initializeSetup()
            break
        case 'PLAYS':
            await initializePlays()
            break
        case 'END':
            await initializeEnd()
            break
        default:
            await initializeDefault()
            break
    }
})