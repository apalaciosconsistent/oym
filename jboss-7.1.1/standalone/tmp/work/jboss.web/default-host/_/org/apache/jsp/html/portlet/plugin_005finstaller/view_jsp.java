package org.apache.jsp.html.portlet.plugin_005finstaller;

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
import com.liferay.portal.deploy.DeployUtil;
import com.liferay.portal.kernel.plugin.License;
import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.plugin.RemotePluginPackageRepository;
import com.liferay.portal.kernel.plugin.Screenshot;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentComparator;
import com.liferay.portal.plugin.PluginPackageException;
import com.liferay.portal.plugin.PluginPackageImpl;
import com.liferay.portal.plugin.PluginPackageUtil;
import com.liferay.portal.plugin.RepositoryReport;

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.plugin_installer.browse_repository_jspf");

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(11);
    _jspx_dependants.add("/html/portlet/plugin_installer/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/plugin_installer/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/plugin_installer/view_plugin_package.jspf");
    _jspx_dependants.add("/html/portlet/plugin_installer/upload_file.jspf");
    _jspx_dependants.add("/html/portlet/plugin_installer/download_file.jspf");
    _jspx_dependants.add("/html/portlet/plugin_installer/configuration.jspf");
    _jspx_dependants.add("/html/portlet/plugin_installer/browse_repository.jspf");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005flocalizeTitle_005fbackURL_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fhref;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fbackURL_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005flocalizeTitle_005fbackURL_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fhref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fbackURL_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005faction.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005flocalizeTitle_005fbackURL_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fhref.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fbackURL_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffieldset.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.release();
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

