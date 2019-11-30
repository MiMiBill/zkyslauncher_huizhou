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
    return (false);
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

    var btns = document.getElementsByTagName('i');
    for (var i = 0; i < btns.length; i++) {
        //给每个button加点击事件
        btns[i].onclick = function () {

            //通过循环可以排除掉其他（包括自己）
            for (var j = 0; j < btns.length; j++) {
                //排除其他
                btns[j].className = '';
            }
            //遍历后可以用this改变自己的样式
            this.className = 'skyblue';
        }
    };

 
    $.ajax({
        url: api + '/baseinfo/dictionaries/code/nursing_experience',
        type: 'get',        
        success: function(res) {
            var data = res.data
            console.log(data);
           
            $("#select").html(template('temp', { "data":data }));            
        }
    });

   
    $('#make').on('click',function() {
        var data = {
            nursingType:$.trim($('[name="nursingType"]').val()),
            mobilePhone:$.trim($('[name="mobilePhone"]').val()),
            patientRemark:$('[name="remark"]').val(),
            userId:userId,
            hospId:hospId,
            deptId:deptId,
            deptName:deptName,
            hospName:hospName,
            imei:imei,
            bedNo:bedNo
        }

        var phone = document.getElementById("phone").value;
        var reg = /^1(3|4|5|6|7|8|9)\d{9}$/.test(phone);
        if(!data.mobilePhone || !reg) {
            alert('请输入正确的联系方式');
            
        }else{
            var timestamp = new Date().getTime()
            $.ajax({
                url: api + "/order/user_schedules?time="+timestamp,
                type:'POST',
                dataType:'json',
                contentType:'application/json;charset=UTF-8',
                data:JSON.stringify(data),
                success: function (res) {
                    console.log(res.messageId);
                    
                    if(res.messageId == 200) {
                        alert('预约成功！')
                        window.location.href ="index.html?userId="+userId+"&hospId=" + hospId + "&deptId=" + deptId + "&deptName=" 
                        + deptName + "&hospName=" + hospName + "&imei=" + imei + "&bedNo=" + bedNo+"&time="+timestamp;
                    }
                }
            });
        }        
       
    })
})