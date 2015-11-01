package com.example.pulsometer.Logic.AsyncTasks;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Model.ExternalProviderViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

/**
 * Created by Szymon Wójcik on 25.10.2015.
 */
public class ExternalUserLoginTask extends AsyncTask<Void, Void, HttpResponse> {

    private String provider;
    private WebView webView;
    public ExternalUserLoginTask(String provider, WebView webView){
        this.provider = provider;
        this.webView = webView;
    }
    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/Account/ExternalLogins?returnUrl=%2F&generateState=true";
            HttpGet getProviders = new HttpGet(url);
            HttpResponse responseProviders = client.execute(getProviders);
            return responseProviders;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        try {
            HttpEntity entity = result.getEntity();
            InputStream in = entity.getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer, "UTF-8");
            Gson g = new Gson();
            List<ExternalProviderViewModel> externalProviderViewModels = g.fromJson(writer.toString(),
                    new TypeToken<Collection<ExternalProviderViewModel>>(){}.getType());
            for (ExternalProviderViewModel externalProviderViewModel : externalProviderViewModels) {
                if (webView != null && externalProviderViewModel.Name.equals(provider)) {
                    webView.loadUrl(GlobalVariables.BaseUrlForRest.substring(0, GlobalVariables.BaseUrlForRest.length() - 1)
                            + externalProviderViewModel.Url);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
