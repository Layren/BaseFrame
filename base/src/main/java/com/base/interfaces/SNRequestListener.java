package com.base.interfaces;


public interface SNRequestListener {

    void onComplete(String response, int whichAPI);

    void onError(Exception e, int whichAPI);

}
