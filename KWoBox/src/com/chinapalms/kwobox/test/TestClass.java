package com.chinapalms.kwobox.test;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.misc.BASE64Encoder;

import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.DoorClosedCallbackCustomization;
import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;
import com.chinapalms.kwobox.javabean.OrderCallbackCustomization;
import com.chinapalms.kwobox.javabean.ReplenishmentCallbackCustomization;
import com.chinapalms.kwobox.javabean.SerializeBoxBody;
import com.chinapalms.kwobox.pay.wxpay.RandomStringGenerator;
import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayService;
import com.chinapalms.kwobox.pay.wxpay.paybank.WxPayBankService;
import com.chinapalms.kwobox.pay.wxpay.paywx.WxPayWxService;
import com.chinapalms.kwobox.service.BoxGoodsXXXService;
import com.chinapalms.kwobox.service.BoxStructureService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.DoorClosedCallbackCustomizationService;
import com.chinapalms.kwobox.service.DoorOpenedCallbackCustomizationService;
import com.chinapalms.kwobox.service.ExceptionShoppingService;
import com.chinapalms.kwobox.service.OrderCallbackCustomizationService;
import com.chinapalms.kwobox.service.ReplenishmentCallbackCustomizationService;
import com.chinapalms.kwobox.service.SerializeBoxBodyService;
import com.chinapalms.kwobox.utils.HttpUtil;
import com.chinapalms.kwobox.utils.IdentifyCodeUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.chinapalms.kwobox.utils.UploadUtil;
import com.chinapalms.kwobox.utils.XmlXXEUtil;
import com.custom.kwobox.service.CustomWebSocketService;
import com.custom.kwobox.utils.CustomRequestSignatureUtil;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Single;

public class TestClass {
    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(TestClass.class);

