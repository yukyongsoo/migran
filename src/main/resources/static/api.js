const host = "http://localhost:8080"

async function getBatchList() {
    let response =  await axios.get(`${host}/batch`)
    return response.data
}

async function getBatchHistory(jobName) {
    let response = await axios.get(`${host}/batch/${jobName}`)
    return response.data
}

async function startBatch(jobName) {
    await axios.post(`${host}/batch/start/${jobName}`)
}

async function stopBatch(jobName) {
    await axios.post(`${host}/batch/stop/${jobName}`)
}