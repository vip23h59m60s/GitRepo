<%@page import="com.chinapalms.kwobox.utils.XmlXXEUtil"%>
<%@page import="com.chinapalms.kwobox.javabean.Order"%>
<%@page import="com.chinapalms.kwobox.pay.wxpay.OrderInfo"%>
<%@page import="com.chinapalms.kwobox.service.OrderService"%>
<%@page import="org.dom4j.Node"%>
<%@page import="org.dom4j.Element"%>
<%@page import="org.dom4j.DocumentHelper"%>
<%@page import="org.dom4j.Document"%>
<%@page import="com.chinapalms.kwobox.pay.wxpay.papay.WxPaypaySignature"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@page import="javax.xml.bind.Marshaller"%>
<%@page import="javax.xml.bind.JAXBContext"%>
<%@page import="com.chinapalms.kwobox.pay.wxpay.papay.WxPapayService"%>
<%@page import="com.thoughtworks.xstream.XStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'wxPapayEntrustReturn.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
    <%
        Log log = LogFactory.getLog(this.getClass());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.info("wxPapayPaySuccesstReturn.jsp sb.xml=" + sb.toString());
            //Xml解析防止XXE实体注入策略合入
            //Document doc = DocumentHelper.parseText(sb.toString());
            Document doc = XmlXXEUtil.getXXEDocument(sb.toString());
            Element root = doc.getRootElement();
            Node resultCode = root.selectSingleNode("result_code");
            //获取out_trade_no(商户侧支付I)和transaction_id(微信侧支付ID)
            Node outTradeNo = root.selectSingleNode("out_trade_no");
            Node transactionId = root.selectSingleNode("transaction_id");

            if (resultCode != null
                    && resultCode.getText().equals("SUCCESS")) {

                //支付成功，通过notify_url中传入的订单号参数来把未支付用户的账单置为未支付状态
                //注意这里的订单号参数可能混合有appid，比如：xxxxxxxappid=xxxx
                Node orderIdTemp = root.selectSingleNode("orderId");
                if (orderIdTemp != null) {
                    if (orderIdTemp.getText().length() > 20) {
                        String orderIdAc = orderIdTemp.getText().substring(
                                0, 20);
                        OrderService orderService = new OrderService();
                        Order order = new Order();
                        order.setOrderId(orderIdAc);
                        order.setPayState(1);
                        if (outTradeNo != null
                                && outTradeNo.getText().length() > 0) {
                            order.setOutTradeNo(outTradeNo.getText());
                        } else {
                            order.setOutTradeNo("0");
                        }
                        if (transactionId != null
                                && transactionId.getText().length() > 0) {
                            order.setTransactionId(transactionId.getText());
                        } else {
                            order.setTransactionId("0");
                        }
                        order.setPayDate(new Date());
                        //更新订单支付时间商户订单支付ID和微信侧订单支付ID
                        orderService.updateOrderInfo(order);
                        //支付成功，告诉微信服务器
                        String resXml = "<xml>"
                                + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>"
                                + "</xml>";
                        //妈蛋，这段代码至关重要，不然微信会回调多次，并且千万不能用response.getWriter.write(resXml),因为无效;
                        BufferedOutputStream bos = new BufferedOutputStream(
                                response.getOutputStream());
                        bos.write(resXml.getBytes());
                        bos.flush();
                        bos.close();
                        bos = null;
                    }
                }
            } else {
                //支付失败，通过notify_url中传入的订单号参数来把未支付用户的账单置为未支付状态
                //注意这里的订单号参数可能混合有appid，比如：xxxxxxxappid=xxxx
                Node orderIdTemp = root.selectSingleNode("orderId");
                if (orderIdTemp != null) {
                    if (orderIdTemp.getText().length() > 20) {
                        String orderIdAc = orderIdTemp.getText().substring(
                                0, 20);

                        //把订单状态设置为未支付
                        OrderService orderService = new OrderService();
                        Order order = new Order();
                        order.setOrderId(orderIdAc);
                        order.setPayState(0);
                        if (outTradeNo != null
                                && outTradeNo.getText().length() > 0) {
                            order.setOutTradeNo(outTradeNo.getText());
                        } else {
                            order.setOutTradeNo("0");
                        }
                        if (transactionId != null
                                && transactionId.getText().length() > 0) {
                            order.setTransactionId(transactionId.getText());
                        } else {
                            order.setTransactionId("0");
                        }
                        order.setPayDate(null);
                        //更新订单支付时间商户订单支付ID和微信侧订单支付ID
                        orderService.updateOrderInfo(order);

                        //支付失败以后，也同样告诉告诉微信服务器已成功的标志，避免微信服务器重复推送消息
                        String resXml = "<xml>"
                                + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>"
                                + "</xml>";
                        //妈蛋，这段代码至关重要，不然微信会回调多次，并且千万不能用response.getWriter.write(resXml),因为无效;
                        BufferedOutputStream bos = new BufferedOutputStream(
                                response.getOutputStream());
                        bos.write(resXml.getBytes());
                        bos.flush();
                        bos.close();
                        bos = null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                br = null;
            }
        }
    %>
</body>
</html>
