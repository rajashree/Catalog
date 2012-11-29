function toggleLayer( whichLayer )
{
  var elem, vis;
  if( document.getElementById ) // this is the way the standards work
    elem = document.getElementById( whichLayer );
  else if( document.all ) // this is the way old msie versions work
      elem = document.all[whichLayer];
  else if( document.layers ) // this is the way nn4 works
    elem = document.layers[whichLayer];
  vis = elem.style;
  // if the style.display value is blank we try to figure it out here
  if(vis.display==''&&elem.offsetWidth!=undefined&&elem.offsetHeight!=undefined)
    vis.display = (elem.offsetWidth!=0&&elem.offsetHeight!=0)?'block':'none';
  vis.display = (vis.display==''||vis.display=='block')?'none':'block';
}


function useLoadingImage(imageSrc) {
		var loadingImage;
		if (imageSrc) loadingImage = imageSrc;
		else loadingImage = "ajax-loader.gif";
		dwr.engine.setPreHook(function() {
		var disabledImageZone = $('disabledImageZone');
		if (!disabledImageZone) {
		
			disabledImageZone = document.createElement('div');
			disabledImageZone.setAttribute('id', 'disabledImageZone');
			disabledImageZone.style.position = "absolute";
			disabledImageZone.style.zIndex = "1000";
			disabledImageZone.style.left = "0px";
			disabledImageZone.style.top = "0px";
			disabledImageZone.style.width = "100%";
			disabledImageZone.style.height = "100%";
			var imageZone = document.createElement('img');
			imageZone.setAttribute('id','imageZone');
			imageZone.setAttribute('src',imageSrc);
			imageZone.style.position = "absolute";
			imageZone.style.top = "0px";
			imageZone.style.right = "0px";
			disabledImageZone.appendChild(imageZone);
			document.body.appendChild(disabledImageZone);
			
		}
		else {
		
			$('imageZone').src = imageSrc;
			disabledImageZone.style.visibility = 'visible';
		}
		});
		dwr.engine.setPostHook(function() {
		$('disabledImageZone').style.visibility = 'hidden';
		});
	}
