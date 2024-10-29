package com.penaestrada.config;

final class DatabaseConfig {

    private DatabaseConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static String getUrl(){
        return "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
    }
    static String getUser(){
      return "rm554489";
    }
    static String getPass(){
      return "280606";
    }


}




