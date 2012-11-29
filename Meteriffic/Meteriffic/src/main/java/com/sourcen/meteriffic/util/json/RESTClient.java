package com.sourcen.meteriffic.util.json;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Base class for REST Clients  
 */
public class RESTClient {
//
//	/**
//	 * Reads the contents of a stream into a string
//	 */
//    private String convertStreamToString(InputStream is) {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//            	throw new RuntimeException(e);
//            }
//        }
//
//        return sb.toString();
//    }
//
//
//
//    /**
//     * Makes a GET request to the specified url and returns a JSON Object
//     * @param url
//     * @return a JSON encoded response
//     * @throws com.sourcen.meteriffic.util.json.JSONException
//     */
//    protected JSONObject makeGETRequest(String url) throws JSONException {
//    	HttpClient httpclient = new DefaultHttpClient();
//    	HttpGet httpget = new HttpGet(url);
//    	HttpResponse response;
//    	String resultText = "";
//
//    	try {
//			response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//			  if (entity != null) {
//				  InputStream instream = null;
//				  try {
//					  instream = entity.getContent();
//					  resultText = convertStreamToString(instream);
//				  }
//	              finally {
//	            	  instream.close();
//	              }
//			  }
//
//		} catch (ClientProtocolException e) {
//			throw new RuntimeException(e);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//    	return new JSONObject(resultText);
//    }
//
//    /**
//     * Makes a POST request to the specified URL and passes the provided JSON Object
//     * @param url URL Endpoint
//     * @param contents JSON Object to post to URL
//     * @return a JSON encoded response
//     * @throws com.sourcen.meteriffic.util.json.JSONException
//     */
//    public JSONObject makePOSTRequest(String url, JSONObject contents) throws JSONException {
//    	HttpClient httpclient = new DefaultHttpClient();
//    	HttpPost post = new HttpPost(url);
//    	post.setHeader("Accept", "application/json");
//    	post.setHeader("Content-Type", "application/json");
//
//
//    	try {
//			post.setEntity(new StringEntity(contents.toString()));
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
//
//		HttpResponse response;
//    	String resultText = "";
//
//    	try {
//			response = httpclient.execute(post);
//			HttpEntity entity = response.getEntity();
//			  if (entity != null) {
//				  InputStream instream = null;
//				  try {
//					  instream = entity.getContent();
//					  resultText = convertStreamToString(instream);
//				  }
//	              finally {
//	            	  instream.close();
//	              }
//			  }
//
//		} catch (ClientProtocolException e) {
//			throw new RuntimeException(e);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//    	System.out.println("**************************"+resultText);
//    	return new JSONObject(resultText);
//
//    }
}
