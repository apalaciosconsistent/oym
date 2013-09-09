AUI.add("aui-parse-content",function(m){var g=m.Lang,k=g.isString,b=m.config.doc,o="append",p="documentElement",a="firstChild",l="head",q="host",n="innerHTML",f="<div>_</div>",r="ParseContent",e="queue",i="script",c=";",h="src",j={"":1,"text/javascript":1};var d=m.Component.create({NAME:r,NS:r,ATTRS:{queue:{value:null}},EXTENDS:m.Plugin.Base,prototype:{initializer:function(){var s=this;d.superclass.initializer.apply(this,arguments);s.set(e,new m.AsyncQueue());s._bindAOP();},globalEval:function(u){var v=m.getDoc();var t=v.one(l)||v.get(p);var s=b.createElement(i);s.type="text/javascript";if(u){s.text=g.trim(u);}t.appendChild(s).remove();},parseContent:function(u){var s=this;var t=s._clean(u);s._dispatch(t);return t;},_addInlineScript:function(t){var s=this;s.get(e).add({args:t,context:s,fn:s.globalEval,timeout:0});},_bindAOP:function(){var t=this;var s=function(x){var w=Array.prototype.slice.call(arguments);var v=t.parseContent(x);w.splice(0,1,v.fragment);return new m.Do.AlterArgs(null,w);};this.doBefore("insert",s);this.doBefore("replaceChild",s);var u=function(w){var v=t.parseContent(w);return new m.Do.AlterArgs(null,[v.fragment]);};this.doBefore("replace",u);this.doBefore("setContent",u);},_clean:function(u){var s={};var t=m.Node.create("<div></div>");if(k(u)){u=f+u;m.DOM.addHTML(t,u,o);}else{t.append(f);t.append(u);}s.js=t.all(i).filter(function(v){return j[v.getAttribute("type").toLowerCase()];});s.js.each(function(w,v){w.remove();});t.get(a).remove();s.fragment=t.get("childNodes").toFrag();return s;},_dispatch:function(v){var t=this;var s=t.get(e);var u=[];v.js.each(function(x,w){var z=x.get(h);if(z){if(u.length){t._addInlineScript(u.join(c));u.length=0;}s.add({autoContinue:false,fn:function(){m.Get.script(z,{onEnd:function(A){A.purge();s.run();}});},timeout:0});}else{var y=x._node;u.push(y.text||y.textContent||y.innerHTML||"");}});if(u.length){t._addInlineScript(u.join(c));}s.run();}}});m.namespace("Plugin").ParseContent=d;},"1.5.2",{requires:["async-queue","aui-base","plugin"],skinnable:false});