    @Test
    public void TestConnection() throws Exception {
        String testString = "";
          System.out.println("testString:"+testString.trim().length());
//        JSONArray jsonArray = new JSONArray();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("test1", "test123");
//        jsonObject.put("test2", "test456");
//        List<JSONObject> aList = new ArrayList<>();
//        JSONObject jsonObject2 = new JSONObject();
//        jsonObject2.put("test2", jsonObject);
//        jsonArray.add(jsonObject);
//        System.out.println(jsonArray.getJSONObject(0).getString("test2"));
//        Map<String, Object> signParaMap = new HashMap<String, Object>();
//        signParaMap.put("mchId", "1531791574502");
//        signParaMap.put("nonceStr", "1234567890");
//        signParaMap.put("requestSerial", "1234567890");
//        signParaMap.put("timeStamp", "1234567890");
//        System.out.print(new CustomRequestSignatureUtil().getSign(signParaMap, "3S4N7SZMMJZT8UER4ZHUJ3PHD6RV1KIB"));
//        Map<String, Object> map = new HashMap<>();
//        map.put("key1", "aaa");
//        map.put("key2", null);
//        System.out.println(map.get("key1"));
//        System.out.println(map.get("key2"));
//        System.out.println("ddd="+String.valueOf(null));
        // new WxPapayService().doRefund("4200000181201809112753053906", 75, 2);
        // InetAddress address = InetAddress.getLocalHost();// 获取的是本地的IP地址
        // //PC-20140317PXKX/192.168.0.121
        // String hostAddress = address.getHostAddress();// 192.168.0.121
        // System.out.println("IP=" + hostAddress);
        // UploadUtil.upload();
        // JSONObject jsonObject = new JSONObject();
        // InetAddress address = InetAddress.getLocalHost();// 获取的是本地的IP地址
        // // //PC-20140317PXKX/192.168.0.121
        // String hostAddress = address.getHostAddress();// 192.168.0.121
        // System.out.println("IP=" + hostAddress);
        // "barCodeId": "6902538004045",
        // "goodsCategoryName": "脉动维生素饮料青柠口味",
        // "salesMode": 0,
        // "goodsPrice": "4.50",
        // "goodsActualPrice": "2.25",
        // "categoryGoodsNumber": 4,
        // "categoryGoodsWeight": 2572,
        // "categoryGoodsTotalPrice": "18.00",
        // "categoryGoodsTotalDiscount": "9.00",
        // "actualCategoryPrice": "9.00"
        // jsonObject.put("barCodeId1", "6902538004045");
        // jsonObject.put("barCodeId2", "6902538004045");
        // jsonObject.put("barCodeId3", "6902538004045");
        // jsonObject.put("barCodeId4", "6902538004045");
        // jsonObject.put("barCodeId5", "6902538004045");
        // jsonObject.put("barCodeId6", "6902538004045");
        // jsonObject.put("barCodeId7", "6902538004045");
        // jsonObject.put("barCodeId8", "6902538004045");
        // jsonObject.put("barCodeId9", "6902538004045");
        // jsonObject.put("barCodeId10", "6902538004045");
        // jsonObject.put("barCodeId11", "6902538004045");
        // jsonObject.put("barCodeId12", "6902538004045");
        // jsonObject.put("barCodeId13", "6902538004045");
        // jsonObject.put("barCodeId14", "6902538004045");
        // jsonObject.put("barCodeId15", "6902538004045");
        // jsonObject.put("barCodeId16", "6902538004045");
        // jsonObject.put("barCodeId17", "6902538004045");
        // jsonObject.put("barCodeId18", "6902538004045");
        // jsonObject.put("barCodeId19", "6902538004045");
        // jsonObject.put("barCodeId20", "6902538004045");
        // jsonObject.put("barCodeId21", "6902538004045");
        // jsonObject.put("barCodeId22", "6902538004045");
        // jsonObject.put("barCodeId23", "6902538004045");
        // jsonObject.put("barCodeId24", "6902538004045");
        // jsonObject.put("barCodeId25", "6902538004045");
        // jsonObject.put("barCodeId26", "6902538004045");
        // jsonObject.put("barCodeId27", "6902538004045");
        // jsonObject.put("barCodeId28", "6902538004045");
        // jsonObject.put("barCodeId29", "6902538004045");
        // jsonObject.put("barCodeId30", "6902538004045");
        // System.out.println(jsonObject);
        // while (true) {
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/KWoBoxServelet",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/SystemTest/index.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayDeleteContrustReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));

        // for (int i = 0; i < 60; i++) {
        // HttpUtil.sendCallbackResultMsg(
        // "https://www.zigoomo.com/KWoBox/wxPapayPaySuccesstReturn.jsp",
        // JSONObject.fromObject(jsonObject.toString()));
        // }
        // Thread.sleep(100);
        // }
        // IdentifyCodeUtil.sendSMS(98374, "18621716031", "uuu");

        // String xmlStr = "<xml><bank_type>CNBank</bank_type></xml>";
        // Document doc = XmlXXEUtil.getXXEDocument(xmlStr);
        // Element root = doc.getRootElement();
        // Node resultCode = root.selectSingleNode("bank_type");
        // System.out.println("resultCode=" + resultCode.getText().toString());

        // new WxPapayService().doRefund("4200000181201809112753053906", 75, 2);

        // JSONObject lingmaoAppsUpgradeJsonObject = new JSONObject();
        // JSONArray appsJsonArray = new JSONArray();
        //
        // JSONObject lingmaoInfoJsonObject = new JSONObject();
        // JSONObject lingmaoInfoJsonBeanObject = new JSONObject();
        // lingmaoInfoJsonBeanObject.put("apkUrl", "xxxUrl");
        // lingmaoInfoJsonBeanObject.put("appVersion", "xxxVersion");
        // lingmaoInfoJsonObject.put("lingmaoAppInfo",
        // lingmaoInfoJsonBeanObject);
        //
        // JSONObject lingmaoToolsInfoJsonObject = new JSONObject();
        // JSONObject lingmaoToolsInfoJsonBeanObject = new JSONObject();
        // lingmaoToolsInfoJsonBeanObject.put("apkUrl", "xxxUrl");
        // lingmaoToolsInfoJsonBeanObject.put("appVersion", "xxxVersion");
        // lingmaoToolsInfoJsonObject.put("lingmaoToolsAppInfo",
        // lingmaoToolsInfoJsonBeanObject);
        //
        // JSONObject lingmaoCoreJsonObject = new JSONObject();
        // JSONObject lingmaoCoreInfoJsonBeanObject = new JSONObject();
        // lingmaoCoreInfoJsonBeanObject.put("apkUrl", "xxxUrl");
        // lingmaoCoreInfoJsonBeanObject.put("appVersion", "xxxVersion");
        // lingmaoCoreJsonObject.put("lingmaoToolsAppInfo",
        // lingmaoCoreInfoJsonBeanObject);
        //
        // appsJsonArray.add(lingmaoInfoJsonObject);
        // appsJsonArray.add(lingmaoToolsInfoJsonObject);
        // appsJsonArray.add(lingmaoCoreJsonObject);
        //
        // lingmaoAppsUpgradeJsonObject.put("appsInfo", appsJsonArray);
        //
        // System.out.println(lingmaoAppsUpgradeJsonObject);

        // UploadUtil.upload();
        // public static boolean isOSLinux() {
        // Properties prop = System.getProperties();
        //
        // String os = prop.getProperty("os.name");
        // System.out.println("os=" + os);
        // if (os != null && os.toLowerCase().indexOf("linux") > -1) {
        // return true;
        // } else {
        // return false;
        // }
        // }
        // System.out.println(System.currentTimeMillis());
        // System.out.println(RandomStringGenerator.getRandomStringByLength(32));
        // JSONObject jsonObject = new JSONObject();
        // jsonObject.put("goods", "aa");
        // System.out.println("goods="+jsonObject.toString());
        // Map<String, Object> signParamMap = new HashMap<String, Object>();
        // signParamMap.put("requestSerial", "1234568796424646546647987513146");
        // signParamMap.put("reqtime", "20180727142552");
        // signParamMap.put("returntype", "2");
        // signParamMap.put("version", "1.0");
        // signParamMap.put("appid", "wisdcat");
        // signParamMap.put("systemid", "wisdcat101");
        // signParamMap.put("terminaltype", "wisdcat");
        // signParamMap.put("boxId",
        // "M03RH-AAAA-20180609051500-20180629171428");
        // signParamMap.put("phoneNumber", "18911750349");
        // String sign = CustomRequestSignatureUtil
        // .getSignWithoutKey(signParamMap);
        // System.out.println("sign="+sign);
        // System.out.println(System.currentTimeMillis());
        // CustomWebSocketService customWebSocketService = new
        // CustomWebSocketService();
        // customWebSocketService.notifyCustomServerDoorClosed("aaa",
        // "M03RH-AAAA-20180609051500-20180629171428", "18621716031");
        // System.out.println(System.currentTimeMillis());
        //
        // String aaaString = "PPPPUUUUU";
        // System.out.println("aaaString="+aaaString.split(",")[0]);
        //
        // InetAddress address = InetAddress.getLocalHost();
        // System.err.println("addressId="+address.getHostAddress());
        // JSONObject jsonObject = new JSONObject();
        // jsonObject.put("testKey", "testValue");
        // HttpUtil.sendCallbackResultMsg("http://localhost:8080/KWoBox/KWoBoxCustomServlet?action=test",
        // jsonObject);

        // ExceptionShoppingService.sendExceptionEmail("boxId");
        //
        // System.out.println(System.currentTimeMillis() / 1000);

        // WxPayBankService wxPayBankService = new WxPayBankService();
        // wxPayBankService.doWxPayBank("6217920181225538", "王义", "1004", 0);
        // wxPayBankService.doQueryPayBank("20180711113934004451527152170446");

        // WxPayWxService wxPayWxService = new WxPayWxService();
        // String key = wxPayWxService.doWxPayWx("o5slF47IbzxskyydYNGBVtDvsOuo",
        // 100);

        // WxPayWxService wxPayWxService = new WxPayWxService();
        // wxPayWxService.doQueryPayWx("20180712110634788571897289160449");
        // System.out.println("key=" + key);

        // String boxId = "T-M03RH1-A-201806202125-20180620212431";
        // String mchId = "1531791574501";
        // String phoneNumber = "18621716031";
        // String userType = "0";
        // String nonceStr = "1234567890";
        // String requestSerial = "9876543210";
        // String timeStamp = "9876543456";
        // // String sign = request.getParameter("sign");
        // Map<String, Object> signParaMap = new HashMap<String, Object>();
        // signParaMap.put("mchId", mchId);
        // signParaMap.put("nonceStr", nonceStr);
        // signParaMap.put("requestSerial", requestSerial);
        // signParaMap.put("timeStamp", timeStamp);
        // String signNew = CustomRequestSignatureUtil
        // .getSign(signParaMap, "A27G7S7YHR8ZULLAGMLU24HGT5YVVUZ1");
        // log.info("signNew=" + signNew);

        // String aString = "";
        //
        // // 获取售货机层数和每层货道数信息
        // BoxesService boxesService = new BoxesService();
        // SerializeBoxBodyService serializeBoxBodyService = new
        // SerializeBoxBodyService();
        // BoxStructureService boxStructureService = new BoxStructureService();
        // Boxes boxes = boxesService
        // .findBoxesByBoxId("T-M03RH-H");
        // System.out.println("boxes======================" + boxes);
        // SerializeBoxBody serializeBoxBody = serializeBoxBodyService
        // .findSerializeBoxBodyBySerializeBoxBodyId(boxes
        // .getSerializeBoxBodyId());
        // JSONObject currentBoxCardgoRoadAndLayerStructureInfoJsonObject =
        // JSONObject
        // .fromObject(boxStructureService
        // .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
        // .getStructureId()));
        //
        // int totalLayer = currentBoxCardgoRoadAndLayerStructureInfoJsonObject
        // .getInt("totalLayer");
        // int totalCardgoRoad =
        // currentBoxCardgoRoadAndLayerStructureInfoJsonObject
        // .getInt("totalCardgoRoad");
        // JSONArray layerCardgoRoadNumberJsonArray =
        // currentBoxCardgoRoadAndLayerStructureInfoJsonObject
        // .getJSONArray("layerCardgoRoadNumber");
        // BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        // List<BoxGoodsXXX> boxGoodsXXXsList = boxGoodsXXXService
        // .findAllBoxGoodsXXX("T-M03RH-H");
        // int x = 0;
        // int y = 0;
        // String bigPicUri = "D:\\big1.jpg";
        // String smallPicUriForSize = "D:\\small.jpg";
        // BufferedImage bigForSize = ImageIO.read(new File(bigPicUri));
        // BufferedImage smallForSize = ImageIO.read(new
        // File(smallPicUriForSize));
        // int smallWidth = 50;
        // int smallHeight = 30;
        // int paddingBottom = 5;
        // Graphics2D g = null;
        //
        // // BufferedImage big = new BufferedImage(bigForSize.getWidth(),
        // // bigForSize
        // // .getHeight(), BufferedImage.TYPE_INT_RGB);
        // BufferedImage big = ImageIO.read(new File(bigPicUri));
        // for (int i = 0; i < layerCardgoRoadNumberJsonArray.size(); i++) {
        // int layerForLoopStart = layerCardgoRoadNumberJsonArray
        // .getJSONObject(i).getInt("layerForLoopStart");
        // int layerCardgoRoadNumber = layerCardgoRoadNumberJsonArray
        // .getJSONObject(i).getInt("number");
        // int layerForLoopEnd = layerForLoopStart + layerCardgoRoadNumber;
        //
        // smallHeight = bigForSize.getHeight() / 2 / totalLayer + 5;
        // smallWidth = smallHeight - 30;
        // System.out
        // .println("bigForSize.getHeight=" + bigForSize.getHeight());
        // System.out.println("layerForLoopStart=" + layerForLoopStart
        // + ",layerForLoopEnd=" + layerForLoopEnd);
        // for (int j = 0; j < boxGoodsXXXsList.size(); j++) {
        // if (j >= layerForLoopStart && j < layerForLoopEnd) {
        // // System.out.println("i="+i+",j="+j);
        // BoxGoodsXXX boxGoodsXXX = boxGoodsXXXsList.get(j);
        // String goodsName = boxGoodsXXX.getGoodsName();
        // // String goodsPictureUrl =
        // // ResponseStatus.SERVER_URL+"/pictures/goods/6918976333388.jpg";
        //
        // String goodsPictureUrl = ResponseStatus.SERVER_APPCHA_URL
        // + boxGoodsXXX.getGoodsPicture().split(",")[0];
        // BigDecimal goodsActualPrice = (boxGoodsXXX.getGoodsPrice())
        // .multiply(boxGoodsXXX.getGoodsDiscount())
        // .subtract(boxGoodsXXX.getFavourable())
        // .setScale(2, BigDecimal.ROUND_UP);
        // String goodsActualPriceString = "￥" + goodsActualPrice;
        //
        // System.out.println("goodsPicture=" + goodsPictureUrl);
        // try {
        // // BufferedImage big = ImageIO.read(new
        // // File(bigPicUri));
        //
        // // BufferedImage big = new
        // // BufferedImage(bigForSize.getWidth(), bigForSize
        // // .getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        // // BufferedImage small = ImageIO.read(new File(
        // // goodsPicture));
        // URL url = new URL(goodsPictureUrl);
        // // 打开连接
        // URLConnection con = url.openConnection();
        // // 设置请求超时为5s
        // con.setConnectTimeout(20 * 1000);
        // // 输入流
        // InputStream is = con.getInputStream();
        //
        // BufferedImage small = ImageIO.read(is);
        //
        // g = big.createGraphics();
        //
        // g.setComposite(AlphaComposite.getInstance(
        // AlphaComposite.SRC_ATOP, 1.0f));// 1.0f为透明度
        // // ，值从0-1.0，依次变得不透明
        // // 防止图片模糊
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        // g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
        // RenderingHints.VALUE_STROKE_DEFAULT);
        // Stroke s = new BasicStroke(50, BasicStroke.CAP_ROUND,
        // BasicStroke.JOIN_MITER);
        // g.setStroke(s);
        //
        // g.setColor(Color.WHITE);
        // g.setStroke(new BasicStroke(1));
        // Font font = new Font("微软雅黑", Font.BOLD, 13);
        // g.setFont(font);
        // // 抗锯齿
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        // g.drawImage(
        // small,
        // x
        // + (big.getWidth()
        // / layerCardgoRoadNumber - smallWidth)
        // / 2,
        // y
        // + (big.getHeight() / totalLayer - smallHeight)
        // / 2 - 20, smallWidth, smallHeight, null);
        // System.out.println("draw count=" + j);
        //
        // // 计算文字长度，计算居中的x点坐标
        // FontMetrics fm = g.getFontMetrics(font);
        // int goodsNameTextWidth = fm.stringWidth(goodsName);
        // int goodsNameWidthX = (big.getWidth()
        // / layerCardgoRoadNumber - goodsNameTextWidth) / 2;
        // // 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容
        // g.drawString(goodsName, x + goodsNameWidthX, y
        // + bigForSize.getHeight() / totalLayer - 25);
        //
        // // 绘制商品价格
        // g.setColor(Color.RED);
        // Font fontPrice = new Font("微软雅黑", Font.BOLD, 18);
        // g.setFont(fontPrice);
        // FontMetrics fmPrice = g.getFontMetrics(fontPrice);
        // int goodsPriceTextWidth = fmPrice
        // .stringWidth(goodsActualPriceString);
        // int goodsPriceWidthX = (big.getWidth()
        // / layerCardgoRoadNumber - goodsPriceTextWidth) / 2;
        // g.drawString(goodsActualPriceString, x
        // + goodsPriceWidthX, y + bigForSize.getHeight()
        // / totalLayer - 5);
        // // g.dispose();
        // ImageIO.write(big, "jpg", new File("D:\\BigSmall.jpg"));
        // // Thread.sleep(1000);
        // bigPicUri = "D:\\BigSmall.jpg";
        // x = x + big.getWidth() / layerCardgoRoadNumber;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }
        // x = 0;
        // y = y + bigForSize.getHeight() / totalLayer - paddingBottom;
        // if (i == layerCardgoRoadNumberJsonArray.size() - 1) {
        // g.dispose();
        // }
        // }

        // try {
        // BufferedImage big = ImageIO.read(new File("D:\\big.jpg"));
        // BufferedImage small = ImageIO.read(new File("D:\\small.jpg"));
        // int x = (big.getWidth() - small.getWidth()) / 2;
        // int y = (big.getHeight() - small.getHeight()) / 2;
        // Graphics2D g = big.createGraphics();
        // g.setColor(Color.RED);
        // g.setStroke(new BasicStroke(5));
        // g.setFont(new Font("Serif", Font.PLAIN, 13));
        // g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
        // g.drawString("可口可乐", x, y);
        // g.drawImage(small, x + 200, y, small.getWidth(), small.getHeight(),
        // null);
        // g.drawString("冰红茶", x + 200, y);
        // g.dispose();
        // ImageIO.write(big, "jpg", new File("D:\\BigSmall.jpg"));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // System.out.println("IP="+IPUtils.getLocalIP());

        // InetAddress address = InetAddress.getLocalHost();// 获取的是本地的IP地址
        // // //PC-20140317PXKX/192.168.0.121
        // String hostAddress = address.getHostAddress();// 192.168.0.121
        // System.out.println("IP=" + hostAddress);
        // InetAddress address1 = InetAddress.getByName("www.zigoomo.com");
        //
        // System.out.println("hostAddress="+hostAddress+",address1="+address1);
        //
        //
        // InetAddress[] addresses =
        // InetAddress.getAllByName("www.zigoomo.com");//根据主机名返回其可能的所有InetAddress对象
        // for (InetAddress add: addresses) {
        // System.out.println("add="+add.getHostAddress());
        // }
        // System.out.println("total Test="+new BigDecimal(0).setScale(2,
        // BigDecimal.ROUND_DOWN));
        // try {
        // int a = 1/0;
        // } catch (Exception e1) {
        // // TODO Auto-generated catch block
        // log.error("业务名称",e1);
        // e1.printStackTrace();
        // }

        // BoxStructureService boxStructureService = new BoxStructureService();
        // String struct = boxStructureService
        // .getCardgoRoadAndLayerStructureInfo(15);
        // System.out.println("struct=" + struct);

        // BoxesService boxesService = new BoxesService();
        // String customerInfoString =
        // boxesService.doGetCustomerInfo("T-M03RH-E-2022-20180611142305");
        // System.out.println("customerInfoString="+customerInfoString);

        // String test = "a,b,c";
        // String [] array = test.split(",");
        // JSONArray jsonObject = JSONArray.fromObject(array);
        //
        //
        // System.out.println(jsonObject.toString());

        // List<Integer> testList = new ArrayList<Integer>();
        // testList.add(0);
        // testList.add(1);
        // testList.add(3);
        // testList.add(4);
        // JSONObject jsonObject = new JSONObject();
        // jsonObject.put("testList", testList);
        // System.out.println(jsonObject.toString()+",jsonObject[0]="+jsonObject.getJSONArray("testList").toString());

        // System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss")
        // .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // .parse("2018-09-08 12:34:50")));

        // try {
        // String aString = null;
        // aString.charAt(0);
        // } catch (Exception e) {
        // log.info("111:" + e);
        // log.info("222:" + e.getMessage());
        // e.printStackTrace();
        // }

        // String timeString = String.valueOf(System.currentTimeMillis());
        // System.out.println(timeString);
        // System.out.println(timeString.substring(timeString.length() - 7));
        // System.out.println(System.currentTimeMillis()/100000);

        // BoxStructureService boxStructureService = new BoxStructureService();
        // List<Integer> sensorList = new ArrayList<Integer>();
        // sensorList.add(141);
        // sensorList.add(142);
        // sensorList.add(143);
        // sensorList.add(143);
        // sensorList.add(144);
        // sensorList.add(144);
        // sensorList.add(241);
        // sensorList.add(242);
        // sensorList.add(243);
        // sensorList.add(243);
        // sensorList.add(244);
        // sensorList.add(245);
        // String listString =
        // boxStructureService.getSensorAndCardgoRoadStructure(sensorList);
        // System.out.println("listString="+listString);

        // TencentLocationService tencentLocationService = new
        // TencentLocationService();
        // String address = tencentLocationService.latLngToAddress("31.220182",
        // "121.62759");
        // System.out.println("address="+address);
        // JSONObject jsonObject = new JSONObject();
        // String action = "LingmaoToolsUpgrade";
        // String boxIdStr = "M03-000021";
        // String apkUrl =
        // "https://www.zigoomo.com/apks/LingmaoTools/LingmaoTools_1.1.3.apk";
        // String appVersion = "1.1.3";
        // DeviceMonitorService deviceMonitorService = new
        // DeviceMonitorService();
        // deviceMonitorService.doLingmaoToolsUpgrade(action, boxIdStr, apkUrl,
        // appVersion);

        // String formatLastSignedInDateIgnoreHourMinSecString = new
        // SimpleDateFormat(
        // "yyyy-MM-dd").format(new Date());
        // Date formatLastSignedInDateIgnoreHourMinSec = new SimpleDateFormat(
        // "yyyy-MM-dd HH:mm:ss")
        // .parse(formatLastSignedInDateIgnoreHourMinSecString + " 00:00:00");
        // System.out.println("formatLastSignedInDateIgnoreHourMinSec="+formatLastSignedInDateIgnoreHourMinSec);
        // long ld1 = formatLastSignedInDateIgnoreHourMinSec.getTime();
        // long ld2 = new Date().getTime();
        // long days = (long) ((ld2 - ld1) / 86400000);
        // System.out.println("days=" + days);
        // Date queryDate = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-05-29 15:24:59");
        // try {
        // Date currentDate = new Date();
        // SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        // System.out.println(fmt.format(queryDate));
        // System.out.println(fmt.format(currentDate));
        // System.out.println(fmt.format(queryDate).equals(
        // fmt.format(currentDate)));
        // } catch (Exception e) {
        // log.error("try->catch exception:", e);
        // e.printStackTrace();
        // }
        // Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // .parse("2018-05-28 00:00:00");
        // Date d2 = new Date();
        // SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        // // fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        // System.out.println(fmt.format(d1));
        // System.out.println(fmt.format(d2));
        // System.out.println(fmt.format(d1).equals(fmt.format(d2)));
        //
        // long ld1 = d1.getTime();
        // long ld2 = currentDate.getTime();
        // long days = (long) ((ld2 - ld1) / 86400000);
        // System.out.println("days=" + days);

        // try {
        // System.out.println("serializeBoxBodyService=");
        // SerializeBoxBodyService serializeBoxBodyService = new
        // SerializeBoxBodyService();
        // System.out.println("serializeBoxBodyService="+serializeBoxBodyService);
        // } catch (Exception e) {
        // System.out.println(e.toString());
        // e.printStackTrace();
        // }

        // try {
        // int a = 1/0;
        // } catch (Exception e) {
        // log.info("exception:="+Arrays.toString(e.getStackTrace()));
        // e.printStackTrace();
        // }

        // List<Double> input = new ArrayList<Double>();
        // input.add(2.3);
        // input.add(3.3);
        // input.add(4.3);
        // input.add(5.3);
        // input.add(6.3);
        // input.add(7.3);
        // input.add(8.3);
        // input.add(9.3);
        // input.add(1.3);
        // System.out.println(input);
        // System.out.println("最大值: " + Collections.max(input));
        // System.out.println("最小值: " + Collections.min(input)
        // +",索引："+input.indexOf(Collections.min(input)));

        // TencentLocationService tencentLocationService = new
        // TencentLocationService();
        // //String address = tencentLocationService.latLngToAddress("31.22395",
        // "121.63345");
        // String distance =
        // tencentLocationService.calculateDistance("31.22395", "121.63345",
        // "31.22594", "121.66337");
        // System.out.println("addressTest="+distance);

        // 腾讯位置服务http://lbs.qq.com/console/mykey.html
        // HttpGet httpGet = new HttpGet(
        // "http://apis.map.qq.com/ws/geocoder/v1/?address="
        // + URLEncoder.encode("北京市海淀区西大街74", "utf-8")
        // + "&key="
        // + URLEncoder.encode(
        // "JJCBZ-BVE6J-C53FT-KIW26-B5OEK-OFFOA", "utf-8"));
        // HttpGet httpGet = new HttpGet(
        // "http://apis.map.qq.com/ws/distance/v1/?mode=driving&from=39.983171,116.308479&to=39.996060,116.353455;39.949227,116.394310&key=JJCBZ-BVE6J-C53FT-KIW26-B5OEK-OFFOA");
        // 设置请求器的配置
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        // HttpResponse res = httpClient.execute(httpGet);
        // HttpEntity entity = res.getEntity();
        // String result = EntityUtils.toString(entity, "UTF-8");
        // JSONObject jsonObject = JSONObject.fromObject(result);
        // System.out.println(jsonObject.toString());

        // System.out.println(BigDecimal.valueOf(
        // Double.valueOf(Double.valueOf(289) / 1000))
        // .setScale(3, BigDecimal.ROUND_UP));
        // System.out.println(BigDecimal.valueOf(
        // Double.valueOf(Float.valueOf(289) / 1000)));
        // String url = "http://www.zigoomo.com/KWoBox/FaceDetectServelet";
        // Map<String, String> paramsMap = new HashMap<String, String>();
        // paramsMap.put("boxId", "Zigoomall00panZigoomall");
        // paramsMap.put("faceDetectPassword", "245246");
        // Map<String, File> filesMap = new HashMap<String, File>();
        // filesMap.put("faceImage", new File("D:/wangyi/18616825061.jpg"));
        // UploadUtil.post(url, paramsMap, filesMap);
        // System.out.println((int) (1.02f));
        // String jsonStr = "{\"shopException\": {\"boxId\":
        // \"M03-000003\",\"data\": [{
        // \"goods\": [],
        // \"pressductor\": 0
        // }, {
        // \"goods\": [{
        // \"goodsUid\": "6901285991240\",
        // \"position\": 380083,
        // \"weight\": 1
        // }],
        // \"pressductor\": 1
        // }, {
        // \"goods\": [{
        // \"goodsUid\": \"6901285991240\",
        // \"position\": 380104,
        // \"weight\": 2
        // }, {
        // \"goodsUid\": \"6901285991240\",
        // \"position\": 380104,
        // \"weight\": 2
        // }],
        // \"pressductor\": 2
        // }, {
        // \"goods\": [],
        // \"pressductor\": 3
        // }, {
        // \"goods\": [],
        // \"pressductor\": 4
        // }, {
        // \"goods\": [{
        // \"goodsUid\": \"6901285991240\",
        // \"position\": 2,
        // \"weight\": 380104
        // }, {
        // \"goodsUid\": \"unknow\",
        // \"position\": 41168,
        // \"weight\": 2
        // }],
        // \"pressductor\": 5
        // }, {
        // \"goods\": [],
        // \"pressductor\": 6
        // }, {
        // \"goods\": [],
        // \"pressductor\": 7
        // }, {
        // \"goods\": [],
        // \"pressductor\": 8
        // }, {
        // \"goods\": [],
        // \"pressductor\": 9
        // }, {
        // \"goods\": [],
        // \"pressductor\": 10
        // }, {
        // \"goods\": [],
        // \"pressductor\": 11
        // }, {
        // \"goods\": [],
        // \"pressductor\": 12
        // }, {
        // \"goods\": [],
        // \"pressductor\": 13
        // }, {
        // \"goods\": [],
        // \"pressductor\": 14
        // }, {
        // \"goods\": [],
        // \"pressductor\": 15
        // }, {
        // \"goods\": [],
        // \"pressductor\": 16
        // }, {
        // \"goods\": [],
        // \"pressductor\": 17
        // }]
        // }
        // }";
        //
        //
        // String jsonStr =
        // "{\"data\":[{\"pressductor\":\"1\",\"goods\":[{\"barcode\":\"6920202888883\",\"count\":2},{\"barcode\":\"6918976333388\",\"count\":3}]},{\"pressductor\":\"2\",\"goods\":[{\"barcode\":\"6902538005141\",\"count\":1},{\"barcode\":\"6901939661604\",\"count\":6}]},{\"pressductor\":\"3\",\"goods\":[{\"barcode\":\"6902538005141\",\"count\":1},{\"barcode\":\"6901939661604\",\"count\":6}]}],\"url1\":\"https://www.zigoomo.com/videos/1.mp4\",\"url2\":\"https://www.zigoomo.com/videos/1.mp4\"}";
        // IdentifyCodeUtil
        // .sendTencentQQIdentifyCodeSms("18621716031", "1234", 10);
        // // 用于提交数据的client
        // HttpClient client = new DefaultHttpClient();
        // // 这是提交的服务端地址
        // String url = "http://192.168.1.101:8080/FaceServer/JWOKServlet";
        // // 采用的是Post方式进行数据的提交
        // HttpPost post = new HttpPost(url);
        // List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        //
        // // 这里就是提交的数据,你在服务端就可以通过request.getParameter("字段名称")
        //
        // paramsList.add(new BasicNameValuePair("id", "123"));
        //
        // paramsList.add(new BasicNameValuePair("name", "wangyi"));
        //
        // post.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
        // HttpResponse response = client.execute(post);
        //
        // if (response.getStatusLine().getStatusCode() == 200) {
        // ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // InputStream in = response.getEntity().getContent();
        // byte[] data = new byte[4096];
        // int count = -1;
        // while ((count = in.read(data, 0, 4096)) != -1)
        // outStream.write(data, 0, count);
        // data = null;
        // // 这是服务端返回的数据
        // String content = new String(outStream.toByteArray(), "utf-8");
        // }
    }
}