PortalPreferences portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(request);

Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);

      out.write('\n');
      out.write('\n');
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
        // /html/portlet/plugin_installer/view.jsp(20,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
        _jspx_th_c_005fwhen_005f0.setTest( permissionChecker.isOmniadmin() );
        int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
        if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\n");
          out.write("\t\t");

		String uploadProgressId = PortalUtil.generateRandomKey(request, "portlet_plugin_installer_view");

		String tabs1Names = "browse-repository,upload-file,download-file,configuration";
		String tabs1 = ParamUtil.getString(request, "tabs1");

		if (!PrefsPropsUtil.getBoolean(PropsKeys.AUTO_DEPLOY_ENABLED, PropsValues.AUTO_DEPLOY_ENABLED)) {
			tabs1Names = "configuration";
			tabs1 = "configuration";
		}

		String tabs2 = ParamUtil.getString(request, "tabs2");

		if (Validator.isNull(tabs2)) {
			tabs2 = "portlet-plugins";
		}

		String redirect = ParamUtil.getString(request, "redirect");
		String backURL = ParamUtil.getString(request, "backURL");

		String pluginType = null;

		if (tabs2.equals("portlet-plugins")) {
			pluginType = Plugin.TYPE_PORTLET;
		}
		else if (tabs2.equals("theme-plugins")) {
			pluginType = Plugin.TYPE_THEME;
		}
		else if (tabs2.equals("layout-template-plugins")) {
			pluginType = Plugin.TYPE_LAYOUT_TEMPLATE;
		}
		else if (tabs2.equals("hook-plugins")) {
			pluginType = Plugin.TYPE_HOOK;
		}
		else if (tabs2.equals("web-plugins")) {
			pluginType = Plugin.TYPE_WEB;
		}

		String moduleId = ParamUtil.getString(request, "moduleId");
		String repositoryURL = ParamUtil.getString(request, "repositoryURL");

		PortletURL portletURL = renderResponse.createRenderURL();

		portletURL.setParameter("struts_action", "/plugin_installer/view");
		portletURL.setParameter("tabs1", tabs1);
		portletURL.setParameter("tabs2", tabs2);
		portletURL.setParameter("backURL", backURL);
		portletURL.setParameter("moduleId", moduleId);
		portletURL.setParameter("repositoryURL", repositoryURL);

		pageContext.setAttribute("portletURL", portletURL);

		String portletURLString = portletURL.toString();
		
          out.write("\n");
          out.write("\n");
          out.write("\t\t");
          //  aui:form
          com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005faction.get(com.liferay.taglib.aui.FormTag.class);
          _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
          // /html/portlet/plugin_installer/view.jsp(77,2) name = action type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fform_005f0.setAction( portletURL );
          // /html/portlet/plugin_installer/view.jsp(77,2) name = name type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fform_005f0.setName("fm");
          int _jspx_eval_aui_005fform_005f0 = _jspx_th_aui_005fform_005f0.doStartTag();
          if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(78,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f0.setName( Constants.CMD );
            // /html/portlet/plugin_installer/view.jsp(78,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f0.setType("hidden");
            int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
            if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(79,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f1.setName( Constants.PROGRESS_ID );
            // /html/portlet/plugin_installer/view.jsp(79,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f1.setType("hidden");
            // /html/portlet/plugin_installer/view.jsp(79,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f1.setValue( uploadProgressId );
            int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
            if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(80,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f2.setName("tabs1");
            // /html/portlet/plugin_installer/view.jsp(80,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f2.setType("hidden");
            // /html/portlet/plugin_installer/view.jsp(80,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f2.setValue( tabs1 );
            int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
            if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f3 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f3.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(81,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f3.setName("tabs2");
            // /html/portlet/plugin_installer/view.jsp(81,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f3.setType("hidden");
            // /html/portlet/plugin_installer/view.jsp(81,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f3.setValue( tabs2 );
            int _jspx_eval_aui_005finput_005f3 = _jspx_th_aui_005finput_005f3.doStartTag();
            if (_jspx_th_aui_005finput_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f4 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f4.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(82,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f4.setName("backURL");
            // /html/portlet/plugin_installer/view.jsp(82,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f4.setType("hidden");
            // /html/portlet/plugin_installer/view.jsp(82,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f4.setValue( backURL );
            int _jspx_eval_aui_005finput_005f4 = _jspx_th_aui_005finput_005f4.doStartTag();
            if (_jspx_th_aui_005finput_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t");
            //  c:if
            com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f0 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
            _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
            _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(84,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_c_005fif_005f0.setTest( Validator.isNull(moduleId) || Validator.isNull(repositoryURL) );
            int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
            if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t");
              //  aui:input
              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f5 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
              _jspx_th_aui_005finput_005f5.setPageContext(_jspx_page_context);
              _jspx_th_aui_005finput_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/plugin_installer/view.jsp(85,4) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005finput_005f5.setName("redirect");
              // /html/portlet/plugin_installer/view.jsp(85,4) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005finput_005f5.setType("hidden");
              // /html/portlet/plugin_installer/view.jsp(85,4) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_aui_005finput_005f5.setValue( portletURLString );
              int _jspx_eval_aui_005finput_005f5 = _jspx_th_aui_005finput_005f5.doStartTag();
              if (_jspx_th_aui_005finput_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
                return;
              }
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
              out.write("\n");
              out.write("\t\t\t");
            }
            if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f6 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f6.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(88,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f6.setName("pluginType");
            // /html/portlet/plugin_installer/view.jsp(88,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f6.setType("hidden");
            // /html/portlet/plugin_installer/view.jsp(88,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f6.setValue( pluginType );
            int _jspx_eval_aui_005finput_005f6 = _jspx_th_aui_005finput_005f6.doStartTag();
            if (_jspx_th_aui_005finput_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f7 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f7.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(89,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f7.setName("moduleId");
            // /html/portlet/plugin_installer/view.jsp(89,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f7.setType("hidden");
            // /html/portlet/plugin_installer/view.jsp(89,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f7.setValue( moduleId );
            int _jspx_eval_aui_005finput_005f7 = _jspx_th_aui_005finput_005f7.doStartTag();
            if (_jspx_th_aui_005finput_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
            out.write("\n");
            out.write("\t\t\t");
            //  aui:input
            com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f8 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
            _jspx_th_aui_005finput_005f8.setPageContext(_jspx_page_context);
            _jspx_th_aui_005finput_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            // /html/portlet/plugin_installer/view.jsp(90,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f8.setName("repositoryURL");
            // /html/portlet/plugin_installer/view.jsp(90,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f8.setType("hidden");
            // /html/portlet/plugin_installer/view.jsp(90,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
            _jspx_th_aui_005finput_005f8.setValue( repositoryURL );
            int _jspx_eval_aui_005finput_005f8 = _jspx_th_aui_005finput_005f8.doStartTag();
            if (_jspx_th_aui_005finput_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
              return;
            }
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t");
            //  c:choose
            com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
            _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
            _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
            int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
            if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:when
              com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f1 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
              // /html/portlet/plugin_installer/view.jsp(93,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_c_005fwhen_005f1.setTest( Validator.isNotNull(moduleId) && Validator.isNotNull(repositoryURL) );
              int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
              if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t");
                out.write('\n');
                out.write('\n');

PluginPackage pluginPackage = PluginPackageUtil.getPluginPackageByModuleId(moduleId, repositoryURL);

                out.write('\n');
                out.write('\n');
                //  aui:input
                com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f9 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                _jspx_th_aui_005finput_005f9.setPageContext(_jspx_page_context);
                _jspx_th_aui_005finput_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(21,0) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f9.setName("redirect");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(21,0) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f9.setType("hidden");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(21,0) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f9.setValue( currentURL );
                int _jspx_eval_aui_005finput_005f9 = _jspx_th_aui_005finput_005f9.doStartTag();
                if (_jspx_th_aui_005finput_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
                  return;
                }
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
                out.write('\n');
                //  aui:input
                com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f10 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                _jspx_th_aui_005finput_005f10.setPageContext(_jspx_page_context);
                _jspx_th_aui_005finput_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(22,0) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f10.setName("url");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(22,0) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f10.setType("hidden");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(22,0) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f10.setValue( pluginPackage.getDownloadURL() );
                int _jspx_eval_aui_005finput_005f10 = _jspx_th_aui_005finput_005f10.doStartTag();
                if (_jspx_th_aui_005finput_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
                  return;
                }
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
                out.write('\n');
                //  aui:input
                com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f11 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                _jspx_th_aui_005finput_005f11.setPageContext(_jspx_page_context);
                _jspx_th_aui_005finput_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(23,0) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f11.setName("deploymentContext");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(23,0) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f11.setType("hidden");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(23,0) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005finput_005f11.setValue( pluginPackage.getContext() );
                int _jspx_eval_aui_005finput_005f11 = _jspx_th_aui_005finput_005f11.doStartTag();
                if (_jspx_th_aui_005finput_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f11);
                  return;
                }
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f11);
                out.write('\n');
                out.write('\n');
                //  liferay-ui:header
                com.liferay.taglib.ui.HeaderTag _jspx_th_liferay_002dui_005fheader_005f0 = (com.liferay.taglib.ui.HeaderTag) _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005flocalizeTitle_005fbackURL_005fnobody.get(com.liferay.taglib.ui.HeaderTag.class);
                _jspx_th_liferay_002dui_005fheader_005f0.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005fheader_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(25,0) name = backURL type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fheader_005f0.setBackURL( redirect );
                // /html/portlet/plugin_installer/view_plugin_package.jspf(25,0) name = localizeTitle type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fheader_005f0.setLocalizeTitle( false );
                // /html/portlet/plugin_installer/view_plugin_package.jspf(25,0) name = title type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fheader_005f0.setTitle( pluginPackage.getName() );
                int _jspx_eval_liferay_002dui_005fheader_005f0 = _jspx_th_liferay_002dui_005fheader_005f0.doStartTag();
                if (_jspx_th_liferay_002dui_005fheader_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005flocalizeTitle_005fbackURL_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005flocalizeTitle_005fbackURL_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
                out.write('\n');
                out.write('\n');
                if (_jspx_meth_liferay_002dui_005fsuccess_005f0(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write('\n');
                out.write('\n');
                //  liferay-ui:error
                com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(33,0) name = key type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ferror_005f0.setKey("invalidUrl");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(33,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ferror_005f0.setMessage("please-enter-a-valid-url");
                int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
                if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                out.write('\n');
                //  liferay-ui:error
                com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f1 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                _jspx_th_liferay_002dui_005ferror_005f1.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ferror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(34,0) name = key type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ferror_005f1.setKey("errorConnectingToUrl");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(34,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ferror_005f1.setMessage("an-unexpected-error-occurred-while-connecting-to-the-specified-url");
                int _jspx_eval_liferay_002dui_005ferror_005f1 = _jspx_th_liferay_002dui_005ferror_005f1.doStartTag();
                if (_jspx_th_liferay_002dui_005ferror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                out.write("\n");
                out.write("\n");
                out.write("<table class=\"lfr-table\">\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\t\t<strong>");
                //  aui:a
                com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f0 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
                _jspx_th_aui_005fa_005f0.setPageContext(_jspx_page_context);
                _jspx_th_aui_005fa_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(42,10) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005fa_005f0.setHref( pluginPackage.getPageURL() );
                // /html/portlet/plugin_installer/view_plugin_package.jspf(42,10) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005fa_005f0.setLabel( HtmlUtil.escape(pluginPackage.getName()) );
                int _jspx_eval_aui_005fa_005f0 = _jspx_th_aui_005fa_005f0.doStartTag();
                if (_jspx_th_aui_005fa_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f0);
                out.write("</strong> (v");
                out.print( HtmlUtil.escape(pluginPackage.getVersion()) );
                out.write(')');
                out.write(' ');
                //  aui:a
                com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f1 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
                _jspx_th_aui_005fa_005f1.setPageContext(_jspx_page_context);
                _jspx_th_aui_005fa_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(42,180) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005fa_005f1.setHref( pluginPackage.getDownloadURL() );
                // /html/portlet/plugin_installer/view_plugin_package.jspf(42,180) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_aui_005fa_005f1.setLabel( "[" + LanguageUtil.get(pageContext, "download") + "]" );
                int _jspx_eval_aui_005fa_005f1 = _jspx_th_aui_005fa_005f1.doStartTag();
                if (_jspx_th_aui_005fa_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f1);
                  return;
                }
                _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f1);
                out.write("\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                out.print( HtmlUtil.escape(pluginPackage.getAuthor()) );
                out.write("\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\n");
                out.write("\t\t");

		List<String> pluginPackageTypes = pluginPackage.getTypes();

		for (int i = 0; i < pluginPackageTypes.size(); i++) {
			String type = pluginPackageTypes.get(i);
		
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t");
                //  liferay-ui:message
                com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(66,3) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fmessage_005f3.setKey( type );
                int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
                if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
                //  c:if
                com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f1 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(66,43) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f1.setTest( i < pluginPackageTypes.size() - 1 );
                int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write(',');
                  out.write(' ');
                }
                if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
                out.write("\n");
                out.write("\n");
                out.write("\t\t");

		}
		
                out.write("\n");
                out.write("\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\n");
                out.write("\t\t");

		List<String> pluginPackageTags = pluginPackage.getTags();

		for (int i = 0; i < pluginPackageTags.size(); i++) {
			String tag = pluginPackageTags.get(i);
		
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t");
                out.print( HtmlUtil.escape(tag) );
                //  c:if
                com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f2 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(87,30) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f2.setTest( i < pluginPackageTags.size() - 1 );
                int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write(',');
                  out.write(' ');
                }
                if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
                out.write("\n");
                out.write("\n");
                out.write("\t\t");

		}
		
                out.write("\n");
                out.write("\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\n");
                out.write("\t\t");

		List<License> pluginPackageLicenses = pluginPackage.getLicenses();

		for (int i = 0; i < pluginPackageLicenses.size(); i++) {
			License license = pluginPackageLicenses.get(i);
		
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t");
                //  c:if
                com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f3 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(108,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f3.setTest( Validator.isNotNull(license.getUrl()) );
                int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f2 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                  // /html/portlet/plugin_installer/view_plugin_package.jspf(109,4) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_aui_005fa_005f2.setHref( license.getUrl() );
                  // /html/portlet/plugin_installer/view_plugin_package.jspf(109,4) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_aui_005fa_005f2.setLabel( HtmlUtil.escape(license.getName()) );
                  int _jspx_eval_aui_005fa_005f2 = _jspx_th_aui_005fa_005f2.doStartTag();
                  if (_jspx_th_aui_005fa_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f2);
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
                _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(112,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f4.setTest( license.isOsiApproved() );
                int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
                if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t\t\t\t(");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fif_005f4, _jspx_page_context))
                    return;
                  out.write(")\n");
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
                _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(116,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f5.setTest( i < pluginPackageLicenses.size() - 1 );
                int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
                if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write(',');
                  out.write(' ');
                }
                if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
                out.write("\n");
                out.write("\n");
                out.write("\t\t");

		}
		
                out.write("\n");
                out.write("\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\n");
                out.write("\t\t");

		List<String> liferayVersions = pluginPackage.getLiferayVersions();

		for (int i = 0; i < liferayVersions.size(); i++) {
			String liferayVersion = liferayVersions.get(i);
		
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t");
                out.print( HtmlUtil.escape(liferayVersion) );
                //  c:if
                com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f6 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
                _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(137,41) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f6.setTest( i < liferayVersions.size() - 1 );
                int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
                if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write(',');
                  out.write(' ');
                }
                if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
                out.write("\n");
                out.write("\n");
                out.write("\t\t");

		}
		
                out.write("\n");
                out.write("\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f8(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\t\t<a href=\"");
                out.print( HtmlUtil.escapeHREF(pluginPackage.getRepositoryURL()) );
                out.write('"');
                out.write('>');
                out.print( HtmlUtil.escape(pluginPackage.getRepositoryURL()) );
                out.write("</a>\n");
                out.write("\n");
                out.write("\t\t");
                //  c:choose
                com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
                _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
                _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
                if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:when
                  com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f2 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                  // /html/portlet/plugin_installer/view_plugin_package.jspf(153,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_c_005fwhen_005f2.setTest( PluginPackageUtil.isTrusted(pluginPackage.getRepositoryURL()) );
                  int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    out.write("\n");
                    out.write("\t\t\t\t(");
                    if (_jspx_meth_liferay_002dui_005fmessage_005f9(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                      return;
                    out.write(")\n");
                    out.write("\t\t\t");
                  }
                  if (_jspx_th_c_005fwhen_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_c_005fotherwise_005f0(_jspx_th_c_005fchoose_005f2, _jspx_page_context))
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
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td colspan=\"2\">\n");
                out.write("\t\t<br />\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("<tr>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                if (_jspx_meth_liferay_002dui_005fmessage_005f11(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                  return;
                out.write(":\n");
                out.write("\t</td>\n");
                out.write("\t<td>\n");
                out.write("\t\t");
                out.print( HtmlUtil.escape(pluginPackage.getShortDescription()) );
                out.write("\n");
                out.write("\t</td>\n");
                out.write("</tr>\n");
                out.write("\n");
                //  c:if
                com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f7 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
                _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(176,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f7.setTest( Validator.isNotNull(pluginPackage.getLongDescription()) );
                int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
                if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t<tr>\n");
                  out.write("\t\t<td>\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f12(_jspx_th_c_005fif_005f7, _jspx_page_context))
                    return;
                  out.write(":\n");
                  out.write("\t\t</td>\n");
                  out.write("\t\t<td>\n");
                  out.write("\t\t\t");
                  out.print( HtmlUtil.escape(pluginPackage.getLongDescription()) );
                  out.write("\n");
                  out.write("\t\t</td>\n");
                  out.write("\t</tr>\n");
                }
                if (_jspx_th_c_005fif_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
                out.write('\n');
                out.write('\n');
                //  c:if
                com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f8 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
                _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(187,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f8.setTest( Validator.isNotNull(pluginPackage.getChangeLog()) );
                int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
                if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t<tr>\n");
                  out.write("\t\t<td>\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f13(_jspx_th_c_005fif_005f8, _jspx_page_context))
                    return;
                  out.write(":\n");
                  out.write("\t\t</td>\n");
                  out.write("\t\t<td>\n");
                  out.write("\t\t\t");
                  out.print( HtmlUtil.escape(pluginPackage.getChangeLog()) );
                  out.write("\n");
                  out.write("\t\t</td>\n");
                  out.write("\t</tr>\n");
                }
                if (_jspx_th_c_005fif_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
                out.write('\n');
                out.write('\n');

List<Screenshot> screenshots = pluginPackage.getScreenshots();

                out.write('\n');
                out.write('\n');
                //  c:if
                com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f9 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
                _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(202,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_c_005fif_005f9.setTest( (screenshots != null) && !screenshots.isEmpty() );
                int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
                if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t<tr>\n");
                  out.write("\t\t<td colspan=\"2\">\n");
                  out.write("\t\t\t<br />\n");
                  out.write("\n");
                  out.write("\t\t\t");

			for (Screenshot screenshot : screenshots) {
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f3 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fhref.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                  // /html/portlet/plugin_installer/view_plugin_package.jspf(211,4) name = href type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_aui_005fa_005f3.setHref( screenshot.getLargeImageURL() );
                  // /html/portlet/plugin_installer/view_plugin_package.jspf(211,4) name = target type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_aui_005fa_005f3.setTarget("_blank");
                  int _jspx_eval_aui_005fa_005f3 = _jspx_th_aui_005fa_005f3.doStartTag();
                  if (_jspx_eval_aui_005fa_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    out.write("<img alt=\"");
                    if (_jspx_meth_liferay_002dui_005fmessage_005f14(_jspx_th_aui_005fa_005f3, _jspx_page_context))
                      return;
                    out.write("\" src=\"");
                    out.print( screenshot.getThumbnailURL() );
                    out.write("\" />");
                  }
                  if (_jspx_th_aui_005fa_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fhref.reuse(_jspx_th_aui_005fa_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005ftarget_005fhref.reuse(_jspx_th_aui_005fa_005f3);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			}
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t</td>\n");
                  out.write("\t</tr>\n");
                }
                if (_jspx_th_c_005fif_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
                out.write("\n");
                out.write("\n");
                out.write("</table>\n");
                out.write("\n");
                //  aui:button-row
                com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f0 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
                _jspx_th_aui_005fbutton_002drow_005f0.setPageContext(_jspx_page_context);
                _jspx_th_aui_005fbutton_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                int _jspx_eval_aui_005fbutton_002drow_005f0 = _jspx_th_aui_005fbutton_002drow_005f0.doStartTag();
                if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write('\n');
                  out.write('	');
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
                  // /html/portlet/plugin_installer/view_plugin_package.jspf(224,1) name = onClick type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_aui_005fbutton_005f0.setOnClick( uploadProgressId + ".startProgress();" + renderResponse.getNamespace() + "installPluginPackage(" + StringPool.APOSTROPHE + "remoteDeploy" + StringPool.APOSTROPHE + ");" );
                  // /html/portlet/plugin_installer/view_plugin_package.jspf(224,1) name = value type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_aui_005fbutton_005f0.setValue("install");
                  int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                  out.write('\n');
                }
                if (_jspx_th_aui_005fbutton_002drow_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
                out.write('\n');
                out.write('\n');
                //  liferay-ui:upload-progress
                com.liferay.taglib.ui.UploadProgressTag _jspx_th_liferay_002dui_005fupload_002dprogress_005f0 = (com.liferay.taglib.ui.UploadProgressTag) _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.get(com.liferay.taglib.ui.UploadProgressTag.class);
                _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                // /html/portlet/plugin_installer/view_plugin_package.jspf(227,0) name = id type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setId( uploadProgressId );
                // /html/portlet/plugin_installer/view_plugin_package.jspf(227,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setMessage("downloading");
                // /html/portlet/plugin_installer/view_plugin_package.jspf(227,0) name = redirect type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setRedirect( currentURL );
                int _jspx_eval_liferay_002dui_005fupload_002dprogress_005f0 = _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.doStartTag();
                if (_jspx_th_liferay_002dui_005fupload_002dprogress_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f0);
                out.write("\n");
                out.write("\t\t\t\t");
              }
              if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:otherwise
              com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
              int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                out.write("\n");
                out.write("\t\t\t\t\t");
                //  liferay-ui:tabs
                com.liferay.taglib.ui.TabsTag _jspx_th_liferay_002dui_005ftabs_005f0 = (com.liferay.taglib.ui.TabsTag) _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fbackURL_005fnobody.get(com.liferay.taglib.ui.TabsTag.class);
                _jspx_th_liferay_002dui_005ftabs_005f0.setPageContext(_jspx_page_context);
                _jspx_th_liferay_002dui_005ftabs_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
                // /html/portlet/plugin_installer/view.jsp(97,5) name = backURL type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ftabs_005f0.setBackURL( backURL );
                // /html/portlet/plugin_installer/view.jsp(97,5) name = names type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ftabs_005f0.setNames( tabs1Names );
                // /html/portlet/plugin_installer/view.jsp(97,5) name = param type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ftabs_005f0.setParam("tabs1");
                // /html/portlet/plugin_installer/view.jsp(97,5) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                _jspx_th_liferay_002dui_005ftabs_005f0.setUrl( portletURLString );
                int _jspx_eval_liferay_002dui_005ftabs_005f0 = _jspx_th_liferay_002dui_005ftabs_005f0.doStartTag();
                if (_jspx_th_liferay_002dui_005ftabs_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fbackURL_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f0);
                  return;
                }
                _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fbackURL_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f0);
                out.write("\n");
                out.write("\n");
                out.write("\t\t\t\t\t");
                //  c:choose
                com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
                _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
                _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
                int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
                if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  c:when
                  com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f3 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                  // /html/portlet/plugin_installer/view.jsp(105,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_c_005fwhen_005f3.setTest( tabs1.equals("upload-file") );
                  int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");
                    out.write('\n');
                    out.write('\n');
                    if (_jspx_meth_liferay_002dui_005fsuccess_005f1(_jspx_th_c_005fwhen_005f3, _jspx_page_context))
                      return;
                    out.write('\n');
                    out.write('\n');
                    //  liferay-ui:error
                    com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f2 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                    _jspx_th_liferay_002dui_005ferror_005f2.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005ferror_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                    // /html/portlet/plugin_installer/upload_file.jspf(19,0) name = exception type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ferror_005f2.setException( UploadException.class );
                    // /html/portlet/plugin_installer/upload_file.jspf(19,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ferror_005f2.setMessage("an-unexpected-error-occurred-while-uploading-your-file");
                    int _jspx_eval_liferay_002dui_005ferror_005f2 = _jspx_th_liferay_002dui_005ferror_005f2.doStartTag();
                    if (_jspx_th_liferay_002dui_005ferror_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
                    out.write('\n');
                    out.write('\n');
                    if (_jspx_meth_aui_005ffieldset_005f0(_jspx_th_c_005fwhen_005f3, _jspx_page_context))
                      return;
                    out.write('\n');
                    out.write('\n');
                    //  aui:fieldset
                    com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f1 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                    _jspx_th_aui_005ffieldset_005f1.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005ffieldset_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                    int _jspx_eval_aui_005ffieldset_005f1 = _jspx_th_aui_005ffieldset_005f1.doStartTag();
                    if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f13 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f13.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /html/portlet/plugin_installer/upload_file.jspf(26,1) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f13.setHelpMessage( LanguageUtil.format(pageContext, "for-example-x", "sample-jsp-portlet") );
                      // /html/portlet/plugin_installer/upload_file.jspf(26,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f13.setLabel("specify-an-optional-context-for-deployment");
                      // /html/portlet/plugin_installer/upload_file.jspf(26,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f13.setName("deploymentContext");
                      // /html/portlet/plugin_installer/upload_file.jspf(26,1) null
                      _jspx_th_aui_005finput_005f13.setDynamicAttribute(null, "size", new String("20"));
                      // /html/portlet/plugin_installer/upload_file.jspf(26,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f13.setType("text");
                      int _jspx_eval_aui_005finput_005f13 = _jspx_th_aui_005finput_005f13.doStartTag();
                      if (_jspx_th_aui_005finput_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f13);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f13);
                      out.write('\n');
                    }
                    if (_jspx_th_aui_005ffieldset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f1);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f1);
                    out.write('\n');
                    out.write('\n');
                    //  aui:button-row
                    com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f1 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
                    _jspx_th_aui_005fbutton_002drow_005f1.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005fbutton_002drow_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                    int _jspx_eval_aui_005fbutton_002drow_005f1 = _jspx_th_aui_005fbutton_002drow_005f1.doStartTag();
                    if (_jspx_eval_aui_005fbutton_002drow_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      //  aui:button
                      com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f1 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                      _jspx_th_aui_005fbutton_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fbutton_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f1);
                      // /html/portlet/plugin_installer/upload_file.jspf(30,1) name = onClick type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fbutton_005f1.setOnClick( uploadProgressId + ".startProgress();" + renderResponse.getNamespace() + "installPluginPackage(" + StringPool.APOSTROPHE + "localDeploy" + StringPool.APOSTROPHE + ");" );
                      // /html/portlet/plugin_installer/upload_file.jspf(30,1) name = value type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fbutton_005f1.setValue("install");
                      int _jspx_eval_aui_005fbutton_005f1 = _jspx_th_aui_005fbutton_005f1.doStartTag();
                      if (_jspx_th_aui_005fbutton_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                      out.write('\n');
                    }
                    if (_jspx_th_aui_005fbutton_002drow_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f1);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f1);
                    out.write('\n');
                    out.write('\n');
                    //  liferay-ui:upload-progress
                    com.liferay.taglib.ui.UploadProgressTag _jspx_th_liferay_002dui_005fupload_002dprogress_005f1 = (com.liferay.taglib.ui.UploadProgressTag) _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.get(com.liferay.taglib.ui.UploadProgressTag.class);
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f1.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                    // /html/portlet/plugin_installer/upload_file.jspf(33,0) name = id type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f1.setId( uploadProgressId );
                    // /html/portlet/plugin_installer/upload_file.jspf(33,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f1.setMessage("uploading");
                    // /html/portlet/plugin_installer/upload_file.jspf(33,0) name = redirect type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f1.setRedirect( portletURLString );
                    int _jspx_eval_liferay_002dui_005fupload_002dprogress_005f1 = _jspx_th_liferay_002dui_005fupload_002dprogress_005f1.doStartTag();
                    if (_jspx_th_liferay_002dui_005fupload_002dprogress_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f1);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f1);
                    out.write("\n");
                    out.write("\t\t\t\t\t\t");
                  }
                  if (_jspx_th_c_005fwhen_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  c:when
                  com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f4 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                  // /html/portlet/plugin_installer/view.jsp(108,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_c_005fwhen_005f4.setTest( tabs1.equals("download-file") );
                  int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");
                    out.write('\n');
                    out.write('\n');
                    if (_jspx_meth_liferay_002dui_005fsuccess_005f2(_jspx_th_c_005fwhen_005f4, _jspx_page_context))
                      return;
                    out.write('\n');
                    out.write('\n');
                    //  liferay-ui:error
                    com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f3 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                    _jspx_th_liferay_002dui_005ferror_005f3.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005ferror_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                    // /html/portlet/plugin_installer/download_file.jspf(19,0) name = key type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ferror_005f3.setKey("invalidUrl");
                    // /html/portlet/plugin_installer/download_file.jspf(19,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ferror_005f3.setMessage("please-enter-a-valid-url");
                    int _jspx_eval_liferay_002dui_005ferror_005f3 = _jspx_th_liferay_002dui_005ferror_005f3.doStartTag();
                    if (_jspx_th_liferay_002dui_005ferror_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
                    out.write('\n');
                    //  liferay-ui:error
                    com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f4 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                    _jspx_th_liferay_002dui_005ferror_005f4.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005ferror_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                    // /html/portlet/plugin_installer/download_file.jspf(20,0) name = key type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ferror_005f4.setKey("errorConnectingToUrl");
                    // /html/portlet/plugin_installer/download_file.jspf(20,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ferror_005f4.setMessage("an-unexpected-error-occurred-while-connecting-to-the-specified-url");
                    int _jspx_eval_liferay_002dui_005ferror_005f4 = _jspx_th_liferay_002dui_005ferror_005f4.doStartTag();
                    if (_jspx_th_liferay_002dui_005ferror_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
                    out.write('\n');
                    out.write('\n');
                    //  aui:fieldset
                    com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f2 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                    _jspx_th_aui_005ffieldset_005f2.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005ffieldset_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                    int _jspx_eval_aui_005ffieldset_005f2 = _jspx_th_aui_005ffieldset_005f2.doStartTag();
                    if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f14 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f14.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
                      // /html/portlet/plugin_installer/download_file.jspf(23,1) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f14.setHelpMessage( LanguageUtil.format(pageContext, "for-example-x", "http://easynews.dl.sourceforge.net/sourceforge/lportal/sample-jsp-portlet-" + ReleaseInfo.getVersion() + ".war") );
                      // /html/portlet/plugin_installer/download_file.jspf(23,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f14.setLabel("specify-a-url-for-a-remote-layout-template,-portlet,-or-theme");
                      // /html/portlet/plugin_installer/download_file.jspf(23,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f14.setName("url");
                      // /html/portlet/plugin_installer/download_file.jspf(23,1) null
                      _jspx_th_aui_005finput_005f14.setDynamicAttribute(null, "size", new String("75"));
                      // /html/portlet/plugin_installer/download_file.jspf(23,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f14.setType("text");
                      int _jspx_eval_aui_005finput_005f14 = _jspx_th_aui_005finput_005f14.doStartTag();
                      if (_jspx_th_aui_005finput_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f14);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f14);
                      out.write('\n');
                    }
                    if (_jspx_th_aui_005ffieldset_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f2);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f2);
                    out.write('\n');
                    out.write('\n');
                    //  aui:fieldset
                    com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f3 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                    _jspx_th_aui_005ffieldset_005f3.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005ffieldset_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                    int _jspx_eval_aui_005ffieldset_005f3 = _jspx_th_aui_005ffieldset_005f3.doStartTag();
                    if (_jspx_eval_aui_005ffieldset_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f15 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f15.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f3);
                      // /html/portlet/plugin_installer/download_file.jspf(27,1) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f15.setHelpMessage( LanguageUtil.format(pageContext, "for-example-x", "sample-jsp-portlet") );
                      // /html/portlet/plugin_installer/download_file.jspf(27,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f15.setLabel("specify-an-optional-context-for-deployment");
                      // /html/portlet/plugin_installer/download_file.jspf(27,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f15.setName("deploymentContext");
                      // /html/portlet/plugin_installer/download_file.jspf(27,1) null
                      _jspx_th_aui_005finput_005f15.setDynamicAttribute(null, "size", new String("20"));
                      // /html/portlet/plugin_installer/download_file.jspf(27,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f15.setType("text");
                      int _jspx_eval_aui_005finput_005f15 = _jspx_th_aui_005finput_005f15.doStartTag();
                      if (_jspx_th_aui_005finput_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f15);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fnobody.reuse(_jspx_th_aui_005finput_005f15);
                      out.write('\n');
                    }
                    if (_jspx_th_aui_005ffieldset_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f3);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f3);
                    out.write('\n');
                    out.write('\n');
                    //  aui:button-row
                    com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f2 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
                    _jspx_th_aui_005fbutton_002drow_005f2.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005fbutton_002drow_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                    int _jspx_eval_aui_005fbutton_002drow_005f2 = _jspx_th_aui_005fbutton_002drow_005f2.doStartTag();
                    if (_jspx_eval_aui_005fbutton_002drow_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      //  aui:button
                      com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f2 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                      _jspx_th_aui_005fbutton_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fbutton_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f2);
                      // /html/portlet/plugin_installer/download_file.jspf(31,1) name = onClick type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fbutton_005f2.setOnClick( uploadProgressId + ".startProgress();" + renderResponse.getNamespace() + "installPluginPackage(" + StringPool.APOSTROPHE + "remoteDeploy" + StringPool.APOSTROPHE + ");" );
                      // /html/portlet/plugin_installer/download_file.jspf(31,1) name = value type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fbutton_005f2.setValue("install");
                      int _jspx_eval_aui_005fbutton_005f2 = _jspx_th_aui_005fbutton_005f2.doStartTag();
                      if (_jspx_th_aui_005fbutton_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
                      out.write('\n');
                    }
                    if (_jspx_th_aui_005fbutton_002drow_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f2);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f2);
                    out.write('\n');
                    out.write('\n');
                    //  liferay-ui:upload-progress
                    com.liferay.taglib.ui.UploadProgressTag _jspx_th_liferay_002dui_005fupload_002dprogress_005f2 = (com.liferay.taglib.ui.UploadProgressTag) _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.get(com.liferay.taglib.ui.UploadProgressTag.class);
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f2.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                    // /html/portlet/plugin_installer/download_file.jspf(34,0) name = id type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f2.setId( uploadProgressId );
                    // /html/portlet/plugin_installer/download_file.jspf(34,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f2.setMessage("downloading");
                    // /html/portlet/plugin_installer/download_file.jspf(34,0) name = redirect type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fupload_002dprogress_005f2.setRedirect( portletURLString );
                    int _jspx_eval_liferay_002dui_005fupload_002dprogress_005f2 = _jspx_th_liferay_002dui_005fupload_002dprogress_005f2.doStartTag();
                    if (_jspx_th_liferay_002dui_005fupload_002dprogress_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f2);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f2);
                    out.write("\n");
                    out.write("\t\t\t\t\t\t");
                  }
                  if (_jspx_th_c_005fwhen_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  c:when
                  com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f5 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                  // /html/portlet/plugin_installer/view.jsp(111,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                  _jspx_th_c_005fwhen_005f5.setTest( tabs1.equals("configuration") );
                  int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");
                    out.write('\n');
                    out.write('\n');
                    //  aui:fieldset
                    com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f4 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                    _jspx_th_aui_005ffieldset_005f4.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005ffieldset_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                    int _jspx_eval_aui_005ffieldset_005f4 = _jspx_th_aui_005ffieldset_005f4.doStartTag();
                    if (_jspx_eval_aui_005ffieldset_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f16 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f16.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(18,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f16.setName("enabled");
                      // /html/portlet/plugin_installer/configuration.jspf(18,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f16.setType("checkbox");
                      // /html/portlet/plugin_installer/configuration.jspf(18,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f16.setValue( PrefsPropsUtil.getBoolean(PropsKeys.AUTO_DEPLOY_ENABLED, PropsValues.AUTO_DEPLOY_ENABLED) );
                      int _jspx_eval_aui_005finput_005f16 = _jspx_th_aui_005finput_005f16.doStartTag();
                      if (_jspx_th_aui_005finput_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f16);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f16);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f17 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f17.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(20,1) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f17.setCssClass("lfr-input-text-container");
                      // /html/portlet/plugin_installer/configuration.jspf(20,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f17.setLabel("deploy-directory");
                      // /html/portlet/plugin_installer/configuration.jspf(20,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f17.setName("deployDir");
                      // /html/portlet/plugin_installer/configuration.jspf(20,1) null
                      _jspx_th_aui_005finput_005f17.setDynamicAttribute(null, "size", new String("75"));
                      // /html/portlet/plugin_installer/configuration.jspf(20,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f17.setType("text");
                      // /html/portlet/plugin_installer/configuration.jspf(20,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f17.setValue( PrefsPropsUtil.getString(PropsKeys.AUTO_DEPLOY_DEPLOY_DIR, PropsValues.AUTO_DEPLOY_DEPLOY_DIR) );
                      int _jspx_eval_aui_005finput_005f17 = _jspx_th_aui_005finput_005f17.doStartTag();
                      if (_jspx_th_aui_005finput_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f17);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f17);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f18 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f18.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(22,1) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f18.setCssClass("lfr-input-text-container");
                      // /html/portlet/plugin_installer/configuration.jspf(22,1) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f18.setHelpMessage( LanguageUtil.format(pageContext, "plugins-will-be-deployed-to-x-if-this-field-is-left-blank", DeployUtil.getAutoDeployServerDestDir()) );
                      // /html/portlet/plugin_installer/configuration.jspf(22,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f18.setLabel("dest-directory");
                      // /html/portlet/plugin_installer/configuration.jspf(22,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f18.setName("destDir");
                      // /html/portlet/plugin_installer/configuration.jspf(22,1) null
                      _jspx_th_aui_005finput_005f18.setDynamicAttribute(null, "size", new String("75"));
                      // /html/portlet/plugin_installer/configuration.jspf(22,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f18.setType("text");
                      // /html/portlet/plugin_installer/configuration.jspf(22,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f18.setValue( PrefsPropsUtil.getString(PropsKeys.AUTO_DEPLOY_DEST_DIR, PropsValues.AUTO_DEPLOY_DEST_DIR) );
                      int _jspx_eval_aui_005finput_005f18 = _jspx_th_aui_005finput_005f18.doStartTag();
                      if (_jspx_th_aui_005finput_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f18);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f18);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:select
                      com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f0 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                      _jspx_th_aui_005fselect_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(24,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fselect_005f0.setName("interval");
                      int _jspx_eval_aui_005fselect_005f0 = _jspx_th_aui_005fselect_005f0.doStartTag();
                      if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write('\n');
                        out.write('	');
                        out.write('	');
                        if (_jspx_meth_aui_005foption_005f0(_jspx_th_aui_005fselect_005f0, _jspx_page_context))
                          return;
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t");

		long interval = PrefsPropsUtil.getLong(PropsKeys.AUTO_DEPLOY_INTERVAL, PropsValues.AUTO_DEPLOY_INTERVAL);

		for (int i = 0;;) {
			if (i < (Time.SECOND * 5)) {
				i += Time.SECOND;
			}
			else if (i < Time.MINUTE) {
				i += Time.SECOND * 5;
			}
			else {
				i += Time.MINUTE;
			}
		
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f1 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f1.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                        // /html/portlet/plugin_installer/configuration.jspf(42,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f1.setLabel( LanguageUtil.getTimeDescription(pageContext, i) );
                        // /html/portlet/plugin_installer/configuration.jspf(42,3) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f1.setSelected( interval == i );
                        // /html/portlet/plugin_installer/configuration.jspf(42,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f1.setValue( i );
                        int _jspx_eval_aui_005foption_005f1 = _jspx_th_aui_005foption_005f1.doStartTag();
                        if (_jspx_th_aui_005foption_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t");

			if (i >= (Time.MINUTE * 5)) {
				break;
			}
		}
		
                        out.write('\n');
                        out.write('\n');
                        out.write('	');
                      }
                      if (_jspx_th_aui_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f0);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:select
                      com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f1 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                      _jspx_th_aui_005fselect_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fselect_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(53,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fselect_005f1.setName("blacklistThreshold");
                      int _jspx_eval_aui_005fselect_005f1 = _jspx_th_aui_005fselect_005f1.doStartTag();
                      if (_jspx_eval_aui_005fselect_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t");

		int blacklistThreshold = PrefsPropsUtil.getInteger(PropsKeys.AUTO_DEPLOY_BLACKLIST_THRESHOLD, PropsValues.AUTO_DEPLOY_BLACKLIST_THRESHOLD);

		for (int i = 5; i <= 20; i = i + 5) {
		
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f2 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f2.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
                        // /html/portlet/plugin_installer/configuration.jspf(61,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f2.setLabel( i );
                        // /html/portlet/plugin_installer/configuration.jspf(61,3) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f2.setSelected( blacklistThreshold == i );
                        int _jspx_eval_aui_005foption_005f2 = _jspx_th_aui_005foption_005f2.doStartTag();
                        if (_jspx_th_aui_005foption_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t");

		}
		
                        out.write('\n');
                        out.write('\n');
                        out.write('	');
                      }
                      if (_jspx_th_aui_005fselect_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f1);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  c:if
                      com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f10 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                      _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(69,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_c_005fif_005f10.setTest( !ServerDetector.isGlassfish() && !ServerDetector.isWebLogic() );
                      int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
                      if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write('\n');
                        out.write('	');
                        out.write('	');
                        //  aui:input
                        com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f19 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                        _jspx_th_aui_005finput_005f19.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005finput_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                        // /html/portlet/plugin_installer/configuration.jspf(70,2) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005finput_005f19.setName("unpackWar");
                        // /html/portlet/plugin_installer/configuration.jspf(70,2) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005finput_005f19.setType("checkbox");
                        // /html/portlet/plugin_installer/configuration.jspf(70,2) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005finput_005f19.setValue( PrefsPropsUtil.getBoolean(PropsKeys.AUTO_DEPLOY_UNPACK_WAR, PropsValues.AUTO_DEPLOY_UNPACK_WAR) );
                        int _jspx_eval_aui_005finput_005f19 = _jspx_th_aui_005finput_005f19.doStartTag();
                        if (_jspx_th_aui_005finput_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f19);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f19);
                        out.write('\n');
                        out.write('	');
                      }
                      if (_jspx_th_c_005fif_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f20 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f20.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(73,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f20.setName("customPortletXml");
                      // /html/portlet/plugin_installer/configuration.jspf(73,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f20.setType("checkbox");
                      // /html/portlet/plugin_installer/configuration.jspf(73,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f20.setValue( PrefsPropsUtil.getBoolean(PropsKeys.AUTO_DEPLOY_CUSTOM_PORTLET_XML, PropsValues.AUTO_DEPLOY_CUSTOM_PORTLET_XML) );
                      int _jspx_eval_aui_005finput_005f20 = _jspx_th_aui_005finput_005f20.doStartTag();
                      if (_jspx_th_aui_005finput_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f20);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f20);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');

	int jbossPrefix = GetterUtil.getInteger(PrefsPropsUtil.getString(PropsKeys.AUTO_DEPLOY_JBOSS_PREFIX, PropsValues.AUTO_DEPLOY_JBOSS_PREFIX));
	
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  c:choose
                      com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write('\n');
                        out.write('	');
                        out.write('	');
                        //  c:when
                        com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f6 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                        _jspx_th_c_005fwhen_005f6.setPageContext(_jspx_page_context);
                        _jspx_th_c_005fwhen_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                        // /html/portlet/plugin_installer/configuration.jspf(80,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_c_005fwhen_005f6.setTest( ServerDetector.isJBoss() );
                        int _jspx_eval_c_005fwhen_005f6 = _jspx_th_c_005fwhen_005f6.doStartTag();
                        if (_jspx_eval_c_005fwhen_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  aui:select
                          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f2 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                          _jspx_th_aui_005fselect_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fselect_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f6);
                          // /html/portlet/plugin_installer/configuration.jspf(81,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005fselect_005f2.setName("jbossPrefix");
                          // /html/portlet/plugin_installer/configuration.jspf(81,3) name = showEmptyOption type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005fselect_005f2.setShowEmptyOption( true );
                          int _jspx_eval_aui_005fselect_005f2 = _jspx_th_aui_005fselect_005f2.doStartTag();
                          if (_jspx_eval_aui_005fselect_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            out.write("\n");
                            out.write("\n");
                            out.write("\t\t\t\t");

				for (int i = 1; i < 9; i++) {
				
                            out.write("\n");
                            out.write("\n");
                            out.write("\t\t\t\t\t");
                            //  aui:option
                            com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f3 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                            _jspx_th_aui_005foption_005f3.setPageContext(_jspx_page_context);
                            _jspx_th_aui_005foption_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f2);
                            // /html/portlet/plugin_installer/configuration.jspf(87,5) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                            _jspx_th_aui_005foption_005f3.setLabel( i );
                            // /html/portlet/plugin_installer/configuration.jspf(87,5) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                            _jspx_th_aui_005foption_005f3.setSelected( jbossPrefix == i );
                            int _jspx_eval_aui_005foption_005f3 = _jspx_th_aui_005foption_005f3.doStartTag();
                            if (_jspx_th_aui_005foption_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
                              return;
                            }
                            _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
                            out.write("\n");
                            out.write("\n");
                            out.write("\t\t\t\t");

				}
				
                            out.write("\n");
                            out.write("\n");
                            out.write("\t\t\t");
                          }
                          if (_jspx_th_aui_005fselect_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.reuse(_jspx_th_aui_005fselect_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.reuse(_jspx_th_aui_005fselect_005f2);
                          out.write('\n');
                          out.write('	');
                          out.write('	');
                        }
                        if (_jspx_th_c_005fwhen_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f6);
                          return;
                        }
                        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f6);
                        out.write('\n');
                        out.write('	');
                        out.write('	');
                        //  c:otherwise
                        com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
                        _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
                        _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                        int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
                        if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f21 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f21.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                          // /html/portlet/plugin_installer/configuration.jspf(96,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f21.setName("jbossPrefix");
                          // /html/portlet/plugin_installer/configuration.jspf(96,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f21.setType("hidden");
                          // /html/portlet/plugin_installer/configuration.jspf(96,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f21.setValue( jbossPrefix );
                          int _jspx_eval_aui_005finput_005f21 = _jspx_th_aui_005finput_005f21.doStartTag();
                          if (_jspx_th_aui_005finput_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f21);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f21);
                          out.write('\n');
                          out.write('	');
                          out.write('	');
                        }
                        if (_jspx_th_c_005fotherwise_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
                          return;
                        }
                        _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
                        out.write('\n');
                        out.write('	');
                      }
                      if (_jspx_th_c_005fchoose_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');

	String tomcatConfDir = PrefsPropsUtil.getString(PropsKeys.AUTO_DEPLOY_TOMCAT_CONF_DIR, PropsValues.AUTO_DEPLOY_TOMCAT_CONF_DIR);
	String tomcatLibDir = PrefsPropsUtil.getString(PropsKeys.AUTO_DEPLOY_TOMCAT_LIB_DIR, PropsValues.AUTO_DEPLOY_TOMCAT_LIB_DIR);
	
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  c:choose
                      com.liferay.taglib.core.ChooseTag _jspx_th_c_005fchoose_005f5 = (com.liferay.taglib.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(com.liferay.taglib.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      int _jspx_eval_c_005fchoose_005f5 = _jspx_th_c_005fchoose_005f5.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write('\n');
                        out.write('	');
                        out.write('	');
                        //  c:when
                        com.liferay.taglib.core.WhenTag _jspx_th_c_005fwhen_005f7 = (com.liferay.taglib.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(com.liferay.taglib.core.WhenTag.class);
                        _jspx_th_c_005fwhen_005f7.setPageContext(_jspx_page_context);
                        _jspx_th_c_005fwhen_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                        // /html/portlet/plugin_installer/configuration.jspf(106,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_c_005fwhen_005f7.setTest( ServerDetector.isTomcat() );
                        int _jspx_eval_c_005fwhen_005f7 = _jspx_th_c_005fwhen_005f7.doStartTag();
                        if (_jspx_eval_c_005fwhen_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f22 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f22.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
                          // /html/portlet/plugin_installer/configuration.jspf(107,3) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f22.setCssClass("lfr-input-text-container");
                          // /html/portlet/plugin_installer/configuration.jspf(107,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f22.setName("tomcatConfDir");
                          // /html/portlet/plugin_installer/configuration.jspf(107,3) null
                          _jspx_th_aui_005finput_005f22.setDynamicAttribute(null, "size", new String("75"));
                          // /html/portlet/plugin_installer/configuration.jspf(107,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f22.setType("text");
                          // /html/portlet/plugin_installer/configuration.jspf(107,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f22.setValue( tomcatConfDir );
                          int _jspx_eval_aui_005finput_005f22 = _jspx_th_aui_005finput_005f22.doStartTag();
                          if (_jspx_th_aui_005finput_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f22);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f22);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f23 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f23.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
                          // /html/portlet/plugin_installer/configuration.jspf(109,3) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f23.setCssClass("lfr-input-text-container");
                          // /html/portlet/plugin_installer/configuration.jspf(109,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f23.setName("tomcatLibDir");
                          // /html/portlet/plugin_installer/configuration.jspf(109,3) null
                          _jspx_th_aui_005finput_005f23.setDynamicAttribute(null, "size", new String("75"));
                          // /html/portlet/plugin_installer/configuration.jspf(109,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f23.setType("text");
                          // /html/portlet/plugin_installer/configuration.jspf(109,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f23.setValue( tomcatLibDir );
                          int _jspx_eval_aui_005finput_005f23 = _jspx_th_aui_005finput_005f23.doStartTag();
                          if (_jspx_th_aui_005finput_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f23);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f23);
                          out.write('\n');
                          out.write('	');
                          out.write('	');
                        }
                        if (_jspx_th_c_005fwhen_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f7);
                          return;
                        }
                        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f7);
                        out.write('\n');
                        out.write('	');
                        out.write('	');
                        //  c:otherwise
                        com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
                        _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
                        _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                        int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
                        if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f24 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f24.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
                          // /html/portlet/plugin_installer/configuration.jspf(112,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f24.setName("tomcatConfDir");
                          // /html/portlet/plugin_installer/configuration.jspf(112,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f24.setType("hidden");
                          // /html/portlet/plugin_installer/configuration.jspf(112,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f24.setValue( tomcatConfDir );
                          int _jspx_eval_aui_005finput_005f24 = _jspx_th_aui_005finput_005f24.doStartTag();
                          if (_jspx_th_aui_005finput_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f24);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f24);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f25 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f25.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
                          // /html/portlet/plugin_installer/configuration.jspf(114,3) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f25.setName("tomcatLibDir");
                          // /html/portlet/plugin_installer/configuration.jspf(114,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f25.setType("hidden");
                          // /html/portlet/plugin_installer/configuration.jspf(114,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                          _jspx_th_aui_005finput_005f25.setValue( tomcatLibDir );
                          int _jspx_eval_aui_005finput_005f25 = _jspx_th_aui_005finput_005f25.doStartTag();
                          if (_jspx_th_aui_005finput_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f25);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f25);
                          out.write('\n');
                          out.write('	');
                          out.write('	');
                        }
                        if (_jspx_th_c_005fotherwise_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
                          return;
                        }
                        _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
                        out.write('\n');
                        out.write('	');
                      }
                      if (_jspx_th_c_005fchoose_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f26 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f26.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(118,1) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f26.setCssClass("lfr-textarea-container");
                      // /html/portlet/plugin_installer/configuration.jspf(118,1) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f26.setHelpMessage("enter-one-url-per-line");
                      // /html/portlet/plugin_installer/configuration.jspf(118,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f26.setLabel("trusted-plugin-repositories");
                      // /html/portlet/plugin_installer/configuration.jspf(118,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f26.setName("pluginRepositoriesTrusted");
                      // /html/portlet/plugin_installer/configuration.jspf(118,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f26.setType("textarea");
                      // /html/portlet/plugin_installer/configuration.jspf(118,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f26.setValue( PrefsPropsUtil.getString(PropsKeys.PLUGIN_REPOSITORIES_TRUSTED) );
                      int _jspx_eval_aui_005finput_005f26 = _jspx_th_aui_005finput_005f26.doStartTag();
                      if (_jspx_th_aui_005finput_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f26);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f26);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f27 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f27.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(120,1) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f27.setCssClass("lfr-textarea-container");
                      // /html/portlet/plugin_installer/configuration.jspf(120,1) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f27.setHelpMessage("enter-one-url-per-line");
                      // /html/portlet/plugin_installer/configuration.jspf(120,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f27.setLabel("untrusted-plugin-repositories");
                      // /html/portlet/plugin_installer/configuration.jspf(120,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f27.setName("pluginRepositoriesUntrusted");
                      // /html/portlet/plugin_installer/configuration.jspf(120,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f27.setType("textarea");
                      // /html/portlet/plugin_installer/configuration.jspf(120,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f27.setValue( PrefsPropsUtil.getString(PropsKeys.PLUGIN_REPOSITORIES_UNTRUSTED) );
                      int _jspx_eval_aui_005finput_005f27 = _jspx_th_aui_005finput_005f27.doStartTag();
                      if (_jspx_th_aui_005finput_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f27);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f27);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f28 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f28.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(122,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f28.setName("pluginNotificationsEnabled");
                      // /html/portlet/plugin_installer/configuration.jspf(122,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f28.setType("checkbox");
                      // /html/portlet/plugin_installer/configuration.jspf(122,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f28.setValue( PrefsPropsUtil.getBoolean(PropsKeys.PLUGIN_NOTIFICATIONS_ENABLED) );
                      int _jspx_eval_aui_005finput_005f28 = _jspx_th_aui_005finput_005f28.doStartTag();
                      if (_jspx_th_aui_005finput_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f28);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f28);
                      out.write('\n');
                      out.write('\n');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f29 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f29.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /html/portlet/plugin_installer/configuration.jspf(124,1) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f29.setCssClass("lfr-textarea-container");
                      // /html/portlet/plugin_installer/configuration.jspf(124,1) name = helpMessage type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f29.setHelpMessage("enter-one-plugin-package-id-per-line");
                      // /html/portlet/plugin_installer/configuration.jspf(124,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f29.setLabel("plugin-packages-with-updates-ignored");
                      // /html/portlet/plugin_installer/configuration.jspf(124,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f29.setName("pluginPackagesIgnored");
                      // /html/portlet/plugin_installer/configuration.jspf(124,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f29.setType("textarea");
                      // /html/portlet/plugin_installer/configuration.jspf(124,1) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f29.setValue( GetterUtil.getString(PrefsPropsUtil.getString(PropsKeys.PLUGIN_NOTIFICATIONS_PACKAGES_IGNORED)) );
                      int _jspx_eval_aui_005finput_005f29 = _jspx_th_aui_005finput_005f29.doStartTag();
                      if (_jspx_th_aui_005finput_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f29);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fhelpMessage_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f29);
                      out.write('\n');
                    }
                    if (_jspx_th_aui_005ffieldset_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f4);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f4);
                    out.write('\n');
                    out.write('\n');
                    if (_jspx_meth_liferay_002dutil_005finclude_005f0(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                      return;
                    out.write('\n');
                    out.write('\n');
                    //  aui:button-row
                    com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f3 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
                    _jspx_th_aui_005fbutton_002drow_005f3.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005fbutton_002drow_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                    int _jspx_eval_aui_005fbutton_002drow_005f3 = _jspx_th_aui_005fbutton_002drow_005f3.doStartTag();
                    if (_jspx_eval_aui_005fbutton_002drow_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      //  aui:button
                      com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f3 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                      _jspx_th_aui_005fbutton_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fbutton_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f3);
                      // /html/portlet/plugin_installer/configuration.jspf(130,1) name = onClick type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fbutton_005f3.setOnClick( renderResponse.getNamespace() + "saveDeployConfiguration();" );
                      // /html/portlet/plugin_installer/configuration.jspf(130,1) name = value type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fbutton_005f3.setValue("save");
                      int _jspx_eval_aui_005fbutton_005f3 = _jspx_th_aui_005fbutton_005f3.doStartTag();
                      if (_jspx_th_aui_005fbutton_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
                      out.write('\n');
                    }
                    if (_jspx_th_aui_005fbutton_002drow_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f3);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f3);
                    out.write('\n');
                    out.write('\n');
                    //  c:if
                    com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f11 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                    _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
                    _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                    // /html/portlet/plugin_installer/configuration.jspf(133,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_c_005fif_005f11.setTest( windowState.equals(WindowState.MAXIMIZED) );
                    int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
                    if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      if (_jspx_meth_aui_005fscript_005f0(_jspx_th_c_005fif_005f11, _jspx_page_context))
                        return;
                      out.write('\n');
                    }
                    if (_jspx_th_c_005fif_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
                      return;
                    }
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
                    out.write("\n");
                    out.write("\t\t\t\t\t\t");
                  }
                  if (_jspx_th_c_005fwhen_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f5);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f5);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  c:otherwise
                  com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f4 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                  int _jspx_eval_c_005fotherwise_005f4 = _jspx_th_c_005fotherwise_005f4.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    out.write("\n");
                    out.write("\t\t\t\t\t\t\t");
                    out.write('\n');
                    out.write('\n');

