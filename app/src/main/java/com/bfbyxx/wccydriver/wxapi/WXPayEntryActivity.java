package com.bfbyxx.wccydriver.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bfbyxx.wccydriver.pay.Constants;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.view.activity.WalletChargeResultActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 微信支付结果回调Activity
 * 这个Activity是无界面的,接受到回调后,立即finish,然后转到统一的支付结果界面去展示
 * 支付结果
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
    }

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch(resp.errCode){
				case 0:  //支付成功
					jumpToChargeResult(true);
					break;
				case -1: //错误
					jumpToChargeResult(false);
					break;
				case -2:  //取消支付
					ProjectUtil.show(this, "取消支付");
					break;
			}
			finish();
		}
	}

	/**
	 * 跳转至充值结果界面
	 */
	private void jumpToChargeResult(boolean isSuccess){
		Intent it = new Intent(this, WalletChargeResultActivity.class);
		it.putExtra("isSuccess", isSuccess);
//		it.putExtra("RemainingSum", "1000");
		startActivity(it);
	}
}