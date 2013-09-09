package org.apache.jsp.html.taglib.ui.icon;

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


private static final String _AUI_PATH = "../aui/";

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(5);
    _jspx_dependants.add("/html/taglib/ui/icon/init.jsp");
    _jspx_dependants.add("/html/taglib/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/taglib/init-ext.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005flang_005fid_005fhref_005fdata_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005flang_005fid_005fhref_005fdata_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005flang_005fid_005fhref_005fdata_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
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

String id = (String)request.getAttribute("liferay-ui:icon:id");

IntegerWrapper iconListIconCount = (IntegerWrapper)request.getAttribute("liferay-ui:icon-list:icon-count");

if (iconListIconCount != null) {
	iconListIconCount.increment();
}

boolean iconListShowWhenSingleIcon = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon-list:showWhenSingleIcon"));

Boolean iconListSingleIcon = (Boolean)request.getAttribute("liferay-ui:icon-list:single-icon");

IntegerWrapper iconMenuIconCount = (IntegerWrapper)request.getAttribute("liferay-ui:icon-menu:icon-count");

if (iconMenuIconCount != null) {
	iconMenuIconCount.increment();
}

Boolean iconMenuSingleIcon = (Boolean)request.getAttribute("liferay-ui:icon-menu:single-icon");

boolean iconMenuShowWhenSingleIcon = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon-menu:showWhenSingleIcon"));

String image = (String)request.getAttribute("liferay-ui:icon:image");
String imageHover = (String)request.getAttribute("liferay-ui:icon:imageHover");

boolean auiImage = (image != null) && image.startsWith(_AUI_PATH);

String alt = (String)request.getAttribute("liferay-ui:icon:alt");

String message = (String)request.getAttribute("liferay-ui:icon:message");

if (message == null) {
	message = StringUtil.replace(image, StringPool.UNDERLINE, StringPool.DASH);
}

String src = (String)request.getAttribute("liferay-ui:icon:src");
String srcHover = (String)request.getAttribute("liferay-ui:icon:srcHover");

if (Validator.isNull(src)) {
	if (auiImage) {
		src = themeDisplay.getPathThemeImages().concat("/spacer.png");
	}
	else if (Validator.isNotNull(image)) {
		StringBundler sb = new StringBundler(4);

		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/common/");
		sb.append(image);
		sb.append(".png");

		src = StringUtil.replace(sb.toString(), "common/../", "");
	}
}

if (Validator.isNull(srcHover) && Validator.isNotNull(imageHover)) {
	StringBundler sb = new StringBundler(4);

	sb.append(themeDisplay.getPathThemeImages());
	sb.append("/common/");
	sb.append(imageHover);
	sb.append(".png");

	srcHover = sb.toString();
}

String url = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:url"));

String method = (String)request.getAttribute("liferay-ui:icon:method");

if (Validator.isNull(method)) {
	method = "post";
}

String target = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:target"));
boolean label = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon:label"));
String lang = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:lang"));
boolean localizeMessage = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon:localizeMessage"));
boolean toolTip = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon:toolTip"));
String cssClass = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:cssClass"));
Map<String, Object> data = (Map<String, Object>)request.getAttribute("liferay-ui:icon:data");
String onClick = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:onClick"));

if ((iconListIconCount != null) || (iconListSingleIcon != null)) {
	label = true;
}

if ((iconMenuIconCount != null) || (iconMenuSingleIcon != null)) {
	label = true;
}

String details = null;

if (alt != null) {
	details = " alt=\"" + LanguageUtil.get(pageContext, alt) + "\"";
}
else if (label) {
	details = " alt=\"\"";
}
else {
	StringBundler sb = new StringBundler(6);

	sb.append(" alt=\"");
	sb.append(LanguageUtil.get(pageContext, message));
	sb.append("\"");

	if (toolTip) {
		sb.append(" onmouseover=\"Liferay.Portal.ToolTip.show(this, '");
		sb.append(UnicodeLanguageUtil.get(pageContext, message));
		sb.append("')\"");
	}
	else {
		sb.append(" title=\"");
		sb.append(LanguageUtil.get(pageContext, message));
		sb.append("\"");
	}

	details = sb.toString();
}

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');

String cssClassHtml = StringPool.BLANK;

if (Validator.isNotNull(cssClass)) {
	cssClassHtml = "class=\"".concat(cssClass).concat("\"");
}

if (Validator.isNotNull(src) && themeDisplay.isThemeImagesFastLoad() && !auiImage) {
	SpriteImage spriteImage = null;
	String spriteFileName = null;
	String spriteFileURL = null;

	String imageFileName = StringUtil.replace(src, "common/../", "");

	String contextPath = theme.getContextPath();

	String imagesPath = contextPath.concat(theme.getImagesPath());

	if (imageFileName.startsWith(imagesPath)) {
		imageFileName = imageFileName.substring(imagesPath.length());

		spriteImage = theme.getSpriteImage(imageFileName);

		if (spriteImage != null) {
			spriteFileName = spriteImage.getSpriteFileName();

			if (BrowserSnifferUtil.isIe(request) && (BrowserSnifferUtil.getMajorVersion(request) < 7)) {
				spriteFileName = StringUtil.replace(spriteFileName, ".png", ".gif");
			}

			String cdnBaseURL = themeDisplay.getCDNBaseURL();

			spriteFileURL = cdnBaseURL.concat(spriteFileName);
		}
	}

	if (spriteImage == null) {
		Portlet portlet = (Portlet)request.getAttribute("liferay-portlet:icon_portlet:portlet");

		if (portlet == null) {
			portlet = (Portlet)request.getAttribute(WebKeys.RENDER_PORTLET);
		}

		if (portlet != null) {
			PortletApp portletApp = portlet.getPortletApp();

			spriteImage = portletApp.getSpriteImage(imageFileName);

			if (spriteImage != null) {
				spriteFileName = spriteImage.getSpriteFileName();

				if (BrowserSnifferUtil.isIe(request) && (BrowserSnifferUtil.getMajorVersion(request) < 7)) {
					spriteFileName = StringUtil.replace(spriteFileName, ".png", ".gif");
				}

				String cdnBaseURL = themeDisplay.getCDNBaseURL();

				spriteFileURL = cdnBaseURL.concat(spriteFileName);
			}
		}
	}

	if (spriteImage != null) {
		String themeImagesPath = themeDisplay.getPathThemeImages();

		src = themeImagesPath.concat("/spacer.png");

		StringBundler sb = new StringBundler(10);

		sb.append(details);
		sb.append(" style=\"background-image: url('");
		sb.append(spriteFileURL);
		sb.append("'); background-position: 50% -");
		sb.append(spriteImage.getOffset());
		sb.append("px; background-repeat: no-repeat; height: ");
		sb.append(spriteImage.getHeight());
		sb.append("px; width: ");
		sb.append(spriteImage.getWidth());
		sb.append("px;\"");

		details = sb.toString();
	}
}

String imgClass = "icon";

if (auiImage) {
	StringBundler sb = new StringBundler(4);

	sb.append(details);
	sb.append(" style=\"background-image: url('");
	sb.append(themeDisplay.getPathThemeImages());
	sb.append("/aui/icon_sprite.png'); height: 16px; width: 16px;\"");

	details = sb.toString();

	imgClass = imgClass.concat(" aui-icon-").concat(image.substring(_AUI_PATH.length()));
}

boolean urlIsNotNull = Validator.isNotNull(url);

      out.write('\n');
      out.write('\n');
      //  liferay-util:buffer
      com.liferay.taglib.util.BufferTag _jspx_th_liferay_002dutil_005fbuffer_005f0 = (com.liferay.taglib.util.BufferTag) _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.get(com.liferay.taglib.util.BufferTag.class);
      _jspx_th_liferay_002dutil_005fbuffer_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dutil_005fbuffer_005f0.setParent(null);
      // /html/taglib/ui/icon/page.jsp(121,0) name = var type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_liferay_002dutil_005fbuffer_005f0.setVar("linkContent");
      int _jspx_eval_liferay_002dutil_005fbuffer_005f0 = _jspx_th_liferay_002dutil_005fbuffer_005f0.doStartTag();
      if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dutil_005fbuffer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dutil_005fbuffer_005f0.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          //  c:if
          com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f0 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f0);
          // /html/taglib/ui/icon/page.jsp(122,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_c_005fif_005f0.setTest( Validator.isNotNull(src) );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write('\n');
            out.write('	');
            out.write('	');
            //  c:choose
            com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
            _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
            _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
            int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
            if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t");
              //  c:when
              com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f0 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /html/taglib/ui/icon/page.jsp(124,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fwhen_005f0.setTest( urlIsNotNull );
              int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
              if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t<img class=\"");
                out.print( imgClass );
                out.write("\" src=\"");
                out.print( src );
                out.write('"');
                out.write(' ');
                out.print( details );
                out.write(" />\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
              out.write("\n");
              out.write("\t\t\t");
              //  c:otherwise
              com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t<img class=\"");
                out.print( imgClass );
                out.write("\" id=\"");
                out.print( id );
                out.write("\" src=\"");
                out.print( src );
                out.write('"');
                out.write(' ');
                out.print( details );
                out.write(" />\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
              out.write('\n');
              out.write('	');
              out.write('	');
            }
            if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
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
          //  c:choose
          com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f0);
          int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
          if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write('\n');
            out.write('	');
            out.write('	');
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f1 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
            // /html/taglib/ui/icon/page.jsp(134,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f1.setTest( (iconMenuIconCount != null) && ((iconMenuSingleIcon == null) || iconMenuShowWhenSingleIcon) );
            int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
            if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t");
              //  liferay-ui:message
              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
              _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
              // /html/taglib/ui/icon/page.jsp(135,3) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dui_005fmessage_005f0.setKey( message );
              // /html/taglib/ui/icon/page.jsp(135,3) name = localizeKey type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dui_005fmessage_005f0.setLocalizeKey( localizeMessage );
              int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
              out.write('\n');
              out.write('	');
              out.write('	');
            }
            if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
            out.write('\n');
            out.write('	');
            out.write('	');
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f2 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
            // /html/taglib/ui/icon/page.jsp(137,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f2.setTest( (iconListIconCount != null) && ((iconListSingleIcon == null) || iconListShowWhenSingleIcon) );
            int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
            if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t<span class=\"taglib-text\">");
              //  liferay-ui:message
              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
              _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
              // /html/taglib/ui/icon/page.jsp(138,29) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dui_005fmessage_005f1.setKey( message );
              // /html/taglib/ui/icon/page.jsp(138,29) name = localizeKey type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dui_005fmessage_005f1.setLocalizeKey( localizeMessage );
              int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
              if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
              out.write("</span>\n");
              out.write("\t\t");
            }
            if (_jspx_th_c_005fwhen_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
            out.write('\n');
            out.write('	');
            out.write('	');
            //  c:otherwise
            com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
            _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
            _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
            int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
            if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f1 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
              _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
              // /html/taglib/ui/icon/page.jsp(141,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fif_005f1.setTest( label );
              int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
              if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t<span class=\"taglib-text\">");
                //  liferay-ui:message
                com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                // /html/taglib/ui/icon/page.jsp(142,30) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fmessage_005f2.setKey( message );
                // /html/taglib/ui/icon/page.jsp(142,30) name = localizeKey type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fmessage_005f2.setLocalizeKey( localizeMessage );
                int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
                if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005flocalizeKey_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
                out.write("</span>\n");
                out.write("\t\t\t");
              }
              if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
              out.write('\n');
              out.write('	');
              out.write('	');
            }
            if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
            out.write('\n');
            out.write('	');
          }
          if (_jspx_th_c_005fchoose_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
          out.write('\n');
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
      java.lang.String linkContent = null;
      linkContent = (java.lang.String) _jspx_page_context.findAttribute("linkContent");
      out.write('\n');
      out.write('\n');
      //  c:choose
      com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f2.setParent(null);
      int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
      if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write('\n');
        out.write('	');
        //  c:when
        com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f3 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
        _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
        _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
        // /html/taglib/ui/icon/page.jsp(149,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fwhen_005f3.setTest( (iconListIconCount != null) && ((iconListSingleIcon == null) || iconListShowWhenSingleIcon) );
        int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
        if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t<li ");
          out.print( cssClassHtml );
          out.write(">\n");
          out.write("\t\t\t");
          //  c:choose
          com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
          int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
          if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f4 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
            // /html/taglib/ui/icon/page.jsp(152,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f4.setTest( urlIsNotNull );
            int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
            if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  aui:a
              com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f0 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005flang_005fid_005fhref_005fdata_005fcssClass.get(com.liferay.taglib.aui.ATag.class);
              _jspx_th_aui_005fa_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fa_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
              // /html/taglib/ui/icon/page.jsp(153,5) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f0.setCssClass("taglib-icon");
              // /html/taglib/ui/icon/page.jsp(153,5) name = data type = java.util.Map reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f0.setData( data );
              // /html/taglib/ui/icon/page.jsp(153,5) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f0.setHref( url );
              // /html/taglib/ui/icon/page.jsp(153,5) name = id type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f0.setId( id );
              // /html/taglib/ui/icon/page.jsp(153,5) name = lang type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f0.setLang( lang );
              // /html/taglib/ui/icon/page.jsp(153,5) name = target type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f0.setTarget( target );
              int _jspx_eval_aui_005fa_005f0 = _jspx_th_aui_005fa_005f0.doStartTag();
              if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t\t");
                out.print( linkContent );
                out.write("\n");
                out.write("\t\t\t\t\t");
              }
              if (_jspx_th_aui_005fa_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005flang_005fid_005fhref_005fdata_005fcssClass.reuse(_jspx_th_aui_005fa_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005flang_005fid_005fhref_005fdata_005fcssClass.reuse(_jspx_th_aui_005fa_005f0);
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fwhen_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:otherwise
            com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
            _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
            _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
            int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
            if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              out.print( linkContent );
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
          if (_jspx_th_c_005fchoose_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
          out.write("\n");
          out.write("\t\t</li>\n");
          out.write("\t");
        }
        if (_jspx_th_c_005fwhen_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
        out.write('\n');
        out.write('	');
        //  c:when
        com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f5 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
        _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
        _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
        // /html/taglib/ui/icon/page.jsp(163,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fwhen_005f5.setTest( (iconMenuIconCount != null) && ((iconMenuSingleIcon == null) || iconMenuShowWhenSingleIcon) );
        int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
        if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t<li ");
          out.print( cssClassHtml );
          out.write(">\n");
          out.write("\t\t\t");
          //  c:choose
          com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
          int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
          if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f6 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f6.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
            // /html/taglib/ui/icon/page.jsp(166,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f6.setTest( urlIsNotNull );
            int _jspx_eval_c_005fwhen_005f6 = _jspx_th_c_005fwhen_005f6.doStartTag();
            if (_jspx_eval_c_005fwhen_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  aui:a
              com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f1 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass.get(com.liferay.taglib.aui.ATag.class);
              _jspx_th_aui_005fa_005f1.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fa_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f6);
              // /html/taglib/ui/icon/page.jsp(167,5) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f1.setCssClass("taglib-icon");
              // /html/taglib/ui/icon/page.jsp(167,5) name = data type = java.util.Map reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f1.setData( data );
              // /html/taglib/ui/icon/page.jsp(167,5) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f1.setHref( url );
              // /html/taglib/ui/icon/page.jsp(167,5) name = id type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f1.setId( id );
              // /html/taglib/ui/icon/page.jsp(167,5) name = lang type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f1.setLang( lang );
              // /html/taglib/ui/icon/page.jsp(167,5) name = onClick type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f1.setOnClick( Validator.isNotNull(onClick) ? onClick : "" );
              // /html/taglib/ui/icon/page.jsp(167,5) name = target type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f1.setTarget( target );
              int _jspx_eval_aui_005fa_005f1 = _jspx_th_aui_005fa_005f1.doStartTag();
              if (_jspx_eval_aui_005fa_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t\t");
                out.print( linkContent );
                out.write("\n");
                out.write("\t\t\t\t\t");
              }
              if (_jspx_th_aui_005fa_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass.reuse(_jspx_th_aui_005fa_005f1);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass.reuse(_jspx_th_aui_005fa_005f1);
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fwhen_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f6);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f6);
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:otherwise
            com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
            _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
            _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
            int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
            if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t<span class=\"taglib-icon\">");
              out.print( linkContent );
              out.write("</span>\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fotherwise_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fchoose_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
          out.write("\n");
          out.write("\t\t</li>\n");
          out.write("\t");
        }
        if (_jspx_th_c_005fwhen_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f5);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f5);
        out.write('\n');
        out.write('	');
        //  c:otherwise
        com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f4 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
        _jspx_th_c_005fotherwise_005f4.setPageContext(_jspx_page_context);
        _jspx_th_c_005fotherwise_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
        int _jspx_eval_c_005fotherwise_005f4 = _jspx_th_c_005fotherwise_005f4.doStartTag();
        if (_jspx_eval_c_005fotherwise_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t<span ");
          out.print( cssClassHtml );
          out.write(" >\n");
          out.write("\t\t\t");
          //  c:choose
          com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f5 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f5.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
          int _jspx_eval_c_005fchoose_005f5 = _jspx_th_c_005fchoose_005f5.doStartTag();
          if (_jspx_eval_c_005fchoose_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:when
            com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f7 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
            _jspx_th_c_005fwhen_005f7.setPageContext(_jspx_page_context);
            _jspx_th_c_005fwhen_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
            // /html/taglib/ui/icon/page.jsp(180,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fwhen_005f7.setTest( urlIsNotNull );
            int _jspx_eval_c_005fwhen_005f7 = _jspx_th_c_005fwhen_005f7.doStartTag();
            if (_jspx_eval_c_005fwhen_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  aui:a
              com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f2 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass.get(com.liferay.taglib.aui.ATag.class);
              _jspx_th_aui_005fa_005f2.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fa_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
              // /html/taglib/ui/icon/page.jsp(181,5) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f2.setCssClass("taglib-icon");
              // /html/taglib/ui/icon/page.jsp(181,5) name = data type = java.util.Map reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f2.setData( data );
              // /html/taglib/ui/icon/page.jsp(181,5) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f2.setHref( url );
              // /html/taglib/ui/icon/page.jsp(181,5) name = id type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f2.setId( id );
              // /html/taglib/ui/icon/page.jsp(181,5) name = lang type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f2.setLang( lang );
              // /html/taglib/ui/icon/page.jsp(181,5) name = onClick type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f2.setOnClick( Validator.isNotNull(onClick) ? onClick : "" );
              // /html/taglib/ui/icon/page.jsp(181,5) name = target type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005fa_005f2.setTarget( target );
              int _jspx_eval_aui_005fa_005f2 = _jspx_th_aui_005fa_005f2.doStartTag();
              if (_jspx_eval_aui_005fa_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t\t");
                out.print( linkContent );
                out.write("\n");
                out.write("\t\t\t\t\t");
              }
              if (_jspx_th_aui_005fa_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass.reuse(_jspx_th_aui_005fa_005f2);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fonClick_005flang_005fid_005fhref_005fdata_005fcssClass.reuse(_jspx_th_aui_005fa_005f2);
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fwhen_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f7);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f7);
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:otherwise
            com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f5 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
            _jspx_th_c_005fotherwise_005f5.setPageContext(_jspx_page_context);
            _jspx_th_c_005fotherwise_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
            int _jspx_eval_c_005fotherwise_005f5 = _jspx_th_c_005fotherwise_005f5.doStartTag();
            if (_jspx_eval_c_005fotherwise_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              out.print( linkContent );
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fotherwise_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_c_005fchoose_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
          out.write("\n");
          out.write("\t\t</span>\n");
          out.write("\t");
        }
        if (_jspx_th_c_005fotherwise_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
        out.write('\n');
      }
      if (_jspx_th_c_005fchoose_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
      out.write('\n');
      out.write('\n');

boolean forcePost = method.equals("post") && (url.startsWith(Http.HTTP_WITH_SLASH) || url.startsWith(Http.HTTPS_WITH_SLASH));

      out.write('\n');
      out.write('\n');
      //  c:if
      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f2 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
      _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f2.setParent(null);
      // /html/taglib/ui/icon/page.jsp(197,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
      _jspx_th_c_005fif_005f2.setTest( Validator.isNotNull(srcHover) || forcePost );
      int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
      if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write('\n');
        out.write('	');
        //  aui:script
        com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
        _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
        _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
        // /html/taglib/ui/icon/page.jsp(198,1) name = use type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_aui_005fscript_005f0.setUse("liferay-icon");
        int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
            out = _jspx_page_context.pushBody();
            _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
            _jspx_th_aui_005fscript_005f0.doInitBody();
          }
          do {
            out.write("\n");
            out.write("\t\tLiferay.Icon.register(\n");
            out.write("\t\t\t{\n");
            out.write("\t\t\t\tforcePost: ");
            out.print( forcePost );
            out.write(",\n");
            out.write("\t\t\t\tid: '");
            if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
              return;
            out.print( id );
            out.write("'\n");
            out.write("\n");
            out.write("\t\t\t\t");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f3 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
            // /html/taglib/ui/icon/page.jsp(204,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f3.setTest( Validator.isNotNull(srcHover) );
            int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
            if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t, src: '");
              out.print( src );
              out.write("',\n");
              out.write("\t\t\t\t\tsrcHover: '");
              out.print( srcHover );
              out.write("'\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
            out.write("\n");
            out.write("\t\t\t}\n");
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
          _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
          return;
        }
        _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
        out.write('\n');
      }
      if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
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
}
