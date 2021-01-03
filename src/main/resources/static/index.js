figlet.defaults({fontPath: 'https://unpkg.com/figlet@1.4.0/fonts/'})
figlet.preloadFonts(["Standard", "Slant"], ready)

function render(text, font) {
    return figlet.textSync(text, {
        font: font || 'Standard',
        width: !term ? 130 : term.cols(),
        whitespaceBreak: true
    })
}

let term

const options = {
    greetings:  function() {
        return render('Welcome To Migran Batch', 'Slant') +
            `\n[[;rgba(255,255,255,0.99);]Migran Batch Terminal]. Please Use "help".\n`;
    },
    completion: true,
    prompt: "Batch [[;green;]>>> ]"
}

const methods = {
    help: function (){
        this.echo('list: show current all batch list')
        this.echo('status [jobName]: show batch history')
        this.echo('start [jobName]: start Batch. if not running')
        this.echo('stop [jobName]: stop Batch. if started')
    },
    list: async function () {
        try {
            const data = await getBatchList()

            const tableData = []
            const header = ['Name','Last Start Date','Last End Date','Current Status']
            tableData.push(header)
            tableData.push(['','','',''])
    
            data.forEach(element => {
                tableData.push(Object.values(element))
            })
    
            term.echo(ascii_table(tableData))
        }
        catch (error) {
            term.echo(`[[;red;]${error.response.data}]`)
        }
    },
    status: async function (jobName) {
        try {
            const data = await getBatchHistory(jobName)

            const tableData = []
            const header = ['Start Date','End Date','Exit Status','Current Status']
            tableData.push(header)
            tableData.push(['','','',''])
    
            data.forEach(element => {
                tableData.push(Object.values(element))
            })
    
            term.echo(ascii_table(tableData))
        } catch (error) {
            term.echo(`[[;red;]${error.response.data}]`)
        }
    },
    start: async function (jobName) {
        try {
            await startBatch(jobName)    
        } catch (error) {
            term.echo(`[[;red;]${error.response.data}]`)
        }
    },
    stop: async function (jobName) {
        try {
            await stopBatch(jobName)
        } catch (error) {
            term.echo(`[[;red;]${error.response.data}]`)
        }
    }
}

function ready() {
    term = $('#terminal').terminal(methods, options)
}