AUI.add("aui-overlay-manager",function(c){var i=c.Lang,j=i.isArray,b=i.isBoolean,n=i.isNumber,a=i.isString,l="boundingBox",f="default",m="host",h="OverlayManager",k="group",d="zIndex",g="zIndexBase";var e=c.Component.create({NAME:h.toLowerCase(),ATTRS:{zIndexBase:{value:1000,validator:n,setter:i.toInt}},EXTENDS:c.Base,prototype:{initializer:function(){var o=this;o._overlays=[];},bringToTop:function(p){var o=this;var r=o._overlays.sort(o.sortByZIndexDesc);var t=r[0];if(t!==p){var s=p.get(d);var q=t.get(d);p.set(d,q+1);p.set("focused",true);}},destructor:function(){var o=this;o._overlays=[];},register:function(s){var p=this;var t=p._overlays;if(j(s)){c.Array.each(s,function(w){p.register(w);});}else{var r=p.get(g);var v=p._registered(s);if(!v&&s&&((s instanceof c.Overlay)||(c.Component&&s instanceof c.Component))){var q=s.get(l);t.push(s);var u=s.get(d)||0;var o=t.length+u+r;s.set(d,o);s.on("focusedChange",p._onFocusedChange,p);q.on("mousedown",p._onMouseDown,p);}}return t;},remove:function(p){var o=this;var q=o._overlays;if(q.length){return c.Array.removeItem(q,p);}return null;},each:function(q){var o=this;var p=o._overlays;c.Array.each(p,q);},showAll:function(){this.each(function(o){o.show();});},hideAll:function(){this.each(function(o){o.hide();});},sortByZIndexDesc:function(p,o){if(!p||!o||!p.hasImpl(c.WidgetStack)||!o.hasImpl(c.WidgetStack)){return 0;}else{var q=p.get(d);var r=o.get(d);if(q>r){return -1;}else{if(q<r){return 1;}else{return 0;}}}},_registered:function(p){var o=this;return c.Array.indexOf(o._overlays,p)!=-1;},_onMouseDown:function(q){var o=this;var p=c.Widget.getByNode(q.currentTarget||q.target);var r=o._registered(p);if(p&&r){o.bringToTop(p);}},_onFocusedChange:function(q){var o=this;if(q.newVal){var p=q.currentTarget||q.target;var r=o._registered(p);if(p&&r){o.bringToTop(p);}}}}});c.OverlayManager=e;},"1.5.2",{requires:["aui-base","aui-overlay-base","overlay","plugin"]});