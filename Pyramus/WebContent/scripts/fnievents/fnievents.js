var fni;if(!fni){fni={}}if(!fni.events){fni.events={}}fni.events.FNIEventSupport={addListener:function(b){var d=$A(arguments);var c=null;var e=null;if(d.length==2){c=this;e=d[1]}else{if(d.length==3){c=d[1];e=d[2]}}var a=new Object();a.eventName=b;a.method=e;a.object=c;if(!this._listeners){this._listeners=new Array()}this._listeners.push(a)},removeListener:function(a,b){if(this._listeners){for(var c=0;c<this._listeners.length;c++){var d=this._listeners[c];if((d.eventName==a)&&(d.object==b)){this._listeners.splice(c,1)}}}},fire:function(b,d){var a=true;var h=Object.extend(d||{},{stop:function(){this.stopped=true},stopped:false});if(this._listeners){var g=this._listeners.length;for(var c=0;(c<g)&&a;c++){if(this._listeners!=null){var f=this._listeners[c];if(f&&(f!=null)){if(f.eventName==b){f.method.call(f.object,h);if(h.stopped==true){a=false}}}}}}return a}};