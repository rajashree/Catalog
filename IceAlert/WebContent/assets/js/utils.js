function debug(data) {
	//log.debug(data);
}
function info(data) {
	//log.info(data);
}


/*  Stripped Down version of Prototype JavaScript framework, version 1.4.0
/*--------------------------------------------------------------------------*/

var IE = document.all?true:false;
if (!IE) document.captureEvents(Event.MOUSEMOVE)

var Class = {
  create: function() {
    return function() {
      this.initialize.apply(this, arguments);
    }
  }
}

var Abstract = new Object();

Object.extend = function(destination, source) {
  for (property in source) {
    destination[property] = source[property];
  }
  return destination;
}

Function.prototype.bind = function() {
  var __method = this, args = $A(arguments), object = args.shift();
  return function() {
    return __method.apply(object, args.concat($A(arguments)));
  }
}

Function.prototype.bindAsEventListener = function(object) {
  var __method = this;
  return function(event) {
    return __method.call(object, event || window.event);
  }
}
/*--------------------------------------------------------------------------*/
function $() {
  var elements = new Array();
  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    if (typeof element == 'string')
      element = document.getElementById(element);
    if (arguments.length == 1)
      return element;
    elements.push(element);
  }
  return elements;
}
var byId = $;

Object.extend(String.prototype, {
  toArray: function() {
    return this.split('');
  },
 isEmail: function(){
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(this))
		{return (true)};return (false)
	  }
});

var $A = Array.from = function(iterable) {
  if (!iterable) return [];
  if (iterable.toArray) {
    return iterable.toArray();
  } else {
    var results = [];
    for (var i = 0; i < iterable.length; i++)
      results.push(iterable[i]);
    return results;
  }
}

Array.prototype._reverse = Array.prototype.reverse;

Object.extend(Array.prototype, {
  _each: function(iterator) {
    for (var i = 0; i < this.length; i++)
      iterator(this[i]);
  },

  clear: function() {
    this.length = 0;
    return this;
  },

  first: function() {
    return this[0];
  },

  last: function() {
    return this[this.length - 1];
  },

  compact: function() {
    return this.select(function(value) {
      return value != undefined || value != null;
    });
  },

  flatten: function() {
    return this.inject([], function(array, value) {
      return array.concat(value.constructor == Array ?
        value.flatten() : [value]);
    });
  },

  without: function() {
    var values = $A(arguments);
    return this.select(function(value) {
      return !values.include(value);
    });
  },

  indexOf: function(object) {
    for (var i = 0; i < this.length; i++)
      if (this[i] == object) return i;
    return -1;
  },

  reverse: function(inline) {
    return (inline !== false ? this : this.toArray())._reverse();
  },

  shift: function() {
    var result = this[0];
    for (var i = 0; i < this.length - 1; i++)
      this[i] = this[i + 1];
    this.length--;
    return result;
  }
});

document.getElementsByClassName = function(className, parentElement) {
  var children = ($(parentElement) || document.body).getElementsByTagName('*');
  return $A(children).inject([], function(elements, child) {
    if (child.className.match(new RegExp("(^|\\s)" + className + "(\\s|$)")))
      elements.push(child);
    return elements;
  });
}

/*--------------------------------------------------------------------------*/
if (!window.Element) {
  var Element = new Object();
}

Object.extend(Element, {
  visible: function(e) {
    	try{return $(e).style.display != 'none';}catch(e){}
  },
  toggle: function(e) {
	  if(Element.visible(e)){Element.hide(e); }else{Element.hide(e);}
  },
  hide: function(e) {
     try{$(e).style.display = 'none';}catch(e){}
  },
  show: function(e) {
      try{$(e).style.display = '';}catch(e){}
  },
  remove: function(e) {
    element = $(e);
    try{
		element.parentNode.removeChild(element);
	}catch(e){}
  },
	create: function(type,id,classId,content){
		div=document.createElement(type);
		if(classId!="" && classId!=undefined ){div.className=classId;};
		if(id!="" && id!=undefined){div.id=id};
		if(content!="" && content!=undefined){div.innerHTML=content};
		return div;
	}
	,
	insert : function (child_id,data){
		var newdiv = document.createElement('div');
		newdiv.setAttribute('id',child_id);
		newdiv.innerHTML = data;
		this.appendChild(newdiv);
		return newdiv;
	},
	clear : function (e){
		$(e).innerHTML = " ";
	}
});




function getEvent(e){
	if (IE){
		var e = new Object();
		e =  window.event;
		e.target = e.srcElement;
	}
	return e;
}


function trimString(str){
while(str.substr(0,1)==" "){str=str.substr(1);}
while(str.substr(str.length-1,str.length)==" "){str=str.substr(0,str.length-1);}
return str;
}
Array.prototype.contains = function(value){

	for(i=0;i<this.length;i++){
		if(this[i]==value){
			return true;
		}
	}
	return false;
}

