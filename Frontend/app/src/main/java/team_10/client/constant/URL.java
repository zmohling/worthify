package team_10.client.constant;

import team_10.client.utility.io.SharedPreferencesManager;

/**
 * Class that keeps track of urls used in http requests.
 */
public class URL {
    public static final String ROOT_URL = "http://cs309-jr-1.misc.iastate.edu:8080/";
    public static final String HOSTNAME = "cs309-jr-1.misc.iastate.edu";

    public static final String URL_REGISTER = ROOT_URL + "register";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_ADD_ACCOUNTS = ROOT_URL + "accounts/add";
    public static final String URL_GET_ACCOUNTS = ROOT_URL + "accounts/get/all";
    public static final String URL_GET_ARTICLES = ROOT_URL + "article/getPersonal/" + SharedPreferencesManager.getUser().getID();
    public static final String URL_GET_API = ROOT_URL + "accounts/fetch";
    public static final String URL_EDIT_EMAIL = ROOT_URL + "emailChange";
    public static final String URL_EDIT_PASSWORD = ROOT_URL + "passwordChange";
}
