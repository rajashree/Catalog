

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>
  newco / chef-mvenablement / source &mdash; Bitbucket
</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="description" content="" />
  <meta name="keywords" content="" />
  
  <!--[if lt IE 9]>
  <script src="https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/js/lib/html5.js"></script>
  <![endif]-->

  <script>
    (function (window) {
      // prevent stray occurrences of `console.log` from causing errors in IE
      var console = window.console || (window.console = {});
      console.log || (console.log = function () {});

      var BB = window.BB || (window.BB = {});
      BB.debug = false;
      BB.cname = false;
      BB.CANON_URL = 'https://bitbucket.org';
      BB.MEDIA_URL = 'https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/';
      BB.images = {
        invitation: 'https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/img/icons/fugue/card_address.png',
        noAvatar: 'https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/img/no_avatar.png'
      };
      BB.user = {"username": "newco", "displayName": "NewCo Account (mvine)", "firstName": "NewCo", "avatarUrl": "https://secure.gravatar.com/avatar/2d8802aa18b18af1fedbeb3b1756f958?d=identicon\u0026s=32", "follows": {"repos": [1069784, 1089065]}, "isTeam": false, "isSshEnabled": true, "lastName": "Account (mvine)", "isKbdShortcutsEnabled": true, "id": 264611};
      BB.user.has = (function () {
        var betaFeatures = [];
        betaFeatures.push('repo2');
        return function (feature) {
          return _.contains(betaFeatures, feature);
        };
      }());
      BB.targetUser = BB.user;
  
    
  
      BB.repo || (BB.repo = {});
  
      
        BB.user.repoPrivilege = "admin";
      
      
        
          BB.user.accountPrivilege = null;
        
      
      BB.repo.id = 961228;
    
    
      BB.repo.language = null;
      BB.repo.pygmentsLanguage = null;
    
    
      BB.repo.slug = 'chef\u002Dmvenablement';
    
    
      BB.repo.owner = {"username": "newco", "displayName": "NewCo Account (mvine)", "firstName": "NewCo", "avatarUrl": "https://secure.gravatar.com/avatar/2d8802aa18b18af1fedbeb3b1756f958?d=identicon\u0026s=32", "follows": {"repos": [1069784, 1089065]}, "isTeam": false, "isSshEnabled": true, "lastName": "Account (mvine)", "isKbdShortcutsEnabled": true, "id": 264611};
    
    
      
        
      
    
    
      // Coerce `BB.repo` to a string to get
      // "davidchambers/mango" or whatever.
      BB.repo.toString = function () {
        return BB.cname ? this.slug : '{owner.username}/{slug}'.format(this);
      }
    
      BB.repo.parent = {
        slug: 'chef\u002Dbase',
        owner: {
          username: 'newco'
        },
        toString: BB.repo.toString
      }
    
    
      BB.changeset = '3695b8d72b72'
    
    
  
    }(this));
  </script>

  


  <link rel="stylesheet" href="https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/bun/css/bundle.css"/>



  <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="Bitbucket" />
  <link rel="icon" href="https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/img/logo_new.png" type="image/png" />
  <link type="text/plain" rel="author" href="/humans.txt" />


  
  
    <script src="https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/bun/js/bundle.js"></script>
  



</head>

