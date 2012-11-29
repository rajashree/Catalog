package com.dell.dw.managers.dataimport.service;

import com.sourcen.core.config.ConfigurationService;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.support.BaseDataAdapter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderDataReader extends BaseDataAdapter implements DataReader {

    public OrderDataReader() {
    }

    @Override
    public Collection<Map<String, Object>> getRows() {
        List<Map<String, Object>> orderItems = new LinkedList<Map<String, java.lang.Object>>();

        String sitesStr = configurationService.getProperty("orders.trackingAPI.sites", "C4A02F89-7415-451E-857B-B952DBB7871E,2D2C2E6C-4E51-44AF-9902-681CBAA357A6");
        List<String> sites = Arrays.asList(sitesStr.split(","));
        //sites.add("C4A02F89-7415-451E-857B-B952DBB7871E");
        //sites.add("2D2C2E6C-4E51-44AF-9902-681CBAA357A6");

        GetMethod method = null;
        try {
            String baseUrl = configurationService.getProperty("orders.trackingAPI.baseUrl", "http://api-stage.marketvine.com/V2/Order/Search");
            String apiKey = configurationService.getProperty("orders.trackingAPI.apiKey", "76790858-c798-4e2a-8b41-882a8a94fef0");

            HttpClient httpClient = new HttpClient();

            for (String siteId : sites) {
                method = new GetMethod(baseUrl + "?ApiKey=" + apiKey + "&SiteId=" + siteId);
                int statusCode = httpClient.executeMethod(method);
                if (statusCode == HttpStatus.SC_OK) {
                    JSONObject totalCountResult = JSONObject.fromObject(method.getResponseBodyAsString());
                    if (totalCountResult.get("Success") != null && totalCountResult.containsKey("Success")) {
                        if ((Boolean) totalCountResult.get("Success")) {
                            if (totalCountResult.get("Data") != null && totalCountResult.containsKey("Data")) {
                                JSONObject totalCountDataObj = totalCountResult.getJSONObject("Data");
                                int pageCount = -1;
                                if (totalCountDataObj.get("Total") != null && totalCountDataObj.containsKey("Total"))
                                    pageCount = totalCountDataObj.getInt("Total") / 100;

                                for (int i = 0; i <= pageCount; i++) {
                                    method = new GetMethod(baseUrl + "?ApiKey=" + apiKey + "&SiteId=" + siteId + "&pageNumber=" + (i + 1));
                                    statusCode = httpClient.executeMethod(method);
                                    if (statusCode == HttpStatus.SC_OK) {
                                        JSONObject result = JSONObject.fromObject(method.getResponseBodyAsString());
                                        if (result.get("Success") != null && result.containsKey("Success")) {
                                            if ((Boolean) result.get("Success")) {
                                                if (result.get("Data") != null && result.containsKey("Data")) {
                                                    if (result.getJSONObject("Data").getJSONArray("Items") != null && result.getJSONObject("Data").containsKey("Items")) {
                                                        JSONArray orderJsonArr = result.getJSONObject("Data").getJSONArray("Items");
                                                        for (Object orderObj : orderJsonArr) {
                                                            JSONObject orderJsonObj = (JSONObject) orderObj;
                                                            JSONArray orderItemsJsonArr = null;
                                                            JSONObject leadJsonObj = null;
                                                            String dateString = null;
                                                            if (orderJsonObj.get("Items") != null && orderJsonObj.containsKey("Items"))
                                                                orderItemsJsonArr = orderJsonObj.getJSONArray("Items");
                                                            if (orderJsonObj.get("Lead") != null && orderJsonObj.containsKey("Lead"))
                                                                leadJsonObj = orderJsonObj.getJSONObject("Lead");
                                                            if (leadJsonObj != null && orderJsonObj != null && orderItemsJsonArr.size() > 0) {
                                                                for (Object orderItemObj : orderItemsJsonArr) {
                                                                    Map<String, Object> objectMap = new HashMap<String, Object>();
                                                                    JSONObject orderItemJsonObj = (JSONObject) orderItemObj;
                                                                    //TODO : to be changed once we have orderItemId as part of json response
                                                                    if (orderItemJsonObj.get("Sku") != null && orderItemJsonObj.containsKey("Sku"))
                                                                        objectMap.put("orderItemId", orderItemJsonObj.getString("Sku"));
                                                                    if (orderItemJsonObj.get("Sku") != null && orderItemJsonObj.containsKey("Sku"))
                                                                        objectMap.put("sku", orderItemJsonObj.getString("Sku"));
                                                                    if (orderItemJsonObj.get("Name") != null && orderItemJsonObj.containsKey("Name"))
                                                                        objectMap.put("name", orderItemJsonObj.getString("Name"));
                                                                    if (orderItemJsonObj.get("Category") != null && orderItemJsonObj.containsKey("Category"))
                                                                        objectMap.put("category", orderItemJsonObj.getString("Category"));
                                                                    if (orderItemJsonObj.get("UnitPrice") != null && orderItemJsonObj.containsKey("UnitPrice"))
                                                                        objectMap.put("unitPrice", orderItemJsonObj.getDouble("UnitPrice"));
                                                                    if (orderItemJsonObj.get("UnitDiscount") != null && orderItemJsonObj.containsKey("UnitDiscount"))
                                                                        objectMap.put("unitDiscount", orderItemJsonObj.getDouble("UnitDiscount"));
                                                                    if (orderItemJsonObj.get("Quantity") != null && orderItemJsonObj.containsKey("Quantity"))
                                                                        objectMap.put("quantity", orderItemJsonObj.getInt("Quantity"));
                                                                    if (orderItemJsonObj.get("IsCancelled") != null && orderItemJsonObj.containsKey("IsCancelled"))
                                                                        objectMap.put("orderItemCancelled", orderItemJsonObj.getBoolean("IsCancelled"));
                                                                    if (orderItemJsonObj.get("CancelQuantity") != null && orderItemJsonObj.containsKey("CancelQuantity"))
                                                                        objectMap.put("cancelQuantity", orderItemJsonObj.getInt("CancelQuantity"));
                                                                    if (orderItemJsonObj.get("CancelReason") != null && orderItemJsonObj.containsKey("CancelReason") && !orderItemJsonObj.getString("CancelReason").equalsIgnoreCase("null"))
                                                                        objectMap.put("cancelReason", orderItemJsonObj.getString("CancelReason"));
                                                                    if (orderItemJsonObj.get("CommissionAmount") != null && orderItemJsonObj.containsKey("CommissionAmount"))
                                                                        objectMap.put("commissionAmount", orderItemJsonObj.getDouble("CommissionAmount"));

                                                                    //TODO : to be changed once we have orderId as part of json response
                                                                    if (orderJsonObj.get("TransactionId") != null && orderJsonObj.containsKey("TransactionId"))
                                                                        objectMap.put("orderOrderId", orderJsonObj.getString("TransactionId"));
                                                                    if (orderJsonObj.get("SiteId") != null && orderJsonObj.containsKey("SiteId"))
                                                                        objectMap.put("orderSiteId", orderJsonObj.getString("SiteId"));
                                                                    if (orderJsonObj.get("City") != null && orderJsonObj.containsKey("City"))
                                                                        objectMap.put("orderCity", orderJsonObj.getString("City"));
                                                                    if (orderJsonObj.get("State") != null && orderJsonObj.containsKey("State"))
                                                                        objectMap.put("orderState", orderJsonObj.getString("State"));
                                                                    if (orderJsonObj.get("Country") != null && orderJsonObj.containsKey("Country"))
                                                                        objectMap.put("orderCountry", orderJsonObj.getString("Country"));
                                                                    if (orderJsonObj.get("TransactionId") != null && orderJsonObj.containsKey("TransactionId"))
                                                                        objectMap.put("orderTransactionId", orderJsonObj.getString("TransactionId"));
                                                                    //TODO : to be changed once we have createdate as part of json response
                                                                    if (orderJsonObj.get("TransactionDate") != null && orderJsonObj.containsKey("TransactionDate")) {
                                                                        dateString = orderJsonObj.getString("TransactionDate").replace("/Date(", "").replace(")/", "");
                                                                        objectMap.put("orderCreationDate", dateString);
                                                                    }
                                                                    if (orderJsonObj.get("TransactionDate") != null && orderJsonObj.containsKey("TransactionDate")) {
                                                                        dateString = orderJsonObj.getString("TransactionDate").replace("/Date(", "").replace(")/", "");
                                                                        objectMap.put("orderTransactionDate", dateString);
                                                                    }
                                                                    if (orderJsonObj.get("Status") != null && orderJsonObj.containsKey("Status"))
                                                                        objectMap.put("orderStatus", orderJsonObj.getString("Status"));
                                                                    if (orderJsonObj.get("IsCancelled") != null && orderJsonObj.containsKey("IsCancelled"))
                                                                        objectMap.put("orderOrderCancelled", orderJsonObj.getBoolean("IsCancelled"));
                                                                    if (orderJsonObj.get("CancelReason") != null && orderJsonObj.containsKey("CancelReason"))
                                                                        objectMap.put("orderCancelReason", orderJsonObj.getString("CancelReason"));
                                                                    if (orderJsonObj.get("OriginalSaleAmount") != null && orderJsonObj.containsKey("OriginalSaleAmount"))
                                                                        objectMap.put("orderOriginalSaleAmount", orderJsonObj.getDouble("OriginalSaleAmount"));
                                                                    if (orderJsonObj.get("OriginalCommissionAmount") != null && orderJsonObj.containsKey("OriginalCommissionAmount"))
                                                                        objectMap.put("orderOriginalCommissionAmount", orderJsonObj.getDouble("OriginalCommissionAmount"));
                                                                    if (orderJsonObj.get("FinalSaleAmount") != null && orderJsonObj.containsKey("FinalSaleAmount"))
                                                                        objectMap.put("orderFinalSaleAmount", orderJsonObj.getDouble("FinalSaleAmount"));
                                                                    if (orderJsonObj.get("FinalCommissionAmount") != null && orderJsonObj.containsKey("FinalCommissionAmount"))
                                                                        objectMap.put("orderFinalCommissionAmount", orderJsonObj.getDouble("FinalCommissionAmount"));
                                                                    if (orderJsonObj.get("DiscountAmountToDate") != null && orderJsonObj.containsKey("DiscountAmountToDate"))
                                                                        objectMap.put("orderTotalDiscountAmountToDate", orderJsonObj.getDouble("DiscountAmountToDate"));
                                                                    if (orderJsonObj.get("CancellationAmountToDate") != null && orderJsonObj.containsKey("CancellationAmountToDate"))
                                                                        objectMap.put("orderTotalCancellationAmountToDate", orderJsonObj.getDouble("CancellationAmountToDate"));
                                                                    if (leadJsonObj.get("Id") != null && leadJsonObj.containsKey("Id"))
                                                                        objectMap.put("leadId", leadJsonObj.getString("Id"));
                                                                    if (leadJsonObj.get("SiteId") != null && leadJsonObj.containsKey("SiteId"))
                                                                        objectMap.put("leadSiteId", leadJsonObj.getString("SiteId"));
                                                                    if (leadJsonObj.get("EntityId") != null && leadJsonObj.containsKey("EntityId"))
                                                                        objectMap.put("leadEntityId", leadJsonObj.getString("EntityId"));
                                                                    if (leadJsonObj.get("RedirectUrl") != null && leadJsonObj.containsKey("RedirectUrl"))
                                                                        objectMap.put("leadRedirectUrl", leadJsonObj.getString("RedirectUrl"));
                                                                    if (leadJsonObj.get("MaxOrderCount") != null && leadJsonObj.containsKey("MaxOrderCount"))
                                                                        objectMap.put("leadMaxOrderCount", leadJsonObj.getInt("MaxOrderCount"));
                                                                    if (leadJsonObj.get("CreateDate") != null && leadJsonObj.containsKey("CreateDate")) {
                                                                        dateString = leadJsonObj.getString("CreateDate").replace("/Date(", "").replace(")/", "");
                                                                        objectMap.put("leadCreationDate", dateString);
                                                                    }
                                                                    if (leadJsonObj.get("ExpirationDate") != null && leadJsonObj.containsKey("ExpirationDate")) {
                                                                        dateString = leadJsonObj.getString("ExpirationDate").replace("/Date(", "").replace(")/", "");
                                                                        objectMap.put("leadExpirationDate", dateString);
                                                                    }
                                                                    orderItems.add(objectMap);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
            return orderItems;
        } catch (NullPointerException e) {
            this.getExceptionHandler().onDataReaderException(e);
        } catch (Exception ie) {
            this.getExceptionHandler().onDataReaderException(ie);
        } finally {
            method.releaseConnection();

        }
        return null;
    }



    protected int resultSize = 0;

    @Override
    public void processRowAfterExtraction(Map<String, Object> record, Integer recordIndex) {
    }

    ConfigurationService configurationService;

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
