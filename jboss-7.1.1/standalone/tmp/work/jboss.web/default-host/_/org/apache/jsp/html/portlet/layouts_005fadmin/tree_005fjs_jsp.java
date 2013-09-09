package org.apache.jsp.html.portlet.layouts_005fadmin;

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
import com.liferay.portal.DuplicateLockException;
import com.liferay.portal.ImageTypeException;
import com.liferay.portal.LARFileException;
import com.liferay.portal.LARFileSizeException;
import com.liferay.portal.LARTypeException;
import com.liferay.portal.LayoutFriendlyURLException;
import com.liferay.portal.LayoutImportException;
import com.liferay.portal.LayoutNameException;
import com.liferay.portal.LayoutPrototypeException;
import com.liferay.portal.LayoutTypeException;
import com.liferay.portal.LocaleException;
import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.NoSuchLayoutRevisionException;
import com.liferay.portal.NoSuchLayoutSetBranchException;
import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.RemoteExportException;
import com.liferay.portal.RemoteOptionsException;
import com.liferay.portal.RequiredLayoutException;
import com.liferay.portal.SitemapChangeFrequencyException;
import com.liferay.portal.SitemapIncludeException;
import com.liferay.portal.SitemapPagePriorityException;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerChoice;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.UserIdStrategy;
import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelperUtil;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.messaging.SchedulerResponse;
import com.liferay.portal.kernel.staging.StagingConstants;
import com.liferay.portal.kernel.staging.StagingUtil;
import com.liferay.portal.lar.LayoutExporter;
import com.liferay.portal.plugin.PluginUtil;
import com.liferay.portal.util.LayoutLister;
import com.liferay.portal.util.LayoutView;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.dynamicdatalists.RecordSetDuplicateRecordSetKeyException;
import com.liferay.portlet.dynamicdatamapping.StructureDuplicateStructureKeyException;
import com.liferay.portlet.layoutconfiguration.util.RuntimePortletUtil;
import com.liferay.portlet.layoutsadmin.util.LayoutsTreeUtil;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroup;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroupInstance;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupInstanceServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.permission.MDRPermissionUtil;
import com.liferay.portlet.mobiledevicerules.service.permission.MDRRuleGroupInstancePermissionUtil;
import com.liferay.portlet.mobiledevicerules.util.RuleGroupInstancePriorityComparator;

public final class tree_005fjs_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(7);
    _jspx_dependants.add("/html/portlet/layouts_admin/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/layouts_admin/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/layouts_admin/init_attributes.jspf");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvarImpl_005fcopyCurrentRenderParameters;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvarImpl_005fcopyCurrentRenderParameters = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvarImpl_005fcopyCurrentRenderParameters.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
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

Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');

String tabs1 = ParamUtil.getString(request, "tabs1", "public-pages");

String redirect = ParamUtil.getString(request, "redirect");
String closeRedirect = ParamUtil.getString(request, "closeRedirect");
String backURL = ParamUtil.getString(request, "backURL", redirect);

if (portletName.equals(PortletKeys.LAYOUTS_ADMIN) || portletName.equals(PortletKeys.MY_ACCOUNT)) {
	portletDisplay.setURLBack(backURL);
}

Group selGroup = (Group)request.getAttribute(WebKeys.GROUP);

Group liveGroup = null;
Group stagingGroup = null;

if (selGroup.isStagingGroup()) {
	liveGroup = selGroup.getLiveGroup();
	stagingGroup = selGroup;
}
else {
	liveGroup = selGroup;

	if (selGroup.hasStagingGroup()) {
		stagingGroup = selGroup.getStagingGroup();
	}
}

Group group = null;

if (stagingGroup != null) {
	group = stagingGroup;
}
else {
	group = liveGroup;
}

long groupId = liveGroup.getGroupId();

if (group != null) {
	groupId = group.getGroupId();
}

long liveGroupId = liveGroup.getGroupId();

long stagingGroupId = 0;

if (stagingGroup != null) {
	stagingGroupId = stagingGroup.getGroupId();
}

long selPlid = ParamUtil.getLong(request, "selPlid", LayoutConstants.DEFAULT_PLID);
long refererPlid = ParamUtil.getLong(request, "refererPlid", LayoutConstants.DEFAULT_PLID);

boolean privateLayout = tabs1.equals("private-pages");
long layoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;

UnicodeProperties groupTypeSettings = null;

if (group != null) {
	groupTypeSettings = group.getTypeSettingsProperties();
}
else {
	groupTypeSettings = new UnicodeProperties();
}

UnicodeProperties liveGroupTypeSettings = liveGroup.getTypeSettingsProperties();

Layout selLayout = null;

try {
	if (selPlid != LayoutConstants.DEFAULT_PLID) {
		selLayout = LayoutLocalServiceUtil.getLayout(selPlid);

		privateLayout = selLayout.isPrivateLayout();
		layoutId = selLayout.getLayoutId();
	}
}
catch (NoSuchLayoutException nsle) {
}

Layout refererLayout = null;

try {
	if (refererPlid != LayoutConstants.DEFAULT_PLID) {
		refererLayout = LayoutLocalServiceUtil.getLayout(refererPlid);
	}
}
catch (NoSuchLayoutException nsle) {
}

Organization organization = null;
User selUser = null;
UserGroup userGroup = null;

if (liveGroup.isOrganization()) {
	organization = OrganizationLocalServiceUtil.getOrganization(liveGroup.getOrganizationId());
}
else if (liveGroup.isUser()) {
	selUser = UserLocalServiceUtil.getUserById(liveGroup.getClassPK());
}
else if (liveGroup.isUserGroup()) {
	userGroup = UserGroupLocalServiceUtil.getUserGroup(liveGroup.getClassPK());
}

String tabs1Names = "public-pages,private-pages";

if (liveGroup.isUser()) {
	boolean hasPowerUserRole = RoleLocalServiceUtil.hasUserRole(selUser.getUserId(), company.getCompanyId(), RoleConstants.POWER_USER, true);

	boolean privateLayoutsModifiable = (!PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_POWER_USER_REQUIRED || hasPowerUserRole) && PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_ENABLED;
	boolean publicLayoutsModifiable = (!PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_POWER_USER_REQUIRED || hasPowerUserRole) && PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_ENABLED;

	if (privateLayoutsModifiable && publicLayoutsModifiable) {
		tabs1Names = "public-pages,private-pages";
	}
	else if (privateLayoutsModifiable) {
		tabs1Names = "private-pages";
	}
	else if (publicLayoutsModifiable) {
		tabs1Names = "public-pages";
	}

	if (!publicLayoutsModifiable && privateLayoutsModifiable && !privateLayout) {
		tabs1 = "private-pages";

		privateLayout = true;
	}
}

if (selGroup.isLayoutSetPrototype()) {
	privateLayout = true;
}

LayoutSet selLayoutSet = LayoutSetLocalServiceUtil.getLayoutSet(groupId, privateLayout);

LayoutLister layoutLister = new LayoutLister();

String pagesName = null;

