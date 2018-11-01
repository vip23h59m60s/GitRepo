package com.chinapalms.kwobox.utils;

public class ResponseStatus {

    public final static String SERVER_URL = "https://www.zigoomo.com";
    public final static String SERVER_IP = "119.23.246.223";
    public final static String WEBSOCKET_SERVER_IP_AND_PORT = "119.23.246.223:8080";
    public final static String SERVER_APPCHA_URL = "http://www.zigoomo.com:81";
    public final static String QRCODE_CONTENT_PREFIX = "https://www.zigoomo.com/H5Zigomall/indexindex.html?boxId=";
    public final static String BOX_PICTURE_URL = "pictures/boxes";
    public final static String SUCCESS = "success";
    public final static String FAIL = "fail";
    public final static String ISNULL = "isnull";
    public final static String ISEXIST = "isexist";
    public final static String ISTIMEOUT = "istimeout";

    public final static String CUSTOM_CATEGORY_ANDROID = "Android";
    public final static String CUSTOM_CATEGORY_WX = "WeChat";
    public final static String CUSTOM_CATEGORY_ALIPAY = "Alipay";
    public final static String CUSTOM_CATEGORY_EXCEPTION = "ExceptionCustom";

    public final static String OPEN_DOOR = "open";
    public final static String CLOSE_DOOR = "close";

    public final static String DOOR_OPENED = "opened";
    public final static String DOOR_CLOSED = "closed";
    public final static String BOX_EXCEPTION = "exception";

    public final static String BOX_BUSY = "busy";
    public final static String BOX_ORDER_BUSY = "boxBusy";// 当前售货机还有异常视频未处理完毕时，此时不允许理货员开门,并通知给理货员
    public final static String BOX_BROKEN_DOWN = "brokenDown";
    public final static int BOX_STATUS_NORMAL = 0;
    public final static int BOX_STATUS_BUSY = 1;
    public final static int BOX_STATUS_BROKEN_DOWN = 2;
    public final static int BOX_STATUS_WAITING_UPDATE_BOXGOODS = 3;
    // 未找到对应商户的售货机
    public final static String CUSTOMER_BOX_NOT_FOUND = "boxNotFound";

    // 柜子商品结构已变化
    public final static String BOX_UPDATED = "boxUpdated";
    public final static String BOX_NO_NEED_SORT_GOODS = "noNeedSortGoods";
    public final static String BOX_MANAGER_SORT_GOODS = "sortGoods";
    public final static String BOX_MANAGER_ADD_GOODS = "addGoods";

    public final static String BOX_OPEN_NO_PERMISSION = "noOpenPermission";

    public final static String NO_ORDERS = "noorders";

    public final static String NO_PAYED_ORDER = "noPayedOrders";

    public final static String NO_BOXES = "noboxes";

    public final static String LAST_PAGE = "lastpage";

    public final static String BOXID_SPLIT = "Zigoomall";

    public final static String PERMISSION_NORMAL_USER = "normaluser";

    public final static String PERMISSION_MANAGER = "manager";

    public final static int BOX_TYPE_HIGH_FREQUENCY_RFID = 0;

    public final static int BOX_TYPE_SUPER_HIGH_FREQUENCY_RFID = 1;

    public final static int BOX_TYPE_WEIGHT = 2;

    public final static String BOX_TYPE_HIGH_FREQUENCY_RFID_DESCRIPTION = "标签型零售店";

    public final static String BOX_TYPE_SUPER_HIGH_FREQUENCY_RFID_DESCRIPTION = "标签型零售店";

    public final static String BOX_TYPE_WEIGHT_DESCRIPTION = "免标签AI智能型零售店";

    public final static String NO_CONTRACT_ID = "nocontractid";

    public final static String NO_OPEN_PAPAY = "noOpenPapay";

    public final static String FACE_MATCH_ERROR = "faceMatchError";
    public final static String FACE_FUNCTION_NOT_ALLOW = "faceFunctionNotAllow";

    public final static String GOODS_UNIT_NONE = "";

    public final static String GOODS_UNIT_ML = "ml";

    public final static String GOODS_UNIT_G = "g";

    public final static int IDENTIFY_CODE_TIMEOUT_MINUTES = 10;

