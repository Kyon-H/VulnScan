var scans_reg =new RegExp(".*/ActiveScan/scans.*");
var vulnInfo_reg=new RegExp(".*/ActiveScan/vulnerabilities.*");

if(scans_reg.test(window.location.href)) {
    $('#home').parent().removeClass('active');
    $('#scans').parent().addClass('active');
}else if(vulnInfo_reg.test(window.location.href)){
    $('#home').parent().removeClass('active');
    $('#vulnerabilities').parent().addClass('active');
}