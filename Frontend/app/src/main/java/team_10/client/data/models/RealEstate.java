package team_10.client.data.models;

import java.time.LocalDate;

import team_10.client.data.UserInputField;

/**
 * Real Estate account type.
 */
public class RealEstate extends Account {
    @UserInputField(
            priority = 1,
            name = "Address",
            inputType = String.class
    )
    public String address;

    @UserInputField(
            priority = 2,
            name = "City",
            inputType = String.class
    )
    public String city;

    @UserInputField(
            priority = 3,
            name = "State",
            inputType = String.class
    )
    public String state;

    public RealEstate(String address, String city, String state) {
        this.address = address;
        this.city = city;
        this.state = state;
    }

    /**
     * Overloaded add transaction to account.
     * @param d date
     * @param value value
     */
    public void addTransaction(LocalDate d, double value)
    {
        addTransaction(d, value, 0, transactions.size());
    }

    public void addTransaction(LocalDate d, double value, int recurring, int transactionId) {
        Transaction t = new Transaction(value, transactionId, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

    /**
     * Overloaded getValue method from account.
     * @param d date
     * @return double value
     */
    public double getValue(LocalDate d) {
//        Vector<LocalDate> transaction_dates = new Vector<LocalDate>(transactions.keySet());
//
//        double total = 0;
//        final double[] stockValue = new double[1];
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
//                String urlStock = ROOT_URL + address + city + state;
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlStock,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    JSONObject returned = new JSONObject(response);
//                                    try {
//                                        //JSONObject userJson = returned.getJSONObject("value");
//                                        stockValue[0] = returned.getDouble("value");
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
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
//                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
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

        Transaction() { }

        Transaction(double value, int transactionID, int recurring, LocalDate date) {
            this.value = value;
            this.transactionID = transactionID;
            this.recurring = recurring;
            this.date = date;
        }
    }
}