<body id="" class=" ">
  <script>
    if (navigator.userAgent.indexOf(' AppleWebKit/') === -1) {
      $('body').addClass('non-webkit')
    }
    $('body')
      .addClass($.client.os.toLowerCase())
      .addClass($.client.browser.toLowerCase())
  </script>
  <!--[if IE 8]>
  <script>jQuery(document.body).addClass('ie8')</script>
  <![endif]-->
  <!--[if IE 9]>
  <script>jQuery(document.body).addClass('ie9')</script>
  <![endif]-->

  <div id="wrapper">



  <div id="header-wrap">
    <div id="header">
    <ul id="global-nav">
      <li><a class="home" href="http://www.atlassian.com">Atlassian Home</a></li>
      <li><a class="docs" href="http://confluence.atlassian.com/display/BITBUCKET">Documentation</a></li>
      <li><a class="support" href="/support">Support</a></li>
      <li><a class="blog" href="http://blog.bitbucket.org">Blog</a></li>
      <li><a class="forums" href="http://groups.google.com/group/bitbucket-users">Forums</a></li>
    </ul>
    <a href="/" id="logo">Bitbucket by Atlassian</a>

    <div id="main-nav">
    

      <ul class="clearfix">
        <li><a href="/explore" id="explore-link">Explore</a></li>
        <li><a href="https://bitbucket.org" id="dashboard-link">Dashboard</a></li>
        <li id="repositories-dropdown" class="inertial-hover active">
          <a class="drop-arrow" href="/repo/mine" id="repositories-link">Repositories</a>
          <div>
            <div>
              <div id="repo-overview"></div>
              <div class="group">
                <a href="/repo/create" class="new-repository" id="create-repo-link">Create repository</a>
                <a href="/repo/import" class="import-repository" id="import-repo-link">Import repository</a>
              </div>
            </div>
          </div>
        </li>
        <li id="user-dropdown" class="inertial-hover">
          <a class="drop-arrow" href="/newco">
            <span>NewCo Account (mvine)</span>
          </a>
          <div>
            <div>
              <div class="group">
                <a href="/account/user/newco/" id="account-link">Account</a>
                <a href="/account/notifications/" id="inbox-link">Inbox <span>(55)</span></a>
                <a href="/account/signout/">Log out</a>
              </div>

    
      
        
      
      
          <div class="group">
            <a href="/account/create-team/" id="create-team">Create team</a>
            <a href="/account/user/newco/convert-team/" id="convert-team">Convert to team</a>
          </div>
      
    

            </div>
          </div>
        </li>
        

<li class="search-box">
  
    <form action="/repo/all">
      <input type="search" results="5" autosave="bitbucket-explore-search"
             name="name" id="searchbox"
             placeholder="owner/repo" />
  
  </form>
</li>

      </ul>

    
    </div>

  

    </div>
  </div>

    <div id="header-messages">
  
    
    
    
    
  

    
   </div>



    <div id="content">
      <div id="source">
      
  
  





  <script>
    jQuery(function ($) {
        var cookie = $.cookie,
            cookieOptions, date,
            $content = $('#content'),
            $pane = $('#what-is-bitbucket'),
            $hide = $pane.find('[href="#hide"]').css('display', 'block').hide();

        date = new Date();
        date.setTime(date.getTime() + 365 * 24 * 60 * 60 * 1000);
        cookieOptions = { path: '/', expires: date };

        if (cookie('toggle_status') == 'hide') $content.addClass('repo-desc-hidden');

        $('#toggle-repo-content').click(function (event) {
            event.preventDefault();
            $content.toggleClass('repo-desc-hidden');
            cookie('toggle_status', cookie('toggle_status') == 'show' ? 'hide' : 'show', cookieOptions);
        });

        if (!cookie('hide_intro_message')) $pane.show();

        $hide.click(function (event) {
            event.preventDefault();
            cookie('hide_intro_message', true, cookieOptions);
            $pane.slideUp('slow');
        });

        $pane.hover(
            function () { $hide.fadeIn('fast'); },
            function () { $hide.fadeOut('fast'); });

      (function () {
        // Update "recently-viewed-repos" cookie for
        // the "repositories" drop-down.
        var
          id = BB.repo.id,
          cookieName = 'recently-viewed-repos_' + BB.user.id,
          rvr = cookie(cookieName),
          ids = rvr? rvr.split(','): [],
          idx = _.indexOf(ids, '' + id);

        // Remove `id` from `ids` if present.
        if (~idx) ids.splice(idx, 1);

        cookie(
          cookieName,
          // Insert `id` as the first item, then call
          // `join` on the resulting array to produce
          // something like "114694,27542,89002,84570".
          [id].concat(ids.slice(0, 4)).join(),
          {path: '/', expires: 1e6} // "never" expires
        );
      }());
    });
  </script>



  <meta name="twitter:card" value="summary"/>
  <meta name="twitter:site" value="@bitbucket"/>
  <meta name="twitter:url" value="/newco/chef-mvenablement"/>
  <meta name="twitter:title" value="newco/chef-mvenablement - bitbucket.org"/>
  <meta name="twitter:description" value="Chef repo for the enablement team."/>




