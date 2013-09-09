package org.apache.jsp.html.taglib.portlet.icon_005foptions;

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
import com.liferay.taglib.aui.AUIUtil;
import com.liferay.taglib.util.InlineUtil;

public final class page_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(4);
    _jspx_dependants.add("/html/taglib/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/taglib/init-ext.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowArrow_005fmessage_005ficon_005fextended_005fdirection_005fcssClass_005falign;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002drefresh_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_002dcss_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dconfiguration_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002ddefaults_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002dguest_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dexport_002dimport_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dhelp_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dprint_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowArrow_005fmessage_005ficon_005fextended_005fdirection_005fcssClass_005falign = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002drefresh_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_002dcss_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dconfiguration_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002ddefaults_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002dguest_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dexport_002dimport_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dhelp_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dprint_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowArrow_005fmessage_005ficon_005fextended_005fdirection_005fcssClass_005falign.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002drefresh_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_002dcss_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dconfiguration_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002ddefaults_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002dguest_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dexport_002dimport_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dhelp_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dprint_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.release();
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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

PortletRequest portletRequest = (PortletRequest)request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);

PortletResponse portletResponse = (PortletResponse)request.getAttribute(JavaConstants.JAVAX_PORTLET_RESPONSE);

String namespace = StringPool.BLANK;

boolean auiFormUseNamespace = GetterUtil.getBoolean((String)request.getAttribute("aui:form:useNamespace"), true);

if ((portletResponse != null) && auiFormUseNamespace) {
	namespace = portletResponse.getNamespace();
}

