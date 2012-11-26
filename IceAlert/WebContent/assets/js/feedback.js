function $(id){
	return document.getElementById(id);
}
// JavaScript Document
feedback = {
	show: function (info,type,time)
	{
		if(info!=undefined )
		{
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("feedback-content");
			if(div)
			{
				div.style.display="block";
				div.innerHTML=info;
				if(type==0)
				{
					div.className="feedback-green_onlyforie6";
				}
				else
				{
					div.className="feedback-red_onlyforie6";
				}
				setTimeout("feedback.clear()",time);
			}		
		}
		else
		 if (info!=undefined )
		 {
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("feedback-content");
			if(div)
			{
				div.style.display="block";
				div.innerHTML=info;
				if(type==0)
				{
					div.className="feedback-green_onlyforie7";
				}
				else
				{
					div.className="feedback-red_onlyforie7";
				}
				setTimeout("feedback.clear()",time);
			}		
		 }
		 else
		  if(info!=undefined)
		  {
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("feedback-content");
			div.innerHTML = "";
			var divchild = document.createElement('div');
			div.appendChild(divchild);
			if(div)
			{
				div.style.display="block";
				divchild.innerHTML = info;
				if(type==0)
				{
					div.className="feedback-green";
					divchild.className="feedback-green_child";
				}
				else
				{
					div.className="feedback-red";
					divchild.className="feedback-red_child";
				}
				setTimeout("feedback.clear()",time);
			}
		}
	},
	clear:function (){
		$("feedback-content").style.display="none";
	}
}

medfeedback = {
	show: function (info,type,time)
	{
		if(info!=undefined )
		{
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("med_feedback_content");
			if(div)
			{
				div.style.display="block";
				div.innerHTML=info;
				if(type==0)
				{
					div.className="feedback-green_onlyforie6";
				}
				else
				{
					div.className="feedback-red_onlyforie6";
				}
				setTimeout("medfeedback.clear()",time);
			}		
		}
		else
		 if (info!=undefined )
		 {
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("med_feedback_content");
			if(div)
			{
				div.style.display="block";
				div.innerHTML=info;
				if(type==0)
				{
					div.className="feedback-green_onlyforie7";
				}
				else
				{
					div.className="feedback-red_onlyforie7";
				}
				setTimeout("medfeedback.clear()",time);
			}		
		 }
		 else
		  if(info!=undefined)
		  {
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("med_feedback_content");
			div.innerHTML = "";
			var divchild = document.createElement('div');
			div.appendChild(divchild);
			if(div)
			{
				div.style.display="block";
				divchild.innerHTML = info;
				if(type==0)
				{
					div.className="feedback-green";
					divchild.className="feedback-green_child";
				}
				else
				{
					div.className="feedback-red";
					divchild.className="feedback-red_child";
				}
				setTimeout("medfeedback.clear()",time);
			}
		}
	},
	clear:function (){
		$("med_feedback_content").style.display="none";
	}
}

passfeedback = {
	show: function (info,type,time)
	{
		if(info!=undefined )
		{
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("pass_feedback_content");
			if(div)
			{
				div.style.display="block";
				div.innerHTML=info;
				if(type==0)
				{
					div.className="feedback-green_onlyforie6";
				}
				else
				{
					div.className="feedback-red_onlyforie6";
				}
				setTimeout("passfeedback.clear()",time);
			}		
		}
		else
		 if (info!=undefined )
		 {
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("pass_feedback_content");
			if(div)
			{
				div.style.display="block";
				div.innerHTML=info;
				if(type==0)
				{
					div.className="feedback-green_onlyforie7";
				}
				else
				{
					div.className="feedback-red_onlyforie7";
				}
				setTimeout("passfeedback.clear()",time);
			}		
		 }
		 else
		  if(info!=undefined)
		  {
			type = (type==undefined) ? 0 :type;
			type = (type>1) ?0:type;
			time = (time==undefined) ? 5000 :time;
			var div = $("pass_feedback_content");
			div.innerHTML = "";
			var divchild = document.createElement('div');
			div.appendChild(divchild);
			if(div)
			{
				div.style.display="block";
				divchild.innerHTML = info;
				if(type==0)
				{
					div.className="feedback-green";
					divchild.className="feedback-green_child";
				}
				else
				{
					div.className="feedback-red";
					divchild.className="feedback-red_child";
				}
				setTimeout("passfeedback.clear()",time);
			}
		}
	},
	clear:function (){
		$("pass_feedback_content").style.display="none";
	}
}

