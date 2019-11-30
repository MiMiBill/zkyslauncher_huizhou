var api = "http://zk-hugong-prod.battcn.com";
function getApi() {
    var protocol = window.location.protocol;
    if (protocol === 'http:' || protocol === 'https:') {
        return window.location.protocol + "//" + window.location.host;
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
    return (false);
}
function findDetail(id,familyCount,praiseRate) {
    var hospId = getQueryVariable("hospId");
    var deptId = getQueryVariable("deptId");
    var deptName = getQueryVariable("deptName");
    var hospName = getQueryVariable("hospName");
    var imei = getQueryVariable("imei");
    var bedNo = getQueryVariable("bedNo");
    console.log()
    var timestamp = new Date().getTime();
    window.location.href = "detile.html?userId=" + id + "&hospId=" + hospId + "&deptId=" + deptId + "&deptName=" + deptName + "&hospName=" 
    + hospName + "&imei=" + imei + "&bedNo=" + bedNo+"&familyCount="+familyCount+"&praiseRate="+praiseRate+"&time="+timestamp;
}

$(function () {
    api = getApi();

    var b = 0;
    var orderByAsc = "asc";
    $(".sort p").on('click', function () {
        b++;
        var div = document.getElementById('sortIcon');
        if (b % 2 == 0) {
            console.log('降序' + b);
            orderByAsc = "asc";
        
            div.setAttribute("class", "fa fa-chevron-up");
        }else{
            orderByAsc = "desc";
            div.setAttribute("class", "fa fa-chevron-down");
        }
        ajax();
    })

    // deptName=aaa&hospName=assd&imei=111&bedNo=12
    var hospId = getQueryVariable("hospId");
    var deptId = getQueryVariable("deptId");
    var deptName = getQueryVariable("deptName");
    var hospName = getQueryVariable("hospName");
    var imei = getQueryVariable("imei");
    var bedNo = getQueryVariable("bedNo");

    var timestamp = new Date().getTime();
    
    function ajax() {
        // 护工列表
        $.ajax({
            url: api + "/baseinfo/users/nursing_workers/" + hospId + "/" + deptId + "?time=" + timestamp,
            type: 'get',
            data: {
                "orderByAsc": orderByAsc
            },
            success: function (res) {
                var data = res.data.list;
                console.log(data);

                if (data != null && data.length > 0) {
                    for (var i = 0; i < data[i].nursingExperience; i++) {
                        nursingExperience = res.data[i].nursingExperience.tagName;
                        console.log(nursingExperience);
                    }
            
                    $("#list-unstyled").html(template('temp', {"data":data }));
                }
            }
        })
    }
    ajax();

})