<div id="anchor-content" class="middle">
            <div id="page:main-container">
                            <div id="messages"/>
                <div class="content-header" style="visibility: visible;">
	<table cellspacing="0">
		<tbody><tr>
			<td style="width: 50%;"><h3 class="icon-head head-newsletter-list">Newsletter Templates</h3></td>
			<td class="form-buttons">
				<button onclick="window.location='http://demo-admin.magentocommerce.com/index.php/admin/newsletter_template/new/'" class="scalable add"><span>Add New Template</span></button>
			</td>
		</tr>
	</tbody></table>
</div>
<div>
                
        <div id="id_c48794135782f11ce11a7c9ced1bb577">
        <table cellspacing="0" class="actions">
        <tbody><tr>
                    <td class="pager">
            Page
                                                    <img class="arrow" alt="Go to Previous page" src="http://demo-admin.magentocommerce.com/skin/adminhtml/default/default/images/pager_arrow_left_off.gif"/>
            
            <input type="text" onkeypress="id_c48794135782f11ce11a7c9ced1bb577JsObject.inputPage(event, '1')" class="input-text page" value="1" name="page"/>

                            <img class="arrow" alt="Go to Previous page" src="http://demo-admin.magentocommerce.com/skin/adminhtml/default/default/images/pager_arrow_right_off.gif"/>
            
            of 1 pages            <span class="separator">|</span>
            View            <select onchange="id_c48794135782f11ce11a7c9ced1bb577JsObject.loadByElement(this)" name="limit">
                <option selected="selected" value="20">20</option>
                <option value="30">30</option>
                <option value="50">50</option>
                <option value="100">100</option>
                <option value="200">200</option>
            </select>
            per page<span class="separator">|</span>
            Total 1 records found            <span class="no-display" id="id_c48794135782f11ce11a7c9ced1bb577-total-count">1</span>
                    </td>
                <td class="filter-actions a-right">
            <button style="" onclick="id_c48794135782f11ce11a7c9ced1bb577JsObject.resetFilter()" class="scalable" type="button" id="id_8073dd9a9c7a8da59865c53d3e884810"><span>Reset Filter</span></button><button style="" onclick="id_c48794135782f11ce11a7c9ced1bb577JsObject.doFilter()" class="scalable task" type="button" id="id_c0eb54f38388a475cd5004f6f3b9e26a"><span>Search</span></button>        </td>
        </tr>
    </tbody></table>
