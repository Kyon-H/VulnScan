var scans_reg =new RegExp(".*/ActiveScan/scans.*");
var vulnInfo_reg=new RegExp(".*/ActiveScan/vulnerabilities.*");
var report_reg=new RegExp(".*/ActiveScan/reports.*");
var detail_reg=new RegExp(".*/ActiveScan/detail.*");

console.log("navbar.js")
if(scans_reg.test(window.location.href)) {
    $('#home').parent().removeClass('active');
    $('#scans').parent().addClass('active');
}else if(vulnInfo_reg.test(window.location.href)){
    $('#home').parent().removeClass('active');
    $('#vulnerabilities').parent().addClass('active');
}else if(report_reg.test(window.location.href)){
    $('#home').parent().removeClass('active');
    $('#reports').parent().addClass('active');
}else if(detail_reg.test(window.location.href)){
    $('#home').parent().removeClass('active');
}