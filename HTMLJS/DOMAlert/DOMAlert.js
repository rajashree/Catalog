/*jslint browser: true */

//Copyright (c) 2008 Lewis Linn White Jr.
//Author: Lewis Linn White Jr.

//Permission is hereby granted, free of charge, to any person
//obtaining a copy of this software and associated documentation
//files (the "Software"), to deal in the Software without
//restriction, including without limitation the rights to use,
//copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the
//Software is furnished to do so, subject to the following
//conditions:

//The above copyright notice and this permission notice shall be
//included in all copies or substantial portions of the Software.
//Except as contained in this notice, the name(s) of the above 
//copyright holders shall not be used in advertising or otherwise 
//to promote the sale, use or other dealings in this Software without 
//prior written authorization.

//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//OTHER DEALINGS IN THE SOFTWARE.

//Icons used in this project are graciously provided by the Silk icons set:
//http://www.famfamfam.com/lab/icons/silk/

function DOMAlert(settings)
{
	var that, modalWindow, iframe, alertWindow, titleBar, title, ricon, licon, contentArea, buttonArea, okButton, cancelButton, defaultCallback, okCallback, cancelCallback;
	
	//create version of ourself for use in closures
	that = this;
	
	//Create our settings
	this.settings = settings;
	
	//Create a namespae object to hold our html elements
	this.html = {};
	
	//ie6 test.  what a crappy browser
	this.isIE6 = (document.all && window.external && (typeof document.documentElement.style.maxHeight === 'undefined')) ? true : false;	
	
	// use the Default skin if none was provided
	this.settings.skin = this.settings.skin ? this.settings.skin : 'default';
	
	// Set up a default for OK setting
	if (!this.settings.ok)
	{
		defaultCallback = function ()
		{
			that.close();
		};
		this.settings.ok = {text: 'Ok', value: true, onclick: defaultCallback};
	}
	
	//Create our modal background
	modalWindow = document.createElement('div');
	modalWindow.style.height = ((document.documentElement.clientHeight > document.documentElement.scrollHeight) ? document.documentElement.clientHeight : document.documentElement.scrollHeight) + 'px';
	modalWindow.style.width = document.documentElement.scrollWidth + 'px';
	if (!this.isIE6)
	{
		modalWindow.style.background = 'url(tp2.png)';  //transparent png with low opacity.  Provides a similar effect as opacy/filter settings, but without the memory leaks
	}
	modalWindow.style.position = 'absolute';
	modalWindow.style.left = '0px';
	modalWindow.style.top = '0px';
	modalWindow.style.zIndex = 998;
	modalWindow.style.visibility = 'hidden';
	document.body.appendChild(modalWindow);
	this.html.modalWindow = modalWindow;
	
	//shoehorn a iframe to cover our select elemtns for ie6.  what a crappy browser....
	if (this.isIE6)
	{
		iframe = document.createElement('iframe');
		iframe.style.position = 'absolute';
		iframe.style.visibility = 'hidden';
		iframe.style.zIndex = 997;
		iframe.frameBorder = 0;
		iframe.style.position = 'absolute';
		document.body.appendChild(iframe);
		this.html.iframe = iframe;
		
		//also, need to add an alpha image loader for ie6 transparency affect.  again, style.filter has a huge memory leak
		modalWindow.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/tp2.png', sizingMethod='scale', enabled=true)";
	}
	
	//Create our alert window
	alertWindow = document.createElement('div');
	alertWindow.className = this.settings.skin + '_alertWindow';
	alertWindow.style.position = this.isIE6 ? 'absolute' : 'fixed';
	alertWindow.style.zIndex = 999;
	if (this.settings.width)
	{
		alertWindow.style.width = this.settings.width + 'px';
	}
	document.body.appendChild(alertWindow);
	alertWindow.style.visibility = 'hidden';
	this.html.alertWindow = alertWindow;
	
	//Create out title bar
	titleBar = document.createElement('div');
	titleBar.className = this.settings.skin + '_titleBar';
	alertWindow.appendChild(titleBar);
	this.html.titleBar = titleBar;
	
	//Create our right Icon
	ricon = document.createElement('div');
	ricon.className = this.settings.skin + '_titleBarRightIcon';
	ricon.style.cssFloat = 'right';
	ricon.style.styleFloat = 'right';
	titleBar.appendChild(ricon);
	this.html.ricon = ricon;
	
	//Create our Left Icon
	licon = document.createElement('div');
	licon.className = this.settings.skin + '_titleBarLeftIcon';
	licon.style.cssFloat = 'left';
	licon.style.styleFloat = 'left';
	titleBar.appendChild(licon);
	this.html.licon = licon;
	
	//Create our span that goes in our title
	title = document.createElement('span');
	title.innerHTML = this.settings.title;
	titleBar.appendChild(title);
	this.html.title = title;
	
	//Create our main content area
	contentArea = document.createElement('div');
	contentArea.className = this.settings.skin + '_contentArea';
	contentArea.innerHTML = this.settings.text;
	if (this.settings.height)
	{
		contentArea.style.height = this.settings.height + 'px';
	}
	alertWindow.appendChild(contentArea);
	this.html.contentArea = contentArea;
	
	//Create out button area
	buttonArea = document.createElement('div');
	buttonArea.className = this.settings.skin + '_buttonArea';
	alertWindow.appendChild(buttonArea);
	this.html.buttonArea = buttonArea;
	
	//Draw an OK button
	okButton = document.createElement('input');
	okButton.type = 'button';
	okButton.className = this.settings.skin + '_okButton';
	okButton.value = this.settings.ok.text;
	okCallback = function ()
	{
		that.settings.ok.onclick(that, that.settings.ok.value);
	};
	okButton.onclick = okCallback;
	buttonArea.appendChild(okButton);
	this.html.okButton = okButton;
	
	//Draw a cancel button, if present
	if (this.settings.cancel)
	{
		cancelButton = document.createElement('input');
		cancelButton.type = 'button';
		cancelButton.className = this.settings.skin + '_cancelButton';
		cancelButton.value = this.settings.cancel.text || 'Cancel';
		cancelCallback = function ()
		{
			that.settings.cancel.onclick(that, that.settings.cancel.value);
		};
		cancelButton.onclick = cancelCallback;
		buttonArea.appendChild(cancelButton);
		this.html.cancelButton = cancelButton;
	}
	
	//Center our alert box on the screen
	this.center();

}
DOMAlert.prototype.show = function (titleText, contentText)
{
	if (contentText)
	{
		this.html.title.innerHTML = titleText;
		this.html.contentArea.innerHTML = contentText;
	}
	if (titleText && !contentText)
	{
		this.html.contentArea.innerHTML = titleText;
	}
	
	this.html.modalWindow.style.visibility = 'visible';
	this.html.alertWindow.style.visibility = 'visible';
	if (this.html.iframe)
	{
		this.html.iframe.style.height = this.html.alertWindow.offsetHeight;
		this.html.iframe.style.width = this.html.alertWindow.offsetWidth;
		this.html.iframe.style.visibility = 'visible';
	}
	if (this.html.cancelButton)
	{
		this.html.cancelButton.focus();
	}
	else
	{
		this.html.okButton.focus();
	}	
};
DOMAlert.prototype.hide = function ()
{
	this.html.modalWindow.style.visibility = 'hidden';
	this.html.alertWindow.style.visibility = 'hidden';
	if (this.html.iframe)
	{
		this.html.iframe.style.visibility = 'hidden';
	}
};
DOMAlert.prototype.close = function ()
{
	var obj, prop;
	
	//make sure our DOM objects are deleted and our onclick statements are nulled
	for (obj in this.html)
	{
		if (this.html[obj].parentNode)
		{
			if (this.html[obj].onclick)
			{
				this.html[obj].onclick = null;
			}
			this.html[obj].parentNode.removeChild(this.html[obj]);
			delete this.html[obj];
		}
	}
		
	//remove object properties
	for (prop in this)
	{
		if (this[prop])
		{
			this[prop] = null;
			delete this[prop];
		}
	}
};
DOMAlert.prototype.center = function ()
{
	var alertWindow, scrollT, scrollL, iframe;
	alertWindow = this.html.alertWindow;
	if (alertWindow.style.position === 'absolute')
	{
		scrollT = window.pageYOffset || document.documentElement.scrollTop;
		scrollL = window.pageXOffset || document.documentElement.scrollLeft;
		alertWindow.style.left = (self.innerWidth || (document.documentElement.clientWidth || document.body.clientWidth)) / 2 + scrollL - alertWindow.offsetWidth / 2 + 'px';
		alertWindow.style.top = (self.innerHeight || (document.documentElement.clientHeight || document.body.clientHeight)) / 2 + scrollT - alertWindow.offsetHeight / 2 + 'px';
		if (this.html.iframe)
		{
			this.html.iframe.style.left = alertWindow.style.left;
			this.html.iframe.style.top = alertWindow.style.top;
		}
	}
	else
	{
		alertWindow.style.left = (self.innerWidth || (document.documentElement.clientWidth || document.body.clientWidth)) / 2 - alertWindow.offsetWidth / 2 + 'px';
		alertWindow.style.top = (self.innerHeight || (document.documentElement.clientHeight || document.body.clientHeight)) / 2 - alertWindow.offsetHeight / 2 + 'px';
	}
};