String currentURL = PortalUtil.getCurrentURL(request);

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      //  liferay-ui:icon-menu
      com.liferay.taglib.ui.IconMenuTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0 = (com.liferay.taglib.ui.IconMenuTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowArrow_005fmessage_005ficon_005fextended_005fdirection_005fcssClass_005falign.get(com.liferay.taglib.ui.IconMenuTag.class);
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setParent(null);
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = align type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setAlign("auto");
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setCssClass("portlet-options");
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = direction type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setDirection("down");
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = extended type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setExtended( false );
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = icon type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setIcon("");
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setMessage("options");
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = showArrow type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setShowArrow( false );
      // /html/taglib/portlet/icon_options/page.jsp(19,0) name = showWhenSingleIcon type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setShowWhenSingleIcon( true );
      int _jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doStartTag();
      if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002drefresh_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dconfiguration_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dedit_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dedit_002dguest_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dexport_002dimport_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dhelp_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dportlet_005ficon_002dprint_005f0(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');

	Portlet portlet = (Portlet)request.getAttribute(WebKeys.RENDER_PORTLET);
	
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f0 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/taglib/portlet/icon_options/page.jsp(51,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f0.setTest( portlet != null );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\n");
            out.write("\t\t");

		PortletPreferences portletSetup = PortletPreferencesFactoryUtil.getStrictLayoutPortletSetup(layout, portletDisplay.getId());

		boolean widgetShowAddAppLink = GetterUtil.getBoolean(portletSetup.getValue("lfrWidgetShowAddAppLink", null), PropsValues.THEME_PORTLET_SHARING_DEFAULT);

		String facebookAPIKey = portletSetup.getValue("lfrFacebookApiKey", StringPool.BLANK);
		String facebookCanvasPageURL = portletSetup.getValue("lfrFacebookCanvasPageUrl", StringPool.BLANK);
		boolean facebookShowAddAppLink = GetterUtil.getBoolean(portletSetup.getValue("lfrFacebookShowAddAppLink", null), true);

		if (Validator.isNull(facebookCanvasPageURL) || Validator.isNull(facebookAPIKey)) {
			facebookShowAddAppLink = false;
		}

		boolean iGoogleShowAddAppLink = GetterUtil.getBoolean(portletSetup.getValue("lfrIgoogleShowAddAppLink", StringPool.BLANK));
		boolean netvibesShowAddAppLinks = GetterUtil.getBoolean(portletSetup.getValue("lfrNetvibesShowAddAppLink", StringPool.BLANK));
		boolean appShowShareWithFriendsLink = GetterUtil.getBoolean(portletSetup.getValue("lfrAppShowShareWithFriendsLink", StringPool.BLANK));
		
            out.write("\n");
            out.write("\n");
            out.write("\t\t");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f1 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
            // /html/taglib/portlet/icon_options/page.jsp(71,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f1.setTest( widgetShowAddAppLink || facebookShowAddAppLink || iGoogleShowAddAppLink || netvibesShowAddAppLinks || appShowShareWithFriendsLink );
            int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
            if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f2 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/taglib/portlet/icon_options/page.jsp(72,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f2.setTest( widgetShowAddAppLink );
              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t");

				String widgetHREF = "javascript:Liferay.PortletSharing.showWidgetInfo('" + PortalUtil.getWidgetURL(portlet, themeDisplay) + "');";
				
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t");
                //  liferay-ui:icon
                com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                // /html/taglib/portlet/icon_options/page.jsp(78,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f0.setCssClass( portletDisplay.getNamespace() + "expose-as-widget" );
                // /html/taglib/portlet/icon_options/page.jsp(78,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f0.setImage("../dock/add_content");
                // /html/taglib/portlet/icon_options/page.jsp(78,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f0.setLabel( true );
                // /html/taglib/portlet/icon_options/page.jsp(78,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f0.setMessage("add-to-any-website");
                // /html/taglib/portlet/icon_options/page.jsp(78,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f0.setUrl( widgetHREF );
                int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                out.write("\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f3 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/taglib/portlet/icon_options/page.jsp(87,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f3.setTest( facebookShowAddAppLink );
              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t");
                //  liferay-ui:icon
                com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                // /html/taglib/portlet/icon_options/page.jsp(88,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f1.setImage("../social_bookmarks/facebook");
                // /html/taglib/portlet/icon_options/page.jsp(88,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f1.setLabel( true );
                // /html/taglib/portlet/icon_options/page.jsp(88,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f1.setMessage("add-to-facebook");
                // /html/taglib/portlet/icon_options/page.jsp(88,4) name = method type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f1.setMethod("get");
                // /html/taglib/portlet/icon_options/page.jsp(88,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f1.setUrl( "http://www.facebook.com/add.php?api_key=" + facebookAPIKey + "&ref=pd" );
                int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
                if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                out.write("\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f4 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/taglib/portlet/icon_options/page.jsp(97,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f4.setTest( iGoogleShowAddAppLink );
              int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
              if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t");

				String googleGadgetHREF = "http://fusion.google.com/add?source=atgs&moduleurl=" + PortalUtil.getGoogleGadgetURL(portlet, themeDisplay);
				
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t");
                //  liferay-ui:icon
                com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                // /html/taglib/portlet/icon_options/page.jsp(103,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f2.setCssClass( portletDisplay.getNamespace() + "expose-as-widget" );
                // /html/taglib/portlet/icon_options/page.jsp(103,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f2.setImage("../dock/add_content");
                // /html/taglib/portlet/icon_options/page.jsp(103,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f2.setLabel( true );
                // /html/taglib/portlet/icon_options/page.jsp(103,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f2.setMessage("add-to-igoogle");
                // /html/taglib/portlet/icon_options/page.jsp(103,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f2.setUrl( googleGadgetHREF );
                int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
                if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                out.write("\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f5 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/taglib/portlet/icon_options/page.jsp(112,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f5.setTest( netvibesShowAddAppLinks );
              int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
              if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t");

				String netvibesHREF = "javascript:Liferay.PortletSharing.showNetvibesInfo('" + PortalUtil.getNetvibesURL(portlet, themeDisplay) + "');";
				
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t");
                //  liferay-ui:icon
                com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                // /html/taglib/portlet/icon_options/page.jsp(118,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f3.setImage("../dock/add_content");
                // /html/taglib/portlet/icon_options/page.jsp(118,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f3.setLabel( true );
                // /html/taglib/portlet/icon_options/page.jsp(118,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f3.setMessage("add-to-netvibes");
                // /html/taglib/portlet/icon_options/page.jsp(118,4) name = method type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f3.setMethod("get");
                // /html/taglib/portlet/icon_options/page.jsp(118,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f3.setUrl( netvibesHREF );
                int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
                if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                out.write("\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f6 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/taglib/portlet/icon_options/page.jsp(127,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f6.setTest( appShowShareWithFriendsLink );
              int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
              if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t");
                //  liferay-ui:icon
                com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f4 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                _jspx_th_liferay_002dui_005ficon_005f4.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ficon_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                // /html/taglib/portlet/icon_options/page.jsp(128,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f4.setImage("share");
                // /html/taglib/portlet/icon_options/page.jsp(128,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f4.setLabel( true );
                // /html/taglib/portlet/icon_options/page.jsp(128,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f4.setMessage("share-this-application-with-friends");
                // /html/taglib/portlet/icon_options/page.jsp(128,4) name = method type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f4.setMethod("get");
                // /html/taglib/portlet/icon_options/page.jsp(128,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ficon_005f4.setUrl("javascript:;");
                int _jspx_eval_liferay_002dui_005ficon_005f4 = _jspx_th_liferay_002dui_005ficon_005f4.doStartTag();
                if (_jspx_th_liferay_002dui_005ficon_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                out.write("\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
              out.write('\n');
              out.write('	');
              out.write('	');
            }
            if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
            out.write('\n');
            out.write('	');
          }
          if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
          out.write('\n');
          int evalDoAfterBody = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowArrow_005fmessage_005ficon_005fextended_005fdirection_005fcssClass_005falign.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowArrow_005fmessage_005ficon_005fextended_005fdirection_005fcssClass_005falign.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
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

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002drefresh_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-refresh
    com.liferay.taglib.portletext.IconRefreshTag _jspx_th_liferay_002dportlet_005ficon_002drefresh_005f0 = (com.liferay.taglib.portletext.IconRefreshTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002drefresh_005fnobody.get(com.liferay.taglib.portletext.IconRefreshTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002drefresh_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002drefresh_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002drefresh_005f0 = _jspx_th_liferay_002dportlet_005ficon_002drefresh_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002drefresh_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002drefresh_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002drefresh_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002drefresh_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002drefresh_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-portlet-css
    com.liferay.taglib.portletext.IconPortletCssTag _jspx_th_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0 = (com.liferay.taglib.portletext.IconPortletCssTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_002dcss_005fnobody.get(com.liferay.taglib.portletext.IconPortletCssTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_002dcss_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_002dcss_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_002dcss_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dconfiguration_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-configuration
    com.liferay.taglib.portletext.IconConfigurationTag _jspx_th_liferay_002dportlet_005ficon_002dconfiguration_005f0 = (com.liferay.taglib.portletext.IconConfigurationTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dconfiguration_005fnobody.get(com.liferay.taglib.portletext.IconConfigurationTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dconfiguration_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dconfiguration_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dconfiguration_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dconfiguration_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dconfiguration_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dconfiguration_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dconfiguration_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dconfiguration_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dconfiguration_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dedit_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-edit
    com.liferay.taglib.portletext.IconEditTag _jspx_th_liferay_002dportlet_005ficon_002dedit_005f0 = (com.liferay.taglib.portletext.IconEditTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_005fnobody.get(com.liferay.taglib.portletext.IconEditTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dedit_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dedit_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dedit_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dedit_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dedit_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dedit_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dedit_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-edit-defaults
    com.liferay.taglib.portletext.IconEditDefaultsTag _jspx_th_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0 = (com.liferay.taglib.portletext.IconEditDefaultsTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002ddefaults_005fnobody.get(com.liferay.taglib.portletext.IconEditDefaultsTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002ddefaults_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002ddefaults_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dedit_002ddefaults_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dedit_002dguest_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-edit-guest
    com.liferay.taglib.portletext.IconEditGuestTag _jspx_th_liferay_002dportlet_005ficon_002dedit_002dguest_005f0 = (com.liferay.taglib.portletext.IconEditGuestTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002dguest_005fnobody.get(com.liferay.taglib.portletext.IconEditGuestTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dedit_002dguest_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dedit_002dguest_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dedit_002dguest_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dedit_002dguest_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dedit_002dguest_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002dguest_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dedit_002dguest_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dedit_002dguest_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dedit_002dguest_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dexport_002dimport_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-export-import
    com.liferay.taglib.portletext.IconExportImportTag _jspx_th_liferay_002dportlet_005ficon_002dexport_002dimport_005f0 = (com.liferay.taglib.portletext.IconExportImportTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dexport_002dimport_005fnobody.get(com.liferay.taglib.portletext.IconExportImportTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dexport_002dimport_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dexport_002dimport_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dexport_002dimport_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dexport_002dimport_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dexport_002dimport_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dexport_002dimport_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dexport_002dimport_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dexport_002dimport_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dexport_002dimport_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dhelp_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-help
    com.liferay.taglib.portletext.IconHelpTag _jspx_th_liferay_002dportlet_005ficon_002dhelp_005f0 = (com.liferay.taglib.portletext.IconHelpTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dhelp_005fnobody.get(com.liferay.taglib.portletext.IconHelpTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dhelp_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dhelp_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dhelp_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dhelp_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dhelp_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dhelp_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dhelp_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dhelp_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dhelp_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002dprint_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-print
    com.liferay.taglib.portletext.IconPrintTag _jspx_th_liferay_002dportlet_005ficon_002dprint_005f0 = (com.liferay.taglib.portletext.IconPrintTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dprint_005fnobody.get(com.liferay.taglib.portletext.IconPrintTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002dprint_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002dprint_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    int _jspx_eval_liferay_002dportlet_005ficon_002dprint_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dprint_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002dprint_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dprint_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dprint_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dprint_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dprint_005f0);
    return false;
  }
}