String keywords = ParamUtil.getString(request, "keywords");
String tag = ParamUtil.getString(request, "tag");
String searchRepositoryURL = ParamUtil.getString(request, "searchRepositoryURL");
String license = ParamUtil.getString(request, "license");
String installStatus = ParamUtil.getString(request, "installStatus", PluginPackageImpl.STATUS_NOT_INSTALLED_OR_OLDER_VERSION_INSTALLED);

                    out.write("\n");
                    out.write("\n");
                    out.write("<style type=\"text/css\">\n");
                    out.write("\t.search-params .aui-field {\n");
                    out.write("\t\tfloat: left;\n");
                    out.write("\t\tmargin-bottom: 1em;\n");
                    out.write("\t}\n");
                    out.write("</style>\n");
                    out.write("\n");
                    //  liferay-ui:tabs
                    com.liferay.taglib.ui.TabsTag _jspx_th_liferay_002dui_005ftabs_005f1 = (com.liferay.taglib.ui.TabsTag) _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fnobody.get(com.liferay.taglib.ui.TabsTag.class);
                    _jspx_th_liferay_002dui_005ftabs_005f1.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005ftabs_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
                    // /html/portlet/plugin_installer/browse_repository.jspf(32,0) name = names type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ftabs_005f1.setNames("portlet-plugins,theme-plugins,layout-template-plugins,hook-plugins,web-plugins");
                    // /html/portlet/plugin_installer/browse_repository.jspf(32,0) name = param type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ftabs_005f1.setParam("tabs2");
                    // /html/portlet/plugin_installer/browse_repository.jspf(32,0) name = url type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005ftabs_005f1.setUrl( portletURLString );
                    int _jspx_eval_liferay_002dui_005ftabs_005f1 = _jspx_th_liferay_002dui_005ftabs_005f1.doStartTag();
                    if (_jspx_th_liferay_002dui_005ftabs_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f1);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fparam_005fnames_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f1);
                    out.write('\n');
                    out.write('\n');

