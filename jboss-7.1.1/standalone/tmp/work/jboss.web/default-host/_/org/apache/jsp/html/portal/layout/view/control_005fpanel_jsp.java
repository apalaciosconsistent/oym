package org.apache.jsp.html.portal.layout.view;

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
import com.liferay.portal.DuplicateUserEmailAddressException;
import com.liferay.portal.LayoutFriendlyURLException;
import com.liferay.portal.LayoutPermissionException;
import com.liferay.portal.NoSuchResourceException;
import com.liferay.portal.PortletActiveException;
import com.liferay.portal.RequiredLayoutException;
import com.liferay.portal.RequiredRoleException;
import com.liferay.portal.ReservedUserEmailAddressException;
import com.liferay.portal.UserActiveException;
import com.liferay.portal.UserEmailAddressException;
import com.liferay.portal.UserLockoutException;
import com.liferay.portal.UserPasswordException;
import com.liferay.portal.UserReminderQueryException;
import com.liferay.portal.kernel.templateparser.TransformException;
import com.liferay.portal.service.permission.OrganizationPermissionUtil;
import com.liferay.portal.struts.PortletRequestProcessor;
import com.liferay.portal.upload.LiferayFileUpload;
import com.liferay.portal.util.LayoutLister;
import com.liferay.portal.util.LayoutView;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.portlet.layoutconfiguration.util.RuntimePortletUtil;
import com.liferay.portlet.usersadmin.util.UsersAdminUtil;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.taglib.tiles.ComponentConstants;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.TilesUtil;

public final class control_005fpanel_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(5);
    _jspx_dependants.add("/html/portal/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portal/layout/view/panel_content.jspf");
    _jspx_dependants.add("/html/portal/layout/view/common.jspf");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005flayout_0026_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005flayout_0026_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005flayout_0026_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
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
      out.write('\n');
      out.write('\n');

String ppid = ParamUtil.getString(request, "p_p_id");

String controlPanelCategory = themeDisplay.getControlPanelCategory();

if (controlPanelCategory.equals(PortletCategoryKeys.CONTENT) && Validator.isNull(ppid)) {
	List<Portlet> portlets = PortalUtil.getControlPanelPortlets(PortletCategoryKeys.CONTENT, themeDisplay);

	for (Portlet portlet : portlets) {
		if (PortletPermissionUtil.contains(permissionChecker, scopeGroupId, 0, portlet.getPortletId(), ActionKeys.ACCESS_IN_CONTROL_PANEL, true)) {
			ppid = portlet.getPortletId();

			break;
		}
	}
}

if (ppid.equals(PortletKeys.PORTLET_CONFIGURATION)) {
	String portletResource = ParamUtil.getString(request, PortalUtil.getPortletNamespace(ppid) + "portletResource");

	if (Validator.isNull(portletResource)) {
		portletResource = ParamUtil.getString(request, "portletResource");
	}

	if (Validator.isNotNull(portletResource)) {
		String strutsAction = ParamUtil.getString(request, PortalUtil.getPortletNamespace(ppid) + "struts_action");

		if (!strutsAction.startsWith("/portlet_configuration/")) {
			ppid = portletResource;
		}
	}
}

if (ppid.equals(PortletKeys.PLUGIN_INSTALLER)) {
	ppid = PortletKeys.ADMIN_PLUGINS;
}

String category = PortalUtil.getControlPanelCategory(ppid, themeDisplay);

List<Layout> scopeLayouts = new ArrayList<Layout>();

Portlet portlet = null;

boolean denyAccess = false;

if (Validator.isNotNull(ppid)) {
	portlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), ppid);

	if ((portlet == null) ||
		(!portlet.isSystem() && !PortalUtil.isControlPanelPortlet(ppid, category, themeDisplay)) && !PortalUtil.isAllowAddPortletDefaultResource(request, portlet)) {

		denyAccess = true;
	}
}

