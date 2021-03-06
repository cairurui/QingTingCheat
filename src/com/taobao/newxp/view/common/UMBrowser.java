package com.taobao.newxp.view.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.taobao.munion.base.caches.t;
import com.taobao.munion.base.volley.s;
import com.taobao.munion.view.base.BaseWebViewDialog;
import com.taobao.munion.view.webview.BaseWebView;
import com.taobao.munion.view.webview.windvane.WindVaneWebView;
import com.taobao.munion.view.webview.windvane.l;
import com.taobao.newxp.a.c;
import com.taobao.newxp.common.ExchangeConstants;
import com.taobao.newxp.common.a.a;
import com.taobao.newxp.common.a.a.f.a;
import com.taobao.newxp.common.a.a.o;
import com.taobao.newxp.common.b.e;
import com.taobao.newxp.controller.ExchangeDataService;
import com.taobao.newxp.net.h;
import com.taobao.newxp.net.q;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class UMBrowser extends BaseWebViewDialog
{
  private static final String j = UMBrowser.class.getName();
  String a;
  View b;
  public boolean beeplan = false;
  View c;
  View d;
  ProgressBar e;
  boolean f = false;
  boolean g = true;
  Map<String, String> h = new HashMap();
  Handler i;

  public UMBrowser(Context paramContext)
  {
    super(paramContext);
    this.h.put("Referer", "native null refer");
    this.i = new Handler();
  }

  private String a(String paramString)
  {
    if ((!paramString.contains("mmusdkwakeup=1")) || (!paramString.contains("?")));
    StringBuffer localStringBuffer;
    for (String str = paramString + "?mmusdkwakeup=1"; ; str = localStringBuffer.toString())
    {
      return a(str, "mmusdkwakeup");
      localStringBuffer = new StringBuffer(paramString);
      localStringBuffer.insert(1 + localStringBuffer.indexOf("?"), "mmusdkwakeup=1&");
    }
  }

  private static String a(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString1.contains(paramString2)));
    while (true)
    {
      int m;
      String str3;
      try
      {
        URL localURL = new URL(paramString1);
        String str1 = localURL.getQuery();
        if ((str1 != null) && (str1.trim().length() > 0))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          String[] arrayOfString = str1.split("&");
          if ((arrayOfString != null) && (arrayOfString.length > 1))
          {
            int k = arrayOfString.length;
            m = 0;
            String str2 = "";
            if (m < k)
            {
              str3 = arrayOfString[m];
              if (!str3.contains("="))
                break label245;
              str4 = str3.substring(0, str3.indexOf("="));
              if ((str3 != null) && (!paramString2.equals(str4)))
              {
                localStringBuilder.append(str3);
                localStringBuilder.append("&");
                break label239;
              }
              if (str3 == null)
                break label239;
              str2 = str3.trim();
              break label239;
            }
            localStringBuilder.append(str2);
            String str5 = localURL.getProtocol() + "://" + localURL.getAuthority() + localURL.getPath() + "?" + localStringBuilder.toString();
            paramString1 = str5;
          }
        }
        return paramString1;
      }
      catch (MalformedURLException localMalformedURLException)
      {
        return paramString1;
      }
      label239: m++;
      continue;
      label245: String str4 = str3;
    }
  }

  private String b(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      paramString = paramString.replace("&mmusdkwakeup=1", "").replace("mmusdkwakeup=1", "");
    return paramString;
  }

  public void dismiss()
  {
    super.dismiss();
    if (com.taobao.munion.base.caches.b.j)
    {
      f.a locala = new f.a(4, System.currentTimeMillis());
      a.a().a(locala);
      o localo = new o(2, 8);
      com.taobao.newxp.common.a.b.a().a(getContext(), localo);
    }
    t.b().a();
  }

  public void initContent()
  {
    int k = com.taobao.newxp.a.d.c(this.mContext);
    ViewGroup localViewGroup = (ViewGroup)LayoutInflater.from(this.mContext).inflate(k, null);
    getWindow().setContentView(localViewGroup, new ViewGroup.LayoutParams(-1, -1));
    this.mWebView = ((BaseWebView)findViewById(c.L(this.mContext)));
    this.b = findViewById(c.a(this.mContext));
    if (this.b != null)
    {
      if (!this.g)
        break label225;
      this.b.setVisibility(0);
    }
    while (true)
    {
      this.c = findViewById(c.b(this.mContext));
      this.d = findViewById(c.c(this.mContext));
      View localView1 = findViewById(c.d(this.mContext));
      View localView2 = findViewById(c.e(this.mContext));
      this.c.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if (UMBrowser.this.mWebView.canGoBack())
          {
            UMBrowser.this.mWebView.goBack();
            return;
          }
          Toast.makeText(UMBrowser.this.mContext, "已经是第一页了，亲～", 0).show();
        }
      });
      this.d.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if (UMBrowser.this.mWebView.canGoForward())
          {
            UMBrowser.this.mWebView.goForward();
            return;
          }
          Toast.makeText(UMBrowser.this.mContext, "已经是最后一页了，亲～", 0).show();
        }
      });
      localView1.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          UMBrowser.this.mWebView.reload();
        }
      });
      localView2.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          UMBrowser.this.cancel();
        }
      });
      this.e = ((ProgressBar)findViewById(c.C(this.mContext)));
      return;
      label225: this.b.setVisibility(8);
    }
  }

  public void initWebview(WebView paramWebView)
  {
    super.initWebview(paramWebView);
    paramWebView.setWebChromeClient(new l((WindVaneWebView)paramWebView)
    {
      public void onProgressChanged(WebView paramAnonymousWebView, int paramAnonymousInt)
      {
        com.taobao.newxp.common.Log.a(ExchangeConstants.LOG_TAG, "Webview loading progress: " + paramAnonymousInt);
        if (UMBrowser.this.e != null)
        {
          UMBrowser.this.e.setProgress(paramAnonymousInt);
          if (paramAnonymousInt > 90)
            UMBrowser.this.e.setVisibility(4);
        }
      }
    });
    paramWebView.setWebViewClient(new com.taobao.munion.view.webview.windvane.m(1)
    {
      public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
      {
        super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
        if ((UMBrowser.this.beeplan) && (UMBrowser.this.a.equals(UMBrowser.b(UMBrowser.this, paramAnonymousString))))
          ExchangeDataService.getVerInfo().d();
        if ((!TextUtils.isEmpty(com.taobao.munion.base.caches.b.d)) && (paramAnonymousString.equals(com.taobao.munion.base.caches.b.d)))
        {
          f.a locala = new f.a(3, System.currentTimeMillis());
          a.a().a(locala);
        }
        if ((UMBrowser.this.c != null) && (UMBrowser.this.d != null))
        {
          if (!paramAnonymousWebView.canGoBack())
            break label178;
          if (Build.VERSION.SDK_INT >= 11)
            UMBrowser.this.c.setAlpha(1.0F);
          UMBrowser.this.c.setClickable(true);
        }
        while (paramAnonymousWebView.canGoForward())
        {
          if (Build.VERSION.SDK_INT >= 11)
            UMBrowser.this.d.setAlpha(1.0F);
          UMBrowser.this.d.setClickable(true);
          return;
          label178: if (Build.VERSION.SDK_INT >= 11)
            UMBrowser.this.c.setAlpha(0.3F);
          UMBrowser.this.c.setClickable(false);
        }
        if (Build.VERSION.SDK_INT >= 11)
          UMBrowser.this.d.setAlpha(0.3F);
        UMBrowser.this.d.setClickable(false);
      }

      public void onPageStarted(WebView paramAnonymousWebView, String paramAnonymousString, Bitmap paramAnonymousBitmap)
      {
        super.onPageStarted(paramAnonymousWebView, paramAnonymousString, paramAnonymousBitmap);
        this.b = UMBrowser.b(UMBrowser.this, this.b);
        if (this.b.equals(com.taobao.munion.base.caches.b.d))
        {
          f.a locala = new f.a(1, System.currentTimeMillis());
          a.a().a(locala);
        }
      }

      public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
      {
        try
        {
          Uri localUri = Uri.parse(paramAnonymousString2);
          String str = localUri.getScheme();
          if ((!str.equals("http")) && (!str.equals("https")))
          {
            android.util.Log.i(UMBrowser.a(), "start url with intent " + paramAnonymousString2);
            Intent localIntent = new Intent();
            localIntent.setData(localUri);
            UMBrowser.this.getContext().startActivity(localIntent);
            if (!UMBrowser.this.mWebView.canGoBack())
              UMBrowser.this.dismiss();
          }
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }

      public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
      {
        try
        {
          Uri localUri = Uri.parse(paramAnonymousString);
          String str = localUri.getScheme();
          if ((!TextUtils.isEmpty(str)) && (!str.equals("http")) && (!str.equals("https")))
          {
            Intent localIntent = new Intent();
            localIntent.setData(localUri);
            UMBrowser.this.mContext.startActivity(localIntent);
            return true;
          }
          if (UMBrowser.this.beeplan)
          {
            paramAnonymousWebView.loadUrl(UMBrowser.a(UMBrowser.this, paramAnonymousString));
            return true;
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          return true;
        }
        return super.shouldOverrideUrlLoading(paramAnonymousWebView, paramAnonymousString);
      }
    });
    paramWebView.setDownloadListener(new DownloadListener()
    {
      public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong)
      {
        if (!TextUtils.isEmpty(paramAnonymousString1))
        {
          com.taobao.newxp.common.Log.a(UMBrowser.a(), "start downlaod url " + paramAnonymousString1);
          new h(UMBrowser.this.mContext, paramAnonymousString1).a();
        }
      }
    });
  }

  public void loadAndShow(String paramString)
  {
    if (paramString.contains("simba.taobao.com"))
    {
      new com.taobao.newxp.net.m().a(paramString, "null", new q()
      {
        public void a(s paramAnonymouss)
        {
        }

        public void a(String paramAnonymousString)
        {
          UMBrowser.this.a = paramAnonymousString;
          UMBrowser.this.i.post(new Runnable()
          {
            public void run()
            {
              UMBrowser.this.show();
            }
          });
        }

        public void a(JSONObject paramAnonymousJSONObject)
        {
        }
      });
      return;
    }
    this.a = paramString;
    show();
  }

  public void onBackPressed()
  {
    if ((this.f) && (this.mWebView.canGoBack()))
    {
      this.mWebView.goBack();
      return;
    }
    super.onBackPressed();
  }

  public void onLoadUrl()
  {
    e.a(this.mContext);
    if (this.e != null)
      this.e.setVisibility(0);
    if (!TextUtils.isEmpty(this.a))
    {
      if ((Build.VERSION.SDK_INT < 8) || (this.h == null))
        break label92;
      this.mWebView.loadUrl(this.a, this.h);
    }
    while (true)
    {
      com.taobao.newxp.common.Log.a(j, "load url: " + this.a);
      return;
      label92: this.mWebView.loadUrl(this.a);
    }
  }

  public UMBrowser setExtraHeaders(Map<String, String> paramMap)
  {
    this.h.putAll(paramMap);
    return this;
  }

  public UMBrowser setInterceptBack(boolean paramBoolean)
  {
    this.f = paramBoolean;
    return this;
  }

  public UMBrowser showActionBar(boolean paramBoolean)
  {
    this.g = paramBoolean;
    return this;
  }
}

/* Location:           /Users/zhangxun-xy/Downloads/qingting2/classes_dex2jar.jar
 * Qualified Name:     com.taobao.newxp.view.common.UMBrowser
 * JD-Core Version:    0.6.2
 */