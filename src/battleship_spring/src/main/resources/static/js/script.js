
/*
$(document).ready(async () => {
    await $.ajax({
        url: '/api/initialize',
        method: 'POST',
        contentType: 'application/json', // Tells the server you're sending JSON
        data: JSON.stringify( ['A', 'B'] ),
        error: function() {
            alert('Error in initializaiton.');
        }
    });

    await $.ajax({
        url: '/api/board/A',
        method: 'GET',
        success: function(res) {
            console.log(res)
        }
    })

    await $.ajax({
        url: '/api/gameStage',
        method: 'GET',
        success: function(res) {
            console.log(res)
        }
    })
})
*/
/*
import { initializeHider } from "./initializers"
import { initializeSetup, initializeDefualt, initializeEnd, initializePlays } from "./initialize";
import { toggleHider } from "./visibility";
*/
$(document).ready(async () => {

    // common setups

    initializeHider()

    // gameStage setup

    const gameStage = await $.ajax({
        url: '/api/gameStage',
        method: 'GET',
    })

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