<div id="tabs" class="tabs">
  <ul>
    
      <li>
        <a href="/newco/chef-mvenablement/overview" id="repo-overview-link">Overview</a>
      </li>
    

    
      <li>
        <a href="/newco/chef-mvenablement/downloads" id="repo-downloads-link">Downloads (<span id="downloads-count">0</span>)</a>
      </li>
    

    
      
    

    
      <li>
        <a href="/newco/chef-mvenablement/pull-requests" id="repo-pr-link">Pull requests (0)</a>
      </li>
    

    
      <li class="selected">
        
          <a href="/newco/chef-mvenablement/src" id="repo-source-link">Source</a>
        
      </li>
    

    
      <li>
        <a href="/newco/chef-mvenablement/changesets" id="repo-commits-link">Commits</a>
      </li>
    

    <li id="wiki-tab" class="dropdown"
      style="display:
                        none  
        
      ">
      <a href="/newco/chef-mvenablement/wiki" id="repo-wiki-link">Wiki</a>
    </li>

    <li id="issues-tab" class="dropdown inertial-hover"
      style="display:
                      none  
        
      ">
      <a href="/newco/chef-mvenablement/issues?status=new&amp;status=open" id="repo-issues-link">Issues (0) &raquo;</a>
      <ul>
        <li><a href="/newco/chef-mvenablement/issues/new">Create new issue</a></li>
        <li><a href="/newco/chef-mvenablement/issues?status=new">New issues</a></li>
        <li><a href="/newco/chef-mvenablement/issues?status=new&amp;status=open">Open issues</a></li>
        <li><a href="/newco/chef-mvenablement/issues?status=duplicate&amp;status=invalid&amp;status=resolved&amp;status=wontfix">Closed issues</a></li>
        
          <li><a href="/newco/chef-mvenablement/issues?responsible=newco">My issues</a></li>
        
        <li><a href="/newco/chef-mvenablement/issues">All issues</a></li>
        <li><a href="/newco/chef-mvenablement/issues/query">Advanced query</a></li>
      </ul>
    </li>

    
        <li>
          <a href="/newco/chef-mvenablement/admin" id="repo-admin-link">Admin</a>
        </li>
    
  </ul>

  <ul>
    
      <li>
        <a href="/newco/chef-mvenablement/descendants" id="repo-forks-link">Forks/queues (0)</a>
      </li>
    

    
      <li>
        <a href="/newco/chef-mvenablement/zealots">Followers (<span id="followers-count">0</span>)</a>
      </li>
    
  </ul>
</div>




  <div id="invitation-dialog" title="Send an invitation">

<form class="invitation-form newform"
  method="post"
  action="/api/1.0/invitations/newco/chef-mvenablement"
  novalidate>
  <div style='display:none'><input type='hidden' name='csrfmiddlewaretoken' value='ff4d591c2f0233af0738a0fef50afb3d' /></div>
  <div class="error_ message_"><h4></h4></div>
  <div class="success_ message_"><h4></h4></div>
  <label for="id-email-address">Email address</label>
  <input type="email" id="id-email-address" name="email-address">
  <select name="permission" class="nosearch">
  
    <option value="read">Read access</option>
  
    <option value="write">Write access</option>
    <option value="admin">Admin access</option>
  </select>
  <input type="submit" value="Send invitation" />
</form>
</div>
 


  <div class="repo-menu" id="repo-menu">
    <ul id="repo-menu-links">
    
    
      <li>
        <a id="repo-invite-link" href="#share" class="share">invite</a>
      </li>
    
      <li>
        <a href="/newco/chef-mvenablement/rss?token=c1dcfd2ec00916667b184e9e127d45dc" class="rss" title="RSS feed for chef-mvenablement">RSS</a>
      </li>

      <li><a id="repo-fork-link" href="/newco/chef-mvenablement/fork" class="fork">fork</a></li>
      
      <li>
        <a id="repo-follow-link" rel="nofollow" href="/newco/chef-mvenablement/follow" class="follow">follow</a>
      </li>
      
          
      
      
        <li class="get-source inertial-hover">
          <a class="source">get source</a>
          <ul class="downloads">
            
              
              <li><a rel="nofollow" href="/newco/chef-mvenablement/get/3695b8d72b72.zip">zip</a></li>
              <li><a rel="nofollow" href="/newco/chef-mvenablement/get/3695b8d72b72.tar.gz">gz</a></li>
              <li><a rel="nofollow" href="/newco/chef-mvenablement/get/3695b8d72b72.tar.bz2">bz2</a></li>
            
          </ul>
        </li>
      
      
    </ul>

  
    <ul class="metadata">
      
      
      
        <li class="branches inertial-hover">branches
          <ul>
            <li class="filter">
              <input type="text" class="dropdown-filter" placeholder="Filter branches" autosave="branch-dropdown-961228"/>
            </li>
            
            <li class="comprev"><a href="/newco/chef-mvenablement/src/3695b8d72b72" title="master">master</a>
              
            </li>
          </ul>
        </li>
      
      
     
      
    </ul>
  
  </div>




