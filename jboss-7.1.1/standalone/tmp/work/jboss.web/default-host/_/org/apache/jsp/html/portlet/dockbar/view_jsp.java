package org.apache.jsp.html.portlet.dockbar;

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

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(6);
    _jspx_dependants.add("/html/portlet/dockbar/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/dockbar/init-ext.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fid_005fhref_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fuseNamespace_005ftype_005fname_005flabel_005finputCssClass_005fid_005fhelpMessage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmy_002dsites_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005fhref_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005fhref;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fcssClass_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fid_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse_005fposition;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fid_005fhref_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fuseNamespace_005ftype_005fname_005flabel_005finputCssClass_005fid_005fhelpMessage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmy_002dsites_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005fhref_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fcssClass_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fid_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse_005fposition = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fid_005fhref_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fuseNamespace_005ftype_005fname_005flabel_005finputCssClass_005fid_005fhelpMessage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmy_002dsites_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005fhref_005fcssClass.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fcssClass_005faction.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fid_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse_005fposition.release();
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
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');

Group group = null;
LayoutSet layoutSet = null;

if (layout != null) {
	group = layout.getGroup();
	layoutSet = layout.getLayoutSet();
}

List<Portlet> portlets = new ArrayList<Portlet>();

for (String portletId : PropsValues.DOCKBAR_ADD_PORTLETS) {
	Portlet portlet = PortletLocalServiceUtil.getPortletById(portletId);

	if ((portlet != null) && portlet.isInclude() && portlet.isActive() && PortletPermissionUtil.contains(permissionChecker, layout, portlet, ActionKeys.ADD_TO_PAGE)) {
		portlets.add(portlet);
	}
}

boolean hasLayoutCustomizePermission = LayoutPermissionUtil.contains(permissionChecker, layout, ActionKeys.CUSTOMIZE);
boolean hasLayoutUpdatePermission = LayoutPermissionUtil.contains(permissionChecker, layout, ActionKeys.UPDATE);

      out.write("\n");
      out.write("\n");
      out.write("<div class=\"dockbar\" data-namespace=\"");
      if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_page_context))
        return;
      out.write("\" id=\"dockbar\">\n");
      out.write("\t<ul class=\"admin-toolbar aui-toolbar\">\n");
      out.write("\t\t<li class=\"pin-dockbar\">\n");
      out.write("\t\t\t<a href=\"javascript:;\"><img alt='");
      if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("' src=\"");
      out.print( HtmlUtil.escape(themeDisplay.getPathThemeImages()) );
      out.write("/spacer.png\" /></a>\n");
      out.write("\t\t</li>\n");
      out.write("\n");
      out.write("\t\t");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f0 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /html/portlet/dockbar/view.jsp(48,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f0.setTest( !group.isControlPanel() && (!group.hasStagingGroup() || group.isStagingGroup()) && (GroupPermissionUtil.contains(permissionChecker, group.getGroupId(), ActionKeys.ADD_LAYOUT) || hasLayoutUpdatePermission || (layoutTypePortlet.isCustomizable() && layoutTypePortlet.isCustomizedView() && hasLayoutCustomizePermission)) );
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t<li class=\"add-content has-submenu\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return;
        out.write("addContent\">\n");
        out.write("\t\t\t\t<a class=\"menu-button\" href=\"javascript:;\">\n");
        out.write("\t\t\t\t\t<span>\n");
        out.write("\t\t\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return;
        out.write("\n");
        out.write("\t\t\t\t\t</span>\n");
        out.write("\t\t\t\t</a>\n");
        out.write("\n");
        out.write("\t\t\t\t<div class=\"aui-menu add-content-menu aui-overlaycontext-hidden\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return;
        out.write("addContentContainer\">\n");
        out.write("\t\t\t\t\t<div class=\"aui-menu-content\">\n");
        out.write("\t\t\t\t\t\t<ul>\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f1 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
        // /html/portlet/dockbar/view.jsp(59,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f1.setTest( GroupPermissionUtil.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_LAYOUT) && !group.isLayoutPrototype() );
        int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
        if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class=\"first add-page\">\n");
          out.write("\t\t\t\t\t\t\t\t\t<a href=\"javascript:;\" id=\"addPage\">\n");
          out.write("\t\t\t\t\t\t\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fif_005f1, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t</a>\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f2 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
        // /html/portlet/dockbar/view.jsp(67,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f2.setTest( !themeDisplay.isStateMaximized() && layout.isTypePortlet() && !layout.isLayoutPrototypeLinkActive() );
        int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
        if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class=\"last common-items\">\n");
          out.write("\t\t\t\t\t\t\t\t\t<div class=\"aui-menugroup\">\n");
          out.write("\t\t\t\t\t\t\t\t\t\t<div class=\"aui-menugroup-content\">\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f3 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
          // /html/portlet/dockbar/view.jsp(71,11) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f3.setTest( hasLayoutUpdatePermission || (layoutTypePortlet.isCustomizable() && layoutTypePortlet.isCustomizedView() && hasLayoutCustomizePermission) );
          int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
          if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t<span class=\"aui-menu-label\">");
            if (_jspx_meth_liferay_002dui_005fmessage_005f3(_jspx_th_c_005fif_005f3, _jspx_page_context))
              return;
            out.write("</span>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t<ul>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");

													Set<String> runtimePortletIds = (Set<String>)request.getAttribute(WebKeys.RUNTIME_PORTLET_IDS);

													int j = 0;

													for (int i = 0; i < portlets.size(); i++) {
														Portlet portlet = portlets.get(i);

														boolean portletInstanceable = portlet.isInstanceable();

														boolean portletUsed = layoutTypePortlet.hasPortletId(portlet.getPortletId());

														if (runtimePortletIds != null) {
															for (String runtimePortletId : runtimePortletIds) {
																String portletId = portlet.getPortletId();

																if (runtimePortletId.equals(portletId) ||
																	runtimePortletId.startsWith(portletId.concat(PortletConstants.INSTANCE_SEPARATOR))) {

																	portletUsed = true;
																}
															}
														}

														boolean portletLocked = (!portletInstanceable && portletUsed);

														if (!PortletPermissionUtil.contains(permissionChecker, layout, portlet.getPortletId(), ActionKeys.ADD_TO_PAGE)) {
															continue;
														}
													
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li class=\"");
            out.print( (j == 0) ? "first" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<a class=\"app-shortcut ");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f4 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
            // /html/portlet/dockbar/view.jsp(108,38) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f4.setTest( portletLocked );
            int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
            if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("lfr-portlet-used");
            }
            if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
            out.write(' ');
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f5 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
            // /html/portlet/dockbar/view.jsp(108,96) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f5.setTest( portletInstanceable );
            int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
            if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("lfr-instanceable");
            }
            if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
            out.write("\" data-portlet-id=\"");
            out.print( portlet.getPortletId() );
            out.write("\" href=\"javascript:;\" ");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f6 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
            // /html/portlet/dockbar/view.jsp(108,229) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f6.setTest( portletLocked );
            int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
            if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("tabIndex=\"-1\"");
            }
            if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
            out.write(">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            //  liferay-portlet:icon-portlet
            com.liferay.taglib.portletext.IconPortletTag _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0 = (com.liferay.taglib.portletext.IconPortletTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.get(com.liferay.taglib.portletext.IconPortletTag.class);
            _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setPageContext(_jspx_page_context);
            _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
            // /html/portlet/dockbar/view.jsp(109,16) name = portlet type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setPortlet( portlet );
            int _jspx_eval_liferay_002dportlet_005ficon_002dportlet_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.doStartTag();
            if (_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0);
              return;
            }
            _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0);
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            out.print( PortalUtil.getPortletTitle(portlet.getPortletId(), locale) );
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</a>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");

														j++;
													}
													
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t<li class=\"add-application last more-applications\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t<a href=\"javascript:;\" id=\"");
            if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_c_005fif_005f3, _jspx_page_context))
              return;
            out.write("addApplication\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fif_005f3, _jspx_page_context))
              return;
            out.write("&hellip;\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t</a>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t</li>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");
          }
          if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t\t</div>\n");
          out.write("\t\t\t\t\t\t\t\t\t</div>\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
        out.write("\n");
        out.write("\t\t\t\t\t\t</ul>\n");
        out.write("\t\t\t\t\t</div>\n");
        out.write("\t\t\t\t</div>\n");
        out.write("\t\t\t</li>\n");
        out.write("\t\t");
      }
      if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
      out.write("\n");
      out.write("\n");
      out.write("\t\t");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f7 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f7.setParent(null);
      // /html/portlet/dockbar/view.jsp(137,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f7.setTest( !group.isControlPanel() && (themeDisplay.isShowLayoutTemplatesIcon() || themeDisplay.isShowManageSiteMembershipsIcon() || themeDisplay.isShowPageSettingsIcon() || themeDisplay.isShowSiteContentIcon() || themeDisplay.isShowSiteMapSettingsIcon() || themeDisplay.isShowSiteSettingsIcon()) );
      int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
      if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t<li class=\"manage-content has-submenu\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_c_005fif_005f7, _jspx_page_context))
          return;
        out.write("manageContent\">\n");
        out.write("\t\t\t\t<a class=\"menu-button\" href=\"javascript:;\">\n");
        out.write("\t\t\t\t\t<span>\n");
        out.write("\t\t\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_c_005fif_005f7, _jspx_page_context))
          return;
        out.write("\n");
        out.write("\t\t\t\t\t</span>\n");
        out.write("\t\t\t\t</a>\n");
        out.write("\n");
        out.write("\t\t\t\t<div class=\"aui-menu manage-content-menu aui-overlaycontext-hidden\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_c_005fif_005f7, _jspx_page_context))
          return;
        out.write("manageContentContainer\">\n");
        out.write("\t\t\t\t\t<div class=\"aui-menu-content\">\n");
        out.write("\t\t\t\t\t\t<ul>\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");

							String useDialogFullDialog = StringPool.BLANK;

							if (PropsValues.DOCKBAR_ADMINISTRATIVE_LINKS_SHOW_IN_POP_UP) {
								useDialogFullDialog = " use-dialog full-dialog";
							}
							
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f8 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(157,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f8.setTest( themeDisplay.isShowPageSettingsIcon() );
        int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
        if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class='");
          out.print( "first manage-page" + useDialogFullDialog );
          out.write("'>\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f0 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
          // /html/portlet/dockbar/view.jsp(159,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f0.setHref( themeDisplay.getURLPageSettings().toString() + "#details" );
          // /html/portlet/dockbar/view.jsp(159,9) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f0.setLabel("page");
          // /html/portlet/dockbar/view.jsp(159,9) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f0.setTitle("manage-page");
          int _jspx_eval_aui_005fa_005f0 = _jspx_th_aui_005fa_005f0.doStartTag();
          if (_jspx_th_aui_005fa_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f0);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f9 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(163,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f9.setTest( themeDisplay.isShowLayoutTemplatesIcon() );
        int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
        if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class='");
          out.print( "page-layout" + useDialogFullDialog );
          out.write("'>\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f1 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f1.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
          // /html/portlet/dockbar/view.jsp(165,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f1.setHref( themeDisplay.getURLPageSettings().toString() + "#layout" );
          // /html/portlet/dockbar/view.jsp(165,9) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f1.setLabel("page-layout");
          // /html/portlet/dockbar/view.jsp(165,9) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f1.setTitle("manage-page");
          int _jspx_eval_aui_005fa_005f1 = _jspx_th_aui_005fa_005f1.doStartTag();
          if (_jspx_th_aui_005fa_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f1);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f1);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f10 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(169,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f10.setTest( themeDisplay.isShowPageCustomizationIcon() && !themeDisplay.isStateMaximized() );
        int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
        if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class=\"manage-page-customization\">\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f2 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fid_005fhref_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f2.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
          // /html/portlet/dockbar/view.jsp(171,9) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f2.setCssClass( themeDisplay.isFreeformLayout() ? "disabled" : StringPool.BLANK );
          // /html/portlet/dockbar/view.jsp(171,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f2.setHref( themeDisplay.isFreeformLayout() ? null : "javascript:;" );
          // /html/portlet/dockbar/view.jsp(171,9) name = id type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f2.setId("manageCustomization");
          // /html/portlet/dockbar/view.jsp(171,9) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f2.setLabel( group.isLayoutPrototype() ? "page-modifications" : "page-customizations" );
          // /html/portlet/dockbar/view.jsp(171,9) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f2.setTitle( themeDisplay.isFreeformLayout() ? "it-is-not-possible-to-specify-customization-settings-for-freeform-layouts" : null );
          int _jspx_eval_aui_005fa_005f2 = _jspx_th_aui_005fa_005f2.doStartTag();
          if (_jspx_th_aui_005fa_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fid_005fhref_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fa_005f2);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fid_005fhref_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fa_005f2);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f11 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(175,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f11.setTest( themeDisplay.isShowSiteSettingsIcon() );
        int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
        if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class='");
          out.print( "settings" + useDialogFullDialog );
          out.write("'>\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f3 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f3.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
          // /html/portlet/dockbar/view.jsp(177,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f3.setHref( themeDisplay.getURLSiteSettings().toString() );
          // /html/portlet/dockbar/view.jsp(177,9) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f3.setLabel("site-settings");
          // /html/portlet/dockbar/view.jsp(177,9) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f3.setTitle("edit-site-settings");
          int _jspx_eval_aui_005fa_005f3 = _jspx_th_aui_005fa_005f3.doStartTag();
          if (_jspx_th_aui_005fa_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f3);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f3);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f12 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(181,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f12.setTest( themeDisplay.isShowSiteMapSettingsIcon() );
        int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
        if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class='");
          out.print( "sitemap" + useDialogFullDialog );
          out.write("'>\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f4 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f4.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
          // /html/portlet/dockbar/view.jsp(183,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f4.setHref( themeDisplay.getURLSiteMapSettings().toString() );
          // /html/portlet/dockbar/view.jsp(183,9) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f4.setLabel("site-pages");
          // /html/portlet/dockbar/view.jsp(183,9) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f4.setTitle("manage-site-pages");
          int _jspx_eval_aui_005fa_005f4 = _jspx_th_aui_005fa_005f4.doStartTag();
          if (_jspx_th_aui_005fa_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f4);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f4);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f13 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(187,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f13.setTest( themeDisplay.isShowManageSiteMembershipsIcon() );
        int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
        if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class='");
          out.print( "manage-site-memberships" + useDialogFullDialog );
          out.write("'>\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f5 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f5.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
          // /html/portlet/dockbar/view.jsp(189,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f5.setHref( themeDisplay.getURLManageSiteMemberships().toString() );
          // /html/portlet/dockbar/view.jsp(189,9) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f5.setLabel("site-memberships");
          // /html/portlet/dockbar/view.jsp(189,9) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f5.setTitle("manage-site-memberships");
          int _jspx_eval_aui_005fa_005f5 = _jspx_th_aui_005fa_005f5.doStartTag();
          if (_jspx_th_aui_005fa_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f5);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f5);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f14 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(193,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f14.setTest( themeDisplay.isShowSiteContentIcon() );
        int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
        if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class='");
          out.print( "manage-site-content" + useDialogFullDialog );
          out.write("'>\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f6 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f6.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
          // /html/portlet/dockbar/view.jsp(195,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f6.setHref( themeDisplay.getURLSiteContent() );
          // /html/portlet/dockbar/view.jsp(195,9) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f6.setLabel("site-content");
          // /html/portlet/dockbar/view.jsp(195,9) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f6.setTitle("manage-site-content");
          int _jspx_eval_aui_005fa_005f6 = _jspx_th_aui_005fa_005f6.doStartTag();
          if (_jspx_th_aui_005fa_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f6);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f6);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
        out.write("\n");
        out.write("\t\t\t\t\t\t</ul>\n");
        out.write("\t\t\t\t\t</div>\n");
        out.write("\t\t\t\t</div>\n");
        out.write("\t\t\t</li>\n");
        out.write("\n");
        out.write("\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f15 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
        // /html/portlet/dockbar/view.jsp(203,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f15.setTest( themeDisplay.isShowPageCustomizationIcon() );
        int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
        if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t<div class=\"aui-helper-hidden layout-customizable-controls\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_c_005fif_005f15, _jspx_page_context))
            return;
          out.write("layout-customizable-controls\">\n");
          out.write("\t\t\t\t\t<span title='");
          if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fif_005f15, _jspx_page_context))
            return;
          out.write("'>\n");
          out.write("\t\t\t\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fuseNamespace_005ftype_005fname_005flabel_005finputCssClass_005fid_005fhelpMessage_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
          // /html/portlet/dockbar/view.jsp(206,6) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005finput_005f0.setHelpMessage( group.isLayoutPrototype() ? "modifiable-help" : "customizable-help" );
          // /html/portlet/dockbar/view.jsp(206,6) name = id type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005finput_005f0.setId("TypeSettingsProperties--[COLUMN_ID]-customizable--");
          // /html/portlet/dockbar/view.jsp(206,6) name = inputCssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005finput_005f0.setInputCssClass("layout-customizable-checkbox");
          // /html/portlet/dockbar/view.jsp(206,6) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005finput_005f0.setLabel( (group.isLayoutSetPrototype() || group.isLayoutPrototype()) ? "modifiable" : "customizable" );
          // /html/portlet/dockbar/view.jsp(206,6) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005finput_005f0.setName("TypeSettingsProperties--[COLUMN_ID]-customizable--");
          // /html/portlet/dockbar/view.jsp(206,6) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005finput_005f0.setType("checkbox");
          // /html/portlet/dockbar/view.jsp(206,6) name = useNamespace type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005finput_005f0.setUseNamespace( false );
          int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
          if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fuseNamespace_005ftype_005fname_005flabel_005finputCssClass_005fid_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fuseNamespace_005ftype_005fname_005flabel_005finputCssClass_005fid_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
          out.write("\n");
          out.write("\t\t\t\t\t</span>\n");
          out.write("\t\t\t\t</div>\n");
          out.write("\t\t\t");
        }
        if (_jspx_th_c_005fif_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
        out.write('\n');
        out.write('	');
        out.write('	');
      }
      if (_jspx_th_c_005fif_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
      out.write("\n");
      out.write("\n");
      out.write("\t\t<li class=\"aui-toolbar-separator\">\n");
      out.write("\t\t\t<span></span>\n");
      out.write("\t\t</li>\n");
      out.write("\n");
      out.write("\t\t");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f16 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f16.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f16.setParent(null);
      // /html/portlet/dockbar/view.jsp(216,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f16.setTest( !group.isControlPanel() && (!group.hasStagingGroup() || group.isStagingGroup()) && (hasLayoutUpdatePermission || (layoutTypePortlet.isCustomizable() && layoutTypePortlet.isCustomizedView() && hasLayoutCustomizePermission) || PortletPermissionUtil.hasConfigurationPermission(permissionChecker, themeDisplay.getParentGroupId(), layout, ActionKeys.CONFIGURATION)) );
      int _jspx_eval_c_005fif_005f16 = _jspx_th_c_005fif_005f16.doStartTag();
      if (_jspx_eval_c_005fif_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t<li class=\"toggle-controls\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_c_005fif_005f16, _jspx_page_context))
          return;
        out.write("toggleControls\">\n");
        out.write("\t\t\t\t<a href=\"javascript:;\">\n");
        out.write("\t\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_c_005fif_005f16, _jspx_page_context))
          return;
        out.write("\n");
        out.write("\t\t\t\t</a>\n");
        out.write("\t\t\t</li>\n");
        out.write("\t\t");
      }
      if (_jspx_th_c_005fif_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
      out.write("\n");
      out.write("\n");
      out.write("\t\t");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f17 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f17.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f17.setParent(null);
      // /html/portlet/dockbar/view.jsp(224,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f17.setTest( group.isControlPanel() );
      int _jspx_eval_c_005fif_005f17 = _jspx_th_c_005fif_005f17.doStartTag();
      if (_jspx_eval_c_005fif_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t");

			String refererGroupDescriptiveName = null;
			String backURL = null;

			if (themeDisplay.getRefererPlid() > 0) {
				Layout refererLayout = LayoutLocalServiceUtil.fetchLayout(themeDisplay.getRefererPlid());

				if (refererLayout != null) {
					Group refererGroup = refererLayout.getGroup();

					if (refererGroup.isUserGroup() && (themeDisplay.getRefererGroupId() > 0)) {
						refererGroup = GroupLocalServiceUtil.getGroup(themeDisplay.getRefererGroupId());

						refererLayout = new VirtualLayout(refererLayout, refererGroup);
					}

					refererGroupDescriptiveName = refererGroup.getDescriptiveName(locale);

					if (refererGroup.isUser() && (refererGroup.getClassPK() == user.getUserId())) {
						if (refererLayout.isPublicLayout()) {
							refererGroupDescriptiveName = LanguageUtil.get(pageContext, "my-public-pages");
						}
						else {
							refererGroupDescriptiveName = LanguageUtil.get(pageContext, "my-private-pages");
						}
					}

					backURL = PortalUtil.getLayoutURL(refererLayout, themeDisplay);

					if (!CookieKeys.hasSessionId(request)) {
						backURL = PortalUtil.getURLWithSessionId(backURL, session.getId());
					}
				}
			}

			if (Validator.isNull(refererGroupDescriptiveName) || Validator.isNull(backURL)) {
				refererGroupDescriptiveName = themeDisplay.getAccount().getName();
				backURL = themeDisplay.getURLHome();
			}

			if (Validator.isNotNull(themeDisplay.getDoAsUserId())) {
				backURL = HttpUtil.addParameter(backURL, "doAsUserId", themeDisplay.getDoAsUserId());
			}

			if (Validator.isNotNull(themeDisplay.getDoAsUserLanguageId())) {
				backURL = HttpUtil.addParameter(backURL, "doAsUserLanguageId", themeDisplay.getDoAsUserLanguageId());
			}
			
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t<li class=\"back-link\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_c_005fif_005f17, _jspx_page_context))
          return;
        out.write("backLink\">\n");
        out.write("\t\t\t\t<a class=\"portlet-icon-back nobr\" href=\"");
        out.print( PortalUtil.escapeRedirect(backURL) );
        out.write("\">\n");
        out.write("\t\t\t\t\t");
        out.print( LanguageUtil.format(pageContext, "back-to-x", HtmlUtil.escape(refererGroupDescriptiveName), false) );
        out.write("\n");
        out.write("\t\t\t\t</a>\n");
        out.write("\t\t\t</li>\n");
        out.write("\t\t");
      }
      if (_jspx_th_c_005fif_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
      out.write("\n");
      out.write("\t</ul>\n");
      out.write("\n");
      out.write("\t<ul class=\"aui-toolbar user-toolbar\">\n");
      out.write("\t\t");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f18 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f18.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f18.setParent(null);
      // /html/portlet/dockbar/view.jsp(284,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f18.setTest( user.hasMySites() );
      int _jspx_eval_c_005fif_005f18 = _jspx_th_c_005fif_005f18.doStartTag();
      if (_jspx_eval_c_005fif_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t<li class=\"my-sites has-submenu\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return;
        out.write("mySites\">\n");
        out.write("\t\t\t\t<a class=\"menu-button\" href=\"javascript:;\">\n");
        out.write("\t\t\t\t\t<span>\n");
        out.write("\t\t\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f8(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return;
        out.write("\n");
        out.write("\t\t\t\t\t</span>\n");
        out.write("\t\t\t\t</a>\n");
        out.write("\n");
        out.write("\t\t\t\t<div class=\"aui-menu my-sites-menu aui-overlaycontext-hidden\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f10(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return;
        out.write("mySitesContainer\">\n");
        out.write("\t\t\t\t\t<div class=\"aui-menu-content\">\n");
        out.write("\t\t\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmy_002dsites_005f0(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return;
        out.write("\n");
        out.write("\t\t\t\t\t</div>\n");
        out.write("\t\t\t\t</div>\n");
        out.write("\t\t\t</li>\n");
        out.write("\t\t");
      }
      if (_jspx_th_c_005fif_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
      out.write("\n");
      out.write("\n");
      out.write("\t\t<li class=\"aui-toolbar-separator\">\n");
      out.write("\t\t\t<span></span>\n");
      out.write("\t\t</li>\n");
      out.write("\n");
      out.write("\t\t<li class=\"user-avatar ");
      out.print( themeDisplay.isImpersonated() ? "impersonating-user has-submenu" : "" );
      out.write("\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f11(_jspx_page_context))
        return;
      out.write("userAvatar\">\n");
      out.write("\t\t\t<span class=\"user-links ");
      out.print( themeDisplay.isImpersonated() ? "menu-button": "" );
      out.write("\">\n");
      out.write("\n");
      out.write("\t\t\t\t");

				String useDialog = StringPool.BLANK;

				if (!group.isControlPanel() && PropsValues.DOCKBAR_ADMINISTRATIVE_LINKS_SHOW_IN_POP_UP) {
					useDialog = StringPool.SPACE + "use-dialog";
				}

				String controlPanelCategory = StringPool.BLANK;

				if (!group.isControlPanel()) {
					controlPanelCategory = PortletCategoryKeys.MY;
				}

				String myAccountURL = themeDisplay.getURLMyAccount().toString();

				myAccountURL = HttpUtil.setParameter(myAccountURL, "controlPanelCategory", controlPanelCategory);
				
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t");
      //  liferay-util:buffer
      com.liferay.taglib.util.BufferTag _jspx_th_liferay_002dutil_005fbuffer_005f0 = (com.liferay.taglib.util.BufferTag) _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.get(com.liferay.taglib.util.BufferTag.class);
      _jspx_th_liferay_002dutil_005fbuffer_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dutil_005fbuffer_005f0.setParent(null);
      // /html/portlet/dockbar/view.jsp(325,4) name = var type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dutil_005fbuffer_005f0.setVar("userName");
      int _jspx_eval_liferay_002dutil_005fbuffer_005f0 = _jspx_th_liferay_002dutil_005fbuffer_005f0.doStartTag();
      if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dutil_005fbuffer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dutil_005fbuffer_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\t\t\t\t\t<img alt=\"");
          if (_jspx_meth_liferay_002dui_005fmessage_005f9(_jspx_th_liferay_002dutil_005fbuffer_005f0, _jspx_page_context))
            return;
          out.write("\" src=\"");
          out.print( HtmlUtil.escape(user.getPortraitURL(themeDisplay)) );
          out.write("\" />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<span class=\"user-full-name\">\n");
          out.write("\t\t\t\t\t\t");
          out.print( HtmlUtil.escape(user.getFullName()) );
          out.write("\n");
          out.write("\t\t\t\t\t</span>\n");
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
      java.lang.String userName = null;
      userName = (java.lang.String) _jspx_page_context.findAttribute("userName");
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t");
      //  c:choose
      com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f0.setParent(null);
      int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
      if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t\t\t");
        //  c:when
        com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f0 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
        _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
        // /html/portlet/dockbar/view.jsp(334,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fwhen_005f0.setTest( PortalPermissionUtil.contains(permissionChecker, ActionKeys.VIEW_CONTROL_PANEL) );
        int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
        if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f7 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005fhref_005fcssClass.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f7.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
          // /html/portlet/dockbar/view.jsp(335,6) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f7.setCssClass( "user-portrait" + useDialog );
          // /html/portlet/dockbar/view.jsp(335,6) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f7.setHref( myAccountURL );
          // /html/portlet/dockbar/view.jsp(335,6) name = title type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f7.setTitle("manage-my-account");
          int _jspx_eval_aui_005fa_005f7 = _jspx_th_aui_005fa_005f7.doStartTag();
          if (_jspx_eval_aui_005fa_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");
            out.print( userName );
            out.write("\n");
            out.write("\t\t\t\t\t\t");
          }
          if (_jspx_th_aui_005fa_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005fhref_005fcssClass.reuse(_jspx_th_aui_005fa_005f7);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005ftitle_005fhref_005fcssClass.reuse(_jspx_th_aui_005fa_005f7);
          out.write("\n");
          out.write("\t\t\t\t\t");
        }
        if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
        out.write("\n");
        out.write("\t\t\t\t\t");
        //  c:otherwise
        com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
        _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
        int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
        if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t<span class=\"user-portrait\">\n");
          out.write("\t\t\t\t\t\t\t");
          out.print( userName );
          out.write("\n");
          out.write("\t\t\t\t\t\t</span>\n");
          out.write("\t\t\t\t\t");
        }
        if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
        out.write("\n");
        out.write("\t\t\t\t");
      }
      if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f19 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f19.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f19.setParent(null);
      // /html/portlet/dockbar/view.jsp(346,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f19.setTest( themeDisplay.isShowSignOutIcon() );
      int _jspx_eval_c_005fif_005f19 = _jspx_th_c_005fif_005f19.doStartTag();
      if (_jspx_eval_c_005fif_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t\t\t<span class=\"sign-out\">(");
        //  aui:a
        com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f8 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
        _jspx_th_aui_005fa_005f8.setPageContext(_jspx_page_context);
        _jspx_th_aui_005fa_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f19);
        // /html/portlet/dockbar/view.jsp(347,29) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_aui_005fa_005f8.setHref( themeDisplay.getURLSignOut() );
        // /html/portlet/dockbar/view.jsp(347,29) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_aui_005fa_005f8.setLabel("sign-out");
        int _jspx_eval_aui_005fa_005f8 = _jspx_th_aui_005fa_005f8.doStartTag();
        if (_jspx_th_aui_005fa_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f8);
          return;
        }
        _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f8);
        out.write(")</span>\n");
        out.write("\t\t\t\t");
      }
      if (_jspx_th_c_005fif_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
      out.write("\n");
      out.write("\t\t\t</span>\n");
      out.write("\n");
      out.write("\t\t\t");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f20 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f20.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f20.setParent(null);
      // /html/portlet/dockbar/view.jsp(351,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f20.setTest( themeDisplay.isImpersonated() );
      int _jspx_eval_c_005fif_005f20 = _jspx_th_c_005fif_005f20.doStartTag();
      if (_jspx_eval_c_005fif_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t\t<div class=\"aui-menu impersonation-menu aui-overlaycontext-hidden\" id=\"");
        if (_jspx_meth_portlet_005fnamespace_005f12(_jspx_th_c_005fif_005f20, _jspx_page_context))
          return;
        out.write("userOptionsContainer\">\n");
        out.write("\t\t\t\t\t<div class=\"aui-menu-content\">\n");
        out.write("\t\t\t\t\t\t<div class=\"notice-message portlet-msg-info\">\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:choose
        com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
        _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
        _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f20);
        int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
        if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t");
          //  c:when
          com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f1 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
          // /html/portlet/dockbar/view.jsp(356,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fwhen_005f1.setTest( themeDisplay.isSignedIn() );
          int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
          if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t");
            out.print( LanguageUtil.format(pageContext, "you-are-impersonating-x", new Object[] {HtmlUtil.escape(user.getFullName())}) );
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t");
          }
          if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t");
          if (_jspx_meth_c_005fotherwise_005f1(_jspx_th_c_005fchoose_005f1, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fchoose_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
        out.write("\n");
        out.write("\t\t\t\t\t\t</div>\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t<ul>\n");
        out.write("\t\t\t\t\t\t\t<li>\n");
        out.write("\t\t\t\t\t\t\t\t");
        //  aui:a
        com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f9 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
        _jspx_th_aui_005fa_005f9.setPageContext(_jspx_page_context);
        _jspx_th_aui_005fa_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f20);
        // /html/portlet/dockbar/view.jsp(367,8) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_aui_005fa_005f9.setHref( PortalUtil.getLayoutURL(layout, themeDisplay, false) );
        int _jspx_eval_aui_005fa_005f9 = _jspx_th_aui_005fa_005f9.doStartTag();
        if (_jspx_eval_aui_005fa_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          if (_jspx_meth_liferay_002dui_005fmessage_005f11(_jspx_th_aui_005fa_005f9, _jspx_page_context))
            return;
          out.write(' ');
          out.write('(');
          out.print( HtmlUtil.escape(realUser.getFullName()) );
          out.write(')');
        }
        if (_jspx_th_aui_005fa_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f9);
          return;
        }
        _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f9);
        out.write("\n");
        out.write("\t\t\t\t\t\t\t</li>\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");

							Locale realUserLocale = realUser.getLocale();
							Locale userLocale = user.getLocale();
							
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f21 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f21.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f20);
        // /html/portlet/dockbar/view.jsp(375,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f21.setTest( !realUserLocale.equals(userLocale) );
        int _jspx_eval_c_005fif_005f21 = _jspx_th_c_005fif_005f21.doStartTag();
        if (_jspx_eval_c_005fif_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t");

								String doAsUserLanguageId = null;
								String changeLanguageMessage = null;

								if (locale.getLanguage().equals(realUserLocale.getLanguage()) && locale.getCountry().equals(realUserLocale.getCountry())) {
									doAsUserLanguageId = userLocale.getLanguage() + "_" + userLocale.getCountry();
									changeLanguageMessage = LanguageUtil.format(realUserLocale, "use-x's-preferred-language-(x)", new String[] {HtmlUtil.escape(user.getFullName()), userLocale.getDisplayLanguage(realUserLocale)});
								}
								else {
									doAsUserLanguageId = realUserLocale.getLanguage() + "_" + realUserLocale.getCountry();
									changeLanguageMessage = LanguageUtil.format(realUserLocale, "use-your-preferred-language-(x)", realUserLocale.getDisplayLanguage(realUserLocale));
								}
								
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t<li class=\"current-user-language\">\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:a
          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f10 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
          _jspx_th_aui_005fa_005f10.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fa_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /html/portlet/dockbar/view.jsp(392,9) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fa_005f10.setHref( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsUserLanguageId", doAsUserLanguageId) );
          int _jspx_eval_aui_005fa_005f10 = _jspx_th_aui_005fa_005f10.doStartTag();
          if (_jspx_eval_aui_005fa_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.print( changeLanguageMessage );
          }
          if (_jspx_th_aui_005fa_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f10);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f10);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t</li>\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_c_005fif_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
        out.write("\n");
        out.write("\t\t\t\t\t\t</ul>\n");
        out.write("\t\t\t\t\t</div>\n");
        out.write("\t\t\t\t</div>\n");
        out.write("\t\t\t");
      }
      if (_jspx_th_c_005fif_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
      out.write("\n");
      out.write("\t\t</li>\n");
      out.write("\t</ul>\n");
      out.write("\n");
      out.write("\t<div class=\"dockbar-messages\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f13(_jspx_page_context))
        return;
      out.write("dockbarMessages\">\n");
      out.write("\t\t<div class=\"aui-header\"></div>\n");
      out.write("\n");
      out.write("\t\t<div class=\"aui-body\"></div>\n");
      out.write("\n");
      out.write("\t\t<div class=\"aui-footer\"></div>\n");
      out.write("\t</div>\n");
      out.write("\n");
      out.write("\t");

	List<LayoutPrototype> layoutPrototypes = LayoutPrototypeServiceUtil.search(company.getCompanyId(), Boolean.TRUE, null);
	
      out.write('\n');
      out.write('\n');
      out.write('	');
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f22 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f22.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f22.setParent(null);
      // /html/portlet/dockbar/view.jsp(414,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f22.setTest( !layoutPrototypes.isEmpty() );
      int _jspx_eval_c_005fif_005f22 = _jspx_th_c_005fif_005f22.doStartTag();
      if (_jspx_eval_c_005fif_005f22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t<div class=\"aui-html-template\" id=\"layoutPrototypeTemplate\">\n");
        out.write("\t\t\t<ul>\n");
        out.write("\n");
        out.write("\t\t\t\t");

				for (LayoutPrototype layoutPrototype : layoutPrototypes) {
				
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t<li>\n");
        out.write("\t\t\t\t\t\t<label>\n");
        out.write("\t\t\t\t\t\t\t<a href=\"javascript:;\">\n");
        out.write("\t\t\t\t\t\t\t\t<input name=\"template\" type=\"radio\" value=\"");
        out.print( layoutPrototype.getLayoutPrototypeId() );
        out.write("\" /> ");
        out.print( HtmlUtil.escape(layoutPrototype.getName(user.getLanguageId())) );
        out.write("\n");
        out.write("\t\t\t\t\t\t\t</a>\n");
        out.write("\t\t\t\t\t\t</label>\n");
        out.write("\t\t\t\t\t</li>\n");
        out.write("\n");
        out.write("\t\t\t\t");

				}
				
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t</ul>\n");
        out.write("\t\t</div>\n");
        out.write("\t");
      }
      if (_jspx_th_c_005fif_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f23 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f23.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f23.setParent(null);
      // /html/portlet/dockbar/view.jsp(439,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f23.setTest( (layoutSet != null) && layoutSet.isLayoutSetPrototypeLinkActive() && SitesUtil.isLayoutModifiedSinceLastMerge(layout) && LayoutPermissionUtil.contains(themeDisplay.getPermissionChecker(), layout, ActionKeys.UPDATE) );
      int _jspx_eval_c_005fif_005f23 = _jspx_th_c_005fif_005f23.doStartTag();
      if (_jspx_eval_c_005fif_005f23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t<div class=\"page-customization-bar\">\n");
        out.write("\t\t<img alt=\"\" class=\"customized-icon\" src=\"");
        out.print( themeDisplay.getPathThemeImages() );
        out.write("/common/edit.png\" />\n");
        out.write("\n");
        out.write("\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f12(_jspx_th_c_005fif_005f23, _jspx_page_context))
          return;
        out.write("\n");
        out.write("\n");
        out.write("\t\t");
        //  liferay-portlet:actionURL
        com.liferay.taglib.portlet.ActionURLTag _jspx_th_liferay_002dportlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.get(com.liferay.taglib.portlet.ActionURLTag.class);
        _jspx_th_liferay_002dportlet_005factionURL_005f0.setPageContext(_jspx_page_context);
        _jspx_th_liferay_002dportlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f23);
        // /html/portlet/dockbar/view.jsp(445,2) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dportlet_005factionURL_005f0.setPortletName( PortletKeys.LAYOUTS_ADMIN );
        // /html/portlet/dockbar/view.jsp(445,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dportlet_005factionURL_005f0.setVar("resetPrototypeURL");
        int _jspx_eval_liferay_002dportlet_005factionURL_005f0 = _jspx_th_liferay_002dportlet_005factionURL_005f0.doStartTag();
        if (_jspx_eval_liferay_002dportlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_liferay_002dportlet_005factionURL_005f0, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t");
          //  portlet:param
          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
          _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f0);
          // /html/portlet/dockbar/view.jsp(447,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f1.setName( Constants.CMD );
          // /html/portlet/dockbar/view.jsp(447,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f1.setValue("reset_prototype");
          int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
          if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
          out.write("\n");
          out.write("\t\t\t");
          //  portlet:param
          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
          _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f0);
          // /html/portlet/dockbar/view.jsp(448,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f2.setName("redirect");
          // /html/portlet/dockbar/view.jsp(448,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f2.setValue( PortalUtil.getLayoutURL(themeDisplay) );
          int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
          if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
          out.write("\n");
          out.write("\t\t\t");
          //  portlet:param
          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
          _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f0);
          // /html/portlet/dockbar/view.jsp(449,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f3.setName("groupId");
          // /html/portlet/dockbar/view.jsp(449,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f3.setValue( String.valueOf(themeDisplay.getParentGroupId()) );
          int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
          if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
          out.write('\n');
          out.write('	');
          out.write('	');
        }
        if (_jspx_th_liferay_002dportlet_005factionURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f0);
        java.lang.String resetPrototypeURL = null;
        resetPrototypeURL = (java.lang.String) _jspx_page_context.findAttribute("resetPrototypeURL");
        out.write("\n");
        out.write("\n");
        out.write("\t\t");
        //  aui:form
        com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fcssClass_005faction.get(com.liferay.taglib.aui.FormTag.class);
        _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
        _jspx_th_aui_005fform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f23);
        // /html/portlet/dockbar/view.jsp(452,2) name = action type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_aui_005fform_005f0.setAction( resetPrototypeURL );
        // /html/portlet/dockbar/view.jsp(452,2) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_aui_005fform_005f0.setCssClass("reset-prototype");
        // /html/portlet/dockbar/view.jsp(452,2) name = name type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_aui_005fform_005f0.setName("resetFm");
        int _jspx_eval_aui_005fform_005f0 = _jspx_th_aui_005fform_005f0.doStartTag();
        if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005fbutton_005f0(_jspx_th_aui_005fform_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('	');
          out.write('	');
        }
        if (_jspx_th_aui_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fcssClass_005faction.reuse(_jspx_th_aui_005fform_005f0);
          return;
        }
        _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fcssClass_005faction.reuse(_jspx_th_aui_005fform_005f0);
        out.write("\n");
        out.write("\t</div>\n");
      }
      if (_jspx_th_c_005fif_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
      out.write('\n');
      out.write('\n');
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f24 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f24.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f24.setParent(null);
      // /html/portlet/dockbar/view.jsp(458,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f24.setTest( (!SitesUtil.isLayoutUpdateable(layout) || (layout.isLayoutPrototypeLinkActive() && !group.hasStagingGroup())) && LayoutPermissionUtil.containsWithoutViewableGroup(themeDisplay.getPermissionChecker(), layout, null, false, ActionKeys.UPDATE) );
      int _jspx_eval_c_005fif_005f24 = _jspx_th_c_005fif_005f24.doStartTag();
      if (_jspx_eval_c_005fif_005f24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t<div class=\"page-customization-bar\">\n");
        out.write("\t\t<img alt=\"\" class=\"customized-icon\" src=\"");
        out.print( themeDisplay.getPathThemeImages() );
        out.write("/common/site_icon.png\" />\n");
        out.write("\n");
        out.write("\t\t");
        //  c:choose
        com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
        _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
        _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f24);
        int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
        if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t");
          //  c:when
          com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f2 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
          // /html/portlet/dockbar/view.jsp(463,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fwhen_005f2.setTest( layout.isLayoutPrototypeLinkActive() && !group.hasStagingGroup() );
          int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
          if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_liferay_002dui_005fmessage_005f13(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fwhen_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
          out.write("\n");
          out.write("\t\t\t");
          //  c:when
          com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f3 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
          // /html/portlet/dockbar/view.jsp(466,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fwhen_005f3.setTest( layout instanceof VirtualLayout );
          int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
          if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_liferay_002dui_005fmessage_005f14(_jspx_th_c_005fwhen_005f3, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fwhen_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_c_005fotherwise_005f2(_jspx_th_c_005fchoose_005f2, _jspx_page_context))
            return;
          out.write('\n');
          out.write('	');
          out.write('	');
        }
        if (_jspx_th_c_005fchoose_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
        out.write("\n");
        out.write("\t</div>\n");
      }
      if (_jspx_th_c_005fif_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
      out.write('\n');
      out.write('\n');
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f25 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f25.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f25.setParent(null);
      // /html/portlet/dockbar/view.jsp(476,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f25.setTest( !(group.isLayoutPrototype() || group.isLayoutSetPrototype() || group.isUserGroup()) && layoutTypePortlet.isCustomizable() && LayoutPermissionUtil.containsWithoutViewableGroup(permissionChecker, layout, null, false, ActionKeys.CUSTOMIZE) );
      int _jspx_eval_c_005fif_005f25 = _jspx_th_c_005fif_005f25.doStartTag();
      if (_jspx_eval_c_005fif_005f25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t<div class=\"page-customization-bar\">\n");
        out.write("\t\t<img alt=\"\" class=\"customized-icon\" src=\"");
        out.print( themeDisplay.getPathThemeImages() );
        out.write("/common/guest_icon.png\" />\n");
        out.write("\n");
        out.write("\t\t");
        //  c:choose
        com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
        _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
        _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
        int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
        if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t");
          //  c:when
          com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f4 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
          // /html/portlet/dockbar/view.jsp(481,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fwhen_005f4.setTest( layoutTypePortlet.isCustomizedView() );
          int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
          if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_liferay_002dui_005fmessage_005f16(_jspx_th_c_005fwhen_005f4, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_liferay_002dui_005ficon_002dhelp_005f0(_jspx_th_c_005fwhen_005f4, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fwhen_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
          out.write("\n");
          out.write("\t\t\t");
          //  c:otherwise
          com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
          _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
          int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
          if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_liferay_002dui_005fmessage_005f17(_jspx_th_c_005fotherwise_005f3, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f26 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f26.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
            // /html/portlet/dockbar/view.jsp(489,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f26.setTest( hasLayoutUpdatePermission );
            int _jspx_eval_c_005fif_005f26 = _jspx_th_c_005fif_005f26.doStartTag();
            if (_jspx_eval_c_005fif_005f26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_liferay_002dui_005ficon_002dhelp_005f1(_jspx_th_c_005fif_005f26, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fif_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f26);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f26);
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fotherwise_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
          out.write('\n');
          out.write('	');
          out.write('	');
        }
        if (_jspx_th_c_005fchoose_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
        out.write("\n");
        out.write("\n");
        out.write("\t\t<span class=\"page-customization-actions\">\n");
        out.write("\n");
        out.write("\t\t\t");

			String taglibImage = "search";
			String taglibMessage = "view-default-page";

			if (!layoutTypePortlet.isCustomizedView()) {
				taglibMessage = "view-my-customized-page";
			}
			else if (layoutTypePortlet.isDefaultUpdated()) {
				taglibImage = "activate";
				taglibMessage = "the-defaults-for-the-current-page-have-been-updated-click-here-to-see-them";
			}
			
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t");
        //  liferay-ui:icon
        com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fid_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
        _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
        _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
        // /html/portlet/dockbar/view.jsp(510,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dui_005ficon_005f0.setCssClass( layoutTypePortlet.isCustomizedView() ? StringPool.BLANK : "false" );
        // /html/portlet/dockbar/view.jsp(510,3) name = id type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dui_005ficon_005f0.setId("toggleCustomizedView");
        // /html/portlet/dockbar/view.jsp(510,3) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dui_005ficon_005f0.setImage( taglibImage );
        // /html/portlet/dockbar/view.jsp(510,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dui_005ficon_005f0.setLabel( true );
        // /html/portlet/dockbar/view.jsp(510,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dui_005ficon_005f0.setMessage( taglibMessage );
        // /html/portlet/dockbar/view.jsp(510,3) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dui_005ficon_005f0.setUrl("javascript:;");
        int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
        if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fid_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fid_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t");
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f27 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f27.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
        // /html/portlet/dockbar/view.jsp(512,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f27.setTest( layoutTypePortlet.isCustomizedView() );
        int _jspx_eval_c_005fif_005f27 = _jspx_th_c_005fif_005f27.doStartTag();
        if (_jspx_eval_c_005fif_005f27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t");
          //  liferay-portlet:actionURL
          com.liferay.taglib.portlet.ActionURLTag _jspx_th_liferay_002dportlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.get(com.liferay.taglib.portlet.ActionURLTag.class);
          _jspx_th_liferay_002dportlet_005factionURL_005f1.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dportlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f27);
          // /html/portlet/dockbar/view.jsp(513,4) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dportlet_005factionURL_005f1.setPortletName( PortletKeys.LAYOUTS_ADMIN );
          // /html/portlet/dockbar/view.jsp(513,4) name = var type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dportlet_005factionURL_005f1.setVar("resetCustomizationViewURL");
          int _jspx_eval_liferay_002dportlet_005factionURL_005f1 = _jspx_th_liferay_002dportlet_005factionURL_005f1.doStartTag();
          if (_jspx_eval_liferay_002dportlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\t");
            if (_jspx_meth_portlet_005fparam_005f4(_jspx_th_liferay_002dportlet_005factionURL_005f1, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t");
            //  portlet:param
            com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
            _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
            _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f1);
            // /html/portlet/dockbar/view.jsp(515,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_portlet_005fparam_005f5.setName("groupId");
            // /html/portlet/dockbar/view.jsp(515,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_portlet_005fparam_005f5.setValue( String.valueOf(themeDisplay.getParentGroupId()) );
            int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
            if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
              return;
            }
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
            out.write("\n");
            out.write("\t\t\t\t\t");
            //  portlet:param
            com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
            _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
            _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f1);
            // /html/portlet/dockbar/view.jsp(516,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_portlet_005fparam_005f6.setName( Constants.CMD );
            // /html/portlet/dockbar/view.jsp(516,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_portlet_005fparam_005f6.setValue("reset_customized_view");
            int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
            if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
              return;
            }
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
            out.write("\n");
            out.write("\t\t\t\t");
          }
          if (_jspx_th_liferay_002dportlet_005factionURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f1);
          java.lang.String resetCustomizationViewURL = null;
          resetCustomizationViewURL = (java.lang.String) _jspx_page_context.findAttribute("resetCustomizationViewURL");
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t");

				String taglibURL = "javascript:if (confirm('" + UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-reset-your-customizations-to-default") + "')){submitForm(document.hrefFm, '" + HttpUtil.encodeURL(resetCustomizationViewURL) + "');}";
				
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t");
          //  liferay-ui:icon
          com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
          _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f27);
          // /html/portlet/dockbar/view.jsp(523,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dui_005ficon_005f1.setImage("../portlet/refresh");
          // /html/portlet/dockbar/view.jsp(523,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dui_005ficon_005f1.setLabel( true );
          // /html/portlet/dockbar/view.jsp(523,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dui_005ficon_005f1.setMessage("reset-my-customizations");
          // /html/portlet/dockbar/view.jsp(523,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_liferay_002dui_005ficon_005f1.setUrl( taglibURL );
          int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
          if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
          out.write("\n");
          out.write("\t\t\t");
        }
        if (_jspx_th_c_005fif_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f27);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f27);
        out.write("\n");
        out.write("\t\t</span>\n");
        out.write("\t</div>\n");
        out.write("\n");
        out.write("\t");
        //  aui:script
        com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
        _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
        _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
        int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
            out = _jspx_page_context.pushBody();
            _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
            _jspx_th_aui_005fscript_005f0.doInitBody();
          }
          do {
            out.write("\n");
            out.write("\t\tLiferay.provide(\n");
            out.write("\t\t\twindow,\n");
            out.write("\t\t\t'");
            if (_jspx_meth_portlet_005fnamespace_005f14(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
              return;
            out.write("toggleCustomizedView',\n");
            out.write("\t\t\tfunction(event) {\n");
            out.write("\t\t\t\tvar A = AUI();\n");
            out.write("\n");
            out.write("\t\t\t\tA.io.request(\n");
            out.write("\t\t\t\t\tthemeDisplay.getPathMain() + '/portal/update_layout',\n");
            out.write("\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\tdata: {\n");
            out.write("\t\t\t\t\t\t\tcmd: 'toggle_customized_view',\n");
            out.write("\t\t\t\t\t\t\tcustomized_view: '");
            out.print( String.valueOf(!layoutTypePortlet.isCustomizedView()) );
            out.write("',\n");
            out.write("\t\t\t\t\t\t\tp_auth: '");
            out.print( AuthTokenUtil.getToken(request) );
            out.write("'\n");
            out.write("\t\t\t\t\t\t},\n");
            out.write("\t\t\t\t\t\ton: {\n");
            out.write("\t\t\t\t\t\t\tsuccess: function(event, id, obj) {\n");
            out.write("\t\t\t\t\t\t\t\twindow.location.href = themeDisplay.getLayoutURL();\n");
            out.write("\t\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t);\n");
            out.write("\t\t\t},\n");
            out.write("\t\t\t['aui-io-request']\n");
            out.write("\t\t);\n");
            out.write("\t");
            int evalDoAfterBody = _jspx_th_aui_005fscript_005f0.doAfterBody();
            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
              break;
          } while (true);
          if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
            out = _jspx_page_context.popBody();
          }
        }
        if (_jspx_th_aui_005fscript_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
          return;
        }
        _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
        out.write('\n');
        out.write('\n');
        out.write('	');
        if (_jspx_meth_aui_005fscript_005f1(_jspx_th_c_005fif_005f25, _jspx_page_context))
          return;
        out.write('\n');
      }
      if (_jspx_th_c_005fif_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
      out.write('\n');
      out.write('\n');
      if (_jspx_meth_aui_005fscript_005f2(_jspx_page_context))
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

  private boolean _jspx_meth_portlet_005fnamespace_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent(null);
    // /html/portlet/dockbar/view.jsp(45,36) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("pin-the-dockbar");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /html/portlet/dockbar/view.jsp(52,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("add");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f2 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    int _jspx_eval_portlet_005fnamespace_005f2 = _jspx_th_portlet_005fnamespace_005f2.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    // /html/portlet/dockbar/view.jsp(62,10) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("page");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /html/portlet/dockbar/view.jsp(72,41) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f3.setKey("applications");
    int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f3 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    int _jspx_eval_portlet_005fnamespace_005f3 = _jspx_th_portlet_005fnamespace_005f3.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /html/portlet/dockbar/view.jsp(122,15) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("more");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f4 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    int _jspx_eval_portlet_005fnamespace_005f4 = _jspx_th_portlet_005fnamespace_005f4.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    // /html/portlet/dockbar/view.jsp(141,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("manage");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    // /html/portlet/dockbar/view.jsp(205,18) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("customizable-help");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f7 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
    int _jspx_eval_portlet_005fnamespace_005f7 = _jspx_th_portlet_005fnamespace_005f7.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
    // /html/portlet/dockbar/view.jsp(219,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("edit-controls");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f17, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f8 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
    int _jspx_eval_portlet_005fnamespace_005f8 = _jspx_th_portlet_005fnamespace_005f8.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f9 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    int _jspx_eval_portlet_005fnamespace_005f9 = _jspx_th_portlet_005fnamespace_005f9.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    // /html/portlet/dockbar/view.jsp(288,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f8.setKey("go-to");
    int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f10 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    int _jspx_eval_portlet_005fnamespace_005f10 = _jspx_th_portlet_005fnamespace_005f10.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmy_002dsites_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:my-sites
    com.liferay.taglib.ui.MySitesTag _jspx_th_liferay_002dui_005fmy_002dsites_005f0 = (com.liferay.taglib.ui.MySitesTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmy_002dsites_005fnobody.get(com.liferay.taglib.ui.MySitesTag.class);
    _jspx_th_liferay_002dui_005fmy_002dsites_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmy_002dsites_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    int _jspx_eval_liferay_002dui_005fmy_002dsites_005f0 = _jspx_th_liferay_002dui_005fmy_002dsites_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmy_002dsites_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmy_002dsites_005fnobody.reuse(_jspx_th_liferay_002dui_005fmy_002dsites_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmy_002dsites_005fnobody.reuse(_jspx_th_liferay_002dui_005fmy_002dsites_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f11 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f11.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f11 = _jspx_th_portlet_005fnamespace_005f11.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dutil_005fbuffer_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f9 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f0);
    // /html/portlet/dockbar/view.jsp(326,15) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f9.setKey("manage-my-account");
    int _jspx_eval_liferay_002dui_005fmessage_005f9 = _jspx_th_liferay_002dui_005fmessage_005f9.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f20, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f12 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f20);
    int _jspx_eval_portlet_005fnamespace_005f12 = _jspx_th_portlet_005fnamespace_005f12.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
    int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      out.write("\n");
      out.write("\t\t\t\t\t\t\t\t\t");
      if (_jspx_meth_liferay_002dui_005fmessage_005f10(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
        return true;
      out.write("\n");
      out.write("\t\t\t\t\t\t\t\t");
    }
    if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f10 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /html/portlet/dockbar/view.jsp(360,9) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f10.setKey("you-are-impersonating-the-guest-user");
    int _jspx_eval_liferay_002dui_005fmessage_005f10 = _jspx_th_liferay_002dui_005fmessage_005f10.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fa_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f11 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f11.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fa_005f9);
    // /html/portlet/dockbar/view.jsp(367,82) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f11.setKey("be-yourself-again");
    int _jspx_eval_liferay_002dui_005fmessage_005f11 = _jspx_th_liferay_002dui_005fmessage_005f11.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f13(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f13 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f13.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f13 = _jspx_th_portlet_005fnamespace_005f13.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f23, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f12 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f12.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f23);
    // /html/portlet/dockbar/view.jsp(443,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f12.setKey("this-page-has-been-changed-since-the-last-update-from-the-site-template");
    int _jspx_eval_liferay_002dui_005fmessage_005f12 = _jspx_th_liferay_002dui_005fmessage_005f12.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f0);
    // /html/portlet/dockbar/view.jsp(446,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/dockbar/view.jsp(446,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f0.setValue("/layouts_admin/edit_layouts");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
    // /html/portlet/dockbar/view.jsp(453,3) name = name type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fbutton_005f0.setName("submit");
    // /html/portlet/dockbar/view.jsp(453,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fbutton_005f0.setType("submit");
    // /html/portlet/dockbar/view.jsp(453,3) name = value type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fbutton_005f0.setValue("reset");
    int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
    if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f13 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f13.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/portlet/dockbar/view.jsp(464,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f13.setKey("this-page-is-linked-to-a-page-template");
    int _jspx_eval_liferay_002dui_005fmessage_005f13 = _jspx_th_liferay_002dui_005fmessage_005f13.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f14 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f14.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
    // /html/portlet/dockbar/view.jsp(467,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f14.setKey("this-page-belongs-to-a-user-group");
    int _jspx_eval_liferay_002dui_005fmessage_005f14 = _jspx_th_liferay_002dui_005fmessage_005f14.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
    int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      out.write("\n");
      out.write("\t\t\t\t");
      if (_jspx_meth_liferay_002dui_005fmessage_005f15(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
        return true;
      out.write("\n");
      out.write("\t\t\t");
    }
    if (_jspx_th_c_005fotherwise_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f15 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f15.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
    // /html/portlet/dockbar/view.jsp(470,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f15.setKey("this-page-is-linked-to-a-site-template-which-does-not-allow-modifications-to-it");
    int _jspx_eval_liferay_002dui_005fmessage_005f15 = _jspx_th_liferay_002dui_005fmessage_005f15.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f16 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f16.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
    // /html/portlet/dockbar/view.jsp(482,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f16.setKey("you-can-customize-this-page");
    int _jspx_eval_liferay_002dui_005fmessage_005f16 = _jspx_th_liferay_002dui_005fmessage_005f16.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_002dhelp_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon-help
    com.liferay.taglib.ui.IconHelpTag _jspx_th_liferay_002dui_005ficon_002dhelp_005f0 = (com.liferay.taglib.ui.IconHelpTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.get(com.liferay.taglib.ui.IconHelpTag.class);
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
    // /html/portlet/dockbar/view.jsp(484,4) name = message type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.setMessage("customizable-user-help");
    int _jspx_eval_liferay_002dui_005ficon_002dhelp_005f0 = _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_002dhelp_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002dhelp_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002dhelp_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f17 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f17.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
    // /html/portlet/dockbar/view.jsp(487,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f17.setKey("this-is-the-default-page-without-your-customizations");
    int _jspx_eval_liferay_002dui_005fmessage_005f17 = _jspx_th_liferay_002dui_005fmessage_005f17.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f17);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_002dhelp_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f26, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon-help
    com.liferay.taglib.ui.IconHelpTag _jspx_th_liferay_002dui_005ficon_002dhelp_005f1 = (com.liferay.taglib.ui.IconHelpTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.get(com.liferay.taglib.ui.IconHelpTag.class);
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f26);
    // /html/portlet/dockbar/view.jsp(490,5) name = message type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f1.setMessage("customizable-admin-help");
    int _jspx_eval_liferay_002dui_005ficon_002dhelp_005f1 = _jspx_th_liferay_002dui_005ficon_002dhelp_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_002dhelp_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002dhelp_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002dhelp_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005factionURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f1);
    // /html/portlet/dockbar/view.jsp(514,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f4.setName("struts_action");
    // /html/portlet/dockbar/view.jsp(514,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f4.setValue("/layouts_admin/edit_layouts");
    int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
    if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f14 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f14 = _jspx_th_portlet_005fnamespace_005f14.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f25, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
    // /html/portlet/dockbar/view.jsp(555,1) name = use type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fscript_005f1.setUse("aui-base");
    int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
    if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f1.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\tvar toggleCustomizedView = A.one('#");
        if (_jspx_meth_portlet_005fnamespace_005f15(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
          return true;
        out.write("toggleCustomizedView');\n");
        out.write("\n");
        out.write("\t\tif (toggleCustomizedView) {\n");
        out.write("\t\t\ttoggleCustomizedView.on('click', ");
        if (_jspx_meth_portlet_005fnamespace_005f16(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
          return true;
        out.write("toggleCustomizedView);\n");
        out.write("\t\t}\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_aui_005fscript_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fscript_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f15 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f15 = _jspx_th_portlet_005fnamespace_005f15.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f16 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f16.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f16 = _jspx_th_portlet_005fnamespace_005f16.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f2 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse_005fposition.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f2.setParent(null);
    // /html/portlet/dockbar/view.jsp(564,0) name = position type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fscript_005f2.setPosition("inline");
    // /html/portlet/dockbar/view.jsp(564,0) name = use type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fscript_005f2.setUse("liferay-dockbar");
    int _jspx_eval_aui_005fscript_005f2 = _jspx_th_aui_005fscript_005f2.doStartTag();
    if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f2.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tLiferay.Dockbar.init();\n");
        out.write("\n");
        out.write("\tvar customizableColumns = A.all('.portlet-column-content.customizable');\n");
        out.write("\n");
        out.write("\tif (customizableColumns.size() > 0) {\n");
        out.write("\t\tcustomizableColumns.get('parentNode').addClass('customizable');\n");
        out.write("\t}\n");
        int evalDoAfterBody = _jspx_th_aui_005fscript_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fscript_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse_005fposition.reuse(_jspx_th_aui_005fscript_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse_005fposition.reuse(_jspx_th_aui_005fscript_005f2);
    return false;
  }
}