if (liveGroup.isLayoutPrototype() || liveGroup.isLayoutSetPrototype() || liveGroup.isUserGroup()) {
	pagesName = "pages";
}
else if (privateLayout) {
	pagesName = "private-pages";
}
else {
	pagesName = "public-pages";
}

String rootNodeName = LanguageUtil.get(pageContext, pagesName);

LayoutView layoutView = layoutLister.getLayoutView(groupId, privateLayout, rootNodeName, locale);

List layoutList = layoutView.getList();

request.setAttribute(WebKeys.LAYOUT_LISTER_LIST, layoutList);

      out.write('\n');
      out.write('\n');
      //  liferay-portlet:renderURL
      com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl.get(com.liferay.taglib.portlet.RenderURLTag.class);
      _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dportlet_005frenderURL_005f0.setParent(null);
      // /html/portlet/layouts_admin/init_attributes.jspf(176,0) name = varImpl type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dportlet_005frenderURL_005f0.setVarImpl("redirectURL");
      int _jspx_eval_liferay_002dportlet_005frenderURL_005f0 = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doStartTag();
      if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write('\n');
        out.write('	');
        if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_liferay_002dportlet_005frenderURL_005f0, _jspx_page_context))
          return;
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(178,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f1.setName("tabs1");
        // /html/portlet/layouts_admin/init_attributes.jspf(178,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f1.setValue( tabs1 );
        int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
        if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(179,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f2.setName("redirect");
        // /html/portlet/layouts_admin/init_attributes.jspf(179,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f2.setValue( redirect );
        int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
        if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
        out.write('\n');
        out.write('\n');
        out.write('	');
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f0 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(181,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f0.setTest( (portletName.equals(PortletKeys.LAYOUTS_ADMIN) || portletName.equals(PortletKeys.MY_ACCOUNT) || portletName.equals(PortletKeys.USERS_ADMIN)) );
        int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
        if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write('\n');
          out.write('	');
          out.write('	');
          //  portlet:param
          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
          _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
          // /html/portlet/layouts_admin/init_attributes.jspf(182,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f3.setName("backURL");
          // /html/portlet/layouts_admin/init_attributes.jspf(182,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f3.setValue( backURL );
          int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
          if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
          out.write('\n');
          out.write('	');
        }
        if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
        out.write('\n');
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(185,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f4.setName("groupId");
        // /html/portlet/layouts_admin/init_attributes.jspf(185,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f4.setValue( String.valueOf(liveGroupId) );
        int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
        if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
        out.write('\n');
      }
      if (_jspx_th_liferay_002dportlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
      com.liferay.portal.kernel.portlet.LiferayPortletURL redirectURL = null;
      redirectURL = (com.liferay.portal.kernel.portlet.LiferayPortletURL) _jspx_page_context.findAttribute("redirectURL");
      out.write('\n');
      out.write('\n');
      //  liferay-portlet:resourceURL
      com.liferay.taglib.portlet.ResourceURLTag _jspx_th_liferay_002dportlet_005fresourceURL_005f0 = (com.liferay.taglib.portlet.ResourceURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvarImpl_005fcopyCurrentRenderParameters.get(com.liferay.taglib.portlet.ResourceURLTag.class);
      _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setParent(null);
      // /html/portlet/layouts_admin/init_attributes.jspf(188,0) name = copyCurrentRenderParameters type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setCopyCurrentRenderParameters( false );
      // /html/portlet/layouts_admin/init_attributes.jspf(188,0) name = varImpl type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setVarImpl("portletURL");
      int _jspx_eval_liferay_002dportlet_005fresourceURL_005f0 = _jspx_th_liferay_002dportlet_005fresourceURL_005f0.doStartTag();
      if (_jspx_eval_liferay_002dportlet_005fresourceURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write('\n');
        out.write('	');
        if (_jspx_meth_portlet_005fparam_005f5(_jspx_th_liferay_002dportlet_005fresourceURL_005f0, _jspx_page_context))
          return;
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(190,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f6.setName("tabs1");
        // /html/portlet/layouts_admin/init_attributes.jspf(190,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f6.setValue( tabs1 );
        int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
        if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(191,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f7.setName("redirect");
        // /html/portlet/layouts_admin/init_attributes.jspf(191,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f7.setValue( redirect );
        int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
        if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(192,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f8.setName("closeRedirect");
        // /html/portlet/layouts_admin/init_attributes.jspf(192,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f8.setValue( closeRedirect );
        int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
        if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
        out.write('\n');
        out.write('\n');
        out.write('	');
        //  c:if
        com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f1 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
        _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(194,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fif_005f1.setTest( (portletName.equals(PortletKeys.LAYOUTS_ADMIN) || portletName.equals(PortletKeys.MY_ACCOUNT) || portletName.equals(PortletKeys.USERS_ADMIN)) );
        int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
        if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write('\n');
          out.write('	');
          out.write('	');
          //  portlet:param
          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
          _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
          // /html/portlet/layouts_admin/init_attributes.jspf(195,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f9.setName("backURL");
          // /html/portlet/layouts_admin/init_attributes.jspf(195,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_portlet_005fparam_005f9.setValue( backURL );
          int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
          if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
          out.write('\n');
          out.write('	');
        }
        if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
        out.write('\n');
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(198,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f10.setName("groupId");
        // /html/portlet/layouts_admin/init_attributes.jspf(198,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f10.setValue( String.valueOf(liveGroupId) );
        int _jspx_eval_portlet_005fparam_005f10 = _jspx_th_portlet_005fparam_005f10.doStartTag();
        if (_jspx_th_portlet_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
        out.write('\n');
        out.write('	');
        //  portlet:param
        com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f11 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
        _jspx_th_portlet_005fparam_005f11.setPageContext(_jspx_page_context);
        _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
        // /html/portlet/layouts_admin/init_attributes.jspf(199,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f11.setName("viewLayout");
        // /html/portlet/layouts_admin/init_attributes.jspf(199,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_portlet_005fparam_005f11.setValue( Boolean.TRUE.toString() );
        int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
        if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
          return;
        }
        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
        out.write('\n');
      }
      if (_jspx_th_liferay_002dportlet_005fresourceURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvarImpl_005fcopyCurrentRenderParameters.reuse(_jspx_th_liferay_002dportlet_005fresourceURL_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvarImpl_005fcopyCurrentRenderParameters.reuse(_jspx_th_liferay_002dportlet_005fresourceURL_005f0);
      com.liferay.portal.kernel.portlet.LiferayPortletURL portletURL = null;
      portletURL = (com.liferay.portal.kernel.portlet.LiferayPortletURL) _jspx_page_context.findAttribute("portletURL");
      out.write('\n');
      out.write('\n');

request.setAttribute("edit_pages.jsp-group", group);
request.setAttribute("edit_pages.jsp-selGroup", selGroup);
request.setAttribute("edit_pages.jsp-liveGroup", liveGroup);
request.setAttribute("edit_pages.jsp-stagingGroup", stagingGroup);
request.setAttribute("edit_pages.jsp-groupId", new Long(groupId));
request.setAttribute("edit_pages.jsp-liveGroupId", new Long(liveGroupId));
request.setAttribute("edit_pages.jsp-stagingGroupId", new Long(stagingGroupId));
request.setAttribute("edit_pages.jsp-selPlid", new Long(selPlid));
request.setAttribute("edit_pages.jsp-privateLayout", new Boolean(privateLayout));
request.setAttribute("edit_pages.jsp-layoutId", new Long(layoutId));
request.setAttribute("edit_pages.jsp-groupTypeSettings", groupTypeSettings);
request.setAttribute("edit_pages.jsp-liveGroupTypeSettings", liveGroupTypeSettings);
request.setAttribute("edit_pages.jsp-selLayout", selLayout);
request.setAttribute("edit_pages.jsp-selLayoutSet", selLayoutSet);

request.setAttribute("edit_pages.jsp-rootNodeName", rootNodeName);

request.setAttribute("edit_pages.jsp-portletURL", portletURL);
request.setAttribute("edit_pages.jsp-redirectURL", redirectURL);

      out.write('\n');
      out.write('\n');

boolean incomplete = ParamUtil.getBoolean(request, "incomplete", true);

String treeLoading = PortalUtil.generateRandomKey(request, "treeLoading");

String treeId = ParamUtil.getString(request, "treeId");
boolean checkContentDisplayPage = ParamUtil.getBoolean(request, "checkContentDisplayPage", false);
boolean expandFirstNode = ParamUtil.getBoolean(request, "expandFirstNode", true);
boolean saveState = ParamUtil.getBoolean(request, "saveState", true);
boolean selectableTree = ParamUtil.getBoolean(request, "selectableTree");

String modules = "aui-io-request,aui-tree-view,dataschema-xml,datatype-xml";

if (!selectableTree) {
	modules += ",liferay-history-manager";
}

      out.write('\n');
      out.write('\n');
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f0.setParent(null);
      // /html/portlet/layouts_admin/tree_js.jsp(39,0) name = use type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_aui_005fscript_005f0.setUse( modules );
      int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tvar Lang = A.Lang;\n");
          out.write("\tvar AArray = A.Array;\n");
          out.write("\n");
          out.write("\tvar Util = Liferay.Util;\n");
          out.write("\n");
          out.write("\tvar GET_LAYOUTS_URL = themeDisplay.getPathMain() + '/layouts_admin/get_layouts';\n");
          out.write("\n");
          out.write("\tvar LAYOUT_URL = '");
          out.print( portletURL + StringPool.AMPERSAND + portletDisplay.getNamespace() + "selPlid={selPlid}" + StringPool.AMPERSAND + portletDisplay.getNamespace() + "historyKey={historyKey}" );
          out.write("';\n");
          out.write("\tvar STR_CHILDREN = 'children';\n");
          out.write("\n");
          out.write("\t");

	JSONArray checkedNodesJSONArray = JSONFactoryUtil.createJSONArray();

	String checkedLayoutIds = SessionTreeJSClicks.getOpenNodes(request, treeId + "SelectedNode");

	if (Validator.isNotNull(checkedLayoutIds)) {
		for (long checkedLayoutId : StringUtil.split(checkedLayoutIds, 0L)) {
			try {
				Layout checkedLayout = LayoutLocalServiceUtil.getLayout(groupId, privateLayout, checkedLayoutId);

				checkedNodesJSONArray.put(String.valueOf(checkedLayout.getPlid()));
			}
			catch (NoSuchLayoutException nsle) {
			}
		}
	}
	
          out.write("\n");
          out.write("\n");
          out.write("\tvar TreeUtil = {\n");
          out.write("\t\tCHECKED_NODES: ");
          out.print( checkedNodesJSONArray.toString() );
          out.write(",\n");
          out.write("\t\tDEFAULT_PARENT_LAYOUT_ID: ");
          out.print( LayoutConstants.DEFAULT_PARENT_LAYOUT_ID );
          out.write(",\n");
          out.write("\t\tPAGINATION_LIMIT: ");
          out.print( PropsValues.LAYOUT_MANAGE_PAGES_INITIAL_CHILDREN );
          out.write(",\n");
          out.write("\t\tPREFIX_GROUP_ID: '_groupId_',\n");
          out.write("\t\tPREFIX_LAYOUT: '_layout_',\n");
          out.write("\t\tPREFIX_LAYOUT_ID: '_layoutId_',\n");
          out.write("\t\tPREFIX_PLID: '_plid_',\n");
          out.write("\n");
          out.write("\t\tafterRenderTree: function(event) {\n");
          out.write("\t\t\tvar rootNode = event.target.item(0);\n");
          out.write("\n");
          out.write("\t\t\tvar loadingEl = A.one('#");
          if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("treeLoading");
          out.print( treeLoading );
          out.write("');\n");
          out.write("\n");
          out.write("\t\t\tloadingEl.hide();\n");
          out.write("\n");
          out.write("\t\t\t");
          //  c:choose
          com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
          if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f0 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
            // /html/portlet/layouts_admin/tree_js.jsp(85,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f0.setTest( saveState && selectableTree );
            int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
            if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\tTreeUtil.restoreCheckedNode(rootNode);\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f1 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
            // /html/portlet/layouts_admin/tree_js.jsp(88,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f1.setTest( expandFirstNode );
            int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
            if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\trootNode.expand();\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\tTreeUtil.restoreSelectedNode(rootNode);\n");
          out.write("\n");
          out.write("\t\t\trootNode.eachChildren(TreeUtil.restoreSelectedNode);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tcreateListItemId: function(groupId, layoutId, plid) {\n");
          out.write("\t\t\treturn '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("' + TreeUtil.PREFIX_LAYOUT_ID + layoutId + TreeUtil.PREFIX_PLID + plid + TreeUtil.PREFIX_GROUP_ID + groupId;\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tcreateLinkId: function(friendlyURL) {\n");
          out.write("\t\t\treturn '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("' + TreeUtil.PREFIX_LAYOUT + friendlyURL.substring(1);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tcreateLink: function(data) {\n");
          out.write("\t\t\tvar className = 'layout-tree';\n");
          out.write("\n");
          out.write("\t\t\tif (data.cssClass) {\n");
          out.write("\t\t\t\tclassName += ' ' + data.cssClass;\n");
          out.write("\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\tif (");
          out.print( checkContentDisplayPage );
          out.write(" && !data.contentDisplayPage) {\n");
          out.write("\t\t\t\tclassName += ' layout-page-invalid';\n");
          out.write("\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\tvar href = Lang.sub(\n");
          out.write("\t\t\t\tLAYOUT_URL,\n");
          out.write("\t\t\t\t{\n");
          out.write("\t\t\t\t\thistoryKey: data.historyKey,\n");
          out.write("\t\t\t\t\tselPlid: data.plid\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\n");
          out.write("\t\t\treturn '<a class=\"' + className + '\" data-uuid=\"' + data.uuid + '\" href=\"' + href + '\" id=\"' + data.id + '\" title=\"' + data.title + '\">' + data.label + '</a>';\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\textractGroupId: function(node) {\n");
          out.write("\t\t\treturn node.get('id').match(/groupId_(\\d+)/)[1];\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\textractLayoutId: function(node) {\n");
          out.write("\t\t\treturn node.get('id').match(/layoutId_(\\d+)/)[1];\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\textractPlid: function(node) {\n");
          out.write("\t\t\treturn node.get('id').match(/plid_(\\d+)/)[1];\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tformatJSONResults: function(json) {\n");
          out.write("\t\t\tvar output = [];\n");
          out.write("\n");
          out.write("\t\t\tA.each(\n");
          out.write("\t\t\t\tjson.layouts,\n");
          out.write("\t\t\t\tfunction(node) {\n");
          out.write("\t\t\t\t\tvar childLayouts = [];\n");
          out.write("\t\t\t\t\tvar total = 0;\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar hasChildren = node.hasChildren;\n");
          out.write("\t\t\t\t\tvar nodeChildren = node.children;\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (nodeChildren) {\n");
          out.write("\t\t\t\t\t\tchildLayouts = nodeChildren.layouts;\n");
          out.write("\t\t\t\t\t\ttotal = nodeChildren.total;\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar expanded = (total > 0);\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar newNode = {\n");
          out.write("\t\t\t\t\t\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f2 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(160,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f2.setTest( saveState );
          int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
          if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\tafter: {\n");
            out.write("\t\t\t\t\t\t\t\t");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f3 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
            // /html/portlet/layouts_admin/tree_js.jsp(162,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f3.setTest( selectableTree );
            int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
            if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\tcheckedChange: function(event) {\n");
              out.write("\t\t\t\t\t\t\t\t\t\tif (this === event.originalTarget) {\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\tvar target = event.target;\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\tvar plid = TreeUtil.extractPlid(target);\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\tTreeUtil.updateSessionTreeCheckedState('");
              out.print( HtmlUtil.escape(treeId) );
              out.write("SelectedNode', plid, event.newVal);\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\tTreeUtil.updateCheckedNodes(target, event.newVal);\n");
              out.write("\t\t\t\t\t\t\t\t\t\t}\n");
              out.write("\t\t\t\t\t\t\t\t\t},\n");
              out.write("\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\tchildrenChange: function(event) {\n");
            out.write("\t\t\t\t\t\t\t\t\tvar target = event.target;\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\ttarget.set('alwaysShowHitArea', event.newVal.length > 0);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\ttarget.eachChildren(TreeUtil.restoreSelectedNode);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f4 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
            // /html/portlet/layouts_admin/tree_js.jsp(183,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f4.setTest( selectableTree );
            int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
            if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\tif (target.get('checked')) {\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\tTreeUtil.updateCheckedNodes(target, true);\n");
              out.write("\t\t\t\t\t\t\t\t\t\t}\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\tTreeUtil.restoreCheckedNode(target);\n");
              out.write("\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t},\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\texpandedChange: function(event) {\n");
            out.write("\t\t\t\t\t\t\t\t\tvar layoutId = TreeUtil.extractLayoutId(event.target);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\tTreeUtil.updateSessionTreeOpenedState('");
            out.print( HtmlUtil.escape(treeId) );
            out.write("', layoutId, event.newVal);\n");
            out.write("\t\t\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\t\t},\n");
            out.write("\t\t\t\t\t\t");
          }
          if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
          out.write("\n");
          out.write("\t\t\t\t\t\talwaysShowHitArea: hasChildren,\n");
          out.write("\t\t\t\t\t\tdraggable: node.sortable,\n");
          out.write("\t\t\t\t\t\texpanded: expanded,\n");
          out.write("\t\t\t\t\t\tid: TreeUtil.createListItemId(node.groupId, node.layoutId, node.plid),\n");
          out.write("\t\t\t\t\t\tio: {\n");
          out.write("\t\t\t\t\t\t\tcfg: {\n");
          out.write("\t\t\t\t\t\t\t\tdata: function(node) {\n");
          out.write("\t\t\t\t\t\t\t\t\treturn {\n");
          out.write("\t\t\t\t\t\t\t\t\t\tgroupId: TreeUtil.extractGroupId(node),\n");
          out.write("\t\t\t\t\t\t\t\t\t\tincomplete: ");
          out.print( incomplete );
          out.write(",\n");
          out.write("\t\t\t\t\t\t\t\t\t\tp_auth: Liferay.authToken,\n");
          out.write("\t\t\t\t\t\t\t\t\t\tparentLayoutId: TreeUtil.extractLayoutId(node),\n");
          out.write("\t\t\t\t\t\t\t\t\t\tprivateLayout: ");
          out.print( privateLayout );
          out.write(",\n");
          out.write("\t\t\t\t\t\t\t\t\t\tselPlid: '");
          out.print( selPlid );
          out.write("',\n");
          out.write("\t\t\t\t\t\t\t\t\t\ttreeId: '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("'\n");
          out.write("\t\t\t\t\t\t\t\t\t};\n");
          out.write("\t\t\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\t\t\tmethod: A.config.io.method,\n");
          out.write("\t\t\t\t\t\t\t\ton: {\n");
          out.write("\t\t\t\t\t\t\t\t\tsuccess: function(event, id, xhr) {\n");
          out.write("\t\t\t\t\t\t\t\t\t\tvar instance = this;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t\tvar response;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t\ttry {\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\tresponse = A.JSON.parse(xhr.responseText);\n");
          out.write("\t\t\t\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t\t\t\t\tcatch (e) {\n");
          out.write("\t\t\t\t\t\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t\tif (response) {\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\tinstance.get('paginator').total = response.total;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\tinstance.syncUI();\n");
          out.write("\t\t\t\t\t\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f5 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(235,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f5.setTest( saveState );
          int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
          if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\tTreeUtil.updatePagination(instance);\n");
            out.write("\t\t\t\t\t\t\t\t\t\t");
          }
          if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\t\tformatter: TreeUtil.formatJSONResults,\n");
          out.write("\t\t\t\t\t\t\turl: GET_LAYOUTS_URL\n");
          out.write("\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\tleaf: !node.parentable,\n");
          out.write("\t\t\t\t\t\tpaginator: {\n");
          out.write("\t\t\t\t\t\t\tlimit: TreeUtil.PAGINATION_LIMIT,\n");
          out.write("\t\t\t\t\t\t\toffsetParam: 'start',\n");
          out.write("\t\t\t\t\t\t\tstart: Math.max(childLayouts.length - TreeUtil.PAGINATION_LIMIT, 0),\n");
          out.write("\t\t\t\t\t\t\ttotal: total\n");
          out.write("\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\ttype: '");
          out.print( !selectableTree ? "io" : "task" );
          out.write("'\n");
          out.write("\t\t\t\t\t};\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (nodeChildren && expanded) {\n");
          out.write("\t\t\t\t\t\tnewNode.children = TreeUtil.formatJSONResults(nodeChildren);\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar cssClass = '';\n");
          out.write("\t\t\t\t\tvar title = '';\n");
          out.write("\n");
          out.write("\t\t\t\t\tnewNode.label = Util.escapeHTML(node.name);\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (node.layoutRevisionId) {\n");
          out.write("\t\t\t\t\t\tif (node.layoutBranchName) {\n");
          out.write("\t\t\t\t\t\t\tnode.layoutBranchName = Util.escapeHTML(node.layoutBranchName);\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\tnewNode.label += Lang.sub(' <span class=\"layout-branch-name\" title=\"");
          out.print( UnicodeLanguageUtil.get(pageContext, "this-is-the-page-variation-that-is-marked-as-ready-for-publication") );
          out.write("\">[{layoutBranchName}]</span>', node);\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\t\tif (node.incomplete) {\n");
          out.write("\t\t\t\t\t\t\tcssClass = 'incomplete-layout';\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\ttitle = '");
          out.print( UnicodeLanguageUtil.get(pageContext, "this-page-is-not-enabled-in-this-site-pages-variation,-but-is-available-in-other-variations") );
          out.write("';\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (!node.sortable) {\n");
          out.write("\t\t\t\t\t\tnewNode.cssClass = 'lfr-page-locked';\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (!");
          out.print( selectableTree );
          out.write(") {\n");
          out.write("\t\t\t\t\t\tnewNode.label = TreeUtil.createLink(\n");
          out.write("\t\t\t\t\t\t\t{\n");
          out.write("\t\t\t\t\t\t\t\tcontentDisplayPage: node.contentDisplayPage,\n");
          out.write("\t\t\t\t\t\t\t\tcssClass: cssClass,\n");
          out.write("\t\t\t\t\t\t\t\tid: TreeUtil.createLinkId(node.friendlyURL),\n");
          out.write("\t\t\t\t\t\t\t\tlabel: newNode.label,\n");
          out.write("\t\t\t\t\t\t\t\tplid: node.plid,\n");
          out.write("\t\t\t\t\t\t\t\ttitle: title,\n");
          out.write("\t\t\t\t\t\t\t\tuuid: node.uuid\n");
          out.write("\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t);\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\toutput.push(newNode);\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\n");
          out.write("\t\t\treturn output;\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\trestoreCheckedNode: function(node) {\n");
          out.write("\t\t\tvar instance = this;\n");
          out.write("\n");
          out.write("\t\t\tvar plid = TreeUtil.extractPlid(node);\n");
          out.write("\n");
          out.write("\t\t\tvar tree = node.get('ownerTree');\n");
          out.write("\n");
          out.write("\t\t\tvar treeNodeTaskSuperClass = A.TreeNodeTask.superclass;\n");
          out.write("\n");
          out.write("\t\t\tif (AArray.indexOf(TreeUtil.CHECKED_NODES, plid) > -1) {\n");
          out.write("\t\t\t\ttreeNodeTaskSuperClass.check.call(node, tree);\n");
          out.write("\t\t\t}\n");
          out.write("\t\t\telse {\n");
          out.write("\t\t\t\ttreeNodeTaskSuperClass.uncheck.call(node, tree);\n");
          out.write("\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\tAArray.each(node.get(STR_CHILDREN), TreeUtil.restoreCheckedNode);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\trestoreSelectedNode: function(node) {\n");
          out.write("\t\t\tvar plid = TreeUtil.extractPlid(node);\n");
          out.write("\n");
          out.write("\t\t\tif (plid == '");
          out.print( selPlid );
          out.write("') {\n");
          out.write("\t\t\t\tnode.select();\n");
          out.write("\t\t\t}\n");
          out.write("\t\t\telse {\n");
          out.write("\t\t\t\tnode.unselect();\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tupdateLayout: function(data) {\n");
          out.write("\t\t\tA.io.request(\n");
          out.write("\t\t\t\tthemeDisplay.getPathMain() + '/layouts_admin/update_page',\n");
          out.write("\t\t\t\t{\n");
          out.write("\t\t\t\t\tdata: A.mix(\n");
          out.write("\t\t\t\t\t\tdata,\n");
          out.write("\t\t\t\t\t\t{\n");
          out.write("\t\t\t\t\t\t\tp_auth: Liferay.authToken\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t)\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tupdateLayoutParent: function(dragPlid, dropPlid, index) {\n");
          out.write("\t\t\tTreeUtil.updateLayout(\n");
          out.write("\t\t\t\t{\n");
          out.write("\t\t\t\t\tcmd: 'parent_layout_id',\n");
          out.write("\t\t\t\t\tparentPlid: dropPlid,\n");
          out.write("\t\t\t\t\tplid: dragPlid,\n");
          out.write("\t\t\t\t\tpriority: index\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\t\t}\n");
          out.write("\n");
          out.write("\t\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f6 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(357,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f6.setTest( saveState );
          int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
          if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t, invokeSessionClick: function(data, callback) {\n");
            out.write("\t\t\t\tA.mix(\n");
            out.write("\t\t\t\t\tdata,\n");
            out.write("\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\tuseHttpSession: true\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t);\n");
            out.write("\n");
            out.write("\t\t\t\tA.io.request(\n");
            out.write("\t\t\t\t\tthemeDisplay.getPathMain() + '/portal/session_click',\n");
            out.write("\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\tafter: {\n");
            out.write("\t\t\t\t\t\t\tsuccess: function(event) {\n");
            out.write("\t\t\t\t\t\t\t\tvar responseData = this.get('responseData');\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\tif (callback && responseData) {\n");
            out.write("\t\t\t\t\t\t\t\t\tcallback(Liferay.Util.unescapeHTML(responseData));\n");
            out.write("\t\t\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\t},\n");
            out.write("\t\t\t\t\t\tdata: data\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t);\n");
            out.write("\t\t\t},\n");
            out.write("\n");
            out.write("\t\t\tupdatePagination: function(node) {\n");
            out.write("\t\t\t\tvar paginationMap = {};\n");
            out.write("\n");
            out.write("\t\t\t\tvar updatePaginationMap = function(map, curNode) {\n");
            out.write("\t\t\t\t\tif (A.instanceOf(curNode, A.TreeNodeIO)) {\n");
            out.write("\t\t\t\t\t\tvar paginationLimit = TreeUtil.PAGINATION_LIMIT;\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tvar layoutId = TreeUtil.extractLayoutId(curNode);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tvar children = curNode.get(STR_CHILDREN);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tmap[layoutId] = Math.ceil(children.length / paginationLimit) * paginationLimit;\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t}\n");
            out.write("\n");
            out.write("\t\t\t\tTreeUtil.invokeSessionClick(\n");
            out.write("\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\tcmd: 'get',\n");
            out.write("\t\t\t\t\t\tkey: '");
            out.print( HtmlUtil.escape(treeId) );
            out.write(':');
            out.print( groupId );
            out.write(':');
            out.print( privateLayout );
            out.write(":Pagination'\n");
            out.write("\t\t\t\t\t},\n");
            out.write("\t\t\t\t\tfunction(responseData) {\n");
            out.write("\t\t\t\t\t\ttry {\n");
            out.write("\t\t\t\t\t\t\tpaginationMap = A.JSON.parse(responseData);\n");
            out.write("\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\tcatch(e) {\n");
            out.write("\t\t\t\t\t\t}\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tupdatePaginationMap(paginationMap, node)\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tnode.eachParent(\n");
            out.write("\t\t\t\t\t\t\tfunction(parent) {\n");
            out.write("\t\t\t\t\t\t\t\tupdatePaginationMap(paginationMap, parent);\n");
            out.write("\t\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\t);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tTreeUtil.invokeSessionClick(\n");
            out.write("\t\t\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\t\t\t'");
            out.print( HtmlUtil.escape(treeId) );
            out.write(':');
            out.print( groupId );
            out.write(':');
            out.print( privateLayout );
            out.write(":Pagination': A.JSON.stringify(paginationMap)\n");
            out.write("\t\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\t);\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t);\n");
            out.write("\t\t\t},\n");
            out.write("\n");
            out.write("\t\t\tupdateCheckedNodes: function(node, state) {\n");
            out.write("\t\t\t\tvar plid = TreeUtil.extractPlid(node);\n");
            out.write("\n");
            out.write("\t\t\t\tvar checkedNodes = TreeUtil.CHECKED_NODES;\n");
            out.write("\n");
            out.write("\t\t\t\tvar index = AArray.indexOf(checkedNodes, plid);\n");
            out.write("\n");
            out.write("\t\t\t\tif (state) {\n");
            out.write("\t\t\t\t\tif (index == -1) {\n");
            out.write("\t\t\t\t\t\tcheckedNodes.push(plid);\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t}\n");
            out.write("\t\t\t\telse if (index > -1) {\n");
            out.write("\t\t\t\t\tAArray.remove(checkedNodes, index);\n");
            out.write("\t\t\t\t}\n");
            out.write("\t\t\t},\n");
            out.write("\n");
            out.write("\t\t\tupdateSessionTreeCheckedState: function(treeId, nodeId, state) {\n");
            out.write("\t\t\t\tvar data = {\n");
            out.write("\t\t\t\t\tcmd: state ? 'layoutCheck' : 'layoutUncheck',\n");
            out.write("\t\t\t\t\tplid: nodeId\n");
            out.write("\t\t\t\t};\n");
            out.write("\n");
            out.write("\t\t\t\tTreeUtil.updateSessionTreeClick(treeId, data);\n");
            out.write("\t\t\t},\n");
            out.write("\n");
            out.write("\t\t\tupdateSessionTreeClick: function(treeId, data) {\n");
            out.write("\n");
            out.write("\t\t\t\tdata = A.merge(\n");
            out.write("\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\tgroupId: ");
            out.print( groupId );
            out.write(",\n");
            out.write("\t\t\t\t\t\tprivateLayout: ");
            out.print( privateLayout );
            out.write(",\n");
            out.write("\t\t\t\t\t\trecursive: true,\n");
            out.write("\t\t\t\t\t\ttreeId: treeId\n");
            out.write("\t\t\t\t\t},\n");
            out.write("\t\t\t\t\tdata\n");
            out.write("\t\t\t\t);\n");
            out.write("\n");
            out.write("\t\t\t\tA.io.request(\n");
            out.write("\t\t\t\t\tthemeDisplay.getPathMain() + '/portal/session_tree_js_click',\n");
            out.write("\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\tdata: data\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t);\n");
            out.write("\t\t\t},\n");
            out.write("\n");
            out.write("\t\t\tupdateSessionTreeOpenedState: function(treeId, nodeId, state) {\n");
            out.write("\t\t\t\tvar data = {\n");
            out.write("\t\t\t\t\tnodeId: nodeId,\n");
            out.write("\t\t\t\t\topenNode: state\n");
            out.write("\t\t\t\t};\n");
            out.write("\n");
            out.write("\t\t\t\tTreeUtil.updateSessionTreeClick(treeId, data);\n");
            out.write("\t\t\t}\n");
            out.write("\t\t");
          }
          if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
          out.write("\n");
          out.write("\t};\n");
          out.write("\n");
          out.write("\tvar rootLabel = '");
          out.print( HtmlUtil.escapeJS(rootNodeName) );
          out.write("';\n");
          out.write("\tvar treeElId = '");
          if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( HtmlUtil.escape(treeId) );
          out.write("Output';\n");
          out.write("\n");
          out.write("\tvar RootNodeType = A.TreeNodeTask;\n");
          out.write("\tvar TreeViewType = A.TreeView;\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f7 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(490,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f7.setTest( !selectableTree );
          int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
          if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\tRootNodeType = A.TreeNodeIO;\n");
            out.write("\t\tTreeViewType = A.TreeViewDD;\n");
            out.write("\n");
            out.write("\t\t");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f8 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
            // /html/portlet/layouts_admin/tree_js.jsp(494,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f8.setTest( !checkContentDisplayPage );
            int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
            if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\trootLabel = TreeUtil.createLink(\n");
              out.write("\t\t\t{\n");
              out.write("\t\t\t\tlabel: Util.escapeHTML(rootLabel),\n");
              out.write("\t\t\t\tplid: TreeUtil.DEFAULT_PARENT_LAYOUT_ID\n");
              out.write("\t\t\t}\n");
              out.write("\t\t);\n");
              out.write("\t\t");
            }
            if (_jspx_th_c_005fif_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
            out.write('\n');
            out.write('	');
          }
          if (_jspx_th_c_005fif_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
          out.write("\n");
          out.write("\n");
          out.write("\tvar rootNode = new RootNodeType(\n");
          out.write("\t\t{\n");
          out.write("\t\t\tafter: {\n");
          out.write("\t\t\t\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f9 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(507,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f9.setTest( selectableTree );
          int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
          if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\tcheckedChange: function(event) {\n");
            out.write("\t\t\t\t\t\tTreeUtil.updateSessionTreeCheckedState('");
            out.print( HtmlUtil.escape(treeId) );
            out.write("SelectedNode', ");
            out.print( LayoutConstants.DEFAULT_PLID );
            out.write(", event.newVal);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tTreeUtil.updateCheckedNodes(event.target, event.newVal);\n");
            out.write("\t\t\t\t\t},\n");
            out.write("\t\t\t\t");
          }
          if (_jspx_th_c_005fif_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\texpandedChange: function(event) {\n");
          out.write("\t\t\t\t\tvar sessionClickURL = themeDisplay.getPathMain() + '/portal/session_click';\n");
          out.write("\n");
          out.write("\t\t\t\t\tA.io.request(\n");
          out.write("\t\t\t\t\t\tsessionClickURL,\n");
          out.write("\t\t\t\t\t\t{\n");
          out.write("\t\t\t\t\t\t\tdata: {\n");
          out.write("\t\t\t\t\t\t\t\t'");
          out.print( HtmlUtil.escape(treeId) );
          out.write("RootNode': event.newVal\n");
          out.write("\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t);\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t},\n");
          out.write("\n");
          out.write("\t\t\talwaysShowHitArea: true,\n");
          out.write("\n");
          out.write("\t\t\t");

			long[] openNodes = StringUtil.split(SessionTreeJSClicks.getOpenNodes(request, treeId), 0L);

			JSONObject layoutsJSON = JSONFactoryUtil.createJSONObject(LayoutsTreeUtil.getLayoutsJSON(request, groupId, privateLayout, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, openNodes, true));
			
          out.write("\n");
          out.write("\n");
          out.write("\t\t\tchildren: TreeUtil.formatJSONResults(");
          out.print( layoutsJSON );
          out.write("),\n");
          out.write("\t\t\tdraggable: false,\n");
          out.write("\n");
          out.write("\t\t\t");

			boolean rootNodeExpanded = GetterUtil.getBoolean(SessionClicks.get(request, treeId + "RootNode", null), true);
			
          out.write("\n");
          out.write("\n");
          out.write("\t\t\texpanded: ");
          out.print( rootNodeExpanded );
          out.write(",\n");
          out.write("\t\t\tid: TreeUtil.createListItemId(");
          out.print( groupId );
          out.write(", TreeUtil.DEFAULT_PARENT_LAYOUT_ID, 0),\n");
          out.write("\t\t\tlabel: rootLabel,\n");
          out.write("\t\t\tleaf: false,\n");
          out.write("\t\t\tpaginator: {\n");
          out.write("\t\t\t\tlimit: TreeUtil.PAGINATION_LIMIT,\n");
          out.write("\t\t\t\toffsetParam: 'start',\n");
          out.write("\t\t\t\tstart: Math.max(");
          out.print( layoutsJSON.getJSONArray("layouts").length() );
          out.write(" - TreeUtil.PAGINATION_LIMIT, 0),\n");
          out.write("\t\t\t\ttotal: ");
          out.print( layoutsJSON.getInt("total") );
          out.write("\n");
          out.write("\t\t\t}\n");
          out.write("\t\t}\n");
          out.write("\t);\n");
          out.write("\n");
          out.write("\trootNode.get('contentBox').addClass('lfr-root-node');\n");
          out.write("\n");
          out.write("\tvar treeview = new TreeViewType(\n");
          out.write("\t\t{\n");
          out.write("\t\t\tafter: {\n");
          out.write("\t\t\t\trender: TreeUtil.afterRenderTree\n");
          out.write("\t\t\t},\n");
          out.write("\t\t\tboundingBox: '#' + treeElId,\n");
          out.write("\t\t\tchildren: [rootNode],\n");
          out.write("\t\t\tio: {\n");
          out.write("\t\t\t\tcfg: {\n");
          out.write("\t\t\t\t\tdata: function(node) {\n");
          out.write("\t\t\t\t\t\treturn {\n");
          out.write("\t\t\t\t\t\t\tgroupId: TreeUtil.extractGroupId(node),\n");
          out.write("\t\t\t\t\t\t\tincomplete: ");
          out.print( incomplete );
          out.write(",\n");
          out.write("\t\t\t\t\t\t\tp_auth: Liferay.authToken,\n");
          out.write("\t\t\t\t\t\t\tparentLayoutId: TreeUtil.extractLayoutId(node),\n");
          out.write("\t\t\t\t\t\t\tprivateLayout: ");
          out.print( privateLayout );
          out.write(",\n");
          out.write("\t\t\t\t\t\t\tselPlid: '");
          out.print( selPlid );
          out.write("'\n");
          out.write("\t\t\t\t\t\t};\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t\tmethod: AUI.defaults.io.method,\n");
          out.write("\t\t\t\t\ton: {\n");
          out.write("\t\t\t\t\t\tsuccess: function(event, id, xhr) {\n");
          out.write("\t\t\t\t\t\t\tvar instance = this;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\tvar response;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\ttry {\n");
          out.write("\t\t\t\t\t\t\t\tresponse = A.JSON.parse(xhr.responseText);\n");
          out.write("\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t\tcatch(e) {}\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\tif (response) {\n");
          out.write("\t\t\t\t\t\t\t\tinstance.get('paginator').total = response.total;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\tinstance.syncUI();\n");
          out.write("\t\t\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f10 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(596,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f10.setTest( saveState );
          int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
          if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\tTreeUtil.updatePagination(instance);\n");
            out.write("\t\t\t\t\t\t\t");
          }
          if (_jspx_th_c_005fif_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
          out.write("\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\tformatter: TreeUtil.formatJSONResults,\n");
          out.write("\t\t\t\turl: GET_LAYOUTS_URL\n");
          out.write("\t\t\t},\n");
          out.write("\t\t\ton: {\n");
          out.write("\t\t\t\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f11 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(606,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f11.setTest( saveState && selectableTree );
          int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
          if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t\tappend: function(event) {\n");
            out.write("\t\t\t\t\t\tTreeUtil.restoreCheckedNode(event.tree.node);\n");
            out.write("\t\t\t\t\t},\n");
            out.write("\t\t\t\t");
          }
          if (_jspx_th_c_005fif_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\tdropAppend: function(event) {\n");
          out.write("\t\t\t\t\tvar tree = event.tree;\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar index = tree.dragNode.get('parentNode').getChildrenLength() - 1;\n");
          out.write("\n");
          out.write("\t\t\t\t\tTreeUtil.updateLayoutParent(\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dragNode),\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dropNode),\n");
          out.write("\t\t\t\t\t\tindex\n");
          out.write("\t\t\t\t\t);\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\t'drop:hit': function(event) {\n");
          out.write("\t\t\t\t\tvar dropNode = event.drop.get('node').get('parentNode');\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar dropTreeNode = dropNode.getData('tree-node');\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (!dropTreeNode.get('draggable')) {\n");
          out.write("\t\t\t\t\t\tevent.halt();\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\tdropInsert: function(event) {\n");
          out.write("\t\t\t\t\tvar tree = event.tree;\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar index = tree.dragNode.get('parentNode').indexOf(tree.dragNode);\n");
          out.write("\n");
          out.write("\t\t\t\t\tTreeUtil.updateLayoutParent(\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dragNode),\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dropNode.get('parentNode')),\n");
          out.write("\t\t\t\t\t\tindex\n");
          out.write("\t\t\t\t\t);\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t},\n");
          out.write("\t\t\ttype: 'pages'\n");
          out.write("\t\t}\n");
          out.write("\t).render();\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f12 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(648,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f12.setTest( !saveState && checkContentDisplayPage );
          int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
          if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\ttreeview.on(\n");
            out.write("\t\t\t'append',\n");
            out.write("\t\t\tfunction(event) {\n");
            out.write("\t\t\t\tvar node = event.tree.node;\n");
            out.write("\n");
            out.write("\t\t\t\tTreeUtil.restoreSelectedNode(node);\n");
            out.write("\n");
            out.write("\t\t\t\tnode.eachChildren(TreeUtil.restoreSelectedNode);\n");
            out.write("\t\t\t}\n");
            out.write("\t\t);\n");
            out.write("\t");
          }
          if (_jspx_th_c_005fif_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
          out.write("\n");
          out.write("\n");
          out.write("\tA.one('#' + treeElId).setData('treeInstance', treeview);\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f13 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/layouts_admin/tree_js.jsp(663,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f13.setTest( !selectableTree );
          int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
          if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\tvar History = Liferay.HistoryManager;\n");
            out.write("\n");
            out.write("\t\tvar DEFAULT_PLID = '0';\n");
            out.write("\n");
            out.write("\t\tvar HISTORY_SELECTED_PLID = '");
            if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_c_005fif_005f13, _jspx_page_context))
              return;
            out.write("selPlid';\n");
            out.write("\n");
            out.write("\t\tvar layoutsContainer = A.one('#");
            if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_c_005fif_005f13, _jspx_page_context))
              return;
            out.write("layoutsContainer');\n");
            out.write("\n");
            out.write("\t\ttreeview.after(\n");
            out.write("\t\t\t'lastSelectedChange',\n");
            out.write("\t\t\tfunction(event) {\n");
            out.write("\t\t\t\tvar node = event.newVal;\n");
            out.write("\n");
            out.write("\t\t\t\tvar plid = TreeUtil.extractPlid(node);\n");
            out.write("\n");
            out.write("\t\t\t\tvar currentValue = History.get(HISTORY_SELECTED_PLID);\n");
            out.write("\n");
            out.write("\t\t\t\tif (plid != currentValue) {\n");
            out.write("\t\t\t\t\tif ((plid == DEFAULT_PLID) && Lang.isValue(currentValue)) {\n");
            out.write("\t\t\t\t\t\tplid = null;\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\n");
            out.write("\t\t\t\t\tHistory.add(\n");
            out.write("\t\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\t\t'");
            if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_c_005fif_005f13, _jspx_page_context))
              return;
            out.write("selPlid': plid\n");
            out.write("\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t);\n");
            out.write("\t\t\t\t}\n");
            out.write("\t\t\t}\n");
            out.write("\t\t);\n");
            out.write("\n");
            out.write("\t\tfunction compareItemId(item, id) {\n");
            out.write("\t\t\treturn (TreeUtil.extractPlid(item) == id);\n");
            out.write("\t\t}\n");
            out.write("\n");
            out.write("\t\tfunction findNodeByPlid(node, plid) {\n");
            out.write("\t\t\tvar foundItem = null;\n");
            out.write("\n");
            out.write("\t\t\tif (node) {\n");
            out.write("\t\t\t\tif (compareItemId(node, plid)) {\n");
            out.write("\t\t\t\t\tfoundItem = node;\n");
            out.write("\t\t\t\t}\n");
            out.write("\t\t\t}\n");
            out.write("\n");
            out.write("\t\t\tif (!foundItem) {\n");
            out.write("\t\t\t\tvar children = (node || treeview).get(STR_CHILDREN);\n");
            out.write("\n");
            out.write("\t\t\t\tvar length = children.length;\n");
            out.write("\n");
            out.write("\t\t\t\tfor (var i = 0; i < length; i++) {\n");
            out.write("\t\t\t\t\tvar item = children[i];\n");
            out.write("\n");
            out.write("\t\t\t\t\tif (item.isLeaf()) {\n");
            out.write("\t\t\t\t\t\tif (compareItemId(item, plid)) {\n");
            out.write("\t\t\t\t\t\t\tfoundItem = item;\n");
            out.write("\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t\telse {\n");
            out.write("\t\t\t\t\t\tfoundItem = findNodeByPlid(item, plid);\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\n");
            out.write("\t\t\t\t\tif (foundItem) {\n");
            out.write("\t\t\t\t\t\tbreak;\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t}\n");
            out.write("\t\t\t}\n");
            out.write("\n");
            out.write("\t\t\treturn foundItem;\n");
            out.write("\t\t}\n");
            out.write("\n");
            out.write("\t\tHistory.after(\n");
            out.write("\t\t\t'stateChange',\n");
            out.write("\t\t\tfunction(event) {\n");
            out.write("\t\t\t\tvar nodePlid = event.newVal[HISTORY_SELECTED_PLID];\n");
            out.write("\n");
            out.write("\t\t\t\tif (Lang.isValue(nodePlid)) {\n");
            out.write("\t\t\t\t\tvar node = findNodeByPlid(null, nodePlid);\n");
            out.write("\n");
            out.write("\t\t\t\t\tif (node) {\n");
            out.write("\t\t\t\t\t\tvar lastSelected = treeview.get('lastSelected');\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tif (lastSelected) {\n");
            out.write("\t\t\t\t\t\t\tlastSelected.unselect();\n");
            out.write("\t\t\t\t\t\t}\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tnode.select();\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tvar io = layoutsContainer.io;\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tvar uri = Lang.sub(\n");
            out.write("\t\t\t\t\t\t\tLAYOUT_URL,\n");
            out.write("\t\t\t\t\t\t\t{\n");
            out.write("\t\t\t\t\t\t\t\thistoryKey: '',\n");
            out.write("\t\t\t\t\t\t\t\tselPlid: nodePlid\n");
            out.write("\t\t\t\t\t\t\t}\n");
            out.write("\t\t\t\t\t\t);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tio.set('uri', uri);\n");
            out.write("\n");
            out.write("\t\t\t\t\t\tio.start();\n");
            out.write("\t\t\t\t\t}\n");
            out.write("\t\t\t\t}\n");
            out.write("\t\t\t}\n");
            out.write("\t\t);\n");
            out.write("\t");
          }
          if (_jspx_th_c_005fif_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
          out.write('\n');
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
        return;
      }
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
      out.write("\n");
      out.write("\n");
      out.write("<div class=\"lfr-tree-loading\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_page_context))
        return;
      out.write("treeLoading");
      out.print( treeLoading );
      out.write("\">\n");
      out.write("\t<span class=\"aui-icon aui-icon-loading lfr-tree-loading-icon\"></span>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"lfr-tree\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_page_context))
        return;
      out.print( HtmlUtil.escape(treeId) );
      out.write("Output\"></div>");
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

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005frenderURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
    // /html/portlet/layouts_admin/init_attributes.jspf(177,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/layouts_admin/init_attributes.jspf(177,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f0.setValue("/layouts_admin/edit_layouts");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005fresourceURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
    // /html/portlet/layouts_admin/init_attributes.jspf(189,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f5.setName("struts_action");
    // /html/portlet/layouts_admin/init_attributes.jspf(189,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f5.setValue("/layouts_admin/edit_layouts");
    int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
    if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
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

  private boolean _jspx_meth_portlet_005fnamespace_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f2 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    int _jspx_eval_portlet_005fnamespace_005f2 = _jspx_th_portlet_005fnamespace_005f2.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f3 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    int _jspx_eval_portlet_005fnamespace_005f3 = _jspx_th_portlet_005fnamespace_005f3.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f4 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    int _jspx_eval_portlet_005fnamespace_005f4 = _jspx_th_portlet_005fnamespace_005f4.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }
}
