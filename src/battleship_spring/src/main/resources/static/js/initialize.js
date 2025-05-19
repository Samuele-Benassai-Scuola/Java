

document.addEventListener('DOMContentLoaded', () => {
    fetch('http://localhost:9090/api/initialize', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(['A', 'B'])
    })
    .then(res => {
        fetch('http://localhost:9090/api/board/A', {
            method: 'GET',
        })
        .then(res => {
            console.log(res)

            // TODO: fix the logging (logs the whole response, but just want the positions)
        })
    })
})