<div class="repo-menu" id="repo-desc">
    <ul id="repo-menu-links-mini">
      
        <li><a rel="nofollow" class="compare-link"
               href="/newco/chef-mvenablement/compare/..newco/chef-base"
               title="Show changes between chef-mvenablement and chef-base"
               ></a></li>
        
          <li>
            <a href="/newco/chef-mvenablement/pull-request/new" class="pull-request" title="Pull request"></a>
          </li>
        
      

      
        <li>
          <a href="#share" class="share" title="Invite">invite</a>
        </li>
      
      <li>
        <a href="/newco/chef-mvenablement/rss?token=c1dcfd2ec00916667b184e9e127d45dc" class="rss" title="RSS feed for chef-mvenablement"></a>
      </li>

      <li><a href="/newco/chef-mvenablement/fork" class="fork" title="Fork"></a></li>
      
      <li>
        <a rel="nofollow" href="/newco/chef-mvenablement/follow" class="follow">follow</a>
      </li>
      
          
      
      
        <li>
          <a class="source" title="Get source"></a>
          <ul class="downloads">
            
              
              <li><a rel="nofollow" href="/newco/chef-mvenablement/get/3695b8d72b72.zip">zip</a></li>
              <li><a rel="nofollow" href="/newco/chef-mvenablement/get/3695b8d72b72.tar.gz">gz</a></li>
              <li><a rel="nofollow" href="/newco/chef-mvenablement/get/3695b8d72b72.tar.bz2">bz2</a></li>
            
          </ul>
        </li>
      
    </ul>

    <h3 id="repo-heading" class="private git">
      <a class="owner-username" href="/newco">newco</a> /
      <a class="repo-name" href="/newco/chef-mvenablement">chef-mvenablement</a>
    
      (fork of
      
      <a href="/newco/chef-base">chef-base</a>)
    

    
      <ul id="fork-actions" class="button-group">
      
        <li>
          <a id="repo-compare-link" href="/newco/chef-mvenablement/compare/..newco/chef-base"
             rel="nofollow" class="icon compare-link">compare fork</a>
        </li>
      
      
        <li>
          <a id="repo-create-pr-link" href="/newco/chef-mvenablement/pull-request/new"
             class="icon pull-request">create pull request</a>
        </li>
      
      </ul>
    
    </h3>

    
      <p class="repo-desc-description">Chef repo for the enablement team.</p>
    

  <div id="repo-desc-cloneinfo">Clone this repository (size: 15.7 MB):
    <a href="https://newco@bitbucket.org/newco/chef-mvenablement.git" class="https">HTTPS</a> /
    <a href="ssh://git@bitbucket.org/newco/chef-mvenablement.git" class="ssh">SSH</a>
    
      <div id="sourcetree-clone-link" class="clone-in-client mac  help-activated"
          data-desktop-clone-url-ssh="sourcetree://cloneRepo/ssh://git@bitbucket.org/newco/chef-mvenablement.git"
          data-desktop-clone-url-https="sourcetree://cloneRepo/https://newco@bitbucket.org/newco/chef-mvenablement.git">
         /
           <a class="desktop-ssh"
             href="sourcetree://cloneRepo/ssh://git@bitbucket.org/newco/chef-mvenablement.git">SourceTree</a>
           <a class="desktop-https"
             href="sourcetree://cloneRepo/https://newco@bitbucket.org/newco/chef-mvenablement.git">SourceTree</a>
      </div>
    
    <pre id="clone-url-https">git clone https://newco@bitbucket.org/newco/chef-mvenablement.git</pre>
    <pre id="clone-url-ssh">git clone git@bitbucket.org:newco/chef-mvenablement.git</pre>
    
  </div>

        <a href="#" id="toggle-repo-content"></a>

        

        
          
        

</div>






      
  <div id="source-container">
    

  <div id="source-path">
    <h1>
      <a href="/newco/chef-mvenablement/src" class="src-pjax">chef-mvenablement</a> /

  
    
      <a href="/newco/chef-mvenablement/src/3695b8d72b72/cookbooks/" class="src-pjax">cookbooks</a> /
    
  

  
    
      <a href="/newco/chef-mvenablement/src/3695b8d72b72/cookbooks/enablement/" class="src-pjax">enablement</a> /
    
  

  
    
      <span>README.md</span>
    
  

    </h1>
  </div>

  <div class="labels labels-csv">
  
    <dl>
  
    
  
  
    
  
  
    <dt>Branch</dt>
    
      
        <dd class="branch unabridged"><a href="/newco/chef-mvenablement/changesets/tip/master" title="master">master</a></dd>
      
    
  
