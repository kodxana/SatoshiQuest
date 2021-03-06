package com.lbryquest.lbryquest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NodeWallet {
  private LBRYQuest lbryQuest;
  public String account_id;
  public String address;

  public NodeWallet(String _account_id) {
    this.account_id = _account_id;
	try {
	if (!LBRYQuest.REDIS.exists("nodeAddress"+account_id)) {
    		this.address = getNewAccountAddress();
		LBRYQuest.REDIS.set("nodeAddress"+account_id,this.address);
	} else {
	    	this.address = LBRYQuest.REDIS.get("nodeAddress"+account_id);
	}
	} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[address] error.");
		}
  }

  public String getAccountAddress() throws IOException, ParseException {

    JSONParser parser = new JSONParser();

    final JSONObject jsonObject = new JSONObject();
    jsonObject.put("jsonrpc", "1.0");
    jsonObject.put("id", "lbryquest");
    jsonObject.put("method", "getaddressesbylabel");
    JSONArray params = new JSONArray();
    params.add(account_id);
    if (LBRYQuest.LBRYQUEST_ENV == "development")
      System.out.println("[getaddressesbylabel] " + account_id);
    jsonObject.put("params", params);
    URL url = new URL("http://" + LBRYQuest.BITCOIN_NODE_HOST + ":" + LBRYQuest.BITCOIN_NODE_PORT + "/wallet/" + account_id);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    String userPassword = LBRYQuest.BITCOIN_NODE_USERNAME + ":" + LBRYQuest.BITCOIN_NODE_PASSWORD;
    String encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
    con.setRequestProperty("Authorization", "Basic " + encoding);
    con.setConnectTimeout(5000);
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    con.setDoOutput(true);
    OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
    out.write(jsonObject.toString());
    out.close();

    int responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    JSONObject response_object = (JSONObject) parser.parse(response.toString());
    if (LBRYQuest.LBRYQUEST_ENV == "development") System.out.println(response_object);
    return response_object.get("result").toString();
  }

  public String getNewAccountAddress() throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    final JSONObject jsonObject = new JSONObject();
    jsonObject.put("jsonrpc", "1.0");
    jsonObject.put("id", "lbryquest");
    jsonObject.put("method", "getnewaddress");
    JSONArray params = new JSONArray();
    params.add(account_id);
    params.add("legacy");
    if (LBRYQuest.LBRYQUEST_ENV == "development")
      System.out.println("[getnewaddress] " + account_id);
    jsonObject.put("params", params);
    URL url = new URL("http://" + LBRYQuest.BITCOIN_NODE_HOST + ":" + LBRYQuest.BITCOIN_NODE_PORT + "/wallet/" + account_id);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    String userPassword = LBRYQuest.BITCOIN_NODE_USERNAME + ":" + LBRYQuest.BITCOIN_NODE_PASSWORD;
    String encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
    con.setRequestProperty("Authorization", "Basic " + encoding);
    con.setConnectTimeout(5000);
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    con.setDoOutput(true);
    OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
    out.write(jsonObject.toString());
    out.close();

    int responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    JSONObject response_object = (JSONObject) parser.parse(response.toString());
    this.address = response_object.get("result").toString();
    if (LBRYQuest.LBRYQUEST_ENV == "development") System.out.println(response_object);
	this.address = response_object.get("result").toString();
    return response_object.get("result").toString();
  }

  public Long getBalance(int confirmations) throws IOException, org.json.simple.parser.ParseException {
	try {
        JSONParser parser = new JSONParser();

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "1.0");
        jsonObject.put("id", "lbryquest");
        jsonObject.put("method", "getbalance");
        JSONArray params = new JSONArray();
	params.add("*");
	params.add(confirmations);
        System.out.println("Parms: " + params);
        jsonObject.put("params", params);
        URL url = new URL("http://" + LBRYQuest.BITCOIN_NODE_HOST + ":" + LBRYQuest.BITCOIN_NODE_PORT + "/wallet/" + account_id);
        System.out.println(url.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String userPassword = LBRYQuest.BITCOIN_NODE_USERNAME + ":" + LBRYQuest.BITCOIN_NODE_PASSWORD;
        String encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
        con.setRequestProperty("Authorization", "Basic " + encoding);

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
        System.out.println(jsonObject.toString());
        out.write(jsonObject.toString());
        out.close();

        if(con.getResponseCode()==200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            JSONObject response_object = (JSONObject) parser.parse(response.toString());
	    Double d = Double.parseDouble(response_object.get("result").toString().trim()) * 100000000L;
	    final Long balance = d.longValue();
            System.out.println(balance);
	    return balance;

        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            JSONObject response_object = (JSONObject) parser.parse(response.toString());
	    Double d = Double.parseDouble(response_object.get("result").toString().trim()) * 100000000L;
	    final Long balance = d.longValue();
            System.out.println(balance);
	    return balance;
        }
	} catch(Exception e) {
		e.printStackTrace();
	}
	return 0L;
   }


}


/*
{
    System.out.println("getbalance: " + account_id);
    System.out.println("balanceaddress: " + address);
    try {
      JSONParser parser = new JSONParser();
      final JSONObject jsonObject = new JSONObject();
      jsonObject.put("jsonrpc", "1.0");
      jsonObject.put("id", "lbryquest");
      jsonObject.put("method", "getbalance");
      JSONArray params = new JSONArray();
      params.add("*");
      params.add(confirmations);
      //params.add(confirmations);
      jsonObject.put("params", params);
      URL url = new URL("http://" + LBRYQuest.BITCOIN_NODE_HOST + ":" + LBRYQuest.BITCOIN_NODE_PORT + "/wallet/" + account_id);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setConnectTimeout(5000);
      String userPassword = LBRYQuest.BITCOIN_NODE_USERNAME + ":" + LBRYQuest.BITCOIN_NODE_PASSWORD;
      String encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
      con.setRequestProperty("Authorization", "Basic " + encoding);

      con.setRequestMethod("POST");
      con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      con.setDoOutput(true);
      OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
      out.write(jsonObject.toString());
      out.close();

      int responseCode = con.getResponseCode();

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      JSONObject response_object = (JSONObject) parser.parse(response.toString());
      Double d = Double.parseDouble(response_object.get("result").toString().trim()) * 100000000L;

      final Long balance = d.longValue();
      return balance;
    } catch (Exception e) {
      System.out.println(e);
      return Long.valueOf(0);
    }
  }
*/
