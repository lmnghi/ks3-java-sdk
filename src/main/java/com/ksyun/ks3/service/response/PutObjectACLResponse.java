package com.ksyun.ks3.service.response;

/**
 * Created by 杨春建 on 2014/10/20.
 */
public class PutObjectACLResponse extends Ks3WebServiceDefaultResponse<Boolean> {

    public int[] expectedStatus() {
        return new int[]{200};
    }

    @Override
    public void preHandle() {

    }
}
