


document.addEventListener('DOMContentLoaded', () => {
    fetch('http://localhost:9090/api/getGameStage', {
        method: 'GET'
    })
})
.then(res => res.json())
.then(data => console.log(data))