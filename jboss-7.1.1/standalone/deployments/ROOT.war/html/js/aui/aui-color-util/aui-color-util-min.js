AUI.add("aui-color-util",function(d){var h=d.Lang,i=h.isArray,p=h.isObject,c=h.isString,o=function(r){return r&&(r.slice(-3)=="deg"||r.slice(-1)=="\xb0");},l=function(r){return r&&r.slice(-1)=="%";},m={hs:1,rg:1},n=Math,e=n.max,b=n.min,k=/\s*,\s*/,q=/^\s*((#[a-f\d]{6})|(#[a-f\d]{3})|rgba?\(\s*([\d\.]+%?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\)|hsba?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\)|hsla?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\))\s*$/i,g=/^(?=[\da-f]$)/,a=/^\s+|\s+$/g,j="";var f={constrainTo:function(u,v,s,t){var r=this;if(u<v||u>s){u=t;}return u;},getRGB:d.cached(function(u){if(!u||!!((u=String(u)).indexOf("-")+1)){return new f.RGB("error");}if(u=="none"){return new f.RGB();}if(!m.hasOwnProperty(u.toLowerCase().substring(0,2))&&u.charAt(0)!="#"){u=f._toHex(u);}var z;var y;var r;var w;var x;var v=u.match(q);var s;if(v){if(v[2]){r=parseInt(v[2].substring(5),16);y=parseInt(v[2].substring(3,5),16);z=parseInt(v[2].substring(1,3),16);}if(v[3]){r=parseInt((x=v[3].charAt(3))+x,16);y=parseInt((x=v[3].charAt(2))+x,16);z=parseInt((x=v[3].charAt(1))+x,16);}if(v[4]){s=v[4].split(k);z=parseFloat(s[0]);if(l(s[0])){z*=2.55;}y=parseFloat(s[1]);if(l(s[1])){y*=2.55;}r=parseFloat(s[2]);if(l(s[2])){r*=2.55;}if(v[1].toLowerCase().slice(0,4)=="rgba"){w=parseFloat(s[3]);}if(l(s[3])){w/=100;}}if(v[5]){s=v[5].split(k);z=parseFloat(s[0]);if(l(s[0])){z*=2.55;}y=parseFloat(s[1]);if(l(s[1])){y*=2.55;}r=parseFloat(s[2]);if(l(s[2])){r*=2.55;}if(o(s[0])){z/=360;}if(v[1].toLowerCase().slice(0,4)=="hsba"){w=parseFloat(s[3]);}if(l(s[3])){w/=100;}return f.hsb2rgb(z,y,r,w);}if(v[6]){s=v[6].split(k);z=parseFloat(s[0]);if(l(s[0])){z*=2.55;}y=parseFloat(s[1]);if(l(s[1])){y*=2.55;}r=parseFloat(s[2]);if(l(s[2])){r*=2.55;}if(o(s[0])){z/=360;}if(v[1].toLowerCase().slice(0,4)=="hsla"){w=parseFloat(s[3]);}if(l(s[3])){w/=100;}return f.hsb2rgb(z,y,r,w);}v=new f.RGB(z,y,r,w);return v;}return new f.RGB("error");}),hex2rgb:function(s){var r=this;s=String(s).split("#");s.unshift("#");return r.getRGB(s.join(""));},hsb2rgb:function(){var r=this;var s=r._getColorArgs("hsbo",arguments);s[2]/=2;return r.hsl2rgb.apply(r,s);},hsv2rgb:function(){var F=this;var A=F._getColorArgs("hsv",arguments);var z=F.constrainTo(A[0],0,1,0);var H=F.constrainTo(A[1],0,1,0);var E=F.constrainTo(A[2],0,1,0);var u;var B;var D;var y=Math.floor(z*6);var C=z*6-y;var x=E*(1-H);var w=E*(1-C*H);var G=E*(1-(1-C)*H);switch(y%6){case 0:u=E;B=G;D=x;break;case 1:u=w;B=E;D=x;break;case 2:u=x;B=E;D=G;break;case 3:u=x;B=w;D=E;break;case 4:u=G;B=x;D=E;break;case 5:u=E;B=x;D=w;break;}return new f.RGB(u*255,B*255,D*255);},hsl2rgb:function(){var C=this;var D=C._getColorArgs("hslo",arguments);var z=D[0];var E=Math.max(Math.min(D[1],1),0);var y=Math.max(Math.min(D[2],1),0);var x=D[3];var t,A,B;if(E==0){t=A=B=y;}else{var w=C._hue2rgb;var u=y<0.5?y*(1+E):y+E-y*E;var v=2*y-u;t=w(v,u,z+1/3);A=w(v,u,z);B=w(v,u,z-1/3);}return new f.RGB(t*255,A*255,B*255,x);},rgb2hex:function(z,y,u){var t=this;var v=t._getColorArgs("rgb",arguments);var x=v[0];var w=v[1];var s=v[2];return(16777216|s|(w<<8)|(x<<16)).toString(16).slice(1);},rgb2hsb:function(){var r=this;var s=r.rgb2hsv.apply(r,arguments);s.b=s.v;return s;},rgb2hsl:function(){var C=this;var z=C._getColorArgs("rgb",arguments);var t=z[0]/255;var x=z[1]/255;var A=z[2]/255;var B=Math.max(t,x,A);var v=Math.min(t,x,A);var w;var D;var u=(B+v)/2;if(B==v){w=D=0;}else{var y=B-v;D=u>0.5?y/(2-B-v):y/(B+v);switch(B){case t:w=(x-A)/y+(x<A?6:0);break;case x:w=(A-t)/y+2;break;case A:w=(t-x)/y+4;break;}w/=6;}return{h:w,s:D,l:u,toString:f._hsltoString};},rgb2hsv:function(){var D=this;var z=D._getColorArgs("rgb",arguments);var t=z[0]/255;var x=z[1]/255;var A=z[2]/255;var B=Math.max(t,x,A);var u=Math.min(t,x,A);var w;var E;var C=B;var y=B-u;E=B==0?0:y/B;if(B==u){w=0;}else{switch(B){case t:w=(x-A)/y+(x<A?6:0);break;case x:w=(A-t)/y+2;break;case A:w=(t-x)/y+4;break;}w/=6;}return{h:w,s:E,v:C,toString:f._hsbtoString};},_getColorArgs:function(w,t){var s=this;var v=[];var r=t[0];if(i(r)&&r.length){v=r;}else{if(p(r)){var y=w.split("");var x=y.length;for(var u=0;u<x;u++){v[u]=r[y[u]];}}else{v=d.Array(t);}}return v;},_hsbtoString:function(){var r=this;return["hs",(("v" in r)?"v":"b"),"(",r.h,r.s,r.b,")"].join("");},_hsltoString:function(){var r=this;return["hsl(",r.h,r.s,r.l,")"].join("");},_hue2rgb:function(u,s,r){if(r<0){r+=1;}if(r>1){r-=1;}if(r<1/6){return u+(s-u)*6*r;}if(r<1/2){return s;}if(r<2/3){return u+(s-u)*(2/3-r)*6;}return u;},_toHex:function(s){var r=this;if(d.UA.ie){r._toHex=d.cached(function(v){var x;var w=d.config.win;try{var A=new w.ActiveXObject("htmlfile");A.write("<body>");A.close();x=A.body;}catch(z){x=w.createPopup().document.body;}var u=x.createTextRange();try{x.style.color=String(v).replace(a,j);var y=u.queryCommandValue("ForeColor");y=((y&255)<<16)|(y&65280)|((y&16711680)>>>16);return"#"+("000000"+y.toString(16)).slice(-6);}catch(z){return"none";}});}else{var t=d.config.doc.createElement("i");t.title="AlloyUI Color Picker";t.style.display="none";d.getBody().append(t);r._toHex=d.cached(function(u){t.style.color=u;return d.config.doc.defaultView.getComputedStyle(t,j).getPropertyValue("color");});}return r._toHex(s);}};f.RGB=function(v,u,t,w){var s=this;if(v=="error"){s.error=1;}else{if(arguments.length){s.r=~~v;s.g=~~u;s.b=~~t;s.hex="#"+f.rgb2hex(s);if(isFinite(parseFloat(w))){s.o=w;}}}};f.RGB.prototype={r:-1,g:-1,b:-1,hex:"none",toString:function(){var r=this;return r.hex;}};d.ColorUtil=f;},"1.5.2",{skinnable:false});