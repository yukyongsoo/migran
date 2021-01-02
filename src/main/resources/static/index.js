let term;

const options = {
    greetings: 'Migran Batch Terminal',
    completion: true,
    prompt: "Batch [[;green;]>>> ]"
}

const methods = {
    help: function (){
        this.echo('list: show current all batch list')
        this.echo('status [id]: show current batch status')
    },
    list: function () {
        
    },
    status: function (id) {

    }
}

term = $('#terminal').terminal(methods, options)