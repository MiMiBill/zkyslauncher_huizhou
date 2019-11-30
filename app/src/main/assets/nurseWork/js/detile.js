var api = "http://zk-hugong-prod.battcn.com";
function getApi(){
    var protocol = window.location.protocol;
    if(protocol === 'http:'|| protocol === 'https:'){
        return window.location.protocol +"//"+window.location.host;
    }
    return api;
}
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) { return pair[1]; }
    }
    return "";
}


$(function () {

    api = getApi();

    var userId = getQueryVariable("userId");
    var hospId = getQueryVariable("hospId");
    var deptId = getQueryVariable("deptId");
    var deptName = getQueryVariable("deptName");
    var hospName = getQueryVariable("hospName");
    var imei = getQueryVariable("imei");
    var bedNo = getQueryVariable("bedNo");
    var familyCount = getQueryVariable("familyCount");
    var praiseRate = getQueryVariable("praiseRate");
    var timestamp = new Date().getTime();

    $('#make').on('click',function(){
        console.log($(this).text());
        console.log(deptName);
        
        window.location.href ="appointment.html?userId="+userId+"&hospId=" + hospId + "&deptId=" + deptId + "&deptName=" 
        + deptName + "&hospName=" + hospName + "&imei=" + imei + "&bedNo=" + bedNo+"&time="+timestamp;
    });

    $.ajax({
        url: api+"/baseinfo/users/nursing_workers/"+userId+"?time="+timestamp,
        type: "get",
        success: function(res) {
            var data =  res.data;
            data.praiseRate=praiseRate;
            data.familyCount=familyCount;
            // console.log(data);
            
            $(".type_area").html(template('temp', {"data":data}));
        }
    })


})