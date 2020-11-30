$(function() {
  $('#buttonSum').click(function(){
    var a = $('#a').val();
    var b = $('#b').val();

    var data = {'par1': a, 'par2': b};

    $.ajax({
      method: 'POST',
      data: data,
      url: '/ajax/user/login/phone',
      success: function(data) {
        $('#result').text(data);
      }
    });
  });
});