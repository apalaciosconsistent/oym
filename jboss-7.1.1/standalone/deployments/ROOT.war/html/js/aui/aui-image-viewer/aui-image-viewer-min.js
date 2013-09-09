AUI.add("aui-image-viewer-base",function(ar){var ak=ar.Lang,aK=ak.isBoolean,Q=ak.isNumber,D=ak.isObject,aP=ak.isString,au=ar.Plugin.NodeFX,ay=ar.config.doc,aY="anim",d="arrow",M="arrowLeftEl",h="arrowRightEl",aW="auto",aS="bd",T="blank",aC="body",o="boundingBox",aH="caption",x="captionEl",aF="captionFromTitle",c="centered",e="close",at="closeEl",H="createDocumentFragment",aG="currentIndex",aQ="easeBothStrong",aN="footer",m="helper",ah="hidden",t="hide",aD="href",ag="icon",E="image",w="imageAnim",aM="image-viewer",ax="info",P="infoEl",al="infoTemplate",a="left",ap="link",X="links",aL="loader",aV="loading",K="loadingEl",v="lock",n="maxHeight",aR="maxWidth",V="modal",I="offsetHeight",aO="offsetWidth",Y="opacity",k="overlay",aw="preloadAllImages",aj="preloadNeighborImages",ab="px",i="region",j="right",u="scroll",ao="show",C="showArrows",aI="showClose",q=" ",aB="src",aE="title",l="top",O="totalLinks",W="viewportRegion",ad="visible",aq="ownerDocument",aU=function(A){return(A instanceof ar.NodeList);},aT=function(){return Array.prototype.slice.call(arguments).join(q);},z=ar.getClassName,Z=z(m,u,v),S=z(ag,aV),y=z(aM,d),an=z(aM,d,a),aJ=z(aM,d,j),F=z(aM,aS),ac=z(aM,aH),B=z(aM,e),ai=z(aM,E),b=z(aM,ax),aX=z(aM,ap),aA=z(aM,aV),r=z(k,ah),p="ESC",R="RIGHT",az="LEFT",af={height:aW,width:aW},f=ay.createTextNode(""),U="Image {current} of {total}",am='<a href="#" class="'+aT(y,an)+'"></a>',av='<a href="#" class="'+aT(y,aJ)+'"></a>',s='<div class="'+ac+'"></div>',g='<a href="#" class="'+B+'"></a>',G='<img class="'+ai+'" />',aa='<div class="'+b+'"></div>',N='<div class="'+r+'"></div>',J='<div class="'+S+'"></div>';var ae=ar.Component.create({NAME:aM,ATTRS:{anim:{value:true,validator:aK},bodyContent:{value:f},caption:{value:T,validator:aP},captionFromTitle:{value:true,validator:aK},centered:{value:true},currentIndex:{value:0,validator:Q},image:{readOnly:true,valueFn:function(){return ar.Node.create(G);}},imageAnim:{value:{},setter:function(A){return ar.merge({to:{opacity:1},easing:aQ,duration:0.8},A);},validator:D},infoTemplate:{getter:function(A){return this._getInfoTemplate(A);},value:U,validator:aP},links:{setter:function(L){var A=this;if(aU(L)){return L;}else{if(aP(L)){return ar.all(L);}}return new ar.NodeList([L]);}},loading:{value:false,validator:aK},modal:{value:{opacity:0.8,background:"#000"}},preloadAllImages:{value:false,validator:aK},preloadNeighborImages:{value:true,validator:aK},showClose:{value:true,validator:aK},showArrows:{value:true,validator:aK},totalLinks:{readOnly:true,getter:function(A){return this.get(X).size();}},visible:{value:false},zIndex:{value:3000,validator:Q},arrowLeftEl:{readOnly:true,valueFn:function(){return ar.Node.create(am);}},arrowRightEl:{readOnly:true,valueFn:function(){return ar.Node.create(av);}},captionEl:{readOnly:true,valueFn:function(){return ar.Node.create(s);}},closeEl:{readOnly:true,valueFn:function(){return ar.Node.create(g);}},infoEl:{readOnly:true,valueFn:function(){return ar.Node.create(aa);}},loader:{readOnly:true,valueFn:function(){return ar.Node.create(N).appendTo(ay.body);}},loadingEl:{valueFn:function(){return ar.Node.create(J);}},maxHeight:{value:Infinity,validator:Q},maxWidth:{value:Infinity,validator:Q}},EXTENDS:ar.OverlayBase,prototype:{_keyHandler:null,renderUI:function(){var A=this;A._renderControls();A._renderFooter();A.get(X).addClass(aX);},bindUI:function(){var A=this;var L=A.get(X);var a0=A.get(M);var aZ=A.get(h);var a1=A.get(at);a0.on("click",ar.bind(A._onClickLeftArrow,A));aZ.on("click",ar.bind(A._onClickRightArrow,A));a1.on("click",ar.bind(A._onClickCloseEl,A));L.on("click",ar.bind(A._onClickLinks,A));A._keyHandler=ar.bind(A._onKeyInteraction,A);ar.getDoc().on("keydown",A._keyHandler);A.after("render",A._afterRender);A.after("loadingChange",A._afterLoadingChange);A.after("visibleChange",A._afterVisibleChange);},destructor:function(){var A=this;var L=A.get(X);A.close();L.detach("click");L.removeClass(aX);ar.getDoc().detach("keydown",A._keyHandler);A.get(M).remove(true);A.get(h).remove(true);A.get(at).remove(true);A.get(aL).remove(true);},close:function(){var A=this;A.hide();A.hideMask();},getLink:function(L){var A=this;return A.get(X).item(L);},getCurrentLink:function(){var A=this;return A.getLink(A.get(aG));},loadImage:function(A){var a6=this;var a0=a6.bodyNode;var a5=a6.get(aL);a6.set(aV,true);var L=a6._activeImagePool;if(!L){L=[];var a4=a6.get(E);var a3=a4.clone();var a2=a4.clone();var a1=ar.bind(a6._onLoadImage,a6);a3.on("load",a1);a2.on("load",a1);L.push(a3,a2);a6._activeImagePool=L;}var aZ=L[0];aZ.removeAttribute("height");aZ.removeAttribute("width");aZ.setStyles(af);a5.append(aZ);L.push(L.shift(aZ));if(ar.UA.webkit){aZ.attr(aB,"data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==");}aZ.attr(aB,A);a6.fire("request",{image:aZ});},hasLink:function(L){var A=this;return A.getLink(L);},hasNext:function(){var A=this;return A.hasLink(A.get(aG)+1);},hasPrev:function(){var A=this;return A.hasLink(A.get(aG)-1);},hideControls:function(){var A=this;A.get(M).hide();A.get(h).hide();A.get(at).hide();},hideMask:function(){ar.ImageViewerMask.hide();},next:function(){var A=this;if(A.hasNext()){A.set(aG,A.get(aG)+1);A.show();}},preloadAllImages:function(){var A=this;A.get(X).each(function(aZ,L){A.preloadImage(L);});},preloadImage:function(L){var A=this;var aZ=A.getLink(L);if(aZ){var a0=aZ.attr(aD);A._createPreloadImage(a0);}},prev:function(){var A=this;if(A.hasPrev()){A.set(aG,A.get(aG)-1);A.show();}},showLoading:function(){var A=this;var L=A.get(K);A.setStdModContent(aC,L);L.center(A.bodyNode);},showMask:function(){var A=this;var L=A.get(V);if(D(L)){ar.each(L,function(a0,aZ){ar.ImageViewerMask.set(aZ,a0);});}if(L){ar.ImageViewerMask.show();}},show:function(){var A=this;var L=A.getCurrentLink();if(L){A.showMask();ae.superclass.show.apply(this,arguments);A.loadImage(L.attr(aD));}},_clearPreloadImageFn:function(){var A=this;var aZ=A._preloadImagePool;var a0;for(var L in aZ){a0=aZ[L];if(a0&&a0.complete){aZ[L]=null;}}},_createPreloadImage:function(a0){var A=this;
var L=A._preloadImagePool;if(!L){L=A._preloadImagePool={};A._clearPreloadImageTask=ar.debounce(A._clearPreloadImageFn,50,A);}if(!(a0 in L)){var aZ=new Image();aZ.onload=A._clearPreloadImageTask;aZ.src=a0;L[a0]=aZ;}},_renderControls:function(){var L=this;var A=ar.one(aC);A.append(L.get(M).hide());A.append(L.get(h).hide());A.append(L.get(at).hide());},_renderFooter:function(){var A=this;var L=A.get(o);var aZ=L.get(aq).invoke(H);aZ.append(A.get(x));aZ.append(A.get(P));A.setStdModContent(aN,aZ);},_syncCaptionUI:function(){var A=this;var aZ=A.get(aH);var a1=A.get(x);var L=A.get(aF);if(L){var a0=A.getCurrentLink();if(a0){var a2=a0.attr(aE);if(a2){aZ=a0.attr(aE);}}}a1.html(aZ);},_syncControlsUI:function(){var A=this;var aZ=A.get(o);var a0=A.get(M);var L=A.get(h);var a3=A.get(at);if(A.get(ad)){if(A.get(C)){var a2=aZ.get(W);var a1=Math.floor(a2.height/2)+a2.top;a0[A.hasPrev()?ao:t]();L[A.hasNext()?ao:t]();a0.setStyle(l,a1-a0.get(I)+ab);L.setStyle(l,a1-L.get(I)+ab);}if(A.get(aI)){a3.show();}}else{A.hideControls();}},_syncImageViewerUI:function(){var A=this;A._syncControlsUI();A._syncCaptionUI();A._syncInfoUI();},_syncInfoUI:function(){var A=this;var L=A.get(P);L.html(A.get(al));},_getRatio:function(a1,L){var A=this;var a0=1;var a3=A.get(n);var a2=A.get(aR);if((L>a3)||(a1>a2)){var a4=(L/a3);var aZ=(a1/a2);a0=Math.max(a4,aZ);}return a0;},_getInfoTemplate:function(L){var A=this;var aZ=A.get(O);var a0=A.get(aG)+1;return ak.sub(L,{current:a0,total:aZ});},_displayLoadedImage:function(aZ){var A=this;A.setStdModContent(aC,aZ);A._uiSetImageSize(aZ);A._syncImageViewerUI();A._setAlignCenter(true);A.set(aV,false);A.fire("load",{image:aZ});if(A.get(aj)){var L=A.get(aG);A.preloadImage(L+1);A.preloadImage(L-1);}},_afterRender:function(){var A=this;var L=A.bodyNode;L.addClass(F);if(A.get(aw)){A.preloadAllImages();}},_afterLoadingChange:function(aZ){var A=this;var L=A.get(o);if(aZ.newVal){L.addClass(aA);A.showLoading();}else{L.removeClass(aA);}},_afterVisibleChange:function(L){var A=this;A._syncControlsUI();},_onClickCloseEl:function(L){var A=this;A.close();L.halt();},_onClickLeftArrow:function(L){var A=this;A.prev();L.halt();},_onClickRightArrow:function(L){var A=this;A.next();L.halt();},_onClickLinks:function(L){var A=this;var aZ=L.currentTarget;A.set(aG,A.get(X).indexOf(aZ));A.show();L.preventDefault();},_onKeyInteraction:function(L){var A=this;if(!A.get(ad)){return false;}if(L.isKey(az)){A.prev();}else{if(L.isKey(R)){A.next();}else{if(L.isKey(p)){A.close();}}}},_onLoadImage:function(L){var A=this;var aZ=L.currentTarget;var a0=A.get(w);if(A.get(aY)){aZ.setStyle(Y,0);aZ.unplug(au).plug(au);aZ.fx.on("end",function(a1){A.fire("anim",{anim:a1,image:aZ});A._displayLoadedImage(aZ);});aZ.fx.setAttrs(a0);aZ.fx.stop().run();}else{A._displayLoadedImage(aZ);}},_uiSetImageSize:function(a3){var L=this;var a1=L.bodyNode;var a2=a3.get(i);var a0=L._getRatio(a2.width,a2.height);var A=(a2.height/a0);var aZ=(a2.width/a0);a3.set(I,A);a3.set(aO,aZ);a1.setStyles({height:A+ab,width:aZ+ab});}}});ar.ImageViewer=ae;ar.ImageViewerMask=new ar.OverlayMask().render();},"1.5.2",{skinnable:true,requires:["anim","aui-overlay-mask"]});AUI.add("aui-image-viewer-gallery",function(r){var m=r.Lang,E=m.isBoolean,ag=m.isNumber,N=m.isObject,p=m.isString,o="autoPlay",V="body",O="content",e="currentIndex",Q="delay",w=".",K="entry",g="handler",Y="hidden",T="href",d="image-gallery",G="img",x="left",S="links",R="offsetWidth",t="overlay",v="page",aj="paginator",F="paginatorEl",H="paginatorInstance",i="pause",D="paused",b="pausedLabel",aa="play",y="player",B="playing",n="playingLabel",ah="px",l="repeat",s="showPlayer",a=" ",ac="src",ab="thumb",W="toolbar",ai="totalLinks",u="useOriginalImage",I="viewportRegion",af="visible",C=function(){return Array.prototype.slice.call(arguments).join(a);},j=r.getClassName,M=j(d,aj),q=j(d,aj,O),P=j(d,aj,K),U=j(d,aj,S),ae=j(d,aj,ab),c=j(d,y),X=j(d,y,O),f=j(t,Y),al="(playing)",k='<div class="'+q+'">{PageLinks}</div>',z='<span class="'+P+'"><span class="'+ae+'"></span></span>',h='<div class="'+U+'"></div>',ak='<div class="'+C(f,M)+'"></div>',Z='<div class="'+c+'"></div>',ad='<span class="'+X+'"></span>';var J=r.Component.create({NAME:d,ATTRS:{autoPlay:{value:false,validator:E},delay:{value:7000,validator:ag},paginator:{value:{},setter:function(L){var A=this;var an=A.get(F);var am=A.get(ai);return r.merge({containers:an,pageContainerTemplate:h,pageLinkContent:r.bind(A._setThumbContent,A),pageLinkTemplate:z,template:k,total:am,on:{changeRequest:function(ao){A.fire("changeRequest",{state:ao.state});}}},L);},validator:N},paginatorEl:{readyOnly:true,valueFn:function(){return r.Node.create(ak);}},paginatorInstance:{value:null},paused:{value:false,validator:E},pausedLabel:{value:"",validator:p},playing:{value:false,validator:E},playingLabel:{value:al,validator:p},repeat:{value:true,validator:E},showPlayer:{value:true,validator:E},toolbar:{value:{},setter:function(L){var A=this;return r.merge({children:[{id:aa,icon:aa},{id:i,icon:i}]},L);},validator:N},useOriginalImage:{value:false,validator:E}},EXTENDS:r.ImageViewer,prototype:{toolbar:null,_timer:null,renderUI:function(){var A=this;J.superclass.renderUI.apply(this,arguments);A._renderPaginator();if(A.get(s)){A._renderPlayer();}},bindUI:function(){var A=this;J.superclass.bindUI.apply(this,arguments);A._bindToolbarUI();A.on("playingChange",A._onPlayingChange);A.on("pausedChange",A._onPausedChange);A.publish("changeRequest",{defaultFn:this._changeRequest});},destroy:function(){var A=this;J.superclass.destroy.apply(this,arguments);A.get(H).destroy();},hidePaginator:function(){var A=this;A.get(F).addClass(f);},pause:function(){var A=this;A.set(D,true);A.set(B,false);A._syncInfoUI();},play:function(){var A=this;A.set(D,false);A.set(B,true);A._syncInfoUI();},show:function(){var A=this;var am=A.getCurrentLink();if(am){A.showMask();r.ImageViewer.superclass.show.apply(this,arguments);var L=A.get(H);L.set(v,A.get(e)+1);L.changeRequest();}},showPaginator:function(){var A=this;A.get(F).removeClass(f);
},_bindToolbarUI:function(){var A=this;if(A.get(s)){var L=A.toolbar;var an=L.item(aa);var am=L.item(i);if(an){an.set(g,r.bind(A.play,A));}if(am){am.set(g,r.bind(A.pause,A));}}},_cancelTimer:function(){var A=this;if(A._timer){A._timer.cancel();}},_renderPaginator:function(){var A=this;var am=A.get(F);r.one(V).append(am.hide());var L=new r.Paginator(A.get(aj)).render();A.set(H,L);},_renderPlayer:function(){var A=this;var am=A.get(F);var L=r.Node.create(ad);am.append(r.Node.create(Z).append(L));A.toolbar=new r.Toolbar(A.get(W)).render(L);},_startTimer:function(){var A=this;var L=A.get(Q);A._cancelTimer();A._timer=r.later(L,A,A._syncSlideShow);},_syncControlsUI:function(){var A=this;J.superclass._syncControlsUI.apply(this,arguments);if(A.get(af)){A._syncSelectedThumbUI();A.showPaginator();}else{A.hidePaginator();A._cancelTimer();}},_syncSelectedThumbUI:function(){var A=this;var am=A.get(e);var L=A.get(H);var an=L.get(v)-1;if(am!=an){L.set(v,am+1);L.changeRequest();}},_syncSlideShow:function(){var A=this;if(!A.hasNext()){if(A.get(l)){A.set(e,-1);}else{A._cancelTimer();}}A.next();},_changeRequest:function(ao){var A=this;var L=ao.state.paginator;var aq=ao.state;var an=aq.before;var ap=aq.page;if(!A.get(af)){return false;}var am=ap-1;if(!an||(an&&an.page!=ap)){A.set(e,am);L.setState(aq);A._processChangeRequest();}},_processChangeRequest:function(){var A=this;A.loadImage(A.getCurrentLink().attr(T));var am=A.get(D);var L=A.get(B);if(L&&!am){A._startTimer();}},_setThumbContent:function(ar,L){var A=this;var an=L-1;var ap=A.getLink(an);var aq=ar.one(w+ae);var ao=null;if(A.get(u)){ao=ap.attr(T);}else{var am=ap.one(G);if(am){ao=am.attr(ac);}}if(ao&&aq.getData("thumbSrc")!=ao){aq.setStyles({backgroundImage:"url("+ao+")"});aq.setData("thumbSrc",ao);}},_getInfoTemplate:function(L){var am;var A=this;var ao=A.get(D);var an=A.get(B);if(an){am=A.get(n);}else{if(ao){am=A.get(b);}}return C(J.superclass._getInfoTemplate.apply(this,arguments),am);},_afterVisibleChange:function(L){var A=this;J.superclass._afterVisibleChange.apply(this,arguments);if(L.newVal){if(A.get(o)){A.play();}}},_onPausedChange:function(L){var A=this;if(L.newVal){A._cancelTimer();}},_onPlayingChange:function(L){var A=this;if(L.newVal){A._startTimer();}}}});r.ImageGallery=J;},"1.5.2",{skinnable:true,requires:["aui-image-viewer-base","aui-paginator","aui-toolbar"]});AUI.add("aui-media-viewer-plugin",function(j){var e=j.Lang,m=j.Do,h=j.UA.ie,o="about:blank",p="body",c="href",i="iframe",n="image",l="loading",q="providers",r="src",f="mediaViewerPlugin",a="data-options",d={height:360,width:640,wmode:"embed"},g="https?://(?:www\\.)?{domain}",b="(?:[\\?&]|^){param}=([^&#]*)";var k=j.Component.create({NAME:f,NS:"media",ATTRS:{providers:{validator:e.isObject,value:{"flash":{container:'<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="{width}" height="{height}"><param name="wmode" value="{wmode}" /><param name="allowfullscreen" value="true" /><param name="allowscriptaccess" value="always" /><param name="movie" value="{media}" /><embed src="{media}" type="application/x-shockwave-flash" allowfullscreen="true" allowscriptaccess="always" width="{width}" height="{height}" wmode="{wmode}"></embed></object>',matcher:/\b.swf\b/i,options:d,mediaRegex:/([^?&#]+)/},"youtube":{container:'<iframe width="{width}" height="{height}" src="http://www.youtube.com/embed/{media}" frameborder="0" allowfullscreen></iframe>',matcher:new RegExp(e.sub(g,{domain:"youtube.com"}),"i"),options:d,mediaRegex:/[\?&]v=([^&#]*)/i},"vimeo":{container:'<iframe src="http://player.vimeo.com/video/{media}?title=0&amp;byline=0&amp;portrait=0&amp;color=ffffff" width="{width}" height="{height}" frameborder="0"></iframe>',matcher:new RegExp(e.sub(g,{domain:"vimeo.com"}),"i"),options:d,mediaRegex:/\/(\d+)/}}}},EXTENDS:j.Plugin.Base,prototype:{initializer:function(t){var s=this;var u=s._handles;u.changeReqeust=s.afterHostMethod("_changeRequest",s._restoreMedia);u.close=s.beforeHostMethod("close",s.close);u.loadMedia=s.beforeHostMethod("loadImage",s.loadMedia);u.preloadImage=s.beforeHostMethod("preloadImage",s.preloadImage);},close:function(){var s=this;var u=s.get("host");var v=u.getCurrentLink();var t=s._getMediaType(v.attr("href"));if(t!=n){s._redirectIframe(o);u.setStdModContent(p,"");}},loadMedia:function(v){var y=this;var z=y.get("host");var A=y._getMediaType(v);var C=true;y._redirectIframe(o);if(A!=n){var w=y.get(q)[A];var s=z.getCurrentLink();var B=y._updateOptions(s,j.clone(w.options));var u=w.mediaRegex.exec(v);if(u){B.media=u[1];}var t=e.sub(w.container,B);z.setStdModContent(p,t);z._syncImageViewerUI();y._uiSetContainerSize(B.width,B.height);z._setAlignCenter(true);z.set(l,false);z.fire("load",{media:u});if(z.get("preloadNeighborImages")){var x=z.get("currentIndex");z.preloadImage(x+1);z.preloadImage(x-1);}C=new m.Prevent();}return C;},preloadImage:function(v){var t=this;var x=t.get("host");var w=x.getLink(v);var s=new m.Prevent();if(w){var y=w.attr(c);var u=t._getMediaType(y);if(u==n){s=true;}}return s;},_getMediaType:function(v){var s=this;var u=s.get(q);var t=n;j.some(u,function(x,w,y){return x.matcher.test(v)&&(t=w);});return t;},_redirectIframe:function(v){var s=this;var u=s.get("host.bodyNode");if(u){var t=u.one(i);if(t){t.attr(r,v);}}},_restoreMedia:function(w){var s=this;var v=s.get("host");var x=v.getCurrentLink();var u=x.attr("href");var t=s._getMediaType(u);if(t!=n&&!v.getStdModNode(p).html()){v._processChangeRequest();}},_uiSetContainerSize:function(v,t){var s=this;var w=s.get("host");var u=w.bodyNode;u.setStyles({height:t,width:v});},_updateOptions:function(t,s){var v=t.attr(a);var u=t.attr(c);j.each(s,function(z,y,A){var x=new RegExp(e.sub(b,{param:y}));var w=x.exec(v)||x.exec(u);if(w){s[y]=w[1];}});return s;},_handles:{}},DATA_OPTIONS:a,DEFAULT_OPTIONS:d,REGEX_DOMAIN:g,REGEX_PARAM:b});j.MediaViewerPlugin=k;j.MediaViewer=j.ImageViewer;},"1.5.2",{requires:["aui-image-viewer-base"],skinnable:false});AUI.add("aui-image-viewer",function(a){},"1.5.2",{skinnable:true,use:["aui-image-viewer-base","aui-image-viewer-gallery","aui-media-viewer-plugin"]});