try {

                    out.write('\n');
                    out.write('\n');
                    out.write('	');
                    //  aui:fieldset
                    com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f5 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.get(com.liferay.taglib.aui.FieldsetTag.class);
                    _jspx_th_aui_005ffieldset_005f5.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005ffieldset_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
                    // /html/portlet/plugin_installer/browse_repository.jspf(42,1) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_aui_005ffieldset_005f5.setCssClass("aui-field-row search-params");
                    int _jspx_eval_aui_005ffieldset_005f5 = _jspx_th_aui_005ffieldset_005f5.doStartTag();
                    if (_jspx_eval_aui_005ffieldset_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f30 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f30.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
                      // /html/portlet/plugin_installer/browse_repository.jspf(43,2) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f30.setName("keywords");
                      // /html/portlet/plugin_installer/browse_repository.jspf(43,2) null
                      _jspx_th_aui_005finput_005f30.setDynamicAttribute(null, "size", new String("30"));
                      // /html/portlet/plugin_installer/browse_repository.jspf(43,2) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f30.setType("text");
                      // /html/portlet/plugin_installer/browse_repository.jspf(43,2) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005finput_005f30.setValue( HtmlUtil.escape(keywords) );
                      int _jspx_eval_aui_005finput_005f30 = _jspx_th_aui_005finput_005f30.doStartTag();
                      if (_jspx_th_aui_005finput_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f30);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f30);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t");
                      //  aui:select
                      com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f3 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                      _jspx_th_aui_005fselect_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fselect_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
                      // /html/portlet/plugin_installer/browse_repository.jspf(45,2) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fselect_005f3.setName("tag");
                      int _jspx_eval_aui_005fselect_005f3 = _jspx_th_aui_005fselect_005f3.doStartTag();
                      if (_jspx_eval_aui_005fselect_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write("\n");
                        out.write("\t\t\t");
                        if (_jspx_meth_aui_005foption_005f4(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                          return;
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t");

			Iterator itr = PluginPackageUtil.getAvailableTags().iterator();

			while (itr.hasNext()) {
				String curTag = (String)itr.next();
			
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f5 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f5.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
                        // /html/portlet/plugin_installer/browse_repository.jspf(55,4) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f5.setLabel( HtmlUtil.escape(curTag) );
                        // /html/portlet/plugin_installer/browse_repository.jspf(55,4) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f5.setSelected( tag.equals(curTag) );
                        int _jspx_eval_aui_005foption_005f5 = _jspx_th_aui_005foption_005f5.doStartTag();
                        if (_jspx_th_aui_005foption_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f5);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f5);
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t");

			}
			
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t");
                      }
                      if (_jspx_th_aui_005fselect_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f3);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t");
                      //  aui:select
                      com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f4 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.get(com.liferay.taglib.aui.SelectTag.class);
                      _jspx_th_aui_005fselect_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fselect_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
                      // /html/portlet/plugin_installer/browse_repository.jspf(63,2) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fselect_005f4.setLabel("repository");
                      // /html/portlet/plugin_installer/browse_repository.jspf(63,2) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fselect_005f4.setName("searchRepositoryURL");
                      int _jspx_eval_aui_005fselect_005f4 = _jspx_th_aui_005fselect_005f4.doStartTag();
                      if (_jspx_eval_aui_005fselect_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write("\n");
                        out.write("\t\t\t");
                        if (_jspx_meth_aui_005foption_005f6(_jspx_th_aui_005fselect_005f4, _jspx_page_context))
                          return;
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t");

			String[] repositoryURLs = PluginPackageUtil.getRepositoryURLs();

			for (int i = 0; i < repositoryURLs.length; i++) {
				String curRepositoryURL = repositoryURLs[i];
			
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f7 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f7.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f4);
                        // /html/portlet/plugin_installer/browse_repository.jspf(73,4) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f7.setLabel( HtmlUtil.escape(curRepositoryURL) );
                        // /html/portlet/plugin_installer/browse_repository.jspf(73,4) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f7.setSelected( searchRepositoryURL.equals(curRepositoryURL) );
                        int _jspx_eval_aui_005foption_005f7 = _jspx_th_aui_005foption_005f7.doStartTag();
                        if (_jspx_th_aui_005foption_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f7);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f7);
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t\t");

			}
			
                        out.write("\n");
                        out.write("\n");
                        out.write("\t\t");
                      }
                      if (_jspx_th_aui_005fselect_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f4);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t");
                      //  aui:select
                      com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f5 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                      _jspx_th_aui_005fselect_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fselect_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
                      // /html/portlet/plugin_installer/browse_repository.jspf(81,2) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                      _jspx_th_aui_005fselect_005f5.setName("installStatus");
                      int _jspx_eval_aui_005fselect_005f5 = _jspx_th_aui_005fselect_005f5.doStartTag();
                      if (_jspx_eval_aui_005fselect_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        out.write("\n");
                        out.write("\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f8 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f8.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f5);
                        // /html/portlet/plugin_installer/browse_repository.jspf(82,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f8.setLabel(new String("not-installed-or-older-version-installed"));
                        // /html/portlet/plugin_installer/browse_repository.jspf(82,3) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f8.setSelected( (installStatus.equals(PluginPackageImpl.STATUS_NOT_INSTALLED_OR_OLDER_VERSION_INSTALLED)) );
                        // /html/portlet/plugin_installer/browse_repository.jspf(82,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f8.setValue( PluginPackageImpl.STATUS_NOT_INSTALLED_OR_OLDER_VERSION_INSTALLED );
                        int _jspx_eval_aui_005foption_005f8 = _jspx_th_aui_005foption_005f8.doStartTag();
                        if (_jspx_th_aui_005foption_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f8);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f8);
                        out.write("\n");
                        out.write("\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f9 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f9.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f5);
                        // /html/portlet/plugin_installer/browse_repository.jspf(83,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f9.setLabel(new String("older-version-installed"));
                        // /html/portlet/plugin_installer/browse_repository.jspf(83,3) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f9.setSelected( (installStatus.equals(PluginPackageImpl.STATUS_OLDER_VERSION_INSTALLED)) );
                        // /html/portlet/plugin_installer/browse_repository.jspf(83,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f9.setValue( PluginPackageImpl.STATUS_OLDER_VERSION_INSTALLED );
                        int _jspx_eval_aui_005foption_005f9 = _jspx_th_aui_005foption_005f9.doStartTag();
                        if (_jspx_th_aui_005foption_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f9);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f9);
                        out.write("\n");
                        out.write("\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f10 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f10.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f5);
                        // /html/portlet/plugin_installer/browse_repository.jspf(84,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f10.setLabel(new String("not-installed"));
                        // /html/portlet/plugin_installer/browse_repository.jspf(84,3) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f10.setSelected( (installStatus.equals(PluginPackageImpl.STATUS_NOT_INSTALLED)) );
                        // /html/portlet/plugin_installer/browse_repository.jspf(84,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f10.setValue( PluginPackageImpl.STATUS_NOT_INSTALLED );
                        int _jspx_eval_aui_005foption_005f10 = _jspx_th_aui_005foption_005f10.doStartTag();
                        if (_jspx_th_aui_005foption_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f10);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f10);
                        out.write("\n");
                        out.write("\t\t\t");
                        //  aui:option
                        com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f11 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                        _jspx_th_aui_005foption_005f11.setPageContext(_jspx_page_context);
                        _jspx_th_aui_005foption_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f5);
                        // /html/portlet/plugin_installer/browse_repository.jspf(85,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f11.setLabel(new String("all"));
                        // /html/portlet/plugin_installer/browse_repository.jspf(85,3) name = selected type = boolean reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f11.setSelected( (installStatus.equals(PluginPackageImpl.STATUS_ALL)) );
                        // /html/portlet/plugin_installer/browse_repository.jspf(85,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                        _jspx_th_aui_005foption_005f11.setValue( PluginPackageImpl.STATUS_ALL );
                        int _jspx_eval_aui_005foption_005f11 = _jspx_th_aui_005foption_005f11.doStartTag();
                        if (_jspx_th_aui_005foption_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f11);
                          return;
                        }
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f11);
                        out.write('\n');
                        out.write('	');
                        out.write('	');
                      }
                      if (_jspx_th_aui_005fselect_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f5);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t");
                      if (_jspx_meth_aui_005fbutton_002drow_005f4(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
                        return;
                      out.write('\n');
                      out.write('	');
                    }
                    if (_jspx_th_aui_005ffieldset_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f5);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f5);
                    out.write("\n");
                    out.write("\n");
                    out.write("\t<div class=\"separator\"><!-- --></div>\n");
                    out.write("\n");
                    out.write("\t");

	String orderByCol = ParamUtil.getString(request, "orderByCol");
	String orderByType = ParamUtil.getString(request, "orderByType");

	if (Validator.isNotNull(orderByCol) && Validator.isNotNull(orderByType)) {
		portalPreferences.setValue(PortletKeys.PLUGIN_INSTALLER, "plugin-packages-order-by-col", orderByCol);
		portalPreferences.setValue(PortletKeys.PLUGIN_INSTALLER, "plugin-packages-order-by-type", orderByType);
	}
	else {
		orderByCol = portalPreferences.getValue(PortletKeys.PLUGIN_INSTALLER, "plugin-packages-order-by-col", "modified-date");
		orderByType = portalPreferences.getValue(PortletKeys.PLUGIN_INSTALLER, "plugin-packages-order-by-type", "desc");
	}

	List<String> headerNames = new ArrayList<String>();

	headerNames.add(pluginType + "-plugin");
	headerNames.add("trusted");
	headerNames.add("tags");
	headerNames.add("installed-version");
	headerNames.add("available-version");
	headerNames.add("modified-date");

	Map orderableHeaders = new HashMap();

	orderableHeaders.put(pluginType + "-plugin", Field.TITLE);
	orderableHeaders.put("modified-date", "modified-date");

	SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, headerNames, LanguageUtil.get(pageContext, "no-" + pluginType + "-plugins-were-found"));

	searchContainer.setOrderableHeaders(orderableHeaders);
	searchContainer.setOrderByCol(orderByCol);
	searchContainer.setOrderByType(orderByType);

	List results = PluginPackageUtil.search(keywords, pluginType, tag, license, searchRepositoryURL, installStatus, QueryUtil.ALL_POS, QueryUtil.ALL_POS).toList();

	DocumentComparator docComparator = null;

	if (orderByType.equals("desc")) {
		docComparator = new DocumentComparator(false, false);
	}
	else {
		docComparator = new DocumentComparator();
	}

	if (orderByCol.equals("modified-date")) {
		docComparator.addOrderBy(Field.MODIFIED_DATE);
	}
	else {
		docComparator.addOrderBy(Field.TITLE);
		docComparator.addOrderBy("version");
	}

	results = ListUtil.sort(results, docComparator);

	int total = results.size();

	searchContainer.setTotal(total);

	results = ListUtil.subList(results, searchContainer.getStart(), searchContainer.getEnd());

	searchContainer.setResults(results);

	List resultRows = searchContainer.getResultRows();

	for (int i = 0; i < results.size(); i++) {
		Document doc = (Document)results.get(i);

		String pluginPackageName = doc.get(Field.TITLE);
		String pluginPackageModuleId = doc.get("moduleId");
		String pluginPackageGroupId = doc.get("groupId");
		String pluginPackageArtifactId = doc.get("artifactId");
		String pluginPackageAvailableVersion = doc.get("version");
		Date pluginPackageModifiedDate = doc.getDate(Field.MODIFIED);
		String pluginPackageTags = StringUtil.merge(doc.getValues("tag"), StringPool.COMMA + StringPool.SPACE);
		String pluginPackageShortDescription = doc.get("shortDescription");
		String pluginPackageChangeLog = doc.get("changeLog");
		String pluginPackageRepositoryURL = doc.get("repositoryURL");

		// The value for pluginPackage should never be null except when Lucene
		// is out of sync as in LEP-4783 or when the plugin was not installed
		// from a repository.

		PluginPackage pluginPackage = null;

		if (!pluginPackageRepositoryURL.startsWith(RemotePluginPackageRepository.LOCAL_URL)) {
			pluginPackage = PluginPackageUtil.getPluginPackageByModuleId(pluginPackageModuleId, pluginPackageRepositoryURL);
		}

		PluginPackage installedPluginPackage = PluginPackageUtil.getLatestInstalledPluginPackage(pluginPackageGroupId, pluginPackageArtifactId);

		ResultRow row = new ResultRow(doc, HtmlUtil.escapeAttribute(pluginPackageModuleId), i);

		PortletURL rowURL = renderResponse.createRenderURL();

		rowURL.setParameter("struts_action", "/plugin_installer/view");
		rowURL.setParameter("tabs1", tabs1);
		rowURL.setParameter("tabs2", tabs2);
		rowURL.setParameter("redirect", currentURL);
		rowURL.setParameter("moduleId", pluginPackageModuleId);
		rowURL.setParameter("repositoryURL", pluginPackageRepositoryURL);

		// Name, screenshots, and short description

		StringBundler sb = new StringBundler();

		if (pluginPackage != null) {
			sb.append("<a href='");
			sb.append(rowURL.toString());
			sb.append("'>");

			if (tabs2.equals("layout-templates") || tabs2.equals("themes")) {
				List screenshots = pluginPackage.getScreenshots();

				if (!screenshots.isEmpty()) {
					Screenshot screenshot = (Screenshot)screenshots.get(0);

					sb.append("<img align=\"left\" src=\"");
					sb.append(screenshot.getThumbnailURL());
					sb.append("\" style=\"margin-right: 10px\" />");
				}
			}
		}

		sb.append("<strong>");
		sb.append(HtmlUtil.escape(pluginPackageName));
		sb.append("</strong> ");
		sb.append(HtmlUtil.escape(pluginPackageAvailableVersion));

		if (pluginPackage != null) {
			sb.append("</a>");
		}

		if (Validator.isNotNull(pluginPackageShortDescription)) {
			sb.append("<br />");
			sb.append(LanguageUtil.get(pageContext, "id"));
			sb.append(": ");
			sb.append(HtmlUtil.escape(pluginPackageModuleId));
			sb.append("<br />");
			sb.append(HtmlUtil.escape(pluginPackageShortDescription));
		}

		row.addText(sb.toString());

		// Trusted

		if (PluginPackageUtil.isTrusted(pluginPackageRepositoryURL)) {
			row.addText(LanguageUtil.get(pageContext, "yes"));
		}
		else {
			row.addText(LanguageUtil.get(pageContext, "no"));
		}

		// Tags

		TextSearchEntry rowTextEntry = new TextSearchEntry();

		rowTextEntry.setName(HtmlUtil.escape(pluginPackageTags));

		row.addText(rowTextEntry);

		// Installed version

		if (installedPluginPackage != null) {
			row.addText(HtmlUtil.escape(installedPluginPackage.getVersion()));
		}
		else {
			row.addText(StringPool.DASH);
		}

		// Available version

		sb.setIndex(0);

		sb.append(HtmlUtil.escape(pluginPackageAvailableVersion));
		sb.append("&nbsp;<img align=\"absmiddle\" border=\"0\" src='");
		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/document_library/page.png");
		sb.append("' onmouseover=\"Liferay.Portal.ToolTip.show(this, '");
		sb.append(HtmlUtil.escapeJS(HtmlUtil.escape(pluginPackageChangeLog)));
		sb.append("')\" />");

		row.addText(sb.toString());

		// Modified date

		row.addText(dateFormatDateTime.format(pluginPackageModifiedDate));

		// Add result row

		resultRows.add(row);
	}
	
                    out.write('\n');
                    out.write('\n');
                    out.write('	');
                    //  liferay-ui:search-iterator
                    com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f0 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
                    _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setPageContext(_jspx_page_context);
                    _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
                    // /html/portlet/plugin_installer/browse_repository.jspf(288,1) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setSearchContainer( searchContainer );
                    int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f0 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doStartTag();
                    if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
                      return;
                    }
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
                    out.write("\n");
                    out.write("\n");
                    out.write("\t<div class=\"separator\"><!-- --></div>\n");
                    out.write("\n");
                    out.write("\t<div>\n");
                    out.write("\t\t");
                    //  c:if
                    com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f12 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                    _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
                    _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
                    // /html/portlet/plugin_installer/browse_repository.jspf(293,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_c_005fif_005f12.setTest( PluginPackageUtil.getLastUpdateDate() != null );
                    int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
                    if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write("\n");
                      out.write("\t\t\t ");
                      out.print( LanguageUtil.format(pageContext, "list-of-plugins-was-last-refreshed-on-x", dateFormatDateTime.format(PluginPackageUtil.getLastUpdateDate())) );
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                    }
                    if (_jspx_th_c_005fif_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
                      return;
                    }
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
                    out.write("\n");
                    out.write("\n");
                    out.write("\t\t");
                    //  aui:button
                    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f5 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                    _jspx_th_aui_005fbutton_005f5.setPageContext(_jspx_page_context);
                    _jspx_th_aui_005fbutton_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
                    // /html/portlet/plugin_installer/browse_repository.jspf(297,2) name = onClick type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_aui_005fbutton_005f5.setOnClick( renderResponse.getNamespace() + "reloadRepositories();" );
                    // /html/portlet/plugin_installer/browse_repository.jspf(297,2) name = value type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_aui_005fbutton_005f5.setValue("refresh");
                    int _jspx_eval_aui_005fbutton_005f5 = _jspx_th_aui_005fbutton_005f5.doStartTag();
                    if (_jspx_th_aui_005fbutton_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f5);
                      return;
                    }
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f5);
                    out.write("\n");
                    out.write("\t</div>\n");
                    out.write("\n");
                    out.write("\t");
                    if (_jspx_meth_liferay_002dutil_005finclude_005f1(_jspx_th_c_005fotherwise_005f4, _jspx_page_context))
                      return;
                    out.write('\n');
                    out.write('\n');

}
catch (PluginPackageException ppe) {
	if (_log.isWarnEnabled()) {
		_log.warn(ppe.getMessage());
	}

                    out.write("\n");
                    out.write("\n");
                    out.write("\t<div class=\"portlet-msg-error\">\n");
                    out.write("\t\t");
                    if (_jspx_meth_liferay_002dui_005fmessage_005f15(_jspx_th_c_005fotherwise_005f4, _jspx_page_context))
                      return;
                    out.write("\n");
                    out.write("\t</div>\n");
                    out.write("\n");
                    out.write("\t");
                    //  c:if
                    com.liferay.taglib.core.IfTag _jspx_th_c_005fif_005f13 = (com.liferay.taglib.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(com.liferay.taglib.core.IfTag.class);
                    _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
                    _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
                    // /html/portlet/plugin_installer/browse_repository.jspf(314,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
                    _jspx_th_c_005fif_005f13.setTest( windowState.equals(WindowState.MAXIMIZED) );
                    int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
                    if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      if (_jspx_meth_aui_005fscript_005f1(_jspx_th_c_005fif_005f13, _jspx_page_context))
                        return;
                      out.write('\n');
                      out.write('	');
                    }
                    if (_jspx_th_c_005fif_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
                      return;
                    }
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
                    out.write('\n');
                    out.write('\n');

}

                    out.write('\n');
                    out.write('\n');
                    out.write("\n");
                    out.write("\t\t\t\t\t\t");
                  }
                  if (_jspx_th_c_005fotherwise_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                }
                if (_jspx_th_c_005fchoose_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                  _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
                  return;
                }
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
                out.write("\n");
                out.write("\t\t\t\t");
              }
              if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
              out.write("\n");
              out.write("\t\t\t");
            }
            if (_jspx_th_c_005fchoose_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
              return;
            }
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
            out.write("\n");
            out.write("\n");
            out.write("\t\t");
          }
          if (_jspx_th_aui_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005faction.reuse(_jspx_th_aui_005fform_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005faction.reuse(_jspx_th_aui_005fform_005f0);
          out.write("\n");
          out.write("\n");
          out.write("\t\t");
          //  aui:script
          com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f2 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
          _jspx_th_aui_005fscript_005f2.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fscript_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
          int _jspx_eval_aui_005fscript_005f2 = _jspx_th_aui_005fscript_005f2.doStartTag();
          if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fscript_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fscript_005f2.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\tfunction ");
              if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("installPluginPackage(cmd) {\n");
              out.write("\t\t\t\tif (cmd == \"localDeploy\") {\n");
              out.write("\t\t\t\t\tdocument.");
              if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("fm.encoding = \"multipart/form-data\";\n");
              out.write("\t\t\t\t}\n");
              out.write("\n");
              out.write("\t\t\t\tdocument.");
              if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write('f');
              out.write('m');
              out.write('.');
              if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.print( Constants.CMD );
              out.write(".value = cmd;\n");
              out.write("\t\t\t\tsubmitForm(document.");
              if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("fm, \"");
              if (_jspx_meth_portlet_005factionURL_005f0(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("\");\n");
              out.write("\t\t\t}\n");
              out.write("\n");
              out.write("\t\t\tfunction ");
              if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("reloadRepositories() {\n");
              out.write("\t\t\t\tdocument.");
              if (_jspx_meth_portlet_005fnamespace_005f10(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write('f');
              out.write('m');
              out.write('.');
              if (_jspx_meth_portlet_005fnamespace_005f11(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.print( Constants.CMD );
              out.write(".value = \"reloadRepositories\";\n");
              out.write("\t\t\t\tsubmitForm(document.");
              if (_jspx_meth_portlet_005fnamespace_005f12(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("fm, \"");
              if (_jspx_meth_portlet_005factionURL_005f1(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("\");\n");
              out.write("\t\t\t}\n");
              out.write("\n");
              out.write("\t\t\tfunction ");
              if (_jspx_meth_portlet_005fnamespace_005f13(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("saveDeployConfiguration() {\n");
              out.write("\t\t\t\tdocument.");
              if (_jspx_meth_portlet_005fnamespace_005f14(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write('f');
              out.write('m');
              out.write('.');
              if (_jspx_meth_portlet_005fnamespace_005f15(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.print( Constants.CMD );
              out.write(".value = 'deployConfiguration';\n");
              out.write("\t\t\t\tsubmitForm(document.");
              if (_jspx_meth_portlet_005fnamespace_005f16(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("fm, \"");
              if (_jspx_meth_portlet_005factionURL_005f2(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                return;
              out.write("\");\n");
              out.write("\t\t\t}\n");
              out.write("\t\t");
              int evalDoAfterBody = _jspx_th_aui_005fscript_005f2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fscript_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f2);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f2);
          out.write("\n");
          out.write("\n");
          out.write("\t\t");
          //  aui:script
          com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f3 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
          _jspx_th_aui_005fscript_005f3.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fscript_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
          // /html/portlet/plugin_installer/view.jsp(144,2) name = use type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
          _jspx_th_aui_005fscript_005f3.setUse("aui-base");
          int _jspx_eval_aui_005fscript_005f3 = _jspx_th_aui_005fscript_005f3.doStartTag();
          if (_jspx_eval_aui_005fscript_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fscript_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fscript_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fscript_005f3.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\tvar description = A.one('#cpContextPanelTemplate');\n");
              out.write("\n");
              out.write("\t\t\tif (description) {\n");
              out.write("\t\t\t\tdescription.append('<span class=\"warn\">");
              //  liferay-ui:message
              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f16 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
              _jspx_th_liferay_002dui_005fmessage_005f16.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fmessage_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f3);
              // /html/portlet/plugin_installer/view.jsp(148,43) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dui_005fmessage_005f16.setKey("warning-x-will-be-replaced-with-liferay-marketplace");
              // /html/portlet/plugin_installer/view.jsp(148,43) name = arguments type = null reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
              _jspx_th_liferay_002dui_005fmessage_005f16.setArguments( portletDisplay.getTitle() );
              int _jspx_eval_liferay_002dui_005fmessage_005f16 = _jspx_th_liferay_002dui_005fmessage_005f16.doStartTag();
              if (_jspx_th_liferay_002dui_005fmessage_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
              out.write("</span>');\n");
              out.write("\t\t\t}\n");
              out.write("\t\t");
              int evalDoAfterBody = _jspx_th_aui_005fscript_005f3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fscript_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fscript_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f3);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f3);
          out.write('\n');
          out.write('	');
        }
        if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
          return;
        }
        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
        out.write('\n');
        out.write('	');
        if (_jspx_meth_c_005fotherwise_005f5(_jspx_th_c_005fchoose_005f0, _jspx_page_context))
          return;
        out.write('\n');
      }
      if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
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

  private boolean _jspx_meth_liferay_002dui_005fsuccess_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:success
    com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f0 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
    _jspx_th_liferay_002dui_005fsuccess_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsuccess_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(31,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fsuccess_005f0.setKey("pluginDownloaded");
    // /html/portlet/plugin_installer/view_plugin_package.jspf(31,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fsuccess_005f0.setMessage("the-plugin-was-downloaded-successfully-and-is-now-being-installed");
    int _jspx_eval_liferay_002dui_005fsuccess_005f0 = _jspx_th_liferay_002dui_005fsuccess_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fsuccess_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(39,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("name");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(47,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("author");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(55,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("types");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(76,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("tags");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(97,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("licenses");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(113,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("open-source");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(126,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("liferay-versions");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(147,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f8.setKey("repository");
    int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f9 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(154,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f9.setKey("trusted");
    int _jspx_eval_liferay_002dui_005fmessage_005f9 = _jspx_th_liferay_002dui_005fmessage_005f9.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
    int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      out.write("\n");
      out.write("\t\t\t\t(");
      if (_jspx_meth_liferay_002dui_005fmessage_005f10(_jspx_th_c_005fotherwise_005f0, _jspx_page_context))
        return true;
      out.write(")\n");
      out.write("\t\t\t");
    }
    if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f10 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(157,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f10.setKey("untrusted");
    int _jspx_eval_liferay_002dui_005fmessage_005f10 = _jspx_th_liferay_002dui_005fmessage_005f10.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f11 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f11.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(169,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f11.setKey("short-description");
    int _jspx_eval_liferay_002dui_005fmessage_005f11 = _jspx_th_liferay_002dui_005fmessage_005f11.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f12 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f12.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(179,3) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f12.setKey("long-description");
    int _jspx_eval_liferay_002dui_005fmessage_005f12 = _jspx_th_liferay_002dui_005fmessage_005f12.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f13 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f13.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(190,3) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f13.setKey("change-log");
    int _jspx_eval_liferay_002dui_005fmessage_005f13 = _jspx_th_liferay_002dui_005fmessage_005f13.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fa_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f14 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f14.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fa_005f3);
    // /html/portlet/plugin_installer/view_plugin_package.jspf(211,81) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f14.setKey("thumbnail");
    int _jspx_eval_liferay_002dui_005fmessage_005f14 = _jspx_th_liferay_002dui_005fmessage_005f14.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsuccess_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:success
    com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f1 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
    _jspx_th_liferay_002dui_005fsuccess_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsuccess_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
    // /html/portlet/plugin_installer/upload_file.jspf(17,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fsuccess_005f1.setKey("pluginUploaded");
    // /html/portlet/plugin_installer/upload_file.jspf(17,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fsuccess_005f1.setMessage("the-plugin-was-uploaded-successfully-and-is-now-being-installed");
    int _jspx_eval_liferay_002dui_005fsuccess_005f1 = _jspx_th_liferay_002dui_005fsuccess_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fsuccess_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f1);
    return false;
  }

  private boolean _jspx_meth_aui_005ffieldset_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:fieldset
    com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f0 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
    _jspx_th_aui_005ffieldset_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005ffieldset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
    int _jspx_eval_aui_005ffieldset_005f0 = _jspx_th_aui_005ffieldset_005f0.doStartTag();
    if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      out.write('\n');
      out.write('	');
      if (_jspx_meth_aui_005finput_005f12(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
        return true;
      out.write('\n');
    }
    if (_jspx_th_aui_005ffieldset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f12 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f12.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/plugin_installer/upload_file.jspf(22,1) name = cssClass type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f12.setCssClass("lfr-input-text");
    // /html/portlet/plugin_installer/upload_file.jspf(22,1) name = label type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f12.setLabel("upload-a-war-file-to-install-a-layout-template,-portlet,-or-theme");
    // /html/portlet/plugin_installer/upload_file.jspf(22,1) name = name type = java.lang.String reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f12.setName("file");
    // /html/portlet/plugin_installer/upload_file.jspf(22,1) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005finput_005f12.setType("file");
    int _jspx_eval_aui_005finput_005f12 = _jspx_th_aui_005finput_005f12.doStartTag();
    if (_jspx_th_aui_005finput_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f12);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsuccess_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:success
    com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f2 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
    _jspx_th_liferay_002dui_005fsuccess_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsuccess_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
    // /html/portlet/plugin_installer/download_file.jspf(17,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fsuccess_005f2.setKey("pluginDownloaded");
    // /html/portlet/plugin_installer/download_file.jspf(17,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fsuccess_005f2.setMessage("the-plugin-was-downloaded-successfully-and-is-now-being-installed");
    int _jspx_eval_liferay_002dui_005fsuccess_005f2 = _jspx_th_liferay_002dui_005fsuccess_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fsuccess_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f2);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f0 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
    // /html/portlet/plugin_installer/configuration.jspf(25,2) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005foption_005f0.setLabel(new String("disable"));
    // /html/portlet/plugin_installer/configuration.jspf(25,2) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005foption_005f0.setValue(new String("0"));
    int _jspx_eval_aui_005foption_005f0 = _jspx_th_aui_005foption_005f0.doStartTag();
    if (_jspx_th_aui_005foption_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    // /html/portlet/plugin_installer/configuration.jspf(127,0) name = page type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dutil_005finclude_005f0.setPage("/html/portlet/plugin_installer/repository_report.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
    int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
    if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f0.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\tLiferay.Util.focusFormField(document.");
        if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
          return true;
        out.write('f');
        out.write('m');
        out.write('.');
        if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
          return true;
        out.write("deployDir);\n");
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
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
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

  private boolean _jspx_meth_aui_005foption_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f4 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f4.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/plugin_installer/browse_repository.jspf(46,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005foption_005f4.setLabel(new String("all"));
    // /html/portlet/plugin_installer/browse_repository.jspf(46,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005foption_005f4.setValue(new String(""));
    int _jspx_eval_aui_005foption_005f4 = _jspx_th_aui_005foption_005f4.doStartTag();
    if (_jspx_th_aui_005foption_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f6 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f6.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f4);
    // /html/portlet/plugin_installer/browse_repository.jspf(64,3) name = label type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005foption_005f6.setLabel(new String("all"));
    // /html/portlet/plugin_installer/browse_repository.jspf(64,3) name = value type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005foption_005f6.setValue(new String(""));
    int _jspx_eval_aui_005foption_005f6 = _jspx_th_aui_005foption_005f6.doStartTag();
    if (_jspx_th_aui_005foption_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f6);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_002drow_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button-row
    com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f4 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
    _jspx_th_aui_005fbutton_002drow_005f4.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_002drow_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    int _jspx_eval_aui_005fbutton_002drow_005f4 = _jspx_th_aui_005fbutton_002drow_005f4.doStartTag();
    if (_jspx_eval_aui_005fbutton_002drow_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      out.write("\n");
      out.write("\t\t\t");
      if (_jspx_meth_aui_005fbutton_005f4(_jspx_th_aui_005fbutton_002drow_005f4, _jspx_page_context))
        return true;
      out.write('\n');
      out.write('	');
      out.write('	');
    }
    if (_jspx_th_aui_005fbutton_002drow_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f4);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fbutton_002drow_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f4 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f4.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f4);
    // /html/portlet/plugin_installer/browse_repository.jspf(89,3) name = type type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fbutton_005f4.setType("submit");
    // /html/portlet/plugin_installer/browse_repository.jspf(89,3) name = value type = java.lang.String reqTime = true required = false fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_aui_005fbutton_005f4.setValue("search");
    int _jspx_eval_aui_005fbutton_005f4 = _jspx_th_aui_005fbutton_005f4.doStartTag();
    if (_jspx_th_aui_005fbutton_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f1 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
    // /html/portlet/plugin_installer/browse_repository.jspf(300,1) name = page type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dutil_005finclude_005f1.setPage("/html/portlet/plugin_installer/repository_report.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f1 = _jspx_th_liferay_002dutil_005finclude_005f1.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f15 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f15.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
    // /html/portlet/plugin_installer/browse_repository.jspf(311,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dui_005fmessage_005f15.setKey("an-error-occurred-while-retrieving-available-plugins");
    int _jspx_eval_liferay_002dui_005fmessage_005f15 = _jspx_th_liferay_002dui_005fmessage_005f15.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
    if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f1.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\t\tLiferay.Util.focusFormField(document.");
        if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
          return true;
        out.write('f');
        out.write('m');
        out.write('.');
        if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
          return true;
        out.write("keywords);\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_aui_005fscript_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fscript_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f2 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f2 = _jspx_th_portlet_005fnamespace_005f2.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f3 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f3 = _jspx_th_portlet_005fnamespace_005f3.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f4 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f4 = _jspx_th_portlet_005fnamespace_005f4.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f7 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f7 = _jspx_th_portlet_005fnamespace_005f7.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f8 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f8 = _jspx_th_portlet_005fnamespace_005f8.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005factionURL_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:actionURL
    com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
    _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005factionURL_005f0 = _jspx_th_portlet_005factionURL_005f0.doStartTag();
    if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
        return true;
    }
    if (_jspx_th_portlet_005factionURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/plugin_installer/view.jsp(130,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/plugin_installer/view.jsp(130,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f0.setValue("/plugin_installer/install_plugin");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f9 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f9 = _jspx_th_portlet_005fnamespace_005f9.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f10 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f10 = _jspx_th_portlet_005fnamespace_005f10.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f11 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f11 = _jspx_th_portlet_005fnamespace_005f11.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f12 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f12 = _jspx_th_portlet_005fnamespace_005f12.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
    return false;
  }

  private boolean _jspx_meth_portlet_005factionURL_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:actionURL
    com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
    _jspx_th_portlet_005factionURL_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005factionURL_005f1 = _jspx_th_portlet_005factionURL_005f1.doStartTag();
    if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_meth_portlet_005fparam_005f1(_jspx_th_portlet_005factionURL_005f1, _jspx_page_context))
        return true;
    }
    if (_jspx_th_portlet_005factionURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
    // /html/portlet/plugin_installer/view.jsp(135,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f1.setName("struts_action");
    // /html/portlet/plugin_installer/view.jsp(135,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f1.setValue("/plugin_installer/install_plugin");
    int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
    if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f13 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f13 = _jspx_th_portlet_005fnamespace_005f13.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f14 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f14 = _jspx_th_portlet_005fnamespace_005f14.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f15 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f15 = _jspx_th_portlet_005fnamespace_005f15.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f16 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f16.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f16 = _jspx_th_portlet_005fnamespace_005f16.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
    return false;
  }

  private boolean _jspx_meth_portlet_005factionURL_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:actionURL
    com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f2 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
    _jspx_th_portlet_005factionURL_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005factionURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005factionURL_005f2 = _jspx_th_portlet_005factionURL_005f2.doStartTag();
    if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_meth_portlet_005fparam_005f2(_jspx_th_portlet_005factionURL_005f2, _jspx_page_context))
        return true;
    }
    if (_jspx_th_portlet_005factionURL_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
    // /html/portlet/plugin_installer/view.jsp(140,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f2.setName("struts_action");
    // /html/portlet/plugin_installer/view.jsp(140,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_portlet_005fparam_005f2.setValue("/plugin_installer/install_plugin");
    int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
    if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    com.liferay.taglib.core.OtherwiseTag _jspx_th_c_005fotherwise_005f5 = (com.liferay.taglib.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(com.liferay.taglib.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f5.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
    int _jspx_eval_c_005fotherwise_005f5 = _jspx_th_c_005fotherwise_005f5.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      out.write('\n');
      out.write('	');
      out.write('	');
      if (_jspx_meth_liferay_002dutil_005finclude_005f2(_jspx_th_c_005fotherwise_005f5, _jspx_page_context))
        return true;
      out.write('\n');
      out.write('	');
    }
    if (_jspx_th_c_005fotherwise_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f2 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f5);
    // /html/portlet/plugin_installer/view.jsp(153,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false deferredMethod = false expectedTypeName = null methodSignature = null 
    _jspx_th_liferay_002dutil_005finclude_005f2.setPage("/html/portal/portlet_access_denied.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f2 = _jspx_th_liferay_002dutil_005finclude_005f2.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f2);
    return false;
  }
}
