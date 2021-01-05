(function(factory) {
    var root = typeof window !== 'undefined' ? window : global;
    if (typeof define === 'function' && define.amd) {
        define([' '], function(wcwidth) {
            return (root.ascii_table = factory(wcwidth));
        });
    } else if (typeof module === 'object' && module.exports) {
        module.exports = factory(require('wcwidth'));
    } else {
        root.ascii_table = factory(root.wcwidth);
    }
})(function(wcwidth, undefined) {

    var strlen = (function() {
        if (typeof wcwidth === 'undefined') {
            return function(string) {
                return string.length;
            };
        } else {
            return wcwidth;
        }
    })();

    function ascii_table(array, header) {

        if (!array.length) {
            return '';
        }

        for (var i = array.length - 1; i >= 0; i--) {
            var row = array[i];
            var stacks = [];
            for (var j = 0; j < row.length; j++) {
                var new_lines;
                if(row[j] == null)
                    new_lines = "".replace(/\r/g).split("\n");
                else
                    new_lines = row[j].toString().replace(/\r/g).split("\n");
                row[j] = new_lines.shift();
                stacks.push(new_lines);
            }
            var stack_lengths = stacks.map(function(column) {
                return column.length;
            });
            var new_rows_count = Math.max.apply(Math, stack_lengths);
            for (var k = new_rows_count - 1; k >= 0; k--) {
                array.splice(i + 1, 0, stacks.map(function(column) {
                    return column[k] || "";
                }));
            }
        }

        var lengths = array[0].map(function(_, i) {
            var col = array.map(function(row) {
                if (row[i] != undefined) {
                    var len = strlen(row[i]);
                    if (row[i].match(/\t/g)) {
                        len += row[i].match(/\t/g).length*3;
                    }
                    return len;
                } else {
                    return 0;
                }
            });
            return Math.max.apply(Math, col);
        });

        array = array.map(function(row) {
            return '| ' + row.map(function(item, i) {
                var size = strlen(item);
                if (item.match(/\t/g)) {
                    size += item.match(/\t/g).length*3;
                }
                if (size < lengths[i]) {
                    item += new Array(lengths[i] - size + 1).join(' ');
                }
                return item;
            }).join(' | ') + ' |';
        });

        array = array.map(function(line) {
            return line.replace(/&(?![^;]+;)/g, '&amp;');
        });

        var sep = '+' + lengths.map(function(length) {
            return new Array(length + 3).join('-');
        }).join('+') + '+';

        if (header) {
            return sep + '\n' + array[0] + '\n' + sep + '\n' +
                array.slice(1).join('\n') + '\n' + sep;
        } else {
            return sep + '\n' + array.join('\n') + '\n' + sep;
        }
    }
    return ascii_table;
});