    public final static int VIP_LEVEL_SVIP = 0;
    public final static int VIP_LEVEL_NORMAL = 1;

    // 个人信用评分基础值
    public final static int PERSONAL_CREDIT_BASE_VALUE = 100;
    public final static int PERSONAL_CREDIT_DEDUCT = 0;
    public final static int PERSONAL_CREDIT_ADD = 1;
    public final static int PERSONAL_CREDIT_DEDUCT_VALUE = -10;
    // 投放异物
    public final static int PERSONAL_CREDIT_DEDUCT_PUT_OTHERTHING_VALUE = -50;
    public final static int PERSONAL_CREDIT_ADD_VALUE = 1;
    public final static String PERSONAL_CREDIT_NO_CREDIT_UPDATE_RECORD = "无评估记录";
    public final static String NO_PERSONAL_CREDIT_RECORD = "noCreditRecord";
    // 个人信用低于60分禁止购物
    public final static int PERSONAL_CREDIT_FORBID_SHOPPING_VALUE = 60;
    public final static String PERSONAL_CREDIT_TOO_LOW = "lowCredit";

    // 用户积分相关
    public final static String NO_POINTS_RECORD = "noPointsRecord";
    // 0 表示积分减少，1表示积分增加
    public final static int POINTS_ADD_REASON_NORMAL_SHOPPING = 1;
    // 每日签到获取的积分
    public final static int POINTS_ADD_REASON_FROM_SIGN_IN = 2;

    // 通知Android主控更新售货机货道商品信息
    public final static String UPDATE_CURRENT_BOXGOODS = "updateCurrentBoxGoods";
    // 通知Android主控更新售货机货道商品价格
    public final static String UPDATE_CURRENT_BOXGOODS_PRICE = "updateCurrentBoxGoodsPrice";

    // 通知Android主控更新货道商品库存
    public final static String UPDATE_CARDGOROAD_GOODS_STOCKNUMBER = "updateCardgoRoadGoodsStockNumber";

    // 商品售卖方式：0：标准商品，按件售卖；1：非标准商品，按重量售卖
    public final static int SALES_MODE_NUMBER = 0;
    public final static int SALES_MODE_WEIGHT = 1;

    // android主控上传Log类型appLog rawDataLog
    public final static String ANDROID_APP_LOG = "appLog";
    public final static String ANDROID_RAWDATA_LOG = "rawDataLog";
    public final static String ANDROID_CALIBRATION_LOG = "calibrationLog";
    public final static String ANDROID_SYSTEM_EXCEPTION_LOG = "systemException";

    // 搜索附近售货柜的默认范围值:3000m
    public final static Double NEARBY_BOXES_NEAYBYRANGE = 2000d;

    // 地图上离我最近的售货柜的提示
    public final static String NEAREST_BOX_ON_MAP_TIP = "离我最近";

    // 动态密码超时时间
    public final static int DYNAMICPASSWORD_TIMEOUT_MINUTES = 10;

    public final static String HAD_TIMOUT = "timeout";

    // 动态密码错误
    public final static String DYNAMICPASSWORD_ERROR = "dynamicPasswordError";

    // 柜体初始化默认商品barCodeId :加多宝
    public final static String DEFAULT_BOX_GOODS_BARCODEID = "4891599338393";

    // 售货机运营状态1：正常运营；2：已撤柜
    public final static int BOX_OPERATION_STATE_NORMAL = 1;
    public final static int BOX_OPERATION_STATE_CLOSED = 0;

    // 异常购物处理状态0:未处理；1：处理中；2：已处理
    public final static int EXCEPTION_HANDLE_STATE_NO_HANDLE = 0;
    public final static int EXCEPTION_HANDLE_STATE_HANDLING = 1;
    public final static int EXCEPTION_HANDLE_STATE_HANDLED = 2;

    // 与商户的商务合作模式
    public final static int COOPERATIONMODE_SELF = 0;// 智狗猫自营
    public final static int COOPERATIONMODE_AGENT = 1;// 代理经营，例如Yoho
    public final static int COOPERATIONMODE_THIRD_INTERFACE = 2;// 与商户服务器对接模式，例如小e
    public final static String COOPERATIONMODE_THIRD_NULL_CALLBACK_URL = "nullCallbackUrl";

