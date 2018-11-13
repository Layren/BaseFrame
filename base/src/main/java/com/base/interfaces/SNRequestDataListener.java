package com.base.interfaces;

import com.base.model.Base;


public interface SNRequestDataListener {

    /**
     * api 访问正常回调函数
     *
     * @param whichAPI api常量
     */
    void onCompleteData(Base base, int whichAPI);

    void onError(Exception e, int whichAPI);

}
