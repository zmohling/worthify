package team_10.client.data.models;

import java.time.LocalDate;

import team_10.client.data.UserInputField;

public class Stock extends Account {

    @UserInputField(
            priority = 1,
            name = "Ticker",
            inputType = String.class
    )
    public String ticker;

    public Stock() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String symbol) {
        this.ticker = symbol;
    }

    public void addTransaction(LocalDate d, double value, int amount) {
        addTransaction(d, amount, 0, value, transactions.size());
    }

    public void addTransaction(LocalDate d, int amount, int recurring, double value, int transactionId) {

        Transaction t = new Transaction(value, amount, transactionId, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

    public double getValue(LocalDate d) {
//        Vector<LocalDate> transaction_dates = new Vector<LocalDate>(transactions.keySet());
//        valueOfStock = 0;
//
//        if (transaction_dates.size() <= 0) {
//            throw new IllegalStateException("No transactions for this account.");
//        } else {
//            for (int i = 0; i < transaction_dates.size(); i++) {
//
//                LocalDate fromDate = transaction_dates.get(i);
//                LocalDate toDate;
//
//                if ((i + 1) >= transaction_dates.size() || transaction_dates.get(i + 1).isAfter(d)) {
//                    toDate = d;
//                    i = Integer.MAX_VALUE - 1; // Stop calculating if date d is before last transaction
//                } else {
//                    toDate = transaction_dates.get(i + 1);
//                }
//                /*
//                String urlStock = ROOT_URL + symbol;
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlStock,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    JSONObject returned = new JSONObject(response);
//                                        try {
//                                            //JSONObject userJson = returned.getJSONObject("value");
//                                            stockValue[0] = returned.getDouble("value");
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                System.out.println(error.getMessage());
//                            }
//                        });
//                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);*/
//
//                String requestBody = null;
//                try {
//                    JsonObject userAccountsRequest = new JsonObject();
//                    JsonArray accountIdArray = new JsonArray();
//                    accountIdArray.add(accountID);
//                    userAccountsRequest.add("accountID", accountIdArray);
//                    requestBody = userAccountsRequest.toString();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                final String finalRequestBody = requestBody;
//                /*StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_GET_API,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    JSONObject returned = new JSONObject(response);
//
//                                    if (returned.has(accountID)) {
//                                        valueOfStock = returned.getDouble(accountID);
//                                        System.out.println("Accounts Retrieval Successful");
//                                    }  else {
//                                        System.out.println("Accounts Retrieval Unsuccessful");
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    // gson catch exceptions
//                                }
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(context,
//                                        (error.getMessage() == null) ? "Error Syncing with Server" : "Error Syncing with Server: " + error.getMessage(),
//                                        Toast.LENGTH_SHORT).show();
//                                System.out.println(error.toString());
//                                error.printStackTrace();
//                            }
//                        }) {
//
//                    @Override
//                    public String getBodyContentType() {
//                        return String.format("text/plain;charset=UTF-8");
//                    }
//
//                    @Override
//                    public byte[] getBody() throws AuthFailureError {
//                        try {
//                            return finalRequestBody == null ? null : finalRequestBody.getBytes("utf-8");
//                        } catch (UnsupportedEncodingException uee) {
//                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
//                                    finalRequestBody, "utf-8");
//                            return null;
//                        }
//                    }
//
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String>  params = new HashMap<String, String>();
//                        final String basicAuth = User.getToken();
//                        params.put("Authorization", basicAuth);
//
//                        return params;
//                    }
//
//
//                };*/
//
//                //RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
//                //requestQueue.add(stringRequest);
//                //requestQueue.start();
//
//                RequestFuture<String> future = RequestFuture.newFuture();
//                JSONObject userAccountsRequest = new JSONObject();
//                JSONArray accountIdArray = new JSONArray();
//                accountIdArray.put(accountID);
//                try {
//                    userAccountsRequest.put("accountID", accountIdArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                StringRequest request = new StringRequest(Request.Method.POST, URL.URL_GET_API, future, future){
//                    @Override
//                    public String getBodyContentType() {
//                        return String.format("text/plain;charset=UTF-8");
//                    }
//
//                    @Override
//                    public byte[] getBody() {
//                        try {
//                            return finalRequestBody == null ? null : finalRequestBody.getBytes("utf-8");
//                        } catch (UnsupportedEncodingException uee) {
//                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
//                                    finalRequestBody, "utf-8");
//                            return null;
//                        }
//                    }
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        final String basicAuth = User.getToken();
//                        params.put("Authorization", basicAuth);
//
//                        return params;
//                    }
//                };
//                requestQueue.add(request);
//
//
//
//                try {
//                    String returned = future.get(10000, TimeUnit.MILLISECONDS); // this will block
//                    try {
//                        JSONObject response = new JSONObject(returned);
//                        if (response.has(accountID)) {
//                            valueOfStock = response.getDouble(accountID);
//                            System.out.println("Accounts Retrieval Successful");
//                        }  else {
//                            System.out.println("Accounts Retrieval Unsuccessful");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        // gson catch exceptions
//                    }
//                } catch (InterruptedException e) {
//                    // exception handling
//                } catch (ExecutionException e) {
//                    // exception handling
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                }
//                //waitForResponse(); */
//            }
//        }
        return 0; // round to nearest cent
    }


    public team_10.client.data.models.Transaction getTransaction(LocalDate date) {
        if (date != null && transactions.containsKey(date)) {
            return transactions.get(date);
        } else {
            return new Transaction();
        }
    }




    private class Transaction extends team_10.client.data.models.Transaction {

        @UserInputField(
                priority = 0,
                name = "Amount",
                inputType = Number.class
        )
        public int amount;

        Transaction() { }

        Transaction(double value, int amount, int transactionID, int recurring, LocalDate date) {
            this.value = value;
            this.amount = amount;
            this.transactionID = transactionID;
            this.recurring = recurring;
            this.date = date;
        }
    }
}
