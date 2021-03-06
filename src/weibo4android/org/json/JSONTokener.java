package weibo4android.org.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener
{
  private int index;
  private char lastChar;
  private Reader reader;
  private boolean useLastChar;

  public JSONTokener(Reader paramReader)
  {
    if (paramReader.markSupported());
    while (true)
    {
      this.reader = paramReader;
      this.useLastChar = false;
      this.index = 0;
      return;
      paramReader = new BufferedReader(paramReader);
    }
  }

  public JSONTokener(String paramString)
  {
    this(new StringReader(paramString));
  }

  public static int dehexchar(char paramChar)
  {
    if ((paramChar >= '0') && (paramChar <= '9'))
      return paramChar - '0';
    if ((paramChar >= 'A') && (paramChar <= 'F'))
      return paramChar - '7';
    if ((paramChar >= 'a') && (paramChar <= 'f'))
      return paramChar - 'W';
    return -1;
  }

  public void back()
    throws JSONException
  {
    if ((this.useLastChar) || (this.index <= 0))
      throw new JSONException("Stepping back two steps is not supported");
    this.index = (-1 + this.index);
    this.useLastChar = true;
  }

  public boolean more()
    throws JSONException
  {
    if (next() == 0)
      return false;
    back();
    return true;
  }

  public char next()
    throws JSONException
  {
    if (this.useLastChar)
    {
      this.useLastChar = false;
      if (this.lastChar != 0)
        this.index = (1 + this.index);
      return this.lastChar;
    }
    int i;
    try
    {
      i = this.reader.read();
      if (i <= 0)
      {
        this.lastChar = '\000';
        return '\000';
      }
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
    this.index = (1 + this.index);
    this.lastChar = ((char)i);
    return this.lastChar;
  }

  public char next(char paramChar)
    throws JSONException
  {
    char c = next();
    if (c != paramChar)
      throw syntaxError("Expected '" + paramChar + "' and instead saw '" + c + "'");
    return c;
  }

  public String next(int paramInt)
    throws JSONException
  {
    if (paramInt == 0)
      return "";
    char[] arrayOfChar = new char[paramInt];
    boolean bool = this.useLastChar;
    int i = 0;
    if (bool)
    {
      this.useLastChar = false;
      arrayOfChar[0] = this.lastChar;
      i = 1;
    }
    while (i < paramInt)
      try
      {
        int j = this.reader.read(arrayOfChar, i, paramInt - i);
        if (j != -1)
          i += j;
      }
      catch (IOException localIOException)
      {
        throw new JSONException(localIOException);
      }
    this.index = (i + this.index);
    if (i < paramInt)
      throw syntaxError("Substring bounds error");
    this.lastChar = arrayOfChar[(paramInt - 1)];
    return new String(arrayOfChar);
  }

  public char nextClean()
    throws JSONException
  {
    char c;
    do
      c = next();
    while ((c != 0) && (c <= ' '));
    return c;
  }

  public String nextString(char paramChar)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while (true)
    {
      char c1 = next();
      switch (c1)
      {
      default:
        if (c1 == paramChar)
          return localStringBuffer.toString();
        break;
      case '\000':
      case '\n':
      case '\r':
        throw syntaxError("Unterminated string");
      case '\\':
        char c2 = next();
        switch (c2)
        {
        default:
          localStringBuffer.append(c2);
          break;
        case 'b':
          localStringBuffer.append('\b');
          break;
        case 't':
          localStringBuffer.append('\t');
          break;
        case 'n':
          localStringBuffer.append('\n');
          break;
        case 'f':
          localStringBuffer.append('\f');
          break;
        case 'r':
          localStringBuffer.append('\r');
          break;
        case 'u':
          localStringBuffer.append((char)Integer.parseInt(next(4), 16));
          break;
        case 'x':
          localStringBuffer.append((char)Integer.parseInt(next(2), 16));
        }
        break;
      }
      localStringBuffer.append(c1);
    }
  }

  public String nextTo(char paramChar)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while (true)
    {
      char c = next();
      if ((c == paramChar) || (c == 0) || (c == '\n') || (c == '\r'))
      {
        if (c != 0)
          back();
        return localStringBuffer.toString().trim();
      }
      localStringBuffer.append(c);
    }
  }

  public String nextTo(String paramString)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while (true)
    {
      char c = next();
      if ((paramString.indexOf(c) >= 0) || (c == 0) || (c == '\n') || (c == '\r'))
      {
        if (c != 0)
          back();
        return localStringBuffer.toString().trim();
      }
      localStringBuffer.append(c);
    }
  }

  public Object nextValue()
    throws JSONException
  {
    char c = nextClean();
    StringBuffer localStringBuffer;
    switch (c)
    {
    default:
      localStringBuffer = new StringBuffer();
    case '"':
    case '\'':
    case '{':
    case '(':
    case '[':
    }
    while ((c >= ' ') && (",:]}/\\\"[{;=#".indexOf(c) < 0))
    {
      localStringBuffer.append(c);
      c = next();
      continue;
      return nextString(c);
      back();
      return new JSONObject(this);
      back();
      return new JSONArray(this);
    }
    back();
    String str = localStringBuffer.toString().trim();
    if (str.equals(""))
      throw syntaxError("Missing value");
    return JSONObject.stringToValue(str);
  }

  public char skipTo(char paramChar)
    throws JSONException
  {
    try
    {
      int i = this.index;
      this.reader.mark(2147483647);
      char c;
      do
      {
        c = next();
        if (c == 0)
        {
          this.reader.reset();
          this.index = i;
          return c;
        }
      }
      while (c != paramChar);
      back();
      return c;
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
  }

  public JSONException syntaxError(String paramString)
  {
    return new JSONException(paramString + toString());
  }

  public String toString()
  {
    return " at character " + this.index;
  }
}

/* Location:           /Users/zhangxun-xy/Downloads/qingting2/classes_dex2jar.jar
 * Qualified Name:     weibo4android.org.json.JSONTokener
 * JD-Core Version:    0.6.2
 */