$('#for_elements_value').html($('#elements').val())
$('#for_population_value').html($('#population').val())
$('#for_iterations_value').html($('#iterations').val())
$('#for_bag_size_value').html($('#bag_size').val())
$('#for_max_cost_value').html($('#max_cost').val())
$('#for_max_weight_value').html($('#max_weight').val())
$('#for_mutation_value').html($('#mutation').val())

//Const
const DAY = 24 * 60 * 60 * 1000;
const HOUR = 60 * 60 * 1000;
const MINUTE = 60 * 1000;
const SECOND = 1000;

$("#submit").click(function () {
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "random_bag",
        data : JSON.stringify(
            {
                elements      : $('#elements').val(),
                population    : $('#population').val(),
                iterations    : $('#iterations').val(),
                bag_size      : $('#bag_size').val(),
                range_costs   : $('#max_cost').val(),
                range_weights : $('#max_weight').val(),
                mutation_rate : $('#mutation').val()
            }
        ),
        dataType : 'json',
    }).done(function(data) {
        let d = Math.floor(data.elapsed_time / DAY);
        let h = Math.floor((data.elapsed_time - d * DAY) / HOUR);
        let m = Math.floor((data.elapsed_time - d * DAY - h * HOUR) / MINUTE);
        let s = Math.floor((data.elapsed_time - d * DAY - h * HOUR - m * MINUTE) / SECOND);
        let ms = Math.floor((data.elapsed_time - d * DAY - h * HOUR - m * MINUTE - s * SECOND));

        $('#costs').text(data.costs);
        $('#weights').text(data.weights);
        $('#result').text(data.result);
        $('#result_fitness').text(data.fitness);
        $('#result_iterations').text(data.iterations);
        $("#result_elapsed_time").text(d + "d " + h + "h " + m + "m " + s + "s " + ms + "ms");
    });
});
