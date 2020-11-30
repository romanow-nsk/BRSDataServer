$(function() {
  $('#buttonSum').click(function(){
    var a = $('#a').val();
    var b = $('#b').val();

    var data = {'a': a, 'b': b};

    $.ajax({
      method: 'POST',
      data: data,
      url: '/api/sum',
      success: function(data) {
        $('#result').text(data);
      }
    });
  });
});