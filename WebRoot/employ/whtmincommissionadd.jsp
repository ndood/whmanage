<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<jsp:useBean id="userSession" scope="session" class="com.wlt.webm.rights.form.SysUserForm" />
<jsp:useBean id="prod" scope="page" class="com.wlt.webm.business.bean.BiProd"/>
 <%
	 List areaList = prod.getAreaList();
	 StringBuffer sBuffer1 = new StringBuffer();
	 sBuffer1.append("��ѡ��[]");
	 for(Object tmp : areaList){
		String[] temp = (String[])tmp;
		sBuffer1.append("|"+temp[1]+"["+temp[0]+"]");
	 }
	 request.setAttribute("areaSel", sBuffer1.toString());
	 
	 List valueList = prod.getValueList();
	 StringBuffer sBuffer2 = new StringBuffer();
	 sBuffer1.append("��ѡ��[]");
	 for(Object tmp : valueList){
		String[] temp = (String[])tmp;
		sBuffer2.append("|"+temp[0]+"["+temp[1]+"]");
	 }
	 request.setAttribute("valueSel", sBuffer2.toString());
 %>
 <html>
<head>
<title>���ͨ</title>
<script type="text/javascript" src="../js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="../js/xml_cascade.js"></script>
<link href="../css/main_style.css" rel="stylesheet" type="text/css" />
<link href="../css/selectCss.css" rel="stylesheet" type="text/css" /> 
<script src="../js/selectCss.js" type="text/javascript"></script> 
<script language="javascript" type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<SCRIPT type=text/javascript>
jQuery(function(){
	$("#changeCity").click(function(a){$("#allCity").slideDown(0);a.stopPropagation();$(this).blur()});$("#allCity").click(function(a){a.stopPropagation()});$(document).click(function(a){a.button!=2&&$("#allCity").slideUp(0)});$("#foldin").click(function(){$("#allCity").slideUp(0)})
	if($("#mess").val() != ""){
		alert($("#mess").val());
	}
});
function check(){
  	with(document.forms[0]){
  		if(cm_area.value==""){
	    alert("��ѡ��ʡ�� !");
	     return false
	    }
	    if(cm_type.value==""){
	    alert("��ѡ����Ӫ������ !");
	     return false
	    }
	    if(cm_value.value==""){
	    alert("��ѡ��������� !");
	     return false
	    }
		var val=cm_prod.value;
		var val1=cm_prod1.value;
		if(val == ""){
		alert("�����뽻�׽�� !");
		return false;
		}
		if(val1 == ""){
		alert("�����뷵Ӷ��� !");
		return false;
		}
     	if(!/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(val)){
	        alert("��������ȷ������");
	        return false;
            }

	    if(confirm("ȷ����˷���?")){
	    document.getElementById("checkCode").value="���Ժ�...";
		document.getElementById("checkCode").disabled="disabled";
		target="_self";
		submit();
		}else{
		return false;
		}
	}
}
</SCRIPT>
</head>
<body>
<div id="popdiv"><span id="sellist"></span><div style="clear:both"></div></div>
<input type="hidden" id="mess" value="${requestScope.mess }"/>
<html:form  method="post" action="/business/prod.do?method=addminEmploy">
<input name="emtype" value="0" type="hidden">
<div class="pass_main">
	<div class="pass_title_1">����С��Ӷ��</div>
    <div class="pass_list">
    	<ul>
            <li><strong>ʡ�� ��</strong><span>
            <div class="sub-input"><a class="sia-2 selhover" id="sl-cm_area" href="javascript:void(0);">��ѡ��</a></div>
			<input name="cm_area" id="cm_area" defaultsel="${requestScope.areaSel }" type="hidden" value="" />
			</span></li>
            <li><strong>ҵ�����ͣ�</strong><span>
            <div class="sub-input"><a class="sia-2 selhover" id="sl-cm_type" href="javascript:void(0);">��ѡ��</a></div>
			<input name="cm_type" id="cm_type" defaultsel="��ѡ��[]|�ƶ�[1]|��ͨ[2]|����[0]" type="hidden" value="" />
			</span></li>
            <li><strong>������� ��</strong><span>
            <div class="sub-input"><a class="sia-2 selhover" id="sl-cm_value" href="javascript:void(0);">��ѡ��</a></div>
			<input name="cm_value" id="cm_value" defaultsel="${requestScope.valueSel }" type="hidden" value="" />
			</span></li>

           <li><strong>���׽�</strong><span><input name="cm_prod" type="text" /><font color="red" size="3"></font></span></li>
<li><strong>��Ӷ��</strong><span><input name="cm_prod1" type="text" /><font color="red" size="3"></font></span></li>
        </ul>
   </div>
   <div class="field-item_button_m">
   <input type="button" value="ȷ��" id="checkCode" onclick="check()" class="field-item_button" />
   <input type="button" value="ȡ��" id="checkCode1" onClick="javascript:history.go(-1)" class="field-item_button" />
   </div>
</div>
</html:form>
</body>
</html>