request.setAttribute("control_panel.jsp-ppid", ppid);

      out.write('\n');
      out.write('\n');
      //  c:choose
      com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f0.setParent(null);
      int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
      if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write('\n');
        out.write('	');
        //  c:when
        com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f0 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
        _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
        // /html/portal/layout/view/control_panel.jsp(78,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fwhen_005f0.setTest( !themeDisplay.isStateExclusive() && !themeDisplay.isStatePopUp() );
        int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
        if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\n");
          out.write("\t\t");

		String panelBodyCssClass = "panel-page-body";
		String panelCategory = "lfr-ctrl-panel";
		String categoryTitle = Validator.isNotNull(category) ? LanguageUtil.get(pageContext, "category." + category) : StringPool.BLANK;

		if (!layoutTypePortlet.hasStateMax()) {
			panelBodyCssClass += " panel-page-frontpage";
		}
		else {
			panelBodyCssClass += " panel-page-application";
		}

		if (category.equals(PortletCategoryKeys.CONTENT)) {
			panelCategory += " panel-manage-content";
		}
		else if (category.equals(PortletCategoryKeys.MY)) {
			panelCategory += " panel-manage-my";
			categoryTitle = user.getFullName();
		}
		else if (category.equals(PortletCategoryKeys.PORTAL)) {
			panelCategory += " panel-manage-portal";

			if (CompanyLocalServiceUtil.getCompaniesCount(false) > 1) {
				categoryTitle += " " + company.getName();
			}
		}
		else if (category.equals(PortletCategoryKeys.SERVER)) {
			panelCategory += " panel-manage-server";
		}
		else {
			panelCategory += " panel-manage-frontpage";
		}

		Layout scopeLayout = null;
		Group curGroup = themeDisplay.getScopeGroup();

		if (curGroup.isLayout()) {
			scopeLayout = LayoutLocalServiceUtil.getLayout(curGroup.getClassPK());
			curGroup = scopeLayout.getGroup();
		}

		if (Validator.isNotNull(categoryTitle) && !category.equals(PortletCategoryKeys.CONTENT)) {
			PortalUtil.addPortletBreadcrumbEntry(request, categoryTitle, null);
		}
		
          out.write("\n");
          out.write("\n");
          out.write("\t\t<div id=\"content-wrapper\">\n");
          out.write("\t\t\t");
          //  aui:layout
          com.liferay.taglib.aui.LayoutTag _jspx_th_aui_005flayout_005f0 = (com.liferay.taglib.aui.LayoutTag) _005fjspx_005ftagPool_005faui_005flayout_0026_005fcssClass.get(com.liferay.taglib.aui.LayoutTag.class);
          _jspx_th_aui_005flayout_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005flayout_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
          // /html/portal/layout/view/control_panel.jsp(127,3) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005flayout_005f0.setCssClass( panelCategory );
          int _jspx_eval_aui_005flayout_005f0 = _jspx_th_aui_005flayout_005f0.doStartTag();
          if (_jspx_eval_aui_005flayout_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            //  aui:column
            com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f0 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.get(com.liferay.taglib.aui.ColumnTag.class);
            _jspx_th_aui_005fcolumn_005f0.setPageContext(_jspx_page_context);
            _jspx_th_aui_005fcolumn_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f0);
            // /html/portal/layout/view/control_panel.jsp(128,4) name = columnWidth type = int reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005fcolumn_005f0.setColumnWidth( 25 );
            // /html/portal/layout/view/control_panel.jsp(128,4) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005fcolumn_005f0.setCssClass("panel-page-menu");
            // /html/portal/layout/view/control_panel.jsp(128,4) name = first type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005fcolumn_005f0.setFirst( true );
            int _jspx_eval_aui_005fcolumn_005f0 = _jspx_th_aui_005fcolumn_005f0.doStartTag();
            if (_jspx_eval_aui_005fcolumn_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_liferay_002dportlet_005fruntime_005f0(_jspx_th_aui_005fcolumn_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_aui_005fcolumn_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f0);
              return;
            }
            _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f0);
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t");
            //  aui:column
            com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f1 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.get(com.liferay.taglib.aui.ColumnTag.class);
            _jspx_th_aui_005fcolumn_005f1.setPageContext(_jspx_page_context);
            _jspx_th_aui_005fcolumn_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f0);
            // /html/portal/layout/view/control_panel.jsp(132,4) name = columnWidth type = int reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005fcolumn_005f1.setColumnWidth( 75 );
            // /html/portal/layout/view/control_panel.jsp(132,4) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005fcolumn_005f1.setCssClass( panelBodyCssClass );
            // /html/portal/layout/view/control_panel.jsp(132,4) name = last type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005fcolumn_005f1.setLast( true );
            int _jspx_eval_aui_005fcolumn_005f1 = _jspx_th_aui_005fcolumn_005f1.doStartTag();
            if (_jspx_eval_aui_005fcolumn_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              out.write('\n');
              out.write('\n');

if (!denyAccess && (themeDisplay.isStateExclusive() || themeDisplay.isStatePopUp() || (layoutTypePortlet.hasStateMax() && (portlet != null)) || Validator.isNotNull(controlPanelCategory))) {
	String velocityTemplateId = null;

	String content = null;

	if (themeDisplay.isStateExclusive()) {
		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "exclusive";

		content = LayoutTemplateLocalServiceUtil.getContent("exclusive", true, theme.getThemeId());
	}
	else if (themeDisplay.isStatePopUp()) {
		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "pop_up";

		content = LayoutTemplateLocalServiceUtil.getContent("pop_up", true, theme.getThemeId());
	}
	else if (layoutTypePortlet.hasStateMax()) {
		String portletTitle = PortalUtil.getPortletTitle(portlet, application, locale);

		PortletURL portletURL = new PortletURLImpl(request, ppid, plid, PortletRequest.RENDER_PHASE);

		portletURL.setPortletMode(PortletMode.VIEW);
		portletURL.setWindowState(WindowState.MAXIMIZED);

		PortalUtil.addPortletBreadcrumbEntry(request, portletTitle, portletURL.toString());

		ppid = StringUtil.split(layoutTypePortlet.getStateMax())[0];

		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "max";

		content = LayoutTemplateLocalServiceUtil.getContent("max", true, theme.getThemeId());
	}
	else {
		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "max";

		content = LayoutTemplateLocalServiceUtil.getContent("max", true, theme.getThemeId());

		Set<String> runtimePortletIds = (Set<String>)request.getAttribute(WebKeys.RUNTIME_PORTLET_IDS);

		if (runtimePortletIds == null) {
			runtimePortletIds = new HashSet<String>();
		}

		runtimePortletIds.add(ppid);

		request.setAttribute(WebKeys.RUNTIME_PORTLET_IDS, runtimePortletIds);
	}

	StringBundler sb = null;

	if (Validator.isNotNull(velocityTemplateId) && Validator.isNotNull(content)) {
		sb = RuntimePortletUtil.getProcessedTemplate(application, request, response, pageContext, out, ppid, velocityTemplateId, content);
	}
	
              out.write("\n");
              out.write("\n");
              out.write("\t<div class=\"site-breadcrumbs\" id=\"breadcrumbs\">\n");
              out.write("\t\t<h1>\n");
              out.write("\t\t\t<span>\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_aui_005fcolumn_005f1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t</span>\n");
              out.write("\t\t</h1>\n");
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_liferay_002dui_005fbreadcrumb_005f0(_jspx_th_aui_005fcolumn_005f1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t</div>\n");
              out.write("\n");
              out.write("\t");

	if (sb != null) {
		sb.writeTo(pageContext.getOut());
	}
}
else {
	String description = StringPool.BLANK;

	String className = "portlet-msg-info";

	if (denyAccess) {
		description = LanguageUtil.get(pageContext, "you-do-not-have-the-required-permissions-to-access-this-application");

		className = "portlet-msg-error";
	}

	if (Validator.isNull(description)) {
		description = LanguageUtil.get(pageContext, "please-select-a-tool-from-the-left-menu");
	}

              out.write("\n");
              out.write("\n");
              out.write("\t<div class=\"");
              out.print( className );
              out.write("\">\n");
              out.write("\t\t");
              out.print( description );
              out.write("\n");
              out.write("\t</div>\n");
              out.write("\n");

}

              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_aui_005fcolumn_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f1);
              return;
            }
            _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f1);
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_aui_005flayout_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005flayout_0026_005fcssClass.reuse(_jspx_th_aui_005flayout_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005flayout_0026_005fcssClass.reuse(_jspx_th_aui_005flayout_005f0);
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\t");
        }
        if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
        out.write('\n');
        out.write('	');
        //  c:otherwise
        com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
        _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
        int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
        if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write('\n');
          out.write('	');
          out.write('	');
          out.write('\n');
          out.write('\n');