<div class="grid hor-scroll">
<table cellspacing="0" id="id_c48794135782f11ce11a7c9ced1bb577_table" class="data">
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col width="170"/>
                <thead>
                            <tr class="headings">
                                    <th><span class="nobr"><a class="not-sort" target="asc" name="id" href="#"><span class="sort-title">ID</span></a></span></th>
                                    <th><span class="nobr"><a class="not-sort" target="asc" name="code" href="#"><span class="sort-title">Template Name</span></a></span></th>
                                    <th><span class="nobr"><a class="not-sort" target="asc" name="added_at" href="#"><span class="sort-title">Date Added</span></a></span></th>
                                    <th><span class="nobr"><a class="not-sort" target="asc" name="modified_at" href="#"><span class="sort-title">Date Updated</span></a></span></th>
                                    <th><span class="nobr"><a class="not-sort" target="asc" name="subject" href="#"><span class="sort-title">Subject</span></a></span></th>
                                    <th><span class="nobr"><a class="not-sort" target="asc" name="sender" href="#"><span class="sort-title">Sender</span></a></span></th>
                                    <th><span class="nobr"><a class="not-sort" target="asc" name="type" href="#"><span class="sort-title">Template Type</span></a></span></th>
                                    <th class="no-link last"><span class="nobr">Action</span></th>
                                </tr>
                                        <tr class="filter">
                                    <th><input type="text" class="input-text no-changes" value="" id="filter_id" name="id"/></th>
                                    <th><input type="text" class="input-text no-changes" value="" id="filter_code" name="code"/></th>
                                    <th><div class="range"><div class="range-line date">
            <span class="label">From:</span>
            <input type="text" class="input-text no-changes" value="" id="filter_added_at_from" name="added_at[from]"/>
            <img title="Date selector" id="filter_added_at_from_trig" class="v-middle" alt="" src="http://demo-admin.magentocommerce.com/skin/adminhtml/default/default/images/grid-cal.gif"/>
            </div><div class="range-line date">
            <span class="label">To :</span>
            <input type="text" class="input-text no-changes" value="" id="filter_added_at_to" name="added_at[to]"/>
            <img title="Date selector" id="filter_added_at_to_trig" class="v-middle" alt="" src="http://demo-admin.magentocommerce.com/skin/adminhtml/default/default/images/grid-cal.gif"/>
            </div></div><input type="hidden" value="en_US" name="added_at[locale]"/><script type="text/javascript">
            Calendar.setup({
                inputField : "filter_added_at_from",
                ifFormat : "%m/%e/%y",
                button : "filter_added_at_from_trig",
                align : "Bl",
                singleClick : true
            });
            Calendar.setup({
                inputField : "filter_added_at_to",
                ifFormat : "%m/%e/%y",
                button : "filter_added_at_to_trig",
                align : "Bl",
                singleClick : true
            });
        </script></th>
                                    <th><div class="range"><div class="range-line date">
            <span class="label">From:</span>
            <input type="text" class="input-text no-changes" value="" id="filter_modified_at_from" name="modified_at[from]"/>
            <img title="Date selector" id="filter_modified_at_from_trig" class="v-middle" alt="" src="http://demo-admin.magentocommerce.com/skin/adminhtml/default/default/images/grid-cal.gif"/>
            </div><div class="range-line date">
            <span class="label">To :</span>
            <input type="text" class="input-text no-changes" value="" id="filter_modified_at_to" name="modified_at[to]"/>
            <img title="Date selector" id="filter_modified_at_to_trig" class="v-middle" alt="" src="http://demo-admin.magentocommerce.com/skin/adminhtml/default/default/images/grid-cal.gif"/>
            </div></div><input type="hidden" value="en_US" name="modified_at[locale]"/><script type="text/javascript">
            Calendar.setup({
                inputField : "filter_modified_at_from",
                ifFormat : "%m/%e/%y",
                button : "filter_modified_at_from_trig",
                align : "Bl",
                singleClick : true
            });
            Calendar.setup({
                inputField : "filter_modified_at_to",
                ifFormat : "%m/%e/%y",
                button : "filter_modified_at_to_trig",
                align : "Bl",
                singleClick : true
            });
        </script></th>
                                    <th><input type="text" class="input-text no-changes" value="" id="filter_subject" name="subject"/></th>
                                    <th><input type="text" class="input-text no-changes" value="" id="filter_sender" name="sender"/></th>
                                    <th><select class="no-changes" id="filter_type" name="type"><option value=""/><option value="2">html</option><option value="1">text</option></select></th>
                                    <th class="no-link last"><div style="width: 100%;"> </div></th>
                                </tr>
                    </thead>
            <tbody>
                <tr id="http://demo-admin.magentocommerce.com/index.php/admin/newsletter_template/edit/id/3/" class="even pointer">
                    <td class="a-center">3</td>
                    <td class="">Great Newsletter</td>
                    <td class="">Mar 30, 2008 10:31:49 PM</td>
                    <td class="">Mar 30, 2008 10:31:49 PM</td>
                    <td class="">Greatness</td>
                    <td class="">Magento [admin@magentocommerce.com]</td>
                    <td class="">html</td>
                    <td class="last"><select onchange="varienGridAction.execute(this);" class="action-select"><option value=""/><option value="{"href":"http:\/\/demo-admin.magentocommerce.com\/index.php\/admin\/newsletter_template\/toqueue\/id\/3\/"}">Queue Newsletter</option><option value="{"popup":"1","href":"http:\/\/demo-admin.magentocommerce.com\/index.php\/admin\/newsletter_template\/preview\/id\/3\/","onclick":"popWin(this.href, 'windth=800,height=700,resizable=1,scrollbars=1');return false;"}">Preview</option></select></td>
                </tr>
            </tbody>
</table>
</div>
</div>
<script language="javascript" type="text/javascript">
    id_c48794135782f11ce11a7c9ced1bb577JsObject = new varienGrid('id_c48794135782f11ce11a7c9ced1bb577', 'http://demo-admin.magentocommerce.com/index.php/admin/newsletter_template/index/', 'page', 'sort', 'dir', 'filter');
    id_c48794135782f11ce11a7c9ced1bb577JsObject.useAjax = '';
            id_c48794135782f11ce11a7c9ced1bb577JsObject.rowClickCallback = openGridRow;
                </script>
</div>                        </div>
        </div>