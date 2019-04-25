package team_10.client.constant;

import team_10.client.data.source.local.SharedPreferencesManager;

public class URL {
    public static final String ROOT_URL = "http://cs309-jr-1.misc.iastate.edu:8080/"; //http://cs309-jr-1.misc.iastate.edu:8080/login //http://10.26.3.234/test/Api.php?apicall=

    public static final String URL_REGISTER = ROOT_URL + "register";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_ADD_ACCOUNTS = ROOT_URL + "accounts/add";
    public static final String URL_GET_ACCOUNTS = ROOT_URL + "accounts/get/all";
    public static final String URL_GET_ARTICLES = ROOT_URL + "article/getPersonal/" + SharedPreferencesManager.getUser().getID();
    public static final String URL_GET_API = ROOT_URL + "accounts/fetch";
}