if (!denyAccess && (themeDisplay.isStateExclusive() || themeDisplay.isStatePopUp() || (layoutTypePortlet.hasStateMax() && (portlet != null)) || Validator.isNotNull(controlPanelCategory))) {
	String velocityTemplateId = null;

	String content = null;

	if (themeDisplay.isStateExclusive()) {
		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "exclusive";

		content = LayoutTemplateLocalServiceUtil.getContent("exclusive", true, theme.getThemeId());
	}
	else if (themeDisplay.isStatePopUp()) {
		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "pop_up";

		content = LayoutTemplateLocalServiceUtil.getContent("pop_up", true, theme.getThemeId());
	}
	else if (layoutTypePortlet.hasStateMax()) {
		String portletTitle = PortalUtil.getPortletTitle(portlet, application, locale);

		PortletURL portletURL = new PortletURLImpl(request, ppid, plid, PortletRequest.RENDER_PHASE);

		portletURL.setPortletMode(PortletMode.VIEW);
		portletURL.setWindowState(WindowState.MAXIMIZED);

		PortalUtil.addPortletBreadcrumbEntry(request, portletTitle, portletURL.toString());

		ppid = StringUtil.split(layoutTypePortlet.getStateMax())[0];

		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "max";

		content = LayoutTemplateLocalServiceUtil.getContent("max", true, theme.getThemeId());
	}
	else {
		velocityTemplateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "max";

		content = LayoutTemplateLocalServiceUtil.getContent("max", true, theme.getThemeId());

		Set<String> runtimePortletIds = (Set<String>)request.getAttribute(WebKeys.RUNTIME_PORTLET_IDS);

		if (runtimePortletIds == null) {
			runtimePortletIds = new HashSet<String>();
		}

		runtimePortletIds.add(ppid);

		request.setAttribute(WebKeys.RUNTIME_PORTLET_IDS, runtimePortletIds);
	}

	StringBundler sb = null;

	if (Validator.isNotNull(velocityTemplateId) && Validator.isNotNull(content)) {
		sb = RuntimePortletUtil.getProcessedTemplate(application, request, response, pageContext, out, ppid, velocityTemplateId, content);
	}
	
          out.write("\n");
          out.write("\n");
          out.write("\t<div class=\"site-breadcrumbs\" id=\"breadcrumbs\">\n");
          out.write("\t\t<h1>\n");
          out.write("\t\t\t<span>\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fotherwise_005f0, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t</span>\n");
          out.write("\t\t</h1>\n");
          out.write("\n");
          out.write("\t\t");
          if (_jspx_meth_liferay_002dui_005fbreadcrumb_005f1(_jspx_th_c_005fotherwise_005f0, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t</div>\n");
          out.write("\n");
          out.write("\t");

	if (sb != null) {
		sb.writeTo(pageContext.getOut());
	}
}
else {
	String description = StringPool.BLANK;

	String className = "portlet-msg-info";

	if (denyAccess) {
		description = LanguageUtil.get(pageContext, "you-do-not-have-the-required-permissions-to-access-this-application");

		className = "portlet-msg-error";
	}

	if (Validator.isNull(description)) {
		description = LanguageUtil.get(pageContext, "please-select-a-tool-from-the-left-menu");
	}

          out.write("\n");
          out.write("\n");
          out.write("\t<div class=\"");
          out.print( className );
          out.write("\">\n");
          out.write("\t\t");
          out.print( description );
          out.write("\n");
          out.write("\t</div>\n");
          out.write("\n");

}

          out.write('\n');
          out.write('	');
        }
        if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
        out.write('\n');
      }
      if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f0 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /html/portal/layout/view/common.jspf(17,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f0.setTest( !themeDisplay.isFacebook() && !themeDisplay.isStateExclusive() && !themeDisplay.isStatePopUp() && !themeDisplay.isWidget() );
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write('\n');
        out.write('\n');
        out.write('	');

	for (String portletId : PropsValues.LAYOUT_STATIC_PORTLETS_ALL) {
		if (PortletLocalServiceUtil.hasPortlet(company.getCompanyId(), portletId)) {
	
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t");
        //  liferay-portlet:runtime
        com.liferay.taglib.portletext.RuntimeTag _jspx_th_liferay_002dportlet_005fruntime_005f1 = (com.liferay.taglib.portletext.RuntimeTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody.get(com.liferay.taglib.portletext.RuntimeTag.class);
        _jspx_th_liferay_002dportlet_005fruntime_005f1.setPageContext(_jspx_page_context);
        _jspx_th_liferay_002dportlet_005fruntime_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
        // /html/portal/layout/view/common.jspf(24,3) name = portletName type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_liferay_002dportlet_005fruntime_005f1.setPortletName( portletId );
        int _jspx_eval_liferay_002dportlet_005fruntime_005f1 = _jspx_th_liferay_002dportlet_005fruntime_005f1.doStartTag();
        if (_jspx_th_liferay_002dportlet_005fruntime_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody.reuse(_jspx_th_liferay_002dportlet_005fruntime_005f1);
          return;
        }
        _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody.reuse(_jspx_th_liferay_002dportlet_005fruntime_005f1);
        out.write('\n');
        out.write('\n');
        out.write('	');

		}
	}
	
        out.write('\n');
        out.write('\n');
      }
      if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
      out.write('\n');
      out.write('\n');
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f1 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f1.setParent(null);
      // /html/portal/layout/view/common.jspf(33,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f1.setTest( PropsValues.WEB_SERVER_DISPLAY_NODE );
      int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
      if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t<div class=\"portlet-msg-info\">\n");
        out.write("\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fif_005f1, _jspx_page_context))
          return;
        out.write(':');
        out.write(' ');
        out.print( PortalUtil.getComputerName().toLowerCase() );
        out.write("\n");
        out.write("\t</div>\n");
      }
      if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
      out.write("\n");
      out.write("\n");
      out.write("<form action=\"#\" id=\"hrefFm\" method=\"post\" name=\"hrefFm\">\n");
      out.write("\t<span></span>\n");
      out.write("</form>");
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

  private boolean _jspx_meth_liferay_002dportlet_005fruntime_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fcolumn_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:runtime
    com.liferay.taglib.portletext.RuntimeTag _jspx_th_liferay_002dportlet_005fruntime_005f0 = (com.liferay.taglib.portletext.RuntimeTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody.get(com.liferay.taglib.portletext.RuntimeTag.class);
    _jspx_th_liferay_002dportlet_005fruntime_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005fruntime_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f0);
    // /html/portal/layout/view/control_panel.jsp(129,5) name = portletName type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dportlet_005fruntime_005f0.setPortletName("160");
    int _jspx_eval_liferay_002dportlet_005fruntime_005f0 = _jspx_th_liferay_002dportlet_005fruntime_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005fruntime_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody.reuse(_jspx_th_liferay_002dportlet_005fruntime_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005fruntime_0026_005fportletName_005fnobody.reuse(_jspx_th_liferay_002dportlet_005fruntime_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fcolumn_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f1);
    // /html/portal/layout/view/panel_content.jspf(75,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("breadcrumb");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fbreadcrumb_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fcolumn_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:breadcrumb
    com.liferay.taglib.ui.BreadcrumbTag _jspx_th_liferay_002dui_005fbreadcrumb_005f0 = (com.liferay.taglib.ui.BreadcrumbTag) _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody.get(com.liferay.taglib.ui.BreadcrumbTag.class);
    _jspx_th_liferay_002dui_005fbreadcrumb_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fbreadcrumb_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f1);
    int _jspx_eval_liferay_002dui_005fbreadcrumb_005f0 = _jspx_th_liferay_002dui_005fbreadcrumb_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fbreadcrumb_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody.reuse(_jspx_th_liferay_002dui_005fbreadcrumb_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody.reuse(_jspx_th_liferay_002dui_005fbreadcrumb_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /html/portal/layout/view/panel_content.jspf(75,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("breadcrumb");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fbreadcrumb_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:breadcrumb
    com.liferay.taglib.ui.BreadcrumbTag _jspx_th_liferay_002dui_005fbreadcrumb_005f1 = (com.liferay.taglib.ui.BreadcrumbTag) _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody.get(com.liferay.taglib.ui.BreadcrumbTag.class);
    _jspx_th_liferay_002dui_005fbreadcrumb_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fbreadcrumb_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    int _jspx_eval_liferay_002dui_005fbreadcrumb_005f1 = _jspx_th_liferay_002dui_005fbreadcrumb_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fbreadcrumb_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody.reuse(_jspx_th_liferay_002dui_005fbreadcrumb_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fbreadcrumb_005fnobody.reuse(_jspx_th_liferay_002dui_005fbreadcrumb_005f1);
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
    // /html/portal/layout/view/common.jspf(35,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("node");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }
}