    // 售货机部署状态
    public final static int BOX_STATE_FINISHED_REGISTER = 1;
    public final static int BOX_STATE_CANCELED = 0;
    public final static int BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT = 2;

    // 门店免密模式超时时间(分钟)(一周)
    public final static int SHOP_FACE_DETECT_TIMEOUT_MINUTES = 24 * 7 * 60;

    // 智购猫层架状态
    public final static int WEIGHT_SENSOR_STATUS_OK = 1;// 从异常中恢复
    public final static int WEIGHT_SENSOR_STATUS_ERROR = 0;// 层架异常

    // 当前worker为普通商户员工还是智购猫超级理货员
    public final static int WORKER_PERMISSION_SUPER = 1;// 智购猫超级理货员
    public final static int WORKER_PERMISSION_NORMAL = 0;// 普通商户员工

    // 购物信息分页查询信息
    public final static String NO_SHOPPING_INFO = "noShoppingInfo";
    public final static String DO_SHOPPING_INFO_NOBODY = "nobody";// 无人处理购物信息
    public final static int UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_CODE_444 = 444;
    public final static String UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_MSG_444 = "服务器更新购物信息失败,请重试";
    public final static int UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_CODE_445 = 445;
    public final static String UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_MSG_445 = "该信息已被其他用户处理中";

    // 获取购物信息相关视频状态标志
    public final static String SHOPPING_INFO_VIDEO_IS_DELETED = "videoIsDeleted";
    public final static int SHOPPING_INFO_VIDEO_IS_NOT_EXIST_CODE_444 = 444;
    public final static String SHOPPING_INFO_VIDEO_IS_NOT_EXIST_MSG_444 = "该视频尚不存在，正在为您加载，请稍后重试";
    public final static int SHOPPING_INFO_VIDEO_IS_DELETED_CODE_445 = 445;
    public final static String SHOPPING_INFO_VIDEO_IS_DELETED_MSG_445 = "该视频因时间过长已被系统删除";

    // 工控机发送的订单类型
    public final static String ORDER_TYPE_NORMAL = "normalOrder";// 正常订单（正常盘点的明确订单）
    public final static String ORDER_TYPE_EXCEPTION = "exceptionOrder";// 异常订单（未能正常盘点已经报了视频的异常订单）
    public final static String ORDER_TYPE_DELAY = "delayOrder"; // 视频端人工处理订单

    // 开门请求平台通用requestSerialNumber
    public final static String OPEN_DOOR_REQUEST_SERIALNUMBER_NORMAL = "1234567890";

    // 一组异常视频处理完成后库存同步标志
    public final static int SYNC_STOCKINFO_FAIL_EXIST_UNHANDLE_EXCEPTION_CODE_444 = 444;
    public final static String SYNC_STOCKINFO_FAIL_EXIST_UNHANDLE_EXCEPTION_MSG_444 = "还有未处理异常，请处理完成后进行库存同步";
    public final static int SYNC_STOCKINFO_FAIL_BOX_BUSY_CODE_445 = 445;
    public final static String SYNC_STOCKINFO_FAIL_BOX_BUSY_MSG_445 = "设备服务中，请稍后重试";
    public final static int SYNC_STOCKINFO_FAIL_NULL_STOCKINFO_CODE_446 = 446;
    public final static String SYNC_STOCKINFO_FAIL_NULL_STOCKINFO_MSG_446 = "库存信息不能为空";
    public final static int SYNC_STOCKINFO_FAIL_UNKNOW_CODE_447 = 447;
    public final static String SYNC_STOCKINFO_FAIL_UNKNOW_MSG_447 = "服务器未知异常";

    // 异常视频购物信息的处理状态
    public final static int SHOPPING_INFO_STATE_OPENED = 0;
    public final static int SHOPPING_INFO_STATE_ONGOING = 1;
    public final static int SHOPPING_INFO_STATE_CLOSED = 2;

    // 异常视频处理完成以后是否已经完成售货机库存状态同步
    public final static int SYNC_BOX_STOCK_ONGOING = 0;
    public final static int SYNC_BOX_STOCK_FAIL = 1;
    public final static int SYNC_BOX_STOCK_SUCCESS = 2;

}