</dl>

  
  </div>


  
  <div id="source-view">
    <div class="header">
      <ul class="metadata">
        <li><code>3695b8d72b72</code></li>
        
          
            <li>12 loc</li>
          
        
        <li>88 bytes</li>
      </ul>
      <ul class="source-view-links">
        
        <li><a href="/newco/chef-mvenablement/history/cookbooks/enablement/README.md">history</a></li>
        
        <li><a href="/newco/chef-mvenablement/annotate/3695b8d72b72/cookbooks/enablement/README.md">annotate</a></li>
        
        <li><a href="/newco/chef-mvenablement/raw/3695b8d72b72/cookbooks/enablement/README.md">raw</a></li>
        <li>
          <form action="/newco/chef-mvenablement/diff/cookbooks/enablement/README.md" class="source-view-form">
          
            <input type="hidden" name="diff2" value="895edee007e3" />
            <select name="diff1">
            
              
                <option value="895edee007e3">895edee007e3</option>
              
            
            </select>
            <input type="submit" value="diff" />
          
          </form>
        </li>
      </ul>
    </div>
  
    <div>
    <table class="highlighttable"><tr><td class="linenos"><div class="linenodiv"><pre><a href="#cl-1"> 1</a>
<a href="#cl-2"> 2</a>
<a href="#cl-3"> 3</a>
<a href="#cl-4"> 4</a>
<a href="#cl-5"> 5</a>
<a href="#cl-6"> 6</a>
<a href="#cl-7"> 7</a>
<a href="#cl-8"> 8</a>
<a href="#cl-9"> 9</a>
<a href="#cl-10">10</a>
<a href="#cl-11">11</a>
</pre></div></td><td class="code"><div class="highlight"><pre><a name="cl-1"></a><span class="n">Description</span>
<a name="cl-2"></a><span class="p">===========</span>
<a name="cl-3"></a>
<a name="cl-4"></a><span class="n">Requirements</span>
<a name="cl-5"></a><span class="p">============</span>
<a name="cl-6"></a>
<a name="cl-7"></a><span class="n">Attributes</span>
<a name="cl-8"></a><span class="p">==========</span>
<a name="cl-9"></a>
<a name="cl-10"></a><span class="n">Usage</span>
<a name="cl-11"></a><span class="p">=====</span>
</pre></div>
</td></tr></table>
    </div>
  
  </div>
  


  <div id="mask"><div></div></div>

  </div>

      </div>
    </div>

  </div>

  <div id="footer">
    <ul id="footer-nav">
      <li>Copyright © 2012 <a href="http://atlassian.com">Atlassian</a></li>
      <li><a href="http://www.atlassian.com/hosted/terms.jsp">Terms of Service</a></li>
      <li><a href="http://www.atlassian.com/about/privacy.jsp">Privacy</a></li>
      <li><a href="//bitbucket.org/site/master/issues/new">Report a Bug to Bitbucket</a></li>
      <li><a href="http://confluence.atlassian.com/x/IYBGDQ">API</a></li>
      <li><a href="http://status.bitbucket.org/">Server Status</a></li>
    </ul>
    <ul id="social-nav">
      <li class="blog"><a href="http://blog.bitbucket.org">Bitbucket Blog</a></li>
      <li class="twitter"><a href="http://www.twitter.com/bitbucket">Twitter</a></li>
    </ul>
    <h5>We run</h5>
    <ul id="technologies">
      <li><a href="http://www.djangoproject.com/">Django 1.3.1</a></li>
      <li><a href="//bitbucket.org/jespern/django-piston/">Piston 0.3dev</a></li>
      <li><a href="http://git-scm.com/">Git 1.7.10.3</a></li>
      <li><a href="http://www.selenic.com/mercurial/">Hg 2.2.2</a></li>
      <li><a href="http://www.python.org">Python 2.7.3</a></li>
      <li>fce3e2651951 | bitbucket02</li>
    </ul>
  </div>

  <script src="https://dwz7u9t8u8usb.cloudfront.net/m/4b9c6ec9c86e/js/lib/global.js"></script>






  <script>
    BB.gaqPush(['_trackPageview']);
  
    BB.gaqPush(['atl._trackPageview']);

    

    

    (function () {
        var ga = document.createElement('script');
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        ga.setAttribute('async', 'true');
        document.documentElement.firstChild.appendChild(ga);
    }());
  </script>

</body>
</html>
