/*
 * The MIT License
 *
 * Copyright 2022 Meire de Jesus Torres.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package HighService.data;

import java.sql.*;

/**
 * Connecting dataBase MySQL
 * @author Meire de Jesus Torres
 * 
 */
public class Dataconnect 
{

 /**
  * this method connect database
  * in this case i am using MySql as my database
  * @return cone
  */
    public static Connection conector()
    {
        Connection cone = null;
        //this method call the driver in the API JDBC
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/highservice?characterEncoding=utf-8";
        String user = "hs";
        String password = "hs220822@";
        //this syntax if to connect and if something brake internet
        //connecting Database
        
        try 
        {
            Class.forName(driver);
            cone = DriverManager.getConnection(url, user, password);
            return cone;
        }
        catch (Exception e)//iff doesn't find any connection will return null
        {//it shows the error
            System.out.println(e);
            return null;
        }
        
    } 
    
}
