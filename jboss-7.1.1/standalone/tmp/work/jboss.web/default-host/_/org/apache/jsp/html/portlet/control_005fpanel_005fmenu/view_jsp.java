package org.apache.jsp.html.portlet.control_005fpanel_005fmenu;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.cal.Recurrence;
import com.liferay.portal.kernel.captcha.CaptchaMaxChallengesException;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.AlwaysTrueRowChecker;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.JSPSearchEntry;
import com.liferay.portal.kernel.dao.search.ResultRow;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.dao.search.SearchEntry;
import com.liferay.portal.kernel.dao.search.TextSearchEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.language.LanguageWrapper;
import com.liferay.portal.kernel.language.UnicodeLanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.DynamicRenderRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletMode;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.servlet.ServletContextUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.servlet.StringServletResponse;
import com.liferay.portal.kernel.staging.LayoutStagingUtil;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.BooleanWrapper;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderedProperties;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.SortedArrayList;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.UnicodeFormatter;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UniqueList;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.*;
import com.liferay.portal.model.impl.*;
import com.liferay.portal.security.auth.AuthTokenUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.pacl.PACLClassLoaderUtil;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.service.*;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.LayoutPrototypePermissionUtil;
import com.liferay.portal.service.permission.LayoutSetPrototypePermissionUtil;
import com.liferay.portal.service.permission.PortalPermissionUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.struts.StrutsUtil;
import com.liferay.portal.struts.TilesAttributeUtil;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.CookieKeys;
import com.liferay.portal.util.JavaScriptBundleUtil;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletCategoryKeys;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.SessionClicks;
import com.liferay.portal.util.SessionTreeJSClicks;
import com.liferay.portal.util.ShutdownUtil;
import com.liferay.portal.util.WebAppPool;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.util.comparator.PortletCategoryComparator;
import com.liferay.portal.util.comparator.PortletTitleComparator;
import com.liferay.portal.webserver.WebServerServletTokenUtil;
import com.liferay.portlet.InvokerPortlet;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletConfigFactoryUtil;
import com.liferay.portlet.PortletInstanceFactoryUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletResponseImpl;
import com.liferay.portlet.PortletSetupUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.PortletURLUtil;
import com.liferay.portlet.RenderParametersPool;
import com.liferay.portlet.RenderRequestFactory;
import com.liferay.portlet.RenderRequestImpl;
import com.liferay.portlet.RenderResponseFactory;
import com.liferay.portlet.RenderResponseImpl;
import com.liferay.portlet.portletconfiguration.util.PortletConfigurationUtil;
import com.liferay.portlet.sites.util.SitesUtil;
import com.liferay.util.ContentUtil;
import com.liferay.util.CreditCard;
import com.liferay.util.Encryptor;
import com.liferay.util.JS;
import com.liferay.util.PKParser;
import com.liferay.util.PwdGenerator;
import com.liferay.util.State;
import com.liferay.util.StateUtil;
import com.liferay.util.log4j.Levels;
import com.liferay.util.portlet.PortletRequestUtil;
import com.liferay.util.xml.XMLFormatter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.UnavailableException;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;
import com.liferay.util.bridges.alloy.AlloyPortlet;
import com.liferay.util.bridges.alloy.BaseAlloyControllerImpl;

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(6);
    _jspx_dependants.add("/html/portlet/control_panel_menu/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/control_panel_menu/init-ext.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finputCssClass_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005flocalizeMessage_005fid_005ficon_005fdirection_005falign;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flocalizeMessage_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fstaging_0026_005fshowManageBranches_005ficon_005fgroupId_005fextended_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005flabel_005fimage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005ficon_005fdirection_005falign;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName_005fdoAsGroupId_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finputCssClass_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005flocalizeMessage_005fid_005ficon_005fdirection_005falign = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flocalizeMessage_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fstaging_0026_005fshowManageBranches_005ficon_005fgroupId_005fextended_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005flabel_005fimage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005ficon_005fdirection_005falign = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName_005fdoAsGroupId_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finputCssClass_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005flocalizeMessage_005fid_005ficon_005fdirection_005falign.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flocalizeMessage_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fstaging_0026_005fshowManageBranches_005ficon_005fgroupId_005fextended_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005ficon_005fdirection_005falign.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName_005fdoAsGroupId_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      response.addHeader("X-Powered-By", "JSP/2.2");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      //  liferay-theme:defineObjects
      com.liferay.taglib.theme.DefineObjectsTag _jspx_th_liferay_002dtheme_005fdefineObjects_005f0 = (com.liferay.taglib.theme.DefineObjectsTag) _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.get(com.liferay.taglib.theme.DefineObjectsTag.class);
      _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.setParent(null);
      int _jspx_eval_liferay_002dtheme_005fdefineObjects_005f0 = _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.doStartTag();
      if (_jspx_th_liferay_002dtheme_005fdefineObjects_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fdefineObjects_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fdefineObjects_005f0);
      com.liferay.portal.theme.ThemeDisplay themeDisplay = null;
      com.liferay.portal.model.Company company = null;
      com.liferay.portal.model.Account account = null;
      com.liferay.portal.model.User user = null;
      com.liferay.portal.model.User realUser = null;
      com.liferay.portal.model.Contact contact = null;
      com.liferay.portal.model.Layout layout = null;
      java.util.List layouts = null;
      java.lang.Long plid = null;
      com.liferay.portal.model.LayoutTypePortlet layoutTypePortlet = null;
      java.lang.Long scopeGroupId = null;
      com.liferay.portal.security.permission.PermissionChecker permissionChecker = null;
      java.util.Locale locale = null;
      java.util.TimeZone timeZone = null;
      com.liferay.portal.model.Theme theme = null;
      com.liferay.portal.model.ColorScheme colorScheme = null;
      com.liferay.portal.theme.PortletDisplay portletDisplay = null;
      java.lang.Long portletGroupId = null;
      themeDisplay = (com.liferay.portal.theme.ThemeDisplay) _jspx_page_context.findAttribute("themeDisplay");
      company = (com.liferay.portal.model.Company) _jspx_page_context.findAttribute("company");
      account = (com.liferay.portal.model.Account) _jspx_page_context.findAttribute("account");
      user = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("user");
      realUser = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("realUser");
      contact = (com.liferay.portal.model.Contact) _jspx_page_context.findAttribute("contact");
      layout = (com.liferay.portal.model.Layout) _jspx_page_context.findAttribute("layout");
      layouts = (java.util.List) _jspx_page_context.findAttribute("layouts");
      plid = (java.lang.Long) _jspx_page_context.findAttribute("plid");
      layoutTypePortlet = (com.liferay.portal.model.LayoutTypePortlet) _jspx_page_context.findAttribute("layoutTypePortlet");
      scopeGroupId = (java.lang.Long) _jspx_page_context.findAttribute("scopeGroupId");
      permissionChecker = (com.liferay.portal.security.permission.PermissionChecker) _jspx_page_context.findAttribute("permissionChecker");
      locale = (java.util.Locale) _jspx_page_context.findAttribute("locale");
      timeZone = (java.util.TimeZone) _jspx_page_context.findAttribute("timeZone");
      theme = (com.liferay.portal.model.Theme) _jspx_page_context.findAttribute("theme");
      colorScheme = (com.liferay.portal.model.ColorScheme) _jspx_page_context.findAttribute("colorScheme");
      portletDisplay = (com.liferay.portal.theme.PortletDisplay) _jspx_page_context.findAttribute("portletDisplay");
      portletGroupId = (java.lang.Long) _jspx_page_context.findAttribute("portletGroupId");
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      //  portlet:defineObjects
      com.liferay.taglib.portlet.DefineObjectsTag _jspx_th_portlet_005fdefineObjects_005f0 = (com.liferay.taglib.portlet.DefineObjectsTag) _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.get(com.liferay.taglib.portlet.DefineObjectsTag.class);
      _jspx_th_portlet_005fdefineObjects_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005fdefineObjects_005f0.setParent(null);
      int _jspx_eval_portlet_005fdefineObjects_005f0 = _jspx_th_portlet_005fdefineObjects_005f0.doStartTag();
      if (_jspx_th_portlet_005fdefineObjects_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.reuse(_jspx_th_portlet_005fdefineObjects_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.reuse(_jspx_th_portlet_005fdefineObjects_005f0);
      javax.portlet.ActionRequest actionRequest = null;
      javax.portlet.ActionResponse actionResponse = null;
      javax.portlet.EventRequest eventRequest = null;
      javax.portlet.EventResponse eventResponse = null;
      com.liferay.portal.kernel.portlet.LiferayPortletRequest liferayPortletRequest = null;
      com.liferay.portal.kernel.portlet.LiferayPortletResponse liferayPortletResponse = null;
      javax.portlet.PortletConfig portletConfig = null;
      java.lang.String portletName = null;
      javax.portlet.PortletPreferences portletPreferences = null;
      java.util.Map portletPreferencesValues = null;
      javax.portlet.PortletSession portletSession = null;
      java.util.Map portletSessionScope = null;
      javax.portlet.RenderRequest renderRequest = null;
      javax.portlet.RenderResponse renderResponse = null;
      javax.portlet.ResourceRequest resourceRequest = null;
      javax.portlet.ResourceResponse resourceResponse = null;
      actionRequest = (javax.portlet.ActionRequest) _jspx_page_context.findAttribute("actionRequest");
      actionResponse = (javax.portlet.ActionResponse) _jspx_page_context.findAttribute("actionResponse");
      eventRequest = (javax.portlet.EventRequest) _jspx_page_context.findAttribute("eventRequest");
      eventResponse = (javax.portlet.EventResponse) _jspx_page_context.findAttribute("eventResponse");
      liferayPortletRequest = (com.liferay.portal.kernel.portlet.LiferayPortletRequest) _jspx_page_context.findAttribute("liferayPortletRequest");
      liferayPortletResponse = (com.liferay.portal.kernel.portlet.LiferayPortletResponse) _jspx_page_context.findAttribute("liferayPortletResponse");
      portletConfig = (javax.portlet.PortletConfig) _jspx_page_context.findAttribute("portletConfig");
      portletName = (java.lang.String) _jspx_page_context.findAttribute("portletName");
      portletPreferences = (javax.portlet.PortletPreferences) _jspx_page_context.findAttribute("portletPreferences");
      portletPreferencesValues = (java.util.Map) _jspx_page_context.findAttribute("portletPreferencesValues");
      portletSession = (javax.portlet.PortletSession) _jspx_page_context.findAttribute("portletSession");
      portletSessionScope = (java.util.Map) _jspx_page_context.findAttribute("portletSessionScope");
      renderRequest = (javax.portlet.RenderRequest) _jspx_page_context.findAttribute("renderRequest");
      renderResponse = (javax.portlet.RenderResponse) _jspx_page_context.findAttribute("renderResponse");
      resourceRequest = (javax.portlet.ResourceRequest) _jspx_page_context.findAttribute("resourceRequest");
      resourceResponse = (javax.portlet.ResourceResponse) _jspx_page_context.findAttribute("resourceResponse");
      out.write('\n');
      out.write('\n');

PortletMode portletMode = liferayPortletRequest.getPortletMode();
WindowState windowState = liferayPortletRequest.getWindowState();

PortletURL currentURLObj = PortletURLUtil.getCurrent(liferayPortletRequest, liferayPortletResponse);

String currentURL = currentURLObj.toString();
//String currentURL = PortalUtil.getCurrentURL(request);

      out.write('\n');
      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<h1 class=\"user-greeting\">\n");
      out.write("\t");
      if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("</h1>\n");
      out.write("\n");
      out.write("<div class=\"portal-add-content\">\n");
      out.write("\t<div class=\"control-panel-tools\">\n");
      out.write("\t\t");
      if (_jspx_meth_aui_005finput_005f0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("\t</div>\n");
      out.write("\n");
      out.write("\t");
      //  liferay-ui:panel-container
      com.liferay.taglib.ui.PanelContainerTag _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0 = (com.liferay.taglib.ui.PanelContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.get(com.liferay.taglib.ui.PanelContainerTag.class);
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setParent(null);
      // /html/portlet/control_panel_menu/view.jsp(28,1) name = extended type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setExtended( true );
      // /html/portlet/control_panel_menu/view.jsp(28,1) name = id type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setId("controlPanelMenuAddContentPanelContainer");
      // /html/portlet/control_panel_menu/view.jsp(28,1) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPersistState( true );
      int _jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 = _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doStartTag();
      if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\n");
          out.write("\t\t");

		String ppid = GetterUtil.getString((String)request.getAttribute("control_panel.jsp-ppid"), layoutTypePortlet.getStateMaxPortletId());

		String category = PortalUtil.getControlPanelCategory(ppid, themeDisplay);

		String[] allCategories = PortletCategoryKeys.ALL;

		String controlPanelCategory = HttpUtil.getParameter(PortalUtil.getCurrentURL(request), "controlPanelCategory", false);

		if (Validator.isNotNull(controlPanelCategory)) {
			allCategories = new String[] {controlPanelCategory};
		}

		for (String curCategory : allCategories) {
			Collection<Portlet> categoryPortlets = PortalUtil.getControlPanelPortlets(themeDisplay.getCompanyId(), curCategory);

			List<Layout> scopeLayouts = new ArrayList<Layout>();

			String curGroupLabel = null;
			Group curGroup = null;

			String title = null;

			if (curCategory.equals(PortletCategoryKeys.MY)) {
				title = HtmlUtil.escape(StringUtil.shorten(user.getFullName(), 25));
			}
			else if (curCategory.equals(PortletCategoryKeys.CONTENT)) {
				Layout scopeLayout = null;

				curGroup = themeDisplay.getScopeGroup();

				if (curGroup.isLayout()) {
					scopeLayout = LayoutLocalServiceUtil.getLayout(curGroup.getClassPK());

					curGroup = scopeLayout.getGroup();
				}

				List<Group> manageableSites = null;

				if (Validator.isNotNull(controlPanelCategory)) {
					manageableSites = new ArrayList<Group>();

					if (curGroup.isUser()) {
						manageableSites.add(user.getGroup());
					}
					else {
						long groupId = GetterUtil.getLong(HttpUtil.getParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", false));

						Group group = GroupLocalServiceUtil.getGroup(groupId);

						manageableSites.add(group);
					}
				}
				else {
					manageableSites = GroupServiceUtil.getManageableSites(categoryPortlets, PropsValues.CONTROL_PANEL_NAVIGATION_MAX_SITES);

					Group userGroup = user.getGroup();

					if (userGroup.hasPrivateLayouts() || userGroup.hasPublicLayouts()) {
						manageableSites.add(0, userGroup);
					}

					if (PortalUtil.isCompanyControlPanelVisible(themeDisplay)) {
						Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(themeDisplay.getCompanyId());

						manageableSites.add(0, companyGroup);
					}
				}

				Group curLiveGroup = curGroup;

				if (curGroup.isStagingGroup()) {
					curLiveGroup = curGroup.getLiveGroup();
				}

				if (!manageableSites.isEmpty() && !manageableSites.contains(curLiveGroup)) {
					if (curLiveGroup.isSite() && PortletPermissionUtil.contains(permissionChecker, curLiveGroup.getGroupId(), 0, categoryPortlets, ActionKeys.ACCESS_IN_CONTROL_PANEL)) {
						manageableSites.add(0, curLiveGroup);
					}
					else {
						curGroup = manageableSites.get(0);

						curLiveGroup = curGroup;

						themeDisplay.setScopeGroupId(curGroup.getGroupId());
					}
				}

				String curGroupName = null;

				if (curGroup.isUser() && (curGroup.getClassPK() == user.getUserId())) {
					curGroupName = LanguageUtil.format(pageContext, "x-personal-site", curLiveGroup.getDescriptiveName(locale));
				}
				else {
					curGroupName = curLiveGroup.getDescriptiveName(locale);
				}

				if (category.equals(PortletCategoryKeys.CONTENT)) {
					PortalUtil.addPortletBreadcrumbEntry(request, curGroupName, null);
				}

				if (scopeLayout == null) {
					curGroupLabel = LanguageUtil.get(pageContext, "default");
				}
				else {
					curGroupLabel = scopeLayout.getName(locale);

					if (category.equals(PortletCategoryKeys.CONTENT)) {
						PortalUtil.addPortletBreadcrumbEntry(request, curGroupLabel, null);
					}
				}
				
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t");
          //  liferay-util:buffer
          com.liferay.taglib.util.BufferTag _jspx_th_liferay_002dutil_005fbuffer_005f0 = (com.liferay.taglib.util.BufferTag) _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.get(com.liferay.taglib.util.BufferTag.class);
          _jspx_th_liferay_002dutil_005fbuffer_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dutil_005fbuffer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
          // /html/portlet/control_panel_menu/view.jsp(143,4) name = var type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dutil_005fbuffer_005f0.setVar("groupSelectorIconMenu");
          int _jspx_eval_liferay_002dutil_005fbuffer_005f0 = _jspx_th_liferay_002dutil_005fbuffer_005f0.doStartTag();
          if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dutil_005fbuffer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dutil_005fbuffer_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  c:choose
              com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f0);
              int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
              if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t\t");
                //  c:when
                com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f0 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
                _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                // /html/portlet/control_panel_menu/view.jsp(145,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fwhen_005f0.setTest( !manageableSites.isEmpty() );
                int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
                if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");

							String icon = themeDisplay.getPathThemeImages() + "/common/site_icon.png";

							if (curGroup.isCompany()) {
								icon = themeDisplay.getPathThemeImages() + "/common/folder.png";
							}
							else if (curGroup.isOrganization()) {
								icon = themeDisplay.getPathThemeImages() + "/common/organization_icon.png";
							}
							else if (curGroup.isUser()) {
								icon = themeDisplay.getPathThemeImages() + "/common/user_icon.png";
							}
							
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  //  liferay-ui:icon-menu
                  com.liferay.taglib.ui.IconMenuTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0 = (com.liferay.taglib.ui.IconMenuTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005flocalizeMessage_005fid_005ficon_005fdirection_005falign.get(com.liferay.taglib.ui.IconMenuTag.class);
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                  // /html/portlet/control_panel_menu/view.jsp(161,7) name = align type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setAlign("left");
                  // /html/portlet/control_panel_menu/view.jsp(161,7) name = direction type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setDirection("down");
                  // /html/portlet/control_panel_menu/view.jsp(161,7) name = icon type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setIcon( icon );
                  // /html/portlet/control_panel_menu/view.jsp(161,7) name = id type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setId("groupSelector");
                  // /html/portlet/control_panel_menu/view.jsp(161,7) name = localizeMessage type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setLocalizeMessage( false );
                  // /html/portlet/control_panel_menu/view.jsp(161,7) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setMessage( HtmlUtil.escape(StringUtil.shorten(curGroupName, 25)) );
                  int _jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doStartTag();
                  if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");

								for (int i = 0; i < manageableSites.size(); i++) {
									Group group = manageableSites.get(i);

									String image = "site_icon";
									String message = group.getDescriptiveName(locale);

									if (group.isCompany()) {
										image = "folder";
									}
									else if (group.isOrganization()) {
										image = "organization_icon";
									}
									else if (group.isUser()) {
										image = "user_icon";
										message = LanguageUtil.format(pageContext, "x-personal-site", group.getDescriptiveName(locale));
									}

									String url = null;

									if (manageableSites.size() > 1) {
										url = HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", group.getGroupId());
									}
								
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flocalizeMessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
                      // /html/portlet/control_panel_menu/view.jsp(188,9) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_liferay_002dui_005ficon_005f0.setImage( image );
                      // /html/portlet/control_panel_menu/view.jsp(188,9) name = localizeMessage type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_liferay_002dui_005ficon_005f0.setLocalizeMessage( false );
                      // /html/portlet/control_panel_menu/view.jsp(188,9) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_liferay_002dui_005ficon_005f0.setMessage( HtmlUtil.escape(message) );
                      // /html/portlet/control_panel_menu/view.jsp(188,9) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_liferay_002dui_005ficon_005f0.setUrl( url );
                      int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flocalizeMessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flocalizeMessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");

								}
								
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005flocalizeMessage_005fid_005ficon_005fdirection_005falign.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005flocalizeMessage_005fid_005ficon_005fdirection_005falign.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  //  c:if
                  com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f0 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                  _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                  // /html/portlet/control_panel_menu/view.jsp(201,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_c_005fif_005f0.setTest( curLiveGroup.isCompany() );
                  int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
                  if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t\t");
                    //  liferay-ui:staging
                    com.liferay.taglib.ui.StagingTag _jspx_th_liferay_002dui_005fstaging_005f0 = (com.liferay.taglib.ui.StagingTag) _005fjspx_005ftagPool_005fliferay_002dui_005fstaging_0026_005fshowManageBranches_005ficon_005fgroupId_005fextended_005fcssClass_005fnobody.get(com.liferay.taglib.ui.StagingTag.class);
                    _jspx_th_liferay_002dui_005fstaging_005f0.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005fstaging_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                    // /html/portlet/control_panel_menu/view.jsp(202,8) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fstaging_005f0.setCssClass("manage-pages-branch-menu");
                    // /html/portlet/control_panel_menu/view.jsp(202,8) name = extended type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fstaging_005f0.setExtended( true );
                    // /html/portlet/control_panel_menu/view.jsp(202,8) name = groupId type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fstaging_005f0.setGroupId( curLiveGroup.getGroupId() );
                    // /html/portlet/control_panel_menu/view.jsp(202,8) name = icon type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fstaging_005f0.setIcon("/common/tool.png");
                    // /html/portlet/control_panel_menu/view.jsp(202,8) name = showManageBranches type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fstaging_005f0.setShowManageBranches( false );
                    int _jspx_eval_liferay_002dui_005fstaging_005f0 = _jspx_th_liferay_002dui_005fstaging_005f0.doStartTag();
                    if (_jspx_th_liferay_002dui_005fstaging_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005fstaging_0026_005fshowManageBranches_005ficon_005fgroupId_005fextended_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005fstaging_005f0);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005fstaging_0026_005fshowManageBranches_005ficon_005fgroupId_005fextended_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005fstaging_005f0);
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");
                  }
                  if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                }
                if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
                out.write("\n");
                out.write("\t\t\t\t\t\t");
                //  c:otherwise
                com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
                _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");

							String image = "site";

							if (curGroup.isCompany()) {
								image = "folder";
							}
							else if (curGroup.isOrganization()) {
								image = "organization_icon";
							}
							else if (curGroup.isUser()) {
								image = "user_icon";
							}
							
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  //  liferay-ui:icon
                  com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                  _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                  // /html/portlet/control_panel_menu/view.jsp(221,7) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_005f1.setCssClass("lfr-panel-title-single");
                  // /html/portlet/control_panel_menu/view.jsp(221,7) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_005f1.setImage( image );
                  // /html/portlet/control_panel_menu/view.jsp(221,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_005f1.setLabel( true );
                  // /html/portlet/control_panel_menu/view.jsp(221,7) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_005f1.setMessage( HtmlUtil.escape(StringUtil.shorten(curGroupName, 25)) );
                  int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                }
                if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
                out.write("\n");
                out.write("\t\t\t\t\t");
              }
              if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
              out.write("\n");
              out.write("\t\t\t\t");
              int evalDoAfterBody = _jspx_th_liferay_002dutil_005fbuffer_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dutil_005fbuffer_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.reuse(_jspx_th_liferay_002dutil_005fbuffer_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.reuse(_jspx_th_liferay_002dutil_005fbuffer_005f0);
          java.lang.String groupSelectorIconMenu = null;
          groupSelectorIconMenu = (java.lang.String) _jspx_page_context.findAttribute("groupSelectorIconMenu");
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");

				scopeLayouts.addAll(LayoutLocalServiceUtil.getScopeGroupLayouts(curGroup.getGroupId(), false));
				scopeLayouts.addAll(LayoutLocalServiceUtil.getScopeGroupLayouts(curGroup.getGroupId(), true));

				title = groupSelectorIconMenu;
			}
			else if (curCategory.equals(PortletCategoryKeys.PORTAL) && (CompanyLocalServiceUtil.getCompaniesCount(false) > 1)) {
				title = HtmlUtil.escape(company.getName());
			}
			else {
				title = LanguageUtil.get(pageContext, "category." + curCategory);
			}

			List<Portlet> portlets = PortalUtil.getControlPanelPortlets(curCategory, themeDisplay);
			
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  liferay-util:buffer
          com.liferay.taglib.util.BufferTag _jspx_th_liferay_002dutil_005fbuffer_005f1 = (com.liferay.taglib.util.BufferTag) _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.get(com.liferay.taglib.util.BufferTag.class);
          _jspx_th_liferay_002dutil_005fbuffer_005f1.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dutil_005fbuffer_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
          // /html/portlet/control_panel_menu/view.jsp(247,3) name = var type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dutil_005fbuffer_005f1.setVar("categoryPortletsContent");
          int _jspx_eval_liferay_002dutil_005fbuffer_005f1 = _jspx_th_liferay_002dutil_005fbuffer_005f1.doStartTag();
          if (_jspx_eval_liferay_002dutil_005fbuffer_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dutil_005fbuffer_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dutil_005fbuffer_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dutil_005fbuffer_005f1.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f1 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f1);
              // /html/portlet/control_panel_menu/view.jsp(248,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f1.setTest( !scopeLayouts.isEmpty() && curCategory.equals(PortletCategoryKeys.CONTENT) );
              int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
              if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t<div class=\"nobr lfr-title-scope-selector\">\n");
                out.write("\t\t\t\t\t\t");
                //  liferay-ui:icon-menu
                com.liferay.taglib.ui.IconMenuTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f1 = (com.liferay.taglib.ui.IconMenuTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005ficon_005fdirection_005falign.get(com.liferay.taglib.ui.IconMenuTag.class);
                _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                // /html/portlet/control_panel_menu/view.jsp(250,6) name = align type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.setAlign("left");
                // /html/portlet/control_panel_menu/view.jsp(250,6) name = direction type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.setDirection("down");
                // /html/portlet/control_panel_menu/view.jsp(250,6) name = icon type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.setIcon("");
                // /html/portlet/control_panel_menu/view.jsp(250,6) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.setMessage( LanguageUtil.get(pageContext, "scope") + StringPool.COLON + StringPool.SPACE + curGroupLabel );
                int _jspx_eval_liferay_002dui_005ficon_002dmenu_005f1 = _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.doStartTag();
                if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.doInitBody();
                  }
                  do {
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");
                    //  liferay-ui:icon
                    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                    _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f1);
                    // /html/portlet/control_panel_menu/view.jsp(251,7) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ficon_005f2.setImage("folder");
                    // /html/portlet/control_panel_menu/view.jsp(251,7) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ficon_005f2.setMessage("default");
                    // /html/portlet/control_panel_menu/view.jsp(251,7) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ficon_005f2.setUrl( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", curGroup.getGroupId()) );
                    int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
                    if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                    out.write("\n");
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");

							for (Layout curScopeLayout : scopeLayouts) {
							
                    out.write("\n");
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t\t");
                    //  liferay-ui:icon
                    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                    _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f1);
                    // /html/portlet/control_panel_menu/view.jsp(261,8) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ficon_005f3.setImage("folder");
                    // /html/portlet/control_panel_menu/view.jsp(261,8) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ficon_005f3.setMessage( HtmlUtil.escape(curScopeLayout.getName(locale)) );
                    // /html/portlet/control_panel_menu/view.jsp(261,8) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ficon_005f3.setUrl( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", curScopeLayout.getScopeGroup().getGroupId()) );
                    int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
                    if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                    out.write("\n");
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");

							}
							
                    out.write("\n");
                    out.write("\n");
                    out.write("\t\t\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_liferay_002dui_005ficon_002dmenu_005f1.doAfterBody();
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                      break;
                  } while (true);
                  if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.popBody();
                  }
                }
                if (_jspx_th_liferay_002dui_005ficon_002dmenu_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005ficon_005fdirection_005falign.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f1);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fmessage_005ficon_005fdirection_005falign.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f1);
                out.write("\n");
                out.write("\t\t\t\t\t</div>\n");
                out.write("\t\t\t\t");
              }
              if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t<ul class=\"category-portlets\">\n");
              out.write("\n");
              out.write("\t\t\t\t\t");

					for (Portlet portlet : portlets) {
						if (portlet.isActive() && !portlet.isInstanceable()) {
							String portletId = portlet.getPortletId();
					
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t<li class=\"");
              out.print( ppid.equals(portletId) ? "selected-portlet" : "" );
              out.write("\">\n");
              out.write("\t\t\t\t\t\t\t\t");
              //  liferay-portlet:renderURL
              com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName_005fdoAsGroupId_005fnobody.get(com.liferay.taglib.portlet.RenderURLTag.class);
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f1);
              // /html/portlet/control_panel_menu/view.jsp(284,8) name = doAsGroupId type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setDoAsGroupId( themeDisplay.getScopeGroupId() );
              // /html/portlet/control_panel_menu/view.jsp(284,8) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPortletName( portlet.getRootPortletId() );
              // /html/portlet/control_panel_menu/view.jsp(284,8) name = var type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setVar("portletURL");
              // /html/portlet/control_panel_menu/view.jsp(284,8) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setWindowState( WindowState.MAXIMIZED.toString() );
              int _jspx_eval_liferay_002dportlet_005frenderURL_005f0 = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doStartTag();
              if (_jspx_th_liferay_002dportlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName_005fdoAsGroupId_005fnobody.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName_005fdoAsGroupId_005fnobody.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
              java.lang.String portletURL = null;
              portletURL = (java.lang.String) _jspx_page_context.findAttribute("portletURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t<a href=\"");
              out.print( portletURL );
              out.write("\" id=\"");
              if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_liferay_002dutil_005fbuffer_005f1, _jspx_page_context))
                return;
              out.write("portlet_");
              out.print( portletId );
              out.write("\">\n");
              out.write("\t\t\t\t\t\t\t\t\t");
              //  c:choose
              com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f1);
              int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
              if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t\t\t\t\t\t");
                //  c:when
                com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f1 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                // /html/portlet/control_panel_menu/view.jsp(293,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fwhen_005f1.setTest( Validator.isNull(portlet.getIcon()) );
                int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t");
                  //  liferay-ui:icon
                  com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f4 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                  _jspx_th_liferay_002dui_005ficon_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                  // /html/portlet/control_panel_menu/view.jsp(294,11) name = src type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dui_005ficon_005f4.setSrc( themeDisplay.getPathContext() + "/html/icons/default.png" );
                  int _jspx_eval_liferay_002dui_005ficon_005f4 = _jspx_th_liferay_002dui_005ficon_005f4.doStartTag();
                  if (_jspx_th_liferay_002dui_005ficon_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t");
                }
                if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
                out.write("\n");
                out.write("\t\t\t\t\t\t\t\t\t\t");
                //  c:otherwise
                com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
                _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
                _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
                if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t");
                  //  liferay-portlet:icon-portlet
                  com.liferay.taglib.portletext.IconPortletTag _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0 = (com.liferay.taglib.portletext.IconPortletTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.get(com.liferay.taglib.portletext.IconPortletTag.class);
                  _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
                  // /html/portlet/control_panel_menu/view.jsp(297,11) name = portlet type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setPortlet( portlet );
                  int _jspx_eval_liferay_002dportlet_005ficon_002dportlet_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.doStartTag();
                  if (_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t");
                }
                if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
                out.write("\n");
                out.write("\t\t\t\t\t\t\t\t\t");
              }
              if (_jspx_th_c_005fchoose_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletTitle(portlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t</a>\n");
              out.write("\t\t\t\t\t\t\t</li>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f2 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f1);
              // /html/portlet/control_panel_menu/view.jsp(305,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f2.setTest( !ppid.equals(portletId) );
              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t\t\t\t\t");

								String portletClassName = portlet.getPortletClass();
								
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t\t\t\t\t");

								if (portletClassName.equals(AlloyPortlet.class.getName())) {
									PortletConfig alloyPortletConfig = PortletConfigFactoryUtil.create(portlet, application);

									PortletContext alloyPortletContext = alloyPortletConfig.getPortletContext();

									if (alloyPortletContext.getAttribute(BaseAlloyControllerImpl.TOUCH + portlet.getRootPortletId()) != Boolean.FALSE) {
								
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t\t\t\t\t\t\t<iframe height=\"0\" src=\"");
                out.print( portletURL );
                out.write("\" style=\"display: none; visibility: hidden;\" width=\"0\"></iframe>\n");
                out.write("\n");
                out.write("\t\t\t\t\t\t\t\t");

									}
								}
								
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t\t\t\t");
              }
              if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t");

						}
					}
					
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t</ul>\n");
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_liferay_002dutil_005fbuffer_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dutil_005fbuffer_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dutil_005fbuffer_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.reuse(_jspx_th_liferay_002dutil_005fbuffer_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.reuse(_jspx_th_liferay_002dutil_005fbuffer_005f1);
          java.lang.String categoryPortletsContent = null;
          categoryPortletsContent = (java.lang.String) _jspx_page_context.findAttribute("categoryPortletsContent");
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  c:choose
          com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
          int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
          if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f2 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
            // /html/portlet/control_panel_menu/view.jsp(338,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f2.setTest( Validator.isNotNull(controlPanelCategory) );
            int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
            if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t<div class=\"lfr-panel-container\" id=\"controlPanelMenuAddContentPanelContainer\">\n");
              out.write("\t\t\t\t\t\t<div class=\"lfr-panel lfr-component panel-page-category lfr-extended\" id=\"panel-manage-content\">\n");
              out.write("\t\t\t\t\t\t\t<div class=\"lfr-panel-content\">\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t");
              out.print( categoryPortletsContent );
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t</div>\n");
              out.write("\t\t\t\t\t\t</div>\n");
              out.write("\t\t\t\t\t</div>\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fwhen_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:otherwise
            com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
            _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
            _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
            int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
            if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f3 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
              // /html/portlet/control_panel_menu/view.jsp(350,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f3.setTest( !portlets.isEmpty() );
              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t\t");
                //  liferay-ui:panel
                com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f0 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                _jspx_th_liferay_002dui_005fpanel_005f0.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005fpanel_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                // /html/portlet/control_panel_menu/view.jsp(351,6) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fpanel_005f0.setCollapsible( true );
                // /html/portlet/control_panel_menu/view.jsp(351,6) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fpanel_005f0.setCssClass("lfr-component panel-page-category");
                // /html/portlet/control_panel_menu/view.jsp(351,6) name = extended type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fpanel_005f0.setExtended( true );
                // /html/portlet/control_panel_menu/view.jsp(351,6) name = id type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fpanel_005f0.setId( "panel-manage-" + curCategory );
                // /html/portlet/control_panel_menu/view.jsp(351,6) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fpanel_005f0.setPersistState( true );
                // /html/portlet/control_panel_menu/view.jsp(351,6) name = title type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fpanel_005f0.setTitle( title );
                int _jspx_eval_liferay_002dui_005fpanel_005f0 = _jspx_th_liferay_002dui_005fpanel_005f0.doStartTag();
                if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  out.print( categoryPortletsContent );
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                }
                if (_jspx_th_liferay_002dui_005fpanel_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f0);
                out.write("\n");
                out.write("\t\t\t\t\t");
              }
              if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fotherwise_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fchoose_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
          out.write("\n");
          out.write("\n");
          out.write("\t\t");

		}
		
          out.write('\n');
          out.write('\n');
          out.write('	');
          int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.reuse(_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.reuse(_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      if (_jspx_meth_aui_005fscript_005f0(_jspx_page_context))
        return;
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent(null);
    // /html/portlet/control_panel_menu/view.jsp(20,1) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("control-panel");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finputCssClass_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f0.setParent(null);
    // /html/portlet/control_panel_menu/view.jsp(25,2) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f0.setCssClass("search-panels");
    // /html/portlet/control_panel_menu/view.jsp(25,2) name = inputCssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f0.setInputCssClass("search-panels-input");
    // /html/portlet/control_panel_menu/view.jsp(25,2) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f0.setLabel("");
    // /html/portlet/control_panel_menu/view.jsp(25,2) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f0.setName("searchPanel");
    int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
    if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finputCssClass_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finputCssClass_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dutil_005fbuffer_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f1);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f0.setParent(null);
    // /html/portlet/control_panel_menu/view.jsp(367,0) name = use type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fscript_005f0.setUse("liferay-panel");
    int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
    if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f0.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tvar trigger = A.one('#");
        if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
          return true;
        out.write("groupSelector a');\n");
        out.write("\n");
        out.write("\tvar panelContainer = Liferay.Panel.get('controlPanelMenuAddContentPanelContainer');\n");
        out.write("\n");
        out.write("\tif (trigger && panelContainer) {\n");
        out.write("\t\ttrigger.on(\n");
        out.write("\t\t\t'click',\n");
        out.write("\t\t\tfunction(event) {\n");
        out.write("\t\t\t\tpanelContainer.once(\n");
        out.write("\t\t\t\t\t'collapse',\n");
        out.write("\t\t\t\t\tfunction(event) {\n");
        out.write("\t\t\t\t\t\tevent.preventDefault();\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t);\n");
        out.write("\t\t\t}\n");
        out.write("\t\t);\n");
        out.write("\t}\n");
        int evalDoAfterBody = _jspx_th_aui_005fscript_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fscript_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
    return false;
  }
}
